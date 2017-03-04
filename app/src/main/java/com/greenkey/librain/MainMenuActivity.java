package com.greenkey.librain;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.greenkey.librain.campaign.CampaignActivity;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Button gogoButton = (Button) findViewById(R.id.gogo);
        gogoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenuActivity.this, CampaignActivity.class));
            }
        });

        final Button startTutorialButton = (Button) findViewById(R.id.startTutorialButton);
        startTutorialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenuActivity.this, TutorialActivity.class));
            }
        });
    }
}
