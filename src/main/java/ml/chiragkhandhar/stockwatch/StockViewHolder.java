package ml.chiragkhandhar.stockwatch;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

class StockViewHolder extends RecyclerView.ViewHolder
{
    TextView symbol, companynName, price, change, changePercent;
    ImageView changeIcon;

    StockViewHolder(@NonNull View itemView)
    {
        super(itemView);
        symbol = itemView.findViewById(R.id.symbol);
        companynName = itemView.findViewById(R.id.companyName);
        price = itemView.findViewById(R.id.price);
        change = itemView.findViewById(R.id.change);
        changePercent = itemView.findViewById(R.id.changePercent);
        changeIcon = itemView.findViewById(R.id.changeIcon);
    }
}
