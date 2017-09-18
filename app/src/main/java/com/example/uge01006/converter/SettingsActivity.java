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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
public class SettingsActivity extends AppCompatActivity
{
    private SharedPreferences settings;
    private SharedPreferences.Editor settingsEditor;
    private Toolbar toolbar;
    private Switch SWcustomSearch;
    private EditText ETcustomSearch;
    private ImageView IVbackSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        SWcustomSearch = (Switch) this.findViewById(R.id.SWcustomSearch);
        ETcustomSearch = (EditText) this.findViewById(R.id.ETcustomSearch);
        IVbackSettings = (ImageView) this.findViewById(R.id.IVbackSettings);
        setSupportActionBar(toolbar);
        settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
        IVbackSettings.setOnClickListener(view -> ETcustomSearch.setText(""));
        ETcustomSearch.addTextChangedListener(new TextWatcher()
        {
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
        SWcustomSearch.setOnCheckedChangeListener((buttonView, isChecked) ->
        {
            if (isChecked)
            {
                IVbackSettings.setVisibility(View.VISIBLE);
                ETcustomSearch.setVisibility(View.VISIBLE);
                ETcustomSearch.setText(settings.getString("text", "default"));
                settingsEditor = settings.edit();
                settingsEditor.putBoolean("custom", true);
                settingsEditor.apply();
            }
            else
            {
                IVbackSettings.setVisibility(View.INVISIBLE);
                ETcustomSearch.setVisibility(View.INVISIBLE);
                settingsEditor = settings.edit();
                settingsEditor.putBoolean("custom", false);
                settingsEditor.apply();
            }
        });
        if (settings.getBoolean("custom", true))
        {
            IVbackSettings.setVisibility(View.VISIBLE);
            ETcustomSearch.setVisibility(View.VISIBLE);
            SWcustomSearch.setChecked(true);
            ETcustomSearch.setText(settings.getString("text", "default"));
        }
        else
        {
            IVbackSettings.setVisibility(View.INVISIBLE);
            ETcustomSearch.setVisibility(View.INVISIBLE);
            SWcustomSearch.setChecked(false);
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
            if (ETcustomSearch.getText().toString().trim().isEmpty())
            {
                SWcustomSearch.setChecked(false);
            }
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
