package com.mess.ordermess.adapter;

import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mess.clientdlnu.R;
import com.mess.ordermess.adapter.LoadImage.GetImageBitmap;
import com.mess.ordermess.constant.Constants;
import com.mess.ordermess.dao.MessMenu;

public class NewsActivityAdapter extends BaseAdapter {

	private Activity context;
	private int type;
	private List<MessMenu> messInfo;
	
	public NewsActivityAdapter(Activity context,int type,List<MessMenu> messInfo){
		this.context = context;
		this.type = type;
		this.messInfo = messInfo;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return messInfo.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		final NewsItemHolder holder;
		if(convertView != null && convertView instanceof LinearLayout){
			view = convertView;
			holder = (NewsItemHolder) view.getTag();
		}else{
			view = View.inflate(context, R.layout.news_activity_items, null);
			holder = new NewsItemHolder();
			holder.order_mess_Loading = (LinearLayout) view.findViewById(R.id.order_mess_Loading);
			holder.iv_news = (ImageView) view.findViewById(R.id.iv_news);
			holder.tv_news_name = (TextView) view.findViewById(R.id.tv_news_name);
			holder.tv_news_content = (TextView) view.findViewById(R.id.tv_news_content);
			holder.tv_news_price = (TextView) view.findViewById(R.id.tv_news_price);
			view.setTag(holder);
		}
		MessMenu messObject = messInfo.get(position);
		holder.order_mess_Loading.setVisibility(View.VISIBLE);
		LoadImage.LoadImageFile(context, Constants.LOADING_SAMLL_IMAGE, messObject.getMess_Graph(), 
				new GetImageBitmap(){

					@Override
					public void getImageBitmap(Bitmap bitmap, String cacheUrl) {
						holder.iv_news.setImageBitmap(bitmap);
						holder.order_mess_Loading.setVisibility(View.INVISIBLE);
					}
			
		});
		
		holder.tv_news_name.setText(messObject.getMess_Name());
		holder.tv_news_content.setText(messObject.getMess_Describe());
		if(Constants.GRIDVIEW_CLASSIFY_ADAPTER == type){
			holder.tv_news_price.setText(messObject.getMess_Price()+"£§  "
					+ messObject.getSales_Volume() + "»À“—π∫¬Ú");
		}else{
			holder.tv_news_price.setText("£§"+messObject.getMess_Price());
		}
		return view;
	}

}
