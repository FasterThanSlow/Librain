package com.greenkeycompany.librain.training;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.greenkeycompany.librain.R;
import com.greenkeycompany.librain.entity.ItemType;
import com.greenkeycompany.librain.entity.Rule;
import com.greenkeycompany.librain.level.Generator;
import com.greenkeycompany.librain.level.Level;
import com.greenkeycompany.librain.level.gameround.GameRound;
import com.greenkeycompany.librain.level.gameround.SecondGameRound;
import com.greenkeycompany.librain.level.gameround.ThirdGameRound;
import com.greenkeycompany.librain.training.entity.TrainingConfig;
import com.greenkeycompany.librain.training.entity.TrainingLevel;
import com.greenkeycompany.librain.app.view.ratingbar.RatingBar;
import com.greenkeycompany.librain.app.view.boardview.BoardView;
import com.greenkeycompany.librain.app.view.distributorview.DistributorView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TrainingGameOLDActivity extends AppCompatActivity {

    private static final int showingTime = 1000;

    private static final int START_ROUND_ANIMATION_DURATION = 1500;
    private static final int END_ROUND_ANIMATION_DURATION = 1000;

    private static final int THIRD_ROUND_ANIMATION_DURATION = 800;

    private TrainingConfig config;
    public static final String TRAINING_CONFIG_PARAM = "training_config";
    private static final Level.LevelType levelType = Level.LevelType.FRUIT;

    private RatingBar ratingBar;

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

    private int[] items;

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

    private RoundNumber roundNumber;
    private enum RoundNumber {FIRST, SECOND, THIRD}


    private List<RoundNumber> trainingRoundNumbers;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.training_game_activity);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        boardView = (BoardView) findViewById(R.id.board_view);

        distributorView = (DistributorView) findViewById(R.id.hidden_stuff);

        blackoutView = findViewById(R.id.tutorial_blackout_view);
        bottomBlackoutView = findViewById(R.id.tutorial_bottom_blackout_view);

        ratingBar = (RatingBar) findViewById(R.id.rating_bar);
        checkResultButton = (TextView) findViewById(R.id.check_button);

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

        config = getIntent().getParcelableExtra(TRAINING_CONFIG_PARAM);
        boardView.createItems(config.getRowCount(), config.getColumnCount());

        trainingRoundNumbers = new ArrayList<>();
        if (config.isFirstRoundSelected()) trainingRoundNumbers.add(RoundNumber.FIRST);
        if (config.isSecondRoundSelected()) trainingRoundNumbers.add(RoundNumber.SECOND);
        if (config.isThirdRoundSelected()) trainingRoundNumbers.add(RoundNumber.THIRD);

        roundCount = trainingRoundNumbers.size();

        ratingBar.setMax(roundCount);

        items = Generator.createTrainingItems(config.getItemTypeCount(), config.getItemCount());

        resetLevelProgress();

        boardView.setOnTouchListener(boardTouchListener);
        checkResultButton.setOnClickListener(checkResultOnClickListener);
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

                if (startRoundAnimator.isRunning()) {
                    startRoundAnimator.cancel();
                } else if (showBoardItemsRunnable.isRunning()) {
                    showBoardItemsRunnable.cancel();
                } else if (endRoundAnimator.isRunning()) {
                    endRoundAnimator.cancel();
                }

                resetLevelProgress();

                startRoundAnimator.start();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private int currentScore;
    private int currentRoundIndex;

    private void resetLevelProgress() {
        ratingBar.setProgress(0);
        ratingBar.setSelectedIndex(0);

        distributorViewWidth = 0;
        distributorViewHeight = 0;

        currentScore = 0;
        currentRoundIndex = 0;
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

        private RoundNumber roundNumber;

        private boolean isSecondRoundFirstPartShowing;
        private boolean isThirdRoundFirstPartShowing;

        public ShowBoardItemRunnable(int showTime, RoundNumber roundNumber) {
            this.showTime = showTime;
            this.roundNumber = roundNumber;

            Rule[] rules = null;

            switch (roundNumber) {
                case FIRST:
                    rules = Generator.createRules(levelType, items);

                    currentGameRound = Generator.createRound1Items(rules, config.getRowCount() * config.getColumnCount());
                    boardView.setItemsResources(currentGameRound.getAnswer());
                    break;
                case SECOND:
                    rules = Generator.createRules(levelType, items);

                    isSecondRoundFirstPartShowing = true;
                    currentGameRound = Generator.createRound2Items(rules, config.getRowCount() * config.getColumnCount());
                    boardView.setItemsResources(((SecondGameRound)currentGameRound).getFirstPart());

                    break;
                case THIRD:
                    rules = Generator.createRules(levelType, items);

                    isThirdRoundFirstPartShowing = true;
                    currentGameRound = Generator.createRound3Items(rules, config.getRowCount() * config.getColumnCount());
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
                        case FIRST:
                            isRunning = false;

                            boardView.removeItemsResources();

                            boardView.setItemsOnTouchListener(boardItemsTouchListener);
                            distributorView.setItemsOnTouchListener(distributorItemsTouchListener);
                            break;

                        case SECOND:
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
                        case THIRD:
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
                roundImageView.setVisibility(View.VISIBLE);
                roundLineView.setVisibility(View.INVISIBLE);

                if (((ThirdGameRound)currentGameRound).getTrueAnswerPart() == 1) {
                    roundImageView.setImageResource(R.drawable.game_round_three_show_first);
                } else {
                    roundImageView.setImageResource(R.drawable.game_round_three_show_second);
                }

                roundTitleTextView.setVisibility(View.GONE);
                roundDescriptionTextView.setVisibility(View.GONE);
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

            roundNumber = trainingRoundNumbers.get(currentRoundIndex);

            switch (roundNumber) {
                case FIRST:
                    roundTitleTextView.setText(R.string.game_round_1_title);
                    roundDescriptionTextView.setText(R.string.game_round_1_description);
                    break;
                case SECOND:
                    roundTitleTextView.setText(R.string.game_round_2_title);
                    roundDescriptionTextView.setText(R.string.game_round_2_description);
                    break;
                case THIRD:
                    roundTitleTextView.setText(R.string.game_round_3_title);
                    roundDescriptionTextView.setText(R.string.game_round_3_description);
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

                showBoardItemsRunnable = new ShowBoardItemRunnable(showingTime, roundNumber);

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

    private int roundCount;

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
                currentRoundIndex++;

                if (isTrueAnswer) {
                    currentScore++;
                    ratingBar.setProgress(currentScore);
                }

                if (currentRoundIndex > roundCount - 1) {
                    ratingBar.setSelectedIndex(-1);
                } else {
                    if (isTrueAnswer) {
                        ratingBar.setSelectedIndex(currentRoundIndex);
                    }
                }

                if (currentRoundIndex < roundCount && isTrueAnswer) {
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
        }

        super.onPause();
    }


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

                //requestToUpdateViews all sizes
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(TrainingGameOLDActivity.this);

        final View dialogView = LayoutInflater.from(TrainingGameOLDActivity.this).inflate(R.layout.result_dialog, null);

        final RatingBar ratingBar = (RatingBar) dialogView.findViewById(R.id.result_dialog_rating_bar);
        ratingBar.setMax(roundCount);
        ratingBar.setProgress(currentScore);

        final ImageView levelsImageView = (ImageView) dialogView.findViewById(R.id.result_dialog_levels_image_view);
        levelsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultDialog.dismiss();

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

        final ImageView nextImageView = (ImageView) dialogView.findViewById(R.id.result_dialog_next_image_view);
        nextImageView.setVisibility(View.GONE);

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

        final AlertDialog.Builder builder = new AlertDialog.Builder(TrainingGameOLDActivity.this);

        final View dialogView = LayoutInflater.from(TrainingGameOLDActivity.this).inflate(R.layout.pause_dialog, null);
        final ImageView levelsTextView = (ImageView) dialogView.findViewById(R.id.pause_dialog_levels_image_view);
        levelsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isContinuePressed = false;
                pauseDialog.dismiss();

                finish();
            }
        });

        final ImageView continueTextView = (ImageView) dialogView.findViewById(R.id.pause_dialog_continue_image_view);
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
