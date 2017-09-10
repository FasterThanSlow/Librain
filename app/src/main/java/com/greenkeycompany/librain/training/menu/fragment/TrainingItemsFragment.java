package com.greenkeycompany.librain.training.menu.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.greenkeycompany.librain.R;
import com.greenkeycompany.librain.entity.ItemType;
import com.greenkeycompany.librain.entity.Rule;
import com.greenkeycompany.librain.level.Generator;
import com.greenkeycompany.librain.level.Level;
import com.greenkeycompany.librain.app.view.boardview.BoardView;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class TrainingItemsFragment extends Fragment {

    private static final int MINIMUM_ITEM_COUNt = 2;

    private static final String COLUMN_COUNT_KEY = "column_count";
    private static final String ROW_COUNT_KEY = "row_count";

    private static final String TYPE_COUNT = "type_count";
    private static final String ITEM_COUNT = "item_count";

    private static final Level.LevelType LEVEL_TYPE = Level.LevelType.FRUIT;

    private int columnCount;
    private int rowCount;

    private int itemTypeCount;
    private int itemCount;

    public interface ItemsFragmentListener {
        void onItemsFragmentNext(int itemTypeCount, int itemCount);
        void onItemsFragmentPrevious();
    }

    public TrainingItemsFragment() {
    }

    public static TrainingItemsFragment newInstance(int rowCount, int columnCount, int itemTypeCount, int itemCount) {
        TrainingItemsFragment fragment = new TrainingItemsFragment();
        Bundle args = new Bundle();
        args.putInt(ROW_COUNT_KEY, rowCount);
        args.putInt(COLUMN_COUNT_KEY, columnCount);
        args.putInt(TYPE_COUNT, itemTypeCount);
        args.putInt(ITEM_COUNT, itemCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            columnCount = getArguments().getInt(COLUMN_COUNT_KEY);
            rowCount = getArguments().getInt(ROW_COUNT_KEY);

            itemTypeCount = getArguments().getInt(TYPE_COUNT);
            itemCount = getArguments().getInt(ITEM_COUNT);

            if (columnCount * rowCount < itemCount) {
                itemCount = MINIMUM_ITEM_COUNt;
                itemTypeCount = 1;
            }
        }
    }

    @BindView(R.id.board_view) BoardView boardView;
    @BindViews({
            R.id.item_types_1_view,
            R.id.item_types_2_view,
            R.id.item_types_3_view,
            R.id.item_types_4_view
    }) List<View> itemTypeCountViewList;

    private View selectedTypeCountView;

    @BindView(R.id.item_count_seek_bar) SeekBar itemCountSeekBar;
    @BindView(R.id.item_count_text_view) TextView itemCountTextView;

    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View parentView = inflater.inflate(R.layout.training_items_fragment, container, false);

        unbinder = ButterKnife.bind(this, parentView);

        boardView.createItems(rowCount, columnCount);
        boardView.post(new Runnable() {
            @Override
            public void run() {
                resetBoardItems();
            }
        });

        selectedTypeCountView = itemTypeCountViewList.get(itemTypeCount - 1);
        selectedTypeCountView.setBackgroundResource(R.drawable.training_item_type_count_background_selected);

        itemCountTextView.setText(String.valueOf(itemCount));

        itemCountSeekBar.setMax(rowCount * columnCount - MINIMUM_ITEM_COUNt);
        itemCountSeekBar.setProgress(itemCount - MINIMUM_ITEM_COUNt);
        itemCountSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setItemCount(progress + MINIMUM_ITEM_COUNt);
                resetBoardItems();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                resetBoardItems();
            }
        });

        setItemCount(itemCount);

        return parentView;
    }

    @OnClick({R.id.item_types_1_view, R.id.item_types_2_view, R.id.item_types_3_view, R.id.item_types_4_view})
    public void onItemTypesViewClick(View view) {
        selectedTypeCountView.setBackgroundResource(R.drawable.training_item_type_count_background);

        selectedTypeCountView = view;
        selectedTypeCountView.setBackgroundResource(R.drawable.training_item_type_count_background_selected);

        switch (view.getId()) {
            case R.id.item_types_1_view: itemTypeCount = 1; break;
            case R.id.item_types_2_view: itemTypeCount = 2; break;
            case R.id.item_types_3_view: itemTypeCount = 3; break;
            case R.id.item_types_4_view: itemTypeCount = 4; break;
        }

        resetBoardItems();
    }

    @OnClick(R.id.previous_button)
    public void onPreviousButtonClick() {
        fragmentListener.onItemsFragmentPrevious();
    }

    @OnClick(R.id.next_button)
    public void onNextButtonClick() {
        fragmentListener.onItemsFragmentNext(itemTypeCount, itemCount);
    }

    private void resetBoardItems() {
        Rule[] rules = Generator.createRulesForTraining(LEVEL_TYPE, itemTypeCount,  itemCount);
        ItemType[] itemTypes = Generator.createFullBoardItems(rules, rowCount * columnCount);

        boardView.setItemsResources(itemTypes);
    }

    private void setItemCount(int itemCount) {
        this.itemCount = itemCount;
        this.itemCountTextView.setText(String.valueOf(itemCount));

        switch (itemCount) {
            case 2:
                itemTypeCountViewList.get(2).setVisibility(View.INVISIBLE);
                itemTypeCountViewList.get(3).setVisibility(View.INVISIBLE);

                selectedTypeCountView.setBackgroundResource(R.drawable.training_item_type_count_background);

                itemTypeCount = 1;
                selectedTypeCountView = itemTypeCountViewList.get(0);
                selectedTypeCountView.setBackgroundResource(R.drawable.training_item_type_count_background_selected);
                break;
            case 3:
                itemTypeCountViewList.get(2).setVisibility(View.VISIBLE);
                itemTypeCountViewList.get(3).setVisibility(View.INVISIBLE);

                selectedTypeCountView.setBackgroundResource(R.drawable.training_item_type_count_background);

                itemTypeCount = 1;
                selectedTypeCountView = itemTypeCountViewList.get(0);
                selectedTypeCountView.setBackgroundResource(R.drawable.training_item_type_count_background_selected);
                break;
            default:
                itemTypeCountViewList.get(2).setVisibility(View.VISIBLE);
                itemTypeCountViewList.get(3).setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private ItemsFragmentListener fragmentListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ItemsFragmentListener) {
            fragmentListener = (ItemsFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ItemsFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentListener = null;
    }
}
