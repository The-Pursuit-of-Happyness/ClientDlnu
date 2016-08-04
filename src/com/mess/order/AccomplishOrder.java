package com.mess.order;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import com.mess.clientdlnu.R;
import com.mess.ordermess.adapter.LoadImage;
import com.mess.ordermess.adapter.ViewMyOrderHolder;
import com.mess.ordermess.adapter.LoadImage.GetImageBitmap;
import com.mess.ordermess.constant.Constants;
import com.mess.ordermess.dao.MessMenu;
import com.mess.ordermess.dao.NavigationContent;
import com.mess.ordermess.dao.ShopMsgInfo;
import com.mess.ordermess.dao.UserMessageInfo;
import com.mess.ordermess.dao.UserOrder;
import com.mess.ordermess.utils.DensityUtils;
import com.mess.publicmethod.PublicUserCommentActivity;

public class AccomplishOrder extends Activity {

	private ListView listview;
	private OrderMessAdapter adapter;
	private List<UserOrder> order;
	
	private MessMenu mess;
	private ShopMsgInfo shop;

	private SharedPreferences sp;
	private String user_id;
	private TextView order_mess_tip,order_title;
	private LinearLayout order_mess_Loading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myorder);

		listview = (ListView) findViewById(R.id.listview2);
		order_mess_tip = (TextView) this.findViewById(R.id.order_mess_tip);
		order_mess_Loading = (LinearLayout) this
				.findViewById(R.id.order_mess_Loading);
		order_title = (TextView) this.findViewById(R.id.order_title);
		order_title.setText("我的消费记录");

		/* initialize bmob java bean */
		Bmob.initialize(this, "b7af65d596b5d5d543ae0271aee8c224");

		UserMessageInfo bmobUser = BmobUser.getCurrentUser(AccomplishOrder.this,
				UserMessageInfo.class);
		if (bmobUser == null) {
			order_mess_Loading.setVisibility(View.INVISIBLE);
			order_mess_tip.setVisibility(View.VISIBLE);
		} else {
			order_mess_tip.setVisibility(View.INVISIBLE);
			sp = getSharedPreferences("config", this.MODE_PRIVATE);
			user_id = sp.getString("User_ID", null);
			fillDate();
		}
	}

	private void fillDate() {
		order_mess_Loading.setVisibility(View.VISIBLE);
		order = new ArrayList<UserOrder>();
		new Thread() {
			public void run() {
				getUserOrder();
			}
		}.start();

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			}
		});

	}

	private void getUserOrder() {
		BmobQuery<UserOrder> query1 = new BmobQuery<UserOrder>();
		BmobQuery<UserOrder> query2 = new BmobQuery<UserOrder>();
		List<BmobQuery<UserOrder>> queries = new ArrayList<BmobQuery<UserOrder>>();
		query1.addWhereEqualTo("user_Id", user_id);
		query2.addWhereEqualTo("state", "3");
		queries.add(query1);
		queries.add(query2);
		BmobQuery<UserOrder> query = new BmobQuery<UserOrder>();
		query.and(queries);	
		query.order("createdAt");
		query.findObjects(this, new FindListener<UserOrder>() {
			@Override
			public void onSuccess(List<UserOrder> object) {
				if (object.size() > 0) {
					order = object;
					if (adapter == null) {
						adapter = new OrderMessAdapter();
						listview.setAdapter(adapter);
					} else {
						adapter.notifyDataSetChanged();
					}
					
				} else {
					order_mess_tip.setVisibility(View.VISIBLE);
					order_mess_tip.setText("你还没有消费历史\n或者记录已被清空！");
				}
				order_mess_Loading.setVisibility(View.INVISIBLE);
			}

			@Override
			public void onError(int code, String msg) {
				order_mess_tip.setVisibility(View.VISIBLE);
				order_mess_Loading.setVisibility(View.INVISIBLE);
				order_mess_tip.setText("获取订单信息失败！");
				Toast.makeText(getApplicationContext(), "获取订单信息失败!", 0).show();
			}
		});
	}

	private class OrderMessAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return order.size();
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ViewMyOrderHolder holder;
			View view;
			if (convertView != null) {
				view = convertView;
				holder = (ViewMyOrderHolder) convertView.getTag();
			} else {
				view = View.inflate(getApplicationContext(),
						R.layout.my_order_lis_item, null);
				holder = new ViewMyOrderHolder();

				holder.order_shop_name = (TextView) view
						.findViewById(R.id.order_shop_name);
				holder.order_deal_state = (TextView) view
						.findViewById(R.id.order_deal_state);
				holder.iv_mess_icon = (ImageView) view
						.findViewById(R.id.iv_mess_icon);
				holder.iv_mess_name = (TextView) view
						.findViewById(R.id.iv_mess_name);
				holder.iv_mess_temp = (TextView) view
						.findViewById(R.id.iv_mess_temp);
				holder.tv_mess_price = (TextView) view
						.findViewById(R.id.tv_mess_price);
				holder.by_count = (TextView) view.findViewById(R.id.by_count);
				holder.my_order_amount = (TextView) view
						.findViewById(R.id.my_order_amount);
				holder.my_order_time = (TextView) view
						.findViewById(R.id.my_order_time);
				holder.my_order_delete = (Button) view
						.findViewById(R.id.my_order_delete);
				holder.my_order_comment = (Button) view
						.findViewById(R.id.my_order_comment);

				view.setTag(holder);
			}

			final UserOrder orderObject = order.get(position);

			holder.order_shop_name.setText("获取中...");

			holder.iv_mess_icon.setBackgroundResource(R.drawable.loading);
			LoadImage.LoadImageFile(AccomplishOrder.this, Constants.LOADING_SAMLL_IMAGE,
					orderObject.getMess_Icon_Url(), new GetImageBitmap(){

						@Override
						public void getImageBitmap(Bitmap bitmap,
								String cacheUrl) {
							if(DensityUtils.judgeFileSame(orderObject.getMess_Icon_Url(), cacheUrl)){
								holder.iv_mess_icon.setBackgroundColor(Color.WHITE);
								holder.iv_mess_icon.setImageBitmap(bitmap);
							}							
						}
				
			});
			holder.iv_mess_name.setText("获取中...");
			holder.iv_mess_temp.setText("食品类型:获取中...");
			holder.tv_mess_price.setText("单价:获取中...");
			holder.my_order_amount.setText("共计:0.00元");
			// 从服务端异步获得店家的信息
			getShopMessage(orderObject.getShop_Id(), holder.order_shop_name);
			// 从服务端异步获得菜品的信息
			getMessMessage(orderObject,holder);

			holder.my_order_time.setText(orderObject.getSubscribeTime());
			holder.by_count.setText("x" + orderObject.getCount());
			
			if (orderObject.getState().equals("0")) {
				holder.order_deal_state.setText("待支付");
			} else if (orderObject.getState().equals("1")) {
				holder.order_deal_state.setText("待收货");
			} else if(orderObject.getState().equals("3")){
				holder.order_deal_state.setText("交易完成");
			}

			holder.my_order_delete.setText("删除记录");
			holder.my_order_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					deleteMyOrder(position);
				}
			});
			holder.my_order_comment.setText("评论订单");
			holder.my_order_comment.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					addUserDiscuss(order.get(position));
				}
			});

			return view;
		}
	}

	private void deleteMyOrder(final int pos) {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("询问");
		builder.setCancelable(false);
		builder.setMessage("你确定要删除吗？");
		builder.setNeutralButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				deleteUserOrder(order.get(pos),pos);				
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", null);
		builder.show();
	}

	/**
	 * 删除服务端的用户订单信息
	 * @param userOrder
	 */
	protected void deleteUserOrder(UserOrder userOrder,final int position) {
		UserOrder deleteOrder = new UserOrder();
		deleteOrder.setObjectId(userOrder.getObjectId());
		deleteOrder.delete(this, new DeleteListener() {

		    @Override
		    public void onSuccess() {
		    	order.remove(position);
				adapter.notifyDataSetChanged();
		    	Toast.makeText(getApplicationContext(), "删除成功！", 0).show();
		    }

		    @Override
		    public void onFailure(int code, String msg) {
		    	Toast.makeText(getApplicationContext(), "删除失败！", 0).show();
		    }
		});
	}

	/**
	 * 异步取得服务端的店家信息
	 * 
	 * @param shop_Id
	 * @param order_shop_name
	 */
	public void getShopMessage(final String shop_Id,
			final TextView order_shop_name) {
		BmobQuery<ShopMsgInfo> query = new BmobQuery<ShopMsgInfo>();
		query.addWhereEqualTo("shop_Id", shop_Id);
		query.findObjects(this, new FindListener<ShopMsgInfo>() {
			@Override
			public void onSuccess(List<ShopMsgInfo> object) {
				for (ShopMsgInfo shopMsg : object) {
					if (shopMsg.getShop_Id().equals(shop_Id)) {
						order_shop_name.setText(shopMsg.getShop_Name());
						break;
					}
				}
			}

			@Override
			public void onError(int code, String msg) {
			}
		});
	}

	/**
	 * 异步取得服务端的菜品信息
	 * 
	 * @param mess_Id
	 * @param iv_mess_name
	 * @param iv_mess_temp
	 * @param tv_mess_price
	 */
	public void getMessMessage(final UserOrder orderObject,final ViewMyOrderHolder holder) {
		
		BmobQuery<MessMenu> query = new BmobQuery<MessMenu>();
		query.addWhereEqualTo("mess_Id", orderObject.getMess_Id());
		query.findObjects(this, new FindListener<MessMenu>() {
			@Override
			public void onSuccess(List<MessMenu> object) {
				for (MessMenu messMenu : object) {
					if (messMenu.getShop_Id().equals(orderObject.getShop_Id())) {
						holder.iv_mess_name.setText(messMenu.getMess_Name());
						holder.iv_mess_temp.setText("食品类型:"+NavigationContent.menuNames[Integer.parseInt(messMenu.getMess_Type())]);
						holder.tv_mess_price.setText("单价:"+messMenu.getMess_Price()+"元");
						float temp = messMenu.getMess_Price().floatValue() * orderObject.getCount().floatValue();
						holder.my_order_amount.setText("共计："+temp+"元");
						break;
					}
				}
			}

			@Override
			public void onError(int code, String msg) {
			}
		});
	}

	protected void addUserDiscuss(UserOrder userOrder) {
		Intent intent = new Intent(getApplicationContext(),PublicUserCommentActivity.class);
		intent.putExtra("com.mess.ordermess.dao.UserOrder", (Parcelable)userOrder);
		startActivity(intent);
	}
}
