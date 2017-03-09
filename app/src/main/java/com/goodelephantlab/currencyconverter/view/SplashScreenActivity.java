package com.goodelephantlab.currencyconverter.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.goodelephantlab.currencyconverter.loaders.LoadDataService;
import com.goodelephantlab.currencyconverter.R;

/**
 * Created by Edridtat on 06.03.2017.
 */
public class SplashScreenActivity extends AppCompatActivity {

    private static final String TAG = "SplashScreenActivity";
    private BroadcastReceiver receiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent mainActivityIntent = new Intent(this, MainActivity.class);
        receiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String loadingResultKey = getResources().getString(R.string.loading_result);
                String loadingErrorKey = getResources().getString(R.string.loading_error);
                int result = intent.getIntExtra(loadingResultKey,
                        getResources().getInteger(R.integer.loading_result_unknown));
                Log.d(TAG, "onReceive result = "+result);
                String error = intent.getStringExtra(loadingErrorKey);
                mainActivityIntent.putExtra(loadingResultKey, result);
                mainActivityIntent.putExtra(loadingErrorKey, error);
                finish();
                startActivity(mainActivityIntent);
            }
        };
        IntentFilter intentFilter = new IntentFilter(getResources().getString(R.string.load_data_broadcast_action));
        registerReceiver(receiver, intentFilter);
        Intent serviceIntent = new Intent(this, LoadDataService.class);
        startService(serviceIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

}
