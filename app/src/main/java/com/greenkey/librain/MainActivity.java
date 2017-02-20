package com.greenkey.librain;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.greenkey.librain.campaign.Level;
import com.greenkey.librain.campaign.LevelGenerator;
import com.greenkey.librain.distributorview.DistributorItemView;
import com.greenkey.librain.distributorview.DistributorView;
import com.greenkey.librain.reciverview.BoardItemView;
import com.greenkey.librain.reciverview.BoardView;
import com.greenkey.librain.reciverview.OnBoardItemDragListener;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final String LEVEL_PARAM = "level";

    private int trueAnswersCount;
    private int countTries;
    private static final int MAX_TRIES_COUNT = 3;

    private int levelNumber;

    private RatingBar ratingBar;
    private TextView confirmTextView;

    private BoardView boardView;
    private DistributorView distributorView;

    private int rowCount;
    private int columnCount;

    private Rule[] rules;
    private ResourceType[] resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Level level = getIntent().getParcelableExtra(LEVEL_PARAM);
        if (level != null) {
            levelNumber = level.getLevelNumber();

            rowCount = level.getRowCount();
            columnCount = level.getColumnCount();

            rules = level.getRules();
        }

        boardView = (BoardView) findViewById(R.id.board_view);
        distributorView = (DistributorView) findViewById(R.id.distributor_view);
        ratingBar = (RatingBar) findViewById(R.id.stars);
        confirmTextView = (TextView) findViewById(R.id.confirm_text_view);

        distributorView.createItems(rules);

        boardView.createItems(rowCount, columnCount);
        boardView.setItemsDragListener(new DragListenerWrapper(distributorView, confirmTextView));

        confirmTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                distributorView.setVisibility(View.VISIBLE);
                confirmTextView.setVisibility(View.GONE);

                ResourceType[] userAnswer = boardView.getItemsResources();
                    if (Arrays.equals(userAnswer, resources)) {
                        trueAnswersCount++;
                        ratingBar.setProgress(trueAnswersCount);


                    } else {

                        //Toast.makeText(MainActivity.this, "Не красавчик", Toast.LENGTH_SHORT).show();

                    }

                countTries++;

                if (countTries < MAX_TRIES_COUNT) {
                    boardView.post(start);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);


                    builder.setTitle("Уровень пройден!")
                            .setMessage("Звёзд: " + trueAnswersCount)
                            .setCancelable(false)
                            .setPositiveButton("Далее", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();

                                    Level nextLevel = LevelGenerator.findLevel(levelNumber + 1);

                                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                    intent.putExtra(LEVEL_PARAM, nextLevel);

                                    startActivity(intent);

                                    MainActivity.this.finish();
                                }
                            })
                            .setNegativeButton("Выход",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();

                                            MainActivity.this.finish();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();                }
            }
        });

        final ImageView restartButton = (ImageView) findViewById(R.id.restartKnopka);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                distributorView.setItemsImageViewOnTouchListener(null);
                boardView.setItemsImageViewOnTouchListener(null);

                boardView.post(start);
            }
        });

        boardView.post(start);
    }

    private Runnable start = new Runnable() {
        @Override
        public void run() {
            ObjectAnimator animator = ObjectAnimator.ofFloat(boardView, View.ALPHA, 0f, 1f);
            animator.setDuration(1000);
            animator.start();

            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    resources = BoardViewItemGenerator.createResources(rules, rowCount * columnCount);

                    distributorView.setRules(rules);

                    boardView.setItemsResources(resources);
                    boardView.postDelayed(end, 5000);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
    };

    private Runnable end = new Runnable() {
        @Override
        public void run() {
            boardView.removeItemsResources();

            boardView.setItemsImageViewOnTouchListener(touchListener);
            distributorView.setItemsImageViewOnTouchListener(touchListener);
        }
    };


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
