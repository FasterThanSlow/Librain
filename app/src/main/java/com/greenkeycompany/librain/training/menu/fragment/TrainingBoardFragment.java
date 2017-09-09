package com.greenkeycompany.librain.training.menu.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.greenkeycompany.librain.R;
import com.greenkeycompany.librain.app.view.boardview.BoardView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class TrainingBoardFragment extends Fragment {

    public interface BoardFragmentListener {
        void onBoardFragmentNextButtonPressed(int rowCount, int columnCount);
        void onBoardFragmentPreviousButtonPressed();
    }

    public TrainingBoardFragment() {
    }

    private static final int MINIMUM_ROW_COUNT = 2;
    private static final int MINIMUM_COLUMN_COUNT = 2;

    private static final String ENABLED_ROW_COUNT_KEY = "enabled_row_count";
    private static final String ENABLED_COLUMN_COUNT_KEY = "enabled_column_count";

    private int enabledRowCount;
    private int enabledColumnCount;

    public static TrainingBoardFragment newInstance(int enabledColumnCount, int enabledRowCount) {
        TrainingBoardFragment fragment = new TrainingBoardFragment();
        Bundle args = new Bundle();
        args.putInt(ENABLED_COLUMN_COUNT_KEY, enabledColumnCount);
        args.putInt(ENABLED_ROW_COUNT_KEY, enabledRowCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            enabledColumnCount = getArguments().getInt(ENABLED_COLUMN_COUNT_KEY);
            enabledRowCount = getArguments().getInt(ENABLED_ROW_COUNT_KEY);
        }
    }

    @BindView(R.id.board_view) BoardView boardView;

    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.training_board_fragment, container, false);

        unbinder = ButterKnife.bind(this, view);

        boardView.setItemsOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    final BoardView.BoardItemView[] items = boardView.getItems();

                    int selectedItemIndex = 0;
                    int boardItemCount = items.length;

                    for (int i = 0; i < boardItemCount; i++) {
                        if (items[i] == view) {
                            selectedItemIndex = i;
                            break;
                        }
                    }

                    enabledRowCount = selectedItemIndex / boardView.getRowCount() + 1;
                    enabledColumnCount = selectedItemIndex % boardView.getRowCount() + 1;

                    if (enabledColumnCount < MINIMUM_COLUMN_COUNT) {
                        enabledColumnCount = MINIMUM_COLUMN_COUNT;
                    }
                    if (enabledRowCount < MINIMUM_ROW_COUNT) {
                        enabledRowCount = MINIMUM_ROW_COUNT;
                    }

                    setEnabledBoardItems(enabledColumnCount, enabledRowCount);
                }

                return false;
            }
        });

        setEnabledBoardItems(enabledColumnCount, enabledRowCount);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setEnabledBoardItems(int enabledColumnCount, int enabledRowCount) {
        int selectedItemIndex = (enabledRowCount - 1) * boardView.getColumnCount() + enabledColumnCount - 1;
        setEnabledBoardItems(selectedItemIndex);
    }

    private void setEnabledBoardItems(int selectedItemIndex) {
        BoardView.BoardItemView[] items = boardView.getItems();
        int boardItemCount = items.length;

        int columnCount = boardView.getColumnCount();
        int superCoefficient = selectedItemIndex % columnCount;

        for (int i = 0; i < boardItemCount; i++) {
            if (i <= selectedItemIndex) {
                if (i % columnCount <= superCoefficient) {
                    items[i].setBackgroundResource(R.drawable.training_board_item_selected_background);
                } else {
                    items[i].setBackgroundResource(R.drawable.training_board_item_background);
                }
            } else {
                items[i].setBackgroundResource(R.drawable.training_board_item_background);
            }
        }
    }

    @OnClick(R.id.next_button)
    public void onNextButtonClick() {
        fragmentListener.onBoardFragmentNextButtonPressed(enabledRowCount, enabledColumnCount);
    }

    @OnClick(R.id.previous_button)
    public void onPreviousButtonClick() {
        fragmentListener.onBoardFragmentPreviousButtonPressed();
    }

    private BoardFragmentListener fragmentListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BoardFragmentListener) {
            fragmentListener = (BoardFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement BoardFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentListener = null;
    }
}
