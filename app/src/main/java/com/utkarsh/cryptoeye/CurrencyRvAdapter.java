package com.utkarsh.cryptoeye;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CurrencyRvAdapter extends RecyclerView.Adapter<CurrencyRvAdapter.ViewHolder> {

    private ArrayList<CurrencyRVModal> currencyRVModalArrayList;
    private Context context;
    private static DecimalFormat df2 = new DecimalFormat("#.##");


    public CurrencyRvAdapter(ArrayList<CurrencyRVModal> currenctRvModalArrayList, Context context) {
        this.currencyRVModalArrayList = currenctRvModalArrayList;
        this.context = context;
    }
    public void filterList(ArrayList<CurrencyRVModal>filteredList){
        currencyRVModalArrayList = filteredList;
        notifyDataSetChanged();

    }
    @NonNull
    @Override
    public CurrencyRvAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.currency_rv_item,parent,false);
        return new CurrencyRvAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyRvAdapter.ViewHolder holder, int position) {
        CurrencyRVModal currencyRVModal = currencyRVModalArrayList.get(position);
        holder.currencyNameTv.setText(currencyRVModal.getName());
        holder.symbolTv.setText(currencyRVModal.getSymbol());
        holder.rateTv.setText("$ "+df2.format(currencyRVModal.getPrice()));
    }

    @Override
    public int getItemCount() {
        return currencyRVModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView currencyNameTv,symbolTv,rateTv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            currencyNameTv=itemView.findViewById(R.id.idTvCurrencyName);
            symbolTv=itemView.findViewById(R.id.idTvSymbol);
            rateTv=itemView.findViewById(R.id.idTvCurrencyRate);
        }
    }
}
