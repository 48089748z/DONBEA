package com.example.uge01006.converter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.example.uge01006.converter.DAOs.API;
import com.example.uge01006.converter.POJOs.VideoYoutube;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
public class MainActivity extends AppCompatActivity
{
    private SharedPreferences settings;
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
    private TextView TVsplitbar1;
    private TextView TVsplitbar2;
    private TextView TVsplitbar3;
    private LinearLayout LLtoolbarLayout;
    private LinearLayout LLsearchLayout;
    private LinearLayout LLmain;
    private TextView TVloading;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
        toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        LVlist = (ListView) this.findViewById(R.id.LVitems);
        ETsearch = (EditText) this.findViewById(R.id.ETsearch);
        IVback = (ImageView) this.findViewById(R.id.IVback);
        loading = (ImageView) this.findViewById(R.id.loading);
        loadingText = (TextView) this.findViewById(R.id.TVloading);
        TVsplitbar1 = (TextView) this.findViewById(R.id.TVsplitbar1);
        TVsplitbar2 = (TextView) this.findViewById(R.id.TVsplitbar2);
        TVsplitbar3 = (TextView) this.findViewById(R.id.TVsplitbar3);
        LLmain = (LinearLayout) this.findViewById(R.id.LLmain);
        TVloading = (TextView) this.findViewById(R.id.TVloading);
        LLtoolbarLayout = (LinearLayout) this.findViewById(R.id.LLtoolbarLayout);
        LLsearchLayout = (LinearLayout) this.findViewById(R.id.LLsearchLayout);

        setSupportActionBar(toolbar);
        SharedPreferences settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        keyboard = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        LLsearchLayout.setVisibility(View.INVISIBLE);
        items = new ArrayList<>();
        LVadapter = new ListViewAdapter(this, 0, items);
        LVlist.setAdapter(LVadapter);
        IVback.setOnClickListener(view -> showToolbarHideKeyboard());
        ETsearch.setOnEditorActionListener((v, actionId, event) ->
        {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE) || actionId == EditorInfo.IME_ACTION_NEXT)
            {
                clearList();
                showToolbarHideKeyboard();
                loadingText.setText(getResources().getString(R.string.loading_results_for)+" '"+ETsearch.getText().toString() + "' ...");
                loadCustom(ETsearch.getText().toString());
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
        checkTheme();
        setFirstExecutionSettings();
        if (settings.getBoolean("custom", true)) {loadCustom(settings.getString("text", "default"));}
        else {loadDefault();}
    }

    private void setFirstExecutionSettings()
    {
        SharedPreferences.Editor settingsEditor = settings.edit();
        if (settings.getString("firstExecution", "default").equals("default"))
        {
            settingsEditor.putString("firstExecution", "NOPE");
            settingsEditor.putString("text", "");
            settingsEditor.putBoolean("custom", false);
            settingsEditor.putBoolean("dark", true);
            settingsEditor.putBoolean("audio", false);
            settingsEditor.apply();
        }
    }
    private void settings()
    {
        Intent settings = new Intent(this, SettingsActivity.class);
        startActivity(settings);
    }
    private void spinImage()
    {
        spinner.setInterpolator(new LinearInterpolator());
        spinner.setDuration(1200);
        spinner.setRepeatCount(Animation.INFINITE);
        loading.startAnimation(spinner);
    }

    private void showToolbarHideKeyboard()
    {
        LLtoolbarLayout.setVisibility(View.VISIBLE);
        LLsearchLayout.setVisibility(View.INVISIBLE);
        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
        ETsearch.clearFocus();
        keyboard.hideSoftInputFromWindow(ETsearch.getWindowToken(), 0);
    }

