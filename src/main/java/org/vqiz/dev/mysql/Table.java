package org.vqiz.dev.mysql;






import org.bot.vqiz.Main;

import java.math.BigInteger;
import java.util.ArrayList;

public class Table {
    public static DatabaseManager db = Main.db;
    private String tablename = "table";
    private String values = "value1 TEXT, value2 TEXT";
    String insertvalues;
    private String insertdefaultvalues = "'0','0'";

    public Table() {
    }

    public Table(String tablename) {
        this.tablename = tablename;
    }

    public Table(String tablename, String values) {
        this.tablename = tablename;
        this.values = values;
        insertvalues = values.replaceAll("BIGINT", "").replaceAll("LONGTEXT", "").replaceAll("TEXT", "").replaceAll("INT", "");
    }

    public Table(String tablename, String values, String insertdefaultvalues) {
        this.tablename = tablename;
        this.values = values;
        this.insertdefaultvalues = values;
        insertvalues = values.replaceAll("BIGINT", "").replaceAll("LONGTEXT", "").replaceAll("TEXT", "").replaceAll("INT", "");
    }

    public DatabaseManager getDB() {
        return db;
    }

    public Table run(Runnable run) {
        db.run(run);
        return this;
    }

    public Table setDB(DatabaseManager db) {
        this.db = db;
        return this;
    }

    public Table create(DatabaseManager db) {
        this.db = db;
        db.createTable(tablename, values);
        return this;
    }

    public Table create() {
        db.createTable(tablename, values);
        return this;
    }

    public Boolean dataexist(String uuidname, String uuid) {
        return db.dataexist(tablename, uuidname, uuid);
    }

    public Table insert(String values) {
        db.insert(tablename, insertvalues, values);
        return this;
    }

    public Table delete(String uuidname, String targetvalues) {
        db.delete(tablename, uuidname, targetvalues);
        return this;
    }

    public Table delete(String uuidname, String targetvalue1, String uuidname2, String targetvalue2) {
        db.delete(tablename, uuidname, targetvalue1, uuidname2, targetvalue2);
        return this;
    }


    public ArrayList<String> getStringListBySortierung(String getValue,String sortvalue,int i) {
        return db.getStringListBySortierung(tablename, getValue, sortvalue, i);
    }
    public ArrayList<String> getStringList(String getValue) {
        return db.getStringList(tablename, getValue);
    }

    public ArrayList<Integer> getIntListBySortierung(String getValue,String sortvalue,int i) {
        return db.getIntListBySortierung(tablename, getValue, sortvalue, i);
    }

    public ArrayList<Long> getLongListBySortierung(String getValue,String sortvalue,int i) {
        return db.getLongListBySortierung(tablename, getValue, sortvalue, i);
    }



    public ArrayList<String> getStringList(String getValue,String sortvalue) {
        return db.getStringList(tablename, getValue, sortvalue);
    }

    public ArrayList<Integer> getIntList(String getValue,String sortvalue) {
        return db.getIntList(tablename, getValue, sortvalue);
    }

    public ArrayList<Long> getLongList(String getValue,String sortvalue) {
        return db.getLongList(tablename, getValue, sortvalue);
    }


    public String getValues() {
        return values;
    }

    public Table setValues(String values) {
        this.values = values;
        insertvalues = values.replaceAll("BIGINT", "").replaceAll("TEXT", "").replaceAll("INT", "");
        return this;
    }

    public String getTablename() {
        return tablename;
    }

    public Table setTablename(String tablename) {
        this.tablename = tablename;
        return this;
    }

    public String getInsertdefaultvalues() {
        return insertdefaultvalues;
    }

    public Table setInsertdefaultvalues(String insertdefaultvalues) {
        this.insertdefaultvalues = insertdefaultvalues;
        return this;
    }

    public Table addInt(String uuid, String uuidname, String targetvalue, int value) {
        run(() -> setInt(uuid, uuidname, targetvalue, getInt(uuid, uuidname, targetvalue) + value));
        return this;
    }

    public Table removeInt(String uuid, String uuidname, String targetvalue, int value) {
        run(() -> setInt(uuid, uuidname, targetvalue, getInt(uuid, uuidname, targetvalue) - value));
        return this;
    }

    public Table addLong(String uuid, String uuidname, String targetvalue, Long value) {
        Long sum = getLong(uuid, uuidname, targetvalue) + value;
        run(() -> setLong(uuid, uuidname, targetvalue, sum));
        return this;
    }

    public Table removeLong(String uuid, String uuidname, String targetvalue, Long value) {
        run(() -> setLong(uuid, uuidname, targetvalue, getLong(uuid, uuidname, targetvalue) - value));
        return this;
    }

    public Table addBigInt(String uuid, String uuidname, String targetvalue, BigInteger value) {
        run(() -> setBigInt(uuid, uuidname, targetvalue, getBigInt(uuid, uuidname, targetvalue).add(value)));
        return this;
    }

    public Table setString(String uuid, String uuidname, String targetvalue, String value) {
        db.setString(tablename, uuidname, uuid, targetvalue, value);
        return this;
    }

    public Table setInt(String uuid, String uuidname, String targetvalue, int value) {
        db.setInt(tablename, uuidname, uuid, targetvalue, value);
        return this;
    }

    public Table setLong(String uuid, String uuidname, String targetvalue, long value) {
        db.setLong(tablename, uuidname, uuid, targetvalue, value);
        return this;
    }

    public Table setBigInt(String uuid, String uuidname, String targetvalue, BigInteger value) {
        db.setBigInt(tablename, uuidname, uuid, targetvalue, value);
        return this;
    }

    public String getString(String uuid, String uuidname, String value) {
        return db.getString(tablename, uuidname, uuid, value);
    }

    public int getInt(String uuid, String uuidname, String value) {
        return db.getInt(tablename, uuidname, uuid, value);
    }

    public Long getLong(String uuid, String uuidname, String value) {
        return db.getLong(tablename, uuidname, uuid, value);
    }

    public BigInteger getBigInt(String uuid, String uuidname, String value) {
        return db.getBigInt(tablename, uuidname, uuid, value);
    }




    public Table pdelete(String uuidname, String uuid) {
        db.pdelete(tablename, uuidname, uuid);
        return this;
    }

    public Table psetString(String uuid, String uuidname, String targetvalue, String value) {
        db.psetString(tablename, uuidname, uuid, targetvalue, value);
        return this;
    }

    public Table pinsert(String... values) {
        db.pinsert(tablename, insertvalues, values);
        return this;
    }

    public Boolean pdataexist(String uuidname, String uuid) {
        return db.pdataexist(tablename, uuidname, uuid);
    }

    public String pgetString(String uuid, String uuidname, String value) {
        return db.pgetString(tablename, uuidname, uuid, value);
    }

}
