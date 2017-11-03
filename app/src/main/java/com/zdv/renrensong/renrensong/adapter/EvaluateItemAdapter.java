package com.zdv.renrensong.renrensong.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.zdv.renrensong.renrensong.R;
import com.zdv.renrensong.renrensong.RenRenSongEvaluateInfo;
import com.zdv.renrensong.renrensong.customView.EllipsizeTextView;
import com.zdv.renrensong.renrensong.customView.RecyclerViewWithEmpty;
import com.zdv.renrensong.renrensong.present.DbPresent;
import com.zdv.renrensong.renrensong.present.QueryPresent;
import com.zdv.renrensong.renrensong.util.Utils;

import java.util.ArrayList;

/**
 * Info: 评价
 * Created by xiaoyl
 * 创建时间:2017/5/17 14:28
 */
public class EvaluateItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<RenRenSongEvaluateInfo> items;
    QueryPresent present;
    Utils util;
    DbPresent dbPresent;
    int[] color_arr = new int[]{R.color.word1,R.color.golden,R.color.darksalmon,R.color.rosybrown,R.color.skyblue,R.color.mediumorchid
            ,R.color.lightcoral};
    RecyclerViewWithEmpty recyclerViewWithEmpty;


    public EvaluateItemAdapter(ArrayList<RenRenSongEvaluateInfo> items, Context context, Fragment f, RecyclerViewWithEmpty recyclerViewWithEmpty) {
        this.items = items;
        this.context = context;
        util = Utils.getInstance();
        present = QueryPresent.getInstance(context);
        present.setView(f);
        dbPresent = DbPresent.getInstance(context);
        dbPresent.setView(f);
        this.recyclerViewWithEmpty = recyclerViewWithEmpty;

    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int VIEW_TYPE) {

        return new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.evaluate_item_lay, viewGroup,
                false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder_, int i) {
        EvaluateItemAdapter.MyViewHolder holder = (EvaluateItemAdapter.MyViewHolder) holder_;
        RenRenSongEvaluateInfo item = items.get(i);
        holder.eva_item_content.setText(item.getRating_content());
        holder.eva_item_order_id.setText(item.getOrder_id());
        holder.eva_item_time.setText(item.getR_time());
        holder.eva_item_person.setText(item.getRating_person());

        holder.eva_item_star.setRating(Float.parseFloat(item.getLevel()));
        holder.eva_item_unread.setVisibility((item.getIs_read())?View.INVISIBLE : View.VISIBLE);
        holder.eva_item_keyword.removeAllViews();
        int c = 0;
        for (String key_w : item.getKey_word().split(",")) {

            TextView tv = new TextView(context);
            tv.setText(key_w);
            tv.setTextSize(12);

            tv.setTextColor(context.getResources().getColor(R.color.white));
            tv.setBackgroundColor(context.getResources().getColor(color_arr[c]));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 5, 10, 5);
            holder.eva_item_keyword.addView(tv, params);
            c++;
        }

        RxView.clicks(holder.eva_item_lay_).subscribe(s -> setRead(holder, i));
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView eva_item_order_id, eva_item_person, eva_item_time;
        EllipsizeTextView eva_item_content;
        CardView eva_item_lay_;
        RatingBar eva_item_star;
        LinearLayout eva_item_keyword;
        ImageView eva_item_unread;

        public MyViewHolder(View view) {
            super(view);
            eva_item_order_id = (TextView) view.findViewById(R.id.eva_item_order_id);
            eva_item_person = (TextView) view.findViewById(R.id.eva_item_person);
            eva_item_content = (EllipsizeTextView) view.findViewById(R.id.eva_item_content);
            eva_item_time = (TextView) view.findViewById(R.id.eva_item_time);
            eva_item_star = (RatingBar) view.findViewById(R.id.eva_item_star);
            eva_item_lay_ = (CardView) view.findViewById(R.id.eva_item_lay_);
            eva_item_unread = (ImageView) view.findViewById(R.id.eva_item_unread);
            eva_item_keyword = (LinearLayout) view.findViewById(R.id.eva_item_keyword);
            eva_item_content.setEllipsize(TextUtils.TruncateAt.valueOf("MARQUEE"));
        }
    }

    private void setRead(MyViewHolder holder, int position) {
        RenRenSongEvaluateInfo item =  items.get(position);
        item.setIs_read(true);
        dbPresent.InsertReplaceEvaluate(item);
    }

}
