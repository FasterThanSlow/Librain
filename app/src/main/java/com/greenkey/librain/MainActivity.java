package com.greenkey.librain;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.greenkey.librain.campaign.Level;
import com.greenkey.librain.distributorview.DistributorItemView;
import com.greenkey.librain.distributorview.DistributorView;
import com.greenkey.librain.reciverview.BoardItemView;
import com.greenkey.librain.reciverview.BoardView;
import com.greenkey.librain.reciverview.OnBoardItemDragListener;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private LevelDao levelDao;

    private static final String LEVEL_PARAM = "level";

    private Level level;

    private int trueAnswersCount;
    private int countTries;
    private static final int MAX_TRIES_COUNT = 3;

    private int levelNumber;
    private int record;

    private int levelShowingTime;

    private RatingBar ratingBar;
    private TextView levelNumberTextView;
    private TextView stateTextView;

    private TextView confirmButton;

    private BoardView boardView;
    private DistributorView distributorView;

    private int rowCount;
    private int columnCount;

    private Rule[] rules;
    private ResourceType[] resources;

    private Handler handler = new Handler();

    private ObjectAnimator preShowAnimator;
    private ShowBoardItemRunnable showBoardItemsRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        levelDao = LevelDao.getInstance(MainActivity.this);

        boardView = (BoardView) findViewById(R.id.board_view);
        distributorView = (DistributorView) findViewById(R.id.distributor_view);
        ratingBar = (RatingBar) findViewById(R.id.stars);
        stateTextView = (TextView) findViewById(R.id.state_text_view);
        levelNumberTextView = (TextView) findViewById(R.id.level_number_text_view);
        confirmButton = (TextView) findViewById(R.id.confirm_text_view);

        level = getIntent().getParcelableExtra(LEVEL_PARAM);
        if (level != null) {
            resetLevelProgress();
            setCurrentLevel(level);
        } else {
            return;
        }

        boardView.setItemsDragListener(new DragListenerWrapper(distributorView, confirmButton));

        preShowAnimator = ObjectAnimator.ofFloat(boardView, View.ALPHA, 0f, 1f);
        preShowAnimator.setDuration(1000);
        preShowAnimator.addListener(new Animator.AnimatorListener() {

            private boolean isAlphaAnimationPaused;

            @Override
            public void onAnimationStart(Animator animation) {
                isAlphaAnimationPaused = false;

                boardView.setItemsImageViewOnTouchListener(null);
                distributorView.setItemsImageViewOnTouchListener(null);

                Log.d("Anim", "Start");
                boardView.removeItemsResources();

                distributorView.setRules(rules);

                distributorView.setVisibility(View.VISIBLE);
                confirmButton.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if ( ! isAlphaAnimationPaused) {
                    Log.d("Anim", "End");
                    resources = BoardViewItemGenerator.createResources(rules, rowCount * columnCount);
                    boardView.setItemsResources(resources);

                    showBoardItemsRunnable = new ShowBoardItemRunnable(levelShowingTime);

                    handler.post(showBoardItemsRunnable);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.d("Anim", "Cancel");
                isAlphaAnimationPaused = true;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                distributorView.setVisibility(View.VISIBLE);
                confirmButton.setVisibility(View.GONE);

                ResourceType[] userAnswer = boardView.getItemsResources();
                    if (Arrays.equals(userAnswer, resources)) {
                        trueAnswersCount++;
                        ratingBar.setProgress(trueAnswersCount);
                    } else {
                        //Toast.makeText(MainActivity.this, "Не красавчик", Toast.LENGTH_SHORT).show();
                    }

                countTries++;

                if (countTries < MAX_TRIES_COUNT) {
                    setState(countTries, MAX_TRIES_COUNT);
                    preShowAnimator.start();
                } else {
                    showResultDialog();
                }
            }
        });

        final ImageView pauseButton = (ImageView) findViewById(R.id.pause);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPauseDialog();
            }
        });

        final ImageView restartButton = (ImageView) findViewById(R.id.restartKnopka);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartButton.setClickable(false);

                if (preShowAnimator.isStarted()) {
                    preShowAnimator.cancel();
                } else if (showBoardItemsRunnable.isRunning()) {
                    showBoardItemsRunnable.cancel();
                }

                preShowAnimator.start();

                restartButton.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (restartButton != null) {
                            restartButton.setClickable(true);
                        }
                    }
                }, 1000);
            }
        });
    }

    private void setCurrentLevel(Level level) {
        levelNumber = level.getLevelId();
        levelNumberTextView.setText(String.valueOf(levelNumber));

        record = level.getRecord();
        levelShowingTime = level.getShowingTime();

        rowCount = level.getRowCount();
        columnCount = level.getColumnCount();
        boardView.createItems(rowCount, columnCount);

        rules = level.getRules();
        distributorView.createItems(rules);
    }

    private void resetLevelProgress() {
        ratingBar.setProgress(0);

        trueAnswersCount = 0;
        countTries = 0;

        setState(countTries, MAX_TRIES_COUNT);
    }

    private void setState(int countTries, int maxCountTries) {
        stateTextView.setText((countTries + 1) + "/" + maxCountTries);
    }

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
        public ShowBoardItemRunnable(int showTime) {
            this.showTime = showTime;
        }

        @Override
        public void run() {
            if (isCancelled) {
                Log.d("Anim", "showingPausing" + getTaskId());
                isRunning = false;
            } else {
                if (currentShowTime < showTime) {
                    Log.d("Anim", "Showing " + getTaskId());

                    isRunning = true;

                    currentShowTime += DEFAULT_DELAY;
                    handler.postDelayed(this, DEFAULT_DELAY);
                } else {
                    Log.d("Anim", "ShowEnded " + getTaskId());

                    isRunning = false;

                    boardView.removeItemsResources();

                    boardView.setItemsImageViewOnTouchListener(touchListener);
                    distributorView.setItemsImageViewOnTouchListener(touchListener);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        showPauseDialog();
    }

    //ResultDialog//////////

    private AlertDialog resultDialog;

    private void showResultDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        final View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.result_dialog, null);

        final TextView levelTextView = (TextView) dialogView.findViewById(R.id.result_dialog_level_text_view);
        levelTextView.setText(String.valueOf(levelNumber));

        final RatingBar ratingBar = (RatingBar) dialogView.findViewById(R.id.result_dialog_rating_bar);
        ratingBar.setProgress(trueAnswersCount);

        if (trueAnswersCount > record) {
            Toast.makeText(MainActivity.this, "Новый рекорд (Тест)", Toast.LENGTH_LONG).show();

            level.setRecord(trueAnswersCount);

            levelDao.updateLevel(level);
        }

        final TextView levelsTextView = (TextView) dialogView.findViewById(R.id.result_dialog_levels_text_view);
        levelsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultDialog.dismiss();
                finish();
            }
        });

        final TextView restartTextView = (TextView) dialogView.findViewById(R.id.result_dialog_restart_text_view);
        restartTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultDialog.dismiss();

                resetLevelProgress();
                preShowAnimator.start();
            }
        });

        final TextView nextTextView = (TextView) dialogView.findViewById(R.id.result_dialog_next_text_view);
        nextTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultDialog.dismiss();

                Level nextLevel = levelDao.findLevel(levelNumber + 1);
                nextLevel.setEnabled(true);

                levelDao.updateLevel(nextLevel);

                setCurrentLevel(nextLevel);
                resetLevelProgress();
                preShowAnimator.start();
            }
        });

        builder.setCancelable(false);
        builder.setView(dialogView);

        resultDialog = builder.create();
        resultDialog.show();
    }

    private AlertDialog pauseDialog;

    private boolean isContinuePressed;
    private boolean isGame;

    private void showPauseDialog() {

        isGame = true;

        if (preShowAnimator.isStarted()) {
            Log.d("Anim", "alphaAnimationIsStarted");
            isGame = false;
            preShowAnimator.cancel();
        } else if (showBoardItemsRunnable.isRunning()) {
            Log.d("Anim", "endRunnableIsRunning");
            isGame = false;
            showBoardItemsRunnable.cancel();
        }

        isContinuePressed = true; // Эмулиция продолжение на нажатие мимо диалога

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        final View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.pause_dialog, null);
        final TextView levelsTextView = (TextView) dialogView.findViewById(R.id.pause_dialog_levels_text_view);
        levelsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isContinuePressed = false;
                pauseDialog.dismiss();

                finish();
            }
        });

        final TextView restartTextView = (TextView) dialogView.findViewById(R.id.pause_dialog_restart_text_view);
        restartTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isContinuePressed = false;
                pauseDialog.dismiss();

                preShowAnimator.start();
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
                        preShowAnimator.start();
                    }
                }
            }
        });

        pauseDialog = builder.create();
        pauseDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (resultDialog == null || ! resultDialog.isShowing()) {
            preShowAnimator.start();
        }
    }

    @Override
    protected void onPause() {
        if (preShowAnimator != null && preShowAnimator.isStarted()) {
            preShowAnimator.cancel();
        } else if (showBoardItemsRunnable != null && showBoardItemsRunnable.isRunning()) {
            showBoardItemsRunnable.cancel();
        } else {
            if (pauseDialog != null && pauseDialog.isShowing()) {
                pauseDialog.dismiss();
            }
        }

        super.onPause();
    }

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(view);

                view.startDrag(null, myShadow, view, 0);

                return true;
            }

            return false;
        }
    };

    private class DragListenerWrapper implements OnBoardItemDragListener {

        private DistributorView distributorView;
        private View confirmButton;

        public DragListenerWrapper(DistributorView distributorView, View confirmButton) {
            this.distributorView = distributorView;
            this.confirmButton = confirmButton;
        }

        private boolean dragIsNotUsing = true;

        @Override
        public boolean onBoardItemDrag(BoardItemView receiverView, DragEvent event) {

            final ImageView dragView = (ImageView) event.getLocalState();
            final ViewParent dragViewParent = dragView.getParent();

            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    receiverView.setBackgroundResource(R.drawable.exist_shape);

                    if (dragIsNotUsing) {
                        if (dragViewParent instanceof BoardItemView) {
                            dragView.setVisibility(View.INVISIBLE);
                        } else if (dragViewParent instanceof DistributorItemView) {
                            ((DistributorItemView) dragViewParent).removeImageView();
                        }

                        dragIsNotUsing = false;
                    }
                    break;

                case DragEvent.ACTION_DRAG_EXITED:
                    receiverView.setBackgroundResource(R.drawable.exist_shape);
                    break;

                case DragEvent.ACTION_DRAG_ENTERED :
                    receiverView.setBackgroundResource(R.drawable.enterd_shape);
                    break;

                case DragEvent.ACTION_DRAG_ENDED:
                    receiverView.setBackgroundResource(R.drawable.normal_shape);

                    if ( !dragIsNotUsing) {
                        if (dragViewParent instanceof BoardItemView) {

                            ResourceType resourceType = ((BoardItemView) dragViewParent).getResourceType();
                            ((BoardItemView)dragViewParent).removeImageView();

                            DistributorItemView itemView = distributorView.findItem(resourceType);
                            if (itemView != null) {
                                itemView.addImageView();
                            }

                            dragView.setVisibility(View.VISIBLE);

                        } else if (dragViewParent instanceof DistributorItemView) {
                                if ( ! isDropped(event)) {
                                    ((DistributorItemView) dragViewParent).addImageView();
                                }
                        }

                        if ( ! distributorView.allItemsResourcesUsed()) {
                            distributorView.setVisibility(View.VISIBLE);
                            confirmButton.setVisibility(View.GONE);
                        }

                        dragIsNotUsing = true;
                    }
                    break;

                case DragEvent.ACTION_DROP:
                    ViewParent viewParent = dragView.getParent();

                    if (viewParent instanceof DistributorItemView) {
                        DistributorItemView distributorItemView = (DistributorItemView) viewParent;

                        if (receiverView.hasImageView()) {
                            ResourceType resourceType = receiverView.getResourceType();

                            DistributorItemView foundDistributorItemView = distributorView.findItem(resourceType);
                            if (foundDistributorItemView != null) {
                                foundDistributorItemView.addImageView();
                            }

                            receiverView.removeImageView();
                            receiverView.createImageView(distributorItemView.getResourceType());
                        } else {
                            receiverView.createImageView(distributorItemView.getResourceType());
                        }

                    } else if (viewParent instanceof BoardItemView) {
                        BoardItemView owner = (BoardItemView) viewParent;
                        ResourceType ownerResourceType = owner.getResourceType();
                        owner.removeImageView();

                        if (receiverView.hasImageView()) {
                            owner.createImageView(receiverView.getResourceType());
                            receiverView.removeImageView();

                            receiverView.createImageView(ownerResourceType);
                        } else {
                            receiverView.createImageView(ownerResourceType);
                        }
                    }

                    if (distributorView.allItemsResourcesUsed()) {
                        distributorView.setVisibility(View.GONE);
                        confirmButton.setVisibility(View.VISIBLE);
                    } else {
                        distributorView.setVisibility(View.VISIBLE);
                        confirmButton.setVisibility(View.GONE);
                    }
                    break;
            }

            return true;
        }

        private boolean isDropped(DragEvent event) {
            return event.getResult();
        }
    }
}
