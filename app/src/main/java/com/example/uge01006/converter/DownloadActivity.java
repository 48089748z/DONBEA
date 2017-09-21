package com.example.uge01006.converter;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.SparseArray;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.uge01006.converter.DAOs.X;
import com.example.uge01006.converter.Extractor.VideoMeta;
import com.example.uge01006.converter.Extractor.YouTubeExtractor;
import com.example.uge01006.converter.Extractor.YoutubeFragmentedVideo;
import com.example.uge01006.converter.Extractor.YtFile;
import com.google.android.gms.ads.AdListener;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class DownloadActivity extends AppCompatActivity
{
    private SharedPreferences settings;
    private RotateAnimation spinner = new RotateAnimation(360f, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    private static final int ITAG_FOR_AUDIO = 140;
    private List<YoutubeFragmentedVideo> formatsToShowList;
    private ImageView IVloadingDownload;
    private TextView TVtitleDownload;
    private TextView TVheaderDownload;
    private Button BTaudio;
    private Button BTvideo360;
    private Button BTvideo480;
    private Button BTvideo720;
    private Button BTvideo1080;
    private Button BTvideo2160;
    private ImageView IVaudioDownload;
    private ImageView IVvideo360Download;
    private ImageView IVvideo480Download;
    private ImageView IVvideo720Download;
    private ImageView IVvideo1080Download;
    private ImageView IVvideo2160Download;
    private LinearLayout LLdownload;
    private TextView TVsplitbar8;
    private TextView TVsplitbar9;
    private TextView TVsplitbar10;
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        TVtitleDownload = (TextView) this.findViewById(R.id.TVtitleDownload);
        TVheaderDownload = (TextView) this.findViewById(R.id.TVheaderDownload);
        BTaudio = (Button) this.findViewById(R.id.BTaudio);
        BTvideo360 = (Button) this.findViewById(R.id.BTvideo360);
        BTvideo480 = (Button) this.findViewById(R.id.BTvideo480);
        BTvideo720 = (Button) this.findViewById(R.id.BTvideo720);
        BTvideo1080 = (Button) this.findViewById(R.id.BTvideo1080);
        BTvideo2160 = (Button) this.findViewById(R.id.BTvideo2160);
        IVloadingDownload = (ImageView) this.findViewById(R.id.IVloadingDownload);
        IVaudioDownload = (ImageView) this.findViewById(R.id.IVaudioDownload) ;
        IVvideo360Download = (ImageView) this.findViewById(R.id.IVvideo360Download);
        IVvideo480Download = (ImageView) this.findViewById(R.id.IVvideo480Download);
        IVvideo720Download = (ImageView) this.findViewById(R.id.IVvideo720Download);
        IVvideo1080Download = (ImageView) this.findViewById(R.id.IVvideo1080Download);
        IVvideo2160Download = (ImageView) this.findViewById(R.id.IVvideo2160Download);
        LLdownload = (LinearLayout) this.findViewById(R.id.LLdownload);
        TVsplitbar8 = (TextView) this.findViewById(R.id.TVsplitbar8);
        TVsplitbar9 = (TextView) this.findViewById(R.id.TVsplitbar9);
        TVsplitbar10 = (TextView) this.findViewById(R.id.TVsplitbar10);
        configureDisplayAd();
        checkTheme();
        spinImage();
        String YOUTUBE_VIDEO_LINK = getIntent().getExtras().getString(Intent.EXTRA_TEXT);
        getYoutubeVideoFileURL(YOUTUBE_VIDEO_LINK);
        BTaudio.setOnClickListener(view -> download(getFragment(-1)));
        BTvideo360.setOnClickListener(view -> download(getFragment(360)));
        BTvideo480.setOnClickListener(view -> download(getFragment(480)));
        BTvideo720.setOnClickListener(view -> download(getFragment(720)));
        BTvideo1080.setOnClickListener(view -> download(getFragment(1080)));
        BTvideo2160.setOnClickListener(view -> download(getFragment(2160)));
    }

    public void configureDisplayAd()
    {
        MobileAds.initialize(this, X.ADVERTISER_TEST);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(X.ADVERTISER_TEST_ADS);
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.setAdListener(new AdListener(){public void onAdLoaded(){mInterstitialAd.show();}});
    }

    private YoutubeFragmentedVideo getFragment (int code)
    {
        for (YoutubeFragmentedVideo chosenFragment: formatsToShowList)
        {
            if (chosenFragment.height == code) {return chosenFragment;}
        }
        return null;
    }
    private void download(final YoutubeFragmentedVideo fragmentedVideo)
    {
        configureDisplayAd();
        String videoTitle = fragmentedVideo.title;
        TVtitleDownload.setText(videoTitle);
        String filename;
        if (videoTitle.length() > 55) {filename = videoTitle.substring(0, 55);}
        else {filename = videoTitle;}
        filename = filename.replaceAll("\\\\|>|<|\"|\\||\\*|\\?|%|:|#|/", "");
        filename += (fragmentedVideo.height == -1) ? "" : "-" + fragmentedVideo.height + "p";
        String downloadIds = "";
        boolean hideAudioDownloadNotification = false;
        if (fragmentedVideo.videoFile != null)
        {
            downloadIds += downloadFromUrl(fragmentedVideo.videoFile.getUrl(), videoTitle, filename + "." + fragmentedVideo.videoFile.getFormat().getExt(), false);
            downloadIds += "-";
            hideAudioDownloadNotification = true;
        }
        if (fragmentedVideo.audioFile != null)
        {
            downloadIds += downloadFromUrl(fragmentedVideo.audioFile.getUrl(), videoTitle, filename + "." + fragmentedVideo.audioFile.getFormat().getExt(), hideAudioDownloadNotification);
        }
        if (fragmentedVideo.audioFile != null)
        {
            cacheDownloadIds(downloadIds);
        }
        finish();
    }
    private void getYoutubeVideoFileURL(String link)
    {
        new YouTubeExtractor(this)
        {
            @Override
            public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta)
            {
                TVheaderDownload.setText(getResources().getString(R.string.download_options));
                TVtitleDownload.setText(vMeta.getTitle());
                IVloadingDownload.clearAnimation();
                formatsToShowList = new ArrayList<>();
                for (int i = 0, itag; i < ytFiles.size(); i++)
                {
                    itag = ytFiles.keyAt(i);
                    YtFile ytFile = ytFiles.get(itag);
                    if (ytFile.getFormat().getHeight() == -1 || ytFile.getFormat().getHeight() >= 360)
                    {
                        fillFormatsArray(ytFile, ytFiles);
                    }
                }
                Collections.sort(formatsToShowList, (lhs, rhs) -> lhs.height - rhs.height);
                for (YoutubeFragmentedVideo fragmentedVideo : formatsToShowList)
                {
                    fragmentedVideo.title = vMeta.getTitle();
                    if (fragmentedVideo.height == -1)
                    {
                        BTaudio.setVisibility(View.VISIBLE);
                        IVaudioDownload.setVisibility(View.VISIBLE);
                    }
                    if (!settings.getBoolean("audio", true))
                    {
                        if (fragmentedVideo.height == 360)
                        {
                            BTvideo360.setVisibility(View.VISIBLE);
                            IVvideo360Download.setVisibility(View.VISIBLE);
                        }
                        if (fragmentedVideo.height == 480)
                        {
                            BTvideo480.setVisibility(View.VISIBLE);
                            IVvideo480Download.setVisibility(View.VISIBLE);
                        }
                        if (fragmentedVideo.height == 720)
                        {
                            BTvideo720.setVisibility(View.VISIBLE);
                            IVvideo720Download.setVisibility(View.VISIBLE);
                        }
                        if (fragmentedVideo.height == 1080)
                        {
                            BTvideo1080.setVisibility(View.VISIBLE);
                            IVvideo1080Download.setVisibility(View.VISIBLE);
                        }
                        if (fragmentedVideo.height == 2160)
                        {
                            BTvideo2160.setVisibility(View.VISIBLE);
                            IVvideo2160Download.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        }.extract(link, true, true);
    }
    private void fillFormatsArray(YtFile ytFile, SparseArray<YtFile> ytFiles)
    {
        int height = ytFile.getFormat().getHeight();
        if (height != -1)
        {
            for (YoutubeFragmentedVideo frVideo : formatsToShowList)
            {
                if (frVideo.height == height && (frVideo.videoFile == null || frVideo.videoFile.getFormat().getFps() == ytFile.getFormat().getFps())) {return;}
            }
        }
        YoutubeFragmentedVideo frVideo = new YoutubeFragmentedVideo();
        frVideo.height = height;
        if (ytFile.getFormat().isDashContainer())
        {
            if (height > 0)
            {
                frVideo.videoFile = ytFile;
                frVideo.audioFile = ytFiles.get(ITAG_FOR_AUDIO);
            }
            else {frVideo.audioFile = ytFile;}
        }
        else {frVideo.videoFile = ytFile;}
        formatsToShowList.add(frVideo);
    }
    private long downloadFromUrl(String youtubeDlUrl, String downloadTitle, String fileName, boolean hide)
    {
        Uri uri = Uri.parse(youtubeDlUrl);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(downloadTitle);
        if (hide)
        {
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
            request.setVisibleInDownloadsUi(false);
        }
        else
        {
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        }
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        return manager.enqueue(request);
    }
    private void cacheDownloadIds(String downloadIds)
    {
        File dlCacheFile = new File(this.getCacheDir().getAbsolutePath() + "/" + downloadIds);
        try {dlCacheFile.createNewFile();}
        catch (IOException e) {e.printStackTrace();}
    }
    private void spinImage()
    {
        spinner.setInterpolator(new LinearInterpolator());
        spinner.setDuration(1200);
        spinner.setRepeatCount(Animation.INFINITE);
        IVloadingDownload.setVisibility(View.VISIBLE);
        BTaudio.setVisibility(View.INVISIBLE);
        BTvideo360.setVisibility(View.INVISIBLE);
        BTvideo480.setVisibility(View.INVISIBLE);
        BTvideo720.setVisibility(View.INVISIBLE);
        BTvideo1080.setVisibility(View.INVISIBLE);
        BTvideo2160.setVisibility(View.INVISIBLE);
        IVaudioDownload.setVisibility(View.INVISIBLE);
        IVvideo360Download.setVisibility(View.INVISIBLE);
        IVvideo480Download.setVisibility(View.INVISIBLE);
        IVvideo720Download.setVisibility(View.INVISIBLE);
        IVvideo1080Download.setVisibility(View.INVISIBLE);
        IVvideo2160Download.setVisibility(View.INVISIBLE);
        IVloadingDownload.startAnimation(spinner);
    }
    public void checkTheme()
    {
        if (settings.getBoolean("dark", true)) {setDarkTheme();}
        else {setLightTheme();}
    }
    private void setDarkTheme()
    {
        LLdownload.setBackgroundResource(R.color.GREY_BACKGROUND_DARK_SUPER);
        TVsplitbar8.setBackgroundResource(R.color.GREY_TEXT_LIGHT_SUPER);
        TVsplitbar9.setBackgroundResource(R.color.GREY_TEXT_LIGHT_SUPER);
        TVsplitbar10.setBackgroundResource(R.color.GREY_TEXT_LIGHT_SUPER);
        TVheaderDownload.setTextColor(getResources().getColor(R.color.GREY_TEXT_LIGHT_SUPER));
        TVtitleDownload.setTextColor(getResources().getColor(R.color.GREY_TEXT_LIGHT_SUPER));
        BTaudio.setBackgroundResource(R.color.GREY_TEXT_LIGHT);
        BTaudio.setTextColor(getResources().getColor(R.color.GREY_BACKGROUND_DARK_SUPER));
        BTvideo360.setBackgroundResource(R.color.GREY_TEXT_LIGHT);
        BTvideo360.setTextColor(getResources().getColor(R.color.GREY_BACKGROUND_DARK_SUPER));
        BTvideo480.setBackgroundResource(R.color.GREY_TEXT_LIGHT);
        BTvideo480.setTextColor(getResources().getColor(R.color.GREY_BACKGROUND_DARK_SUPER));
        BTvideo720.setBackgroundResource(R.color.GREY_TEXT_LIGHT);
        BTvideo720.setTextColor(getResources().getColor(R.color.GREY_BACKGROUND_DARK_SUPER));
        BTvideo1080.setBackgroundResource(R.color.GREY_TEXT_LIGHT);
        BTvideo1080.setTextColor(getResources().getColor(R.color.GREY_BACKGROUND_DARK_SUPER));
        BTvideo2160.setBackgroundResource(R.color.GREY_TEXT_LIGHT);
        BTvideo2160.setTextColor(getResources().getColor(R.color.GREY_BACKGROUND_DARK_SUPER));
        Picasso.with(this).load(R.drawable.audio_48_grey_light).fit().into(IVaudioDownload);
        Picasso.with(this).load(R.drawable.video_48_grey_light).fit().into(IVvideo360Download);
        Picasso.with(this).load(R.drawable.video_48_grey_light).fit().into(IVvideo480Download);
        Picasso.with(this).load(R.drawable.video_48_grey_light).fit().into(IVvideo720Download);
        Picasso.with(this).load(R.drawable.video_48_grey_light).fit().into(IVvideo1080Download);
        Picasso.with(this).load(R.drawable.video_48_grey_light).fit().into(IVvideo2160Download);
    }
    private void setLightTheme()
    {
        LLdownload.setBackgroundResource(R.color.GREY_TEXT_LIGHT_SUPER);
        TVsplitbar8.setBackgroundResource(R.color.GREY_BACKGROUND_DARK_SUPER);
        TVsplitbar9.setBackgroundResource(R.color.GREY_BACKGROUND_DARK_SUPER);
        TVsplitbar10.setBackgroundResource(R.color.GREY_BACKGROUND_DARK_SUPER);
        TVheaderDownload.setTextColor(getResources().getColor(R.color.GREY_BACKGROUND_DARK_SUPER));
        TVtitleDownload.setTextColor(getResources().getColor(R.color.GREY_BACKGROUND_DARK_SUPER));
        BTaudio.setBackgroundResource(R.color.GREY_BACKGROUND_DARK_SUPER);
        BTaudio.setTextColor(getResources().getColor(R.color.GREY_TEXT_LIGHT));
        BTvideo360.setBackgroundResource(R.color.GREY_BACKGROUND_DARK_SUPER);
        BTvideo360.setTextColor(getResources().getColor(R.color.GREY_TEXT_LIGHT));
        BTvideo480.setBackgroundResource(R.color.GREY_BACKGROUND_DARK_SUPER);
        BTvideo480.setTextColor(getResources().getColor(R.color.GREY_TEXT_LIGHT));
        BTvideo720.setBackgroundResource(R.color.GREY_BACKGROUND_DARK_SUPER);
        BTvideo720.setTextColor(getResources().getColor(R.color.GREY_TEXT_LIGHT));
        BTvideo1080.setBackgroundResource(R.color.GREY_BACKGROUND_DARK_SUPER);
        BTvideo1080.setTextColor(getResources().getColor(R.color.GREY_TEXT_LIGHT));
        BTvideo2160.setBackgroundResource(R.color.GREY_BACKGROUND_DARK_SUPER);
        BTvideo2160.setTextColor(getResources().getColor(R.color.GREY_TEXT_LIGHT));
        Picasso.with(this).load(R.drawable.audio_48_grey_dark).fit().into(IVaudioDownload);
        Picasso.with(this).load(R.drawable.video_48_grey_dark).fit().into(IVvideo360Download);
        Picasso.with(this).load(R.drawable.video_48_grey_dark).fit().into(IVvideo480Download);
        Picasso.with(this).load(R.drawable.video_48_grey_dark).fit().into(IVvideo720Download);
        Picasso.with(this).load(R.drawable.video_48_grey_dark).fit().into(IVvideo1080Download);
        Picasso.with(this).load(R.drawable.video_48_grey_dark).fit().into(IVvideo2160Download);
    }
}
