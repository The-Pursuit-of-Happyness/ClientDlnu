package com.mess.order;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.mess.clientdlnu.R;
import com.mess.ordermess.utils.GetAPPMessage;

public class AboutUs extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aboutus);
		TextView versionsmessage = (TextView)findViewById(R.id.versionsmessage);
		versionsmessage.setText("V"+GetAPPMessage.getVersion(this));
	
	}
}
