package com.greenkey.librain.tutorial;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.greenkey.librain.R;
import com.greenkey.librain.campaign.CampaignMenuActivity;
import com.greenkey.librain.entity.ItemType;
import com.greenkey.librain.entity.Rule;
import com.greenkey.librain.view.boardview.BoardView;
import com.greenkey.librain.view.distributorview.DistributorView;

public class TutorialActivity extends AppCompatActivity implements View.OnClickListener {

    private BoardView boardView;
    private DistributorView distributorView;

    private View tutorialDescriptionView;
    private TextView tutorialDescriptionTitleTextView;
    private TextView tutorialDescriptionMessageTextView;

    private ImageView roundImageView;

    private FrameLayout blackoutView;
    private View bottomBlackoutView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial_activity);

        boardView = (BoardView) findViewById(R.id.tutorial_board_view);
        distributorView = (DistributorView) findViewById(R.id.tutorial_distributor_view);

        blackoutView = (FrameLayout) findViewById(R.id.tutorial_blackout_view);
        bottomBlackoutView = findViewById(R.id.tutorial_bottom_blackout_view);

        tutorialDescriptionView = findViewById(R.id.tutorial_description_view);
        tutorialDescriptionTitleTextView = (TextView) tutorialDescriptionView.findViewById(R.id.tutorial_description_title_text_view);
        tutorialDescriptionMessageTextView = (TextView) tutorialDescriptionView.findViewById(R.id.tutorial_description_message_text_view);

        roundImageView = (ImageView) findViewById(R.id.tutorial_third_round_image_view);

        tutorialDescriptionView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (currentStage) {
            case TUTORIAL_DESCRIPTION:
                helloAction.cancel();
                firstRoundDescriptionAction.start();
                break;
            case FIRST_ROUND_DESCRIPTION:
                firstRoundDescriptionAction.cancel();
                firstRoundShowingAction.start();
                break;
            case SECOND_ROUND_DESCRIPTION:
                secondRoundDescription.cancel();
                secondRoundShowing.start();
                break;
            case THIRD_ROUND_DESCRIPTION:
                thirdRoundDescription.cancel();
                thirdRound.start();
                break;
        }
    }

    private enum TutorialStage {
        TUTORIAL_DESCRIPTION,
        FIRST_ROUND_DESCRIPTION, FIRST_ROUND_SHOWING, FIRST_ROUND_GUESSING,
        SECOND_ROUND_DESCRIPTION, SECOND_ROUND_SHOWING, SECOND_ROUND_GUESSING,
        THIRD_ROUND_DESCRIPTION, THIRD_ROUND
    }

    private TutorialStage currentStage;

    @Override
    protected void onResume() {
        super.onResume();
        helloAction.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (tutorialCompletedDialog == null || ! tutorialCompletedDialog.isShowing()) {
            switch (currentStage) {
                case TUTORIAL_DESCRIPTION:
                    helloAction.cancel();
                    break;
                case FIRST_ROUND_DESCRIPTION:
                    firstRoundDescriptionAction.cancel();
                    break;
                case FIRST_ROUND_SHOWING:
                    firstRoundShowingAction.cancel();
                    break;
                case FIRST_ROUND_GUESSING:
                    firstRoundGuessAction.cancel();
                    break;
                case SECOND_ROUND_DESCRIPTION:
                    secondRoundDescription.cancel();
                    break;
                case SECOND_ROUND_SHOWING:
                    secondRoundShowing.cancel();
                    break;
                case SECOND_ROUND_GUESSING:
                    secondRoundGuessing.cancel();
                    break;
                case THIRD_ROUND_DESCRIPTION:
                    thirdRoundDescription.cancel();
                    break;
                case THIRD_ROUND:
                    thirdRound.cancel();
                    break;
            }
        }
    }

    private abstract class Action {
        abstract void start();
        abstract void cancel();
    }

    //Привет, это обучение
    private Action helloAction = new Action() {

        private static final int DURATION = 2000;

        private ObjectAnimator animator;

        @Override
        void start() {
            currentStage = TutorialStage.TUTORIAL_DESCRIPTION;

            boardView.setVisibility(View.INVISIBLE);
            distributorView.setVisibility(View.INVISIBLE);

            blackoutView.setVisibility(View.INVISIBLE);
            bottomBlackoutView.setVisibility(View.INVISIBLE);

            tutorialDescriptionTitleTextView.setText(R.string.tutorial_start_title);
            tutorialDescriptionMessageTextView.setText(R.string.tutorial_start_message);
            tutorialDescriptionView.setVisibility(View.VISIBLE);

            animator = ObjectAnimator.ofFloat(tutorialDescriptionView, View.ALPHA, 0f, 1f);
            animator.setDuration(DURATION);
            animator.start();
        }

        @Override
        void cancel() {
            if (animator != null) {
                animator.cancel();
            }

            tutorialDescriptionView.setVisibility(View.INVISIBLE);
        }
    };


    //Описание Раунда 1
    private Action firstRoundDescriptionAction = new Action() {

        private static final int DURATION = 2000;

        private ObjectAnimator animator;

        @Override
        void start() {
            currentStage = TutorialStage.FIRST_ROUND_DESCRIPTION;

            boardView.setVisibility(View.INVISIBLE);
            distributorView.setVisibility(View.INVISIBLE);

            blackoutView.setVisibility(View.INVISIBLE);
            bottomBlackoutView.setVisibility(View.INVISIBLE);


            tutorialDescriptionTitleTextView.setText(R.string.tutorial_first_round_title);
            tutorialDescriptionMessageTextView.setText(R.string.tutorial_first_round_message);
            tutorialDescriptionView.setVisibility(View.VISIBLE);

            animator = ObjectAnimator.ofFloat(tutorialDescriptionView, View.ALPHA, 0f, 1f);
            animator.setDuration(DURATION);
            animator.start();
        }

        @Override
        void cancel() {
            if (animator != null) {
                animator.cancel();
            }

            tutorialDescriptionView.setVisibility(View.INVISIBLE);
        }
    };

    //Показ фигур Раунда 1
    private Action firstRoundShowingAction = new Action() {

        private static final int SHOW_ITEM_DURATION = 1000;

        private BoardView.BoardItemView shownBoardItemView;

        private Runnable showItemRunnable;

        @Override
        void start() {
            currentStage = TutorialStage.FIRST_ROUND_SHOWING;

            tutorialDescriptionView.setVisibility(View.INVISIBLE);

            boardView.setVisibility(View.VISIBLE);

            shownBoardItemView = (BoardView.BoardItemView)((ViewGroup) boardView.getChildAt(0)).getChildAt(1);
            shownBoardItemView.createImageView(ItemType.EARTH);

            showItemRunnable = new Runnable() {
                @Override
                public void run() {
                    shownBoardItemView.removeImageView();

                    firstRoundGuessAction.start();
                }
            };

            shownBoardItemView.postDelayed(showItemRunnable, SHOW_ITEM_DURATION);
        }

        @Override
        void cancel() {
            if (shownBoardItemView != null) {
                shownBoardItemView.removeImageView();
                shownBoardItemView.removeCallbacks(showItemRunnable);
            }

            boardView.setVisibility(View.INVISIBLE);
        }
    };

    //Раунд 1 - Пользователь отгадывает
    private Action firstRoundGuessAction = new Action() {

        private ObjectAnimator handAnimator;

        private static final int START_NEXT_STEP_DELAY = 500;

        @Override
        void start() {
            currentStage = TutorialStage.FIRST_ROUND_GUESSING;

            BoardView.BoardItemView boardItemView = (BoardView.BoardItemView)((ViewGroup) boardView.getChildAt(0)).getChildAt(1);

            float x = boardItemView.getX();
            float y = boardItemView.getY();

            final BoardView.BoardItemView tutorialBoardItem = new BoardView.BoardItemView(TutorialActivity.this, boardItemView.getHeight());
            tutorialBoardItem.setX(x);
            tutorialBoardItem.setY(y);

            tutorialBoardItem.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        tutorialBoardItem.createImageView(ItemType.EARTH);

                        tutorialBoardItem.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                blackoutView.removeAllViews();

                                secondRoundDescription.start();
                            }
                        }, START_NEXT_STEP_DELAY);
                    }

                    return false;
                }
            });

            final ImageView handImageView = new ImageView(TutorialActivity.this);
            handImageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            handImageView.setImageResource(R.drawable.tutorial_hand_icon);
            handImageView.setX(x);
            handImageView.setY(y + boardItemView.getHeight());

            blackoutView.addView(tutorialBoardItem);
            blackoutView.addView(handImageView);

            tutorialDescriptionView.setVisibility(View.INVISIBLE);

            boardView.setVisibility(View.VISIBLE);
            blackoutView.setVisibility(View.VISIBLE);
            bottomBlackoutView.setVisibility(View.VISIBLE);

            handAnimator = ObjectAnimator.ofFloat(handImageView, View.TRANSLATION_Y, y + boardItemView.getHeight(), y + boardItemView.getHeight() + 100);
            handAnimator.setRepeatCount(ValueAnimator.INFINITE);
            handAnimator.setRepeatMode(ValueAnimator.REVERSE);
            handAnimator.setDuration(1000);
            handAnimator.start();
        }

        @Override
        void cancel() {
            if (handAnimator != null) {
                handAnimator.cancel();
            }

            blackoutView.removeAllViews();
        }
    };


    //Описание Раунда 2
    private Action secondRoundDescription = new Action() {

        private static final int DURATION = 2000;

        private ObjectAnimator animator;

        @Override
        void start() {
            currentStage = TutorialStage.SECOND_ROUND_DESCRIPTION;

            boardView.setVisibility(View.INVISIBLE);
            distributorView.setVisibility(View.INVISIBLE);

            blackoutView.setVisibility(View.INVISIBLE);
            bottomBlackoutView.setVisibility(View.INVISIBLE);

            tutorialDescriptionTitleTextView.setText(R.string.tutorial_second_round_title);
            tutorialDescriptionMessageTextView.setText(R.string.tutorial_second_round_message);
            tutorialDescriptionView.setVisibility(View.VISIBLE);

            animator = ObjectAnimator.ofFloat(tutorialDescriptionView, View.ALPHA, 0f, 1f);
            animator.setDuration(DURATION);
            animator.start();
        }

        @Override
        void cancel() {
            if (animator != null) {
                animator.cancel();
            }

            tutorialDescriptionView.setVisibility(View.INVISIBLE);
        }
    };

    //Показ фигур Раунда 2
    private Action secondRoundShowing = new Action() {

        private BoardView.BoardItemView firstBoardItem;
        private BoardView.BoardItemView secondBoardItem;

        private Runnable showItemsRunnable;

        private boolean isFirstShowing;

        private static final int SHOW_ITEM_DELAY = 1500;

        @Override
        void start() {
            currentStage = TutorialStage.SECOND_ROUND_SHOWING;

            boardView.setVisibility(View.VISIBLE);
            distributorView.setVisibility(View.INVISIBLE);

            tutorialDescriptionView.setVisibility(View.INVISIBLE);

            isFirstShowing = true;

            firstBoardItem = (BoardView.BoardItemView)((ViewGroup) boardView.getChildAt(0)).getChildAt(0);
            secondBoardItem = (BoardView.BoardItemView)((ViewGroup) boardView.getChildAt(1)).getChildAt(2);

            firstBoardItem.createImageView(ItemType.EARTH);

            showItemsRunnable =  new Runnable() {
                @Override
                public void run() {
                    if (isFirstShowing) {
                        firstBoardItem.removeImageView();
                        secondBoardItem.createImageView(ItemType.MARS);

                        isFirstShowing = false;

                        boardView.postDelayed(this, SHOW_ITEM_DELAY);
                    } else {
                        secondBoardItem.removeImageView();

                        secondRoundGuessing.start();
                    }
                }
            };

            boardView.postDelayed(showItemsRunnable, SHOW_ITEM_DELAY);
        }

        @Override
        void cancel() {
            boardView.removeCallbacks(showItemsRunnable);

            boardView.setVisibility(View.INVISIBLE);
            distributorView.setVisibility(View.INVISIBLE);

            if (firstBoardItem != null) {
                firstBoardItem.removeImageView();
            }

            if (secondBoardItem != null) {
                secondBoardItem.removeImageView();
            }
        }
    };

    //Раунд 2 - Пользователь отгадывает
    private Action secondRoundGuessing = new Action() {

        private ImageView handImageView;

        private ObjectAnimator handAnimator;

        private boolean isFirstShowing;

        private BoardView.BoardItemView firstBoardItem;
        private BoardView.BoardItemView secondBoardItem;

        private BoardView.BoardItemView tutorialBoardItem;

        private static final int START_NEXT_STEP_DELAY = 500;

        @Override
        void start() {
            currentStage = TutorialStage.SECOND_ROUND_GUESSING;

            isFirstShowing = true;

            firstBoardItem = (BoardView.BoardItemView)((ViewGroup) boardView.getChildAt(0)).getChildAt(0);
            secondBoardItem = (BoardView.BoardItemView)((ViewGroup) boardView.getChildAt(1)).getChildAt(2);

            final float firstItemX = firstBoardItem.getX();
            final float firstItemY = firstBoardItem.getY();

            tutorialBoardItem = new BoardView.BoardItemView(TutorialActivity.this, firstBoardItem.getHeight());
            tutorialBoardItem.setX(firstItemX);
            tutorialBoardItem.setY(firstItemY);

            distributorView.setItems(new Rule[]{new Rule(1, ItemType.EARTH), new Rule(1, ItemType.MARS)});
            distributorView.post(new Runnable() {
                @Override
                public void run() {
                    if (firstItemX + firstBoardItem.getWidth() / 2 + distributorView.getWidth() / 2 >= boardView.getWidth()) {
                        distributorView.setX(boardView.getWidth() - distributorView.getWidth() - 10);
                    } else if(firstItemX + firstBoardItem.getWidth() / 2 - distributorView.getWidth() / 2 <= 0) {
                        distributorView.setX(10);
                    }else{
                        distributorView.setX(firstItemX + firstBoardItem.getWidth() / 2 - distributorView.getWidth() / 2);
                    }

                    distributorView.setY(firstItemY - distributorView.getHeight() + distributorView.getTriangleViewSizePx() / 2);
                    distributorView.setTriangleOffset((int)(firstBoardItem.getX() - distributorView.getX() + firstBoardItem.getWidth() / 2 - distributorView.getTriangleViewSizePx() / 2));
                }
            });

            tutorialBoardItem.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {

                        if (isFirstShowing) {
                            distributorView.setVisibility(View.VISIBLE);
                        } else {
                            distributorView.setVisibility(View.INVISIBLE);

                            secondBoardItem.createImageView(ItemType.MARS);

                            handAnimator.cancel();

                            blackoutView.removeAllViews();
                            blackoutView.setVisibility(View.INVISIBLE);
                            bottomBlackoutView.setVisibility(View.INVISIBLE);

                            tutorialBoardItem.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    firstBoardItem.removeImageView();
                                    secondBoardItem.removeImageView();

                                    thirdRoundDescription.start();
                                }
                            }, START_NEXT_STEP_DELAY);
                        }

                        handImageView.setVisibility(View.INVISIBLE);
                        handAnimator.cancel();

                        final DistributorView.DistributorItemView item = distributorView.getItem(0);
                        item.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                    firstBoardItem.createImageView(item.getItemType());

                                    distributorView.setVisibility(View.INVISIBLE);

                                    tutorialBoardItem.setX(secondBoardItem.getX());
                                    tutorialBoardItem.setY(secondBoardItem.getY());

                                    handImageView.setX(secondBoardItem.getX());
                                    handImageView.setY(secondBoardItem.getY() + secondBoardItem.getHeight());

                                    handImageView.setVisibility(View.VISIBLE);
                                    handAnimator.setFloatValues(secondBoardItem.getY() + secondBoardItem.getHeight(), secondBoardItem.getY() + secondBoardItem.getHeight() + 100);
                                    handAnimator.start();

                                    isFirstShowing = false;
                            }
                        });

                        if (isFirstShowing) {
                            item.setBackgroundResource(R.drawable.tutorial_distributor_selected_item_background);
                        }
                    }

                    return false;
                }
            });

            handImageView = new ImageView(TutorialActivity.this);
            handImageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            handImageView.setImageResource(R.drawable.tutorial_hand_icon);
            handImageView.setX(firstItemX);
            handImageView.setY(firstItemY + firstBoardItem.getHeight());

            blackoutView.addView(tutorialBoardItem);
            blackoutView.addView(handImageView);

            tutorialDescriptionView.setVisibility(View.INVISIBLE);

            boardView.setVisibility(View.VISIBLE);
            blackoutView.setVisibility(View.VISIBLE);
            bottomBlackoutView.setVisibility(View.VISIBLE);

            handAnimator = ObjectAnimator.ofFloat(handImageView, View.TRANSLATION_Y, firstItemY + firstBoardItem.getHeight(), firstItemY + firstBoardItem.getHeight() + 100);
            handAnimator.setRepeatCount(ValueAnimator.INFINITE);
            handAnimator.setRepeatMode(ValueAnimator.REVERSE);
            handAnimator.setDuration(1000);
            handAnimator.start();
        }

        @Override
        void cancel() {
            if (handAnimator != null) {
                handAnimator.cancel();
            }

            if (firstBoardItem != null) {
                firstBoardItem.removeImageView();
            }

            if (secondBoardItem != null) {
                secondBoardItem.removeImageView();
            }

            blackoutView.removeAllViews();
        }
    };


    //Раунд 3, описание
    private Action thirdRoundDescription = new Action() {

        private static final int DURATION = 2000;

        private ObjectAnimator animator;

        @Override
        void start() {
            currentStage = TutorialStage.THIRD_ROUND_DESCRIPTION;

            boardView.setVisibility(View.INVISIBLE);
            distributorView.setVisibility(View.INVISIBLE);

            blackoutView.setVisibility(View.INVISIBLE);
            bottomBlackoutView.setVisibility(View.INVISIBLE);

            tutorialDescriptionTitleTextView.setText(R.string.tutorial_third_round_title);
            tutorialDescriptionMessageTextView.setText(R.string.tutorial_second_round_message);
            tutorialDescriptionView.setVisibility(View.VISIBLE);

            animator = ObjectAnimator.ofFloat(tutorialDescriptionView, View.ALPHA, 0f, 1f);
            animator.setDuration(DURATION);
            animator.start();
        }

        @Override
        void cancel() {
            if (animator != null) {
                animator.cancel();
            }

            tutorialDescriptionView.setVisibility(View.INVISIBLE);
        }
    };

    //Раун 3 - Показ + Пользователь отгадывает
    private Action thirdRound = new Action() {

        private ImageView handImageView;

        private ObjectAnimator handAnimator;

        private BoardView.BoardItemView firstBoardItem;
        private BoardView.BoardItemView secondBoardItem;

        private BoardView.BoardItemView tutorialBoardItem;

        private static final int SHOW_ITEMS_DELAY = 1500;

        private static final int SHOW_IMAGE_DELAY = 1000;

        private static final int START_END_DIALOG_DELAY = 500;

        private boolean allItemAreSet;

        @Override
        void start() {
            currentStage = TutorialStage.THIRD_ROUND;

            boardView.setVisibility(View.VISIBLE);

            tutorialDescriptionView.setVisibility(View.INVISIBLE);

            firstBoardItem = (BoardView.BoardItemView)((ViewGroup) boardView.getChildAt(0)).getChildAt(1);
            secondBoardItem = (BoardView.BoardItemView)((ViewGroup) boardView.getChildAt(2)).getChildAt(2);

            firstBoardItem.createImageView(ItemType.EARTH);
            secondBoardItem.createImageView(ItemType.EARTH);

            boardView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    firstBoardItem.removeImageView();
                    secondBoardItem.removeImageView();

                    firstBoardItem = (BoardView.BoardItemView)((ViewGroup) boardView.getChildAt(1)).getChildAt(1);
                    secondBoardItem = (BoardView.BoardItemView)((ViewGroup) boardView.getChildAt(2)).getChildAt(0);

                    firstBoardItem.createImageView(ItemType.EARTH);
                    secondBoardItem.createImageView(ItemType.EARTH);

                    boardView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            firstBoardItem.removeImageView();
                            secondBoardItem.removeImageView();

                            boardView.post(new Runnable() {
                                @Override
                                public void run() {
                                    roundImageView.setVisibility(View.VISIBLE);
                                    //roundImageView.setImageResource(R.drawable.game_round_three_show_second);

                                    boardView.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            roundImageView.setVisibility(View.INVISIBLE);

                                            tutorialBoardItem = new BoardView.BoardItemView(TutorialActivity.this, firstBoardItem.getHeight());
                                            tutorialBoardItem.setX(firstBoardItem.getX());
                                            tutorialBoardItem.setY(firstBoardItem.getY());

                                            handImageView = new ImageView(TutorialActivity.this);
                                            handImageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                            handImageView.setImageResource(R.drawable.tutorial_hand_icon);
                                            handImageView.setX(firstBoardItem.getX());
                                            handImageView.setY(firstBoardItem.getY() + firstBoardItem.getHeight());

                                            blackoutView.addView(tutorialBoardItem);
                                            blackoutView.addView(handImageView);

                                            blackoutView.setVisibility(View.VISIBLE);
                                            bottomBlackoutView.setVisibility(View.VISIBLE);

                                            handAnimator = ObjectAnimator.ofFloat(handImageView, View.TRANSLATION_Y, firstBoardItem.getY() + firstBoardItem.getHeight(), firstBoardItem.getY() + firstBoardItem.getHeight() + 100);
                                            handAnimator.setRepeatCount(ValueAnimator.INFINITE);
                                            handAnimator.setRepeatMode(ValueAnimator.REVERSE);
                                            handAnimator.setDuration(1000);
                                            handAnimator.start();

                                            tutorialBoardItem.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if ( ! allItemAreSet) {
                                                        firstBoardItem.createImageView(ItemType.EARTH);

                                                        handImageView.setVisibility(View.INVISIBLE);
                                                        tutorialBoardItem.setVisibility(View.INVISIBLE);

                                                        handAnimator.cancel();

                                                        boardView.post(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                allItemAreSet = true;

                                                                tutorialBoardItem.setX(secondBoardItem.getX());
                                                                tutorialBoardItem.setY(secondBoardItem.getY());

                                                                handImageView.setX(secondBoardItem.getX());
                                                                handImageView.setY(secondBoardItem.getY() + secondBoardItem.getHeight());

                                                                handImageView.setVisibility(View.VISIBLE);
                                                                tutorialBoardItem.setVisibility(View.VISIBLE);

                                                                handAnimator.setFloatValues(secondBoardItem.getY() + secondBoardItem.getHeight(), secondBoardItem.getY() + secondBoardItem.getHeight() + 100);
                                                                handAnimator.start();
                                                            }
                                                        });
                                                    } else {
                                                        secondBoardItem.createImageView(ItemType.EARTH);
                                                        blackoutView.removeAllViews();

                                                        blackoutView.setVisibility(View.INVISIBLE);
                                                        bottomBlackoutView.setVisibility(View.INVISIBLE);

                                                        boardView.postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                showCompletedDialog();
                                                            }
                                                        }, START_END_DIALOG_DELAY);
                                                   }
                                                }
                                            });
                                        }
                                    }, SHOW_IMAGE_DELAY);
                                }
                            });
                        }
                    }, SHOW_ITEMS_DELAY);
                }
            }, SHOW_ITEMS_DELAY);
        }

        @Override
        void cancel() {
            if (handAnimator != null) {
                handAnimator.cancel();
            }

            if (firstBoardItem != null) {
                firstBoardItem.removeImageView();
            }

            if (secondBoardItem != null) {
                secondBoardItem.removeImageView();
            }

            blackoutView.removeAllViews();
            //boardView.setVisibility(View.INVISIBLE);
        }
    };

    private static final String TUTORIAL_COMPLETED_KEY = "tutorial_completed_key";

    private AlertDialog tutorialCompletedDialog;
    private void showCompletedDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(TutorialActivity.this);

        final View dialogView = LayoutInflater.from(TutorialActivity.this).inflate(R.layout.tutorial_dialog, null);

        final TextView checkView = (TextView) dialogView.findViewById(R.id.tutorial_dialog_check_button);
        checkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tutorialCompletedDialog != null) {
                    tutorialCompletedDialog.dismiss();
                }

                PreferenceManager.getDefaultSharedPreferences(TutorialActivity.this)
                        .edit()
                        .putBoolean(TUTORIAL_COMPLETED_KEY, true)
                        .apply();

                startActivity(new Intent(TutorialActivity.this, CampaignMenuActivity.class));

                finish();
            }
        });

        builder.setCancelable(false);
        builder.setView(dialogView);

        tutorialCompletedDialog = builder.create();
        tutorialCompletedDialog.show();
    }
}
