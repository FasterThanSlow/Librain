package com.greenkey.librain;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.greenkey.librain.dao.LevelDao;
import com.greenkey.librain.entity.ItemType;
import com.greenkey.librain.entity.Rule;
import com.greenkey.librain.level.Generator;
import com.greenkey.librain.level.Level;
import com.greenkey.librain.level.gameround.GameRound;
import com.greenkey.librain.level.gameround.SecondGameRound;
import com.greenkey.librain.level.gameround.ThirdGameRound;
import com.greenkey.librain.view.RatingBar;
import com.greenkey.librain.view.boardview.BoardView;
import com.greenkey.librain.view.distributorview.DistributorView2;

import java.util.Arrays;

public class GameActivity extends AppCompatActivity {

    private static final String LEVEL_PARAM = "level";

    private static final int ROUNDS_COUNT = 3;
    private int currentRound;

    private LevelDao levelDao;

    private Level currentLevel;

    private int record;
    private int currentScore;

    private int levelId;

    private int levelShowingTime;

    private RatingBar ratingBar;
    private TextView levelNumberTextView;

    private View roundView;
    private ImageView roundImageView;
    private TextView roundTitleTextView;
    private TextView roundDescriptionTextView;

    private TextView checkResultButton;

    private View blackoutView;
    private View bottomBlackoutView;

    private BoardView boardView;
    private DistributorView2 distributorView;

    private int rowCount;
    private int columnCount;

    private int[] firstRoundLevelItems;
    private int[] secondRoundLevelItems;
    private int[] thirdRoundLevelItems;

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

        levelDao = LevelDao.getInstance(GameActivity.this);

        boardView = (BoardView) findViewById(R.id.board_view);
        distributorView = (DistributorView2) findViewById(R.id.hidden_stuff);

        blackoutView = findViewById(R.id.game_blackout_view);
        bottomBlackoutView = findViewById(R.id.game_bottom_blackout_view);

        ratingBar = (RatingBar) findViewById(R.id.stars);
        levelNumberTextView = (TextView) findViewById(R.id.level_number_text_view);
        checkResultButton = (TextView) findViewById(R.id.check_result_button);

        roundView = findViewById(R.id.game_round_banner);
        roundImageView = (ImageView) roundView.findViewById(R.id.game_round_image_view);
        roundTitleTextView = (TextView) roundView.findViewById(R.id.game_round_title_text_view);
        roundDescriptionTextView = (TextView) roundView.findViewById(R.id.game_round_description_text_view);

        startRoundAnimator = ObjectAnimator.ofFloat(roundView, View.ALPHA, 0.0f, 1.0f);
        startRoundAnimator.setDuration(1500);
        startRoundAnimator.addListener(startRoundAnimatorListener);

        endRoundAnimator = ObjectAnimator.ofFloat(roundView, View.ALPHA, 0.0f, 1.0f);
        endRoundAnimator.setDuration(1000);
        endRoundAnimator.addListener(endRoundAnimatorListener);

        thirdRoundAnimator = ObjectAnimator.ofFloat(roundView, View.ALPHA, 0.0f, 1.0f);
        thirdRoundAnimator.setDuration(1000);
        thirdRoundAnimator.addListener(thirdRoundAnimatorListener);

        currentLevel = getIntent().getParcelableExtra(LEVEL_PARAM);
        if (currentLevel != null) {
            resetLevelProgress();
            setCurrentLevel(currentLevel);
        } else {
            return;
        }

        boardView.setOnTouchListener(boardTouchListener); // Нажатие за пределами полей
        checkResultButton.setOnClickListener(checkResultOnClickListener); //

        final ImageView pauseButton = (ImageView) findViewById(R.id.pause_image_view);
        pauseButton.setOnClickListener(pauseOnClickListener);

