package com.example.uge01006.converter;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
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
    private static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";
    private YouTubePlayerView youtubePlayer;
    private VideoYoutube clickedVideo;
    private TextView TVtitleDetail;
    private TextView TVviewsDetail;
    private TextView TVlikesDetail;
    private TextView TVdislikesDetail;
    private TextView TVuserDetail;
    private LinearLayout LLdownloadVideo;
    private LinearLayout LLdownloadAudio;
    private LinearLayout LLwatchYoutube;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        clickedVideo = (VideoYoutube) getIntent().getExtras().getSerializable("selectedVideo");

        TVtitleDetail = this.findViewById(R.id.TVtitleDetail);
        youtubePlayer = this.findViewById(R.id.YTVplayer);
        TVviewsDetail = this.findViewById(R.id.TVviewsDetail);
        TVlikesDetail = this.findViewById(R.id.TVlikesDetail);
        TVdislikesDetail = this.findViewById(R.id.TVdislikesDetail);
        TVuserDetail = this.findViewById(R.id.TVuserDetail);
        LLdownloadVideo = this.findViewById(R.id.LLdownloadVideo);
        LLdownloadAudio = this.findViewById(R.id.LLdownloadAudio);
        LLwatchYoutube = this.findViewById(R.id.LLwatchYoutube);

        TVtitleDetail.setText(clickedVideo.getTitle());
        TVviewsDetail.setText(addDots(clickedVideo.getViewCount()));
        TVlikesDetail.setText(addDots(clickedVideo.getLikeCount()));
        TVdislikesDetail.setText(addDots(clickedVideo.getDislikeCount()));
        TVuserDetail.setText(clickedVideo.getChannelTitle());
        youtubePlayer.initialize(DeveloperKey.DEVELOPER_KEY, this);

        LLdownloadVideo.setOnClickListener(view ->
        {
            watchOnYoutube(clickedVideo.getId());
            //TODO Download Youtube Video to Smartphone
        });

        LLdownloadAudio.setOnClickListener(view ->
        {
            //TODO Convert to Audio the Youtube Video & Download it to Smartphone
        });
        LLwatchYoutube.setOnClickListener(view -> {watchOnYoutube(clickedVideo.getId());});
    }

    public void watchOnYoutube(String id)
    {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {startActivity(appIntent);}
        catch (ActivityNotFoundException ex) {startActivity(webIntent);}
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
