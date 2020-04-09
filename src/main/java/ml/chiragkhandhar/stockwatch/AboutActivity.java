package ml.chiragkhandhar.stockwatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    public void iexClicked(View v)
    {
        String URL = "https://iexcloud.io/";
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
        startActivity(i);
    }

    public void marketWatchClicked(View v)
    {
        String URL = "https://www.marketwatch.com/";
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
        startActivity(i);
    }
}
