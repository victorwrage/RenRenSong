package com.zdv.renrensong.renrensong.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.zdv.renrensong.renrensong.R;

/**
 * 事件编辑Sipnner适配器
 * @author xiaoyl
 * @date 2013-07-15
 */
public class ChoiceSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
	private Context context;
	private String[] list;
    public ChoiceSpinnerAdapter(Context context,String[] list){
    	this.context = context;
    	this.list = list;
    }
	@Override
	public int getCount() {
		
		return list.length;
	}

	@Override
	public String getItem(int position) {
	
		return list[position];
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

	    View view = View.inflate(context, R.layout.spinner_lay_item,null);
		TextView v = (TextView) view.findViewById(R.id.spinner_txt_tv);
	    v.setText(list[position]);
		return view;
	}

}
