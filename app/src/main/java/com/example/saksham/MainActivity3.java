package com.example.saksham;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.text.DateFormat;
import java.util.Date;

public class MainActivity3 extends AppCompatActivity implements EmojiAdapter.OnEmojiClickListener {

    private static final String TAG = "SakshamEmojiApp";
    private List<Emoji> emojis;
    private RecyclerView emojiRecyclerView;
    private EmojiAdapter emojiAdapter;

    // Parent lock variables
    private Handler timerHandler = new Handler();
    private long dailyTimeLimitMillis;
    private long remainingTimeMillis;
    private long startTime;
    private boolean isLocked = false;
    private long warningTimeMillis;

    // UI elements for locking the app.
    private ImageView lockOverlay;
    private ConstraintLayout mainLayout;

    private final String PREFS_NAME = "ParentLockPrefs";
    private final String KEY_REMAINING_TIME = "remainingTime";
    private final String KEY_LAST_USED_DATE = "lastUsedDate";
    private final String KEY_DAILY_LIMIT = "dailyTimeLimitMillis";
    private final String KEY_WARNING_TIME = "warningTimeMillis";

    private final Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long elapsedTime = System.currentTimeMillis() - startTime;
            remainingTimeMillis -= elapsedTime;
            startTime = System.currentTimeMillis();

            if (remainingTimeMillis <= 0) {
                remainingTimeMillis = 0;
                lockApp();
            } else {
                if (remainingTimeMillis < warningTimeMillis) {
                    float ratio = (float) remainingTimeMillis / warningTimeMillis;
                    applyDesaturationFilter(ratio);
                }
            }
            saveRemainingTime();
            timerHandler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        Log.d(TAG, "MainActivity3 onCreate called.");

        // Load parent lock settings
        loadParentLockSettings();
        loadRemainingTime();

        // Initialize UI components for the lock
        lockOverlay = findViewById(R.id.lockOverlay);
        mainLayout = findViewById(R.id.mainLayout);

        // Prepare emoji data
        emojis = new ArrayList<>();
        emojis.add(new Emoji(R.drawable.happy, "Happy"));
        emojis.add(new Emoji(R.drawable.sad, "Sad"));
        emojis.add(new Emoji(R.drawable.apple, "Apple"));
        emojis.add(new Emoji(R.drawable.teeth, "Teeth"));
        emojis.add(new Emoji(R.drawable.sick, "Feeling sick"));
        emojis.add(new Emoji(R.drawable.toothbrush, "Tooth Brush"));
        emojis.add(new Emoji(R.drawable.soap, "Bath Soap"));
        emojis.add(new Emoji(R.drawable.toilet, "Toilet"));
        emojis.add(new Emoji(R.drawable.sleeping_bed, "Bed"));
        emojis.add(new Emoji(R.drawable.medicines, "Medicines"));
        emojis.add(new Emoji(R.drawable.bath, "Taking Bath"));
        emojis.add(new Emoji(R.drawable.bandage, "Bandage"));
        emojis.add(new Emoji(R.drawable.cake, "Cake"));
        emojis.add(new Emoji(R.drawable.donut, "Donut"));
        emojis.add(new Emoji(R.drawable.eyes, "Eyes"));
        emojis.add(new Emoji(R.drawable.angry, "angry"));
        emojis.add(new Emoji(R.drawable.crabs, "Crabs"));
        emojis.add(new Emoji(R.drawable.strawberry, "Strawberry"));
        emojis.add(new Emoji(R.drawable.plates, "Plates"));
        emojis.add(new Emoji(R.drawable.dog, "Dog"));
        emojis.add(new Emoji(R.drawable.elephant, "Elephant"));
        emojis.add(new Emoji(R.drawable.butterfly, "Butterfly"));
        emojis.add(new Emoji(R.drawable.swan, "Swan"));
        emojis.add(new Emoji(R.drawable.school, "School"));
        emojis.add(new Emoji(R.drawable.ear, "Ear"));
        emojis.add(new Emoji(R.drawable.lovely, "Love it"));
        emojis.add(new Emoji(R.drawable.rose, "Rose"));
        emojis.add(new Emoji(R.drawable.hospital, "Hospital"));
        emojis.add(new Emoji(R.drawable.stethoscope, "Stethoscpe"));

        emojiRecyclerView = findViewById(R.id.emojiRecyclerView);
        if (emojiRecyclerView == null) {
            Log.e(TAG, "emojiRecyclerView is null!");
        } else {
            Log.d(TAG, "emojiRecyclerView is ready.");
        }

        emojiRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        emojiAdapter = new EmojiAdapter(emojis, this);
        emojiRecyclerView.setAdapter(emojiAdapter);

        // Initial check to lock the app if time is already zero
        if (remainingTimeMillis <= 0) {
            lockApp();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Start the timer when the app is in the foreground
        loadParentLockSettings();
        loadRemainingTime();
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 1000);

        // Apply the current state of the lock and UI filter
        if (remainingTimeMillis <= 0) {
            lockApp();
        } else if (remainingTimeMillis < warningTimeMillis) {
            float ratio = (float) remainingTimeMillis / warningTimeMillis;
            applyDesaturationFilter(ratio);
        } else {
            applyDesaturationFilter(1.0f);
            lockOverlay.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Pause the timer when the app goes into the background
        timerHandler.removeCallbacks(timerRunnable);
        saveRemainingTime();
    }

    @Override
    public void onEmojiClick(Emoji emoji) {
        if (!isLocked) {
            Log.d(TAG, "Emoji clicked: " + emoji.getName());
            Toast.makeText(this, "Clicked: " + emoji.getName(), Toast.LENGTH_SHORT).show();
            showEmojiPopup(emoji);
        } else {
            // This is the crucial fix: If the app is locked, we show the puzzle.
            Toast.makeText(this, "The app is locked. Please unlock to continue.", Toast.LENGTH_SHORT).show();
            showParentLockDialog();
        }
    }

