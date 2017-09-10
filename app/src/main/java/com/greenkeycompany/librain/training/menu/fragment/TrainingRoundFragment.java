package com.greenkeycompany.librain.training.menu.fragment;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class TrainingRoundFragment extends Fragment {

    public interface RoundFragmentListener {
        void onRoundFragmentNextClick(boolean firstRoundSelected, boolean secondRoundSelected, boolean thirdRoundSelected);
        void onRoundFragmentPreviousClick();
    }
    
    private static final String FIRST_ROUND_SELECTED_PARAM = "first_round_selected";
    private static final String SECOND_ROUND_SELECTED_PARAM = "second_round_selected";
    private static final String THIRD_ROUND_SELECTED_PARAM = "third_round_selected";

    private boolean firstRoundSelected;
    private boolean secondRoundSelected;
    private boolean thirdRoundSelected;

    public TrainingRoundFragment() {
    }

    public static TrainingRoundFragment newInstance(boolean firstRoundSelected, boolean secondRoundSelected, boolean thirdRoundSelected) {
        TrainingRoundFragment fragment = new TrainingRoundFragment();
        Bundle args = new Bundle();
        args.putBoolean(FIRST_ROUND_SELECTED_PARAM, firstRoundSelected);
        args.putBoolean(SECOND_ROUND_SELECTED_PARAM, secondRoundSelected);
        args.putBoolean(THIRD_ROUND_SELECTED_PARAM, thirdRoundSelected);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            firstRoundSelected = getArguments().getBoolean(FIRST_ROUND_SELECTED_PARAM);
            secondRoundSelected = getArguments().getBoolean(SECOND_ROUND_SELECTED_PARAM);
            thirdRoundSelected = getArguments().getBoolean(THIRD_ROUND_SELECTED_PARAM);
        }
    }

    @BindView(R.id.next_button) Button nextButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.training_round_fragment, container, false);
        ButterKnife.bind(this, view);

        final CardView firstRoundCardView = (CardView) view.findViewById(R.id.first_round_card_view);
        final CheckBox firstRoundCheckBox = (CheckBox) view.findViewById(R.id.first_round_check_box);
        firstRoundCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstRoundCheckBox.performClick();
            }
        });

        final CardView secondRoundCardView = (CardView) view.findViewById(R.id.second_round_card_view);
        final CheckBox secondRoundCheckBox = (CheckBox) view.findViewById(R.id.second_round_check_box);
        secondRoundCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secondRoundCheckBox.performClick();
            }
        });

        final CardView thirdRoundCardView = (CardView) view.findViewById(R.id.third_round_card_view);
        final CheckBox thirdRoundCheckBox = (CheckBox) view.findViewById(R.id.third_round_check_box);
        thirdRoundCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thirdRoundCheckBox.performClick();
            }
        });

        firstRoundCheckBox.setChecked(firstRoundSelected);
        secondRoundCheckBox.setChecked(secondRoundSelected);
        thirdRoundCheckBox.setChecked(thirdRoundSelected);

        firstRoundCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                firstRoundSelected = isChecked;
                nextButton.setVisibility(isValid() ? View.VISIBLE : View.INVISIBLE);
            }
        });
        secondRoundCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                secondRoundSelected = isChecked;
                nextButton.setVisibility(isValid() ? View.VISIBLE : View.INVISIBLE);
            }
        });
        thirdRoundCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                thirdRoundSelected = isChecked;
                nextButton.setVisibility(isValid() ? View.VISIBLE : View.INVISIBLE);
            }
        });

        return view;
    }

    @OnClick(R.id.next_button)
    public void onNextButtonClick() {
        fragmentListener.onRoundFragmentNextClick(firstRoundSelected, secondRoundSelected, thirdRoundSelected);
    }

    @OnClick(R.id.previous_button)
    public void onPreviousButtonClick() {
        fragmentListener.onRoundFragmentPreviousClick();
    }

    private boolean isValid() {
        return firstRoundSelected || secondRoundSelected || thirdRoundSelected;
    }

    private RoundFragmentListener fragmentListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RoundFragmentListener) {
            fragmentListener = (RoundFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement RoundFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentListener = null;
    }
}
