package com.greenkeycompany.librain.training.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.greenkeycompany.librain.R;


public class TrainingRoundFragment extends Fragment {

    private static final String FIRST_ROUND_PARAM = "first_round_selected";
    private static final String SECOND_ROUND_PARAM = "second_round_selected";
    private static final String THIRD_ROUND_PARAM = "third_round_selected";

    private boolean isFirstRoundSelected;
    private boolean isSecondRoundSelected;
    private boolean isThirdRoundSelected;

    private RoundFragmentListener listener;

    public TrainingRoundFragment() {
    }

    public static TrainingRoundFragment newInstance(boolean isFirstRoundSelected, boolean isSecondRoundSelected, boolean isThirdRoundSelected) {
        TrainingRoundFragment fragment = new TrainingRoundFragment();
        Bundle args = new Bundle();
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
            isFirstRoundSelected = getArguments().getBoolean(FIRST_ROUND_PARAM);
            isSecondRoundSelected = getArguments().getBoolean(SECOND_ROUND_PARAM);
            isThirdRoundSelected = getArguments().getBoolean(THIRD_ROUND_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View parentView = inflater.inflate(R.layout.training_round_fragment, container, false);

        final Button nextButton = (Button) parentView.findViewById(R.id.training_next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onRoundFragmentNext(isFirstRoundSelected, isSecondRoundSelected, isThirdRoundSelected);
            }
        });

        final Button previousButton = (Button) parentView.findViewById(R.id.training_previous_button);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onRoundFragmentPrevious();
            }
        });

        final CardView firstRoundCardView = (CardView) parentView.findViewById(R.id.training_first_round_card_view);
        final CheckBox firstRoundCheckBox = (CheckBox) parentView.findViewById(R.id.training_first_round_check_box);
        firstRoundCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstRoundCheckBox.performClick();
            }
        });

        final CardView secondRoundCardView = (CardView) parentView.findViewById(R.id.training_second_round_card_view);
        final CheckBox secondRoundCheckBox = (CheckBox) parentView.findViewById(R.id.training_second_round_check_box);
        secondRoundCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secondRoundCheckBox.performClick();
            }
        });

        final CardView thirdRoundCardView = (CardView) parentView.findViewById(R.id.training_third_round_card_view);
        final CheckBox thirdRoundCheckBox = (CheckBox) parentView.findViewById(R.id.training_third_round_check_box);
        thirdRoundCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thirdRoundCheckBox.performClick();
            }
        });

        firstRoundCheckBox.setChecked(isFirstRoundSelected);
        secondRoundCheckBox.setChecked(isSecondRoundSelected);
        thirdRoundCheckBox.setChecked(isThirdRoundSelected);

        firstRoundCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isFirstRoundSelected = isChecked;

                if (isValid(isFirstRoundSelected, isSecondRoundSelected, isThirdRoundSelected)) {
                    nextButton.setEnabled(true);
                } else {
                    nextButton.setEnabled(false);
                }
            }
        });
        secondRoundCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isSecondRoundSelected = isChecked;

                if (isValid(isFirstRoundSelected, isSecondRoundSelected, isThirdRoundSelected)) {
                    nextButton.setEnabled(true);
                } else {
                    nextButton.setEnabled(false);
                }
            }
        });
        thirdRoundCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isThirdRoundSelected = isChecked;

                if (isValid(isFirstRoundSelected, isSecondRoundSelected, isThirdRoundSelected)) {
                    nextButton.setEnabled(true);
                } else {
                    nextButton.setEnabled(false);
                }
            }
        });

        return parentView;
    }

    private boolean isValid(boolean isFirstRoundSelected, boolean isSecondRoundSelected, boolean isThirdRoundSelected) {
        return isFirstRoundSelected || isSecondRoundSelected || isThirdRoundSelected;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RoundFragmentListener) {
            listener = (RoundFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement RoundFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface RoundFragmentListener {
        void onRoundFragmentNext(boolean isFirstRoundSelected, boolean isSecondRoundSelected, boolean isThirdRoundSelected);
        void onRoundFragmentPrevious();
    }
}
