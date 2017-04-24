package com.greenkey.librain;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.greenkey.librain.entity.ItemType;
import com.greenkey.librain.entity.Rule;
import com.greenkey.librain.view.boardview.BoardView;
import com.greenkey.librain.view.distributorview.DistributorView;

public class TutorialActivity extends AppCompatActivity implements View.OnClickListener {

    private BoardView boardView;
    private DistributorView distributorView;

    private Button checkResultButton;

    //private RatingBar ratingBar;
    //private TextView levelNumberTextView;

    private TextView tutorialMessageTextView;

    private View roundView;
    private ImageView roundImageView;
    private View roundLineView;
    private TextView roundTitleTextView;
    private TextView roundDescriptionTextView;

    private FrameLayout blackoutView;
    private View bottomBlackoutView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial_activity);

        boardView = (BoardView) findViewById(R.id.tutorial_board_view);
        distributorView = (DistributorView) findViewById(R.id.tutorial_distributor_view);

        checkResultButton = (Button) findViewById(R.id.tutorial_check_result_button);

        blackoutView = (FrameLayout) findViewById(R.id.tutorial_blackout_view);
        bottomBlackoutView = findViewById(R.id.tutorial_bottom_blackout_view);

        tutorialMessageTextView = (TextView) findViewById(R.id.tutorial_panel_text_text_view);

        roundView = findViewById(R.id.tutorial_round_banner);
        roundImageView = (ImageView) roundView.findViewById(R.id.game_round_image_view);
        roundLineView = roundView.findViewById(R.id.game_round_line);
        roundTitleTextView = (TextView) roundView.findViewById(R.id.game_round_title_text_view);
        roundDescriptionTextView = (TextView) roundView.findViewById(R.id.game_round_description_text_view);

        currentStep = 1;

        roundView.setOnClickListener(this);
        tutorialMessageTextView.setOnClickListener(this);

        /*
        ratingBar = (RatingBar) findViewById(R.id.stars);
        checkResultButton = (TextView) findViewById(R.id.check_result_button);
*/
    }

    @Override
    public void onClick(View v) {
        switch (currentStep) {
            case 1:
                firstStep.cancel();
                secondStep.start();

                currentStep++;
                break;
            case 2:
                secondStep.cancel();
                thirdStep.start();

                currentStep++;
                break;
            case 3:
                thirdStep.cancel();
                fourthStep.start();

                currentStep++;
                break;
            case 6:
                seventhStep.cancel();
                eighthStep.start();

                currentStep++;
                break;
            case 11:
                twelfthStep.cancel();
                thirteenthStep.start();

                currentStep++;
                break;
        }
    }

    private int currentStep;

    @Override
    protected void onResume() {
        super.onResume();

        firstStep.start();
    }

    private abstract class Action {
        abstract void start();
        abstract void cancel();
    }

    //Привет, это обучение
    private Action firstStep = new Action() {

        private static final int DURATION = 2000;

        private ObjectAnimator firstMessageAnimator;

        @Override
        void start() {
            boardView.setVisibility(View.INVISIBLE);
            distributorView.setVisibility(View.INVISIBLE);

            roundView.setVisibility(View.INVISIBLE);
            checkResultButton.setVisibility(View.INVISIBLE);

            blackoutView.setVisibility(View.INVISIBLE);
            bottomBlackoutView.setVisibility(View.INVISIBLE);

            tutorialMessageTextView.setVisibility(View.VISIBLE);

            firstMessageAnimator = ObjectAnimator.ofFloat(tutorialMessageTextView, View.ALPHA, 0f, 1f);
            firstMessageAnimator.setDuration(DURATION);
            firstMessageAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    tutorialMessageTextView.setText(R.string.tutorial_start_message);
                }

                @Override
                public void onAnimationEnd(Animator animation) {

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            firstMessageAnimator.start();
        }

        @Override
        void cancel() {
            if (firstMessageAnimator != null) {
                firstMessageAnimator.cancel();
            }
        }
    };

    //Запомни фигуры
    private Action secondStep = new Action() {

        private static final int DURATION = 2000;

        private ObjectAnimator secondMessageAnimator;

        @Override
        void start() {
            boardView.setVisibility(View.INVISIBLE);
            distributorView.setVisibility(View.INVISIBLE);

            roundView.setVisibility(View.INVISIBLE);
            checkResultButton.setVisibility(View.INVISIBLE);

            blackoutView.setVisibility(View.INVISIBLE);
            bottomBlackoutView.setVisibility(View.INVISIBLE);

            tutorialMessageTextView.setVisibility(View.VISIBLE);

            secondMessageAnimator = ObjectAnimator.ofFloat(tutorialMessageTextView, View.ALPHA, 0f, 1f);
            secondMessageAnimator.setDuration(DURATION);
            secondMessageAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    tutorialMessageTextView.setText(R.string.tutorial_first_round_message);
                }

                @Override
                public void onAnimationEnd(Animator animation) {

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            secondMessageAnimator.start();
        }

        @Override
        void cancel() {
            if (secondMessageAnimator != null) {
                secondMessageAnimator.cancel();
            }
        }
    };

    //Надпись Раунд 1
    private Action thirdStep = new Action() {

        private static final int DURATION = 2000;

        private ObjectAnimator animator;

        @Override
        void start() {
            animator = ObjectAnimator.ofFloat(roundView, View.ALPHA, 0f, 1f);
            animator.setDuration(DURATION);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    tutorialMessageTextView.setVisibility(View.INVISIBLE);

                    roundView.setVisibility(View.VISIBLE);
                    roundImageView.setVisibility(View.GONE);
                    roundLineView.setVisibility(View.VISIBLE);
                    roundTitleTextView.setVisibility(View.VISIBLE);
                    roundDescriptionTextView.setVisibility(View.VISIBLE);

                    roundTitleTextView.setText(R.string.game_round_1_title);
                    roundDescriptionTextView.setText(R.string.game_round_1_description);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    currentStep++;
                    fourthStep.start();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.start();
        }

        @Override
        void cancel() {
            if (animator != null) {
                animator.removeAllListeners();
                animator.cancel();
            }
        }
    };

    //Показ фигур РАунда 1
    private Action fourthStep = new Action() {

        private static final int DURATION = 1000;

        private BoardView.BoardItemView shownBoardItemView;

        private Runnable showItemRunnable;

        @Override
        void start() {
            roundView.setVisibility(View.INVISIBLE);
            tutorialMessageTextView.setVisibility(View.INVISIBLE);

            boardView.setVisibility(View.VISIBLE);

            shownBoardItemView = (BoardView.BoardItemView)((ViewGroup) boardView.getChildAt(0)).getChildAt(1);
            shownBoardItemView.createImageView(ItemType.APRICOT);

            showItemRunnable = new Runnable() {
                @Override
                public void run() {
                    shownBoardItemView.removeImageView();

                    fifthStep.start();
                }
            };

            shownBoardItemView.postDelayed(showItemRunnable, DURATION);
        }

        @Override
        void cancel() {
            if (shownBoardItemView != null) {
                shownBoardItemView.removeCallbacks(showItemRunnable);
            }
        }
    };

    //Текст - Покажи фигруы
    private Action fifthStep = new Action() {
        private static final int DURATION = 2000;

        private ObjectAnimator animator;

        @Override
        void start() {
            boardView.setVisibility(View.INVISIBLE);
            distributorView.setVisibility(View.INVISIBLE);

            roundView.setVisibility(View.INVISIBLE);
            checkResultButton.setVisibility(View.INVISIBLE);

            blackoutView.setVisibility(View.INVISIBLE);
            bottomBlackoutView.setVisibility(View.INVISIBLE);

            tutorialMessageTextView.setVisibility(View.VISIBLE);

            animator = ObjectAnimator.ofFloat(tutorialMessageTextView, View.ALPHA, 0f, 1f);
            animator.setDuration(DURATION);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    tutorialMessageTextView.setText(R.string.tutorial_first_round_description_message);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    currentStep++;
                    sixthStep.start();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.start();
        }

        @Override
        void cancel() {
            if (animator != null) {
                animator.removeAllListeners();
                animator.cancel();
            }
        }
    };

    //Картинка - Тыкни по ним    /////////////КНОПКА!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private Action sixthStep = new Action() {

        private ObjectAnimator handAnimator;

        private static final int START_NEXT_STEP_DELAY = 500;

        @Override
        void start() {
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
                        tutorialBoardItem.createImageView(ItemType.APRICOT);

                        tutorialBoardItem.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                blackoutView.removeAllViews();

                                currentStep++;

                                seventhStep.start();
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

            roundView.setVisibility(View.INVISIBLE);
            tutorialMessageTextView.setVisibility(View.INVISIBLE);

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

    //ОПИСАНИЕ РАУНДА 2
    private Action seventhStep = new Action() {

        private static final int DURATION = 2000;

        private ObjectAnimator animator;

        @Override
        void start() {
            boardView.setVisibility(View.INVISIBLE);
            distributorView.setVisibility(View.INVISIBLE);

            roundView.setVisibility(View.INVISIBLE);
            checkResultButton.setVisibility(View.INVISIBLE);

            blackoutView.setVisibility(View.INVISIBLE);
            bottomBlackoutView.setVisibility(View.INVISIBLE);

            tutorialMessageTextView.setVisibility(View.VISIBLE);

            animator = ObjectAnimator.ofFloat(tutorialMessageTextView, View.ALPHA, 0f, 1f);
            animator.setDuration(DURATION);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    tutorialMessageTextView.setText(R.string.tutorial_pre_second_round_message);
                }

                @Override
                public void onAnimationEnd(Animator animation) {

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.start();
        }

        @Override
        void cancel() {
            if (animator != null) {
                animator.cancel();
            }
        }
    };

    //Надпись Раунд 2
    private Action eighthStep = new Action() {

        private static final int DURATION = 2000;

        private ObjectAnimator animator;

        @Override
        void start() {
            animator = ObjectAnimator.ofFloat(roundView, View.ALPHA, 0f, 1f);
            animator.setDuration(DURATION);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    tutorialMessageTextView.setVisibility(View.INVISIBLE);

                    roundView.setVisibility(View.VISIBLE);
                    roundImageView.setVisibility(View.GONE);
                    roundLineView.setVisibility(View.VISIBLE);
                    roundTitleTextView.setVisibility(View.VISIBLE);
                    roundDescriptionTextView.setVisibility(View.VISIBLE);

                    roundTitleTextView.setText(R.string.game_round_2_title);
                    roundDescriptionTextView.setText(R.string.game_round_2_description);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    currentStep++;
                    ninthStep.start();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.start();
        }

        @Override
        void cancel() {
            if (animator != null) {
                animator.removeAllListeners();
                animator.cancel();
            }
        }
    };

    //ПОКАЗ ФИГУР РАУНДА 2
    private Action ninthStep = new Action() {

        private BoardView.BoardItemView firstBoardItem;
        private BoardView.BoardItemView secondBoardItem;

        private Runnable showItemsRunnable;

        private boolean isFirstShowing;

        private static final int SHOW_ITEM_DELAY = 1500;

        @Override
        void start() {
            boardView.setVisibility(View.VISIBLE);
            distributorView.setVisibility(View.INVISIBLE);

            roundView.setVisibility(View.INVISIBLE);
            tutorialMessageTextView.setVisibility(View.INVISIBLE);

            isFirstShowing = true;

            firstBoardItem = (BoardView.BoardItemView)((ViewGroup) boardView.getChildAt(0)).getChildAt(0);
            secondBoardItem = (BoardView.BoardItemView)((ViewGroup) boardView.getChildAt(1)).getChildAt(2);

            firstBoardItem.createImageView(ItemType.BANANAS);

            showItemsRunnable =  new Runnable() {
                @Override
                public void run() {
                    if (isFirstShowing) {
                        firstBoardItem.removeImageView();
                        secondBoardItem.createImageView(ItemType.ORANGE);

                        isFirstShowing = false;

                        boardView.postDelayed(this, SHOW_ITEM_DELAY);
                    } else {
                        secondBoardItem.removeImageView();

                        currentStep++;
                        tenthSteep.start();
                    }
                }
            };

            boardView.postDelayed(showItemsRunnable, SHOW_ITEM_DELAY);
        }

        @Override
        void cancel() {
            boardView.removeCallbacks(showItemsRunnable);
        }
    };

    //Описание что сделать Раунда 2  - НУЖНО ЛИ?
    private Action tenthSteep = new Action() {
        private static final int DURATION = 2000;

        private ObjectAnimator animator;

        @Override
        void start() {
            boardView.setVisibility(View.INVISIBLE);
            distributorView.setVisibility(View.INVISIBLE);

            roundView.setVisibility(View.INVISIBLE);
            checkResultButton.setVisibility(View.INVISIBLE);

            blackoutView.setVisibility(View.INVISIBLE);
            bottomBlackoutView.setVisibility(View.INVISIBLE);

            tutorialMessageTextView.setVisibility(View.VISIBLE);

            animator = ObjectAnimator.ofFloat(tutorialMessageTextView, View.ALPHA, 0f, 1f);
            animator.setDuration(DURATION);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    tutorialMessageTextView.setText(R.string.tutorial_second_round_description_message);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    currentStep++;
                    eleventhStep.start();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.start();
        }

        @Override
        void cancel() {
            if (animator != null) {
                animator.removeAllListeners();
                animator.cancel();
            }
        }
    };

    //РАУНД 2
    private Action eleventhStep = new Action() {

        private ImageView handImageView;

        private ObjectAnimator handAnimator;

        private boolean isFirstShowing;

        private BoardView.BoardItemView firstBoardItem;
        private BoardView.BoardItemView secondBoardItem;

        private BoardView.BoardItemView tutorialBoardItem;

        private static final int START_NEXT_STEP_DELAY = 500;

        @Override
        void start() {
            isFirstShowing = true;

            firstBoardItem = (BoardView.BoardItemView)((ViewGroup) boardView.getChildAt(0)).getChildAt(0);
            secondBoardItem = (BoardView.BoardItemView)((ViewGroup) boardView.getChildAt(1)).getChildAt(2);

            final float firstItemX = firstBoardItem.getX();
            final float firstItemY = firstBoardItem.getY();

            tutorialBoardItem = new BoardView.BoardItemView(TutorialActivity.this, firstBoardItem.getHeight());
            tutorialBoardItem.setX(firstItemX);
            tutorialBoardItem.setY(firstItemY);

            distributorView.setItems(new Rule[]{new Rule(1, ItemType.BANANAS), new Rule(1, ItemType.ORANGE)});
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

                            secondBoardItem.createImageView(ItemType.ORANGE);

                            handAnimator.cancel();

                            blackoutView.removeAllViews();
                            blackoutView.setVisibility(View.INVISIBLE);
                            bottomBlackoutView.setVisibility(View.INVISIBLE);

                            currentStep++;

                            tutorialBoardItem.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    firstBoardItem.removeImageView();
                                    secondBoardItem.removeImageView();

                                    twelfthStep.start();
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
                                    //item.removeImageView();

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
                            item.setBackgroundColor(Color.GRAY);
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

            roundView.setVisibility(View.INVISIBLE);
            tutorialMessageTextView.setVisibility(View.INVISIBLE);

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
        }
    };

    //Раунд 3, описание
    private Action twelfthStep = new Action() {

        private static final int DURATION = 2000;

        private ObjectAnimator secondMessageAnimator;

        @Override
        void start() {
            boardView.setVisibility(View.INVISIBLE);
            distributorView.setVisibility(View.INVISIBLE);

            roundView.setVisibility(View.INVISIBLE);
            checkResultButton.setVisibility(View.INVISIBLE);

            blackoutView.setVisibility(View.INVISIBLE);
            bottomBlackoutView.setVisibility(View.INVISIBLE);

            tutorialMessageTextView.setVisibility(View.VISIBLE);

            secondMessageAnimator = ObjectAnimator.ofFloat(tutorialMessageTextView, View.ALPHA, 0f, 1f);
            secondMessageAnimator.setDuration(DURATION);
            secondMessageAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    tutorialMessageTextView.setText(R.string.tutorial_third_round_message);
                }

                @Override
                public void onAnimationEnd(Animator animation) {

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            secondMessageAnimator.start();
        }

        @Override
        void cancel() {
            if (secondMessageAnimator != null) {
                secondMessageAnimator.cancel();
            }
        }
    };

    //Надпись Раунд 3
    private Action thirteenthStep = new Action() {

        private static final int DURATION = 2000;

        private ObjectAnimator animator;

        @Override
        void start() {
            animator = ObjectAnimator.ofFloat(roundView, View.ALPHA, 0f, 1f);
            animator.setDuration(DURATION);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    tutorialMessageTextView.setVisibility(View.INVISIBLE);

                    roundView.setVisibility(View.VISIBLE);
                    roundImageView.setVisibility(View.GONE);
                    roundLineView.setVisibility(View.VISIBLE);
                    roundTitleTextView.setVisibility(View.VISIBLE);
                    roundDescriptionTextView.setVisibility(View.VISIBLE);

                    roundTitleTextView.setText(R.string.game_round_3_title);
                    roundDescriptionTextView.setText(R.string.game_round_3_description);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    currentStep++;
                    fourteenthStep.start();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.start();
        }

        @Override
        void cancel() {
            if (animator != null) {
                animator.removeAllListeners();
                animator.cancel();
            }
        }
    };

    //Раун 3
    private Action fourteenthStep = new Action() {

        private ImageView handImageView;

        private ObjectAnimator handAnimator;

        private boolean isFirstShowing;

        private BoardView.BoardItemView firstBoardItem;
        private BoardView.BoardItemView secondBoardItem;

        private BoardView.BoardItemView tutorialBoardItem;

        private static final int SHOW_DELAY = 1500;

        private static final int START_NEXT_STEP_DELAY = 500;

        private boolean allItemAreSet;

        @Override
        void start() {
            isFirstShowing = true;

            boardView.setVisibility(View.VISIBLE);

            roundView.setVisibility(View.INVISIBLE);
            tutorialMessageTextView.setVisibility(View.INVISIBLE);

            firstBoardItem = (BoardView.BoardItemView)((ViewGroup) boardView.getChildAt(0)).getChildAt(1);
            secondBoardItem = (BoardView.BoardItemView)((ViewGroup) boardView.getChildAt(2)).getChildAt(2);

            firstBoardItem.createImageView(ItemType.CHERRY);
            secondBoardItem.createImageView(ItemType.CHERRY);

            boardView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    firstBoardItem.removeImageView();
                    secondBoardItem.removeImageView();

                    firstBoardItem = (BoardView.BoardItemView)((ViewGroup) boardView.getChildAt(1)).getChildAt(1);
                    secondBoardItem = (BoardView.BoardItemView)((ViewGroup) boardView.getChildAt(2)).getChildAt(0);

                    firstBoardItem.createImageView(ItemType.CHERRY);
                    secondBoardItem.createImageView(ItemType.CHERRY);

                    boardView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            firstBoardItem.removeImageView();
                            secondBoardItem.removeImageView();

                            boardView.post(new Runnable() {
                                @Override
                                public void run() {

                                    roundImageView.setVisibility(View.VISIBLE);

                                    roundLineView.setVisibility(View.GONE);
                                    roundTitleTextView.setVisibility(View.GONE);
                                    roundDescriptionTextView.setVisibility(View.GONE);

                                    roundImageView.setImageResource(R.drawable.game_round_three_show_second);

                                    roundView.setVisibility(View.VISIBLE);

                                    boardView.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            roundView.setVisibility(View.INVISIBLE);

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
                                                        firstBoardItem.createImageView(ItemType.CHERRY);

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
                                                        boardView.postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                secondBoardItem.createImageView(ItemType.CHERRY);

                                                                blackoutView.removeAllViews();

                                                                Toast.makeText(TutorialActivity.this, "PROSHEL!", Toast.LENGTH_LONG).show();
                                                            }
                                                        }, START_NEXT_STEP_DELAY);
                                                   }
                                                }
                                            });
                                        }
                                    }, 1000);
                                }
                            });
                        }
                    }, SHOW_DELAY);
                }
            }, SHOW_DELAY);
        }

        @Override
        void cancel() {
            if (handAnimator != null) {
                handAnimator.cancel();
            }
        }
    };


}
