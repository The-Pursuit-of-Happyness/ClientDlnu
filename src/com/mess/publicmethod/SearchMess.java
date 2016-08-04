package com.mess.publicmethod;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.mess.clientdlnu.R;
import com.mess.ordermess.adapter.OrderMessAdapter;
import com.mess.ordermess.constant.Constants;
import com.mess.ordermess.dao.MessMenu;
import com.mess.ordermess.dao.MessNameDao;

public class SearchMess extends Activity {

	private AutoCompleteTextView search_auto_complete;
	private LinearLayout search_loading;
	private List<String> messName;
	private ArrayAdapter<String> adapter;
	private Button search_activity_ok;
	private List<MessMenu> messSameResult;
	private ListView search_order_mess;
	private OrderMessAdapter resultAdapter;
	private SharedPreferences sp;
	private boolean isHave;
	private TextView search_result_mess;
	private String searchMessTemp;

	private MessNameDao messDao;

	private int SELECT_ALL_MESS = 2;
	private int SELECT_SAME_MESS = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_search_layout);
		search_auto_complete = (AutoCompleteTextView) this
				.findViewById(R.id.search_auto_complete);
		search_loading = (LinearLayout) this.findViewById(R.id.search_loading);
		search_activity_ok = (Button) this
				.findViewById(R.id.search_activity_ok);
		search_order_mess = (ListView) this
				.findViewById(R.id.search_order_mess);
		search_result_mess = (TextView) this
				.findViewById(R.id.search_result_mess);

		sp = this.getSharedPreferences("config", MODE_PRIVATE);
		isHave = sp.getBoolean("DB_Is_Have", false);
		messDao = new MessNameDao(this);

		initialize();
	}

	/**
	 * 初始化界面及其数据
	 */
	public void initialize() {
		messName = new ArrayList<String>();
		search_loading.setVisibility(View.VISIBLE);
		search_auto_complete.setEnabled(false);
		search_activity_ok.setClickable(false);
		new Thread() {
			public void run() {
				// 判断数据库是否有菜品名称，如果有，就从数据库里取得数据
				// 如果没有就从网络端获取
				if (isHave) {
					messName = messDao.getMessMsg();
				} else {
					getMessMessageData(SELECT_ALL_MESS, "");
				}
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if (isHave) {
							initDataComplete();
						}
					}

				});
			}
		}.start();

		search_activity_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String mess = search_auto_complete.getText().toString().trim();
				if (mess.equals("")) {
					Toast.makeText(getApplicationContext(), "查询内容不能为空!", 0)
							.show();
					return;
				}
				//当用户没有输入新的菜名，就不再搜索，防止用户乱点
				if(searchMessTemp != null && searchMessTemp.equals(mess)){
					return;
				}
				searchMessTemp = mess;
				searchMess(mess);
			}
		});

		search_order_mess.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				showMess(messSameResult.get(position));
			}

		});

		search_auto_complete.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 得到选择的哪一项
				String str = parent.getItemAtPosition(position).toString();
				searchMess(str);
			}

		});
	}

	public void showMess(MessMenu obj) {
		Intent intent = new Intent(getApplicationContext(),
				PublicShowMessMsgActivity.class);
		intent.putExtra("com.mess.ordermess.dao.MessMenu", (Parcelable) obj);
		startActivity(intent);
	}

	/**
	 * 通过输入的名称进行查询
	 * 
	 * @param messName
	 */
	public void searchMess(String mess) {
		getMessMessageData(SELECT_SAME_MESS, mess);
	}

	public void initDataComplete() {
		lifeCompent();
		adapter = new ArrayAdapter<String>(SearchMess.this,
				android.R.layout.simple_dropdown_item_1line, messName);
		search_auto_complete.setAdapter(adapter);
	}

	private void lifeCompent() {
		search_loading.setVisibility(View.INVISIBLE);
		search_auto_complete.setEnabled(true);
		search_activity_ok.setClickable(true);
	}

	public void getMessMessageData(final int type, String condition) {
		final BmobQuery<MessMenu> query = new BmobQuery<MessMenu>();
		if (type == SELECT_SAME_MESS) {
			query.addWhereContains("mess_Name", condition);
		}
		query.findObjects(SearchMess.this, new FindListener<MessMenu>() {
			@Override
			public void onSuccess(List<MessMenu> object) {
				if (object.size() > 0) {
					if (type != SELECT_SAME_MESS) {
						messDao.insertMessMsg(object);
						// 保存到数据后，对标记提更新,缺点，无法实时跟新
						Editor editor = sp.edit();
						editor.putBoolean("DB_Is_Have", true);
						editor.commit();
					} else {
						messSameResult = object;
						resultAdapter = new OrderMessAdapter(SearchMess.this,
								Constants.COMMEND_MESS_CLISSIFY, object);
						search_order_mess.setAdapter(resultAdapter);

						search_result_mess.setText("找到相似菜品：" + object.size()
								+ "个");
					}
				} else {
					search_result_mess.setText("没有找到类似菜品!");
				}
				lifeCompent();
			}

			@Override
			public void onError(int code, String msg) {
				lifeCompent();
				Toast.makeText(SearchMess.this, "获取数据失败!", 0).show();
			}
		});
	}

	/**
	 * 反回上级菜单
	 * 
	 * @param view
	 */
	public void backMenu(View view) {
		this.finish();
	}
}
