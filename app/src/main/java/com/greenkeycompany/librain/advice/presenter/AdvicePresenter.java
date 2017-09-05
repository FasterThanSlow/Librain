package com.greenkeycompany.librain.advice.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.greenkeycompany.librain.R;
import com.greenkeycompany.librain.advice.model.Advice;
import com.greenkeycompany.librain.advice.view.IAdviseView;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by tert0 on 04.09.2017.
 */

public class AdvicePresenter extends MvpBasePresenter<IAdviseView>
        implements IAdvicePresenter {

    private Context context;
    private SharedPreferences sharedPreferences;
    public AdvicePresenter(@NonNull Context context) {
        this.context = context;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private String ADVICE_FAVORITE_ID_STRING_SET_KEY = "advice_favorite_id_string_set";

    @Override
    public void saveData() {
        Set<String> idSet = new HashSet<>();

        for (Advice advice : adviceList) {
            if (advice.isFavorite()) {
                idSet.add(String.valueOf(advice.getId()));
            }
        }

        sharedPreferences.edit().putStringSet(ADVICE_FAVORITE_ID_STRING_SET_KEY, idSet).apply();
    }

    @NonNull
    private int[] getFavoriteItemIdArray() {
        Set<String> idSet = sharedPreferences.getStringSet(ADVICE_FAVORITE_ID_STRING_SET_KEY, new HashSet<String>());

        int index = 0;
        int[] resultArray = new int[idSet.size()];
        for (String stingId : idSet) {
            resultArray[index] = Integer.valueOf(stingId);
            index++;
        }

        return resultArray;
    }

    private ArrayList<Advice> adviceList;
    private ArrayList<Advice> favoriteAdviceList;

    private boolean isContainInArray(int item, int[] array) {
        for (int arrayItem : array) {
            if (item == arrayItem) return true;
        }

        return false;
    }

    @Override
    public void init() {
        int[] favoriteItemIdArray = getFavoriteItemIdArray();

        String[] adviceTitleArray = context.getResources().getStringArray(R.array.advice_title_array);
        String[] adviceMessageArray = context.getResources().getStringArray(R.array.advice_message_array);

        adviceList = new ArrayList<>(adviceTitleArray.length);
        favoriteAdviceList = new ArrayList<>(favoriteItemIdArray.length);

        for (int i = 0; i < adviceTitleArray.length; i++) {
            int id = i + 1;

            if (isContainInArray(id, favoriteItemIdArray)) {
                Advice advice = new Advice(true, id, adviceTitleArray[i], adviceMessageArray[i]);

                adviceList.add(advice);
                favoriteAdviceList.add(advice);
            } else {
                adviceList.add(new Advice(id, adviceTitleArray[i], adviceMessageArray[i]));
            }
        }

        if (isViewAttached()) {
            getView().initAdviceFragments(adviceList, favoriteAdviceList);
        }
    }

    private Advice findAdviceById(int id) {
        for (Advice advice : adviceList) {
            if (advice.getId() == id) return advice;
        }

        return null;
    }

    @Override
    public void onAdviceFragmentAddFavoriteItem(int id) {
        if (isViewAttached()) {
            getView().addItemToFavoriteAdviceFragment(findAdviceById(id));
        }
    }

    @Override
    public void onAdviceFragmentRemoveFavoriteItem(int id) {
        if (isViewAttached()) {
            getView().removeItemFromFavoriteAdviceFragment(id);
        }
    }

    @Override
    public void onFavoriteAdviceFragmentRemoveFavoriteItem(int id) {
        if (isViewAttached()) {
            getView().notifyItemChangeInAdviceFragment(id);
        }
    }
}
