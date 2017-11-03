package com.zdv.renrensong.renrensong.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.socks.library.KLog;
import com.zdv.renrensong.renrensong.R;
import com.zdv.renrensong.renrensong.RenRenSongEvaluateInfo;
import com.zdv.renrensong.renrensong.adapter.EvaluateItemAdapter;
import com.zdv.renrensong.renrensong.bean.RenRenSongEvaluateResponse;
import com.zdv.renrensong.renrensong.customView.RecyclerViewWithEmpty;
import com.zdv.renrensong.renrensong.present.DbPresent;
import com.zdv.renrensong.renrensong.present.QueryPresent;
import com.zdv.renrensong.renrensong.util.Constant;
import com.zdv.renrensong.renrensong.util.Utils;
import com.zdv.renrensong.renrensong.util.VToast;
import com.zdv.renrensong.renrensong.view.IDBEvaluateView;
import com.zdv.renrensong.renrensong.view.IDataEvaluateView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.gmariotti.recyclerview.adapter.AlphaAnimatorAdapter;

public class FragmentEvaluate extends BaseFragment implements IDataEvaluateView, IDBEvaluateView {
    private static final String COOKIE_KEY = "cookie";
    IEvaluateListener listener;
    @Bind(R.id.header_btn)
    ImageView header_btn;
    @Bind(R.id.header_title)
    TextView header_title;

	/*@Bind(R.id.spinner_scope)
    CustomSinnper spinner_scope;
	@Bind(R.id.spinner_order)
	CustomSinnper spinner_order;*/

    @Bind(R.id.recycle_view)
    RecyclerViewWithEmpty recycle_view;

    @Bind(R.id.header_count)
    TextView header_count;
    @Bind(R.id.empty_iv)
    ImageView empty_iv;
    @Bind(R.id.empty_tv)
    TextView empty_tv;

    @Bind(R.id.empty_lay)
    RelativeLayout empty_lay;

    ArrayList<RenRenSongEvaluateInfo> data;
    QueryPresent present;
    DbPresent dbPresent;
    Utils util;
    boolean isInit;

    @Override
    public void onAttach(Context context) {
        try {
            listener = (IEvaluateListener) context;
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.evaluate_lay, container, false);
        ButterKnife.bind(FragmentEvaluate.this, view);

        data = new ArrayList<>();
        adapter = new EvaluateItemAdapter(data, getActivity(), FragmentEvaluate.this, recycle_view);
        empty_iv = (ImageView) view.findViewById(R.id.empty_iv);
        empty_tv = (TextView) view.findViewById(R.id.empty_tv);
        empty_lay = (RelativeLayout) view.findViewById(R.id.empty_lay);

		/*spinner_scope = (CustomSinnper) view.findViewById(R.id.spinner_scope);
		spinner_order = (CustomSinnper) view.findViewById(R.id.spinner_order);
		ChoiceSpinnerAdapter adapter_scope = new ChoiceSpinnerAdapter(getActivity(),scopes);
		spinner_scope.setAdapter(adapter_scope);
		spinner_scope.setOnItemSeletedListener((parent,v, position,  id)->changeScope(position));

		ChoiceSpinnerAdapter adapter_order = new ChoiceSpinnerAdapter(getActivity(),orders);
		spinner_order.setAdapter(adapter_order);
		spinner_order.setOnItemSeletedListener((parent,v, position,  id)->changeOrder(position));*/

        recycle_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        //	recycle_view.setItemAnimator(new SlideInOutLeftItemAnimator(recycle_view));
        AlphaAnimatorAdapter animatorApdapter = new AlphaAnimatorAdapter(adapter, recycle_view);
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        recycle_view.setEmptyView(empty_lay);
        recycle_view.setAdapter(animatorApdapter);

        util = Utils.getInstance();
        present = QueryPresent.getInstance(getContext());
        present.setView(FragmentEvaluate.this);

        dbPresent = DbPresent.getInstance(getContext());
        dbPresent.setView(FragmentEvaluate.this);

        RxView.clicks(header_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> listener.finishEvaluate());
        header_title.setText("我的评价");
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //showWaitDialog("获取评价中");
        initDBDate();

    }

