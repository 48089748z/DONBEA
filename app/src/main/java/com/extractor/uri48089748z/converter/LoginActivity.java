package com.extractor.uri48089748z.converter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {Thread.sleep(3000);}
        catch (InterruptedException e) {e.printStackTrace();}
        finally
        {
            Intent main = new Intent(this, MainActivity.class);
            startActivity(main);
        }
    }
}
