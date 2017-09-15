package com.example.uge01006.converter;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class SettingsActivity extends AppCompatActivity
{
    private SharedPreferences settings;
    private SharedPreferences.Editor settingsEditor;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");
        settings = getSharedPreferences("settings", Context.MODE_PRIVATE);

    }

    public void setSettingsExample()
    {
        settingsEditor = settings.edit();
        settingsEditor.putBoolean("musicON", true);
        settingsEditor.putString("id", "Popular");
        settingsEditor.commit();
    }

    public void getSettingsExample()
    {
        settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
        if (settings.getBoolean("musicON", true))
        {
            //Si hay musica haz esto...
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_ok)
        {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
