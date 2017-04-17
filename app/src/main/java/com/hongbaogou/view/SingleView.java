package com.hongbaogou.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hongbaogou.R;

public class SingleView extends LinearLayout implements Checkable {
	private 	TextView tv_personaldata_username, tv_personaldata_usertel, tv_default, tv_showAddress;
	private 	CheckBox iv_address_check;
	public SingleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public SingleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public SingleView(Context context) {
		super(context);
		initView(context);
	}

	private void initView(Context context){

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.item_single_layout, this, true);
		tv_personaldata_username = (TextView) v.findViewById(R.id.tv_personaldata_username);
		tv_personaldata_usertel = (TextView) v.findViewById(R.id.tv_personaldata_usertel);
		tv_default = (TextView) v.findViewById(R.id.tv_default);
		tv_showAddress = (TextView) v.findViewById(R.id.tv_showAddress);
		iv_address_check = (CheckBox) v.findViewById(R.id.iv_address_check);

	}

	@Override
	public void setChecked(boolean checked) {
		iv_address_check.setChecked(checked);
		
	}

	@Override
	public boolean isChecked() {
		return iv_address_check.isChecked();
	}

	@Override
	public void toggle() {
		iv_address_check.toggle();
	}
	
	public void setUserName(String text){
		tv_personaldata_username.setText(text);
	}
	public void setUserTel(String text){
		tv_personaldata_usertel.setText(text);
	}
	public void setDefault(String text){
		if ("Y".equals(text)) {
			tv_default.setText("[默认]");
		} else {
			tv_default.setVisibility(View.GONE);
		}
	}
	public void setAddress(String text){
		tv_showAddress.setText(text);
	}

}
