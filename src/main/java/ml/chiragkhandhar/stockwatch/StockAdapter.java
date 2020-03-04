package ml.chiragkhandhar.stockwatch;

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

    public StockAdapter(List<Stock> stockList, MainActivity mainAct)
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

    @Override
    public void onBindViewHolder(@NonNull StockViewHolder holder, int position)
    {
        Stock temp = stockList.get(position);
        holder.symbol.setText(temp.getSymbol());
        holder.companynName.setText(temp.getCompanyName());
        holder.price.setText("$ "+temp.getLatestPrice());
        holder.change.setText(""+temp.getChange());
        holder.changePercent.setText(""+temp.getChangePercent()+" %");
    }

    @Override
    public int getItemCount()
    {
        return stockList.size();
    }
}
