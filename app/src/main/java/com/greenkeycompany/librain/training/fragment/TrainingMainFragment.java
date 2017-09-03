package com.greenkeycompany.librain.training.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.greenkeycompany.librain.R;
import com.greenkeycompany.librain.entity.ItemType;
import com.greenkeycompany.librain.entity.Rule;
import com.greenkeycompany.librain.level.Generator;
import com.greenkeycompany.librain.level.Level;
import com.greenkeycompany.librain.view.boardview.BoardView;

import java.util.Random;

public class TrainingMainFragment extends Fragment {

    private static final String COLUMN_COUNT_PARAM = "column_count";
    private static final String ROW_COUNT_PARAM = "row_count";

    private static final String TYPE_COUNT_PARAM = "type_count";
    private static final String ITEM_COUNT_PARAM = "item_count";

    private static final String FIRST_ROUND_PARAM = "first_round_selected";
    private static final String SECOND_ROUND_PARAM = "second_round_selected";
    private static final String THIRD_ROUND_PARAM = "third_round_selected";

    private boolean isFirstRoundSelected;
    private boolean isSecondRoundSelected;
    private boolean isThirdRoundSelected;

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

    public static TrainingMainFragment newInstance(int columnCount, int rowCount,
                                                   int typeCount, int itemCount,
                                                   boolean isFirstRoundSelected, boolean isSecondRoundSelected,
                                                   boolean isThirdRoundSelected) {

        TrainingMainFragment fragment = new TrainingMainFragment();
        Bundle args = new Bundle();
        args.putInt(COLUMN_COUNT_PARAM, columnCount);
        args.putInt(ROW_COUNT_PARAM, rowCount);
        args.putInt(TYPE_COUNT_PARAM, typeCount);
        args.putInt(ITEM_COUNT_PARAM, itemCount);
        args.putBoolean(FIRST_ROUND_PARAM, isFirstRoundSelected);
        args.putBoolean(SECOND_ROUND_PARAM, isSecondRoundSelected);
        args.putBoolean(THIRD_ROUND_PARAM, isThirdRoundSelected);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            columnCount = getArguments().getInt(COLUMN_COUNT_PARAM);
            rowCount = getArguments().getInt(ROW_COUNT_PARAM);
            typeCount = getArguments().getInt(TYPE_COUNT_PARAM);
            itemCount = getArguments().getInt(ITEM_COUNT_PARAM);
            isFirstRoundSelected = getArguments().getBoolean(FIRST_ROUND_PARAM);
            isSecondRoundSelected = getArguments().getBoolean(SECOND_ROUND_PARAM);
            isThirdRoundSelected = getArguments().getBoolean(THIRD_ROUND_PARAM);
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

        final View firstRoundIndicator = parentView.findViewById(R.id.training_main_first_round_indicator_view);
        firstRoundIndicator.setBackgroundResource(isFirstRoundSelected ?
                R.drawable.training_main_round_selected_background :
                R.drawable.training_main_round_background);

        final View secondRoundIndicator = parentView.findViewById(R.id.training_main_second_round_indicator_view);
        secondRoundIndicator.setBackgroundResource(isSecondRoundSelected ?
                R.drawable.training_main_round_selected_background :
                R.drawable.training_main_round_background);

        final View thirdRoundIndicator = parentView.findViewById(R.id.training_main_third_round_indicator_view);
        thirdRoundIndicator.setBackgroundResource(isThirdRoundSelected ?
                R.drawable.training_main_round_selected_background :
                R.drawable.training_main_round_background);

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
