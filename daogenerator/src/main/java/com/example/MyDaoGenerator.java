package com.example;


import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;


public class MyDaoGenerator {

    private static final String PATH = "com.example.user.task_4.database";
    private static final int VERSION = 1;

    public static void main(String[] args) {
        Schema schema = new Schema(VERSION, PATH);

        Entity item = schema.addEntity("RssItem");

        item.addIdProperty().autoincrement();
        item.addStringProperty("title");
        item.addStringProperty("link");

        try {
            new DaoGenerator().generateAll(schema, "./app/src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
