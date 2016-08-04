package com.mess.publicmethod;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import com.mess.clientdlnu.R;
import com.mess.ordermess.dao.MessMenu;
import com.mess.ordermess.dao.ShopMsgInfo;
import com.mess.ordermess.dao.UserCommentInfo;
import com.mess.ordermess.dao.UserOrder;
import com.mess.ordermess.utils.DensityUtils;

public class PublicUserCommentActivity extends Activity {

	private TextView tv_by_mess_shopName, iv_mess_name, tv_mess_price;
	private ImageView iv_mess_icon;
	private RatingBar ratingBar_decscribe, ratingBar_mess, ratingBar_server;
	private EditText mess_shop_comment;

	private UserOrder receiveUserOrder;

	private float star1 = 0, star2 = 0, star3 = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_user_comment_activity);
		tv_by_mess_shopName = (TextView) this
				.findViewById(R.id.tv_by_mess_shopName);
		iv_mess_name = (TextView) this.findViewById(R.id.iv_mess_name);
		tv_mess_price = (TextView) this.findViewById(R.id.tv_mess_price);
		iv_mess_icon = (ImageView) this.findViewById(R.id.iv_mess_icon);
		ratingBar_decscribe = (RatingBar) this
				.findViewById(R.id.ratingBar_decscribe);
		ratingBar_mess = (RatingBar) this.findViewById(R.id.ratingBar_mess);
		ratingBar_server = (RatingBar) this.findViewById(R.id.ratingBar_server);
		mess_shop_comment = (EditText) this
				.findViewById(R.id.mess_shop_comment);

		getData();
		fillData();
	}

	public void getData() {
		Intent intent = this.getIntent();
		receiveUserOrder = intent
				.getParcelableExtra("com.mess.ordermess.dao.UserOrder");

	}

	private void fillData() {

		tv_by_mess_shopName.setText("店铺:***");
		iv_mess_name.setText("菜名:***");
		tv_mess_price.setText("0.00元");

		getShopMessage();
		getMessMessage();

		iv_mess_icon.setBackgroundResource(R.drawable.loading);

		ratingBar_decscribe
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

					@Override
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {
						star1 = rating;
					}

				});
		ratingBar_mess
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

					@Override
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {
						star2 = rating;
					}

				});
		ratingBar_server
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

					@Override
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {
						star3 = rating;
					}

				});
	}

	/**
	 * 从服务器端取得店家信息
	 */
	private void getShopMessage() {
		BmobQuery<ShopMsgInfo> query = new BmobQuery<ShopMsgInfo>();
		query.addWhereEqualTo("shop_Id", receiveUserOrder.getShop_Id());
		query.findObjects(this, new FindListener<ShopMsgInfo>() {
			@Override
			public void onSuccess(List<ShopMsgInfo> object) {
				for (ShopMsgInfo shopObject : object) {
					if (shopObject.getShop_Id().equals(
							receiveUserOrder.getShop_Id())) {
						tv_by_mess_shopName.setText("店铺:"
								+ shopObject.getShop_Name());
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

	/**
	 * 异步从服务端取得菜品信息
	 */
	private void getMessMessage() {
		BmobQuery<MessMenu> query = new BmobQuery<MessMenu>();
		query.addWhereEqualTo("mess_Id", receiveUserOrder.getMess_Id());
		query.findObjects(this, new FindListener<MessMenu>() {
			@Override
			public void onSuccess(List<MessMenu> object) {
				for (MessMenu messMenu : object) {
					if (messMenu.getMess_Id().equals(
							receiveUserOrder.getMess_Id())) {
						iv_mess_name.setText("菜名:" + messMenu.getMess_Name());
						tv_mess_price.setText(messMenu.getMess_Price() + "元");
						break;
					}
				}
			}

			@Override
			public void onError(int code, String msg) {
			}
		});
	}

	public void backMenu(View view) {
		this.finish();
	}

	/**
	 * 提交用户评论
	 * @param view
	 */
	public void OkMessComment(View view) {
		String time = DensityUtils.getSystemTime();
		UserCommentInfo comment = new UserCommentInfo();
		comment.setUser_id(receiveUserOrder.getUser_Id());
		comment.setMess_Id(receiveUserOrder.getMess_Id());
		int star = (int) (star1 + star2 + star3) / 3;
		comment.setComment_Start(star);
		comment.setComment_type("0");
		comment.setComment_Time(time);
		comment.setShop_Id(receiveUserOrder.getShop_Id());
		String content = mess_shop_comment.getText().toString().trim();
		if (content.equals("")) {
			comment.setComment_content("他太懒了，什么灰机也没有留下！");
		} else {
			comment.setComment_content(content);
		}

		comment.save(PublicUserCommentActivity.this, new SaveListener() {

			@Override
			public void onSuccess() {
				PublicUserCommentActivity.this.finish();
				Toast.makeText(getApplicationContext(), "评论成功！", 0).show();
			}

			@Override
			public void onFailure(int code, String arg0) {
				Toast.makeText(getApplicationContext(), "评论失败！", 0).show();
			}
		});
	}
}
