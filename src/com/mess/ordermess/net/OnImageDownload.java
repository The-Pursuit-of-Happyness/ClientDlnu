package com.mess.ordermess.net;

import android.graphics.Bitmap;
import android.widget.ImageView;


public interface OnImageDownload {  
    void onDownloadSucc(Bitmap bitmap,String c_url);  
}  