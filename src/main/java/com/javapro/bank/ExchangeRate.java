package com.javapro.bank;

public class ExchangeRate {
    private String baseCurrency;
    private String currency;
    private float saleRate;
    private float purchaseRate;
    private float saleRateNB;
    private float purchaseRateNB;

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public float getSaleRate() {
        return saleRate;
    }

    public void setSaleRate(float saleRate) {
        this.saleRate = saleRate;
    }

    public float getPurchaseRate() {
        return purchaseRate;
    }

    public void setPurchaseRate(float purchaseRate) {
        this.purchaseRate = purchaseRate;
    }

    public float getSaleRateNB() {
        return saleRateNB;
    }

    public void setSaleRateNB(float saleRateNB) {
        this.saleRateNB = saleRateNB;
    }

    public float getPurchaseRateNB() {
        return purchaseRateNB;
    }

    public void setPurchaseRateNB(float purchaseRateNB) {
        this.purchaseRateNB = purchaseRateNB;
    }

    @Override
    public String toString() {
        return "ExchangeRate{" +
                "baseCurrency='" + baseCurrency + '\'' +
                ", currency='" + currency + '\'' +
                ", saleRate=" + saleRate +
                ", purchaseRate=" + purchaseRate +
                ", saleRateNB=" + saleRateNB +
                ", purchaseRateNB=" + purchaseRateNB +
                '}';
    }
}
