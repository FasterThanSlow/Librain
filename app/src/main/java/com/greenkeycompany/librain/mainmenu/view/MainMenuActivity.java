package com.greenkeycompany.librain.mainmenu.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.greenkeycompany.librain.advice.view.AdviceActivity;
import com.greenkeycompany.librain.app.App;
import com.greenkeycompany.librain.app.util.GoogleApiUtil;
import com.greenkeycompany.librain.app.util.PremiumUtil;
import com.greenkeycompany.librain.R;
import com.greenkeycompany.librain.app.RateDialog;
import com.greenkeycompany.librain.campaign.menu.view.CampaignMenuActivity;
import com.greenkeycompany.librain.mainmenu.presenter.IMainMenuPresenter;
import com.greenkeycompany.librain.mainmenu.presenter.MainMenuPresenter;
import com.greenkeycompany.librain.purchase.PurchaseActivity;
import com.greenkeycompany.librain.dao.LevelDao;
import com.greenkeycompany.librain.rating.RatingGameActivity;
import com.greenkeycompany.librain.training.menu.view.TrainingMenuActivity;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;

import org.solovyev.android.checkout.ActivityCheckout;
import org.solovyev.android.checkout.Checkout;
import org.solovyev.android.checkout.Inventory;
import org.solovyev.android.checkout.ProductTypes;

import javax.annotation.Nonnull;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainMenuActivity extends MvpActivity<IMainMenuView, IMainMenuPresenter> implements IMainMenuView {

    @NonNull
    @Override
    public IMainMenuPresenter createPresenter() {
        return new MainMenuPresenter(LevelDao.getInstance(this));
    }

    private ActivityCheckout checkout = Checkout.forActivity(this, App.get().getBilling());
    private PremiumUtil premiumUtil = App.get().getPremiumUtil();

    //private GoogleApiClient googleApiClient;
    //private static final int REQUEST_LEADERBOARD = 300;

    @BindView(R.id.rating_button) TextView ratingTextView;
    @BindView(R.id.campaign_star_count_text_view) TextView campaignStarCountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_activity);
        ButterKnife.bind(this);

        Toast.makeText(this, "Премиум ли ты?[1]), " + premiumUtil.isPremiumUser(), Toast.LENGTH_LONG).show();

        checkout.start();
        checkout.loadInventory(Inventory.Request.create().loadAllPurchases(), new Inventory.Callback() {
            @Override
            public void onLoaded(@Nonnull Inventory.Products products) {
                final Inventory.Product product = products.get(ProductTypes.IN_APP);
                if ( ! product.supported) {
                    return;
                }

                boolean isPurchased = product.isPurchased(PremiumUtil.PREMIUM_USER_SKU);
                boolean isPremium = premiumUtil.isPremiumUser();
                if (isPurchased != isPremium) {
                    premiumUtil.setPremiumUser(isPurchased);
                    presenter.requestToUpdateRatingView(isPurchased);
                }

                Toast.makeText(MainMenuActivity.this, "Премиум ли ты?[2]), " + premiumUtil.isPremiumUser(), Toast.LENGTH_LONG).show();
            }
        });

        //googleApiClient = GoogleApiUtil.getGoogleApi(this);

        presenter.requestToUpdateRatingView(premiumUtil.isPremiumUser());
        presenter.requestToUpdateCampaignStarView();

        RateDialog rateDialog = new RateDialog(MainMenuActivity.this);
        if (rateDialog.isShouldShow()) {
            rateDialog.show();
        }
    }

    @Override
    public void updateRatingView(boolean isPremiumUser) {
        if (isPremiumUser) {
            ratingTextView.setText(R.string.rating_start);
            ratingTextView.setBackgroundResource(R.drawable.main_menu_button_color_selector);
        } else {
            ratingTextView.setText(R.string.rating_activate);
            ratingTextView.setBackgroundResource(R.drawable.main_menu_button_activate_premium_color_selector);
        }
    }

    @Override
    public void updateCampaignStarView(int starCount) {
        campaignStarCountTextView.setText(String.valueOf(starCount));
    }

    @OnClick(R.id.campaign_button)
    public void onCampaignButtonClick() {
        ///START FOR RESULT TO UPDATE STARS COUNT AFTER GAME
        startActivityForResult(new Intent(this, CampaignMenuActivity.class), CAMPAIGN_REQUEST_CODE);
    }

    @OnClick(R.id.rating_button)
    public void onRatingGameButtonClick() {
        if (premiumUtil.isPremiumUser()) {
            startActivity(new Intent(this, RatingGameActivity.class));
        } else {
            startActivityForResult(new Intent(this, PurchaseActivity.class), PURCHASE_REQUEST_CODE);
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

    /*
    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }
    */

    private static final int CAMPAIGN_REQUEST_CODE = 100;
    private static final int PURCHASE_REQUEST_CODE = 200;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        checkout.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PURCHASE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    premiumUtil.setPremiumUser(true);
                    presenter.requestToUpdateRatingView(true);
                }
                break;
            case CAMPAIGN_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    presenter.requestToUpdateCampaignStarView();
                    presenter.requestToUpdateRatingView(premiumUtil.isPremiumUser());
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        checkout.stop();
        super.onDestroy();
    }
}
