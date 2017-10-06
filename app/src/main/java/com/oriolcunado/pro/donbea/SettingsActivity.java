package com.oriolcunado.pro.donbea;
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
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
public class SettingsActivity extends AppCompatActivity
{
    private SharedPreferences settings;
    private SharedPreferences.Editor settingsEditor;
    private Toolbar toolbar;
    private Switch SWcustomSearch;
    private EditText ETcustomSearch;
    private ImageView IVbackSettings;
    private Switch SWtheme;
    private LinearLayout LLsettings;
    private TextView TVsplitbar4;
    private TextView TVsplitbar5;
    private TextView TVsplitbar6;
    private TextView TVsplitbar16;
    private Switch SWdownloadOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.settings));
        settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
        settingsEditor = settings.edit();
        setSupportActionBar(toolbar);
        SWcustomSearch = (Switch) this.findViewById(R.id.SWcustomSearch);
        ETcustomSearch = (EditText) this.findViewById(R.id.ETcustomSearch);
        IVbackSettings = (ImageView) this.findViewById(R.id.IVbackSettings);
        SWtheme = (Switch) this.findViewById(R.id.SWtheme);
        LLsettings = (LinearLayout) this.findViewById(R.id.LLsettings);
        TVsplitbar4 = (TextView) this.findViewById(R.id.TVsplitbar4);
        TVsplitbar5 = (TextView) this.findViewById(R.id.TVsplitbar5);
        TVsplitbar6 = (TextView) this.findViewById(R.id.TVsplitbar6);
        TVsplitbar16 = (TextView) this.findViewById(R.id.TVsplitbar16);
        SWdownloadOptions = (Switch) this.findViewById(R.id.SWdownloadOptions);
        IVbackSettings.setOnClickListener(view -> ETcustomSearch.setText(""));
        configureCustomSearchSwitch();
        configureThemeSwitch();
        configureDownloadOptions();
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
    }
    public void configureDownloadOptions()
    {
        SWdownloadOptions.setOnCheckedChangeListener((compoundButton, isChecked) ->
        {
            if (isChecked)
            {
                settingsEditor.putBoolean("audio", true);
                settingsEditor.apply();
            }
            else
            {
                settingsEditor.putBoolean("audio", false);
                settingsEditor.apply();
            }

        });
        if (settings.getBoolean("audio", true)) {SWdownloadOptions.setChecked(true);}
        else {SWdownloadOptions.setChecked(false);}
    }
    public void configureThemeSwitch()
    {
        SWtheme.setOnCheckedChangeListener((compoundButton, isChecked) ->
        {
            if (isChecked)
            {
                settingsEditor.putBoolean("dark", true);
                settingsEditor.apply();
                setDarkTheme();
            }
            else
            {
                settingsEditor.putBoolean("dark", false);
                settingsEditor.apply();
                setLightTheme();
            }
        });
        if (settings.getBoolean("dark", true))
        {
            SWtheme.setChecked(true);
            setDarkTheme();
        }
        else
        {
            SWtheme.setChecked(false);
            setLightTheme();
        }
    }

    public void configureCustomSearchSwitch()
    {
        SWcustomSearch.setOnCheckedChangeListener((compoundButton, isChecked) ->
        {
            if (isChecked)
            {
                IVbackSettings.setVisibility(View.VISIBLE);
                ETcustomSearch.setVisibility(View.VISIBLE);
                ETcustomSearch.setText(settings.getString("text", "settings"));
                settingsEditor.putBoolean("custom", true);
                settingsEditor.apply();
            }
            else
            {
                IVbackSettings.setVisibility(View.INVISIBLE);
                ETcustomSearch.setVisibility(View.INVISIBLE);
                settingsEditor.putBoolean("custom", false);
                settingsEditor.apply();
            }
        });
        if (settings.getBoolean("custom", true))
        {
            IVbackSettings.setVisibility(View.VISIBLE);
            ETcustomSearch.setVisibility(View.VISIBLE);
            SWcustomSearch.setChecked(true);
            ETcustomSearch.setText(settings.getString("text", "settings"));
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
    private void setDarkTheme()
    {
        LLsettings.setBackgroundResource(R.color.GREY_BACKGROUND_DARK_SUPER);
        TVsplitbar4.setBackgroundResource(R.color.GREY_TEXT_LIGHT_SUPER);
        TVsplitbar5.setBackgroundResource(R.color.GREY_TEXT_LIGHT_SUPER);
        TVsplitbar6.setBackgroundResource(R.color.GREY_TEXT_LIGHT_SUPER);
        TVsplitbar16.setBackgroundResource(R.color.GREY_TEXT_LIGHT_SUPER);
        SWcustomSearch.setTextColor(getResources().getColor(R.color.GREY_TEXT_LIGHT_SUPER));
        SWtheme.setTextColor(getResources().getColor(R.color.GREY_TEXT_LIGHT_SUPER));
        SWdownloadOptions.setTextColor(getResources().getColor(R.color.GREY_TEXT_LIGHT_SUPER));
        ETcustomSearch.setTextColor(getResources().getColor(R.color.GREY_TEXT_LIGHT_SUPER));
        ETcustomSearch.setHintTextColor(getResources().getColor(R.color.GREY_TEXT_LIGHT));

    }
    private void setLightTheme()
    {
        LLsettings.setBackgroundResource(R.color.GREY_TEXT_LIGHT_SUPER);
        TVsplitbar4.setBackgroundResource(R.color.GREY_BACKGROUND_DARK_SUPER);
        TVsplitbar5.setBackgroundResource(R.color.GREY_BACKGROUND_DARK_SUPER);
        TVsplitbar6.setBackgroundResource(R.color.GREY_BACKGROUND_DARK_SUPER);
        TVsplitbar16.setBackgroundResource(R.color.GREY_BACKGROUND_DARK_SUPER);
        SWcustomSearch.setTextColor(getResources().getColor(R.color.GREY_BACKGROUND_DARK_SUPER));
        SWtheme.setTextColor(getResources().getColor(R.color.GREY_BACKGROUND_DARK_SUPER));
        SWdownloadOptions.setTextColor(getResources().getColor(R.color.GREY_BACKGROUND_DARK_SUPER));
        ETcustomSearch.setTextColor(getResources().getColor(R.color.GREY_BACKGROUND_DARK_SUPER));
        ETcustomSearch.setHintTextColor(getResources().getColor(R.color.GREY_TEXT_DARK));
    }
}
