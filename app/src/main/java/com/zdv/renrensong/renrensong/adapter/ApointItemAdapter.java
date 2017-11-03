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
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.zdv.renrensong.renrensong.R;
import com.zdv.renrensong.renrensong.bean.IAppointAdapterItemClick;
import com.zdv.renrensong.renrensong.RenRenSongContentInfo;
import com.zdv.renrensong.renrensong.customView.RoundImageView;
import com.zdv.renrensong.renrensong.present.QueryPresent;
import com.zdv.renrensong.renrensong.util.Constant;
import com.zdv.renrensong.renrensong.util.Utils;

import java.util.ArrayList;

/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/5/17 14:28
 */
public class ApointItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<RenRenSongContentInfo> items;
    QueryPresent present;
    Utils util;
    IAppointAdapterItemClick listener;

    public ApointItemAdapter(ArrayList<RenRenSongContentInfo> items, Context context, Fragment f) {
        this.items = items;
        this.context = context;
        util = Utils.getInstance();
        present = QueryPresent.getInstance(context);
        present.setView(f);
        listener = (IAppointAdapterItemClick) f;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int VIEW_TYPE) {
        return new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.apoint_item_lay, viewGroup,
                false));
    }

    public void setOnClickListener(IAppointAdapterItemClick listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder_, int i) {

        MyViewHolder holder = (MyViewHolder) holder_;
        RenRenSongContentInfo item = items.get(i);

        if (!item.getIsShow()) {
            holder.fetch_item_btn_lay.setVisibility(View.GONE);
            holder.fetch_item_lay.setVisibility(View.GONE);
            return;
        }

        holder.fetch_item_type.setText(item.getItem_name());
        holder.fetch_item_discribe.setText("");
        holder.fetch_item_time.setText(item.getCreate_time() + "  " + item.getH_address());
        holder.fetch_item_weight.setText("重量:" + item.getItem_weight() + "kg");
        holder.fetch_item_order_no.setText("订单ID:" + item.getOrder_id());

      /*  String dis_kmile = item.getDistance() + "米";
        if(Integer.parseInt(item.getDistance())>1500) {
            BigDecimal b = new BigDecimal(Integer.parseInt(item.getDistance()) / 1000);
            dis_kmile = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue() + "公里";
        }
        holder.fetch_item_price.setText(" 距离:" + dis_kmile);*/

        RxView.clicks(holder.fetch_item_cancel).subscribe(s -> cancelOrder(holder, item, i));
        RxView.clicks(holder.fetch_item_fetch).subscribe(s -> fetchOrder(holder, item, i));
        RxView.clicks(holder.fetch_item_lay).subscribe(s -> setVisible(holder));
        holder.fetch_item_time.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
        if (item.getIsOperating()) {
            holder.fetch_item_cancel.setText("操作中");
            holder.fetch_item_fetch.setText("操作中");
            holder.fetch_item_fetch.setEnabled(false);
            holder.fetch_item_cancel.setEnabled(false);
        } else {
            holder.fetch_item_cancel.setText("取消预约");
            holder.fetch_item_fetch.setText("取件");
            holder.fetch_item_fetch.setEnabled(true);
            holder.fetch_item_cancel.setEnabled(true);
        }
        if (i % 2 == 0) {
            holder.fetch_item_lay.setCardBackgroundColor(context.getResources().getColor(R.color.snow));
        } else {
            holder.fetch_item_lay.setCardBackgroundColor(context.getResources().getColor(R.color.white));
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

    private void cancelOrder(MyViewHolder holder, RenRenSongContentInfo item, int position) {

        item.setIsOperating(true);
        notifyDataSetChanged();
        listener.onItemCancelClick(item);

        present.initRetrofit(Constant.URL_RENRENSONG, false);
        present.QueryFetchCancelOrder(Constant.user_info.get(Constant.USER_INFO_ID), Constant.user_info.get(Constant.USER_INFO_TOKEN),
                item.getOrder_id(), Constant.user_info.get(Constant.USER_INFO_SHOPPER_ID));

    }

    private void fetchOrder(MyViewHolder holder, RenRenSongContentInfo item, int position) {

        item.setIsOperating(true);
        notifyDataSetChanged();
        listener.onItemFetchClick(item);

        present.initRetrofit(Constant.URL_RENRENSONG, false);
        present.QueryFetchFetchOrder(Constant.user_info.get(Constant.USER_INFO_ID), Constant.user_info.get(Constant.USER_INFO_TOKEN),
                item.getOrder_id(), item.getOrder_num(), Constant.user_info.get(Constant.USER_INFO_SHOPPER_ID));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView fetch_item_type, fetch_item_discribe, fetch_item_time, fetch_item_weight, fetch_item_order_no, fetch_item_price;
        RoundImageView fetch_item_icon;
        Button fetch_item_fetch, fetch_item_cancel;
        CardView fetch_item_lay, fetch_item_btn_lay;

        public MyViewHolder(View view) {
            super(view);
            fetch_item_type = (TextView) view.findViewById(R.id.fetch_item_type);
            fetch_item_discribe = (TextView) view.findViewById(R.id.fetch_item_discribe);
            fetch_item_time = (TextView) view.findViewById(R.id.fetch_item_time);
            fetch_item_weight = (TextView) view.findViewById(R.id.fetch_item_weight);
            fetch_item_order_no = (TextView) view.findViewById(R.id.fetch_item_order_no);
            fetch_item_price = (TextView) view.findViewById(R.id.fetch_item_price);
            fetch_item_icon = (RoundImageView) view.findViewById(R.id.fetch_item_icon);
            fetch_item_cancel = (Button) view.findViewById(R.id.fetch_item_cancel);
            fetch_item_fetch = (Button) view.findViewById(R.id.fetch_item_fetch);
            fetch_item_lay = (CardView) view.findViewById(R.id.fetch_item_lay_);
            fetch_item_btn_lay = (CardView) view.findViewById(R.id.fetch_item_btn_lay);
            fetch_item_icon.setBorderRadius(90);

        }
    }

    class MyEmptyViewHolder extends RecyclerView.ViewHolder {
        TextView empty_tv;
        ImageView empty_iv;

        public MyEmptyViewHolder(View view) {
            super(view);
            empty_tv = (TextView) view.findViewById(R.id.empty_tv);
            empty_iv = (ImageView) view.findViewById(R.id.empty_iv);
        }
    }
}
