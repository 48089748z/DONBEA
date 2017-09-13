package com.example.uge01006.converter;
import android.app.ActionBar;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        LVlist = (ListView) this.findViewById(R.id.LVitems);
        ETsearch = (EditText) this.findViewById(R.id.ETsearch);
        IVback = (ImageView) this.findViewById(R.id.IVback);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        IVback.setOnClickListener(view -> {
            ETsearch.setVisibility(View.INVISIBLE);
            IVback.setVisibility(View.INVISIBLE);
            toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
        });

        setSupportActionBar(toolbar);
        ETsearch.setVisibility(View.INVISIBLE);
        items = new ArrayList<>();
        LVadapter = new ListViewAdapter(this, 0, items);
        LVlist.setAdapter(LVadapter);
        loadPopularMusic();
    }

    public void loadPopularMusic()
    {
        clearList();
        getSupportActionBar().setTitle("Popular Videos Today");
        AsyncListLoader async = new AsyncListLoader();
        async.execute();
    }

    private void clearList()
    {
        items.clear();
        LVadapter.clear();
        LVlist.setAdapter(null);
        LVlist.setAdapter(LVadapter);
    }

    class AsyncListLoader extends AsyncTask<String, Void, Integer>
    {
        private String query = "";
        private YoutubeAPI youtubeAPI = new YoutubeAPI();
        public void setQuery(String query) {this.query = query;}
        protected void onPreExecute()
        {
            /**Mostrar barra de carga*/
        }
        protected Integer doInBackground(String... params)
        {
            /**Actualizar barra de carga*/
            while (items.size()<youtubeAPI.MAX_ITEMS_RETURNED)
            {
                if (query == "") {items.addAll(youtubeAPI.getMusicChannelVideos());}
                else {items.addAll(youtubeAPI.searchVideos(query));}
            }
            return 0;
        }
        protected void onPostExecute(Integer result)
        {
           /**Quitar barra de carga*/
            if (result == 0) {LVadapter.notifyDataSetChanged();}
            query = "";
        }
    }

    private void search()
    {
        ETsearch.setText("");
        ETsearch.setVisibility(View.VISIBLE);
        ETsearch.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(ETsearch, InputMethodManager.SHOW_IMPLICIT);
        IVback.setVisibility(View.VISIBLE);
        toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateInterpolator()).start();
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
            search();
            return true;
        }
        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
