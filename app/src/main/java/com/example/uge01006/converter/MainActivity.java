package com.example.uge01006.converter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.uge01006.converter.POJOs.VideoYoutube;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private ArrayList<VideoYoutube> items;
    private ListViewAdapter listViewAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        items = new ArrayList<>();
        listViewAdapter = new ListViewAdapter(this, 0, items);
        listView = (ListView) this.findViewById(R.id.LVitems);
        listView.setAdapter(listViewAdapter);

        testItems();
    }

    public void testItems()
    {
        VideoYoutube video = new VideoYoutube();
        video.setTitle("Martin Garrix - Pizza");
        video.setLikeCount("372.484");
        video.setViewCount("34.478.424");
        items.add(video);

        video.setTitle("David Guetta ft. Justin Bieber - 2U");
        video.setLikeCount("15.484");
        video.setViewCount("3.685.485");
        items.add(video);

        video.setTitle("Calvin Harris ft. Rihanna - This Is What You Came For");
        video.setLikeCount("2.147.484");
        video.setViewCount("1.373.685.485");
        items.add(video);
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
