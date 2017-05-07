package com.greenkey.librain.mainmenu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.greenkey.librain.R;
import com.greenkey.librain.RateDialog;
import com.greenkey.librain.rating.RatingGameActivity;
import com.greenkey.librain.tutorial.TutorialActivity;
import com.greenkey.librain.campaign.CampaignMenuActivity;
import com.greenkey.librain.dao.LevelDao;
import com.greenkey.librain.training.TrainingMenuActivity;

public class MainMenuActivity extends AppCompatActivity {

    private static final String TUTORIAL_COMPLETED_KEY = "tutorial_completed_key";
    private static final boolean TUTORIAL_COMPLETED_DEFAULT_VALUE = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_activity);

        final LevelDao levelDao = LevelDao.getInstance(MainMenuActivity.this);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainMenuActivity.this);

        final TextView startCampaignButton = (TextView) findViewById(R.id.main_start_campaign_button);
        startCampaignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean isTutorialCompleted = sharedPreferences.getBoolean(TUTORIAL_COMPLETED_KEY, TUTORIAL_COMPLETED_DEFAULT_VALUE);

                if (isTutorialCompleted) {
                    startActivity(new Intent(MainMenuActivity.this, CampaignMenuActivity.class));
                } else {
                    startActivity(new Intent(MainMenuActivity.this, TutorialActivity.class));
                }
            }
        });

        final TextView campaignStarsCountTextView = (TextView) findViewById(R.id.main_stars_count_text_view);
        campaignStarsCountTextView.setText(String.valueOf(levelDao.getCompletedStarCount()));

        final TextView startRatingGameButton = (TextView) findViewById(R.id.main_start_rating_game_button);
        startRatingGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenuActivity.this, RatingGameActivity.class));
            }
        });

        final TextView startTrainingButton = (TextView) findViewById(R.id.main_start_training_button);
        startTrainingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenuActivity.this, TrainingMenuActivity.class));
            }
        });

        RateDialog rateDialog = new RateDialog(MainMenuActivity.this);
        if (rateDialog.isShouldShow()) {
            rateDialog.show();
        }
    }
}
