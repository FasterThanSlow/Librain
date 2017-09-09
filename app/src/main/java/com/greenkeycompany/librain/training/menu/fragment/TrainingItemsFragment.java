package com.greenkeycompany.librain.training.menu.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.greenkeycompany.librain.R;
import com.greenkeycompany.librain.entity.ItemType;
import com.greenkeycompany.librain.entity.Rule;
import com.greenkeycompany.librain.level.Generator;
import com.greenkeycompany.librain.level.Level;
import com.greenkeycompany.librain.app.view.boardview.BoardView;

import java.util.ArrayList;
import java.util.List;

public class TrainingItemsFragment extends Fragment {

    private static final int MINIMUM_ITEM_COUNt = 1;

    private static final String COLUMN_COUNT_KEY = "column_count";
    private static final String ROW_COUNT_KEY = "row_count";

    private static final String TYPE_COUNT = "type_count";
    private static final String ITEM_COUNT = "item_count";

    private int columnCount;
    private int rowCount;

    private int typeCount;
    private int itemCount;

    private int[] items;

    private TextView selectedTypeCountTextView;
    private List<TextView> typeCountTextViewList;

    private BoardView boardView;

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

            typeCount = getArguments().getInt(TYPE_COUNT);
            itemCount = getArguments().getInt(ITEM_COUNT);

            if (columnCount * rowCount < itemCount) {
                itemCount = MINIMUM_ITEM_COUNt;
                typeCount = 1;
            }
        }
    }

    private SeekBar itemCountSeekBar;
    private TextView itemCountTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View parentView = inflater.inflate(R.layout.training_items_fragment, container, false);

        final Button nextButton = (Button) parentView.findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentListener != null)
                    fragmentListener.onItemsFragmentNext(typeCount, itemCount);
            }
        });

        final Button previousButton = (Button) parentView.findViewById(R.id.previous_button);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentListener != null)
                    fragmentListener.onItemsFragmentPrevious();
            }
        });

        boardView = (BoardView) parentView.findViewById(R.id.board_view);
        boardView.createItems(rowCount, columnCount);

        typeCountTextViewList = new ArrayList<>();
        typeCountTextViewList.add((TextView) parentView.findViewById(R.id.training_item_types_count_1));
        typeCountTextViewList.add((TextView) parentView.findViewById(R.id.training_item_types_count_2));
        typeCountTextViewList.add((TextView) parentView.findViewById(R.id.training_item_types_count_3));
        typeCountTextViewList.add((TextView) parentView.findViewById(R.id.training_item_types_count_4));

        for (TextView itemTypesCount : typeCountTextViewList) {
            itemTypesCount.setOnClickListener(itemTypesCountOnClickListener);
        }

        selectedTypeCountTextView = typeCountTextViewList.get(typeCount - 1);
        selectedTypeCountTextView.setBackgroundResource(R.drawable.training_item_type_count_background_selected);

        itemCountTextView = (TextView) parentView.findViewById(R.id.training_level_item_count_text_view);
        itemCountTextView.setText(String.valueOf(itemCount));

        itemCountSeekBar = (SeekBar) parentView.findViewById(R.id.training_level_item_count_seek_bar);
        itemCountSeekBar.setMax(rowCount * columnCount - MINIMUM_ITEM_COUNt);
        itemCountSeekBar.setProgress(itemCount - MINIMUM_ITEM_COUNt);
        itemCountSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setItemCount(progress + MINIMUM_ITEM_COUNt);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                resetBoardItems(itemCount, typeCount, Level.LevelType.FRUIT);
            }
        });

        resetBoardItems(itemCount, typeCount, Level.LevelType.FRUIT);

        return parentView;
    }

    private View.OnClickListener itemTypesCountOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View item) {
            selectedTypeCountTextView.setBackgroundResource(R.drawable.training_item_type_count_background);

            selectedTypeCountTextView = (TextView) item;
            selectedTypeCountTextView.setBackgroundResource(R.drawable.training_item_type_count_background_selected);

            typeCount = Integer.valueOf(selectedTypeCountTextView.getText().toString());

            resetBoardItems(itemCount, typeCount, Level.LevelType.FRUIT);
        }
    };

    private void setItemCount(int itemCount) {
        Log.d("TrainingTest", "itemCount " + itemCount);

        this.itemCount = itemCount;
        itemCountTextView.setText(String.valueOf(itemCount));

        switch (itemCount) {
            case 2:
                typeCountTextViewList.get(2).setVisibility(View.INVISIBLE);
                typeCountTextViewList.get(3).setVisibility(View.INVISIBLE);

                selectedTypeCountTextView.setBackgroundResource(R.drawable.training_item_type_count_background);

                typeCount = 1;
                selectedTypeCountTextView = typeCountTextViewList.get(0);
                selectedTypeCountTextView.setBackgroundResource(R.drawable.training_item_type_count_background_selected);
                break;
            case 3:
                typeCountTextViewList.get(2).setVisibility(View.VISIBLE);
                typeCountTextViewList.get(3).setVisibility(View.INVISIBLE);

                selectedTypeCountTextView.setBackgroundResource(R.drawable.training_item_type_count_background);

                typeCount = 1;
                selectedTypeCountTextView = typeCountTextViewList.get(0);
                selectedTypeCountTextView.setBackgroundResource(R.drawable.training_item_type_count_background_selected);
                break;
            default:
                typeCountTextViewList.get(2).setVisibility(View.VISIBLE);
                typeCountTextViewList.get(3).setVisibility(View.VISIBLE);
                break;
        }
    }

    private void resetBoardItems(int itemCount, int typeCount, Level.LevelType levelType) {
        items = new int[typeCount];

        if (typeCount == 1) {
            items[0] = itemCount;
        } else {
            int lastValueBuf = itemCount % typeCount;
            int value = (itemCount - lastValueBuf) / typeCount;

            for (int i = 0; i < typeCount; i++) {
                items[i] = value;
            }
            items[typeCount - 1] += lastValueBuf;
        }

        Rule[] rules = Generator.createRules(levelType, items);
        ItemType[] boardResources = Generator.createFullBoardItems(rules, columnCount * rowCount);

        boardView.setItemsResources(boardResources);
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
