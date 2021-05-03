package com.example.colforjava;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CaloriesTimescaleAdapter extends RecyclerView.Adapter<CaloriesTimescaleAdapter.ViewHolder>{

    private List<CaloriesTimescale> caloriesTimescaleList;
    private int position;
    private Context mContext;
    public int getContextMenuPosition() {
        return position;
    }
    public void setContextMenuPosition(int position) {
        this.position = position;
    }

    @NonNull
    @Override
    public CaloriesTimescaleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext!=null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.test_item,parent,false);
        final CaloriesTimescaleAdapter.ViewHolder holder = new CaloriesTimescaleAdapter.ViewHolder(view);
        //单击->返回元素值
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                String s = ""+dataParse.getNowCT().get(position).getCalories();
                Toast.makeText(v.getContext(),s,Toast.LENGTH_SHORT).show();
            }
        });

        //长按->弹出菜单
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getAdapterPosition();
                String s = ""+dataParse.getNowCT().get(position).getCalories();
                Toast.makeText(v.getContext(),s,Toast.LENGTH_SHORT).show();
                Log.d("my", "onClick: "+s);


                return false;
            }
        });


        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull CaloriesTimescaleAdapter.ViewHolder holder, int position) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        CaloriesTimescale ct = caloriesTimescaleList.get(position);
        holder.text1.setText(""+dateFormat.format(ct.getDate()));
        Log.d("holderID", "onBindViewHolder: "+ct.getDate());
        holder.text2.setText(""+ct.getCalories());
        Log.d("holderID2", "onBindViewHolder: "+ct.getCalories());
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setContextMenuPosition(holder.getLayoutPosition());
                return false;
            }
        });
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        Log.d("t.size()", "getItemCount: "+caloriesTimescaleList.size());
        return caloriesTimescaleList.size();
    }

    class ViewHolder extends  RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        //保存子项最外层布局的view
        View itemView;
        TextView text1, text2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            text1 = (TextView) itemView.findViewById(R.id.text1);
            text2 = (TextView) itemView.findViewById(R.id.text2);
            itemView.setOnCreateContextMenuListener(this);

        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("记录"+(getContextMenuPosition()+1));
            menu.add(ContextMenu.NONE, 0, ContextMenu.NONE, "查看");
            menu.add(ContextMenu.NONE, 1, ContextMenu.NONE, "删除");
            menu.add(ContextMenu.NONE, 2, ContextMenu.NONE, "修改");
        }
    }

    public CaloriesTimescaleAdapter(List<CaloriesTimescale> CT){
        caloriesTimescaleList = CT;
    }




}
