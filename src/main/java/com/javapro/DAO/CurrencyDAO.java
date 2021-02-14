package com.javapro.DAO;

import com.javapro.utils.Id;

import java.lang.reflect.Field;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

public class CurrencyDAO<T> {
    private final Connection conn;
    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd");

    public CurrencyDAO(Connection conn) {
        this.conn = conn;
    }

    public void init(Class<T> cls) {
        try {
            Field[] fields = cls.getDeclaredFields();

            StringBuilder tableBld = new StringBuilder();
            for (Field f : fields) {
                tableBld.append(f.getName()).append(" ");
                if (f.getType().equals(Integer.class) || f.getType().equals(int.class)) tableBld.append("INTEGER");
                else if (f.getType().equals(String.class)) tableBld.append("VARCHAR(12)");
                else if (f.getType().equals(Date.class)) tableBld.append("DATE");
                else if (f.getType().equals(Boolean.class)) tableBld.append("BOOLEAN");
                else if (f.getType().equals(float.class)) tableBld.append("FLOAT(14,7)");
                if (f.isAnnotationPresent(Id.class)) tableBld.append(" PRIMARY KEY");
                tableBld.append(",");
            }
            tableBld.deleteCharAt(tableBld.length() - 1);

            String drop = "DROP TABLE IF EXISTS " + cls.getSimpleName();
            String sql = "CREATE TABLE " + cls.getSimpleName() + "(" + tableBld.toString() + ")";

            try (Statement st = conn.createStatement()) {
                st.execute(drop);
                st.execute(sql);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void add(T t) {
        try {
            Field[] fields = t.getClass().getDeclaredFields();
            String primaryKey = null;

            StringBuilder names = new StringBuilder();
            StringBuilder values = new StringBuilder();

            for (Field f : fields) {
                f.setAccessible(true);

                names.append(f.getName()).append(',');
                if (f.isAnnotationPresent(Id.class)) {
                    primaryKey = df.format(f.get(t));
                    values.append('"').append(primaryKey).append("\",");
                }
                else values.append('"').append(f.get(t)).append("\",");
            }
            names.deleteCharAt(names.length() - 1);
            values.deleteCharAt(values.length() - 1);

            String sql = "INSERT INTO " + t.getClass().getSimpleName() +
                    "(" + names.toString() + ") VALUES(" + values.toString() + ")";

            try (Statement st = conn.createStatement()) {
                st.execute(sql);
            } catch (SQLIntegrityConstraintViolationException sqlEx) {
                System.out.println("Uploading date " + primaryKey + " already exist in DB");
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<T> getValues(Class<T> cls, String condition, Date first, Date last, String... names) {
        List<T> res = new ArrayList<>();
        StringJoiner selectedNames = new StringJoiner(",");
        for (String name : names) selectedNames.add(name);

        try {
            try (Statement st = conn.createStatement()) {
                String sql = "SELECT " + selectedNames + " FROM " + cls.getSimpleName() +
                        " WHERE " + condition + " BETWEEN \'" + df.format(first) + "\' AND \'" + df.format(last) + "\'";
                try (ResultSet rs = st.executeQuery(sql)) {
                    ResultSetMetaData md = rs.getMetaData();

                    while (rs.next()) {
                        T t = cls.newInstance();

                        for (int i = 1; i <= md.getColumnCount(); i++) {
                            String columnName = md.getColumnName(i);

                            Field field = cls.getDeclaredField(columnName);
                            field.setAccessible(true);

                            field.set(t, rs.getObject(columnName));
                        }
                        res.add(t);
                    }
                }
            }
            return res;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
