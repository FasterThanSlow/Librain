package com.greenkey.librain;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.greenkey.librain.campaign.Level;
import com.greenkey.librain.distributorview.DistributorItemView;
import com.greenkey.librain.distributorview.DistributorView;
import com.greenkey.librain.reciverview.BoardItemView;
import com.greenkey.librain.reciverview.BoardView;
import com.greenkey.librain.reciverview.OnBoardItemDragListener;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final int COLUMN_COUNT = 3;
    private static final int ROW_COUNT = 3;

    private StarsContainer starsContainer;

    private BoardView boardView;
    private DistributorView distributorView;

    private int rowCount;
    private int columnCount;

    private Rule[] rules;
    private ResourceType[] resourceItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boardView = (BoardView) findViewById(R.id.table_linear_layout);
        distributorView = (DistributorView) findViewById(R.id.distributorFrameLayout);

        rowCount = getIntent().getIntExtra("rowCount", ROW_COUNT);
        columnCount = getIntent().getIntExtra("columnCount", COLUMN_COUNT);

        starsContainer = (StarsContainer) findViewById(R.id.stars);

        int itemsTypesCount = getIntent().getIntExtra("itemsTypeCount", 2);
        int itemsCount = getIntent().getIntExtra("itemsCount", 4);

        Rule rule1 = new Rule(itemsCount / itemsTypesCount, ResourceType.EU);
        Rule rule2 = new Rule(itemsCount / itemsTypesCount, ResourceType.GB);
        Rule rule3 = new Rule(itemsCount / itemsTypesCount + itemsCount % itemsTypesCount, ResourceType.EN);

        rules = new Rule[itemsTypesCount];

        System.arraycopy(new Rule[] {rule1, rule2, rule3}, 0, rules, 0, itemsTypesCount);

        final Level level = getIntent().getParcelableExtra("level");
        if (level != null) {
            rowCount = level.getRowCount();
            columnCount = level.getColumnCount();

            rules = level.getRules();

        }

        resourceItems = ItemGenerator.createItems(rules, rowCount * columnCount);

        boardView.createItems(rowCount, columnCount);
        boardView.setItemsDragListener(new DragListenerWrapper(distributorView));

        distributorView.setItems(rules);

        final Button button = (Button) findViewById(R.id.knopka);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( ! distributorView.allItemsResourcesUsed()) {
                    Toast.makeText(MainActivity.this, "Не все элементы испольованы", Toast.LENGTH_SHORT).show();
                } else {
                    ResourceType[] userAnswer = boardView.getItemsResources();
                    if (Arrays.equals(userAnswer, resourceItems)) {
                        Toast.makeText(MainActivity.this, "Красавчик", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Не красавчик", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        Button restartButton = (Button) findViewById(R.id.restartKnopka);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resourceItems = ItemGenerator.createItems(rules, rowCount * columnCount);

                distributorView.setItems(rules);
                distributorView.setItemsImageViewOnTouchListener(null);

                boardView.setItemsImageViewOnTouchListener(null);
                boardView.setItemsResources(resourceItems);

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
                    boardView.setItemsResources(resourceItems);
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

        public DragListenerWrapper(DistributorView distributorView) {
            this.distributorView = distributorView;
        }

        private boolean dragIsSupported = true;

        @Override
        public boolean onBoardItemDrag(BoardItemView receiverView, DragEvent event) {

            final ImageView dragView = (ImageView) event.getLocalState();
            final ViewParent dragViewParent = dragView.getParent();

            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    receiverView.setBackgroundResource(R.drawable.exist_shape);

                    if (dragIsSupported) {
                        if (dragViewParent instanceof BoardItemView) {
                            dragView.setVisibility(View.INVISIBLE);
                        } else if (dragViewParent instanceof DistributorItemView) {
                            ((DistributorItemView) dragViewParent).removeImageView();
                        }

                        dragIsSupported = false;
                    }
                    /*
                    if (dragViewParent instanceof DistributorItemView) {
                        if (dragIsSupported) {
                            Log.d("ACTION_DRAG", "ACTION_DRAG_STARTED");

                            ((DistributorItemView) dragViewParent).removeImageView();
                            dragIsSupported = false;
                        }
                    }*/
                    break;

                case DragEvent.ACTION_DRAG_EXITED:
                    receiverView.setBackgroundResource(R.drawable.exist_shape);
                    break;

                case DragEvent.ACTION_DRAG_ENTERED :
                    receiverView.setBackgroundResource(R.drawable.enterd_shape);
                    break;

                case DragEvent.ACTION_DRAG_ENDED:
                    receiverView.setBackgroundResource(R.drawable.normal_shape);

                    if ( ! dragIsSupported) {
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

                        dragIsSupported = true;
                    }
                    /*
                    if (dragViewParent instanceof BoardItemView) {

                        ResourceType resourceType = ((BoardItemView)dragViewParent).getResourceType();
                        ((BoardItemView)dragViewParent).removeImageView();

                        DistributorItemView itemView = distributorView.findItem(resourceType);
                        if (itemView != null) {
                            itemView.addImageView();
                        }

                        dragView.post(new Runnable() {
                            @Override
                            public void run() {
                                dragView.setVisibility(View.VISIBLE);
                            }
                        });
                    } else if (dragViewParent instanceof DistributorItemView) {
                        if ( ! dragIsSupported) {
                            if (!isDropped(event)) {
                                ((DistributorItemView) dragViewParent).addImageView();
                                Log.d("ACTION_DRAG", "ACTION_DRAG_ENDED");
                            }
                            dragIsSupported = true;
                        }
                    }
                    */
                    break;

                case DragEvent.ACTION_DROP:
                    ViewParent viewParent = dragView.getParent();

                    if (viewParent instanceof DistributorItemView) {
                        DistributorItemView distributorItemView = (DistributorItemView) viewParent;

                        if (receiverView.hasImageView()) {
                            ResourceType resourceType = receiverView.getResourceType();

                            DistributorItemView founddistributorItemView = ((DistributorView) distributorItemView.getParent()).findItem(resourceType);
                            if (founddistributorItemView != null) {
                                founddistributorItemView.addImageView();
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
                    break;
            }

            return true;
        }

        private boolean isDropped(DragEvent event) {
            return event.getResult();
        }
    }
}
