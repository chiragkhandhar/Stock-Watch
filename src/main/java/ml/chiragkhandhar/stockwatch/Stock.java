package ml.chiragkhandhar.stockwatch;

import java.io.Serializable;

public class Stock implements Serializable
{
    private String symbol, companyName;
    private double latestPrice, change, changePercent;

    public Stock()
    {
        this.symbol = "";
        this.companyName = "";
        this.latestPrice = 0;
        this.change = 0;
        this.changePercent = 0;
    }

    public Stock(String symbol, String companyName)
    {
        this.symbol = symbol;
        this.companyName = companyName;
    }

    public Stock(String symbol, String companyName, float latestPrice, double change, double changePercent)
    {
        this.symbol = symbol;
        this.companyName = companyName;
        this.latestPrice = latestPrice;
        this.change = change;
        this.changePercent = changePercent;
    }

    public String getSymbol()
    {
        return symbol;
    }

    public void setSymbol(String symbol)
    {
        this.symbol = symbol;
    }

    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    public double getLatestPrice()
    {
        return latestPrice;
    }

    public void setLatestPrice(double latestPrice)
    {
        this.latestPrice = latestPrice;
    }

    public double getChange()
    {
        return change;
    }

    public void setChange(double change)
    {
        this.change = change;
    }

    public double getChangePercent()
    {
        return changePercent;
    }

    public void setChangePercent(double changePercent)
    {
        this.changePercent = changePercent;
    }
}
