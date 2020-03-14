package ml.chiragkhandhar.stockwatch;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class StockLoader extends AsyncTask<Void,Void,ArrayList<Stock>>
{
    @SuppressLint("StaticFieldLeak")
    private MainActivity mainActivity;
    private static final String TAG = "StockLoader";
    private static final String API_TOKEN = "sk_ddd06e00c8944e06975dd84945130a63";


    public StockLoader(MainActivity mainActivity)
    {
        this.mainActivity = mainActivity;
    }


    @Override
    protected ArrayList<Stock> doInBackground(Void... voids)
    {
        ArrayList<Stock> stocksArrayList = mainActivity.getStocksArrayList();
        ArrayList<Stock> tempList = new ArrayList<>();

        for(Stock temp: stocksArrayList)
        {
            String STOCK_SYMBOL = temp.getSymbol();
            String DATA_URL = "https://cloud.iexapis.com/stable/stock/"+STOCK_SYMBOL+"/quote?token="+API_TOKEN;
            String data = getStockDatafromURL(DATA_URL);
            Stock jsonData = parseJSON(data);
            tempList.add(jsonData);
        }
        return tempList;
    }

    public  String getStockDatafromURL(String URL)
    {
        Uri dataUri = Uri.parse(URL);
        String urlToUse = dataUri.toString();

        StringBuilder sb = new StringBuilder();
        try
        {
            java.net.URL url = new URL(urlToUse);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null)
                sb.append(line).append('\n');
        }
        catch (Exception e)
        {
            Log.e(TAG, "StockLoader: doInBackground: bp:", e);
            return null;
        }
        return sb.toString();
    }

    @Override
    protected void onPostExecute(ArrayList<Stock> tempList)
    {
        mainActivity.updateStockData(tempList);
        super.onPostExecute(tempList);
    }

    private Stock parseJSON(String s)
    {
        String companyName;
        double latestPrice, change, changePercent;
        try
        {
                JSONObject jStock = new JSONObject(s);
                String tSymbol = jStock.getString("symbol");
                String tCompanyName = jStock.getString("companyName");
                String tLatestPrice = jStock.getString("latestPrice");
                String tChange = jStock.getString("change");
                String tChangePercent = jStock.getString("changePercent");

                if(tCompanyName.length() == 0)
                    tCompanyName = "";
                if(tLatestPrice.length() == 0)
                    tLatestPrice = "0";
                if(tChange.length() == 0)
                    tChange = "0";
                if(tChangePercent.length() == 0)
                    tChangePercent = "0";

                String symbol = jStock.getString("symbol");
                companyName = tCompanyName;
                latestPrice = Math.round(Double.parseDouble(tLatestPrice)*100.00)/100.00;
                change = Math.round(Double.parseDouble(tChange)*100.00)/100.00;
                changePercent = Math.round(Double.parseDouble(tChangePercent)*100.00)/100.00;
                Log.d(TAG, "parseJSON: bp: companyName: "+companyName + " latestPrice: "+ latestPrice + " change: "+change );
                return new Stock(symbol,companyName,latestPrice,change,changePercent);
        }
        catch (Exception e)
        {
            Log.d(TAG, "parseJSON ERROR: bp:" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}
