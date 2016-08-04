package com.mess.order;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.mess.clientdlnu.R;

public class Myname extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myname);
		
		final Intent intent = getIntent();
		String na = intent.getStringExtra("name");
		final EditText name = (EditText)findViewById(R.id.name);
		
		if(!name.getText().toString().equals(R.string.hint)){
		name.setText(na);}
			
		
		Button sure = (Button)findViewById(R.id.sure);
		sure.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				String Username = name.getText().toString().trim();
				setResult(0x718,intent);
				intent.putExtra("name", Username);
				finish();
			}			
		});
		Button cancle = (Button)findViewById(R.id.cancle);
		cancle.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				//name.setText(R.string.hint);
				finish();
			}			
		});
	}
}
