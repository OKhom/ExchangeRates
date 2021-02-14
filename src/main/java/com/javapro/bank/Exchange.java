package com.javapro.bank;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Exchange {
    private Date date;
    private String bank;
    private int baseCurrency;
    private String baseCurrencyLit;
    private List<ExchangeRate> exchangeRate;

    public Exchange() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public int getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(int baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getBaseCurrencyLit() {
        return baseCurrencyLit;
    }

    public void setBaseCurrencyLit(String baseCurrencyLit) {
        this.baseCurrencyLit = baseCurrencyLit;
    }

    public List<ExchangeRate> getExchangeRate() {
        return exchangeRate;
    }

    public ExchangeRate getExchangeRateCur(String currency) {
        for (ExchangeRate er : getExchangeRate()) {
            try {
                if (er.getCurrency().equals(currency)) return er;
            } catch (NullPointerException ignored) {}
        }
        return null;
    }

    public void setExchangeRate(ArrayList<ExchangeRate> exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    @Override
    public String toString() {
        return "Exchange{" +
                "date=" + date +
                ", bank='" + bank + '\'' +
                ", baseCurrency=" + baseCurrency +
                ", baseCurrencyLit='" + baseCurrencyLit + '\'' +
                ", exchangeRate=" + exchangeRate +
                '}';
    }
}
