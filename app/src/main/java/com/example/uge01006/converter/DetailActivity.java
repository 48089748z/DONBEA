package com.example.uge01006.converter;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
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
    private TextView TVtitleDetail;
    private TextView TVviewsDetail;
    private TextView TVlikesDetail;
    private TextView TVdislikesDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        clickedVideo = (VideoYoutube) getIntent().getExtras().getSerializable("selectedVideo");

        TVtitleDetail = findViewById(R.id.TVtitleDetail);
        youtubePlayer = findViewById(R.id.YTVplayer);
        TVviewsDetail = findViewById(R.id.TVviewsDetail);
        TVlikesDetail = findViewById(R.id.TVlikesDetail);
        TVdislikesDetail = findViewById(R.id.TVdislikesDetail);

        TVtitleDetail.setText(clickedVideo.getTitle().toString());
        TVviewsDetail.setText(addDots(clickedVideo.getViewCount().toString()));
        TVlikesDetail.setText(addDots(clickedVideo.getLikeCount().toString()));
        TVdislikesDetail.setText(addDots(clickedVideo.getDislikeCount().toString()));

        youtubePlayer.initialize(DeveloperKey.DEVELOPER_KEY, this);
    }

    private String addDots(String number)
    {

        int counter = 0;
        for (int x=number.length(); x>0; x--)
        {
            if (counter%3==0)
            {
                number = number.substring(0,x)+"."+number.substring(x,number.length());
            }
            counter++;
        }
        return number.substring(0,number.length()-1);
    }
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored)
    {
        if (!wasRestored) {player.cueVideo(clickedVideo.getId().toString());}
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

    @Override
    protected void onPause() {
        super.onPause();
        this.finish();
    }
}