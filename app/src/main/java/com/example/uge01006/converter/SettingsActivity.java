package com.example.uge01006.converter;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
public class SettingsActivity extends AppCompatActivity
{
    private SharedPreferences settings;
    private SharedPreferences.Editor settingsEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

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
}
