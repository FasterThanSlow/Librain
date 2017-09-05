package com.greenkeycompany.librain.advice.view.fragment.advice;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.greenkeycompany.librain.advice.model.Advice;
import com.greenkeycompany.librain.advice.view.fragment.AdviceAdapter;
import com.greenkeycompany.librain.advice.view.fragment.AdviceAbstractFragment;

import java.util.ArrayList;

/**
 * Created by tert0 on 05.09.2017.
 */

public class AdviceFragment extends AdviceAbstractFragment implements IAdviceFragment {

    public static AdviceFragment newInstance(@NonNull ArrayList<Advice> adviceArrayList) {
        AdviceFragment fragment = new AdviceFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ADVISE_ARRAY_LIST_PARAM, adviceArrayList);
        fragment.setArguments(args);
        return fragment;
    }

    private AdviceAdapter adviceAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        adviceAdapter = new AdviceAdapter(new AdviceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Advice advice = adviceList.get(position);

                if (advice.isFavorite()) {
                    advice.setFavorite(false);
                    adviceAdapter.notifyItemChanged(position);
                    fragmentListener.onAdviceFragmentRemoveFavoriteItem(advice.getId());
                } else {
                    advice.setFavorite(true);
                    adviceAdapter.notifyItemChanged(position);
                    fragmentListener.onAdviceFragmentAddFavoriteItem(advice.getId());
                }
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adviceAdapter);
        adviceAdapter.setItems(adviceList);

        return view;
    }

    @Override
    public void notifyItemChange(int id) {
        for (int i = 0; i < adviceList.size(); i++) {
            Advice advice = adviceList.get(i);
            if (advice.getId() == id) {
                adviceAdapter.notifyItemChanged(i);
                break;
            }
        }
    }

    private AdviceFragmentListener fragmentListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AdviceFragmentListener) {
            fragmentListener = (AdviceFragmentListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement AdviceFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentListener = null;
    }
}
