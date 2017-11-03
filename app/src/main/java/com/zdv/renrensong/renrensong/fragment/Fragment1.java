package com.zdv.renrensong.renrensong.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.zdv.renrensong.renrensong.view.IFragment;

/**
 *
 * @author xiaoyl
 * @date 2013-07-20
 */
public class Fragment1 extends BaseFetchFragment implements IFragment {
    private String title = "今天";

    IFragment1Listener listener1;
    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void onAttach(Context context) {
        try {
            listener1 = (IFragment1Listener) context;
        }catch(Exception e){
            e.fillInStackTrace();
        }
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener1.fragment1Created();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isInit){
            hideWaitDialog();
        }
    }

    public  interface IFragment1Listener {

        void fragment1Created();

    }
}
