package com.myksb1223.pulltorefesh;

import java.util.LinkedList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private LinkedList<String> lists;
	
	public CustomAdapter(Context context, LinkedList<String> lists) {
		mInflater = LayoutInflater.from(context);
		this.lists = lists;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		 
		if(convertView == null) {
			holder = new ViewHolder();
			 
			convertView = mInflater.inflate(R.layout.data_row, null);
					 							 				 
			holder.text = (TextView)convertView.findViewById(R.id.text);			 
					 
			convertView.setTag(holder);			 
		}
		else {
			holder = (ViewHolder)convertView.getTag();
		}
		 
		String text = lists.get(position);
		holder.text.setText(text);
		
		return convertView;
	}
	
	static class ViewHolder {
		TextView text;
	}	

}
