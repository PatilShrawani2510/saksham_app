package com.example.saksham;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.saksham.R;
import com.google.gson.Gson;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MainActivity4 extends AppCompatActivity {


    private static final int PICK_IMAGE_REQUEST_CODE = 1;


    private Button uploadImageButton;
    private GridLayout imageGridLayout;
    private Gson gson;
    private List<ImageEntry> imageEntries;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_grid);


        uploadImageButton = findViewById(R.id.uploadImageButton);
        imageGridLayout = findViewById(R.id.imageGridLayout);
        gson = new Gson();
        imageEntries = new ArrayList<>();

        uploadImageButton.setOnClickListener(v -> openImageGallery());
        loadSavedImages(); // loading the image

    }


    /**
     * Opens the device's image gallery for the user to pick an image.
     */
    private void openImageGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT); // 👈 Use OPEN_DOCUMENT for long-term access
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            try {
                // ⬇️ This is critical: take permission
                final int takeFlags = data.getFlags()
                        & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                getContentResolver().takePersistableUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

                // Your logic to show image
                addImageViewToGrid(imageUri);
                //saveImageUri(imageUri.toString()); // Save string URI
            } catch (SecurityException se) {
                se.printStackTrace();
                Toast.makeText(this, "Permission issue for image access", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




    /**
     * Creates an ImageView from the given URI and adds it to the GridLayout.
     *
     * @param imageUri The URI of the image to display.
     * @throws IOException if there's an error loading the image.
     */
    private void addImageViewToGrid(Uri imageUri) throws IOException {
        ImageView imageView = new ImageView(this);


        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        int columns = imageGridLayout.getColumnCount();
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        params.width = screenWidth / columns - 20;
        params.height = params.width;
        params.setMargins(10, 10, 10, 10);
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);


        // Use InputStream instead of deprecated getBitmap()
        InputStream inputStream = getContentResolver().openInputStream(imageUri);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        inputStream.close();
        imageView.setImageBitmap(bitmap);

        // clickable button
        imageView.setOnClickListener(v -> {
            openFullscreenDialog(imageUri);
        });


        imageGridLayout.addView(imageView);
        saveImageUri(imageUri.toString());   // method calling for the image to be saved

        Toast.makeText(this, "Image added to grid!", Toast.LENGTH_SHORT).show();
    }

    // to make image persistant , I have created this method .
    private void saveImageUri(String uriString) {
        SharedPreferences prefs = getSharedPreferences("ImagePrefs", MODE_PRIVATE);
        String existing = prefs.getString("image_uris", "");

        if (!existing.contains(uriString)) {
            existing += uriString + ";"; // Separate with ;
            Log.d("SaveURI","Saved"+uriString);   // new
            prefs.edit().putString("image_uris", existing).apply();
        }
    }

    // to load saved images . this is also new
    private void loadSavedImages() {
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String uriStringCombined = prefs.getString("image_uris", "");

        if (!uriStringCombined.isEmpty()) {
            String[] uriStrings = uriStringCombined.split(";");
            for (String uriString : uriStrings) {
                try {
                    Uri uri = Uri.parse(uriString);
                    addImageViewToGrid(uri); // only if permission is valid
                    Log.d("LoadURI","Loaded" + uriString);
                } catch (Exception e) {
                    e.printStackTrace(); // prevent crash if URI is no longer valid
                }
            }
        }
    }
    private void openFullscreenDialog(Uri imageUri) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_fullscreen_image);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false); // Don't allow manual closing

        ImageView fullImageView = dialog.findViewById(R.id.fullscreenImageView);
        TextView imageNameText = dialog.findViewById(R.id.imageNameTextView);

        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
            fullImageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Set image name below (last segment of URI as "name")
        String imageName = imageUri.getLastPathSegment();
        imageNameText.setText(imageName != null ? imageName : "Image");

        // Animation: big-small-big-small-big-small
        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(fullImageView, "scaleX", 1f, 1.2f);
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(fullImageView, "scaleY", 1f, 1.2f);
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(fullImageView, "scaleX", 1.2f, 1f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(fullImageView, "scaleY", 1.2f, 1f);

        AnimatorSet popAnimator = new AnimatorSet();
        popAnimator.playSequentially(
                scaleUpX.setDuration(200),
                scaleDownX.setDuration(200),
                scaleUpY.setDuration(200),
                scaleDownY.setDuration(200),
                scaleUpX.clone().setDuration(200),
                scaleDownX.clone().setDuration(200)
        );
        popAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                dialog.dismiss(); // Close the dialog after the animation
            }
        });
        popAnimator.start();


        dialog.show();
    }


}
