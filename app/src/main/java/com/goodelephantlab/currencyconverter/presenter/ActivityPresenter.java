package com.goodelephantlab.currencyconverter.presenter;

import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import com.goodelephantlab.currencyconverter.loaders.CurrencyLoader;
import com.goodelephantlab.currencyconverter.view.CurrencySelectionDialog;
import com.goodelephantlab.currencyconverter.R;
import com.goodelephantlab.currencyconverter.model.db.DataBase;
import com.goodelephantlab.currencyconverter.view.MainActivity;
import java.util.ArrayList;

/**
 * Created by Edridtat on 06.03.2017.
 */
public class ActivityPresenter implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String TAG = "ActivityPresenter";
    private final MainActivity activity;
    private ArrayList<String> currencyNames;
    private ArrayList<String> currencyIdList;
    private ArrayList<Integer> currencyFlags;
    private Float startValue;
    private Float finishValue;
    private int startFlagImageId;
    private String startName;
    private int finishFlagImageId;
    private String finishName;

    public ActivityPresenter(MainActivity activity, Intent intent){
        this.activity = activity;
        int result = intent.getIntExtra(activity
                .getResources().getString(R.string.loading_result),
                activity.getResources().getInteger(R.integer.loading_result_unknown));
        if(result == activity.getResources().getInteger(R.integer.loading_result_error)){
            String error = intent.getStringExtra(activity.getString(R.string.loading_error));
            activity.showError(error);
        }
    }

    public void updateData(){
        activity.showProgressBar();
        activity.getLoaderManager().initLoader(R.id.currency_loader, Bundle.EMPTY, this);
        activity.getLoaderManager().initLoader(R.id.all_currency_loader, Bundle.EMPTY, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        Log.d(TAG, "onCreateLoader");
        if(id==R.id.currency_loader || id==R.id.all_currency_loader){
            return new CurrencyLoader(activity);
        }else{
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        int id = loader.getId();
        if(data == null||!data.moveToFirst()){
            Log.d(TAG, "onLoadFinished data == null");
            activity.showContent(false);
            return;
        }
        int flagNameColumnIndex = data.getColumnIndex(DataBase.CURRENCY_COLUMN_FLAG_IMAGE_NAME);
        int nameIndex = data.getColumnIndex(DataBase.CURRENCY_COLUMN_NAME);
        int idIndex = data.getColumnIndex(DataBase.CURRENCY_COLUMN_ID);
        if (id == R.id.currency_loader ) {
            Log.d(TAG, "currency_loader onLoadFinished dataCount = "+data.getCount());
            initCurrencyParams(data, flagNameColumnIndex, nameIndex, idIndex);
            activity.updateUI(startName, startFlagImageId, finishName, finishFlagImageId);
            activity.showContent(true);
        }else if(id == R.id.all_currency_loader){
            Log.d(TAG, "all_currency_loader onLoadFinished dataCount = "+data.getCount());
            initAllCurrency(data, flagNameColumnIndex, nameIndex, idIndex);
        }
    }

    private void initCurrencyParams(Cursor data, int flagNameColumnIndex, int nameIndex, int idIndex){
        Resources resources = activity.getResources();
        int valueIndex = data.getColumnIndex(DataBase.CURRENCY_COLUMN_VALUE);
        String startCurrencyPreference = PreferenceManager
                .getDefaultSharedPreferences(activity).getString(
                        resources.getString(R.string.start_currency_id),
                        resources.getString(R.string.rub_id));
        String finishCurrencyPreference = PreferenceManager
                .getDefaultSharedPreferences(activity).getString(
                        resources.getString(R.string.finish_currency_id),
                        resources.getString(R.string.usd_id));
        if(!data.moveToFirst()){
            activity.showContent(false);
            return;
        }
        do{
            String id = data.getString(idIndex);
            if(id.equals(startCurrencyPreference)){
                String startFlagName = data.getString(flagNameColumnIndex);
                startFlagImageId = resources.getIdentifier(startFlagName, "drawable", activity.getPackageName());
                startName = data.getString(nameIndex);
                startValue = Float.valueOf(data.getString(valueIndex));
            }
            if(id.equals(finishCurrencyPreference)){
                String finishFlagName = data.getString(flagNameColumnIndex);
                finishFlagImageId = resources.getIdentifier(finishFlagName, "drawable", activity.getPackageName());
                finishName = data.getString(nameIndex);
                finishValue = Float.valueOf(data.getString(valueIndex));
            }
        }while (data.moveToNext());
    }

    private void initAllCurrency(Cursor data, int flagNameColumnIndex, int nameIndex, int idIndex){
        Resources resources = activity.getResources();
        currencyNames = new ArrayList<>();
        currencyIdList = new ArrayList<>();
        currencyFlags = new ArrayList<>();
        if(!data.moveToFirst()){
            activity.showContent(false);
            return;
        }
        do{
            currencyNames.add(data.getString(nameIndex));
            currencyIdList.add(data.getString(idIndex));
            String flagName = data.getString(flagNameColumnIndex);
            int flagImageId = resources.getIdentifier(
                    flagName, "drawable", activity.getPackageName());
            currencyFlags.add(flagImageId);
        }
        while (data.moveToNext());
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset");
    }

    public void convertButtonClicked(double count, int editTextId) {
        double value;
        if(editTextId == R.id.finish_currency_edit_text){
            value = startValue/finishValue * count;
        }else{
            value = finishValue/startValue * count;
        }
        activity.updateCountValue(value, editTextId);
    }

    public void currencyButtonClicked(View view) {
        Resources resources = activity.getResources();
        String currencyPreferenceId = "";
        int clickedViewId = view.getId();
        switch (clickedViewId){
            case R.id.start_currency_flag_image_view:
                currencyPreferenceId = resources.getString(R.string.start_currency_id);
                break;
            case R.id.finish_currency_flag_image_view:
                currencyPreferenceId = resources.getString(R.string.finish_currency_id);
                break;
        }
        FragmentManager fm = activity.getFragmentManager();
        CurrencySelectionDialog selectionDialog = new CurrencySelectionDialog();
        Bundle args = new Bundle();
        args.putStringArrayList(
                resources.getString(R.string.selection_dialog_names_arg_id), currencyNames);
        args.putIntegerArrayList(
                resources.getString(R.string.selection_dialog_flags_arg_id), currencyFlags);
        args.putString(
                resources.getString(
                        R.string.selection_dialog_clicked_preference_id), currencyPreferenceId);
        selectionDialog.setArguments(args);
        selectionDialog.setPresenter(this);
        selectionDialog.show(fm, "");
    }

    public void currencySelected(int clickedPosition, String clickedPreferenceId) {
        String selectedCurrencyId = currencyIdList.get(clickedPosition);
        PreferenceManager.getDefaultSharedPreferences(activity)
                .edit().putString(clickedPreferenceId, selectedCurrencyId).apply();
        activity.getLoaderManager().destroyLoader(R.id.currency_loader);
        updateData();
    }
}