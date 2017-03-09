package com.goodelephantlab.currencyconverter.view;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.goodelephantlab.currencyconverter.presenter.ActivityPresenter;
import com.goodelephantlab.currencyconverter.R;

/**
 * Created by Edridtat on 06.03.2017.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private ActivityPresenter presenter;
    private EditText startCurrencyEditText;
    private EditText finishCurrencyEditText;
    private ImageButton startFlagImageButton;
    private ImageButton finishFlagImageButton;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        startFlagImageButton = (ImageButton) findViewById(R.id.start_currency_flag_image_view);
        startFlagImageButton.setOnClickListener(this);
        finishFlagImageButton = (ImageButton)findViewById(R.id.finish_currency_flag_image_view);
        finishFlagImageButton.setOnClickListener(this);
        startCurrencyEditText = (EditText) findViewById(R.id.start_currency_edit_text);
        finishCurrencyEditText = (EditText) findViewById(R.id.finish_currency_edit_text);
        presenter = new ActivityPresenter(this, getIntent());
        presenter.updateData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab:
                hideKeyboard();
                double value = 0;
                String text = startCurrencyEditText.getText().toString();
                int editTextId = finishCurrencyEditText.getId();
                if(text.equals("")){
                    text = finishCurrencyEditText.getText().toString();
                    editTextId = startCurrencyEditText.getId();
                    startCurrencyEditText.setText("0.0");
                }
                if(!text.equals("")){
                    value = Double.parseDouble(text);
                }else{
                    editTextId = finishCurrencyEditText.getId();
                }
                presenter.convertButtonClicked(value, editTextId);
                break;
            case R.id.start_currency_flag_image_view:
                presenter.currencyButtonClicked(view);
                break;
            case R.id.finish_currency_flag_image_view:
                presenter.currencyButtonClicked(view);
                break;
        }
    }

    public void showError(String error) {
        Snackbar.make(findViewById(R.id.coordinator), error, Snackbar.LENGTH_LONG).show();
    }

    public void showProgressBar() {
        findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
        findViewById(R.id.content_wrapper).setVisibility(View.GONE);
        findViewById(R.id.error_text_view).setVisibility(View.GONE);
        fab.hide();
    }

    public void showContent(boolean success) {
        findViewById(R.id.progress_bar).setVisibility(View.GONE);
        if(success){
            findViewById(R.id.content_wrapper).setVisibility(View.VISIBLE);
            findViewById(R.id.error_text_view).setVisibility(View.GONE);
            fab.show();
        }else{
            findViewById(R.id.error_text_view).setVisibility(View.VISIBLE);
            findViewById(R.id.content_wrapper).setVisibility(View.GONE);
            fab.hide();
        }
    }

    public void updateUI(String startName, int startFlagImageId, String finishName, int finishFlagImageId) {
        startFlagImageButton.setImageResource(startFlagImageId);
        ((TextView)findViewById(R.id.start_currency_text_view)).setText(startName);
        finishFlagImageButton.setImageResource(finishFlagImageId);
        ((TextView)findViewById(R.id.finish_currency_text_view)).setText(finishName);

    }

    public void updateCountValue(double value, int editTextId) {
        ((EditText)findViewById(editTextId)).setText(String.valueOf(value));
    }

    public void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
