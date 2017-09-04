package com.greenkeycompany.librain.campaign;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.greenkeycompany.librain.app.App;
import com.greenkeycompany.librain.PremiumHelper;
import com.greenkeycompany.librain.R;
import com.greenkeycompany.librain.dao.LevelDao;
import com.greenkeycompany.librain.entity.ItemType;
import com.greenkeycompany.librain.entity.Rule;
import com.greenkeycompany.librain.level.Generator;
import com.greenkeycompany.librain.level.Level;
import com.greenkeycompany.librain.level.Round;
import com.greenkeycompany.librain.level.gameround.GameRound;
import com.greenkeycompany.librain.level.gameround.SecondGameRound;
import com.greenkeycompany.librain.level.gameround.ThirdGameRound;
import com.greenkeycompany.librain.view.ratingbar.RatingBar;
import com.greenkeycompany.librain.view.boardview.BoardView;
import com.greenkeycompany.librain.view.distributorview.DistributorView;

import org.solovyev.android.checkout.ActivityCheckout;
import org.solovyev.android.checkout.Checkout;
import org.solovyev.android.checkout.Purchase;

import java.util.Arrays;

import javax.annotation.Nonnull;

public class CampaignGameActivity extends AppCompatActivity {

    private static final String LEVEL_PARAM = "level";

    private static final int START_ROUND_ANIMATION_DURATION = 1500;
    private static final int END_ROUND_ANIMATION_DURATION = 1000;

    private static final int THIRD_ROUND_ANIMATION_DURATION = 800;

    private static final int ROUNDS_COUNT = 3;
    private int currentRound;

    private LevelDao levelDao;

    private ActivityCheckout checkout;

    private Level currentLevel;

    private int record;
    private int currentScore;

    private int levelId;

    private RatingBar ratingBar;
    private TextView levelNumberTextView;

    private View roundView;
    private ImageView roundImageView;
    private TextView roundTitleTextView;
    private TextView roundDescriptionTextView;
    private View roundLineView;

    private TextView checkResultButton;

    private View blackoutView;
    private View bottomBlackoutView;

    private BoardView boardView;
    private DistributorView distributorView;

    private Round firstRound;
    private Round secondRound;
    private Round thirdRoundLevelDescription;

    private Level.LevelType levelType;

    private GameRound currentGameRound;

    private Handler handler = new Handler();

    private ObjectAnimator startRoundAnimator;
    private ObjectAnimator endRoundAnimator;
    private ObjectAnimator thirdRoundAnimator;

    private ShowBoardItemRunnable showBoardItemsRunnable;

    private int distributorViewHeight;
    private int distributorViewWidth;

