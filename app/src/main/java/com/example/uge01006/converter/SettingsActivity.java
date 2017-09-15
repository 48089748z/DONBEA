package com.example.uge01006.converter;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity
{
    private SharedPreferences settings;
    private SharedPreferences.Editor settingsEditor;
    private Toolbar toolbar;
    private Switch SWcustomSearch;
    private EditText ETcustomSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        SWcustomSearch = (Switch) this.findViewById(R.id.SWcustomSearch);
        ETcustomSearch = (EditText) this.findViewById(R.id.ETcustomSearch);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");
        settings = getSharedPreferences("settings", Context.MODE_PRIVATE);

        ETcustomSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable)
            {
                settingsEditor = settings.edit();
                settingsEditor.putString("text", ETcustomSearch.getText().toString());
                settingsEditor.apply();
            }
        });

        if (settings.getBoolean("custom", true))
        {
            ETcustomSearch.setVisibility(View.VISIBLE);
            SWcustomSearch.setChecked(true);
            ETcustomSearch.setText(settings.getString("text", "default"));
        }
        else
        {
            ETcustomSearch.setVisibility(View.INVISIBLE);
            SWcustomSearch.setChecked(false);
        }

        SWcustomSearch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
            {
                ETcustomSearch.setVisibility(View.VISIBLE);
                ETcustomSearch.setText(settings.getString("text", "default"));
                settingsEditor = settings.edit();
                settingsEditor.putBoolean("custom", true);
                settingsEditor.apply();
            }
            else
            {
                ETcustomSearch.setVisibility(View.INVISIBLE);
                settingsEditor = settings.edit();
                settingsEditor.putBoolean("custom", false);
                settingsEditor.apply();
            }
        });

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
        if (id == R.id.action_info)
        {
            //info();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
