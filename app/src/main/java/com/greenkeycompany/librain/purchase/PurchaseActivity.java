package com.greenkeycompany.librain.purchase;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.greenkeycompany.librain.R;
import com.greenkeycompany.librain.app.App;
import com.greenkeycompany.librain.app.PremiumUtil;

import org.solovyev.android.checkout.ActivityCheckout;
import org.solovyev.android.checkout.Checkout;
import org.solovyev.android.checkout.ProductTypes;
import org.solovyev.android.checkout.Purchase;
import org.solovyev.android.checkout.RequestListener;

import javax.annotation.Nonnull;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PurchaseActivity extends AppCompatActivity {

    private ActivityCheckout checkout = Checkout.forActivity(this, App.get().getBilling());
    private PremiumUtil premiumUtil = App.get().getPremiumUtil();

    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_activity);
        ButterKnife.bind(this);

        checkout.start();

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(R.string.premium_access);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.action_bar_back_icon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnClick(R.id.purchase_button)
    public void onPurchaseButtonClick() {
        checkout.startPurchaseFlow(ProductTypes.IN_APP, PremiumUtil.PREMIUM_USER_SKU, null, new RequestListener<Purchase>() {
            @Override
            public void onSuccess(@Nonnull Purchase result) {
                premiumUtil.setPremiumUser(true);
                Toast.makeText(PurchaseActivity.this, "ПРЕМИУМ!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(int response, @Nonnull Exception e) {
                Toast.makeText(PurchaseActivity.this, "НЕ ПОВЕЗЛО!", Toast.LENGTH_LONG).show();
            }
        });
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
}
