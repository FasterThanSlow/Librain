package com.greenkeycompany.librain.advice.view.fragment.favorite;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.greenkeycompany.librain.R;
import com.greenkeycompany.librain.advice.model.Advice;
import com.greenkeycompany.librain.advice.view.fragment.AdviceAbstractFragment;
import com.greenkeycompany.librain.advice.view.fragment.AdviceAdapter;

import java.util.ArrayList;

import butterknife.BindColor;
import butterknife.BindDimen;

/**
 * Created by tert0 on 05.09.2017.
 */

public class FavoriteAdviceFragment extends AdviceAbstractFragment implements IFavoriteAdviceFragment {

    public static FavoriteAdviceFragment newInstance(@NonNull ArrayList<Advice> adviceArrayList) {
        FavoriteAdviceFragment fragment = new FavoriteAdviceFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ADVISE_ARRAY_LIST_PARAM, adviceArrayList);
        fragment.setArguments(args);
        return fragment;
    }

    private TextView emptyTextView;
    private AdviceAdapter adviceAdapter;

    private static final int EMPTY_VIEW_TEXT_SIZE_SP = 14;

    @BindColor(android.R.color.primary_text_light) int emptyViewTextColor;
    @BindDimen(R.dimen.advice_favorite_empty_view_padding) int emptyViewPadding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        emptyTextView = new TextView(getContext());
        emptyTextView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        emptyTextView.setPadding(emptyViewPadding, emptyViewPadding, emptyViewPadding, emptyViewPadding);
        emptyTextView.setGravity(Gravity.CENTER);
        emptyTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, EMPTY_VIEW_TEXT_SIZE_SP);
        emptyTextView.setText(R.string.advice_favorite_empty_view_message);

        ((ViewGroup) view).addView(emptyTextView);

        adviceAdapter = new AdviceAdapter(new AdviceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Advice advice = adviceList.get(position);
                advice.setFavorite(false);

                adviceList.remove(position);
                adviceAdapter.removeItem(position);
                fragmentListener.onFavoriteAdviceFragmentRemoveFavoriteItem(advice.getId());

                emptyViewVisibilityChecker();
            }
        });
        recyclerView.setAdapter(adviceAdapter);
        adviceAdapter.setItems(adviceList);

        emptyViewVisibilityChecker();

        return view;
    }

    private void emptyViewVisibilityChecker() {
        if (adviceList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void addItem(@NonNull Advice advice) {
        adviceList.add(advice);
        adviceAdapter.addItem(advice);

        emptyViewVisibilityChecker();
    }

    @Override
    public void removeItem(int id) {
        for (int i = 0; i < adviceList.size(); i++) {
            Advice advice = adviceList.get(i);
            if (advice.getId() == id) {
                adviceList.remove(i);
                adviceAdapter.removeItem(i);
                break;
            }
        }

        emptyViewVisibilityChecker();
    }


    private FavoriteAdviceFragmentListener fragmentListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FavoriteAdviceFragmentListener) {
            fragmentListener = (FavoriteAdviceFragmentListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement FavoriteAdviceFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentListener = null;
    }

}