    private void initDBDate() {
        dbPresent.QueryEvaluate();
    }

    private void fetchFromNetWork() {
        present.initRetrofit(Constant.URL_RENRENSONG, false);
        present.QueryEvaluate(Constant.user_info.get(Constant.USER_INFO_ID), Constant.user_info.get(Constant.USER_INFO_TOKEN),
                Constant.user_info.get(Constant.USER_INFO_SHOPPER_ID));
    }

    @Override
    public void ResolveEvaluateDateInfo(RenRenSongEvaluateResponse info) {
        KLog.v(info.toString());
        hideWaitDialog();
        if (info.getStatus() == null) {
            //setEmptyStatus(true);
            VToast.toast(getContext(), "网络错误,缓存未刷新");
            return;
        }
        if (info.getContent() == null || info.getContent().size() == 0) {
            KLog.v(info.toString());
            setEmptyStatus(false);
            header_count.setText("0个评价");
            data.clear();
            adapter.notifyDataSetChanged();
            return;
        }
        Iterator<RenRenSongEvaluateInfo> i_t = info.getContent().iterator();
        while (i_t.hasNext()) {
            RenRenSongEvaluateInfo item = i_t.next();
            boolean isContain = false;
            for (RenRenSongEvaluateInfo tmp_item : data) {
                if (tmp_item.getOrder_id().equals(item.getOrder_id())) {
                    isContain = true;
                }
            }
            if (!isContain) {
                item.setIs_read(false);
                item.setEvaluate_owner(Constant.user_info.get(Constant.USER_INFO_ID));
                dbPresent.InsertReplaceEvaluate(item);
                data.add(item);
            }
        }
        ArrayList<RenRenSongEvaluateInfo> cache_delete = new ArrayList<>();
        for (RenRenSongEvaluateInfo c_d : data) {
            if (!c_d.getEvaluate_owner().equals(Constant.user_info.get(Constant.USER_INFO_ID))) {
                cache_delete.add(c_d);
            }
        }
        for (RenRenSongEvaluateInfo c_d : cache_delete) {
            data.remove(c_d);
            dbPresent.DeleteEvaluate(c_d);
        }
        adapter.notifyDataSetChanged();

        //	SortEvaluateComparator comparator = new SortEvaluateComparator(0,0);//默认升序
        //Collections.sort(data,comparator);

        isInit = true;
        header_count.setText(data.size() + "个评价");
    }

    protected void setEmptyStatus(boolean isOffLine) {
        if (isOffLine) {
            empty_iv.setImageResource(R.drawable.netword_error);
            empty_tv.setText("(=^_^=)，粗错了，点我刷新试试~");
            empty_lay.setEnabled(true);
            RxView.clicks(empty_iv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> emptyClick());
        } else {
            empty_lay.setEnabled(false);
            empty_iv.setImageResource(R.drawable.null_ico);
            empty_tv.setText("怎么还没有评价捏？");
        }
    }

    protected void emptyClick() {
        showWaitDialog("正在努力加载...");
        fetchFromNetWork();
    }

    @Override
    public void ResolveDBEvaluateData(List<RenRenSongEvaluateInfo> info) {
        hideWaitDialog();
        KLog.v("缓存数量" + info.size());
        Iterator<RenRenSongEvaluateInfo> i_t = info.iterator();
        while (i_t.hasNext()) {
            RenRenSongEvaluateInfo item = i_t.next();
        //    KLog.v(item.getIs_read() + "");
            data.add(item);
        }
        adapter.notifyDataSetChanged();
        header_count.setText(data.size() + "个评价");
        fetchFromNetWork();
    }

    @Override
    public void ResolveDBEvaluateInsertData(boolean isInsert) {
    //    KLog.v("isInsert" + isInsert);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void ResolveDBEvaluateDeleteData(boolean isDelete) {
    //    KLog.v("isDelete" + isDelete);
    }

    public interface IEvaluateListener {
        void finishEvaluate();
    }
}
