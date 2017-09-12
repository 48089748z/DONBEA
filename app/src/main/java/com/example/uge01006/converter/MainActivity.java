package com.example.uge01006.converter;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

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
        items = new ArrayList<>();
        listViewAdapter = new ListViewAdapter(this, 0, items);
        listView = (ListView) this.findViewById(R.id.LVitems);
        listView.setAdapter(listViewAdapter);
        LoadListTask async = new LoadListTask();
        async.execute();
    }
    class LoadListTask extends AsyncTask<String, Void, Integer>
    {
        private String query = "Garrix";
        private YoutubeAPI youtubeAPI = new YoutubeAPI();
        protected void onPreExecute() {/*progressDialog.show();*/}
        protected Integer doInBackground(String... params)
        {
            while (items.size()<youtubeAPI.MAX_ITEMS_RETURNED) {items.addAll(youtubeAPI.searchVideos(query));}
            return 0;
        }
        protected void onPostExecute(Integer result)
        {
          /*  progressDialog.dismiss();*/
            if (result == 0) {listViewAdapter.notifyDataSetChanged();}
        }
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
