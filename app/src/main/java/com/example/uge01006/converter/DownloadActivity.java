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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class DownloadActivity extends AppCompatActivity
{
    private static final int ITAG_FOR_AUDIO = 140;
    private LinearLayout mainLayout;
    private ProgressBar mainProgressBar;
    private List<YtFragmentedVideo> formatsToShowList;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        mainLayout = (LinearLayout) this.findViewById(R.id.LLdownloader);
        mainProgressBar = (ProgressBar) this.findViewById(R.id.prgrBar);
        button = (Button) this.findViewById(R.id.button);
        String link = getIntent().getStringExtra(Intent.EXTRA_TEXT);
        getYoutubeDownloadUrl(link);
    }
    private void addButtonToMainLayout(final String videoTitle, final YtFragmentedVideo ytFrVideo)
    {
        Log.e("MENUUUUUUUUUUU", videoTitle);
        // Display some buttons and let the user choose the format
        String btnText;
        if (ytFrVideo.height == -1)
        {
            btnText = "Audio " + ytFrVideo.audioFile.getFormat().getAudioBitrate() + " kbit/s";
        }
        else
        {
            btnText = (ytFrVideo.videoFile.getFormat().getFps() == 60) ? ytFrVideo.height + "p60" : ytFrVideo.height + "p";
        }
        //Button btn = new Button(this);
        button.setText(btnText);
        button.setOnClickListener(v ->
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
        Log.e("ERRORRRRRRRRRRRRR", "mainLayout.addView not working");
    }
    private void getYoutubeDownloadUrl(String link)
    {
        new YouTubeExtractor(this)
        {
            @Override
            public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta)
            {
                mainProgressBar.setVisibility(View.GONE);
                if (ytFiles == null)
                {
                    TextView tv = new TextView(DownloadActivity.this);
                    tv.setText("Couldn't extract URLs");
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    mainLayout.addView(tv);
                    return;
                }
                formatsToShowList = new ArrayList<>();
                for (int i = 0, itag; i < ytFiles.size(); i++)
                {
                    itag = ytFiles.keyAt(i);
                    YtFile ytFile = ytFiles.get(itag);
                    if (ytFile.getFormat().getHeight() == -1 || ytFile.getFormat().getHeight() >= 360) {addFormatToList(ytFile, ytFiles);}
                }
                Collections.sort(formatsToShowList, (lhs, rhs) -> lhs.height - rhs.height);
                for (YtFragmentedVideo files : formatsToShowList) {addButtonToMainLayout(vMeta.getTitle(), files);}
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
