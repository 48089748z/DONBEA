package com.example.uge01006.converter;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.uge01006.converter.DAOs.YoutubeAPI;
import com.example.uge01006.converter.POJOs.VideoYoutube;
import java.util.ArrayList;
public class MainActivity extends AppCompatActivity
{
    private Toolbar toolbar;
    private ArrayList<VideoYoutube> items;
    private ListViewAdapter LVadapter;
    private ListView LVlist;
    private EditText ETsearch;
    private ImageView IVback;
    private InputMethodManager keyboard;
    private ImageView loading;
    private TextView loadingText;
    private RotateAnimation spinner = new RotateAnimation(360f, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        LVlist = (ListView) this.findViewById(R.id.LVitems);
        ETsearch = (EditText) this.findViewById(R.id.ETsearch);
        IVback = (ImageView) this.findViewById(R.id.IVback);
        loading = (ImageView) this.findViewById(R.id.loading);
        loadingText = (TextView) this.findViewById(R.id.TVloading);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        keyboard = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        IVback.setOnClickListener(view -> showToolbarHideKeyboard(""));
        ETsearch.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE) || actionId == EditorInfo.IME_ACTION_NEXT)
            {
                clearList();
                showToolbarHideKeyboard(ETsearch.getText().toString());
                AsyncListLoader loader = new AsyncListLoader();
                loader.setQuery(ETsearch.getText().toString());
                loadingText.setText("Loading Results for '"+ETsearch.getText().toString()+"' ...");
                loader.execute();
            }
            return false;
        });
        LVlist.setOnItemClickListener((adapterView, view, i, l) ->
        {
            VideoYoutube clickedVideo = (VideoYoutube) LVlist.getAdapter().getItem(i);
            Intent detail = new Intent(this, DetailActivity.class);
            detail.putExtra("selectedVideo", clickedVideo);
            startActivity(detail);
        });
        setSupportActionBar(toolbar);
        ETsearch.setVisibility(View.INVISIBLE);
        items = new ArrayList<>();
        LVadapter = new ListViewAdapter(this, 0, items);
        LVlist.setAdapter(LVadapter);
        loadPopularMusic();
    }
    private void spinImage()
    {
        spinner.setInterpolator(new LinearInterpolator());
        spinner.setDuration(1200);
        spinner.setRepeatCount(Animation.INFINITE);
        loading.startAnimation(spinner);
    }

    private void showToolbarHideKeyboard(String text)
    {
        ETsearch.setVisibility(View.INVISIBLE);
        IVback.setVisibility(View.INVISIBLE);
        ETsearch.clearFocus();
        keyboard.hideSoftInputFromWindow(ETsearch.getWindowToken(), 0);
        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
        if (text!=""){toolbar.setTitle(text);}
    }

    private void hideToolbarShowKeyboard()
    {
        toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateInterpolator()).start();
        ETsearch.setVisibility(View.VISIBLE);
        IVback.setVisibility(View.VISIBLE);
        ETsearch.setText("");
        if (ETsearch.requestFocus()) {keyboard.showSoftInput(ETsearch, InputMethodManager.SHOW_IMPLICIT);}
    }

    public void loadPopularMusic()
    {
        clearList();
        getSupportActionBar().setTitle("Popular Videos Today");
        AsyncListLoader loader = new AsyncListLoader();
        loader.setQuery("");
        loader.execute();
    }

    private void clearList()
    {
        items.clear();
        LVadapter.clear();
        LVlist.setAdapter(null);
        LVlist.setAdapter(LVadapter);
    }

    private void search() {hideToolbarShowKeyboard();}

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_search)
        {
            search();
            return true;
        }
        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private class AsyncListLoader extends AsyncTask<String, Void, Integer>
    {
        private String query = "";
        private YoutubeAPI youtubeAPI = new YoutubeAPI();
        public void setQuery(String query) {this.query = query;}
        protected void onPreExecute()
        {
            spinImage();
            loading.setVisibility(View.VISIBLE);
            loadingText.setVisibility(View.VISIBLE);
        }
        protected Integer doInBackground(String... params)
        {
            while (items.size()<youtubeAPI.MAX_ITEMS_RETURNED)
            {
                if (query.equals("")) {items.addAll(youtubeAPI.getMusicChannelVideos());}
                else {items.addAll(youtubeAPI.searchVideos(query));}
            }
            return 0;
        }
        protected void onPostExecute(Integer result)
        {
            if (result == 0)
            {
                loading.clearAnimation();
                loading.setVisibility(View.INVISIBLE);
                loadingText.setVisibility(View.INVISIBLE);
                LVadapter.notifyDataSetChanged();
            }
            query = "";
        }
    }
}
