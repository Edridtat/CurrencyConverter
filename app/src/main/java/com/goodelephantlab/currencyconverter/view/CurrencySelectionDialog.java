package com.goodelephantlab.currencyconverter.view;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import com.goodelephantlab.currencyconverter.view.adapters.CurrencyRecyclerViewAdapter;
import com.goodelephantlab.currencyconverter.R;
import com.goodelephantlab.currencyconverter.presenter.ActivityPresenter;
import java.util.ArrayList;

/**
 * Created by Edridtat on 07.03.2017.
 */
public class CurrencySelectionDialog extends DialogFragment implements View.OnClickListener {

    private RecyclerView selectorRecyclerView;
    private ArrayList<String> currencyNames;
    private ArrayList<Integer> currencyFlagsId;
    private ActivityPresenter presenter;
    private String clickedPreferenceId;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        Bundle args = getArguments();
        if(args!=null){
            currencyNames  = args.getStringArrayList(
                    getString(R.string.selection_dialog_names_arg_id));
            currencyFlagsId = args.getIntegerArrayList(
                    getString(R.string.selection_dialog_flags_arg_id));
            clickedPreferenceId = args.getString(
                    getString(R.string.selection_dialog_clicked_preference_id));
        }
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.currency_selector_dialog, container, false);
        selectorRecyclerView = (RecyclerView) rootView.findViewById(R.id.currency_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        RecyclerView.ItemAnimator statItemAnimator = new DefaultItemAnimator();
        selectorRecyclerView.setLayoutManager(layoutManager);
        selectorRecyclerView.setItemAnimator(statItemAnimator);
        selectorRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getActivity(), R.drawable.currency_recycler_divider));
        CurrencyRecyclerViewAdapter currencyRecyclerAdapter =
                new CurrencyRecyclerViewAdapter(currencyNames, currencyFlagsId, this);
        selectorRecyclerView.setAdapter(currencyRecyclerAdapter);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public void setPresenter(ActivityPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onClick(View view) {
        int clickedPosition = selectorRecyclerView.getChildLayoutPosition(view);
        if(presenter!=null){
            presenter.currencySelected(clickedPosition, clickedPreferenceId);
        }
        dismiss();
    }
}