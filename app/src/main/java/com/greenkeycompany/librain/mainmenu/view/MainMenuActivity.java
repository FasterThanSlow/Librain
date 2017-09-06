package com.greenkeycompany.librain.mainmenu.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.greenkeycompany.librain.advice.view.AdviceActivity;
import com.greenkeycompany.librain.app.App;
import com.greenkeycompany.librain.app.util.PremiumUtil;
import com.greenkeycompany.librain.R;
import com.greenkeycompany.librain.app.RateDialog;
import com.greenkeycompany.librain.campaign.menu.view.CampaignMenuActivity;
import com.greenkeycompany.librain.purchase.PurchaseActivity;
import com.greenkeycompany.librain.dao.LevelDao;
import com.greenkeycompany.librain.rating.RatingGameActivity;
import com.greenkeycompany.librain.training.TrainingMenuActivity;

import org.solovyev.android.checkout.ActivityCheckout;
import org.solovyev.android.checkout.Checkout;
import org.solovyev.android.checkout.Inventory;
import org.solovyev.android.checkout.ProductTypes;

import javax.annotation.Nonnull;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainMenuActivity extends AppCompatActivity implements IMainMenuView {

    private ActivityCheckout checkout = Checkout.forActivity(this, App.get().getBilling());
    private PremiumUtil premiumUtil = App.get().getPremiumUtil();

    //private GoogleApiClient mGoogleApiClient;
    //private static final int REQUEST_LEADERBOARD = 300;

    @BindView(R.id.rating_button) TextView ratingTextView;
    @BindView(R.id.campaign_star_count_text_view) TextView campaignStarCountTextView;

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
                    Toast.makeText(MainMenuActivity.this, "Продукт не поддерживается", Toast.LENGTH_LONG).show();
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

        RateDialog rateDialog = new RateDialog(MainMenuActivity.this);
        if (rateDialog.isShouldShow()) {
            rateDialog.show();
        }
    }

    @Override
    public void updateRatingView(boolean isPremiumUser) {
        if (isPremiumUser) {
            //ratingTextView.setText();
            //ratingTextView.setBackgroundResource();
        } else {
            //ratingTextView.setText();
            //ratingTextView.setBackgroundResource();
        }
    }

    @Override
    public void updateCampaignStarView(int starCount) {
        campaignStarCountTextView.setText(String.valueOf(starCount));
    }

    @OnClick(R.id.campaign_button)
    public void onCampaignButtonClick() {
        startActivity(new Intent(this, CampaignMenuActivity.class));
    }

    @OnClick(R.id.rating_button)
    public void onRatingGameButtonClick() {
        if (premiumUtil.isPremiumUser()) {
            startActivity(new Intent(this, RatingGameActivity.class));
        } else {
            startActivity(new Intent(this, PurchaseActivity.class));
        }
    }

    @OnClick(R.id.training_button)
    public void onTrainingButtonClick() {
        startActivity(new Intent(this, TrainingMenuActivity.class));
    }

    @OnClick(R.id.advice_button)
    public void onAdviceButtonClick() {
        startActivity(new Intent(this, AdviceActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
       // mGoogleApiClient.connect();
    }

    private static final int PURCHASE_REQUEST = 900;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        checkout.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PURCHASE_REQUEST) {
            if (resultCode == RESULT_OK) {

            }
        }
    }

    @Override
    protected void onDestroy() {
        checkout.stop();
        super.onDestroy();
    }
}