    private int boardViewWidth;
    private int boardItemViewWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);

        checkout = Checkout.forActivity(this, App.get().getBilling());
        checkout.start();

        levelDao = LevelDao.getInstance(CampaignGameActivity.this);

        boardView = (BoardView) findViewById(R.id.board_view);
        distributorView = (DistributorView) findViewById(R.id.hidden_stuff);

        blackoutView = findViewById(R.id.tutorial_blackout_view);
        bottomBlackoutView = findViewById(R.id.tutorial_bottom_blackout_view);

        ratingBar = (RatingBar) findViewById(R.id.stars);
        levelNumberTextView = (TextView) findViewById(R.id.header_level_number_text_view);
        checkResultButton = (TextView) findViewById(R.id.check_result_button);

        roundView = findViewById(R.id.game_round_banner);
        roundImageView = (ImageView) roundView.findViewById(R.id.tutorial_third_round_image_view);
        roundTitleTextView = (TextView) roundView.findViewById(R.id.game_round_title_text_view);
        roundDescriptionTextView = (TextView) roundView.findViewById(R.id.game_round_description_text_view);
        roundLineView = roundView.findViewById(R.id.game_round_line);

        startRoundAnimator = ObjectAnimator.ofFloat(roundView, View.ALPHA, 0.0f, 1.0f);
        startRoundAnimator.setDuration(START_ROUND_ANIMATION_DURATION);
        startRoundAnimator.addListener(startRoundAnimatorListener);

        endRoundAnimator = ObjectAnimator.ofFloat(roundView, View.ALPHA, 0.0f, 1.0f);
        endRoundAnimator.setDuration(END_ROUND_ANIMATION_DURATION);
        endRoundAnimator.addListener(endRoundAnimatorListener);

        thirdRoundAnimator = ObjectAnimator.ofFloat(roundView, View.ALPHA, 0.0f, 1.0f);
        thirdRoundAnimator.setDuration(THIRD_ROUND_ANIMATION_DURATION);
        thirdRoundAnimator.addListener(thirdRoundAnimatorListener);

        currentLevel = getIntent().getParcelableExtra(LEVEL_PARAM);
        if (currentLevel != null) {
            resetLevelProgress();
            setCurrentLevel(currentLevel);
        } else {
            return;
        }

        boardView.setOnTouchListener(boardTouchListener); // Нажатие за пределами полей
        checkResultButton.setOnClickListener(checkResultOnClickListener);

        final ImageView restartButton = (ImageView) findViewById(R.id.restart_image_view);
        restartButton.setOnClickListener(restartOnClickListener);
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

    private void setCurrentLevel(Level level) {
        levelId = level.getLevelId();
        levelNumberTextView.setText(String.valueOf(levelId));

        record = level.getRecord();

        firstRound = level.getFirstRound();
        secondRound = level.getSecondRound();
        thirdRoundLevelDescription = level.getThirdRound();

        levelType = level.getLevelType();
    }

    private void resetLevelProgress() {
        ratingBar.setProgress(0);
        ratingBar.setSelectedIndex(0);

        distributorViewWidth = 0;
        distributorViewHeight = 0;

        currentScore = 0;
        currentRound = 1;
    }

    //Показ фигур на указанное время и дальнейшее их удаление c поля
    private class ShowBoardItemRunnable implements Runnable {

        private static final int DEFAULT_DELAY = 100;

        private volatile boolean isRunning;
        public boolean isRunning() {
            return isRunning;
        }

        private boolean isCancelled;

        private int currentPlayTime;
        public int getCurrentPlayTime() {
            return currentPlayTime;
        }

        public void setCurrentPlayTime(int currentPlayTime) {
            this.currentPlayTime = currentPlayTime;
        }

        public void cancel() {
            isCancelled = true;
            currentPlayTime = 0;
        }

        private final int showTime;

        private final int roundNumber;

        private boolean isSecondRoundFirstPartShowing;
        private boolean isThirdRoundFirstPartShowing;

        public ShowBoardItemRunnable(int showTime, int roundNumber) {
            this.showTime = showTime;
            this.roundNumber = roundNumber;

            Rule[] rules = null;

            switch (roundNumber) {
                case 1:
                    rules = Generator.createRules(levelType, firstRound.getRoundItems());

                    currentGameRound = Generator.createRound1Items(rules, firstRound.getRowCount() * firstRound.getColumnCount());
                    boardView.setItemsResources(currentGameRound.getAnswer());

                    break;
                case 2:
                    rules = Generator.createRules(levelType, secondRound.getRoundItems());

                    isSecondRoundFirstPartShowing = true;
                    currentGameRound = Generator.createRound2Items(rules, secondRound.getRowCount() * secondRound.getColumnCount());
                    boardView.setItemsResources(((SecondGameRound)currentGameRound).getFirstPart());

                    break;
                case 3:
                    rules = Generator.createRules(levelType, thirdRoundLevelDescription.getRoundItems());

                    isThirdRoundFirstPartShowing = true;
                    currentGameRound = Generator.createRound3Items(rules, thirdRoundLevelDescription.getRowCount() * thirdRoundLevelDescription.getColumnCount());
                    boardView.setItemsResources(((ThirdGameRound) currentGameRound).getFirstPart());
                    break;
            }
            if (rules != null) {
                distributorView.setItems(rules);
            }
        }

        @Override
        public void run() {
            if (isCancelled) {
                isRunning = false;
                isCancelled = false;
            } else {
                if (currentPlayTime < showTime) {

                    currentPlayTime += DEFAULT_DELAY;

                    isRunning = true;

                    handler.postDelayed(this, DEFAULT_DELAY);
                } else {
                    switch (roundNumber) {
                        case 1:
                            isRunning = false;

                            boardView.removeItemsResources();

                            boardView.setItemsOnTouchListener(boardItemsTouchListener);
                            distributorView.setItemsOnTouchListener(distributorItemsTouchListener);
                            break;

                        case 2:
                            if (isSecondRoundFirstPartShowing) {
                                isSecondRoundFirstPartShowing = false;
                                currentPlayTime = 0;
                                boardView.setItemsResources(((SecondGameRound)currentGameRound).getSecondPart());

                                handler.post(this);
                            } else {
                                isRunning = false;

                                boardView.removeItemsResources();

                                boardView.setItemsOnTouchListener(boardItemsTouchListener);
                                distributorView.setItemsOnTouchListener(distributorItemsTouchListener);
                            }
                            break;
                        case 3:
                            if (isThirdRoundFirstPartShowing) {
                                isThirdRoundFirstPartShowing = false;
                                currentPlayTime = 0;
                                boardView.setItemsResources(((ThirdGameRound)currentGameRound).getSecondPart());

                                handler.post(this);
                            } else {
                                isRunning = false;

                                boardView.removeItemsResources();

                                thirdRoundAnimator.start();
                            }
                            break;
                    }
                }
            }
        }
    }

    private Animator.AnimatorListener thirdRoundAnimatorListener = new Animator.AnimatorListener() {

        private boolean isAnimationPaused;

        @Override
        public void onAnimationStart(Animator animation) {
            isAnimationPaused = false;

            if (currentGameRound instanceof ThirdGameRound) {
                roundView.setVisibility(View.VISIBLE);
                //boardView.setVisibility(View.INVISIBLE);
                roundImageView.setVisibility(View.VISIBLE);
                roundLineView.setVisibility(View.INVISIBLE);

                if (((ThirdGameRound)currentGameRound).getTrueAnswerPart() == 1) {
                    roundImageView.setImageResource(R.drawable.game_round_three_show_first);
                } else {
                    roundImageView.setImageResource(R.drawable.game_round_three_show_second);
                }

                roundTitleTextView.setVisibility(View.GONE);
                roundDescriptionTextView.setVisibility(View.GONE);
                //roundTitleTextView.setText("Покажи мне");
            }
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if ( ! isAnimationPaused) {
                roundView.setVisibility(View.INVISIBLE);
                boardView.setVisibility(View.VISIBLE);

                boardView.setItemsOnTouchListener(boardItemsTouchListener);
                distributorView.setItemsOnTouchListener(distributorItemsTouchListener);
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            isAnimationPaused = true;
        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    private Animator.AnimatorListener startRoundAnimatorListener = new Animator.AnimatorListener() {

        private boolean isAnimationPaused;

        @Override
        public void onAnimationStart(Animator animation) {

            isAnimationPaused = false;

            blackoutView.setVisibility(View.INVISIBLE);
            bottomBlackoutView.setVisibility(View.INVISIBLE);

            roundImageView.setVisibility(View.GONE);
            roundTitleTextView.setVisibility(View.VISIBLE);
            roundLineView.setVisibility(View.VISIBLE);
            roundDescriptionTextView.setVisibility(View.VISIBLE);
            roundView.setVisibility(View.VISIBLE);

            boardView.setVisibility(View.INVISIBLE);

            switch (currentRound) {
                case 1:
                    roundTitleTextView.setText(R.string.game_round_1_title);
                    roundDescriptionTextView.setText(R.string.game_round_1_description);

                    boardView.createItems(firstRound.getRowCount(), firstRound.getColumnCount());
                    break;
                case 2:
                    roundTitleTextView.setText(R.string.game_round_2_title);
                    roundDescriptionTextView.setText(R.string.game_round_2_description);

                    boardView.createItems(secondRound.getRowCount(), secondRound.getColumnCount());
                    break;
                case 3:
                    roundTitleTextView.setText(R.string.game_round_3_title);
                    roundDescriptionTextView.setText(R.string.game_round_3_description);

                    boardView.createItems(thirdRoundLevelDescription.getRowCount(), thirdRoundLevelDescription.getColumnCount());
                    break;
            }

            boardView.setItemsOnTouchListener(null);
            distributorView.setItemsOnTouchListener(null);

            boardView.removeItemsResources();

            distributorView.setVisibility(View.INVISIBLE);
            checkResultButton.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if ( ! isAnimationPaused) {

                roundView.setVisibility(View.INVISIBLE);
                boardView.setVisibility(View.VISIBLE);

                switch (currentRound) {
                    case 1:
                        showBoardItemsRunnable = new ShowBoardItemRunnable(firstRound.getShowingTime(), currentRound);
                        break;
                    case 2:
                        showBoardItemsRunnable = new ShowBoardItemRunnable(secondRound.getShowingTime(), currentRound);
                        break;
                    case 3:
                        showBoardItemsRunnable = new ShowBoardItemRunnable(thirdRoundLevelDescription.getShowingTime(), currentRound);
                        break;
                }

                handler.post(showBoardItemsRunnable);
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            isAnimationPaused = true;
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    };

    private Animator.AnimatorListener endRoundAnimatorListener = new Animator.AnimatorListener() {

        private boolean isAnimationPaused;
        private boolean isTrueAnswer;

        @Override
        public void onAnimationStart(Animator animation) {

            isAnimationPaused = false;

            roundTitleTextView.setVisibility(View.GONE);
            roundDescriptionTextView.setVisibility(View.GONE);
            roundImageView.setVisibility(View.VISIBLE);
            roundView.setVisibility(View.VISIBLE);
            roundLineView.setVisibility(View.INVISIBLE);
            boardView.setVisibility(View.INVISIBLE);

            checkResultButton.setVisibility(View.INVISIBLE);

            ItemType[] userAnswer = boardView.getItemsResources();
            if (Arrays.equals(userAnswer, currentGameRound.getAnswer())) {
                isTrueAnswer = true;

                roundImageView.setImageResource(R.drawable.game_round_right_answer);
            } else {
                isTrueAnswer = false;

                roundImageView.setImageResource(R.drawable.game_round_wrong_answer);
            }
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if ( ! isAnimationPaused) {
                currentRound++;

                if (isTrueAnswer) {
                    currentScore++;
                    ratingBar.setProgress(currentScore);
                }

                if (currentRound > ROUNDS_COUNT) {
                    ratingBar.setSelectedIndex(-1);
                } else {
                    if (isTrueAnswer) {
                        ratingBar.setSelectedIndex(currentRound - 1);
                    }
                }

                if (currentRound <= ROUNDS_COUNT && isTrueAnswer) {
                    startRoundAnimator.start();
                } else {
                    roundImageView.setVisibility(View.INVISIBLE);

                    showResultDialog();
                }
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            Log.d("Anim", "Cancel");
            isAnimationPaused = true;
        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };


    @Override
    public void onBackPressed() {
        showPauseDialog();
    }


    @Override
    protected void onResume() {
        super.onResume();

        if ((resultDialog == null || ! resultDialog.isShowing()) && (pauseDialog == null || ! pauseDialog.isShowing())) {
            startRoundAnimator.start();
        }
    }

    @Override
    protected void onPause() {
        if ((resultDialog == null || ! resultDialog.isShowing())) {
            if (pauseDialog == null || ! pauseDialog.isShowing()) {
                if (startRoundAnimator != null && startRoundAnimator.isRunning()) {
                    startRoundAnimator.cancel();
                }
                if (endRoundAnimator != null && endRoundAnimator.isRunning()) {
                    endRoundAnimator.cancel();
                }
                if (thirdRoundAnimator != null && thirdRoundAnimator.isRunning()) {
                    thirdRoundAnimator.cancel();
                }
                if (showBoardItemsRunnable != null && showBoardItemsRunnable.isRunning()) {
                    showBoardItemsRunnable.cancel();
                }

                resetLevelProgress();
            }

            if (currentScore > record) {
                currentLevel = levelDao.updateRecord(levelId, currentScore);
            }

            final Level nextLevel = levelDao.getLevel(levelId + 1);
            if (nextLevel != null && ! nextLevel.isEnabled()) {
                if (currentScore > 0) {
                    levelDao.unlockLevel(levelId + 1);
                }
            }
        }

        super.onPause();
    }

    private View.OnClickListener restartOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View restartButton) {
            restartButton.setClickable(false);

            if (startRoundAnimator.isRunning()) {
                startRoundAnimator.cancel();
            } else if (showBoardItemsRunnable.isRunning()) {
                showBoardItemsRunnable.cancel();
            } else if (endRoundAnimator.isRunning()) {
                endRoundAnimator.cancel();
            }

            resetLevelProgress();

            startRoundAnimator.start();

            restartButton.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (restartButton != null) {
                        restartButton.setClickable(true);
                    }
                }
            }, 1000);
        }
    };

    private View.OnClickListener checkResultOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            endRoundAnimator.start();
        }
    };


    //ВЗАИМОДЕЙСТВИЕ С ПРЕДМЕТАМИ (РАССТАНОВКА И УДАЛЕНИЕ КАРТОЧЕК)
    private BoardView.BoardItemView selectedBoardItem;

    private BoardTouchListenerWrapper boardTouchListener = new BoardTouchListenerWrapper();
    private class BoardTouchListenerWrapper implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                selectedBoardItem = null;

                blackoutView.setVisibility(View.INVISIBLE);
                bottomBlackoutView.setVisibility(View.INVISIBLE);

                distributorView.setVisibility(View.INVISIBLE);
            }
            return false;
        }
    }

    private BoardItemsTouchListenerWrapper boardItemsTouchListener = new BoardItemsTouchListenerWrapper();
    private class BoardItemsTouchListenerWrapper implements View.OnTouchListener {

        @Override
        public boolean onTouch(View touchView, MotionEvent event) {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                BoardView.BoardItemView boardItemView = (BoardView.BoardItemView) touchView;

                //init all sizes
                if (boardViewWidth == 0 || boardItemViewWidth == 0)  {
                    boardViewWidth = boardView.getWidth();
                    boardItemViewWidth = boardItemView.getWidth();
                }

                distributorViewHeight = distributorView.getHeight();
                distributorViewWidth = distributorView.getWidth();

                if (boardItemView.hasImageView()) {
                    Log.d("Lel", "Удаление фигурки с поля");

                    distributorView.addResource(boardItemView.getItemType());
                    distributorView.setVisibility(View.INVISIBLE);

                    boardItemView.removeImageView();

                    checkResultButton.setVisibility(View.INVISIBLE);
                } else {
                    if (boardItemView.isItemPressed()) { //Повторный клик по пустой клетке

                        Log.d("Lel", "Убрать инструменты");
                        distributorView.setVisibility(View.INVISIBLE);

                        blackoutView.setVisibility(View.INVISIBLE);
                        bottomBlackoutView.setVisibility(View.INVISIBLE);

                        selectedBoardItem = null;

                    } else { //Клик по пустой клетке
                        int unusedResourceTypesCount = distributorView.getUnusedResourceTypesCount();

                        switch (unusedResourceTypesCount) {
                            case 0:
                                //do nothing
                                break;
                            case 1:
                                Log.d("Lel", "Всего одна фигура");
                                DistributorView.DistributorItemView hiddenItem = distributorView.getItem(0);

                                boardItemView.createImageView(hiddenItem.getItemType());
                                hiddenItem.removeImageView();

                                if (distributorView.allItemsResourcesUsed()) {
                                    checkResultButton.setVisibility(View.VISIBLE);
                                } else {
                                    checkResultButton.setVisibility(View.INVISIBLE);
                                }
                                break;
                            default:
                                Log.d("Lel", "Показать инструменты");

                                blackoutView.setVisibility(View.VISIBLE);
                                bottomBlackoutView.setVisibility(View.VISIBLE);

                                selectedBoardItem = boardItemView;

                                float x = boardItemView.getX();
                                float y = boardItemView.getY();

                                if (x + boardItemViewWidth / 2 + distributorViewWidth / 2 >= boardViewWidth) {
                                    distributorView.setX(boardViewWidth - distributorViewWidth - 10);
                                } else if(x + boardItemViewWidth / 2 - distributorViewWidth / 2 <= 0) {
                                    distributorView.setX(10);
                                }else{
                                    distributorView.setX(x + boardItemViewWidth / 2 - distributorViewWidth / 2);
                                }

                                distributorView.setY(y - distributorViewHeight + distributorView.getTriangleViewSizePx() / 2);
                                distributorView.setTriangleOffset((int)(boardItemView.getX() - distributorView.getX() + boardItemViewWidth / 2 - distributorView.getTriangleViewSizePx() / 2));

                                distributorView.bringToFront();
                                distributorView.invalidate();
                                distributorView.setVisibility(View.VISIBLE);
                                break;
                        }
                    }
                }
            }

            return true;
        }
    }

    private DistributorItemsTouchListenerWrapper distributorItemsTouchListener = new DistributorItemsTouchListenerWrapper();
    private class DistributorItemsTouchListenerWrapper implements View.OnTouchListener {

        @Override
        public boolean onTouch(View touchView, MotionEvent event) {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                DistributorView.DistributorItemView distributorItemView = (DistributorView.DistributorItemView) touchView;

                selectedBoardItem.createImageView(distributorItemView.getItemType());

                distributorItemView.removeImageView();

                distributorView.setVisibility(View.INVISIBLE);

                blackoutView.setVisibility(View.INVISIBLE);
                bottomBlackoutView.setVisibility(View.INVISIBLE);

                if (distributorView.allItemsResourcesUsed()) {
                    checkResultButton.setVisibility(View.VISIBLE);
                } else {
                    checkResultButton.setVisibility(View.INVISIBLE);
                }
            }

            return true;
        }
    }


    //Result Dialog
    private AlertDialog resultDialog;

    private void showResultDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(CampaignGameActivity.this);

        final View dialogView = LayoutInflater.from(CampaignGameActivity.this).inflate(R.layout.result_dialog, null);

        final RatingBar ratingBar = (RatingBar) dialogView.findViewById(R.id.result_dialog_rating_bar);
        ratingBar.setProgress(currentScore);

        final ImageView levelsImageView = (ImageView) dialogView.findViewById(R.id.result_dialog_levels_image_view);
        levelsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultDialog.dismiss();

                setResult(RESULT_OK);

                finish();
            }
        });

        final ImageView restartImageView = (ImageView) dialogView.findViewById(R.id.result_dialog_restart_image_view);
        restartImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultDialog.dismiss();

                resetLevelProgress();
                startRoundAnimator.start();
            }
        });

        if (currentScore > record) {
            currentLevel = levelDao.updateRecord(levelId, currentScore);
        }

        final ImageView nextImageView = (ImageView) dialogView.findViewById(R.id.result_dialog_next_image_view);
        nextImageView.setBackgroundResource(R.drawable.dialog_orange_background_shape);
        nextImageView.setImageResource(R.drawable.result_dialog_next_icon);

        final Level nextLevel = levelDao.getLevel(levelId + 1);
        if (nextLevel == null) {
            nextImageView.setVisibility(View.GONE);
        } else {
            if (nextLevel.isEnabled()) {
                if (nextLevel.isPremium()) {
                    if (PremiumHelper.isPremiumUser()) {
                        nextImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                resultDialog.dismiss();

                                setCurrentLevel(nextLevel);
                                resetLevelProgress();

                                startRoundAnimator.start();
                            }
                        });
                    } else {
                        nextImageView.setBackgroundResource(R.drawable.dialog_green_background_shape);
                        nextImageView.setImageResource(R.drawable.premium_dialog_buy_icon);
                        nextImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                resultDialog.dismiss();

                                PremiumHelper.PremiumDialog premiumDialog = new PremiumHelper.PremiumDialog(CampaignGameActivity.this, checkout);
                                premiumDialog.setPurchaseListener(new PremiumHelper.PremiumDialogPurchaseListener() {
                                    @Override
                                    public void onSuccess(@Nonnull Purchase result) {
                                        super.onSuccess(result);

                                        Toast.makeText(CampaignGameActivity.this, R.string.premium_success_message, Toast.LENGTH_LONG).show();

                                        setCurrentLevel(nextLevel);
                                        resetLevelProgress();

                                        startRoundAnimator.start();
                                    }

                                    @Override
                                    public void onError(int response, @Nonnull Exception e) {
                                        super.onError(response, e);

                                        Toast.makeText(CampaignGameActivity.this, R.string.premium_error_message, Toast.LENGTH_LONG).show();

                                        showResultDialog();
                                    }
                                });
                                premiumDialog.show();
                            }
                        });
                    }
                }
            } else {
                if (currentScore > 0) {
                    final Level unlockedLevel = levelDao.unlockLevel(levelId + 1);
                    if (unlockedLevel.isPremium()) {
                        if (PremiumHelper.isPremiumUser()) {
                            nextImageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    resultDialog.dismiss();

                                    setCurrentLevel(unlockedLevel);
                                    resetLevelProgress();

                                    startRoundAnimator.start();
                                }
                            });
                        } else {
                            nextImageView.setBackgroundResource(R.drawable.dialog_green_background_shape);
                            nextImageView.setImageResource(R.drawable.premium_dialog_buy_icon);
                            nextImageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    resultDialog.dismiss();

                                    PremiumHelper.PremiumDialog premiumDialog = new PremiumHelper.PremiumDialog(CampaignGameActivity.this, checkout);
                                    premiumDialog.setPurchaseListener(new PremiumHelper.PremiumDialogPurchaseListener() {
                                        @Override
                                        public void onSuccess(@Nonnull Purchase result) {
                                            super.onSuccess(result);

                                            Toast.makeText(CampaignGameActivity.this, R.string.premium_success_message, Toast.LENGTH_LONG).show();

                                            setCurrentLevel(unlockedLevel);
                                            resetLevelProgress();

                                            startRoundAnimator.start();
                                        }

                                        @Override
                                        public void onError(int response, @Nonnull Exception e) {
                                            super.onError(response, e);

                                            Toast.makeText(CampaignGameActivity.this, R.string.premium_error_message, Toast.LENGTH_LONG).show();

                                            showResultDialog();
                                        }
                                    });
                                    premiumDialog.show();
                                }
                            });
                        }
                    } else {
                        nextImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                resultDialog.dismiss();

                                setCurrentLevel(unlockedLevel);
                                resetLevelProgress();

                                startRoundAnimator.start();
                            }
                        });
                    }
                } else {
                    nextImageView.setVisibility(View.GONE);
                }
            }
        }


        builder.setCancelable(false);
        builder.setView(dialogView);

        resultDialog = builder.create();
        resultDialog.show();
    }


    //Pause Dialog
    private AlertDialog pauseDialog;

    private boolean isContinuePressed;
    private enum GameState {START_ROUND_ANIMATION, SHOW_BOARD_ITEMS_ANIMATION,
        SHOW_THIRD_ROUND_CONDITION_ANIMATION, END_ROUND_ANIMATION, GAME};

    private GameState gameState;
    private int playTime = 0;

    private void showPauseDialog() {

        gameState = GameState.GAME;

        if (startRoundAnimator.isRunning()) {
            Log.d("Anim", "alphaAnimationIsStarted");

            gameState = GameState.START_ROUND_ANIMATION;
            startRoundAnimator.cancel();
        } else if (showBoardItemsRunnable.isRunning()) {
            Log.d("Anim", "showBoardItemsRunnable paused");

            gameState = GameState.SHOW_BOARD_ITEMS_ANIMATION;

            playTime = showBoardItemsRunnable.getCurrentPlayTime();
            showBoardItemsRunnable.cancel();

            boardView.setVisibility(View.INVISIBLE);
        } else if (thirdRoundAnimator.isRunning()) {
            Log.d("Anim", "thirdRoundAnimator");

            gameState = GameState.SHOW_THIRD_ROUND_CONDITION_ANIMATION;
            thirdRoundAnimator.cancel();
        } else if (endRoundAnimator.isRunning()) {
            Log.d("Anim", "endRoundAnimator");

            gameState = GameState.END_ROUND_ANIMATION;
            endRoundAnimator.cancel();
        }

        isContinuePressed = true; // Эмулиция продолжение на нажатие мимо диалога

        final AlertDialog.Builder builder = new AlertDialog.Builder(CampaignGameActivity.this);

        final View dialogView = LayoutInflater.from(CampaignGameActivity.this).inflate(R.layout.pause_dialog, null);
        final ImageView levelsTextView = (ImageView) dialogView.findViewById(R.id.pause_dialog_levels_image_view);
        levelsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isContinuePressed = false;
                pauseDialog.dismiss();

                setResult(RESULT_OK);

                finish();
            }
        });

        final ImageView continueTextView = (ImageView) dialogView.findViewById(R.id.pause_dialog_continue_text_view);
        continueTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isContinuePressed = true;
                pauseDialog.dismiss();
            }
        });

        builder.setView(dialogView);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (isContinuePressed) {
                    if (gameState == GameState.START_ROUND_ANIMATION) {
                        startRoundAnimator.start();
                    } else if (gameState == GameState.SHOW_BOARD_ITEMS_ANIMATION) {

                        Log.d("Anim", "showBoardItemsRunnable " + playTime);

                        showBoardItemsRunnable.setCurrentPlayTime(playTime);
                        handler.post(showBoardItemsRunnable);
                        Log.d("Anim", "showBoardItemsRunnable resumed");

                        boardView.setVisibility(View.VISIBLE);
                    }  else if (gameState == GameState.SHOW_THIRD_ROUND_CONDITION_ANIMATION) {
                        thirdRoundAnimator.start();
                    } else if (gameState == GameState.END_ROUND_ANIMATION) {
                        endRoundAnimator.start();
                    }
                }
            }
        });

        pauseDialog = builder.create();
        pauseDialog.show();
    }
}
