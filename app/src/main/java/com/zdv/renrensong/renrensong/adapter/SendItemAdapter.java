package com.zdv.renrensong.renrensong.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.zdv.renrensong.renrensong.R;
import com.zdv.renrensong.renrensong.bean.ISendAdapterItemClick;
import com.zdv.renrensong.renrensong.RenRenSongContentInfo;
import com.zdv.renrensong.renrensong.customView.RoundImageView;
import com.zdv.renrensong.renrensong.present.QueryPresent;
import com.zdv.renrensong.renrensong.util.Constant;
import com.zdv.renrensong.renrensong.util.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/5/17 14:28
 */
public class SendItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<RenRenSongContentInfo> items;
    QueryPresent present;
    Utils util;
    ISendAdapterItemClick listener;

    public SendItemAdapter(ArrayList<RenRenSongContentInfo> items, Context context, Fragment f) {
        this.items = items;
        this.context = context;
        util = Utils.getInstance();
        present = QueryPresent.getInstance(context);
        present.setView(f);
        listener = (ISendAdapterItemClick) f;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int VIEW_TYPE) {

        return new SendItemAdapter.MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.send_item_lay, viewGroup,
                false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder_, int i) {

        SendItemAdapter.MyViewHolder holder = (SendItemAdapter.MyViewHolder) holder_;
        RenRenSongContentInfo item = items.get(i);

        if (!item.getIsShow()) {
            holder.fetch_item_btn_lay.setVisibility(View.GONE);
            holder.fetch_item_lay_.setVisibility(View.GONE);
            return;
        }
        holder.fetch_item_type.setText(item.getItem_name());
        holder.fetch_item_discribe.setText("");
        holder.fetch_item_time.setText(item.getCreate_time() + "  " + item.getH_address());
        holder.fetch_item_weight.setText("重量:" + item.getItem_weight() + "kg");
        holder.fetch_item_order_no.setText("订单ID:" + item.getOrder_id());
        if(item.getDistance()==null)item.setDistance("0");
        String dis_km = item.getDistance() + "米";
        if (Integer.parseInt(item.getDistance()) > 1500) {
            BigDecimal b = new BigDecimal(Integer.parseInt(item.getDistance()) / 1000);
            dis_km = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue() + "公里";
        }
        holder.fetch_item_price.setText(" 距离:" + dis_km);

        RxView.clicks(holder.fetch_item_date).subscribe(s -> receive(item));
        RxView.clicks(holder.fetch_item_lay_).subscribe(s -> setVisible(holder));
        holder.fetch_item_time.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
        if (item.getIsOperating()) {
            holder.fetch_item_date.setEnabled(false);
            holder.fetch_item_date.setText("操作中");
        } else {
            holder.fetch_item_date.setEnabled(true);
            holder.fetch_item_date.setText("签收");
        }
        if (i % 2 == 0) {
            holder.fetch_item_lay_.setCardBackgroundColor(context.getResources().getColor(R.color.snow));
        } else {
            holder.fetch_item_lay_.setCardBackgroundColor(context.getResources().getColor(R.color.white));
        }
        holder.fetch_item_btn_lay.setVisibility(View.GONE);

    }

    private void setVisible(MyViewHolder holder) {

        if (holder.fetch_item_btn_lay.getVisibility() == View.VISIBLE) {
            holder.fetch_item_btn_lay.setVisibility(View.GONE);
            holder.fetch_item_time.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
        } else {
            holder.fetch_item_btn_lay.setVisibility(View.VISIBLE);
            holder.fetch_item_time.setEllipsize(TextUtils.TruncateAt.valueOf("MARQUEE"));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {


        TextView fetch_item_type, fetch_item_discribe, fetch_item_time, fetch_item_weight, fetch_item_order_no, fetch_item_price;
        RoundImageView fetch_item_icon;
        Button fetch_item_date;
        CardView fetch_item_lay_;
        CardView fetch_item_btn_lay;

        public MyViewHolder(View view) {
            super(view);
            fetch_item_type = (TextView) view.findViewById(R.id.fetch_item_type);
            fetch_item_discribe = (TextView) view.findViewById(R.id.fetch_item_discribe);
            fetch_item_time = (TextView) view.findViewById(R.id.fetch_item_time);
            fetch_item_weight = (TextView) view.findViewById(R.id.fetch_item_weight);
            fetch_item_order_no = (TextView) view.findViewById(R.id.fetch_item_order_no);
            fetch_item_price = (TextView) view.findViewById(R.id.fetch_item_price);
            fetch_item_icon = (RoundImageView) view.findViewById(R.id.fetch_item_icon);
            fetch_item_date = (Button) view.findViewById(R.id.fetch_item_date);
            fetch_item_lay_ = (CardView) view.findViewById(R.id.fetch_item_lay_);
            fetch_item_btn_lay = (CardView) view.findViewById(R.id.fetch_item_btn_lay);

            fetch_item_icon.setBorderRadius(90);

        }
    }

    private void receive(RenRenSongContentInfo item) {

        listener.onItemReceiveClick(item);
        item.setIsOperating(true);
        notifyDataSetChanged();
        present.initRetrofit(Constant.URL_RENRENSONG, false);

        present.QueryReceiveOrder(Constant.user_info.get(Constant.USER_INFO_ID), Constant.user_info.get(Constant.USER_INFO_TOKEN),
                item.getOrder_id(), item.getOrder_num(), Constant.user_info.get(Constant.USER_INFO_SHOPPER_ID));

    }

}
