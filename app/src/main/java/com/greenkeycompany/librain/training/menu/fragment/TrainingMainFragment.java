package com.greenkeycompany.librain.training.menu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.greenkeycompany.librain.R;
import com.greenkeycompany.librain.entity.ItemType;
import com.greenkeycompany.librain.entity.Rule;
import com.greenkeycompany.librain.level.Generator;
import com.greenkeycompany.librain.level.Level;
import com.greenkeycompany.librain.app.view.boardview.BoardView;
import com.greenkeycompany.librain.training.entity.TrainingConfig;
import com.greenkeycompany.librain.training.game.view.TrainingGameActivity;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class TrainingMainFragment extends Fragment {

    private static final String TRAINING_CONFIG_PARAM = "training_config";
    private TrainingConfig config;

    public TrainingMainFragment() {
    }

    public static TrainingMainFragment newInstance(@NonNull TrainingConfig trainingConfig) {
        TrainingMainFragment fragment = new TrainingMainFragment();
        Bundle args = new Bundle();
        args.putParcelable(TRAINING_CONFIG_PARAM, trainingConfig);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            config = getArguments().getParcelable(TRAINING_CONFIG_PARAM);
        }
    }

    @BindView(R.id.first_round_indicator_view) View firstRoundIndicatorView;
    @BindView(R.id.second_round_indicator_view) View secondRoundIndicatorView;
    @BindView(R.id.third_round_indicator_view) View thirdRoundIndicatorView;

    @BindView(R.id.board_view) BoardView boardView;

    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.training_main_fragment, container, false);

        unbinder = ButterKnife.bind(this, view);

        firstRoundIndicatorView.setBackgroundResource(config.isFirstRoundSelected() ?
                R.drawable.training_main_round_selected_background :
                R.drawable.training_main_round_background);

        secondRoundIndicatorView.setBackgroundResource(config.isSecondRoundSelected() ?
                R.drawable.training_main_round_selected_background :
                R.drawable.training_main_round_background);

        thirdRoundIndicatorView.setBackgroundResource(config.isThirdRoundSelected() ?
                R.drawable.training_main_round_selected_background :
                R.drawable.training_main_round_background);

        boardView.createItems(config.getRowCount(), config.getRowCount());
        boardView.post(new Runnable() {
            @Override
            public void run() {
                Level.LevelType[] levelTypes = Level.LevelType.values();
                Level.LevelType levelType = levelTypes[new Random().nextInt(levelTypes.length)];

                Rule[] rules = Generator.createRulesForTraining(levelType, config.getItemTypeCount(),  config.getRowCount() * config.getColumnCount());
                ItemType[] itemTypes = Generator.createFullBoardItems(rules, config.getRowCount() * config.getColumnCount());

                boardView.setItemsResources(itemTypes);
            }
        });

        return view;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.training_start_button)
    public void onStartButtonClick() {
        Intent intent = new Intent(getActivity(), TrainingGameActivity.class).
                putExtra(TrainingGameActivity.TRAINING_CONFIG_PARAM, config);
        startActivity(intent);
    }
}
