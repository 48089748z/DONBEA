package com.extractor.uri48089748z.converter;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.extractor.uri48089748z.converter.DAOs.X;
import com.extractor.uri48089748z.converter.POJOs.VideoYoutube;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
public class DetailActivity extends YouTubeBaseActivity implements  YouTubePlayer.OnInitializedListener
{
    private static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";
    private SharedPreferences settings;
    private YouTubePlayerView youtubePlayer;
    private VideoYoutube clickedVideo;
    private TextView TVtitleDetail;
    private TextView TVviewsDetail;
    private TextView TVlikesDetail;
    private TextView TVdislikesDetail;
    private TextView TVuserDetail;
    private LinearLayout LLconvert;
    private LinearLayout LLwatchYoutube;
    private LinearLayout LLshareVideo;
    private LinearLayout LLdetail;
    private TextView TVsplitbar11;
    private TextView TVsplitbar12;
    private TextView TVsplitbar13;
    private TextView TVsplitbar14;
    private TextView TVsplitbar15;
    private TextView TVconvert;
    private TextView TVwatchYoutube;
    private TextView TVshare;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
        clickedVideo = (VideoYoutube) getIntent().getExtras().getSerializable("selectedVideo");
        TVtitleDetail = this.findViewById(R.id.TVtitleDetail);
        youtubePlayer = this.findViewById(R.id.YTVplayer);
        TVviewsDetail = this.findViewById(R.id.TVviewsDetail);
        TVlikesDetail = this.findViewById(R.id.TVlikesDetail);
        TVdislikesDetail = this.findViewById(R.id.TVdislikesDetail);
        TVuserDetail = this.findViewById(R.id.TVuserDetail);
        LLconvert = this.findViewById(R.id.LLconvert);
        LLwatchYoutube = this.findViewById(R.id.LLwatchYoutube);
        LLshareVideo = this.findViewById(R.id.LLshareVideo);
        LLdetail = this.findViewById(R.id.LLdetail);
        TVsplitbar11 = this.findViewById(R.id.TVsplitbar11);
        TVsplitbar12 = this.findViewById(R.id.TVsplitbar12);
        TVsplitbar13 = this.findViewById(R.id.TVsplitbar13);
        TVsplitbar14 = this.findViewById(R.id.TVsplitbar14);
        TVsplitbar15 = this.findViewById(R.id.TVsplitbar15);
        TVconvert = this.findViewById(R.id.TVconvert);
        TVwatchYoutube = this.findViewById(R.id.TVwatchYoutube);
        TVshare = this.findViewById(R.id.TVshare);
        checkTheme();
        TVtitleDetail.setText(clickedVideo.getTitle());
        TVviewsDetail.setText(addDots(clickedVideo.getViewCount()));
        TVlikesDetail.setText(addDots(clickedVideo.getLikeCount()));
        TVdislikesDetail.setText(addDots(clickedVideo.getDislikeCount()));
        TVuserDetail.setText(clickedVideo.getChannelTitle());
        youtubePlayer.initialize(X.DEVELOPER, this);
        LLconvert.setOnClickListener(view ->
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
        if (requestCode == 1) {youtubePlayer.initialize(X.DEVELOPER, this);}
    }
    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {}
    @Override
    protected void onPause() {super.onPause(); this.finish();}
    public void checkTheme()
    {
        if (settings.getBoolean("dark", true)) {setDarkTheme();}
        else {setLightTheme();}
    }
    private void setDarkTheme()
    {
        LLdetail.setBackgroundResource(R.color.GREY_BACKGROUND_DARK_SUPER);
        TVsplitbar11.setBackgroundResource(R.color.GREY_TEXT_LIGHT_SUPER);
        TVsplitbar12.setBackgroundResource(R.color.GREY_TEXT_LIGHT_SUPER);
        TVsplitbar13.setBackgroundResource(R.color.GREY_TEXT_LIGHT_SUPER);
        TVsplitbar14.setBackgroundResource(R.color.GREY_TEXT_LIGHT_SUPER);
        TVsplitbar15.setBackgroundResource(R.color.GREY_TEXT_LIGHT_SUPER);
        TVtitleDetail.setTextColor(getResources().getColor(R.color.GREY_TEXT_LIGHT_SUPER));
        TVviewsDetail.setTextColor(getResources().getColor(R.color.GREY_TEXT_LIGHT_SUPER));
        TVlikesDetail.setTextColor(getResources().getColor(R.color.GREY_TEXT_LIGHT_SUPER));
        TVdislikesDetail.setTextColor(getResources().getColor(R.color.GREY_TEXT_LIGHT_SUPER));
        TVuserDetail.setTextColor(getResources().getColor(R.color.GREY_TEXT_LIGHT_SUPER));
        TVconvert.setTextColor(getResources().getColor(R.color.GREY_TEXT_LIGHT_SUPER));
        TVwatchYoutube.setTextColor(getResources().getColor(R.color.GREY_TEXT_LIGHT_SUPER));
        TVshare.setTextColor(getResources().getColor(R.color.GREY_TEXT_LIGHT_SUPER));
    }
    private void setLightTheme()
    {
        LLdetail.setBackgroundResource(R.color.GREY_TEXT_LIGHT_SUPER);
        TVsplitbar11.setBackgroundResource(R.color.GREY_BACKGROUND_DARK_SUPER);
        TVsplitbar12.setBackgroundResource(R.color.GREY_BACKGROUND_DARK_SUPER);
        TVsplitbar13.setBackgroundResource(R.color.GREY_BACKGROUND_DARK_SUPER);
        TVsplitbar14.setBackgroundResource(R.color.GREY_BACKGROUND_DARK_SUPER);
        TVsplitbar15.setBackgroundResource(R.color.GREY_BACKGROUND_DARK_SUPER);
        TVtitleDetail.setTextColor(getResources().getColor(R.color.GREY_BACKGROUND_DARK_SUPER));
        TVviewsDetail.setTextColor(getResources().getColor(R.color.GREY_BACKGROUND_DARK_SUPER));
        TVlikesDetail.setTextColor(getResources().getColor(R.color.GREY_BACKGROUND_DARK_SUPER));
        TVdislikesDetail.setTextColor(getResources().getColor(R.color.GREY_BACKGROUND_DARK_SUPER));
        TVuserDetail.setTextColor(getResources().getColor(R.color.GREY_BACKGROUND_DARK_SUPER));
        TVconvert.setTextColor(getResources().getColor(R.color.GREY_BACKGROUND_DARK_SUPER));
        TVwatchYoutube.setTextColor(getResources().getColor(R.color.GREY_BACKGROUND_DARK_SUPER));
        TVshare.setTextColor(getResources().getColor(R.color.GREY_BACKGROUND_DARK_SUPER));
    }
}
