package com.example.uge01006.converter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private ArrayList<Result> items;
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
        Result a = new Result();
        a.setTitle("David Guetta ft. Justin Bieber - 2U");
        a.setLength("3:27 minutes");
        items.add(a);

        Result b = new Result();
        b.setTitle("Lost Frequencies & Netsky - Here With You");
        b.setLength("Length 2:53");
        items.add(a);
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
