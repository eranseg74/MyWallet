package com.sgl.mywallet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CurrenciesAdapter extends RecyclerView.Adapter<CurrenciesAdapter.CurrencyViewHolder> {

    private Context context;
    private List<Currency> currenciesList;

    public CurrenciesAdapter(Context context, List<Currency> currenciesList) {
        this.context = context;
        this.currenciesList = currenciesList;
    }

    @NonNull
    @Override
    public CurrencyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.currency_layout, null);
        return new CurrencyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyViewHolder holder, int position) {
        Currency currency = currenciesList.get(position);
        holder.cAcronym.setText(currency.getCoinAcronym());
        holder.cFullName.setText(currency.getCoinFullName());
        holder.cVal.setText(currency.getCoinValue());

        holder.flag.setImageDrawable(context.getResources().getDrawable(currency.getImage(), null));
        holder.cFormat.setImageDrawable(context.getResources().getDrawable(currency.getFormat(), null));
    }

    @Override
    public int getItemCount() {
        return currenciesList.size();
    }

    class CurrencyViewHolder extends RecyclerView.ViewHolder {

        ImageView flag, cFormat;
        TextView cAcronym, cFullName, cVal;

        public CurrencyViewHolder(@NonNull View itemView) {
            super(itemView);
            flag = itemView.findViewById(R.id.countryFlag);
            cAcronym = itemView.findViewById(R.id.coinAcronym);
            cFullName = itemView.findViewById(R.id.coinFullName);
            cVal = itemView.findViewById(R.id.coinValToBase);
            cFormat = itemView.findViewById(R.id.coinFormat);
        }
    }
}
