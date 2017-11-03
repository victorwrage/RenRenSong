package com.zdv.renrensong.renrensong.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.zdv.renrensong.renrensong.R;
import com.zdv.renrensong.renrensong.RenRenSongContentInfo;
import com.zdv.renrensong.renrensong.bean.IAdapterItemClick;
import com.zdv.renrensong.renrensong.bean.IAdapterLoadDate;
import com.zdv.renrensong.renrensong.customView.RecyclerViewWithEmpty;
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
public class FetchItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_ITEM = 0;
    private static final int LOADING_ITEM = VIEW_ITEM + 1;
    private static final int LOADED_ITEM = LOADING_ITEM + 1;
    Context context;
    ArrayList<RenRenSongContentInfo> items;
    QueryPresent present;
    Utils util;
    IAdapterItemClick listener;
    /**
     * 加载更多数据
     */
    IAdapterLoadDate listener2;
    RecyclerViewWithEmpty recyclerViewWithEmpty;
    DisplayMetrics dm;
    private boolean hasMore = true;

    public FetchItemAdapter(ArrayList<RenRenSongContentInfo> items, Context context, Fragment f, RecyclerViewWithEmpty recyclerViewWithEmpty) {
        this.items = items;
        this.context = context;
        util = Utils.getInstance();
        present = QueryPresent.getInstance(context);
        present.setView(f);
        listener = (IAdapterItemClick) f;
        listener2 = (IAdapterLoadDate) f;
        this.recyclerViewWithEmpty = recyclerViewWithEmpty;
        dm = new DisplayMetrics();
        f.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

    }

    @Override
    public int getItemViewType(int position) {
//        KLog.v("position" + position);
        if (items.get(position) != null) {
            if (items.get(position).getIsShow() == null) {
                return LOADED_ITEM;
            } else {
                return VIEW_ITEM;
            }
        } else {
            return LOADING_ITEM;
        }

    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
//        KLog.v("viewType--" + viewType);
        if (viewType == LOADING_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.loadmore_layout, viewGroup, false);
            return new LoadingItemVH(view);
        }else if (viewType == LOADED_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.loadmore_nomore_layout, viewGroup, false);
            return new LoadingItemVH(view);
        }
        else {
            return new MyViewHolder(LayoutInflater.from(
                    context).inflate(R.layout.fetch_item_lay, viewGroup,
                    false));
        }
    }

    public void setMoreStatus(boolean hasMore_) {
        hasMore = hasMore_;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder_, int i) {

        if (holder_ instanceof LoadingItemVH) {
//            KLog.v("LoadingItemVH");
            ((LoadingItemVH) holder_).pb.setIndeterminate(true);
        } else  if (holder_ instanceof LoadedItemVH) {

        } else {
            //        KLog.v("FetchItemAdapter");

            FetchItemAdapter.MyViewHolder holder = (FetchItemAdapter.MyViewHolder) holder_;
            RenRenSongContentInfo item = items.get(i);
            if (item == null) {
                return;
            }
            if (!item.getIsShow()) {
                holder.fetch_item_btn_lay.setVisibility(View.GONE);
                holder.fetch_item_lay_.setVisibility(View.GONE);
                return;
            }
            holder.fetch_item_lay_.setVisibility(View.VISIBLE);
            holder.fetch_item_type.setText(item.getItem_name());
            holder.fetch_item_discrib.setText("预约后需15分钟内取件");
            holder.fetch_item_time.setText(item.getCreate_time() + "  " + item.getH_address());
            holder.fetch_item_weight.setText("重量:" + item.getItem_weight() + "kg");
            holder.fetch_item_order_no.setText("订单ID:" + item.getOrder_id());

            String dis_km = item.getDistance() + "米";
            if (item.getDistance() != null) {
                if (Integer.parseInt(item.getDistance()) > 1500) {
                    BigDecimal b = new BigDecimal(Integer.parseInt(item.getDistance()) / 1000);
                    dis_km = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue() + "公里";
                }
                holder.fetch_item_price.setText(" 距离:" + dis_km);
            }


            RxView.clicks(holder.fetch_item_date).subscribe(s -> dateOrder(holder, item));
            RxView.clicks(holder.fetch_item_lay_).subscribe(s -> setVisible(holder, i));
            holder.fetch_item_time.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
            if (i % 2 == 0) {
                holder.fetch_item_lay_.setCardBackgroundColor(context.getResources().getColor(R.color.snow));
            } else {
                holder.fetch_item_lay_.setCardBackgroundColor(context.getResources().getColor(R.color.white));
            }
            if (item.getIsOperating()) {
                holder.fetch_item_date.setText("预约中");
                holder.fetch_item_date.setEnabled(false);
            } else {
                holder.fetch_item_date.setEnabled(true);
                holder.fetch_item_date.setText("预约");
            }
            holder.fetch_item_btn_lay.setVisibility(View.GONE);
        }

    }


    private void setVisible(MyViewHolder holder, int position) {

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
//        KLog.v("getItemCount"+  items.size());
        return items.size();
    }


    static class LoadingItemVH extends RecyclerView.ViewHolder {

        public LoadingItemVH(View itemView) {
            super(itemView);
            pb = (ProgressBar) itemView.findViewById(R.id.pb);
        }
        private final ProgressBar pb;
    }

    static class LoadedItemVH extends RecyclerView.ViewHolder {
        public LoadedItemVH(View itemView) {
            super(itemView);
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView fetch_item_type, fetch_item_discrib, fetch_item_time, fetch_item_weight, fetch_item_order_no, fetch_item_price;
        RoundImageView fetch_item_icon;
        Button fetch_item_date;

        CardView fetch_item_lay_, fetch_item_btn_lay;

        public MyViewHolder(View view) {
            super(view);
            fetch_item_type = (TextView) view.findViewById(R.id.fetch_item_type);
            fetch_item_discrib = (TextView) view.findViewById(R.id.fetch_item_discribe);
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

    private void dateOrder(MyViewHolder holder, RenRenSongContentInfo item) {

        item.setIsOperating(true);
        notifyDataSetChanged();
        listener.onItemDateClick(item);

        present.initRetrofit(Constant.URL_RENRENSONG, false);
        present.QueryFetchOrderOrder(Constant.user_info.get(Constant.USER_INFO_ID), Constant.user_info.get(Constant.USER_INFO_TOKEN),
                item.getOrder_id(), Constant.user_info.get(Constant.USER_INFO_SHOPPER_ID));

    }


}
