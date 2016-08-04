package com.mess.ordermess.constant;

import android.os.Environment;

public class Constants {
	/*****************************************/
	
	public static String BMOB_API_ID = "b7af65d596b5d5d543ae0271aee8c224";
	public static String BMOB_API_ACCESSKEY = "981551412b1011855f230d14a4e64668";
	
	/*****************************************/
	
	public static String mSdRootPath = Environment.getExternalStorageDirectory().getPath();  
	public final static String FOLDER_NAME = "/ClientDlnu";  
	public final static String PICTURE_YUAN = "big_";  
	   
	/*****************************************/
	
	public static String HOME = "主页";
	public static String ORDER_MESS = "推荐";
	public static String ORDER_CLASSIFY = "分类";
	public static String MY_ORDER_MESS = "我的";
	
	/*****************************************/
	
	public static int SQL_COUNT = 24;	
	public static float DIALOG_SCREEN_WIDTH_SCALE = 0.8f;
	public static float DIALOG_SCREEN_HEIGHT_SCALE = 0.4f;
	
	/*****************************************/
	
	//显示新品菜品排行标识id
	public static int COMMEND_MESS_NEW = 0x0001;
	//显示热销菜品排行标识id
	public static int COMMEND_MESS_HOT = 0x0002;
	//显示低价菜品排行标识id
	public static int COMMEND_MESS_DISCOUNT = 0x0003;
	//显示商店排行标识id
	public static int COMMEND_MESS_SHOP = 0x0004;
	
	/*******************************************/
	
	public static final int UPDATE_EMAIL = 0x0005;
	public static final int UPDATE_CALL = 0x0006;
	public static final int UPDATE_ADDRESS = 0x0007;
	public static final int UPDATE_SEX = 0x0008;
	public static final int UPDATE_MAJOR = 0x00024;
	public static final int UPDATE_NICK_NAME = 0x00025;
	public static final int UPDATE_AUTOGRAPH = 0x0009;
	public static final int UPDATE_GRAPH = 0x0010;
	
	/********************************************/
	
	public static int COMMEND_MESS_CLISSIFY = 0x0011;
	
	/*********************************************/
	
	public static int LOADING_SAMLL_IMAGE = 0x0012;
	public static int LOADING_BIG_IMAGE = 0x0013;
	
	/*****************************************/
	
	public final static int GAIN_MESS_MSG_SUCCESS = 0x0014;
	public final static int GAIN_SHOP_MSG_SUCCESS = 0x0015;
	public final static int GAIN_MESS_MSG_ERROR = 0x0016;
	public final static int GAIN_SHOP_MSG_ERROR = 0x0017;
	public final static int REFRESH_SYSTEM_TIME = 0x0018;
	public final static int SHOP_ACTIVITY_DATA = 0x0019;
	public final static int GALLERY_REFRESH = 0x0020;
	public final static int DOWNLOAD_ACTIVITY_IMAGE = 0x0021;
	
	
	public final static int GRIDVIEW_NEWS_ADAPTER = 0x0022;
	public final static int GRIDVIEW_CLASSIFY_ADAPTER = 0x0023;
	
	/*****************************************/
	
	public static String CLEAR_CACHE_NOTIFICATION = "已经将应用缓存清理完毕！";
	public static String NOTIFICATION_CONTENT_TITLE = "系统清理";
	public static String NOTIFICATION_CONTENT_TEXT = "已经将应用“点参宝”缓存图片清理完毕！";
	
	public static String CLEAR_CACHE_TIME[] = {
		"15分钟","30分钟","1小时","24小时","手动清理"
	} ;
}
