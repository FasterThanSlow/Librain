package com.greenkeycompany.librain.purchase;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.greenkeycompany.librain.R;
import com.greenkeycompany.librain.app.App;
import com.greenkeycompany.librain.app.util.PremiumUtil;

import org.solovyev.android.checkout.ActivityCheckout;
import org.solovyev.android.checkout.Checkout;
import org.solovyev.android.checkout.ProductTypes;
import org.solovyev.android.checkout.Purchase;
import org.solovyev.android.checkout.RequestListener;

import java.util.List;

import javax.annotation.Nonnull;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PurchaseActivity extends AppCompatActivity {

    private ActivityCheckout checkout = Checkout.forActivity(this, App.get().getBilling());

    @BindView(R.id.toolbar) Toolbar toolbar;

    @BindView(R.id.purchase_premium_success_view) View premiumSuccessPurchasedView;
    @BindViews({
            R.id.purchase_premium_top_view,
            R.id.purchase_premium_features_view,
            R.id.purchase_premium_button
    }) List<View> purchaseViewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_activity);
        ButterKnife.bind(this);

        checkout.start();

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(R.string.purchase_premium_access);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.action_bar_back_icon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.purchase_premium_button)
    public void onPurchaseButtonClick() {
        checkout.startPurchaseFlow(ProductTypes.IN_APP, PremiumUtil.PREMIUM_USER_SKU, null, new RequestListener<Purchase>() {
            @Override
            public void onSuccess(@Nonnull Purchase result) {
                setResult(RESULT_OK);
                premiumSuccessPurchasedView.setVisibility(View.VISIBLE);
                for (View view : purchaseViewList) {
                    view.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(int response, @Nonnull Exception e) {
                Toast.makeText(PurchaseActivity.this, getString(R.string.purchase_error_message, response), Toast.LENGTH_LONG).show();
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
