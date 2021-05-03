package com.example.colforjava;

import java.util.Date;

public class CaloriesTimescale {
    private Date date;
    private int Calories;

    public CaloriesTimescale(Date date, int Calories ){
        this.date = date;
        this.Calories = Calories;
    }



    public void setCalories(int calories) {
        this.Calories = calories;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public int getCalories() {
        return Calories;
    }
}
