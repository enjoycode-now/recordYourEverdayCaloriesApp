package com.example.colforjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CalendarAndCalories extends AppCompatActivity {
    private Button btn_add = null;//“添加数据”按钮
    //测试
    private List<CaloriesTimescale> ct = new ArrayList<>();
    private RecyclerView recyclerView = null;
    CaloriesTimescaleAdapter testD =null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_and_calories);

        //读取文件获得卡路里数据

//        ct = dataParse.readDataFromFile(CalendarAndCalories.this);
        dataParse.readDataFromFile(CalendarAndCalories.this);
        Log.d("mywriter", "onCreate: "+dataParse.getNowCT());

        btn_add =(Button)findViewById(R.id.btn_add);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CalendarAndCalories.this);
        recyclerView.setLayoutManager(linearLayoutManager);
//        CaloriesTimescaleAdapter testD = new CaloriesTimescaleAdapter(ct);
        testD = new CaloriesTimescaleAdapter(dataParse.getNowCT());
        recyclerView.setAdapter(testD);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarAndCalories.this,EditCalories.class);
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        int position;
        position =testD.getContextMenuPosition();
        switch (item.getItemId()) {
            case 0://查看
            {
                int calories = dataParse.getNowCT().get(position).getCalories();
                if(calories>3000){
                    Toast.makeText(CalendarAndCalories.this, "你摄入了"+calories+"卡路里,数值偏高，请注意节制，保持合理膳食哦！", Toast.LENGTH_LONG).show();
                }else if(calories>1500){
                    Toast.makeText(CalendarAndCalories.this, "你摄入了"+calories+"卡路里,数值居中，请继续保持哦，每天好身材！", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(CalendarAndCalories.this, "你摄入了"+calories+"卡路里,数值偏低，请务必吃多一点，减肥不是节食，而是运动健身哦！", Toast.LENGTH_LONG).show();
                }

            }

                break;
            case 1://删除
                dataParse.delItem(position);
                Gson gson = new Gson();
                dataParse.writeDataToFile(CalendarAndCalories.this,gson.toJson(dataParse.getNowCT()));
                finish();
                Intent intent = new Intent(CalendarAndCalories.this,CalendarAndCalories.class);
                startActivity(intent);
                Toast.makeText(CalendarAndCalories.this, item.getTitle()+""+ position, Toast.LENGTH_SHORT).show();

                break;
            case 2://修改

                Toast.makeText(CalendarAndCalories.this, "暂时还不支持，抱歉", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onContextItemSelected(item);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        Gson gson = new Gson();
//        String s = gson.toJson(ct);
        String s = gson.toJson(dataParse.getNowCT());
        dataParse.writeDataToFile(CalendarAndCalories.this,s);
    }



}