package com.goodelephantlab.currencyconverter.loaders;

import android.content.Context;
import android.content.CursorLoader;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.util.Log;
import com.goodelephantlab.currencyconverter.R;
import com.goodelephantlab.currencyconverter.model.db.DataBase;

/**
 * Created by Edridtat on 06.03.2017.
 */
public class CurrencyLoader extends CursorLoader {

    private static final String TAG = "CurrencyLoader";

    public CurrencyLoader(Context context) {
        super(context);
        forceLoad();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        Log.d(TAG, "onStartLoading");
    }

    @Override
    public Cursor loadInBackground() {
        DataBase dataBase = new DataBase(getContext());
        switch (getId()){
            case R.id.currency_loader:
                Log.d(TAG, "currency_loader loadInBackground");
                SharedPreferences sp = PreferenceManager
                        .getDefaultSharedPreferences(getContext());
                Resources resources = getContext().getResources();
                String startCurrencyId = sp.getString(
                        resources.getString(R.string.start_currency_id),
                        resources.getString(R.string.rub_id));
                String finishCurrencyId = sp.getString(
                        resources.getString(R.string.finish_currency_id),
                        resources.getString(R.string.usd_id));
                return dataBase.getCurrent(startCurrencyId, finishCurrencyId);
            case R.id.all_currency_loader:
                Log.d(TAG, "all_currency_loader loadInBackground");
                String[] projection = new String[]{
                        DataBase.CURRENCY_COLUMN_ID,
                        DataBase.CURRENCY_COLUMN_NAME,
                        DataBase.CURRENCY_COLUMN_FLAG_IMAGE_NAME};
                return dataBase.getAll(projection);
            default:
                return null;
        }
    }

    @Override
    protected boolean onCancelLoad() {
        Log.d(TAG, "onCancelLoad");
        return super.onCancelLoad();
    }
}