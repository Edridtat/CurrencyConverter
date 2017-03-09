package com.goodelephantlab.currencyconverter.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.goodelephantlab.currencyconverter.R;
import java.util.ArrayList;

/**
 * Created by Edridtat on 07.03.2017.
 */
public class CurrencyRecyclerViewAdapter extends
        RecyclerView.Adapter<CurrencyRecyclerViewAdapter.ViewHolder>{

    private ArrayList<String> currencyNames;
    private ArrayList<Integer> currencyFlagsId;
    private View.OnClickListener onClickListener;

    public CurrencyRecyclerViewAdapter(ArrayList<String> currencyNames,
                                       ArrayList<Integer> currencyFlagsId,
                                       View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        this.currencyNames = currencyNames;
        this.currencyFlagsId = currencyFlagsId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.currency_selector_item, viewGroup, false);
        view.setOnClickListener(onClickListener);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.currencyNameTextView.setText(currencyNames.get(i));
        viewHolder.currencyImageView.setImageResource(currencyFlagsId.get(i));
    }

    @Override
    public int getItemCount() {
        if(currencyNames!=null){
            return currencyNames.size();
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView currencyNameTextView;
        private ImageView currencyImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            currencyNameTextView = (TextView) itemView.findViewById(R.id.currency_name_text_view);
            currencyImageView = (ImageView) itemView.findViewById(R.id.currency_image_view);
        }
    }
}
