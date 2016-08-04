package com.mess.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mess.clientdlnu.R;
import com.mess.ordermess.utils.DensityUtils;

public class MyEmill extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myemill);

		final Intent intent = getIntent();
		String ps = intent.getStringExtra("myemill");
		final EditText emill = (EditText) findViewById(R.id.emill);

		if (!emill.getText().toString().equals(R.string.unsure)) {
			emill.setText(ps);
		}

		Button sure = (Button) findViewById(R.id.sure);
		sure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String res = emill.getText().toString().trim();
				if(res.equals("")){
					MyEmill.this.setTitle("输入不能为空！");
					return;
				}
				if(!DensityUtils.isEmail(res)){
					MyEmill.this.setTitle("邮箱格式不合法！");
					return;
				}
				setResult(0x720, intent);
				intent.putExtra("myemill", res);
				finish();
			}
		});
		Button cancle = (Button) findViewById(R.id.cancle);
		cancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}
}
