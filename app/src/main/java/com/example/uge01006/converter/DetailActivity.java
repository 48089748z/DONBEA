package com.example.uge01006.converter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.example.uge01006.converter.POJOs.VideoYoutube;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class DetailActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        VideoYoutube clickedVideo = (VideoYoutube) getIntent().getExtras().getSerializable("selectedVideo");
        /**Crear el layout
         * Reproducir video
         * Poner info de VIEWS LIKES DISLIKSE
         * Poner Botones de DOWNLOAD mp3 y mp4
         * */


        Log.e("TEST", clickedVideo.getViewCount());
    }
}
