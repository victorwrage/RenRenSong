package com.zdv.renrensong.renrensong.fragment;


import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.socks.library.KLog;
import com.zdv.renrensong.renrensong.R;
import com.zdv.renrensong.renrensong.RenRenSongContentInfo;
import com.zdv.renrensong.renrensong.bean.RenRenSongContentResponse;
import com.zdv.renrensong.renrensong.customView.ProgressBarItem;
import com.zdv.renrensong.renrensong.present.DbPresent;
import com.zdv.renrensong.renrensong.util.Constant;
import com.zdv.renrensong.renrensong.util.SortComparator;

import java.util.ArrayList;
import java.util.Collections;

public class BaseFragment extends Fragment {
    protected final int REFRESH_ADAPTER_SUCCESS = 1001;
    protected final int REFRESH_ADAPTER_FAIL = REFRESH_ADAPTER_SUCCESS + 1;
    protected final int REFRESH_ADAPTER_FAIL_NETWORK = REFRESH_ADAPTER_FAIL + 1;

    protected long isFar = 5000;//5公里
    protected String[] scopes = new String[]{"全部目的地", "只看近的", "只看远的"};
    protected String[] orders = new String[]{"距离(升序)", "距离(降序)", "时间(升序)", "时间(降序)"};
    protected final static String SUCCESS = "success";

    RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
    ArrayList<RenRenSongContentInfo> data;
    ArrayList<RenRenSongContentInfo> delete_temp;
    ProgressDialog progressDialog;
    protected String lock = "lock";
    protected boolean isVisible = false;
    protected DbPresent dbPresent;

    int start_position = 0;
    int load_count = 20;

    protected void showWaitDialog(String tip) {
        ProgressBarItem.show(getActivity(), tip, false, null);

    }

    protected void onProgressDissmiss() {

    }

    protected void hideWaitDialog() {
        ProgressBarItem.hideProgress();
    }

    protected void startLoading(ImageView imageView) {
        imageView.setVisibility(View.VISIBLE);
        Animation rotate = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
        LinearInterpolator lin = new LinearInterpolator();
        rotate.setInterpolator(lin);
        imageView.startAnimation(rotate);
    }

    protected void stopLoading(ImageView imageView) {
        imageView.setVisibility(View.GONE);
        imageView.clearAnimation();
    }

    protected void changeOrder(int position) {
        SortComparator disComparator = null;
        switch (position) {
            case 0://距离升序
                disComparator = new SortComparator(0, 0);
                break;
            case 1:
                disComparator = new SortComparator(0, 1);
                break;
            case 2://时间升序
                disComparator = new SortComparator(1, 0);
                break;
            case 3:
                disComparator = new SortComparator(1, 1);
                break;
        }
        Collections.sort(data, disComparator);
        adapter.notifyDataSetChanged();
    }

    protected void changeScope(int position) {
        switch (position) {
            case 0:
                for (RenRenSongContentInfo item : data) {
                    item.setIsShow(true);
                }
                break;
            case 1:
                for (RenRenSongContentInfo item : data) {
                    if (Integer.parseInt(item.getDistance()) > isFar) {
                        item.setIsShow(false);
                    } else {
                        item.setIsShow(true);
                    }
                }
                break;
            case 2:
                for (RenRenSongContentInfo item : data) {
                    if (Integer.parseInt(item.getDistance()) > isFar) {
                        item.setIsShow(true);
                    } else {
                        item.setIsShow(false);
                    }
                }
                break;
        }
        adapter.notifyDataSetChanged();
    }

    protected void duplicatedData(RenRenSongContentResponse info, int page) {
        if(info.getContent()==null) {
            for (RenRenSongContentInfo c_d : data) {
                dbPresent.DeleteContent(c_d);
            }
            data.clear();
            return;
        }
        ArrayList<RenRenSongContentInfo> temp_add_data = new ArrayList<>();
        ArrayList<RenRenSongContentInfo> temp_del_data = new ArrayList<>();
        ArrayList<RenRenSongContentInfo> cache_del_data = new ArrayList<>();

        for (RenRenSongContentInfo i_t : info.getContent()) {
            boolean isExist = false;
            for (RenRenSongContentInfo i_d : data) {
                if(i_d.getOrder_id()==null || i_t.getOrder_id()==null){
                    break;
                }
                if (i_d.getOrder_id().equals(i_t.getOrder_id())) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                KLog.v("duplicatedData addddd" + i_t.getOrder_id());
                temp_add_data.add(i_t);
            }
        }

        for (RenRenSongContentInfo i_d : temp_add_data) {
            i_d.setActivity_page(page);
            i_d.setIsOperating(false);
            i_d.setIsShow(true);
            i_d.setOrder_owner(Constant.user_info.get(Constant.USER_INFO_ID));
            KLog.v("duplicatedData add" + i_d.getOrder_id());
            dbPresent.InsertReplaceContent(i_d);
            data.add(i_d);
        }

        for (RenRenSongContentInfo i_t : data) {
            boolean isExist = false;
            for (RenRenSongContentInfo i_d : info.getContent()) {
                if(i_d.getOrder_id()==null || i_t.getOrder_id()==null){
                    break;
                }
                if (i_d.getOrder_id().equals(i_t.getOrder_id())) {
//                    KLog.v("duplicatedData deeel" + i_t.getOrder_id());
                    isExist = true;
                    break;
                }
            }
            if( !i_t.getOrder_owner().equals(Constant.user_info.get(Constant.USER_INFO_ID))){
                cache_del_data.add(i_t);
                temp_del_data.add(i_t);
            }
            if (!isExist) {//本列表
                temp_del_data.add(i_t);
            }
        }
        for (RenRenSongContentInfo i_d : temp_del_data) {
     //       KLog.v("duplicatedData remove" + i_d.getOrder_id());
            data.remove(i_d);
        }

        for (RenRenSongContentInfo c_d : cache_del_data) {
  //          KLog.v("duplicatedData cache remove" + c_d.getOrder_id());
            dbPresent.DeleteContent(c_d);
        }
    }

}
