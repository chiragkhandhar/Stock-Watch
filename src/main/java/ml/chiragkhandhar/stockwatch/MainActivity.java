package ml.chiragkhandhar.stockwatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener
{
    private SwipeRefreshLayout swiper;
    private RecyclerView rv;
    private ArrayList<Stock>stocksArrayList= new ArrayList<>();
    private StockAdapter stockAdapter;
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupComponents();

        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                Log.d(TAG, "onRefresh: bp:");
                swiper.setRefreshing(false);
            }
        });

        stockAdapter = new StockAdapter(stocksArrayList, this);
        rv.setAdapter(stockAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));


        //loadDummydata();
        // Load the data
        new NameLoader(this).execute();
    }

    public void loadDummydata()
    {

        for (int i = 0; i < 10; i++)
        {
            Stock temp = new Stock();
            Stock temp2 = new Stock();
            temp.setCompanyName("Apple");
            temp.setSymbol("APL");
            temp.setLatestPrice(116.23);
            temp.setChange(0);
            temp.setChangePercent(0);
            stocksArrayList.add(temp);
            temp2.setCompanyName("Tesla");
            temp2.setSymbol("TSL");
            temp2.setLatestPrice(816.23);
            temp2.setChange(0.5);
            temp2.setChangePercent(10.2);
            stocksArrayList.add(temp2);
        }
    }

    public void setupComponents()
    {
        swiper = findViewById(R.id.swiper);
        rv = findViewById(R.id.recycler);
    }

    public void updateData(ArrayList<Stock> cList)
    {
        stocksArrayList.addAll(cList);
        stockAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.opt_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.addStock:
                break;
            default:
                Toast.makeText(this,"Invalid Option",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view)
    {
        int position = rv.getChildAdapterPosition(view);
        Stock s = stocksArrayList.get(position);

        Toast.makeText(this, "Selected "+s.getSymbol()+" stock.",Toast.LENGTH_SHORT).show();


    }

    @Override
    public boolean onLongClick(final View view)
    {
        deleteAlert(view);
        return true;
    }

    private void deleteAlert(final View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                int pos = rv.getChildLayoutPosition(view);
                stocksArrayList.remove(pos);
                stockAdapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {

            }
        });

        int pos = rv.getChildLayoutPosition(view);
        Stock s = stocksArrayList.get(pos);

        builder.setTitle("Do you want to delete " + s.getSymbol()+ "?");
        builder.setMessage(R.string.delete_note2);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
