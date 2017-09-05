package com.greenkeycompany.librain.mainmenu;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.greenkeycompany.librain.app.App;
import com.greenkeycompany.librain.app.PremiumUtil;
import com.greenkeycompany.librain.R;
import com.greenkeycompany.librain.app.RateDialog;
import com.greenkeycompany.librain.advice.view.AdviceActivity;
import com.greenkeycompany.librain.campaign.menu.view.CampaignMenuActivity;
import com.greenkeycompany.librain.purchase.PurchaseActivity;
import com.greenkeycompany.librain.dao.LevelDao;
import com.greenkeycompany.librain.training.TrainingMenuActivity;

import org.solovyev.android.checkout.ActivityCheckout;
import org.solovyev.android.checkout.Checkout;
import org.solovyev.android.checkout.Inventory;
import org.solovyev.android.checkout.ProductTypes;

import javax.annotation.Nonnull;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainMenuActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private ActivityCheckout checkout = Checkout.forActivity(this, App.get().getBilling());
    private PremiumUtil premiumUtil = App.get().getPremiumUtil();

    //private GoogleApiClient mGoogleApiClient;
    //private static final int REQUEST_LEADERBOARD = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_activity);
        ButterKnife.bind(this);

        final LevelDao levelDao = LevelDao.getInstance(MainMenuActivity.this);

        Toast.makeText(this, "Премиум ли ты?[1]), " + premiumUtil.isPremiumUser(), Toast.LENGTH_LONG).show();

        checkout.start();
        checkout.loadInventory(Inventory.Request.create().loadAllPurchases(), new Inventory.Callback() {
            @Override
            public void onLoaded(@Nonnull Inventory.Products products) {
                final Inventory.Product product = products.get(ProductTypes.IN_APP);
                if ( ! product.supported) {
                    return;
                }

                premiumUtil.setPremiumUser(product.isPurchased(PremiumUtil.PREMIUM_USER_SKU));

                Toast.makeText(MainMenuActivity.this, "Премиум ли ты?[2]), " + premiumUtil.isPremiumUser(), Toast.LENGTH_LONG).show();
            }
        });

        /*
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();
                */

        final TextView campaignStarsCountTextView = (TextView) findViewById(R.id.main_stars_count_text_view);
        campaignStarsCountTextView.setText(String.valueOf(levelDao.getCompletedStarCount()));

        RateDialog rateDialog = new RateDialog(MainMenuActivity.this);
        if (rateDialog.isShouldShow()) {
            rateDialog.show();
        }
    }

    @OnClick(R.id.main_start_campaign_button)
    public void onCampaignButtonClick() {
        startActivity(new Intent(this, CampaignMenuActivity.class));
    }

    @OnClick(R.id.main_start_rating_game_button)
    public void onRatingGameButtonClick() {
        startActivity(new Intent(this, PurchaseActivity.class));
    }

    @OnClick(R.id.main_start_training_button)
    public void onTrainingButtonClick() {
        startActivity(new Intent(this, TrainingMenuActivity.class));
    }

    @OnClick(R.id.main_menu_advice_button)
    public void onAdviceButtonClick() {
        startActivity(new Intent(this, AdviceActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
       // mGoogleApiClient.connect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        checkout.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        checkout.stop();
        super.onDestroy();
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
