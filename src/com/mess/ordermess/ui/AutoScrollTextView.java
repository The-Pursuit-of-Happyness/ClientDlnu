package com.mess.ordermess.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewDebug.CapturedViewProperty;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class AutoScrollTextView extends TextView {
	
	private BufferType mBufferType = BufferType.NORMAL;
	
	public AutoScrollTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public AutoScrollTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public AutoScrollTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean isFocused(){
		return true;
	} 
	
	@Override
	@CapturedViewProperty
	public CharSequence getText() {
		// TODO Auto-generated method stub
		return super.getText();
	}

	@Override
	public void setText(CharSequence text, BufferType type) {
		// TODO Auto-generated method stub
		super.setText(text, type);
	}
	
	public final void setText(String text){
		setText(text,mBufferType);
	}
}
