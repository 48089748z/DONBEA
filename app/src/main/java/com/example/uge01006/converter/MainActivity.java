package com.example.uge01006.converter;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.uge01006.converter.DAOs.YoutubeAPI;
import com.example.uge01006.converter.POJOs.VideoYoutube;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private ArrayList<VideoYoutube> items;
    private ListViewAdapter listViewAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        //this.getSupportActionBar().setLogo(R.drawable.dislikes_24_red);

        items = new ArrayList<>();
        listViewAdapter = new ListViewAdapter(this, 0, items);
        listView = (ListView) this.findViewById(R.id.LVitems);
        listView.setAdapter(listViewAdapter);
        clearList();

        loadPopularMusic();
    }

    public void loadPopularMusic()
    {
        this.getSupportActionBar().setTitle("Popular Videos Today");
        AsyncListLoader async = new AsyncListLoader();
        async.execute();
    }

    private void clearList()
    {
        items.clear();
        listViewAdapter.clear();
        listView.setAdapter(null);
        listView.setAdapter(listViewAdapter);
    }

    class AsyncListLoader extends AsyncTask<String, Void, Integer>
    {
        private String query = "";
        private YoutubeAPI youtubeAPI = new YoutubeAPI();
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
            if (result == 0) {listViewAdapter.notifyDataSetChanged();}
            query = "";
        }
        public void setQuery(String query) {this.query = query;}
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
        if (id == R.id.action_search) {return true;}

        if (id == R.id.action_settings) {return true;}
        return super.onOptionsItemSelected(item);
    }

}
