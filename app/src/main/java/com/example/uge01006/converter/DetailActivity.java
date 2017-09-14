package com.example.uge01006.converter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.uge01006.converter.DAOs.DeveloperKey;
import com.example.uge01006.converter.POJOs.VideoYoutube;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class DetailActivity extends YouTubeBaseActivity implements  YouTubePlayer.OnInitializedListener
{
    private YouTubePlayerView youtubePlayer;
    private VideoYoutube clickedVideo;
        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_detail);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            clickedVideo = (VideoYoutube) getIntent().getExtras().getSerializable("selectedVideo");
            youtubePlayer = findViewById(R.id.YTVplayer);
            youtubePlayer.initialize(DeveloperKey.DEVELOPER_KEY, this);

            /**Crear el layout
             * Reproducir video
             * Poner info de VIEWS LIKES DISLIKSE
             * Poner Botones de DOWNLOAD mp3 y mp4
             * */
        }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored)
    {
        if (!wasRestored)
        {
            player.cueVideo("wKJ9KzGQq0w");
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1) {youtubePlayer.initialize(DeveloperKey.DEVELOPER_KEY, this);}
    }
    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason)
    {
        if (errorReason.isUserRecoverableError()) {errorReason.getErrorDialog(this, 1).show();}
        else {Toast.makeText(this, "There was an error initializing the Video", Toast.LENGTH_LONG).show();}
    }
}
