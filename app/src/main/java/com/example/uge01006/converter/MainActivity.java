package com.example.uge01006.converter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        items = new ArrayList<>();
        listViewAdapter = new ListViewAdapter(this, 0, items);
        listView = (ListView) this.findViewById(R.id.LVitems);
        listView.setAdapter(listViewAdapter);
        testItems();
    }

    public void testItems ()
    {
        VideoYoutube x = new VideoYoutube();
        x.setTitle("aaaaa");
        VideoYoutube y = new VideoYoutube();
        y.setTitle("bbbb");
        VideoYoutube z = new VideoYoutube();
        z.setTitle("cccccc");
        items.add(x);
        items.add(y);
        items.add(z);
        Log.e("ERROR", String.valueOf(items.size()));
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
