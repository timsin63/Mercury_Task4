package com.example.user.task_4;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.task_4.database.RssItem;

import java.util.List;

import static com.example.user.task_4.MainActivity.selectedPosition;

/**
 * Created by User on 11/23/2016.
 */

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.MyViewHolder> {

    public static final String EXTRA_URI_TAG = "uri_tag";

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView itemTitle;


        public MyViewHolder(View itemView) {
            super(itemView);
            itemTitle = (TextView) itemView.findViewById(R.id.item_title);

        }
    }

    List<RssItem> items;

    public NewsListAdapter(List<RssItem> items) {
        this.items = items;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.i_news_list, parent, false);

        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.itemTitle.setText(items.get(position).getTitle());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedPosition != position) {
                    Intent intent = new Intent(MainActivity.SHOW_ARTICLE);

                    int oldSelectedPostion = selectedPosition;
                    selectedPosition = position;

                    notifyItemChanged(oldSelectedPostion);
                    view.setBackgroundColor(Color.BLUE);

                    intent.putExtra(EXTRA_URI_TAG, items.get(position).getLink());
                    view.getContext().sendBroadcast(intent);
                }
            }
        });

        if (holder.itemView.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            holder.itemView.setBackgroundColor(selectedPosition == position ? Color.BLUE : Color.WHITE);
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
        }
    }


    //TODO не пересоздавать фрагмент при перевороте

    @Override
    public int getItemCount() {
        return items.size();
    }
}
