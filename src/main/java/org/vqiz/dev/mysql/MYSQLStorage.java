package org.vqiz.dev.mysql;

import java.util.ArrayList;

import com.google.gson.Gson;
import org.bot.vqiz.Main;


public class MYSQLStorage {

    DatabaseManager db = Main.db;

    Table data;

    public MYSQLStorage(DatabaseManager db, String name) {
        this.db = db;
        db.Connect();
        data = new Table("Data_" + name, "UUID TEXT,DATA LONGTEXT");
        data.create(db);
    }

    public ArrayList<String> getIds() {
        return data.getStringList("UUID");
    }

    public void deleteObj(String id) {
        data.pdelete("UUID", id);
    }

    public void removeObj(String id) {
        deleteObj(id);
    }

    public void storeObj(String id, Object obj) {
        data.pinsert(id, new Gson().toJson(obj));
    }

    public void addObj(String id, Object obj) {
        storeObj(id, obj);
    }

    public void updateObj(String id, Object obj) {
        data.psetString(id, "UUID", "DATA", new Gson().toJson(obj));
    }

    public boolean existsObj(String id) {
        return hasObj(id);
    }

    public boolean hasObj(String id) {
        return data.pdataexist("UUID", id);
    }

    public MYSQLObject getObj(String id) {
        return new MYSQLObject(id, data.pgetString(id, "UUID", "DATA"));
    }


    public class MYSQLObject {

        private String id;

        private String json;

        public MYSQLObject(String id, String json) {
            this.id = id;
            this.json = json;
        }

        public Object getObject() {
            return new Gson().fromJson(json, Object.class);
        }

        public Object getString() {
            return json;
        }

        public Object getObject(Class<?> clas) {
            return new Gson().fromJson(json, clas);
        }

        public MYSQLObject save(Object obj) {
            json = new Gson().toJson(obj);
            return this;
        }

        public String getId() {
            return id;
        }

        public MYSQLObject update() {
            json = data.pgetString(id, "UUID", "DATA");
            return this;
        }

        public MYSQLObject sendupdate() {
            data.psetString(id, "UUID", "DATA", json);
            return this;
        }
    }
}