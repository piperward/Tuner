package com.example.ward.tuner;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.InputStream;
import java.util.Scanner;


public class LegalTextActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legal_text);
        setUpLicenseText();
    }

    private void setUpLicenseText()
    {
        InputStream inputStream = getResources().openRawResource(R.raw.apachelicense);
        Scanner in = new Scanner(inputStream);
        StringBuilder stringBuilder = new StringBuilder();
        TextView textView = (TextView)findViewById(R.id.licenseText);

        while(in.hasNextLine())
        {
            stringBuilder.append(in.nextLine()+"\n");
        }

        textView.setText(stringBuilder.toString());
    }
}
