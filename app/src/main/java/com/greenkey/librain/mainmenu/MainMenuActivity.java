package com.greenkey.librain.mainmenu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.greenkey.librain.MyApplication;
import com.greenkey.librain.PremiumDialog;
import com.greenkey.librain.R;
import com.greenkey.librain.RateDialog;
import com.greenkey.librain.rating.RatingGameActivity;
import com.greenkey.librain.tutorial.TutorialActivity;
import com.greenkey.librain.campaign.CampaignMenuActivity;
import com.greenkey.librain.dao.LevelDao;
import com.greenkey.librain.training.TrainingMenuActivity;

import org.solovyev.android.checkout.ActivityCheckout;
import org.solovyev.android.checkout.Checkout;
import org.solovyev.android.checkout.EmptyRequestListener;
import org.solovyev.android.checkout.Inventory;
import org.solovyev.android.checkout.ProductTypes;
import org.solovyev.android.checkout.Purchase;

import javax.annotation.Nonnull;

public class MainMenuActivity extends AppCompatActivity {

    private static final String TUTORIAL_COMPLETED_KEY = "tutorial_completed_key";
    private static final boolean TUTORIAL_COMPLETED_DEFAULT_VALUE = false;

    private static final String PREMIUM_USER_SKU = "com.greenkey.premium";

    private final ActivityCheckout checkout = Checkout.forActivity(this, MyApplication.get().getBilling());
    private Inventory inventory;

    private boolean isPremiumUser;

    private class PurchaseListener extends EmptyRequestListener<Purchase> {
        @Override
        public void onSuccess(@Nonnull Purchase result) {
            Log.d("BILLING_TEST", "PurchaseListenerOnSuccess");

            isPremiumUser = true;

            Toast.makeText(MainMenuActivity.this, "You've bought the premium!", Toast.LENGTH_LONG).show();

            super.onSuccess(result);
        }

        @Override
        public void onError(int response, @Nonnull Exception e) {
            Log.d("BILLING_TEST", "PurchaseListenerOnError");

            Toast.makeText(MainMenuActivity.this, "Error!", Toast.LENGTH_LONG).show();

            super.onError(response, e);
        }
    }

    private class InventoryCallback implements Inventory.Callback {
        @Override
        public void onLoaded(@Nonnull Inventory.Products products) {
            Log.d("BILLING_TEST", "InventoryCallbackOnLoaded");

            final Inventory.Product product = products.get(ProductTypes.IN_APP);
            if ( ! product.supported) {
                Log.d("BILLING_TEST", "InventoryCallbackOnLoaded isNotSupported");

                isPremiumUser = false;
                return;
            }

            Log.d("BILLING_TEST", "InventoryCallbackOnLoaded isSupported");
            isPremiumUser = product.isPurchased(PREMIUM_USER_SKU);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_activity);

        final LevelDao levelDao = LevelDao.getInstance(MainMenuActivity.this);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainMenuActivity.this);

        checkout.start();

        inventory = checkout.makeInventory();
        inventory.load(Inventory.Request.create()
                .loadAllPurchases()
                .loadSkus(ProductTypes.IN_APP, PREMIUM_USER_SKU), new InventoryCallback());

        final View vdimZaeb = findViewById(R.id.sprint_vdim_zaebal_view);
        vdimZaeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPremiumUser) {

                    Toast.makeText(MainMenuActivity.this, "You are premium user yet!", Toast.LENGTH_LONG).show();

                } else {
                    PremiumDialog premiumDialog = new PremiumDialog(MainMenuActivity.this);
                    premiumDialog.setBuyOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //checkout.createPurchaseFlow(new PurchaseListener());
                            checkout.startPurchaseFlow(ProductTypes.IN_APP, PREMIUM_USER_SKU, null, new PurchaseListener());
                        }
                    });
                    premiumDialog.show();
                }
            }
        });

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
}
