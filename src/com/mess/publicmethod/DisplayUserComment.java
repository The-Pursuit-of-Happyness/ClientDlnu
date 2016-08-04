package com.mess.publicmethod;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.mess.clientdlnu.R;
import com.mess.ordermess.adapter.ViewUserCommentHolder;
import com.mess.ordermess.dao.NavigationContent;
import com.mess.ordermess.dao.UserCommentInfo;
import com.mess.ordermess.dao.UserMessageInfo;

public class DisplayUserComment extends Activity {

	private TextView order_mess_tip;
	private LinearLayout order_mess_Loading;
	private ListView show_user_comment_listView;
	private UserCommentAdapter adapter;
	private List<UserCommentInfo> comment;
	private String mess_Id;

	private int firstX;
	private int firstY;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display_user_comment_activity);
		order_mess_tip = (TextView) this.findViewById(R.id.order_mess_tip);
		order_mess_Loading = (LinearLayout) this
				.findViewById(R.id.order_mess_Loading);
		show_user_comment_listView = (ListView) this
				.findViewById(R.id.show_user_comment_listView);

		getData();
		fillDate();
	}

	public void getData() {
		Intent intent = this.getIntent();
		mess_Id = intent
				.getStringExtra("com.dlnu.ordermess.DisplayUserComment");
	}

	private void fillDate() {
		order_mess_Loading.setVisibility(View.VISIBLE);
		comment = new ArrayList<UserCommentInfo>();
		new Thread() {
			public void run() {
				getMessDiscuss();
			}
		}.start();
	}

	/**
	 * 从服务端取得用户评论
	 */
	protected void getMessDiscuss() {
		BmobQuery<UserCommentInfo> query = new BmobQuery<UserCommentInfo>();
		query.addWhereEqualTo("mess_Id", mess_Id);
		query.findObjects(this, new FindListener<UserCommentInfo>() {
			@Override
			public void onSuccess(List<UserCommentInfo> object) {
				if (object.size() > 0) {
					comment = object;
					if (adapter == null) {
						adapter = new UserCommentAdapter();
						show_user_comment_listView.setAdapter(adapter);
					} else {
						adapter.notifyDataSetChanged();
					}
				} else {
					order_mess_tip.setVisibility(View.VISIBLE);
					order_mess_tip.setText("额，暂时还没有用户评论！");
				}
				order_mess_Loading.setVisibility(View.INVISIBLE);
			}

			@Override
			public void onError(int code, String msg) {
				order_mess_Loading.setVisibility(View.INVISIBLE);
				Toast.makeText(getApplicationContext(), "获取数据失败!" + code, 0)
						.show();
			}
		});
	}

	public void onBack(View view) {
		this.finish();
	}

	private class UserCommentAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return comment.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewUserCommentHolder holder;
			View view;
			if (convertView != null) {
				view = convertView;
				holder = (ViewUserCommentHolder) view.getTag();
			} else {
				holder = new ViewUserCommentHolder();
				view = View.inflate(getApplicationContext(),
						R.layout.user_comment_lis_item, null);
				view.setTag(holder);
				holder.user_graph = (ImageView) view
						.findViewById(R.id.user_graph);
				holder.user_name = (TextView) view.findViewById(R.id.user_name);
				holder.user_comment_content = (TextView) view
						.findViewById(R.id.user_comment_content);
				holder.user_comment_time = (TextView) view
						.findViewById(R.id.user_comment_time);
				holder.user_comment_star = (RatingBar) view
						.findViewById(R.id.user_comment_star);
			}

			holder.user_graph.setImageResource(NavigationContent.defaultImage);

			holder.user_name.setText(comment.get(position).getUser_id());
			// getUserName(holder,comment.get(position));

			holder.user_comment_content.setText(comment.get(position)
					.getComment_content());
			holder.user_comment_time.setText(comment.get(position)
					.getComment_Time());
			int star = comment.get(position).getComment_Start().intValue() * 2;
			holder.user_comment_star.setProgress(star);
			return view;
		}
	}
}
