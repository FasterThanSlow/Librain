package com.greenkeycompany.librain.training.game.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.greenkeycompany.librain.R;
import com.greenkeycompany.librain.app.view.boardview.BoardView;
import com.greenkeycompany.librain.app.view.ratingbar.RatingBar;
import com.greenkeycompany.librain.entity.ItemType;
import com.greenkeycompany.librain.training.entity.TrainingConfig;
import com.greenkeycompany.librain.training.game.presenter.ITrainingGamePresenter;
import com.greenkeycompany.librain.training.game.presenter.TrainingGamePresenter;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tert0 on 08.09.2017.
 */

public class TrainingGameActivity extends MvpActivity<ITrainingGameView, ITrainingGamePresenter>
        implements ITrainingGameView {

    public static final String TRAINING_CONFIG_PARAM = "training_config";

    @NonNull
    @Override
    public ITrainingGamePresenter createPresenter() {
        return new TrainingGamePresenter();
    }

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.rating_bar) RatingBar ratingBar;

    @BindView(R.id.check_button) Button checkButton;

    @BindView(R.id.board_view) BoardView boardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.training_game_activity);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        presenter.init((TrainingConfig) getIntent().getParcelableExtra(TRAINING_CONFIG_PARAM));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.game_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.restart:
                //
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void initBoard(int rowCount, int columnCount) {
        boardView.createItems(rowCount, columnCount);
    }

    @Override
    public void setBoardItems(@NonNull ItemType[] items) {
        boardView.setItemsResources(items);
    }

    @Override
    public void initRatingBar(int max) {
        ratingBar.setMax(max);
    }

    @Override
    public void setRatingBarProgress(int progress) {
        ratingBar.setProgress(progress);
    }

    @Override
    public void setRatingBarSelected(int selectedIndex) {
        ratingBar.setSelectedIndex(selectedIndex);
    }

    @OnClick(R.id.check_button)
    public void onCheckButtonClick() {
        presenter.requestToCheckResult();
    }

    @Override
    public void setCheckButtonVisibility(boolean visible) {
        checkButton.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    private AlertDialog pauseDialog;

    private void showPauseDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final View view = LayoutInflater.from(this).inflate(R.layout.pause_dialog, null);

        final ImageView levelsTextView = ButterKnife.findById(view, R.id.pause_dialog_levels_image_view);
        levelsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseDialog.dismiss();

                finish();
            }
        });

        final ImageView continueTextView = ButterKnife.findById(view, R.id.pause_dialog_continue_image_view);
        continueTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseDialog.dismiss();
            }
        });

        builder.setView(view);

        pauseDialog = builder.create();
        pauseDialog.show();
    }
}
