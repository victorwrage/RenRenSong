package com.zdv.renrensong.renrensong.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zdv.renrensong.renrensong.R;
import com.zdv.renrensong.renrensong.customView.RoundImageView;
import com.zdv.renrensong.renrensong.util.Utils;

import java.util.ArrayList;

/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/5/17 14:28
 */
public class CardItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<Card> items;
    Utils util;

    public CardItemAdapter(ArrayList<Card> items, Context context, Fragment f) {
        this.items = items;
        this.context = context;
        util = Utils.getInstance();


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int VIEW_TYPE) {
        return new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.card_item, viewGroup,
                false));
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder_, int i) {

        MyViewHolder holder = (MyViewHolder) holder_;
        Card item = items.get(i);
        holder.card_icon.setImageResource(item.getCard_icon());
        holder.card_num.setText(item.getCard_num());
        holder.card_type.setText(item.getCard_type());
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView card_type,card_num;
        RoundImageView card_icon;

        public MyViewHolder(View view) {
            super(view);

            card_type = (TextView) view.findViewById(R.id.card_type);
            card_num = (TextView) view.findViewById(R.id.card_num);
            card_icon = (RoundImageView) view.findViewById(R.id.card_icon);
            card_icon.setBorderRadius(90);
        }
    }

    public class Card {
        String card_type;
        String card_num;
        int card_icon;

        public String getCard_type() {
            return card_type;
        }

        public void setCard_type(String card_type) {
            this.card_type = card_type;
        }

        public String getCard_num() {
            return card_num;
        }

        public void setCard_num(String card_num) {
            this.card_num = card_num;
        }

        public int getCard_icon() {
            return card_icon;
        }

        public void setCard_icon(int card_icon) {
            this.card_icon = card_icon;
        }

        public int getCard_bg() {
            return card_bg;
        }

        public void setCard_bg(int card_bg) {
            this.card_bg = card_bg;
        }

        int card_bg;
    }
}
