package com.example.colforjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private Button btn_edit = null;//“编辑”按钮
    private Button btn_begin = null;//“输入开始日期”按钮
    private Button btn_end = null;//“输入结束日期”按钮
    private CalendarView calendarView2 = null;
    private TextView date_start =null;
    private TextView date_end = null;
    private Button btn_query = null;//“查询”按钮
    private TextView resultSum = null;//“查询结果”
    private GraphView graphView = null;//图表



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btn_edit = (Button) findViewById(R.id.btn_edit);
        btn_begin =(Button)findViewById(R.id.btn_begin);
        btn_end =(Button)findViewById(R.id.btn_end);
        date_start = findViewById(R.id.date_start);
        date_end = findViewById(R.id.date_end);
        btn_query = (Button)findViewById(R.id.btn_check);
        resultSum =(TextView)findViewById(R.id.resultSum);
        graphView = (GraphView)findViewById(R.id.graph);

        //先将文件存储的内容读出来
        dataParse.readDataFromFile(MainActivity.this);


        btn_begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //初始化布局activity_popupWindow.xml
                LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
                View contentView = layoutInflater.inflate(R.layout.calendar, null);
                //对布局里面的控件进行初始化并进行相应的操作
                //初始化PopupWindow
                PopupWindow popupWindow = new PopupWindow(contentView, WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT, true);
                popupWindow.setTouchable(true);
                popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                });

                popupWindow.showAsDropDown(v);
                calendarView2 = contentView.findViewById(R.id.calendarView2);
                calendarView2.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView calendarView2, int year, int month, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year,month,dayOfMonth);
                        SimpleDateFormat simpleFormatter = new SimpleDateFormat("yyyy-MM-dd");
                        String date = simpleFormatter.format(calendar.getTime());
                        date_start.setText(date);
                        resultSum.setText("");
                        //显示用户选择的日期
                        Toast.makeText(MainActivity.this,year + "年" + (month+1) + "月" + dayOfMonth + "日",Toast.LENGTH_SHORT).show();
                    }
                });
                String text  = btn_begin.getText().toString();
                if(text.equals("click")){
                    btn_begin.setText("取消选择");
                }else{
                    btn_begin.setText("click");
                }

            }
        });

        btn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text  = btn_end.getText().toString();
                if(text.equals("click")){
                    btn_end.setText("取消选择");
                }else{
                    btn_end.setText("click");
                }

                //初始化布局activity_popupWindow.xml
                LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
                View contentView = layoutInflater.inflate(R.layout.calendar, null);
                //对布局里面的控件进行初始化并进行相应的操作
                //初始化PopupWindow
                PopupWindow popupWindow = new PopupWindow(contentView, WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT, true);
                popupWindow.setTouchable(true);
                popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                });


                popupWindow.showAsDropDown(v);
                calendarView2 = contentView.findViewById(R.id.calendarView2);
                calendarView2.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView calendarView2, int year, int month, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year,month,dayOfMonth);
                        SimpleDateFormat simpleFormatter = new SimpleDateFormat("yyyy-MM-dd");
                        String date = simpleFormatter.format(calendar.getTime());
//
                        date_end.setText(date);
                        resultSum.setText("");
                        //显示用户选择的日期
                        Toast.makeText(MainActivity.this,year + "年" + (month+1) + "月" + dayOfMonth + "日",Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });



        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,CalendarAndCalories.class);
                startActivity(intent);

            }
        });

        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String begin = date_start.getText().toString();
                Log.d("my", begin);
                String end = date_end.getText().toString();
                Log.d("my", end);
                if(begin == null || begin.equals("")){
                    Toast.makeText(MainActivity.this,"开始日期还没选择",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(end == null || end.equals("")){
                    Toast.makeText(MainActivity.this,"开始日期还没选择",Toast.LENGTH_SHORT).show();
                    return;
                }
                Date be=null ;
                Date en=null;
                int sum =0;
                try {
                    be = format.parse(begin);
                    en = format.parse(end);
                    sum = dataParse.checkSumOfCalories(be,en);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                resultSum.setText("你共计消耗"+sum+"卡路里");


            }
        });



        int size = dataParse.getNowCT().size();
        dataParse.sort();
        for(int i=0;i<size;i++){
            BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                    new DataPoint(dataParse.getNowCT().get(i).getDate(),dataParse.getNowCT().get(i).getCalories()),
            });
//            series.setTitle(""+dataParse.getNowCT().get(i).getCalories());
            series.setDrawValuesOnTop(true);
//            series.setDrawDataPoints(true);
//            series.setDataPointsRadius(10);
//            series.setThickness(8);

            graphView.addSeries(series);
        }
        // set date label formatter
        graphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(MainActivity.this));
        //最多同时显示5个，太多会重叠
        graphView.getGridLabelRenderer().setNumHorizontalLabels(4);

        // set manual x bounds to have nice steps
//        graphView.getViewport().setMinX(dataParse.getNowCT().get(0).getDate().getTime());
//        graphView.getViewport().setMaxX(dataParse.getNowCT().get(size-1).getDate().getTime());

//        graphView.getViewport().setMinY(0);
//        graphView.getViewport().setMaxY(5000);
//        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setXAxisBoundsManual(true);

        //水平滚动
        graphView.getViewport().setScrollable(true);
//        graphView.getViewport().setScrollableY(true);


        // as we use dates as labels, the human rounding to nice readable numbers is not necessary
        graphView.getGridLabelRenderer().setHumanRounding(false);


    }



}