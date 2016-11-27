package com.android.learnbymatching.database;

/**
 * Created by Lenovo on 27.11.2016.
 *
 */

public class Matchs {

    private int id;
    private String first;
    private String second;
    private String create_date;

    public Matchs() { }

    public Matchs(String first, String second) {
        this.first = first;
        this.second = second;
    }

    public Matchs(int id, String first, String second) {
        this.id = id;
        this.first = first;
        this.second = second;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }
}
