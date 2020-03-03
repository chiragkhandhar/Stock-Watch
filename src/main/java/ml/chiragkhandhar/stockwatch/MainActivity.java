package ml.chiragkhandhar.stockwatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
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

        stockAdapter = new StockAdapter(stocksArrayList, this);
        rv.setAdapter(stockAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        loadDummydata();
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
        Stock temp = new Stock();
        temp.setCompanyName("Apple");
        temp.setSymbol("APL");
        temp.setLatestPrice(116.23);
        temp.setChange(0);
        temp.setChangePercent(0);
        stocksArrayList.add(temp);
    }

    public void setupComponents()
    {
        swiper = findViewById(R.id.swiper);
        rv = findViewById(R.id.recycler);
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
}