    private void showEmojiPopup(Emoji emoji) {
        if (isLocked) return;

        Log.d(TAG, "Attempting to show emoji popup for: " + emoji.getName());

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_emoji_popup);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            Log.d(TAG, "Dialog window properties set.");
        } else {
            Log.e(TAG, "Dialog window is null, cannot set properties.");
            return;
        }

        ImageView actualEmojiImageView = dialog.findViewById(R.id.modalEmojiImageView);
        TextView actualEmojiNameTextView = dialog.findViewById(R.id.modalEmojiNameTextView);

        if (actualEmojiImageView != null && actualEmojiNameTextView != null) {
            actualEmojiImageView.setImageResource(emoji.getImageResourceId());
            actualEmojiNameTextView.setText(emoji.getName());
            Log.d(TAG, "Emoji image and name set in dialog.");
        } else {
            Log.e(TAG, "Views inside dialog_emoji_popup.xml not found! Check IDs.");
            dialog.dismiss();
            return;
        }

        dialog.show();
        Log.d(TAG, "Dialog shown.");

        Handler handler = new Handler();
        final int[] count = {0};
        final Runnable[] animateOnly = new Runnable[1];

        animateOnly[0] = () -> {
            if (count[0] < 3) {
                Log.d(TAG, "Animation cycle: " + (count[0] + 1));

                ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(actualEmojiImageView, "scaleX", 1f, 1.2f);
                ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(actualEmojiImageView, "scaleY", 1f, 1.2f);
                scaleUpX.setDuration(300);
                scaleUpY.setDuration(300);

                ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(actualEmojiImageView, "scaleX", 1.2f, 1f);
                ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(actualEmojiImageView, "scaleY", 1.2f, 1f);
                scaleDownX.setDuration(300);
                scaleDownY.setDuration(300);

                scaleUpX.start();
                scaleUpY.start();

                scaleUpX.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        scaleDownX.start();
                        scaleDownY.start();
                    }
                });

                scaleDownX.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        count[0]++;
                        handler.postDelayed(animateOnly[0], 500);
                    }
                });
            } else {
                Log.d(TAG, "Animation cycles complete. Dismissing dialog.");
                dialog.dismiss();
            }
        };

        handler.post(animateOnly[0]);
    }

    private void lockApp() {
        isLocked = true;
        applyDesaturationFilter(0.0f);
        if (lockOverlay != null) {
            lockOverlay.setVisibility(View.VISIBLE);
        }
        Toast.makeText(this, "Time's up! Please ask a parent to unlock the app.", Toast.LENGTH_LONG).show();
    }

    private void unlockApp() {
        isLocked = false;
        applyDesaturationFilter(1.0f);
        if (lockOverlay != null) {
            lockOverlay.setVisibility(View.GONE);
        }
        remainingTimeMillis = dailyTimeLimitMillis;
        saveRemainingTime();
    }

    private void showParentLockDialog() {
        Random random = new Random();
        int a = random.nextInt(10) + 1;
        int b = random.nextInt(10) + 1;
        int answer = a + b;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Parent Lock: " + a + " + " + b + " = ?");
        final EditText answerInput = new EditText(this);
        builder.setView(answerInput);

        builder.setPositiveButton("Unlock", (dialog, which) -> {
            if (answerInput.getText().toString().equals(String.valueOf(answer))) {
                unlockApp();
                Toast.makeText(MainActivity3.this, "Unlocked!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity3.this, "Incorrect answer.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNeutralButton("Add 5 min", (dialog, which) -> {
            if (answerInput.getText().toString().equals(String.valueOf(answer))) {
                addTime(5); // Adds 5 minutes
                Toast.makeText(MainActivity3.this, "5 minutes added!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity3.this, "Incorrect answer.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void addTime(long additionalMinutes) {
        remainingTimeMillis += additionalMinutes * 60 * 1000;
        isLocked = false;
        applyDesaturationFilter(1.0f);
        if (lockOverlay != null) {
            lockOverlay.setVisibility(View.GONE);
        }
        saveRemainingTime();
    }

    private void loadParentLockSettings() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        dailyTimeLimitMillis = prefs.getLong(KEY_DAILY_LIMIT, 60 * 60 * 1000); // Default to 1 hour
        warningTimeMillis = prefs.getLong(KEY_WARNING_TIME, 15 * 60 * 1000); // Default to 15 minutes
    }

    private void loadRemainingTime() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String lastUsedDate = prefs.getString(KEY_LAST_USED_DATE, "");
        String currentDate = DateFormat.getDateInstance().format(new Date());

        if (currentDate.equals(lastUsedDate)) {
            remainingTimeMillis = prefs.getLong(KEY_REMAINING_TIME, dailyTimeLimitMillis);
        } else {
            remainingTimeMillis = dailyTimeLimitMillis;
            applyDesaturationFilter(1.0f);
        }
    }

    private void saveRemainingTime() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(KEY_REMAINING_TIME, remainingTimeMillis);
        editor.putString(KEY_LAST_USED_DATE, DateFormat.getDateInstance().format(new Date()));
        editor.apply();
    }

    private void applyDesaturationFilter(float ratio) {
        if (mainLayout == null) return;
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(ratio);
        ColorFilter filter = new ColorMatrixColorFilter(matrix);
        mainLayout.getBackground().setColorFilter(filter);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "MainActivity3 onDestroy called.");
        super.onDestroy();
    }
}
