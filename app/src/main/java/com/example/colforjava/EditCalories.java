package com.example.colforjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;

public class EditCalories extends AppCompatActivity {
    private Button btn_update = null;
    private TextView editText_date = null;
    private CalendarView calendarView = null;
    private EditText editText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_calories);
        btn_update =(Button)findViewById(R.id.btn_update);
        editText_date = (TextView)findViewById(R.id.expressDate);
        calendarView = (CalendarView)findViewById(R.id.calendarView);
        editText = (EditText)findViewById(R.id.editText);



        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.将日期和卡路里数据保存
                SimpleDateFormat simpleFormatter = new SimpleDateFormat("yyyy-MM-dd");
                String date =  editText_date.getText().toString();
                String Calories = editText.getText().toString();
                if(date == null ||date.equals("") ){
                    Toast.makeText(EditCalories.this,"日期尚为空，请输入",Toast.LENGTH_SHORT).show();
                    return ;
                }
                if(Calories == null || Calories.equals("") ){
                    Calories="0";
//                    Log.d(TAG, "onClick: ");
                    Toast.makeText(EditCalories.this,"卡路里尚为空,请输入",Toast.LENGTH_SHORT).show();
                    return ;
                }

                Date d =null;
                try {
                    d = simpleFormatter.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                CaloriesTimescale c = new CaloriesTimescale(d,Integer.parseInt(Calories));
                dataParse dataParse = new dataParse();
                dataParse.addItem(c);

                Gson gson = new Gson();
                dataParse.writeDataToFile(EditCalories.this,gson.toJson(dataParse.getNowCT()));
                Log.d("对象转json: ", gson.toJson(dataParse.getNowCT()));

                //2.然后back到上一个活动
//                finish();
                Intent intent = new Intent(EditCalories.this,CalendarAndCalories.class);
                startActivity(intent);
                finish();
            }

        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year,month,dayOfMonth);
                SimpleDateFormat simpleFormatter = new SimpleDateFormat("yyyy-MM-dd");
                String date = simpleFormatter.format(calendar.getTime());
//
                editText_date.setText(date);
                //显示用户选择的日期
                Toast.makeText(EditCalories.this,year + "年" + (month+1) + "月" + dayOfMonth + "日",Toast.LENGTH_SHORT).show();
            }
        });

    }




    //将更新的json写回record.json中
    public void writeDataToFile(String string){
        FileOutputStream out = null;
        BufferedWriter Writer = null;
        if(string == null){
            string ="";
        }
        try{
            Log.d("mywriter", "writeDataToFile: "+string);
            out = openFileOutput("record.json", Context.MODE_PRIVATE);
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


}