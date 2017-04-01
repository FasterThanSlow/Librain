package com.greenkey.librain;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
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
    private TextView stateTextView;

    private View roundView;
    private TextView roundTitleTextView;
    private TextView roundDescriptionTextView;

    private TextView confirmButton;

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

        ratingBar = (RatingBar) findViewById(R.id.stars);
        stateTextView = (TextView) findViewById(R.id.state_text_view);
        levelNumberTextView = (TextView) findViewById(R.id.level_number_text_view);
        confirmButton = (TextView) findViewById(R.id.confirm_text_view);

        roundView = findViewById(R.id.game_round_banner);
        roundTitleTextView = (TextView) roundView.findViewById(R.id.game_round_title_text_view);
        roundDescriptionTextView = (TextView) roundView.findViewById(R.id.game_round_description_text_view);

        startRoundAnimator = ObjectAnimator.ofFloat(roundTitleTextView, View.ALPHA, 0.0f, 1.0f);
        startRoundAnimator.setDuration(1500);
        startRoundAnimator.addListener(startRoundAnimatorListener);

        endRoundAnimator = ObjectAnimator.ofFloat(roundTitleTextView, View.ALPHA, 0.0f, 1.0f);
        endRoundAnimator.setDuration(1000);
        endRoundAnimator.addListener(endRoundAnimatorListener);

        thirdRoundAnimator = ObjectAnimator.ofFloat(roundTitleTextView, View.ALPHA, 0.0f, 1.0f);
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
        confirmButton.setOnClickListener(confirmOnClickListener); //

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

        distributorViewWidth = 0;
        distributorViewHeight = 0;

        currentScore = 0;
        currentRound = 1;

        setStateTextView(currentRound, ROUNDS_COUNT);
    }

    private void setStateTextView(int countTries, int maxCountTries) {
        stateTextView.setText(currentRound+ "/" + maxCountTries);
    }


    //Показ фигур на указанное время и дальнейшее их удаление c поля
    private class ShowBoardItemRunnable implements Runnable {

        private static final int DEFAULT_DELAY = 100;

        private volatile boolean isRunning;
        public boolean isRunning() {
            return isRunning;
        }

        private boolean isCancelled;
        public boolean isCancelled() {
            return isCancelled;
        }

        private int currentShowTime;
        public int getCurrentShowTime() {
            return currentShowTime;
        }

        public void cancel() {
            isCancelled = true;
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
            } else {
                if (currentShowTime < showTime) {

                    isRunning = true;

                    currentShowTime += DEFAULT_DELAY;
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
                                currentShowTime = 0;
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
                                currentShowTime = 0;
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
                roundTitleTextView.setText("Покажи мне");
                roundDescriptionTextView.setText(String.valueOf(((ThirdGameRound)currentGameRound).getTrueAnswerPart()));
            }
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if ( ! isAnimationPaused) {
                roundView.setVisibility(View.INVISIBLE);

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
            confirmButton.setVisibility(View.INVISIBLE);
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

        @Override
        public void onAnimationStart(Animator animation) {

            isAnimationPaused = false;

            //roundTitleTextView;
            roundView.setVisibility(View.VISIBLE);
            boardView.setVisibility(View.INVISIBLE);

            confirmButton.setVisibility(View.INVISIBLE);

            ItemType[] userAnswer = boardView.getItemsResources();
            if (Arrays.equals(userAnswer, currentGameRound.getAnswer())) {
                currentScore++;
                ratingBar.setProgress(currentScore);

                roundTitleTextView.setText("Pravilno");
                roundDescriptionTextView.setText(null);
            } else {
                roundTitleTextView.setText("Ne Pravilno");
                roundDescriptionTextView.setText(null);
            }

            currentRound++;
        }

        @Override
        public void onAnimationEnd(Animator animation) {

            if ( ! isAnimationPaused) {

                if (currentRound <= ROUNDS_COUNT) {
                    setStateTextView(currentRound, ROUNDS_COUNT);
                    startRoundAnimator.start();
                } else {
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

        if (resultDialog == null || ! resultDialog.isShowing()) {
            startRoundAnimator.start();
        }
    }

    @Override
    protected void onPause() {

        if (startRoundAnimator.isRunning()) {
            startRoundAnimator.cancel();
        } else if (showBoardItemsRunnable != null && showBoardItemsRunnable.isRunning()) {
            showBoardItemsRunnable.cancel();
        } else if (endRoundAnimator.isRunning()) {
            endRoundAnimator.cancel();
        } else if (thirdRoundAnimator.isRunning()) {
            thirdRoundAnimator.cancel();
        } else  {
            if (pauseDialog != null && pauseDialog.isShowing()) {
                pauseDialog.dismiss();
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

    private View.OnClickListener confirmOnClickListener = new View.OnClickListener() {
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

                    confirmButton.setVisibility(View.GONE);
                } else {
                    if (boardItemView.isItemPressed()) { //Повторный клик по пустой клетке
                        Log.d("Lel", "Убрать инструменты");

                        //selectedBoardItem.depress();
                        boardItemView.depress();

                        selectedBoardItem = null;

                        distributorView.setVisibility(View.INVISIBLE);
                    } else { //Клик по пустой клетке
                        int unusedResourceTypesCount = distributorView.getUnusedResourceTypesCount();

                        switch (unusedResourceTypesCount) {
                            case 0:
                                //do nothing
                                break;
                            case 1:
                                Log.d("Lel", "Всего одна фигура");
                                DistributorView2.DistributorItemView hiddenItem = distributorView.getItem(0);

                                //if ( ! hiddenItem.allResourcesUsed()) {
                                    boardItemView.createImageView(hiddenItem.getItemType());
                                    hiddenItem.removeImageView();
                                    //selectedBoardItem.depress();
                                //}

                                if (distributorView.allItemsResourcesUsed()) {
                                    confirmButton.setVisibility(View.VISIBLE);
                                } else {
                                    confirmButton.setVisibility(View.GONE);
                                }
                                break;
                            default:
                                Log.d("Lel", "Показать инструменты");
                                boardItemView.press();

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

                //if ( ! distributorItemView.allResourcesUsed()) {
                    selectedBoardItem.createImageView(distributorItemView.getItemType());
                    selectedBoardItem.depress();

                    distributorItemView.removeImageView();
                //}

                distributorView.setVisibility(View.INVISIBLE);

                if (distributorView.allItemsResourcesUsed()) {
                    confirmButton.setVisibility(View.VISIBLE);
                } else {
                    confirmButton.setVisibility(View.GONE);
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

        final TextView levelTextView = (TextView) dialogView.findViewById(R.id.result_dialog_level_text_view);
        levelTextView.setText(String.valueOf(levelId));

        final RatingBar ratingBar = (RatingBar) dialogView.findViewById(R.id.result_dialog_rating_bar);
        ratingBar.setProgress(currentScore);

        final TextView levelsTextView = (TextView) dialogView.findViewById(R.id.result_dialog_levels_text_view);
        levelsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultDialog.dismiss();

                setResult(RESULT_OK);

                finish();
            }
        });

        final TextView restartTextView = (TextView) dialogView.findViewById(R.id.result_dialog_restart_text_view);
        restartTextView.setOnClickListener(new View.OnClickListener() {
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

        final TextView nextTextView = (TextView) dialogView.findViewById(R.id.result_dialog_next_text_view);
        final Level nextLevel = levelDao.getLevel(levelId + 1);
        if (nextLevel.isEnabled()) {
            nextTextView.setOnClickListener(new View.OnClickListener() {
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

                nextTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resultDialog.dismiss();

                        setCurrentLevel(unlockedLevel);
                        resetLevelProgress();

                        startRoundAnimator.start();
                    }
                });
            } else {
                nextTextView.setVisibility(View.GONE);
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
    private boolean isGame;

    private void showPauseDialog() {
        isGame = true;

        if (startRoundAnimator.isRunning()) {
            Log.d("Anim", "alphaAnimationIsStarted");
            isGame = false;
            startRoundAnimator.cancel();
        } else if (showBoardItemsRunnable.isRunning()) {
            Log.d("Anim", "endRunnableIsRunning");
            isGame = false;
            showBoardItemsRunnable.cancel();
        }

        isContinuePressed = true; // Эмулиция продолжение на нажатие мимо диалога

        final AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);

        final View dialogView = LayoutInflater.from(GameActivity.this).inflate(R.layout.pause_dialog, null);
        final TextView levelsTextView = (TextView) dialogView.findViewById(R.id.pause_dialog_levels_text_view);
        levelsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isContinuePressed = false;
                pauseDialog.dismiss();

                setResult(RESULT_OK);

                finish();
            }
        });

        final TextView restartTextView = (TextView) dialogView.findViewById(R.id.pause_dialog_restart_text_view);
        restartTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isContinuePressed = false;
                pauseDialog.dismiss();

                startRoundAnimator.start();
            }
        });

        final TextView continueTextView = (TextView) dialogView.findViewById(R.id.pause_dialog_continue_text_view);
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
                    if ( ! isGame) {
                        startRoundAnimator.start();
                    }
                }
            }
        });

        pauseDialog = builder.create();
        pauseDialog.show();
    }
}
