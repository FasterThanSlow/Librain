package com.greenkey.librain;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.greenkey.librain.entity.ItemType;
import com.greenkey.librain.entity.Rule;
import com.greenkey.librain.level.Generator;
import com.greenkey.librain.level.Level;
import com.greenkey.librain.level.RatingGameStage;
import com.greenkey.librain.level.Round;
import com.greenkey.librain.level.gameround.GameRound;
import com.greenkey.librain.level.gameround.SecondGameRound;
import com.greenkey.librain.level.gameround.ThirdGameRound;
import com.greenkey.librain.view.RatingBar;
import com.greenkey.librain.view.boardview.BoardView;
import com.greenkey.librain.view.distributorview.DistributorView;

import java.util.Arrays;
import java.util.Random;

public class RatingGameActivity extends AppCompatActivity {

    private static final int DEFAULT_STAGE_NUMBER = 1;
    private static final int DEFAULT_LIVES_COUNT = 3;

    private static final int STAGE_ANIMATION_DURATION = 1000;

    private int stageNumber;

    private ItemType[] trueAnswer;
    private RatingGameStage gameStage;

    private BoardView boardView;
    private DistributorView distributorView;

    private BoardView.BoardItemView selectedBoardItem;

    private Button checkResultButton;

    private View blackoutView;
    private View bottomBlackoutView;

    private RatingBar livesRatingBar;
    private TextView stageNumberTextView;

    private View stageView;
    private TextView stageTitleTextView;
    private TextView stageDescriptionTextView;

    private ImageView answerResultImageView;

    private ShowBoardItemsRunnable showBoardItemsRunnable;

    private Handler handler;

    private ObjectAnimator startStageAnimator;
    private ObjectAnimator endStageAnimator;

    private int distributorViewHeight;
    private int distributorViewWidth;
    private int boardViewWidth;
    private int boardItemViewWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating_game_activity);

        handler = new Handler();

        boardView = (BoardView) findViewById(R.id.board_view);
        distributorView = (DistributorView) findViewById(R.id.distributor_view);

        checkResultButton = (Button) findViewById(R.id.check_result_button);

        blackoutView = findViewById(R.id.blackout_view);
        bottomBlackoutView = findViewById(R.id.bottom_blackout_view);

        stageView = findViewById(R.id.rating_game_stage_view);
        stageTitleTextView = (TextView) stageView.findViewById(R.id.rating_game_stage_title_text_View);
        stageDescriptionTextView = (TextView) stageView.findViewById(R.id.rating_game_stage_description_text_View);

        livesRatingBar = (RatingBar) findViewById(R.id.rating_game_lives_rating_bar);
        stageNumberTextView = (TextView) findViewById(R.id.rating_game_stage_number_text_view);

        answerResultImageView = (ImageView) findViewById(R.id.rating_game_answer_result_image_view);

        startStageAnimator = ObjectAnimator.ofFloat(stageView, View.ALPHA, 0.0f, 1.0f);
        startStageAnimator.setDuration(STAGE_ANIMATION_DURATION);
        startStageAnimator.addListener(startStageAnimatorListener);

        endStageAnimator = ObjectAnimator.ofFloat(stageView, View.ALPHA, 0.0f, 1.0f);
        endStageAnimator.setDuration(STAGE_ANIMATION_DURATION);
        endStageAnimator.addListener(endStageAnimatorListener);

        boardView.setOnTouchListener(boardTouchListener); // Нажатие за пределами полей
        checkResultButton.setOnClickListener(checkResultOnClickListener);

        reset();
    }

    private void reset() {
        stageNumber = DEFAULT_STAGE_NUMBER;
        stageNumberTextView.setText(String.valueOf(stageNumber));

        livesRatingBar.setProgress(DEFAULT_LIVES_COUNT);

        stageView.setVisibility(View.INVISIBLE);
        distributorView.setVisibility(View.INVISIBLE);

        checkResultButton.setVisibility(View.INVISIBLE);
        blackoutView.setVisibility(View.INVISIBLE);
        bottomBlackoutView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //if ((resultDialog == null || ! resultDialog.isShowing()) && (pauseDialog == null || ! pauseDialog.isShowing())) {
            startStageAnimator.start();
        //}
    }

    private Animator.AnimatorListener startStageAnimatorListener = new Animator.AnimatorListener() {

        private boolean isAnimationPaused;

        @Override
        public void onAnimationStart(Animator animation) {

            isAnimationPaused = false;

            selectedBoardItem = null;

            boardView.setVisibility(View.INVISIBLE);
            blackoutView.setVisibility(View.INVISIBLE);
            bottomBlackoutView.setVisibility(View.INVISIBLE);

            stageView.setVisibility(View.VISIBLE);
            stageTitleTextView.setText("Волна " + (stageNumber));

            boardView.setItemsOnTouchListener(null);
            distributorView.setItemsOnTouchListener(null);

            boardView.removeItemsResources();

            distributorView.setVisibility(View.INVISIBLE);
            checkResultButton.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if ( ! isAnimationPaused) {

                stageView.setVisibility(View.INVISIBLE);
                boardView.setVisibility(View.VISIBLE);

                gameStage = Generator.createRatingStage(stageNumber);

                showBoardItemsRunnable = new ShowBoardItemsRunnable(gameStage.getShowingTime());

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

    private Animator.AnimatorListener endStageAnimatorListener = new Animator.AnimatorListener() {

        private boolean isAnimationPaused;
        private boolean isTrueAnswer;

        @Override
        public void onAnimationStart(Animator animation) {

            isAnimationPaused = false;

            stageView.setVisibility(View.INVISIBLE);
            boardView.setVisibility(View.INVISIBLE);
            checkResultButton.setVisibility(View.INVISIBLE);

            ItemType[] userAnswer = boardView.getItemsResources();
            if (Arrays.equals(userAnswer, trueAnswer)) {
                isTrueAnswer = true;

                answerResultImageView.setImageResource(R.drawable.game_round_right_answer);
            } else {
                isTrueAnswer = false;

                answerResultImageView.setImageResource(R.drawable.game_round_wrong_answer);
            }

            answerResultImageView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if ( ! isAnimationPaused) {

                answerResultImageView.setVisibility(View.INVISIBLE);

                if (isTrueAnswer) {
                    stageNumber++;

                    startStageAnimator.start();
                } else {
                    int livesCount = livesRatingBar.getProgress();
                    if (livesCount == 1) {
                        finish();
                    } else {
                        startStageAnimator.start();
                    }

                    livesRatingBar.setProgress(livesCount - 1);
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


    //Показ фигур на указанное время и дальнейшее их удаление c поля
    private class ShowBoardItemsRunnable implements Runnable {

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

        public ShowBoardItemsRunnable(int showTime) {
            this.showTime = showTime;

            trueAnswer = Generator.createFullBoardItems(gameStage.getRules(), gameStage.getRowCount() * gameStage.getColumnCount());

            boardView.createItems(gameStage.getRowCount(), gameStage.getColumnCount());
            boardView.setItemsResources(trueAnswer);

            distributorView.setItems(gameStage.getRules());
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
                    isRunning = false;

                    boardView.removeItemsResources();

                    boardView.setItemsOnTouchListener(boardItemsTouchListener);
                    distributorView.setItemsOnTouchListener(distributorItemsTouchListener);
                }
            }
        }
    }

    private View.OnClickListener checkResultOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            endStageAnimator.start();
        }
    };

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

                                selectedBoardItem = null;

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
}
