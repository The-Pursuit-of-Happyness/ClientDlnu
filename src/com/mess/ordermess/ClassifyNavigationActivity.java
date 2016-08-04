package com.mess.ordermess;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mess.clientdlnu.R;
import com.mess.ordermess.dao.NavigationContent;
import com.mess.publicmethod.PublicShowMessListActivity;
import com.mess.publicmethod.SearchMess;

public class ClassifyNavigationActivity extends Activity {

	private TextView tv_nav_papular;
	private GridView tv_navigation;
	private MenuNavigationAdapter adapter;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.navigation_activity);

		this.tv_navigation = (GridView) this.findViewById(R.id.tv_navigation);

		this.adapter = new MenuNavigationAdapter();
		this.tv_navigation.setAdapter(adapter);
		addListener();
	}
	
	/**
	 * ËÑË÷²Ëµ¥
	 * @param view
	 */
	public void searchMess(View view){
		Intent intent = new Intent(getApplicationContext(),SearchMess.class);
		startActivity(intent);
	}

	private void addListener() {
		tv_navigation.setOnItemClickListener(new OnItemClickListener() {

			Intent intent;
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					intent = new Intent(ClassifyNavigationActivity.this,PublicShowMessListActivity.class);
					intent.putExtra("mess_Type", NavigationContent.MessClassifyID[0]);
					startActivity(intent);
					break;
				case 1:
					intent = new Intent(ClassifyNavigationActivity.this,PublicShowMessListActivity.class);
					intent.putExtra("mess_Type", NavigationContent.MessClassifyID[1]);
					startActivity(intent);
					break;
				case 2:
					intent = new Intent(ClassifyNavigationActivity.this,PublicShowMessListActivity.class);
					intent.putExtra("mess_Type", NavigationContent.MessClassifyID[2]);
					startActivity(intent);
					break;
				case 3:
					intent = new Intent(ClassifyNavigationActivity.this,PublicShowMessListActivity.class);
					intent.putExtra("mess_Type", NavigationContent.MessClassifyID[3]);
					startActivity(intent);
					break;
				case 4:
					intent = new Intent(ClassifyNavigationActivity.this,PublicShowMessListActivity.class);
					intent.putExtra("mess_Type", NavigationContent.MessClassifyID[4]);
					startActivity(intent);
					break;
				case 5:
					intent = new Intent(ClassifyNavigationActivity.this,PublicShowMessListActivity.class);
					intent.putExtra("mess_Type", NavigationContent.MessClassifyID[5]);
					startActivity(intent);
					break;
				case 6:
					intent = new Intent(ClassifyNavigationActivity.this,PublicShowMessListActivity.class);
					intent.putExtra("mess_Type", NavigationContent.MessClassifyID[6]);
					startActivity(intent);
					break;
				case 7:
					intent = new Intent(ClassifyNavigationActivity.this,PublicShowMessListActivity.class);
					intent.putExtra("mess_Type", NavigationContent.MessClassifyID[7]);
					startActivity(intent);
					break;
				case 8:
					intent = new Intent(ClassifyNavigationActivity.this,PublicShowMessListActivity.class);
					intent.putExtra("mess_Type", NavigationContent.MessClassifyID[8]);
					startActivity(intent);
					break;
				case 9:
					intent = new Intent(ClassifyNavigationActivity.this,PublicShowMessListActivity.class);
					intent.putExtra("mess_Type", NavigationContent.MessClassifyID[9]);
					startActivity(intent);
					break;
				case 10:
					intent = new Intent(ClassifyNavigationActivity.this,PublicShowMessListActivity.class);
					intent.putExtra("mess_Type", NavigationContent.MessClassifyID[10]);
					startActivity(intent);
					break;
				case 11:
					intent = new Intent(ClassifyNavigationActivity.this,PublicShowMessListActivity.class);
					intent.putExtra("mess_Type", NavigationContent.MessClassifyID[11]);
					startActivity(intent);
					break;
				default:
					break;
				}
			}

		});
	}
	
	private class MenuNavigationAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return NavigationContent.menuNames.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(getApplicationContext(),
					R.layout.navigation_items_activity, null);
			ImageView iv = (ImageView) view
					.findViewById(R.id.iv_navigation_img);
			TextView tv = (TextView) view.findViewById(R.id.tv_navigation_text);
			iv.setImageResource(NavigationContent.menuIcon[position]);
			tv.setText(NavigationContent.menuNames[position]);
			return view;
		}

	}

}
