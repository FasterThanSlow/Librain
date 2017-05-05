package com.greenkey.librain.training;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.greenkey.librain.R;
import com.greenkey.librain.entity.ItemType;
import com.greenkey.librain.entity.Rule;
import com.greenkey.librain.level.Generator;
import com.greenkey.librain.level.Level;
import com.greenkey.librain.view.boardview.BoardView;

import java.util.ArrayList;
import java.util.List;

public class TrainingItemsFragment extends Fragment {

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
    private ItemsFragmentListener listener;

    public interface ItemsFragmentListener {
        void onItemsFragmentNext(int itemTypeCount, int itemCount, int[] items);
        void onItemsFragmentPrevious();
    }

    public TrainingItemsFragment() {
    }

    public static TrainingItemsFragment newInstance(int columnCount, int rowCount, int typeCount, int itemCount) {
        TrainingItemsFragment fragment = new TrainingItemsFragment();
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

        /*
        namedLevelTypes = new ArrayList<>();

        levelTypes = Level.LevelType.values();
        String[] names = getResources().getStringArray(R.array.level_types);

        for (int i = 0; i < levelTypes.length; i++) {
            namedLevelTypes.add(new NamedLevelType(levelTypes[i], names[i]));
        }

        selectedLevelType = levelTypes[levelTypeIndex];
        */
    }

    //private Spinner levelTypeCountSpinner;

    private SeekBar itemCountSeekBar;
    private TextView itemCountTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View parentView = inflater.inflate(R.layout.training_items_fragment, container, false);

        final Button nextButton = (Button) parentView.findViewById(R.id.training_next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onItemsFragmentNext(typeCount, itemCount, items);
            }
        });

        final Button previousButton = (Button) parentView.findViewById(R.id.training_previous_button);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onItemsFragmentPrevious();
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

        /*
        List<String> levelTypeCountValues = getLevelTypeCountValues(selectedLevelType);

        levelTypeCountSpinner = (Spinner) parentView.findViewById(R.id.training_level_type_count_spinner);
        levelTypeCountSpinner.setAdapter(new LevelTypeCountAdapter(getActivity(), levelTypeCountValues));
        */

        itemCountSeekBar = (SeekBar) parentView.findViewById(R.id.training_level_item_count_seek_bar);
        itemCountSeekBar.setMax(rowCount * columnCount - 1);
        itemCountSeekBar.setProgress(itemCount - 1);
        itemCountSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setItemCount(progress + 1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                resetBoardItems();
            }
        });

        itemCountTextView = (TextView) parentView.findViewById(R.id.training_level_item_count_text_view);
        itemCountTextView.setText(String.valueOf(itemCount));

        resetBoardItems();

        return parentView;
    }

    private View.OnClickListener itemTypesCountOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View item) {
            if (itemCount >= Integer.valueOf(((TextView) item).getText().toString())) {
                selectedTypeCountTextView.setBackgroundResource(R.drawable.training_item_type_count_background);
                item.setBackgroundResource(R.drawable.training_item_type_count_background_selected);

                selectedTypeCountTextView = (TextView) item;
                typeCount = Integer.valueOf(selectedTypeCountTextView.getText().toString());

                resetBoardItems();
            }
        }
    };

    private void setItemCount(int itemCount) {
        this.itemCount = itemCount;
        itemCountTextView.setText(String.valueOf(itemCount));
    }

    private void resetBoardItems() {
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

        Rule[] rules = Generator.createRules(Level.LevelType.FRUIT, items);
        ItemType[] boardResources = Generator.createFullBoardItems(rules, columnCount * rowCount);

        boardView.setItemsResources(boardResources);
    }



/*
    private List<String> getLevelTypeCountValues(Level.LevelType selectedLevelType) {
        int levelTypeCount = selectedLevelType.getResources().length;
        List<String> levelTypeCountValues= new ArrayList<>();
        for (int i = 0; i < levelTypeCount; i++) {
            levelTypeCountValues.add(String.valueOf(i + 1));
        }

        return levelTypeCountValues;
    }

    private class NamedLevelType {

        private final Level.LevelType levelType;
        private final String name;

        public NamedLevelType(Level.LevelType levelType, String name) {
            this.levelType = levelType;
            this.name = name;
        }

        public Level.LevelType getLevelType() {
            return levelType;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private class LevelTypeAdapter extends ArrayAdapter<NamedLevelType> {

        class ViewHolder {
            public TextView name;
        }

        public LevelTypeAdapter(Context context, List<NamedLevelType> items) {
            super(context, R.layout.training_item_type_list_item, items);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            ViewHolder viewHolder;

            if (convertView == null) {
                viewHolder = new ViewHolder();

                convertView = LayoutInflater.from(getContext()).inflate(R.layout.training_item_type_list_item, null);
                convertView.setTag(viewHolder);

                viewHolder.name = (TextView) convertView; //ВОТ ТУТ find view by id
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            NamedLevelType item = getItem(position);
            if (item != null) {
                viewHolder.name.setText(item.getName());
            }

            return convertView;
        }
    }

    private class LevelTypeCountAdapter extends ArrayAdapter<String> {

        class ViewHolder {
            public TextView countTextView;
        }

        public LevelTypeCountAdapter(Context context, List<String> items) {
            super(context, R.layout.training_item_type_count_list_item, items);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            ViewHolder viewHolder;

            if (convertView == null) {
                viewHolder = new ViewHolder();

                convertView = LayoutInflater.from(getContext()).inflate(R.layout.training_item_type_count_list_item, null);
                convertView.setTag(viewHolder);

                viewHolder.countTextView = (TextView) convertView; //ВОТ ТУТ find view by id
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            String item = getItem(position);
            if (item != null) {
                viewHolder.countTextView.setText(item);
            }

            return convertView;
        }
    }
*/


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ItemsFragmentListener) {
            listener = (ItemsFragmentListener) context;
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
