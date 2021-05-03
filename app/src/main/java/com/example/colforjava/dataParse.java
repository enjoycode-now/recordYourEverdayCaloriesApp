package com.example.colforjava;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class dataParse  {

    private static List<CaloriesTimescale> nowCT = new ArrayList<>();



    //从文件record.json读取json出来
    public static List<CaloriesTimescale> readDataFromFile(Context context){
            Gson gson = new Gson();
            FileInputStream in = null;
            BufferedReader reader = null;
            StringBuilder content = new StringBuilder();
            try {
                in =context.openFileInput("record.json");
                reader =new BufferedReader(new InputStreamReader(in));
                String line ="";
                while((line=reader.readLine())!=null){
                    content.append(line);
                }

                //将json字符串解析成List<CalendarAndCalories>类型的成员变量
                nowCT = gson.fromJson(String.valueOf(content), new TypeToken<List<CaloriesTimescale>>(){}.getType());
                sort();
                reader.close();
                in.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return nowCT;


    }


    //将更新的json写回record.txt中
    public static void writeDataToFile(Context context,String string){
        FileOutputStream out = null;
        BufferedWriter Writer = null;
        if(string == null){
            string ="";
        }
        try{
            out = context.openFileOutput("record.json", Context.MODE_PRIVATE);
            Writer = new BufferedWriter(new OutputStreamWriter(out));
            Writer.write(string);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try{
                if(Writer!=null){
                    Writer.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }


    public static List<CaloriesTimescale> getNowCT() {
        return nowCT;
    }

    public void setNowCT(List<CaloriesTimescale> info) {
        this.nowCT = info;
    }

    public static void sort(){

        Collections.sort(nowCT, new Comparator<CaloriesTimescale>() {
                    public int compare(CaloriesTimescale o1, CaloriesTimescale o2) {
                        if(null == o1.getDate()) {
                            return -1;
                        }
                        if(null == o2.getDate()) {
                            return 1;
                        }
                        return o1.getDate().compareTo(o2.getDate());
                    }

                });
    }

    //增加或修改
    public void addItem(CaloriesTimescale c) {
        boolean flag = false;
        int position = 0;

        for(int i =0;i<nowCT.size();i++){
            if(nowCT.get(i).getDate().compareTo(c.getDate())==0){
                flag = true;
                position = i;
                break;
            }
        }

        if(!flag){
            nowCT.add(c);
        }else{
            nowCT.get(position).setCalories(c.getCalories());
        }

    }

    //删1 (根据日期删除元素)
    public void delItem(Date d){
        if(nowCT.size()==0||d==null){
            return;
        }
        for(int i =0;i<nowCT.size();i++){
            if(nowCT.get(i).getDate().compareTo(d)==0){
                nowCT.remove(i);
                return ;
            }
        }
    }
    //删2  (根据index删除元素)
    public static void delItem(int positon){
        if(nowCT==null || nowCT.size()<positon){
            return;
        }
        nowCT.remove(positon);
    }


    //查
    public static int checkSumOfCalories(Date start,Date end){
        if(nowCT.size()==0){
            return 0;
        }else if(start == null || end == null) {
            Log.d("my", "checkSumOfCalories: null");
            return 0;
        }else if(start.compareTo(end)>0){
            return 0;
        }
        int NumOfCalories = 0;
        for(int i =0;i<nowCT.size();i++){
            //在[start,end]这个区间
            if(nowCT.get(i).getDate().compareTo(start)>=0 && nowCT.get(i).getDate().compareTo(end)<=0){
                NumOfCalories = NumOfCalories+nowCT.get(i).getCalories();

            }
        }
        return NumOfCalories;
    }

    //改
    public void changeItem(CaloriesTimescale c){

    }

}