    private void search()
    {
        LLtoolbarLayout.setVisibility(View.INVISIBLE);
        LLsearchLayout.setVisibility(View.VISIBLE);
        toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateInterpolator()).start();
        ETsearch.setText("");
        if (ETsearch.requestFocus()) {keyboard.showSoftInput(ETsearch, InputMethodManager.SHOW_IMPLICIT);}
    }

    public void loadCustom(String custom)
    {
        loadingText.setText(getResources().getString(R.string.loading_results_for)+" '"+custom+"' ...");
        AsyncListLoader loader = new AsyncListLoader();
        loader.setQuery(custom);
        loader.execute();
    }
    public void loadDefault()
    {
        clearList();
        loadingText.setText(getResources().getString(R.string.loading_popular_videos_of_today));
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
            checkTheme();
            search();
            return true;
        }
        if (id == R.id.action_settings)
        {
            settings();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        if (LLsearchLayout.isShown()){ showToolbarHideKeyboard();}
        else {super.onBackPressed();}
    }

    private class AsyncListLoader extends AsyncTask<String, Void, Integer>
    {
        private String query = "";
        private API API = new API();
        void setQuery(String query) {this.query = query;}
        protected void onPreExecute()
        {
            spinImage();
            loading.setVisibility(View.VISIBLE);
            loadingText.setVisibility(View.VISIBLE);
            TVsplitbar1.setVisibility(View.VISIBLE);
            TVsplitbar2.setVisibility(View.VISIBLE);
            TVsplitbar3.setVisibility(View.VISIBLE);
        }
        protected Integer doInBackground(String... params)
        {
            while (items.size()< API.MAX_ITEMS_RETURNED)
            {
                if (query.equals("")) {items.addAll(API.getMusicChannelVideos());}
                else {items.addAll(API.searchVideos(query));}
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
                TVsplitbar1.setVisibility(View.INVISIBLE);
                TVsplitbar2.setVisibility(View.INVISIBLE);
                TVsplitbar3.setVisibility(View.INVISIBLE);
                LVadapter.notifyDataSetChanged();
            }
            query = "";
        }
    }
    public void checkTheme()
    {
        if (settings.getBoolean("dark", true)) {setDarkTheme();}
        else {setLightTheme();}
    }

    private void setDarkTheme()
    {
        LLmain.setBackgroundResource(R.color.GREY_BACKGROUND_DARK_SUPER);
        TVsplitbar1.setBackgroundResource(R.color.GREY_TEXT_LIGHT_SUPER);
        TVsplitbar2.setBackgroundResource(R.color.GREY_TEXT_LIGHT_SUPER);
        TVsplitbar3.setBackgroundResource(R.color.GREY_TEXT_LIGHT_SUPER);
        TVloading.setTextColor(getResources().getColor(R.color.GREY_TEXT_LIGHT_SUPER));
        LLsearchLayout.setBackgroundResource(R.color.GREY_BACKGROUND_DARK_SUPER);
        ETsearch.setTextColor(getResources().getColor(R.color.GREY_TEXT_LIGHT_SUPER));
        Picasso.with(this).load(R.drawable.back_48_lightgrey).fit().into(IVback);
    }
    private void setLightTheme()
    {
        LLmain.setBackgroundResource(R.color.GREY_TEXT_LIGHT_SUPER);
        TVsplitbar1.setBackgroundResource(R.color.GREY_BACKGROUND_DARK_SUPER);
        TVsplitbar2.setBackgroundResource(R.color.GREY_BACKGROUND_DARK_SUPER);
        TVsplitbar3.setBackgroundResource(R.color.GREY_BACKGROUND_DARK_SUPER);
        TVloading.setTextColor(getResources().getColor(R.color.GREY_BACKGROUND_DARK_SUPER));
        LLsearchLayout.setBackgroundResource(R.color.GREY_TEXT_LIGHT_SUPER);
        ETsearch.setTextColor(getResources().getColor(R.color.GREY_BACKGROUND_DARK_SUPER));
        Picasso.with(this).load(R.drawable.back_48_darkgrey).fit().into(IVback);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        LVadapter.notifyDataSetChanged();
    }
}
