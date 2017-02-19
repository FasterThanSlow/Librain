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

        final EditText rowCount = (EditText) findViewById(R.id.rowCountEditText);

        final EditText columnCount = (EditText) findViewById(R.id.columnCountEditText);

        final EditText itemsTypeCountEditText  = (EditText) findViewById(R.id.itemsTypeCountEditText);

        final AppCompatEditText itemsCountET = (AppCompatEditText) findViewById(R.id.itemsCount);

        Button startButton = (Button) findViewById(R.id.startBuuton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, MainActivity.class);

                intent.putExtra("rowCount", Integer.valueOf(rowCount.getText().toString()));
                intent.putExtra("columnCount", Integer.valueOf(columnCount.getText().toString()));
                intent.putExtra("itemsTypeCount", Integer.valueOf(itemsTypeCountEditText.getText().toString()));
                intent.putExtra("itemsCount", Integer.valueOf(itemsCountET.getText().toString()));

                startActivity(intent);
            }
        });

        Button gogoButton = (Button) findViewById(R.id.gogo);
        gogoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenuActivity.this, CampaignActivity.class));
            }
        });
    }
}
