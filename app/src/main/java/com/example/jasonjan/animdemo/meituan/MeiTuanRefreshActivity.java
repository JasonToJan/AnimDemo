package com.example.jasonjan.animdemo.meituan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.jasonjan.animdemo.R;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MeiTuanRefreshActivity extends Activity implements MeiTuanListView.OnMeiTuanRefreshListener {

	private MeiTuanListView mListView;
	private List<String> mDatas;
	private MyAdapter mAdapter;
	private final static int REFRESH_COMPLETE = 0;
	/**
	 * mInterHandler是一个私有静态内部类继承自Handler，内部持有MainActivity的弱引用，
	 * 避免内存泄露
	 */
	private InterHandler mInterHandler = new InterHandler(this);

	private static class InterHandler extends Handler{

		private WeakReference<MeiTuanRefreshActivity> mActivity;

		public InterHandler(MeiTuanRefreshActivity activity){
			mActivity = new WeakReference<MeiTuanRefreshActivity>(activity);
		}
		@Override
		public void handleMessage(Message msg) {
			MeiTuanRefreshActivity activity = mActivity.get();
			if (activity != null) {
				switch (msg.what) {
					case REFRESH_COMPLETE:
							activity.mListView.setOnRefreshComplete();
							activity.mAdapter.notifyDataSetChanged();
							activity.mListView.setSelection(0);
						break;
				}
			}else{
				super.handleMessage(msg);
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mei_tuan_refresh);
		mListView = (MeiTuanListView) findViewById(R.id.listview);
		String[] data = new String[]{"hello world","hello world","hello world","hello world",
				"hello world","hello world","hello world","hello world","hello world",
				"hello world","hello world","hello world","hello world","hello world",};
		mDatas = new ArrayList<String>(Arrays.asList(data));
		mAdapter=new MyAdapter();
		mListView.setAdapter(mAdapter);
		mListView.setOnMeiTuanRefreshListener(this);
	}

	@Override
	public void onRefresh() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					mDatas.add(0, "new data");
					mInterHandler.sendEmptyMessage(REFRESH_COMPLETE);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	class MyAdapter extends BaseAdapter{

	    @Override
        public int getCount(){
	        return mDatas.size();
        }

        @Override
        public Object getItem(int position){
	        return mDatas.get(position);
        }

        @Override
        public long getItemId(int position){
	        return position;
        }

        @Override
        public View getView(int position,View convertView,ViewGroup parent){
	        View view=View.inflate(getApplicationContext(),R.layout.item_meituan_listview,null);
            TextView tv=(TextView)view.findViewById(R.id.item_meituan_listview_tv);
            tv.setText(mDatas.get(position));

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(MeiTuanRefreshActivity.this,MyActivity.class);
                    startActivity(intent);
                }
            });

            return view;
        }
    }


}
