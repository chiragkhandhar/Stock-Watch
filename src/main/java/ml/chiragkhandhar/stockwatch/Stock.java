package ml.chiragkhandhar.stockwatch;

import java.io.Serializable;

public class Stock implements Serializable
{
    private String symbol, companyName;
    private double latestPrice, change, changePercent;
    private  int COLOR_CODE;

    public Stock()
    {
        this.symbol = "";
        this.companyName = "";
        this.latestPrice = 0;
        this.change = 0;
        this.changePercent = 0;
        this.COLOR_CODE = 0;
    }

    public Stock(String symbol, String companyName)
    {
        this.symbol = symbol;
        this.companyName = companyName;
        this.latestPrice = 0;
        this.change = 0;
        this.changePercent = 0;
        this.COLOR_CODE = decideColorCode();
    }

    public Stock(String symbol, String companyName, double latestPrice, double change, double changePercent)
    {
        this.symbol = symbol;
        this.companyName = companyName;
        this.latestPrice = latestPrice;
        this.change = change;
        this.changePercent = changePercent;
        this.COLOR_CODE = decideColorCode();
    }

    String getSymbol()
    {
        return symbol;
    }

    void setSymbol(String symbol)
    {
        this.symbol = symbol;
    }

    String getCompanyName()
    {
        return companyName;
    }

    void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    double getLatestPrice()
    {
        return latestPrice;
    }

    public void setLatestPrice(double latestPrice)
    {
        this.latestPrice = latestPrice;
    }

    double getChange()
    {
        return change;
    }

    public void setChange(double change)
    {
        this.change = change;
    }

    double getChangePercent()
    {
        return changePercent;
    }

    public void setChangePercent(double changePercent)
    {
        this.changePercent = changePercent;
    }

    public int getCOLOR_CODE() {
        return COLOR_CODE;
    }

    public int decideColorCode()
    {
        if (change > 0)
            return 1;
        else if (change < 0)
            return 2;
        else
            return 3;
    }
}
