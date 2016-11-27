package com.android.learnbymatching.database;

/**
 * Created by Lenovo on 27.11.2016.
 *
 */

public class Project {

    private int id;
    private String name;
    private String create_date;

    public Project() { }

    public Project(String name) {
        this.name = name;
    }

    public Project(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }
}
