package com.mess.ordermess.dao;

import android.os.Parcel;
import android.os.Parcelable;
import cn.bmob.v3.BmobObject;

public class ShopActivityInfo extends BmobObject implements Parcelable{

	private String shop_Id;
	private String activity_Id;
	private String shopName;
	private String activity_Content;
	private String image_Url;
	
	public ShopActivityInfo(){}
	
	public ShopActivityInfo(String shop_Id,String activity_Id,String activity_Name,
			String activity_Content,String image_Url){
		this.shop_Id = shop_Id;
		this.activity_Id = activity_Id;
		this.shopName = activity_Name;
		this.activity_Content = activity_Content;
		this.image_Url = image_Url;
	}
	
	
	
	public String getShop_Id() {
		return shop_Id;
	}

	public void setShop_Id(String shop_Id) {
		this.shop_Id = shop_Id;
	}

	public String getActivity_Id() {
		return activity_Id;
	}

	public void setActivity_Id(String activity_Id) {
		this.activity_Id = activity_Id;
	}

	public String getActivity_Name() {
		return shopName;
	}

	public void setActivity_Name(String activity_Name) {
		this.shopName = activity_Name;
	}

	public String getActivity_Content() {
		return activity_Content;
	}

	public void setActivity_Content(String activity_Content) {
		this.activity_Content = activity_Content;
	}

	public String getImage_Url() {
		return image_Url;
	}

	public void setImage_Url(String image_Url) {
		this.image_Url = image_Url;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(shop_Id);
		dest.writeString(activity_Id);
		dest.writeString(shopName);
		dest.writeString(activity_Content);
		dest.writeString(image_Url);
		
	}
	
	public static final Parcelable.Creator<ShopActivityInfo> CREATOR = new ShopActivityInfo.Creator<ShopActivityInfo>() {
		@Override
		public ShopActivityInfo createFromParcel(Parcel source) {
			return new ShopActivityInfo(source.readString(),
					source.readString(), source.readString(),
					source.readString(), source.readString());
		}

		@Override
		public ShopActivityInfo[] newArray(int size) {
			return new ShopActivityInfo[size];
		}
	};

}
