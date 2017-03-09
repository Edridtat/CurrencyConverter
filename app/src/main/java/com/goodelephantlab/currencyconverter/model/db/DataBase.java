package com.goodelephantlab.currencyconverter.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.goodelephantlab.currencyconverter.R;
import com.goodelephantlab.currencyconverter.model.data.Currency;
import java.util.List;

/**
 * Created by Edridtat on 06.03.2017.
 */
public class DataBase {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "currency_converter.db";
    public static final String CURRENCY_TABLE_NAME = "currency";
    public static final String CURRENCY_COLUMN_ID = "currency_id";
    public static final String CURRENCY_COLUMN_NUM_CODE = "currency_num_code";
    public static final String CURRENCY_COLUMN_FLAG_IMAGE_NAME = "currency_char_ag_image_name";
    public static final String CURRENCY_COLUMN_CHAR_CODE = "currency_fl_code";
    public static final String CURRENCY_COLUMN_NOMINAL = "currency_nominal";
    public static final String CURRENCY_COLUMN_NAME = "currency_name";
    public static final String CURRENCY_COLUMN_VALUE = "currency_value";
    private static final String DB_CREATE = "create table "
            +CURRENCY_TABLE_NAME+" ("
            +CURRENCY_COLUMN_ID+" text,"
            +CURRENCY_COLUMN_NUM_CODE+" integer,"
            +CURRENCY_COLUMN_CHAR_CODE+" text,"
            +CURRENCY_COLUMN_FLAG_IMAGE_NAME+" text,"
            +CURRENCY_COLUMN_NOMINAL+" integer,"
            +CURRENCY_COLUMN_NAME+" text,"
            +CURRENCY_COLUMN_VALUE+" text" + ");";
    private static final String DB_DROP = "DROP TABLE IF EXISTS "+CURRENCY_TABLE_NAME;
    private final Context context;
    private DBHelper dBHelper;
    private SQLiteDatabase dataBase;

    public DataBase(Context context) {
        this.context = context;
    }

    public void open() {
        dBHelper = new DBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        dataBase = dBHelper.getWritableDatabase();
    }

    public void close() {
        if (dBHelper!=null) dBHelper.close();
    }

    public Cursor getAll() {
        dBHelper = new DBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        dataBase = dBHelper.getReadableDatabase();
        return dataBase.query(CURRENCY_TABLE_NAME, null, null, null, null, null, null);
    }

    public Cursor getAll(String[] projection){
        dBHelper = new DBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        dataBase = dBHelper.getReadableDatabase();
        return dataBase.query(CURRENCY_TABLE_NAME, projection,
                null, null, null, null, CURRENCY_COLUMN_CHAR_CODE);
    }

    public Cursor getCurrent(String startId, String finishId) {
        dBHelper = new DBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        dataBase = dBHelper.getReadableDatabase();
        String[] projection = new String[]{CURRENCY_COLUMN_ID, CURRENCY_COLUMN_NAME,
                CURRENCY_COLUMN_VALUE, CURRENCY_COLUMN_FLAG_IMAGE_NAME};
        String selection = CURRENCY_COLUMN_ID+"=? OR "+CURRENCY_COLUMN_ID+"=?";
        String [] selectionValues = new String[]{startId, finishId};
        Cursor cursor =  dataBase.query(CURRENCY_TABLE_NAME, projection,
                selection, selectionValues, null, null, CURRENCY_COLUMN_CHAR_CODE);
        return cursor;
    }

    public void add(Currency currency) {
        ContentValues values = new ContentValues();
        values.put(CURRENCY_COLUMN_ID, currency.getId());
        values.put(CURRENCY_COLUMN_CHAR_CODE, currency.getCharCode());
        values.put(CURRENCY_COLUMN_FLAG_IMAGE_NAME, currency.getFlagImageName());
        values.put(CURRENCY_COLUMN_NAME, currency.getName());
        values.put(CURRENCY_COLUMN_NOMINAL, currency.getNominal());
        values.put(CURRENCY_COLUMN_NUM_CODE, currency.getNumCode());
        values.put(CURRENCY_COLUMN_VALUE, currency.getValue());
        dataBase.insert(CURRENCY_TABLE_NAME, "null", values);
    }

    public void addList(List<Currency> currencyList){
        if(currencyList == null){
            return;
        }
        for(Currency currency : currencyList){
            add(currency);
        }
    }

    public void clear(){
        dataBase.delete(CURRENCY_TABLE_NAME, null, null);
    }

    public void addRusCurrency() {
        ContentValues values = new ContentValues();
        values.put(CURRENCY_COLUMN_ID, context.getResources().getString(R.string.rub_id));
        values.put(CURRENCY_COLUMN_CHAR_CODE,
                context.getResources().getString(R.string.rub_char_code));
        values.put(CURRENCY_COLUMN_FLAG_IMAGE_NAME,
                context.getResources().getString(R.string.rub_flag_name));
        values.put(CURRENCY_COLUMN_NAME,
                context.getResources().getString(R.string.rub_name));
        values.put(CURRENCY_COLUMN_NOMINAL,
                context.getResources().getInteger(R.integer.rub_nominal));
        values.put(CURRENCY_COLUMN_NUM_CODE,
                context.getResources().getInteger(R.integer.rub_num_code));
        values.put(CURRENCY_COLUMN_VALUE,
                context.getResources().getString(R.string.rub_value));
        dataBase.insert(CURRENCY_TABLE_NAME, "null", values);
    }


    private class DBHelper extends SQLiteOpenHelper {

        private static final String TAG = "DBHelper";

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(TAG, "onCreate database");
            db.execSQL(DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DB_DROP);
            onCreate(db);
        }
    }
}
