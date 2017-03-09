package com.goodelephantlab.currencyconverter.loaders;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.goodelephantlab.currencyconverter.R;
import com.goodelephantlab.currencyconverter.model.data.Currency;
import com.goodelephantlab.currencyconverter.model.data.CurrencyList;
import com.goodelephantlab.currencyconverter.model.db.DataBase;
import org.simpleframework.xml.core.Persister;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Edridtat on 06.03.2017.
 */
public class LoadDataService extends IntentService {

    private static final String TAG = "LoadDataService";

    public LoadDataService() {
        super(TAG);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent");
        ConnectivityManager cm = (ConnectivityManager) getBaseContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        boolean isConnect = netInfo != null && netInfo.isConnectedOrConnecting();
        if(!isConnect){
            sendResult(getResources().getInteger(R.integer.loading_result_error),
                    getResources().getString(R.string.internet_error));
            return;
        }
        String content;
        try{
            content = getContent(getResources().getString(R.string.data_url));
        }
        catch (IOException e){
            e.printStackTrace();
            sendResult(getResources().getInteger(R.integer.loading_result_error),
                    getResources().getString(R.string.internet_error));
            return;
        }
        List<Currency> currencyList = parseData(content);
        if(currencyList != null && !currencyList.isEmpty()){
            storeData(currencyList);
            sendResult(getResources().getInteger(R.integer.loading_result_success), null);
        }else{
            sendResult(getResources().getInteger(R.integer.loading_result_error),
                    getResources().getString(R.string.unknown_error));
        }
    }

    private void sendResult(int result, String error){
        Intent callbackIntent = new Intent(getResources().getString(R.string.load_data_broadcast_action));
        callbackIntent.putExtra(getResources().getString(R.string.loading_result), result);
        if(result == getResources().getInteger(R.integer.loading_result_error)){
            callbackIntent.putExtra(getResources().getString(R.string.loading_error), error);
        }
        sendBroadcast(callbackIntent);
    }

    private String getContent(String path) throws IOException{
        BufferedReader reader=null;
        try {
            URL url=new URL(path);
            HttpsURLConnection c=(HttpsURLConnection)url.openConnection();
            c.setRequestMethod("GET");
            c.setReadTimeout(getResources().getInteger(R.integer.read_timeout));
            c.connect();
            reader= new BufferedReader(new InputStreamReader(c.getInputStream(),
                    getResources().getString(R.string.charset_name)));
            StringBuilder buf=new StringBuilder();
            String line;
            while ((line=reader.readLine()) != null) {
                buf.append(line.concat("\n"));
            }
            return(buf.toString());
        }
        finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    private List<Currency> parseData(String xml) {
        List<Currency> list = null;
        Reader reader = new StringReader(xml);
        Persister serializer = new Persister();
        try
        {
            CurrencyList currencyList =serializer.read(CurrencyList.class, reader, false);
            Log.d(TAG, "Currency count: " + currencyList.getList().size());
            list =  currencyList.getList();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return list;
    }

    private void storeData(List<Currency> currencyList){
        Log.d(TAG, "storeData");
        DataBase dataBase = new DataBase(getBaseContext());
        dataBase.open();
        dataBase.clear();
        dataBase.addRusCurrency();
        dataBase.addList(currencyList);
        dataBase.close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}