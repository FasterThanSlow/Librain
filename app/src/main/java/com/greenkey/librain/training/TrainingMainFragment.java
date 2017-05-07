package com.greenkey.librain.training;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.greenkey.librain.R;
import com.greenkey.librain.entity.ItemType;
import com.greenkey.librain.entity.Rule;
import com.greenkey.librain.level.Generator;
import com.greenkey.librain.level.Level;
import com.greenkey.librain.view.boardview.BoardView;

import java.util.Random;

public class TrainingMainFragment extends Fragment {

    private static final String COLUMN_COUNT_KEY = "column_count";
    private static final String ROW_COUNT_KEY = "row_count";

    private static final String TYPE_COUNT = "type_count";
    private static final String ITEM_COUNT = "item_count";

    private int columnCount;
    private int rowCount;

    private int typeCount;
    private int itemCount;

    private int[] items;

    private BoardView boardView;
    private MainFragmentListener listener;

    public interface MainFragmentListener {
        void onMainFragmentStartPressed(int[] items);
        void onMainFragmentSettingsPressed();
    }

    public TrainingMainFragment() {
    }

    public static TrainingMainFragment newInstance(int columnCount, int rowCount, int typeCount, int itemCount) {
        TrainingMainFragment fragment = new TrainingMainFragment();
        Bundle args = new Bundle();
        args.putInt(COLUMN_COUNT_KEY, columnCount);
        args.putInt(ROW_COUNT_KEY, rowCount);
        args.putInt(TYPE_COUNT, typeCount);
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View parentView = inflater.inflate(R.layout.training_main_fragment, container, false);

        final Button startButton = (Button) parentView.findViewById(R.id.training_start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onMainFragmentStartPressed(items);
            }
        });

        final ImageView settingsImageView = (ImageView) parentView.findViewById(R.id.settings_icon_image_view);
        settingsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onMainFragmentSettingsPressed();
            }
        });

        boardView = (BoardView) parentView.findViewById(R.id.board_view);
        boardView.createItems(rowCount, columnCount);

        boardView.post(new Runnable() {
            @Override
            public void run() {
                final Level.LevelType[] levelTypes = Level.LevelType.values();
                final Level.LevelType levelType = levelTypes[new Random().nextInt(levelTypes.length)];

                resetBoardItems(itemCount, typeCount, levelType);
            }
        });

        return parentView;
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainFragmentListener) {
            listener = (MainFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement BoardFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
