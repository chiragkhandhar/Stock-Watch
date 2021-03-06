package ml.chiragkhandhar.stockwatch;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class StockLoader extends AsyncTask<Void,Void,ArrayList<Stock>>
{
    @SuppressLint("StaticFieldLeak")
    private MainActivity mainActivity;
    private static final String TAG = "StockLoader";
    // Add your API_KEY here
    private static final String API_TOKEN = BuildConfig.API_KEY;


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

    private String getStockDatafromURL(String URL)
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

    private String getSymbolfromAPI(String s)
    {
        String symbol = "";
        try
        {
            JSONObject jStock = new JSONObject(s);
            symbol = jStock.getString("symbol");
        }
        catch (Exception e)
        {
            Log.d(TAG, "ERROR| getSymbolfromAPI| bp:" + e.getMessage());
            e.printStackTrace();
        }

        return symbol;
    }

    private String getCompanyNamefromAPI(String s)
    {
        String companyName = "";
        try
        {
            JSONObject jStock = new JSONObject(s);
            companyName = jStock.getString("companyName");
        }
        catch (Exception e)
        {
            Log.d(TAG, "ERROR| getCompanyNamefromAPI| bp:" + e.getMessage());
            e.printStackTrace();
        }

        return companyName;
    }

    private double getLatestPricefromAPI(String s)
    {
        double latestPrice = 0.00;
        String tlatestPrice ;
        try
        {
            JSONObject jStock = new JSONObject(s);
            tlatestPrice = jStock.getString("latestPrice");
            latestPrice = Math.round(Double.parseDouble(tlatestPrice)*100.00)/100.00;
        }
        catch (Exception e)
        {
            Log.d(TAG, "ERROR| getLatestPricefromAPI| bp:" + e.getMessage());
            e.printStackTrace();
        }

        return latestPrice;
    }

    private double getChangefromAPI(String s)
    {
        double change = 0.00;
        String tChange;
        try
        {
            JSONObject jStock = new JSONObject(s);
            tChange = jStock.getString("change");
            change = Math.round(Double.parseDouble(tChange)*100.00)/100.00;
        }
        catch (Exception e)
        {
            Log.d(TAG, "ERROR| getChangefromAPI| bp:" + e.getMessage());
            e.printStackTrace();
        }

        return change;
    }

    private double getChangePercentfromAPI(String s)
    {
        double changePercent = 0.00;
        String tChangePercent;
        try
        {
            JSONObject jStock = new JSONObject(s);
            tChangePercent = jStock.getString("changePercent");
            changePercent = Math.round(Double.parseDouble(tChangePercent)*100.00)/100.00;
        }
        catch (Exception e)
        {
            Log.d(TAG, "ERROR| getChangePercentfromAPI| bp:" + e.getMessage());
            e.printStackTrace();
        }

        return changePercent;
    }

    private Stock parseJSON(String s)
    {
        String symbol , companyName;
        double latestPrice , change , changePercent;

        symbol = getSymbolfromAPI(s);
        companyName = getCompanyNamefromAPI(s);
        latestPrice = getLatestPricefromAPI(s);
        change = getChangefromAPI(s);
        changePercent = getChangePercentfromAPI(s);
        Log.d(TAG, "parseJSON: bp: companyName: "+companyName + " latestPrice: "+ latestPrice + " change: "+change );

        return new Stock(symbol,companyName,latestPrice,change,changePercent);
    }

}
