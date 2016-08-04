package com.mess.order;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.mess.clientdlnu.R;

public class MyPersonalizedsignature extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mypersonalizedsignature);
		
		final Intent intent = getIntent();
		String ps = intent.getStringExtra("personalizedsignature");
		final EditText personalizedsignature = (EditText)findViewById(R.id.personalizedsignature);
		
		if(!personalizedsignature.getText().toString().equals(R.string.unsure)){
		personalizedsignature.setText(ps);}
			
		Button sure = (Button)findViewById(R.id.sure);
		sure.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				setResult(0x719,intent);
				intent.putExtra("personalizedsignature", personalizedsignature.getText().toString());
				finish();
			}			
		});
		Button cancle = (Button)findViewById(R.id.cancle);
		cancle.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				//personalizedsignature.setText(R.string.hintofpersonalizedsignature);
				finish();
			}			
		});
		
	}
}
