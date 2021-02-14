package com.javapro;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.javapro.DAO.Currency;
import com.javapro.DAO.CurrencyDAO;
import com.javapro.bank.Exchange;
import com.javapro.utils.DbProperties;
import com.javapro.utils.Server;

import java.io.IOException;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Exchange Rates
 *
 */
public class App {
    private static final SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
    private static final Scanner sc = new Scanner(System.in);
    private static final Gson gson = new GsonBuilder().setDateFormat("dd.MM.yyyy").create();

    public static void main( String[] args ) throws SQLException, IOException {
        DbProperties db = new DbProperties();

        try (Connection conn = DriverManager.getConnection(db.getUrl(), db.getUser(), db.getPassword())) {
            CurrencyDAO<Currency> dao = new CurrencyDAO<>(conn);

            while (true) {
                System.out.println("\nSelect your action:");
                System.out.println("\t1. show currency statistic");
                System.out.println("\t2. load new data");
                System.out.println("\t3. init DB and load new data");
                System.out.println("\t0. exit");
                System.out.print(" -> ");
                String input = sc.nextLine();

                switch (input) {
                    case "1" -> showStatistic(dao);
                    case "2" -> loadDB(dao, "USD");
                    case "3" -> {
                        dao.init(Currency.class);
                        loadDB(dao, "USD");
                    }
                    case "0" -> {
                        return;
                    }
                    default -> System.out.println("Entered value is invalid. Try again...");
                }
            }
        }
    }

    private static void loadDB(CurrencyDAO<Currency> dao, String currencyName) throws IOException {
        Date dateStart, dateFinish;
        try {
            System.out.println("Enter first date (in format DD.MM.YYYY): ");
            String date1 = sc.nextLine();
            dateStart = df.parse(date1);
            System.out.println("Enter last date (in format DD.MM.YYYY): ");
            String date2 = sc.nextLine();
            dateFinish = df.parse(date2);
        } catch (ParseException e) {
            System.out.println("Date format is incorrect. Try again...");
            return;
        }

        for (int i = 0; i < daysBetween(dateStart, dateFinish); i++) {
            System.out.println("Uploading " + df.format(dateIncrement(dateStart, i)));
            String json = Server.request(df.format(dateIncrement(dateStart, i)));
            Exchange exchange = gson.fromJson(json, Exchange.class);
            Currency currency = new Currency(exchange.getDate(),
                    exchange.getBank(),
                    exchange.getBaseCurrency(),
                    exchange.getBaseCurrencyLit(),
                    exchange.getExchangeRateCur(currencyName).getCurrency(),
                    exchange.getExchangeRateCur(currencyName).getSaleRateNB(),
                    exchange.getExchangeRateCur(currencyName).getPurchaseRateNB(),
                    exchange.getExchangeRateCur(currencyName).getSaleRate(),
                    exchange.getExchangeRateCur(currencyName).getPurchaseRate()
            );
            dao.add(currency);
        }
    }

    private static void showStatistic(CurrencyDAO<Currency> dao) {
        Date dateStart, dateFinish;
        try {
            System.out.println("Enter first date (in format DD.MM.YYYY): ");
            String date1 = sc.nextLine();
            dateStart = df.parse(date1);
            System.out.println("Enter last date (in format DD.MM.YYYY): ");
            String date2 = sc.nextLine();
            dateFinish = df.parse(date2);
        } catch (ParseException e) {
            System.out.println("Date format is incorrect. Try again...");
            return;
        }
        List<Currency> list = dao.getValues(Currency.class, "date", dateStart, dateFinish,
                "saleRateNB", "saleRate", "purchaseRate");
        printTable(list);
    }

    private static void printTable(List<Currency> list) {
        float maxSaleRateNB = (float) list.stream().mapToDouble(Currency::getSaleRateNB).max().getAsDouble();
        float minSaleRateNB = (float) list.stream().mapToDouble(Currency::getSaleRateNB).min().getAsDouble();
        float avgSaleRateNB = (float) list.stream().mapToDouble(Currency::getSaleRateNB).average().getAsDouble();
        float maxSaleRate = (float) list.stream().mapToDouble(Currency::getSaleRate).max().getAsDouble();
        float minSaleRate = (float) list.stream().mapToDouble(Currency::getSaleRate).min().getAsDouble();
        float avgSaleRate = (float) list.stream().mapToDouble(Currency::getSaleRate).average().getAsDouble();
        float maxPurchaseRate = (float) list.stream().mapToDouble(Currency::getPurchaseRate).max().getAsDouble();
        float minPurchaseRate = (float) list.stream().mapToDouble(Currency::getPurchaseRate).min().getAsDouble();
        float avgPurchaseRate = (float) list.stream().mapToDouble(Currency::getPurchaseRate).average().getAsDouble();
        System.out.printf("%n%-10s%12s%12s%12s%n", "PRIVATBANK", "MIN  ", "MAX  ", "AVERAGE");
        System.out.println("----------------------------------------------");
        System.out.printf("%-10s%12.4f%12.4f%12.4f%n", "nbu rate", minSaleRateNB, maxSaleRateNB, avgSaleRateNB);
        System.out.printf("%-10s%12.4f%12.4f%12.4f%n", "sale", minSaleRate, maxSaleRate, avgSaleRate);
        System.out.printf("%-10s%12.4f%12.4f%12.4f%n", "purchase", minPurchaseRate, maxPurchaseRate, avgPurchaseRate);
    }

    private static int daysBetween(Date start, Date finish) {
        return (int) ((finish.getTime() - start.getTime())/(24 * 60 * 60 * 1000) + 1);
    }

    private static Date dateIncrement(Date date, int increment) {
        return new Date(date.getTime() + ((long) increment * 24 * 60 * 60 * 1000));
    }
}
