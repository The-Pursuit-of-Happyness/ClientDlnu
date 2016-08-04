package com.mess.publicmethod;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import com.mess.clientdlnu.R;
import com.mess.ordermess.adapter.LoadImage;
import com.mess.ordermess.adapter.LoadImage.GetImageBitmap;
import com.mess.ordermess.constant.Constants;
import com.mess.ordermess.dao.MessMenu;
import com.mess.ordermess.dao.NavigationContent;
import com.mess.ordermess.dao.ShopMsgInfo;
import com.mess.ordermess.dao.UserOrder;
import com.mess.ordermess.utils.DensityUtils;
import com.mess.ordermess.utils.Md5Utils;

public class PublicByOrder extends Activity {

	private MessMenu byMess;
	private ShopMsgInfo shopMsg;
	private SharedPreferences sp;
	private AlertDialog dialog;

	private Button btn_by_mess_add, btn_by_mess_slate;
	private TextView btn_by_mess_tv, by_count, by_mess_order,
			tv_by_mess_shopName, iv_mess_name, iv_mess_temp, tv_mess_price,
			by_mess_address, tv_by_name;
	private EditText by_mess_leave_message;
	private ImageView logo_tip,iv_mess_icon;
	private int count = 1;
	private String user_Id;
	private int inputPWCount = 0;
	private ScrollView show_mess_message_Lin;
	protected int firstX;
	protected int firstY;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_bymess_activity);

		btn_by_mess_add = (Button) this.findViewById(R.id.btn_by_mess_add);
		btn_by_mess_slate = (Button) this.findViewById(R.id.btn_by_mess_slate);
		btn_by_mess_tv = (TextView) this.findViewById(R.id.btn_by_mess_tv);
		by_count = (TextView) this.findViewById(R.id.by_count);
		by_mess_order = (TextView) this.findViewById(R.id.by_mess_order);
		tv_by_mess_shopName = (TextView) this
				.findViewById(R.id.tv_by_mess_shopName);
		iv_mess_name = (TextView) this.findViewById(R.id.iv_mess_name);
		iv_mess_temp = (TextView) this.findViewById(R.id.iv_mess_temp);
		tv_mess_price = (TextView) this.findViewById(R.id.tv_mess_price);
		by_mess_address = (TextView) this.findViewById(R.id.by_mess_address);
		tv_by_name = (TextView) this.findViewById(R.id.tv_by_name);
		by_mess_leave_message = (EditText) this
				.findViewById(R.id.by_mess_leave_message);
		iv_mess_icon = (ImageView) this.findViewById(R.id.iv_mess_icon);
		logo_tip = (ImageView) this.findViewById(R.id.logo_tip);
		show_mess_message_Lin = (ScrollView) this.findViewById(R.id.show_mess_message_Lin);
		gainData();
		fillData();
		btn_by_mess_slate.setOnClickListener(listener);
		btn_by_mess_add.setOnClickListener(listener);
	}

	private void fillData() {

		getShopMessData();
		sp = getSharedPreferences("config", MODE_PRIVATE);

		user_Id = sp.getString("User_ID", null);
		logo_tip.setImageResource(NavigationContent.defaultImage);
		iv_mess_icon.setBackgroundResource(R.drawable.loading);

		LoadImage.LoadImageFile(this, Constants.LOADING_SAMLL_IMAGE, byMess.getMess_Graph(),
				new GetImageBitmap(){

					@Override
					public void getImageBitmap(Bitmap bitmap, String cacheUrl) {
						iv_mess_icon.setBackgroundColor(Color.WHITE);
						iv_mess_icon.setImageBitmap(bitmap);
					}
			
		});

		
		tv_by_name.setText("订餐人:" + user_Id);

		tv_by_mess_shopName.setText("店铺:获取中...");
		iv_mess_name.setText("食品名称:" + byMess.getMess_Name());
		iv_mess_temp.setText("食品类型:" + NavigationContent.menuNames[Integer.parseInt(byMess.getMess_Type())]);
		tv_mess_price.setText("单价:" + byMess.getMess_Price() + "元");
		by_count.setText("x" + count);
		by_mess_address.setText("取饭地点:获取中...");
		by_mess_order.setText("合计:" + (count * byMess.getMess_Price()) + "元");

		
		this.show_mess_message_Lin.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				boolean result = false;
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					firstX = (int) event.getX();
					firstY = (int) event.getY();
					break;
				case MotionEvent.ACTION_UP:
					int distanceX = (int) (firstX - event.getX());
					int distanceY = (int) Math.abs(firstY - event.getY());
					if (distanceY > Math.abs(distanceX))
						return false;
					if (distanceX < -20) {
						PublicByOrder.this.finish();
						result = true;
					} else {
						result = false;
					}
					break;
				case MotionEvent.ACTION_MOVE:
					break;
				}
				return result;
			}

		});
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Button btn = (Button) v;
			if (btn == btn_by_mess_slate) {
				if (count <= 1) {
					Toast.makeText(getApplicationContext(), "至少购买一份", 1).show();
					return;
				}
				count--;
			} else if (btn == btn_by_mess_add) {
				count++;
			}
			by_count.setText("x" + count);
			btn_by_mess_tv.setText("" + count);
			by_mess_order.setText("合计:" + (count * byMess.getMess_Price())
					+ "元");
		}

	};

	/**
	 * 得到数据
	 */
	public void gainData() {
		Intent intent = getIntent();
		byMess = intent.getParcelableExtra("com.dlnu.ordermess.dao.MessMenu");
	}

	/**
	 * 返回上一个页面
	 * 
	 * @param view
	 */
	public void backMenu(View view) {
		this.finish();
	}

	/**
	 * 确认订单
	 * 
	 * @param view
	 */
	public void OkByMess(View view) {
		inputDialog();
	}

	/**
	 * 提交订单
	 * 
	 * @param type
	 *            当单状态
	 * @return
	 */
	private void submitOrder(final int type) {
		String temp = DensityUtils.getSystemTime();
		String dateTime = temp.substring(0, 17);
		String leaveMsg = by_mess_leave_message.getText().toString();

		UserOrder order = new UserOrder();
		String order_id = DensityUtils.subString(temp);
		order.setOrder_id(order_id);
		order.setUser_Id(user_Id);
		if (shopMsg.getShop_Id() != null) {
			order.setShop_Id(shopMsg.getShop_Id());
		}
		order.setMess_Id(byMess.getMess_Id());
		order.setMess_Icon_Url(byMess.getMess_Graph());
		order.setSubscribeTime(dateTime);
		order.setCount(count);
		order.setState(type + "");
		if (leaveMsg.equals("")) {
			order.setLeaveMessage("该用户不挑食，并没有留下什么！");
		} else {
			order.setLeaveMessage(leaveMsg);
		}

		order.save(this, new SaveListener() {

			@Override
			public void onFailure(int arg0, String arg1) {
				if (type != 0) {
					Toast.makeText(getApplicationContext(), "购买失败！" + arg0, 0)
							.show();

				}
			}

			@Override
			public void onSuccess() {
				if (type != 0) {
					Toast.makeText(getApplicationContext(), "购买成功！", 0).show();
				}
			}

		});

		/**
		 * 更新用户的金额
		 */
	}

	/**
	 * 取得服务器端店家的信息
	 * 
	 * @param type
	 */
	public void getShopMessData() {
		BmobQuery<ShopMsgInfo> query = new BmobQuery<ShopMsgInfo>();
		query.addWhereEqualTo("shop_Id", byMess.getShop_Id());
		query.findObjects(this, new FindListener<ShopMsgInfo>() {
			@Override
			public void onSuccess(List<ShopMsgInfo> object) {
				for (ShopMsgInfo shopObject : object) {
					if (shopObject.getShop_Id().equals(byMess.getShop_Id())) {
						shopMsg = shopObject;
						tv_by_mess_shopName.setText("店铺:"
								+ shopObject.getShop_Name());
						by_mess_address.setText("取饭地点:"
								+ shopObject.getShop_Address());
						break;
					}
				}
			}

			@Override
			public void onError(int code, String msg) {
				Toast.makeText(getApplicationContext(), "获取店家信息失败！", 0).show();
			}
		});
	}

	private void inputDialog() {
		Button cancle, sure;
		AlertDialog.Builder builder = new Builder(this);
		View v = View.inflate(this, R.layout.dialog_enter_user_message, null);
		final EditText name = (EditText) v.findViewById(R.id.setup_pw_dialog);
		sure = (Button) v.findViewById(R.id.setup_ok);
		cancle = (Button) v.findViewById(R.id.setup_cancle);
		TextView dialog_title = (TextView) v.findViewById(R.id.dialog_title);
		dialog_title.setText("请输入支付密码:");
		name.setTransformationMethod(PasswordTransformationMethod.getInstance());
		builder.setView(v);
		dialog = builder.show();
		cancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		sure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String result = name.getText().toString().trim();
				if (result.equals("")) {
					Toast.makeText(getApplicationContext(), "密码不能为空！", 0)
							.show();
					return;
				}

				String userPassword = sp.getString("User_Password", null);
				String inputPW = Md5Utils.md5Password32(result);
				if (!inputPW.equals(userPassword)) {
					inputPWCount++;
					Toast.makeText(getApplicationContext(), "密码不正确！", 0).show();
					name.setText("");
					return;
				}
				if (inputPWCount >= 3) {
					submitOrder(0);
					Toast.makeText(getApplicationContext(), "您输错超过3次，请稍后重试！", 0)
							.show();
					return;
				}
				if (shopMsg == null) {
					Toast.makeText(getApplicationContext(), "获取订单信息失败，请稍后重试！",
							0).show();
					return;
				}
				submitOrder(1);
				dialog.dismiss();
				PublicByOrder.this.finish();
			}
		});
	}
}
