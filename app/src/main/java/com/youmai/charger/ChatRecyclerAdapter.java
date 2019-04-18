package com.youmai.charger;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ChatRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<String> mList;

    public ChatRecyclerAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<>();
    }

    public void setDataList(List<String> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void setItem(String item) {
        mList.add(item);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.hx_text_left, parent, false);
        RecyclerView.ViewHolder holder = new TextViewHolder(view);

        return holder;

    }

    // 数据绑定
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        String item = mList.get(position);

        if (viewHolder instanceof TextViewHolder) {
            TextView textView = ((TextViewHolder) viewHolder).textView;
            textView.setText(item);
        }

    }

    class TextViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        TextViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_content);
        }
    }


}

