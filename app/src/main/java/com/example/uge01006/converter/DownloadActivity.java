package com.example.uge01006.converter;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.uge01006.converter.Extractor.VideoMeta;
import com.example.uge01006.converter.Extractor.YouTubeExtractor;
import com.example.uge01006.converter.Extractor.YoutubeFragmentedVideo;
import com.example.uge01006.converter.Extractor.YtFile;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class DownloadActivity extends AppCompatActivity
{
    private RotateAnimation spinner = new RotateAnimation(360f, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    private static final int ITAG_FOR_AUDIO = 140;
    private List<YoutubeFragmentedVideo> formatsToShowList;
    private ImageView loadingDownload;
    private TextView TVtitleDownload;
    private Button BTaudio;
    private Button BTvideo144;
    private Button BTvideo240;
    private Button BTvideo360;
    private Button BTvideo480;
    private Button BTvideo720;
    private Button BTvideo1080;
    private Button BTvideo2160;
    private Button BTvideo4320;
    private ProgressBar PBdownloading;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        TVtitleDownload = (TextView) this.findViewById(R.id.TVtitleDownload);
        BTaudio = (Button) this.findViewById(R.id.BTaudio);
        BTvideo144 = (Button) this.findViewById(R.id.BTvideo144);
        BTvideo240 = (Button) this.findViewById(R.id.BTvideo240);
        BTvideo360 = (Button) this.findViewById(R.id.BTvideo360);
        BTvideo480 = (Button) this.findViewById(R.id.BTvideo480);
        BTvideo720 = (Button) this.findViewById(R.id.BTvideo720);
        BTvideo1080 = (Button) this.findViewById(R.id.BTvideo1080);
        BTvideo2160 = (Button) this.findViewById(R.id.BTvideo2160);
        BTvideo4320 = (Button) this.findViewById(R.id.BTvideo4320);

        loadingDownload = (ImageView) this.findViewById(R.id.loadingDownload);
        spinImage();
        String link = getIntent().getStringExtra(Intent.EXTRA_TEXT);
        getYoutubeVideoFileURL(link);
    }
    private void manageButtons(final String videoTitle, final YoutubeFragmentedVideo ytFrVideo)
    {
        TVtitleDownload.setText(videoTitle);

        String btnText;
        if (ytFrVideo.height == -1)
        {
            btnText = "Audio " + ytFrVideo.audioFile.getFormat().getAudioBitrate() + " kbit/s";
        }
        else
        {
            btnText = (ytFrVideo.videoFile.getFormat().getFps() == 60) ? ytFrVideo.height + "p60" : ytFrVideo.height + "p";
        }

        BTaudio.setOnClickListener(v ->
        {
            String filename;
            if (videoTitle.length() > 55) {filename = videoTitle.substring(0, 55);}
            else {filename = videoTitle;}
            filename = filename.replaceAll("\\\\|>|<|\"|\\||\\*|\\?|%|:|#|/", "");
            filename += (ytFrVideo.height == -1) ? "" : "-" + ytFrVideo.height + "p";
            String downloadIds = "";
            boolean hideAudioDownloadNotification = false;
            if (ytFrVideo.videoFile != null)
            {
                downloadIds += downloadFromUrl(ytFrVideo.videoFile.getUrl(), videoTitle, filename + "." + ytFrVideo.videoFile.getFormat().getExt(), false);downloadIds += "-";
                hideAudioDownloadNotification = true;
            }
            if (ytFrVideo.audioFile != null)
            {
                downloadIds += downloadFromUrl(ytFrVideo.audioFile.getUrl(), videoTitle, filename + "." + ytFrVideo.audioFile.getFormat().getExt(), hideAudioDownloadNotification);
            }
            if (ytFrVideo.audioFile != null)
            {
                cacheDownloadIds(downloadIds);
            }
            finish();
        });
    }
    private void getYoutubeVideoFileURL(String link)
    {
        new YouTubeExtractor(this)
        {
            @Override
            public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta)
            {
                loadingDownload.clearAnimation();
                BTaudio.setVisibility(View.VISIBLE);
                BTvideo144.setVisibility(View.VISIBLE);
                BTvideo240.setVisibility(View.VISIBLE);
                BTvideo360.setVisibility(View.VISIBLE);
                BTvideo480.setVisibility(View.VISIBLE);
                BTvideo720.setVisibility(View.VISIBLE);
                BTvideo1080.setVisibility(View.VISIBLE);
                BTvideo2160.setVisibility(View.VISIBLE);
                BTvideo4320.setVisibility(View.VISIBLE);
                formatsToShowList = new ArrayList<>();
                for (int i = 0, itag; i < ytFiles.size(); i++)
                {
                    itag = ytFiles.keyAt(i);
                    YtFile ytFile = ytFiles.get(itag);
                    if (ytFile.getFormat().getHeight() == -1 || ytFile.getFormat().getHeight() >= 360) {
                        fillFormatsArray(ytFile, ytFiles);}
                }
                Collections.sort(formatsToShowList, (lhs, rhs) -> lhs.height - rhs.height);
                for (YoutubeFragmentedVideo files : formatsToShowList)
                {
                    manageButtons(vMeta.getTitle(), files);
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
        loadingDownload.setVisibility(View.VISIBLE);
        BTaudio.setVisibility(View.INVISIBLE);
        BTvideo144.setVisibility(View.INVISIBLE);
        BTvideo240.setVisibility(View.INVISIBLE);
        BTvideo360.setVisibility(View.INVISIBLE);
        BTvideo480.setVisibility(View.INVISIBLE);
        BTvideo720.setVisibility(View.INVISIBLE);
        BTvideo1080.setVisibility(View.INVISIBLE);
        BTvideo2160.setVisibility(View.INVISIBLE);
        BTvideo4320.setVisibility(View.INVISIBLE);
        loadingDownload.startAnimation(spinner);
    }
}
