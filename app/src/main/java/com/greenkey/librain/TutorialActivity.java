package com.greenkey.librain;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.greenkey.librain.entity.ItemType;
import com.greenkey.librain.view.RatingBar;
import com.greenkey.librain.view.boardview.BoardView;
import com.greenkey.librain.view.distributorview.DistributorView;

import java.util.ArrayList;
import java.util.List;

public class TutorialActivity extends AppCompatActivity {

    private BoardView boardView;
    private DistributorView distributorView;

    private Button checkResultButton;

    //private RatingBar ratingBar;
    //private TextView levelNumberTextView;


    private View tutorialPanelView;
    private ImageView tutorialImageView;
    private TextView tutorialMessageTextView;

    private View roundView;
    private ImageView roundImageView;
    private View roundLineView;
    private TextView roundTitleTextView;
    private TextView roundDescriptionTextView;

    private View blackoutView;
    private View bottomBlackoutView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial_activity);

        boardView = (BoardView) findViewById(R.id.tutorial_board_view);
        distributorView = (DistributorView) findViewById(R.id.tutorial_distributor_view);

        checkResultButton = (Button) findViewById(R.id.tutorial_check_result_button);

        blackoutView = findViewById(R.id.tutorial_blackout_view);
        bottomBlackoutView = findViewById(R.id.tutorial_bottom_blackout_view);

        roundView = findViewById(R.id.tutorial_round_banner);

        tutorialPanelView = findViewById(R.id.tutorial_panel);
        tutorialImageView = (ImageView) tutorialPanelView.findViewById(R.id.tutorial_hand_image_view);
        tutorialMessageTextView = (TextView) tutorialPanelView.findViewById(R.id.tutorial_panel_text_text_view);

        currentStep = 1;
        actions = new ArrayList<>();
        actions.add(firstStep);
        actions.add(secondStep);
        /*
        boardView.post(new Runnable() {
            @Override
            public void run() {
                BoardView.BoardItemView boardItemView = (BoardView.BoardItemView)((ViewGroup) boardView.getChildAt(0)).getChildAt(1);

                float x = boardItemView.getX();
                float y = boardItemView.getY();

                final BoardView.BoardItemView newBoartItem = new BoardView.BoardItemView(TutorialActivity.this, boardItemView.getHeight());
                newBoartItem.setX(x);
                newBoartItem.setY(y);

                newBoartItem.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            newBoartItem.createImageView(ItemType.APRICOT);
                        }

                        return false;
                    }
                });

                ((ViewGroup)blackoutView).addView(newBoartItem);

                handDecriptionView = findViewById(R.id.tutorial_panel);
                handDecriptionView.setVisibility(View.VISIBLE);
                handDecriptionView.setX(x);
                handDecriptionView.setY(y + boardItemView.getHeight());

                handImageView = (ImageView) findViewById(R.id.tutorial_hand_image_view);
                handImageView.setVisibility(View.VISIBLE);

                ObjectAnimator animator = ObjectAnimator.ofFloat(handImageView, View.TRANSLATION_Y, 0, 100);
                animator.setRepeatCount(ValueAnimator.INFINITE);
                animator.setRepeatMode(ValueAnimator.REVERSE);
                animator.setDuration(800);
                animator.start();
            }
        });

*/

        /*
        ratingBar = (RatingBar) findViewById(R.id.stars);
        checkResultButton = (TextView) findViewById(R.id.check_result_button);
*/
        roundView = findViewById(R.id.tutorial_round_banner);
        roundImageView = (ImageView) roundView.findViewById(R.id.game_round_image_view);
        roundLineView = roundView.findViewById(R.id.game_round_line);
        roundTitleTextView = (TextView) roundView.findViewById(R.id.game_round_title_text_view);
        roundDescriptionTextView = (TextView) roundView.findViewById(R.id.game_round_description_text_view);
    }

    private int currentStep;

    @Override
    protected void onResume() {
        super.onResume();

        boardView.setVisibility(View.INVISIBLE);
        distributorView.setVisibility(View.INVISIBLE);

        roundView.setVisibility(View.INVISIBLE);
        checkResultButton.setVisibility(View.INVISIBLE);

        blackoutView.setVisibility(View.INVISIBLE);
        bottomBlackoutView.setVisibility(View.INVISIBLE);

        tutorialPanelView.setVisibility(View.VISIBLE);
        tutorialMessageTextView.setVisibility(View.VISIBLE);

        tutorialPanelView.setOnClickListener(new View.OnClickListener() {
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
                }
            }
        });

        firstStep.start();
    }

    private abstract class Action {
        abstract void start();
        abstract void cancel();
    }

    private List<Action> actions;

    private Action firstStep = new Action() {

        private ObjectAnimator firstMessageAnimator;

        @Override
        void start() {
            firstMessageAnimator = ObjectAnimator.ofFloat(tutorialMessageTextView, View.ALPHA, 0f, 1f);
            firstMessageAnimator.setDuration(5000);
            firstMessageAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    tutorialMessageTextView.setText(R.string.tutorial_first_message);
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
            firstMessageAnimator.cancel();
        }
    };

    private Action secondStep = new Action() {

        private ObjectAnimator secondMessageAnimator;

        @Override
        void start() {
            secondMessageAnimator = ObjectAnimator.ofFloat(tutorialMessageTextView, View.ALPHA, 0f, 1f);
            secondMessageAnimator.setDuration(5000);
            secondMessageAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    tutorialMessageTextView.setText(R.string.tutorial_second_message);
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
            secondMessageAnimator.cancel();
        }
    };

    private Action thirdStep = new Action() {

        private ObjectAnimator animator;

        @Override
        void start() {
            animator = ObjectAnimator.ofFloat(roundView, View.ALPHA, 0f, 1f);
            animator.setDuration(5000);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    tutorialPanelView.setVisibility(View.INVISIBLE);

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
                    ////
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

    private Action fourthStep = new Action() {
        @Override
        void start() {

        }

        @Override
        void cancel() {

        }
    };

}
