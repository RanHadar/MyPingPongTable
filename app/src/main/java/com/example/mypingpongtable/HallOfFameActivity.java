package com.example.mypingpongtable;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class HallOfFameActivity extends YouTubeBaseActivity {
    YouTubePlayer.OnInitializedListener onInitializedListener;
    YouTubePlayerView youTubePlayerView;
    private LottieAnimationView animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall_of_fame);
        youTubePlayerView = findViewById(R.id.youtube_player);
        animationView = findViewById(R.id.pingpong_animation);

        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                TextView textView = findViewById(R.id.fullscreen_content);
                textView.setVisibility(View.GONE);
                animationView.setVisibility(View.GONE);
                youTubePlayerView.setVisibility(View.VISIBLE);
                // you can add any video you want here
                youTubePlayer.loadVideo("57vUPlZF3ZA");
                youTubePlayer.setFullscreen(true);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };
        startAnimation();
        animationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                youTubePlayerView.initialize(YouTubeConfig.getApiKey(), onInitializedListener);
            }
        });
    }

    private void startAnimation() {
        animationView.setProgress(0);
        animationView.playAnimation();
    }
}
