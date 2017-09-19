package com.example.uge01006.converter;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
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
    private LinearLayout LLdownload;
    private LinearLayout LLwatchYoutube;
    private LinearLayout LLshareVideo;

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
        LLdownload = this.findViewById(R.id.LLdownload);
        LLwatchYoutube = this.findViewById(R.id.LLwatchYoutube);
        LLshareVideo = this.findViewById(R.id.LLshareVideo);
        TVtitleDetail.setText(clickedVideo.getTitle());
        TVviewsDetail.setText(addDots(clickedVideo.getViewCount()));
        TVlikesDetail.setText(addDots(clickedVideo.getLikeCount()));
        TVdislikesDetail.setText(addDots(clickedVideo.getDislikeCount()));
        TVuserDetail.setText(clickedVideo.getChannelTitle());
        youtubePlayer.initialize(DeveloperKey.DEVELOPER_KEY, this);
        LLdownload.setOnClickListener(view ->
        {
            Intent downloader = new Intent(this, DownloadActivity.class);
            downloader.setType("text/plain");
            downloader.putExtra(Intent.EXTRA_TEXT, YOUTUBE_BASE_URL+clickedVideo.getId());
            startActivity(downloader);
        });
        LLwatchYoutube.setOnClickListener(view -> {watchOnYoutube();});
        LLshareVideo.setOnClickListener(view -> {share();});
    }

    public void share()
    {
        try
        {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, YOUTUBE_BASE_URL+clickedVideo.getId());
            startActivity(Intent.createChooser(shareIntent, " "+getResources().getString(R.string.share)));
        }
        catch(Exception e) {e.printStackTrace();}
    }
    public void watchOnYoutube()
    {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:"+clickedVideo.getId()));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_BASE_URL+clickedVideo.getId()));
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
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {}
    @Override
    protected void onPause() {super.onPause(); this.finish();}
}
