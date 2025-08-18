package com.example.saksham;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class VideoActivity extends AppCompatActivity {
    private int currentVideoPosition = 0;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        VideoView videoView = findViewById(R.id.videoView);

        // Get the level number passed from LevelsActivity
        int levelNumber = getIntent().getIntExtra("LEVEL_NUMBER", 0); // 0 is a default value
        // Determine which video to play based on the level number
        int videoResId = 0;
        String videoTitle; // Optional: for displaying video title
        switch (levelNumber) {
            case 1:
                videoResId = R.raw.video_level_1; // Make sure you have video_level_1.mp4 in res/raw
                videoTitle = "Level 1 Video";
                break;
                /*
            case 2:
                videoResId = R.raw.video_level_2; // Make sure you have video_level_2.mp4 in res/raw
                videoTitle = "Level 2 Video";
                break;
            case 3:
                videoResId = R.raw.video_level_3; // Make sure you have video_level_3.mp4 in res/raw
                videoTitle = "Level 3 Video";
                break;
            default:
                // Handle cases where levelNumber is not recognized (e.g., show an error or a default video)
                videoResId = R.raw.default_error_video; // You might want a default error video
                videoTitle = "Error: Video Not Found";
                break; */


        }
        // Get the URI for the video resource
        String videoPath = "android.resource://" + getPackageName() + "/" + videoResId;
        Uri uri = Uri.parse(videoPath);

        // Set the video URI to the VideoView
        videoView.setVideoURI(uri);

        // Restore video position if the activity is being recreated
        if (savedInstanceState != null) {
            currentVideoPosition = savedInstanceState.getInt("VIDEO_POSITION");
        }

        // Add media controls (play/pause, seek bar etc.)
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        // Seek to the saved position and start playing
        videoView.seekTo(currentVideoPosition);
        // Start playing the video
        videoView.start();

        // Optional: Listen for video completion (useful for "child has to complete level 1")
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // Handle video completion here
                // For example, you might want to show a "Level Complete!" message
                // or navigate back to the LevelsActivity, or unlock the next level.
                // Toast.makeText(VideoActivity.this, videoTitle + " Completed!", Toast.LENGTH_SHORT).show();
                // finish(); // To go back to the previous activity
            }
        });

        // Optional: Handle errors during video playback
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                // Handle video playback errors
                // Toast.makeText(VideoActivity.this, "Error playing video: " + videoTitle, Toast.LENGTH_LONG).show();
                return true; // Return true if the error was handled
            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Save the current video position when the activity is paused
        if (videoView != null) {
            currentVideoPosition = videoView.getCurrentPosition();
            videoView.pause(); // Explicitly pause the video
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Resume playback from the saved position
        if (videoView != null && !videoView.isPlaying()) {
            videoView.seekTo(currentVideoPosition);
            videoView.start();
        }
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the current position to the bundle to be restored later
        outState.putInt("VIDEO_POSITION", currentVideoPosition);
    }
}