        final ImageView restartButton = (ImageView) findViewById(R.id.restart_image_view);
        restartButton.setOnClickListener(restartOnClickListener);
    }

    private void setCurrentLevel(Level level) {
        levelId = level.getLevelId();
        levelNumberTextView.setText(String.valueOf(levelId));

        record = level.getRecord();
        levelShowingTime = level.getShowingTime();

        rowCount = level.getRowCount();
        columnCount = level.getColumnCount();
        boardView.createItems(rowCount, columnCount);

        firstRoundLevelItems = level.getFirstRoundItems();
        secondRoundLevelItems = level.getSecondRoundItems();
        thirdRoundLevelItems = level.getThirdRoundItems();

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
                    rules = Generator.createRules(levelType, firstRoundLevelItems);

                    currentGameRound = Generator.createRound1Items(rules, rowCount * columnCount);
                    boardView.setItemsResources(currentGameRound.getAnswer());

                    break;
                case 2:
                    rules = Generator.createRules(levelType, secondRoundLevelItems);

                    isSecondRoundFirstPartShowing = true;
                    currentGameRound = Generator.createRound2Items(rules, rowCount * columnCount);
                    boardView.setItemsResources(((SecondGameRound)currentGameRound).getFirstPart());

                    break;
                case 3:
                    rules = Generator.createRules(levelType, thirdRoundLevelItems);

                    isThirdRoundFirstPartShowing = true;
                    currentGameRound = Generator.createRound3Items(rules, rowCount * columnCount);
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

                    //if ( ! isPaused) {
                        currentPlayTime += DEFAULT_DELAY;

                        isRunning = true;
                    //} else {
                    //    isRunning = false;
                    //}

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

            if (selectedBoardItem != null) {
                selectedBoardItem.depress();
                selectedBoardItem = null;
            }

            blackoutView.setVisibility(View.INVISIBLE);
            bottomBlackoutView.setVisibility(View.INVISIBLE);

            roundImageView.setVisibility(View.GONE);
            roundTitleTextView.setVisibility(View.VISIBLE);
            roundDescriptionTextView.setVisibility(View.VISIBLE);
            roundView.setVisibility(View.VISIBLE);

            boardView.setVisibility(View.INVISIBLE);

            switch (currentRound) {
                case 1:
                    roundTitleTextView.setText(R.string.game_round_1_title);
                    roundDescriptionTextView.setText(R.string.game_round_1_description);
                    break;
                case 2:
                    roundTitleTextView.setText(R.string.game_round_2_title);
                    roundDescriptionTextView.setText(R.string.game_round_2_description);
                    break;
                case 3:
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

                showBoardItemsRunnable = new ShowBoardItemRunnable(levelShowingTime, currentRound);
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
                    ratingBar.setSelectedIndex(currentRound - 1);
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
                startRoundAnimator.cancel();
                endRoundAnimator.cancel();
                thirdRoundAnimator.cancel();
                showBoardItemsRunnable.cancel();

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

    //КНОПКИ УПРАВЛЕНИЯ
    private View.OnClickListener pauseOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showPauseDialog();
        }
    };

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
                if (selectedBoardItem != null) {
                    selectedBoardItem.depress();

                    selectedBoardItem = null;
                }

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

                    if (selectedBoardItem != null)
                        selectedBoardItem.depress();

                    boardItemView.removeImageView();

                    checkResultButton.setVisibility(View.INVISIBLE);
                } else {
                    if (boardItemView.isItemPressed()) { //Повторный клик по пустой клетке

                        Log.d("Lel", "Убрать инструменты");
                        distributorView.setVisibility(View.INVISIBLE);

                        blackoutView.setVisibility(View.INVISIBLE);
                        bottomBlackoutView.setVisibility(View.INVISIBLE);

                        //selectedBoardItem.depress();
                        boardItemView.depress();

                        selectedBoardItem = null;

                    } else { //Клик по пустой клетке
                        int unusedResourceTypesCount = distributorView.getUnusedResourceTypesCount();

                        switch (unusedResourceTypesCount) {
                            case 0:
                                //do nothing
                                break;
                            case 1:
                                Log.d("Lel", "Всего одна фигура");
                                DistributorView2.DistributorItemView hiddenItem = distributorView.getItem(0);

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
                                boardItemView.press();

                                blackoutView.setVisibility(View.VISIBLE);
                                bottomBlackoutView.setVisibility(View.VISIBLE);

                                if (selectedBoardItem != null) {
                                    selectedBoardItem.depress();
                                }
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
                DistributorView2.DistributorItemView distributorItemView = (DistributorView2.DistributorItemView) touchView;

                selectedBoardItem.createImageView(distributorItemView.getItemType());
                selectedBoardItem.depress();

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
        final AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);

        final View dialogView = LayoutInflater.from(GameActivity.this).inflate(R.layout.result_dialog, null);

        final View headerView = dialogView.findViewById(R.id.result_dialog_header);
        if (currentScore == 0) {
            headerView.setBackgroundColor(ContextCompat.getColor(GameActivity.this, R.color.result_dialog_title_unsuccessful_background_color));
        } else {
            headerView.setBackgroundColor(ContextCompat.getColor(GameActivity.this, R.color.result_dialog_title_successful_background_color));
        }

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
        final Level nextLevel = levelDao.getLevel(levelId + 1);
        if (nextLevel.isEnabled()) {
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
            if (currentScore > 0) {
                final Level unlockedLevel = levelDao.unlockLevel(levelId + 1);

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
                // TUT
                nextImageView.setVisibility(View.GONE);
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

        final AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);

        final View dialogView = LayoutInflater.from(GameActivity.this).inflate(R.layout.pause_dialog, null);
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
