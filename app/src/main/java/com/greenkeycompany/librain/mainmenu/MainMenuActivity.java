package com.greenkeycompany.librain.mainmenu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.greenkeycompany.librain.MyApplication;
import com.greenkeycompany.librain.PremiumHelper;
import com.greenkeycompany.librain.R;
import com.greenkeycompany.librain.RateDialog;
import com.greenkeycompany.librain.advice.view.AdviceActivity;
import com.greenkeycompany.librain.campaign.CampaignMenuActivity;
import com.greenkeycompany.librain.rating.RatingGameActivity;
import com.greenkeycompany.librain.dao.LevelDao;
import com.greenkeycompany.librain.training.TrainingMenuActivity;

import org.solovyev.android.checkout.ActivityCheckout;
import org.solovyev.android.checkout.Checkout;
import org.solovyev.android.checkout.Inventory;
import org.solovyev.android.checkout.ProductTypes;

import com.google.android.gms.games.Games;

import javax.annotation.Nonnull;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainMenuActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int REQUEST_LEADERBOARD = 300;
    private ActivityCheckout checkout;
    //private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_activity);
        ButterKnife.bind(this);

        final LevelDao levelDao = LevelDao.getInstance(MainMenuActivity.this);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainMenuActivity.this);

        checkout = Checkout.forActivity(this, MyApplication.getInstance().getBilling());
        checkout.start();

        Inventory inventory = checkout.loadInventory(Inventory.Request.create().
                loadAllPurchases(), new Inventory.Callback() {
            @Override
            public void onLoaded(@Nonnull Inventory.Products products) {
                final Inventory.Product product = products.get(ProductTypes.IN_APP);
                if ( ! product.supported) {
                    return;
                }

                boolean isPremiumUser = product.isPurchased(PremiumHelper.PREMIUM_USER_SKU);
                if (isPremiumUser) {

                }

                PremiumHelper.setIsPremiumUser(isPremiumUser);
            }
        });

        inventory.cancel();

        /*
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();
                */

        final TextView startCampaignButton = (TextView) findViewById(R.id.main_start_campaign_button);
        startCampaignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient,
                        getString(R.string.leaderboard_librain_raiting)), REQUEST_LEADERBOARD);*/
                startActivity(new Intent(MainMenuActivity.this, CampaignMenuActivity.class));

            }
        });

        final TextView campaignStarsCountTextView = (TextView) findViewById(R.id.main_stars_count_text_view);
        campaignStarsCountTextView.setText(String.valueOf(levelDao.getCompletedStarCount()));

        final TextView startRatingGameButton = (TextView) findViewById(R.id.main_start_rating_game_button);
        if (PremiumHelper.isPremiumUser()) {
            startRatingGameButton.setBackgroundResource(R.color.campaign_item_premium_disabled_background_color);
            startRatingGameButton.setText(R.string.main_menu_activate);
        }
        startRatingGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PremiumHelper.isPremiumUser()) {
                    startActivity(new Intent(MainMenuActivity.this, RatingGameActivity.class));
                } else {
                    PremiumHelper.PremiumDialog premiumDialog = new PremiumHelper.PremiumDialog(MainMenuActivity.this, checkout);
                    premiumDialog.show();
                }
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

    @OnClick(R.id.main_menu_advice_button)
    public void onAdviceButtonClick() {
        Log.d("onAdviceButtonClick", "onAdviceButtonClick");
        startActivity(new Intent(MainMenuActivity.this, AdviceActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
       // mGoogleApiClient.connect();
    }

    @Override
    protected void onDestroy() {
        checkout.stop();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        checkout.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Toast.makeText(this, "Google Play Services успешно подключён", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        //mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Ошибка с подключением Google Play Services", Toast.LENGTH_SHORT).show();
    }
}
