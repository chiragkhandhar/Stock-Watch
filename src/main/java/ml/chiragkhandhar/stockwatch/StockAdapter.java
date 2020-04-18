package ml.chiragkhandhar.stockwatch;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StockAdapter extends  RecyclerView.Adapter<StockViewHolder>
{
    private static final String TAG = "NotesAdapter";
    private List<Stock> stockList;
    private  MainActivity mainAct;

    StockAdapter(List<Stock> stockList, MainActivity mainAct)
    {
        this.stockList = stockList;
        this.mainAct = mainAct;
    }

    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_entry,parent,false);
        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);
        return new StockViewHolder(itemView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull StockViewHolder holder, int position)
    {
        Stock temp = stockList.get(position);
        holder.symbol.setText(temp.getSymbol());
        holder.companynName.setText(temp.getCompanyName());
        holder.price.setText(String.format("$ %s", temp.getLatestPrice()));
        holder.change.setText(String.format("%s", temp.getChange()));
        holder.changePercent.setText(String.format("(%s %%)", temp.getChangePercent()));

        int COLOR_CODE = temp.getCOLOR_CODE();

        switch (COLOR_CODE)
        {
            case 1: // Positive
                holder.symbol.setTextColor(Color.parseColor("#0f9d58"));
                holder.companynName.setTextColor(Color.parseColor("#0f9d58"));
                holder.price.setTextColor(Color.parseColor("#0f9d58"));
                holder.change.setTextColor(Color.parseColor("#0f9d58"));
                holder.changePercent.setTextColor(Color.parseColor("#0f9d58"));
                holder.changeIcon.setBackgroundResource(R.drawable.ic_up_arrow);
                break;
            case 2: // Negative
                holder.symbol.setTextColor(Color.parseColor("#db4437"));
                holder.companynName.setTextColor(Color.parseColor("#db4437"));
                holder.price.setTextColor(Color.parseColor("#db4437"));
                holder.change.setTextColor(Color.parseColor("#db4437"));
                holder.changePercent.setTextColor(Color.parseColor("#db4437"));
                holder.changeIcon.setBackgroundResource(R.drawable.ic_down_arrow);
                break;
            case 3:
                holder.symbol.setTextColor(Color.parseColor("#FFFFFF"));
                holder.companynName.setTextColor(Color.parseColor("#FFFFFF"));
                holder.price.setTextColor(Color.parseColor("#FFFFFF"));
                holder.change.setTextColor(Color.parseColor("#FFFFFF"));
                holder.changePercent.setTextColor(Color.parseColor("#FFFFFF"));
                holder.changeIcon.setBackgroundResource(R.drawable.ic_neutral);
                break;
            default: // Neutral
                holder.symbol.setTextColor(Color.parseColor("#656565"));
                holder.companynName.setTextColor(Color.parseColor("#656565"));
                holder.price.setTextColor(Color.parseColor("#656565"));
                holder.change.setTextColor(Color.parseColor("#656565"));
                holder.changePercent.setTextColor(Color.parseColor("#656565"));
                holder.changeIcon.setBackgroundResource(R.drawable.ic_neutral);
                break;
        }

    }

    @Override
    public int getItemCount()
    {
        return stockList.size();
    }
}
