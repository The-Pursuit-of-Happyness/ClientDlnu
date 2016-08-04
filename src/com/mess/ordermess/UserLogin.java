package com.mess.ordermess;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import com.mess.clientdlnu.R;
import com.mess.ordermess.constant.Constants;
import com.mess.ordermess.dao.UserMessageInfo;
import com.mess.ordermess.net.NetUntils;
import com.mess.ordermess.ui.ShowDialog;
import com.mess.ordermess.utils.Md5Utils;

public class UserLogin extends Activity {

	protected static final int MESSAGE_ONE = 0x0001;
	protected static final int MESSAGE_TWO = 0x0002;
	protected static final int MESSAGE_ERROR = 0x0003;

	private Button user_login, user_register;
	private EditText user_login_password, user_login_name;
	private CheckBox user_login_checkbox;
	private SharedPreferences sp;
	private int time = 0;
	private long count;
	private boolean isStop = false;
	private ShowDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_login_layout_activity);

		user_login = (Button) this.findViewById(R.id.user_login);
		user_register = (Button) this.findViewById(R.id.user_register);
		user_login_name = (EditText) this.findViewById(R.id.user_login_name);
		user_login_password = (EditText) this
				.findViewById(R.id.user_login_password);
		user_login_checkbox = (CheckBox) this
				.findViewById(R.id.user_login_checkbox);

		Bmob.initialize(this, Constants.BMOB_API_ID);
		count = System.currentTimeMillis();
		initialize();
	}

	private void initialize() {
		sp = getSharedPreferences("config", MODE_PRIVATE);

		boolean isRemember = sp.getBoolean("Is_Remember_PW", false);
		String userId = sp.getString("User_ID", null);
		if (isRemember) {
			if (userId != null) {
				user_login_name.setText(userId);
			}
			user_login_checkbox.setChecked(true);
		} else {
			Editor editor = sp.edit();
			editor.putString("User_ID", null);
			editor.commit();
			user_login_checkbox.setChecked(false);
			user_login_name.setText("");
		}

		user_login.setOnClickListener(listener);
		user_register.setOnClickListener(listener);
		user_login_checkbox
				.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						Editor editor = sp.edit();
						editor.putBoolean("Is_Remember_PW", isChecked);
						editor.commit();
					}
				});
	}

	/**
	 * 事件处理
	 */
	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Button btn = (Button) v;
			if (btn == user_login) {
				if (NetUntils.isNetworkAvailable(UserLogin.this)) {
					login();
				} else {
					Toast.makeText(getApplicationContext(), "网络不可用，请检查网络！", 0)
							.show();
				}

			} else if (user_register == btn) {
				Intent intent = new Intent(getApplicationContext(),
						UserRegister.class);
				startActivity(intent);
			}
		}
	};

	private void login() {
		// 防止用户多次点击，
		if (System.currentTimeMillis() - count < 1000) {
			Toast.makeText(this, "屏幕快点破了！", 0).show();
			return;
		}
		count = System.currentTimeMillis();
		
		edit_User_Id = user_login_name.getText().toString().trim();
		md5UserPw = user_login_password.getText().toString().trim();
		if (edit_User_Id.equals("") || md5UserPw.equals("")) {
			Toast.makeText(getApplicationContext(), "账号或密码不能为空!", 0).show();
			return;
		}
		if (md5UserPw.length() < 6) {
			Toast.makeText(getApplicationContext(), "密码至少为6位!", 0).show();
			return;
		}
		dialog = new ShowDialog(this);
		dialog.createDialog("登录中...");

		userJudge(edit_User_Id);

	}

	private String edit_User_Id;
	private String md5UserPw;

	final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MESSAGE_ONE:
				isStop = false;
				dialog.closeDialog();
				Toast.makeText(getApplicationContext(), "登陆超时！", 0).show();
				break;
			case MESSAGE_ERROR:
				dialog.closeDialog();
				Toast.makeText(getApplicationContext(), "登陆失败！用户名或密码不正确！", 0)
						.show();
			default:
				break;
			}
		}
	};

	/**
	 * 判断是消费者账号还是店家账号
	 * 
	 * @param user_Id
	 */
	private void userJudge(final String user_Id) {
		time = 0;
		isStop = true;
		new Thread(){
			public void run() {
				while (isStop) {
					if (time >= 6) {
						Message message = new Message();
						message.what = MESSAGE_ONE;
						handler.sendMessage(message);
					}
					time++;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
		
		BmobQuery<UserMessageInfo> query = new BmobQuery<UserMessageInfo>();
		query.addWhereEqualTo("username", user_Id);
		query.findObjects(this, new FindListener<UserMessageInfo>() {
			@Override
			public void onSuccess(List<UserMessageInfo> object) {
				for (UserMessageInfo userTemp : object) {
					if (userTemp.getUsername().equals(user_Id)) {
						if (userTemp.getIsShopId()) {
							dialog.closeDialog();
							Toast.makeText(getApplicationContext(),
									"店家账号不能在消费者客户端登陆！", 0).show();
							isStop = false;
						} else {
							userLogin(edit_User_Id, md5UserPw);
						}
						break;
					}
				}
			}

			@Override
			public void onError(int code, String msg) {
			}
		});
	}

	private void userLogin(final String user_Id, String userPW) {
		final String md5PW = Md5Utils.md5Password32(userPW);
		UserMessageInfo user = new UserMessageInfo();
		user.setUsername(user_Id);
		user.setPassword(md5PW);
		user.login(this, new SaveListener() {
			@Override
			public void onSuccess() {

				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				dialog.closeDialog();

				Editor editor = sp.edit();
				editor.putString("User_ID", user_Id);
				editor.putString("User_Password", md5PW);
				editor.commit();

				Intent intent = new Intent();
				intent.setAction("com.dlnu.ordermess.UserLogin");
				intent.putExtra("msg", "Login");
				UserLogin.this.sendBroadcast(intent);
				isStop = false;
				Toast.makeText(getApplicationContext(), "登陆成功！", 0).show();
				UserLogin.this.finish();
			}

			@Override
			public void onFailure(int code, String msg) {
				dialog.closeDialog();
				isStop = false;
				Toast.makeText(getApplicationContext(), "登陆失败！用户名或密码不正确！", 0)
						.show();
			}
		});
	}

	/**
	 * 返回上一页
	 * 
	 * @param view
	 */
	public void userLoginBack(View view) {
		this.finish();
	}

	/**
	 * 忘记密码处理函数
	 */
	public void forgetPassword(View view) {
		Toast.makeText(this, "由于技术条件和时间有限，此功能暂时无法提供服务！", 0).show();
	}
}
