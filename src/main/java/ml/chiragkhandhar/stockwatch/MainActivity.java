package ml.chiragkhandhar.stockwatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener
{
    private SwipeRefreshLayout swiper;
    private RecyclerView rv;
    private ArrayList<Stock>stocksArrayList= new ArrayList<>();
    private HashMap<String,String> hashMap = new HashMap<String, String>(); //Contains stocks to search from
    private StockAdapter stockAdapter;
    private String searchString;
    private static final String TAG = "MainActivity";
    private DatabaseHandler dbh;

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

        // Fetch the Stocks
        new NameLoader(this).execute();
        dbh = new DatabaseHandler(this);
    }

    @Override
    protected void onResume()
    {
        ArrayList<Stock> tempList = dbh.loadStocks();
        stocksArrayList.clear();
        stocksArrayList.addAll(sortList(tempList));
        stockAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    protected void onDestroy()
    {
        dbh.shutDown();
        super.onDestroy();
    }


    public void setupComponents()
    {
        swiper = findViewById(R.id.swiper);
        rv = findViewById(R.id.recycler);
    }

    ArrayList<Stock> sortList(ArrayList<Stock> temp)
    {
        Collections.sort(temp, new Comparator<Stock>() {
            @Override
            public int compare(Stock s1, Stock s2) {
                return s1.getSymbol().compareTo(s2.getSymbol());
            }
        });

        return temp;
    }

    public boolean networkChecker()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm == null)
            return false;

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    public void updateData(HashMap<String,String> stockMap)
    {
        hashMap.putAll(stockMap);
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
                addStock();
                break;
            default:
                Toast.makeText(this,"Invalid Option",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void addStock()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        et.setGravity(Gravity.CENTER_HORIZONTAL);
        builder.setView(et);

        builder.setIcon(R.drawable.ic_search);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id)
            {
                searchString = et.getText().toString().toUpperCase().trim();
                Log.d(TAG, "onClick: bp: Search String = "+searchString);
                if(!searchString.trim().equals(""))
                    searchStock(searchString);
                else
                {
                    Toast.makeText(MainActivity.this, R.string.nullSearchStringMsg, Toast.LENGTH_SHORT).show();
                    addStock();
                }

            }
        });
        builder.setNegativeButton("NO WAY", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id)
            {
                Toast.makeText(MainActivity.this, R.string.negativeBtn2, Toast.LENGTH_SHORT).show();
            }
        });

        builder.setMessage(R.string.searchBoxMsg);
        builder.setTitle(R.string.selectCompanies);

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void searchStock(String searchString)
    {
        ArrayList<Stock> tempList= new ArrayList<>();
        for(String key : hashMap.keySet())
        {
            if(key.startsWith(searchString) || Objects.requireNonNull(hashMap.get(key)).contains(searchString))
            {
                Stock temp = new Stock(key,hashMap.get(key));
                tempList.add(temp);
            }
        }
        Log.d(TAG, "updateData: bp: "+tempList.size() +" Stocks found");
        if(tempList.size() > 1)
        {
            multipleStock(tempList);
        }
        else if (tempList.size() == 1)
        {
            if(checkDuplicate(tempList.get(0)))
                saveDB(tempList.get(0));
        }
        else
        {
            Toast.makeText(this, "No such stock found!", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "searchStock: bp: No Stock Found");
        }
    }

    public void multipleStock(final ArrayList<Stock> tempList)
    {
        final CharSequence[] sArray = new CharSequence[tempList.size()];
        for (int i = 0; i < tempList.size(); i++)
            sArray[i] = tempList.get(i).getSymbol() + " | " + tempList.get(i).getCompanyName().toLowerCase();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.searchTitle);
        builder.setIcon(R.drawable.ic_search);

        builder.setItems(sArray, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which)
            {
                Stock temp  = tempList.get(which);
                Log.d(TAG, "onClick: bp: sArray["+which+"]: "+sArray[which]);
                Log.d(TAG, "onClick: bp: temp: "+temp);
                if(checkDuplicate(temp))
                    saveDB(temp);
            }
        });

        builder.setNegativeButton(R.string.negativeBtn1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(MainActivity.this, "You changed your mind!", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();

        dialog.show();

    }

    public boolean checkDuplicate(Stock s)
    {
        for (Stock temp: stocksArrayList)
        {
            if(temp.getSymbol().equals(s.getSymbol()))
            {
                Toast.makeText(this, s.getSymbol()+" is already added!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;

    }

    public void saveDB(Stock s)
    {
        dbh.addStock(s);
        stocksArrayList.add(s);
        stocksArrayList = sortList(stocksArrayList);
        stockAdapter.notifyDataSetChanged();
        Log.d(TAG, "saveDB: bp: Saved " + s.getSymbol() + " to dB.");
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
                dbh.deleteStock(stocksArrayList.get(pos));
                stocksArrayList.remove(pos);
                stocksArrayList = sortList(stocksArrayList);
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
