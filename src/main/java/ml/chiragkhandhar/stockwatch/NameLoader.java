package ml.chiragkhandhar.stockwatch;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class NameLoader extends AsyncTask<Void, Void, String>
{
    @SuppressLint("StaticFieldLeak")
    private MainActivity mainActivity;

    private static final String DATA_URL = "https://api.iextrading.com/1.0/ref-data/symbols";
    private static final String TAG = "NameLoader";

    public NameLoader(MainActivity mainActivity)
    {
        this.mainActivity = mainActivity;
    }

    @Override
    protected void onPostExecute(String s)
    {
        ArrayList<Stock>stockList = parseJSON(s);
        Log.d(TAG, "onPostExecute: bp:  "+ stockList);
        if (stockList != null)
            Toast.makeText(mainActivity, "Loaded " + stockList.size() + " countries.", Toast.LENGTH_SHORT).show();
        mainActivity.updateData(stockList);
    }

    @Override
    protected String doInBackground(Void... voids)
    {
        Uri dataUri = Uri.parse(DATA_URL);
        String urlToUse = dataUri.toString();
        Log.d(TAG, "doInBackground: bp:" + urlToUse);

        StringBuilder sb = new StringBuilder();
        try
        {
            URL url = new URL(urlToUse);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            Log.d(TAG, "doInBackground: bp: ResponseCode: " + conn.getResponseCode());

            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null)
                sb.append(line).append('\n');

        }
        catch (Exception e)
        {
            Log.e(TAG, "doInBackground: bp:", e);
            return null;
        }
        return sb.toString();
    }

    private ArrayList<Stock> parseJSON(String s)
    {
        ArrayList<Stock> stockList = new ArrayList<>();
        try
        {
            JSONArray jsonArray = new JSONArray(s);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jStock = (JSONObject) jsonArray.get(i);
                String symbol = jStock.getString("symbol");
                String name = jStock.getString("name");
                stockList.add(new Stock(symbol, name));
            }
            return stockList;
        }
        catch (Exception e)
        {
            Log.d(TAG, "parseJSON: bp:" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
