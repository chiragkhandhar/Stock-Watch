package ml.chiragkhandhar.stockwatch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper
{
    private static final String TAG = "DatabaseHandler";
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "Stock_Watch";
    private static final String TABLE_NAME = "Stock_Watchlist";

    private static final String SYMBOL = "Symbol";
    private static final String COMPANY_NAME = "companyName";

    private static final String CREATE_TABLE_QUERY =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    SYMBOL + " TEXT not null unique," +
                    COMPANY_NAME + " TEXT not null)";

    private SQLiteDatabase db;

    DatabaseHandler(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
        db = getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        Log.d(TAG, "onCreate: bp: Creating new Database");
        sqLiteDatabase.execSQL(CREATE_TABLE_QUERY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {

    }

    public ArrayList<Stock> loadStocks()
    {
        ArrayList<Stock> stockArrayList =  new ArrayList<>();

        Cursor cursor = db.query(
                TABLE_NAME,  // The table to query
                new String[]{SYMBOL, COMPANY_NAME}, // The columns to return
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null); // The sort order

        if(cursor!=null)
        {
            cursor.moveToFirst();

            for(int i = 0;  i < cursor.getCount(); i++ )
            {
                String symbol = cursor.getString(0);
                String companyName = cursor.getString(1);
                Stock temp = new Stock(symbol,companyName);
                stockArrayList.add(temp);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return stockArrayList;
    }

    void addStock(Stock temp)
    {
        ContentValues values = new ContentValues();
        Log.d(TAG, "addStock: bp: Adding Stock: "+ temp.getSymbol() + " | " + temp.getCompanyName());
        values.put(SYMBOL, temp.getSymbol());
        values.put(COMPANY_NAME, temp.getCompanyName());
        long key = db.insert(TABLE_NAME, null, values);
        Log.d(TAG, "addStock: bp: Added Stock: "+ temp.getSymbol() + " | " + temp.getCompanyName());
    }

    void deleteStock(Stock temp)
    {
        Log.d(TAG, "deleteStock: bp: Deleting Stock: " + temp.getSymbol()+ " | " + temp.getCompanyName());
        int cnt = db.delete(TABLE_NAME, SYMBOL + " = ?", new String[]{temp.getSymbol()});
        Log.d(TAG, "deleteStock: bp: Deleted Stock: " + temp.getSymbol()+ " | " + temp.getCompanyName());
    }

    void shutDown()
    {
        db.close();
    }
}
