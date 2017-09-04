package com.greenkeycompany.librain.purchase;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.greenkeycompany.librain.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PurchaseActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_activity);
        ButterKnife.bind(this);

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(R.string.premium_access);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.action_bar_back_icon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
