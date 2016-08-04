package com.mess.order;

import java.io.FileNotFoundException;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import com.mess.clientdlnu.R;
import com.mess.ordermess.constant.Constants;
import com.mess.ordermess.dao.NavigationContent;
import com.mess.ordermess.dao.UserMessageInfo;
import com.mess.ordermess.net.NetUntils;
import com.mess.ordermess.ui.AutoScrollTextView;
import com.mess.ordermess.utils.DensityUtils;

public class MyMessage extends Activity {

	final int CODE1 = 0x717;
	final int CODE2 = 0x718;
	final int CODE3 = 0x719;
	final int CODE4 = 0x720;

	private TextView sex, myaddress, myemill,nickName;
	private AutoScrollTextView personalizedsignature;
	private TextView myname, tv_major, my_call, exitbutton, dialog_title;
	private SharedPreferences sp;
	private UserMessageInfo user;
	private ImageView headimage;
	private AlertDialog dialog;
	private LinearLayout show_mess_message_Lin;
	private RelativeLayout my_message_rel6, my_message_rel5, my_message_rel4,
			my_message_rel3, my_message_rel2, my_message_rel1,my_message_rel0;
	protected int firstX;
	protected int firstY;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mymessage);
		myname = (TextView) this.findViewById(R.id.myname);
		nickName = (TextView) this.findViewById(R.id.nickName);
		personalizedsignature = (AutoScrollTextView) findViewById(R.id.personalizedsignature);
		sex = (TextView) findViewById(R.id.sex);
		headimage = (ImageView) findViewById(R.id.headimage);
		myaddress = (TextView) findViewById(R.id.myaddress);
		myemill = (TextView) findViewById(R.id.myemill);
		tv_major = (TextView) this.findViewById(R.id.tv_major);
		my_call = (TextView) this.findViewById(R.id.my_call);
		exitbutton = (TextView) findViewById(R.id.exitbutton);
		show_mess_message_Lin = (LinearLayout) this
				.findViewById(R.id.show_my_message_Lin);
		my_message_rel6 = (RelativeLayout) this
				.findViewById(R.id.my_message_rel6);
		my_message_rel5 = (RelativeLayout) this
				.findViewById(R.id.my_message_rel5);
		my_message_rel4 = (RelativeLayout) this
				.findViewById(R.id.my_message_rel4);
		my_message_rel3 = (RelativeLayout) this
				.findViewById(R.id.my_message_rel3);
		my_message_rel2 = (RelativeLayout) this
				.findViewById(R.id.my_message_rel2);
		my_message_rel1 = (RelativeLayout) this
				.findViewById(R.id.my_message_rel1);
		my_message_rel0 = (RelativeLayout) this.findViewById(R.id.my_message_rel0);

		initialize();
		addListener();
	}

	private void initialize() {
		sp = getSharedPreferences("config", MODE_PRIVATE);
		String userId = sp.getString("User_ID", null);
		if (NetUntils.isNetworkAvailable(this)) {
			NotUserMessage(userId);
			getUserMessage(userId);
		} else {
			Toast.makeText(getApplicationContext(), "网络连接失败！", 0).show();
			NotUserMessage(userId);
		}
	}

	private void gainUserMessage(UserMessageInfo user) {
		String call = "未设置";
		myname.setText(user.getUsername());
		if(user.getNickName() != null){
			call = user.getNickName();
		}
		nickName.setText(call);
		sex.setText(user.getSex());
		myaddress.setText(user.getAddress());
		call = "未设置";
		if (user.getEmail() != null) {
			call = user.getEmail();
		}
		myemill.setText(call);
		personalizedsignature.setText(user.getAutograph());
		tv_major.setText(user.getMajor());
		call = "未设置";
		if (user.getMobilePhoneNumber() != null) {
			call = user.getMobilePhoneNumber();
		}
		my_call.setText(call);
		if (user.getGraphUrl().equals("")) {
			headimage.setImageResource(NavigationContent.defaultImage);
		} else {

		}
	}

	/**
	 * 当没有获取到服务端的数据时的处理
	 */
	private void NotUserMessage(String userName) {
		myname.setText(userName);
		sex.setText("获取中...");
		myaddress.setText("获取中...");
		personalizedsignature.setText("获取中...");
		tv_major.setText("获取中...");
		my_call.setText("获取中...");
		myemill.setText("获取中...");
		headimage.setImageResource(NavigationContent.defaultImage);
	}

	public void getUserMessage(final String userName) {
		BmobQuery<UserMessageInfo> query = new BmobQuery<UserMessageInfo>();
		query.addWhereEqualTo("username", userName);
		query.findObjects(this, new FindListener<UserMessageInfo>() {
			@Override
			public void onSuccess(List<UserMessageInfo> object) {
				for (UserMessageInfo userObject : object) {
					if (userObject.getUsername().equals(userName)) {
						user = userObject;
						gainUserMessage(userObject);
						break;
					}
				}

			}

			@Override
			public void onError(int code, String msg) {
			}
		});
	}

	private void addListener() {
		
		my_message_rel0.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				inputDialog(Constants.UPDATE_NICK_NAME);
			}
		});
		
		my_message_rel3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				inputDialog(Constants.UPDATE_ADDRESS);
			}
		});

		my_message_rel2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				inputDialog(Constants.UPDATE_CALL);
			}
		});

		my_message_rel5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				inputDialog(Constants.UPDATE_MAJOR);
			}
		});

		my_message_rel4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent(MyMessage.this, SelectSex.class);
				intent.putExtra("sex", sex.getText());
				startActivityForResult(intent, CODE1);
			}
		});

		my_message_rel6.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent(MyMessage.this,
						MyPersonalizedsignature.class);
				intent.putExtra("personalizedsignature", personalizedsignature
						.getText().toString());
				startActivityForResult(intent, CODE3);
			}
		});
		
		headimage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent();
				/* 开启Pictures画面Type设定为image */
				intent.setType("image/*");
				/* 使用Intent.ACTION_GET_CONTENT这个Action */
				intent.setAction(Intent.ACTION_GET_CONTENT);
				/* 取得相片后返回本画面 */
				startActivityForResult(intent, 1);
			}
		});
		
		my_message_rel1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent(MyMessage.this, MyEmill.class);
				intent.putExtra("myemill", myemill.getText().toString());
				startActivityForResult(intent, CODE4);
			}
		});

		// 退出当前账号
		exitbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 清除缓存用户对象
				BmobUser.logOut(MyMessage.this);
				// 发送退出登录广播
				Intent intent = new Intent();
				intent.setAction("com.dlnu.ordermess.UserLogin");
				intent.putExtra("msg", "exit");
				MyMessage.this.sendBroadcast(intent);
				Editor edit = sp.edit();
				edit.putString("User_Password", null);
				edit.commit();
				Toast.makeText(getApplicationContext(), "已退出当前账号！", 0).show();
				MyMessage.this.finish();
				// // 现在的currentUser是null了
				// BmobUser currentUser =
				// BmobUser.getCurrentUser(MyMessage.this);
			}
		});

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
						MyMessage.this.finish();
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

	private void inputDialog(final int type) {
		Button cancle, sure;
		AlertDialog.Builder builder = new Builder(this);
		View v = View.inflate(this, R.layout.dialog_enter_user_message, null);
		final EditText name = (EditText) v.findViewById(R.id.setup_pw_dialog);
		sure = (Button) v.findViewById(R.id.setup_ok);
		cancle = (Button) v.findViewById(R.id.setup_cancle);
		dialog_title = (TextView) v.findViewById(R.id.dialog_title);
		if (type == Constants.UPDATE_MAJOR) {
			dialog_title.setText("专业");
			name.setHint("请输入专业:");
		} else if (type == Constants.UPDATE_CALL) {
			dialog_title.setText("我的电话");
			name.setHint("电话:");
		}else if (type == Constants.UPDATE_NICK_NAME) {
			dialog_title.setText("昵称");
			name.setHint("请输入昵称:");
		}else{
			dialog_title.setText("地址");
			name.setHint("请输入地址:");
		}
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
					dialog_title.setText("输入不能为空!");
					return;
				}
				if (type == Constants.UPDATE_MAJOR) {
					tv_major.setText(result);
					updateUserMsg(result, Constants.UPDATE_MAJOR);
					dialog.dismiss();
				} else if (Constants.UPDATE_CALL == type) {
					if (!DensityUtils.isPhoneNumberValid(result)) {
						dialog_title.setText("电话号码输入不合法!");
						return;
					}
					my_call.setText(result);
					updateUserMsg(result, Constants.UPDATE_CALL);
					dialog.dismiss();
				}else if (Constants.UPDATE_NICK_NAME == type) {
					nickName.setText(result);
					updateUserMsg(result, Constants.UPDATE_NICK_NAME);
					dialog.dismiss();
				}else{
					myaddress.setText(result);
					updateUserMsg(result, Constants.UPDATE_ADDRESS);
					dialog.dismiss();
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 自动生成的方法存根
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CODE1 && resultCode == CODE1) {
			String presentsex = data.getStringExtra("sex");
			sex.setText(presentsex);
			updateUserMsg(presentsex, Constants.UPDATE_SEX);
		}
		if (requestCode == CODE3 && resultCode == CODE3) {
			String presentpersonalizedsignature = data
					.getStringExtra("personalizedsignature");
			personalizedsignature.setText(presentpersonalizedsignature);
			updateUserMsg(presentpersonalizedsignature,
					Constants.UPDATE_AUTOGRAPH);
		}
		if (requestCode == CODE4 && resultCode == CODE4) {
			String emill = data.getStringExtra("myemill");
			myemill.setText(emill);
			updateUserMsg(emill, Constants.UPDATE_EMAIL);
		}
		if (resultCode == RESULT_OK) {
			Uri uri = data.getData();
			ContentResolver cr = this.getContentResolver();
			updateUserMsg(uri.toString(), Constants.UPDATE_GRAPH);
			try {
				Bitmap bitmap = BitmapFactory.decodeStream(cr
						.openInputStream(uri));
				/* 将Bitmap设定到ImageView */
				headimage.setImageBitmap(bitmap);
			} catch (FileNotFoundException e) {
			}
		}
	}

	public void updateUserMsg(String values, int index) {
		UserMessageInfo bmobUser = BmobUser.getCurrentUser(MyMessage.this,
				UserMessageInfo.class);
		UserMessageInfo updateUser = new UserMessageInfo();
		if (index == Constants.UPDATE_EMAIL) {
			updateUser.setEmail(values);
		} else if (index == Constants.UPDATE_CALL) {
			updateUser.setMobilePhoneNumber(values);
		} else if (index == Constants.UPDATE_ADDRESS) {
			updateUser.setAddress(values);
		} else if (index == Constants.UPDATE_SEX) {
			updateUser.setSex(values);
		} else if (index == Constants.UPDATE_MAJOR) {
			updateUser.setMajor(values);
		} else if (index == Constants.UPDATE_AUTOGRAPH) {
			updateUser.setAutograph(values);
		}else if(index == Constants.UPDATE_NICK_NAME){
			updateUser.setNickName(values);
		}
		updateUser.update(this, bmobUser.getObjectId(), new UpdateListener() {
			@Override
			public void onSuccess() {
				Toast.makeText(getApplicationContext(), "用户信息更新成功！", 0).show();
			}

			@Override
			public void onFailure(int code, String msg) {
				Toast.makeText(getApplicationContext(), "用户信息更新失败！", 0).show();
			}
		});
	}
}
