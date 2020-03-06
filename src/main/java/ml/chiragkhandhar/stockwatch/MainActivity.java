package ml.chiragkhandhar.stockwatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
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
import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener
{
    private SwipeRefreshLayout swiper;
    private RecyclerView rv;
    private ArrayList<Stock>stocksArrayList= new ArrayList<>();
    private HashMap<String,String> hashMap = new HashMap<String, String>();
    private StockAdapter stockAdapter;
    private String searchString;
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

        // Load the data
        new NameLoader(this).execute();
    }



    public void setupComponents()
    {
        swiper = findViewById(R.id.swiper);
        rv = findViewById(R.id.recycler);
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
                    Toast.makeText(MainActivity.this, "Seems, you've not entered anything!", Toast.LENGTH_SHORT).show();
                    addStock();
                }

            }
        });
        builder.setNegativeButton("NO WAY", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id)
            {
                Toast.makeText(MainActivity.this, "You changed your mind!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setMessage("Please enter a Stock symbol: ");
        builder.setTitle("Select Companies");

        AlertDialog dialog = builder.create();
        dialog.show();



    }

    public void searchStock(String searchString)
    {
        ArrayList<Stock> tempList= new ArrayList<>();
        for(String key : hashMap.keySet())
        {
            if(key.startsWith(searchString))
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
            // Save to DB
        }
        else
        {
            Log.d(TAG, "searchStock: bp: No Stock Found");
        }
    }

    public void multipleStock(final ArrayList<Stock> tempList)
    {

        final CharSequence[] sArray = new CharSequence[tempList.size()];
        for (int i = 0; i < tempList.size(); i++)
            sArray[i] = tempList.get(i).getSymbol() + " | " + tempList.get(i).getCompanyName();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Make a selection");
        builder.setIcon(R.drawable.ic_search);

        builder.setItems(sArray, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which)
            {
                Stock temp  = tempList.get(which);
                Log.d(TAG, "onClick: bp: sArray["+which+"]: "+sArray[which]);
                Log.d(TAG, "onClick: bp: temp: "+temp);
                saveDB(temp);
            }
        });

        builder.setNegativeButton("Nevermind", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(MainActivity.this, "You changed your mind!", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();

        dialog.show();

    }

    public void saveDB(Stock s)
    {

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
