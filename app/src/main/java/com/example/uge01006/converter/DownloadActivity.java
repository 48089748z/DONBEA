package com.example.uge01006.converter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.uge01006.converter.Extractor.VideoMeta;
import com.example.uge01006.converter.Extractor.YouTubeExtractor;
import com.example.uge01006.converter.Extractor.YtFile;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class DownloadActivity extends AppCompatActivity
{
    private static final int ITAG_FOR_AUDIO = 140;
    private ProgressBar PBdownloading;
    private List<YtFragmentedVideo> formatsToShowList;
    private TextView TVtitleDownload;
    private Button BTaudio;
    private Button BTvideo144;
    private Button BTvideo240;
    private Button BTvideo360;
    private Button BTvideo480;
    private Button BTvideo720;
    private Button BTvideo1080;
    private Button BTvideo2160;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        PBdownloading = (ProgressBar) this.findViewById(R.id.PBdownloading);
        TVtitleDownload = (TextView) this.findViewById(R.id.TVtitleDownload);
        BTaudio = (Button) this.findViewById(R.id.BTaudio);
        BTvideo144 = (Button) this.findViewById(R.id.BTvideo144);
        BTvideo240 = (Button) this.findViewById(R.id.BTvideo240);
        BTvideo360 = (Button) this.findViewById(R.id.BTvideo360);
        BTvideo480 = (Button) this.findViewById(R.id.BTvideo480);
        BTvideo720 = (Button) this.findViewById(R.id.BTvideo720);
        BTvideo1080 = (Button) this.findViewById(R.id.BTvideo1080);
        BTvideo2160 = (Button) this.findViewById(R.id.BTvideo2160);

        String link = getIntent().getStringExtra(Intent.EXTRA_TEXT);
        getYoutubeDownloadUrl(link);
    }
    private void manageButtons(final String videoTitle, final YtFragmentedVideo ytFrVideo)
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
    private void getYoutubeDownloadUrl(String link)
    {
        new YouTubeExtractor(this)
        {
            @Override
            public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta)
            {
                PBdownloading.setVisibility(View.GONE);
                formatsToShowList = new ArrayList<>();
                for (int i = 0, itag; i < ytFiles.size(); i++)
                {
                    itag = ytFiles.keyAt(i);
                    YtFile ytFile = ytFiles.get(itag);
                    if (ytFile.getFormat().getHeight() == -1 || ytFile.getFormat().getHeight() >= 360) {addFormatToList(ytFile, ytFiles);}
                }
                Collections.sort(formatsToShowList, (lhs, rhs) -> lhs.height - rhs.height);
                for (YtFragmentedVideo files : formatsToShowList)
                {
                    manageButtons(vMeta.getTitle(), files);
                }
            }
        }.extract(link, true, true);
    }
    private void addFormatToList(YtFile ytFile, SparseArray<YtFile> ytFiles)
    {
        int height = ytFile.getFormat().getHeight();
        if (height != -1)
        {
            for (YtFragmentedVideo frVideo : formatsToShowList)
            {
                if (frVideo.height == height && (frVideo.videoFile == null || frVideo.videoFile.getFormat().getFps() == ytFile.getFormat().getFps())) {return;}
            }
        }
        YtFragmentedVideo frVideo = new YtFragmentedVideo();
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
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        return manager.enqueue(request);
    }
    private void cacheDownloadIds(String downloadIds)
    {
        File dlCacheFile = new File(this.getCacheDir().getAbsolutePath() + "/" + downloadIds);
        try {dlCacheFile.createNewFile();}
        catch (IOException e) {e.printStackTrace();}
    }
    private class YtFragmentedVideo
    {
        int height;
        YtFile audioFile;
        YtFile videoFile;
    }
}
