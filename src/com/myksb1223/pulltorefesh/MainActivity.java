package com.myksb1223.pulltorefesh;

import java.util.LinkedList;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity {

	private LinkedList<String> lists;
	private CustomAdapter mAdapter;
	private KSBRefreshListView listView;
	private boolean isPullRefresh, isPullLoadmore;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		isPullRefresh = false;
		isPullLoadmore = false;
		
		lists = new LinkedList<String>();
		lists.add("It's rather good to be supposed");
		lists.add("There is good chance of~");
		lists.add("It looks delicious!");
		lists.add("Who are you ~ing now?");
		lists.add("It is just as well (that)");
		lists.add("I feel better");
		lists.add("I want to be with you");
		lists.add("That is a shame");
		lists.add("What a bummer");
		lists.add("I am going to be late a bit to be there");
		lists.add("I knew it");
		lists.add("I thought so");
		lists.add("There is no answer");
		lists.add("Don't skip meals");
		lists.add("labor at a task");
		
		mAdapter = new CustomAdapter(this, lists);
		listView = (KSBRefreshListView)findViewById(R.id.refreshList);
		listView.setAdapter(mAdapter);
		
		listView.setOnPullListener(new OnPullListener() {

			@Override
			public void pullToRefresh(boolean isRefresh) {
				// TODO Auto-generated method stub
				if(isPullRefresh == isRefresh) {
					return;
				}
				
				isPullRefresh = isRefresh;
				new RefreshTask().execute();
			}

			@Override
			public void pullToLoadmore(boolean isLoadmore) {
				// TODO Auto-generated method stub
				
				if(isPullLoadmore == isLoadmore) {
					return;
				}
				
				isPullLoadmore = isLoadmore;
				new RefreshTask().execute();								
			}
			
		});
	}
	
	private class RefreshTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			// Simulates a background job.
			
			try {
				Thread.sleep(3000);
			}
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			if(isPullRefresh) {
				lists.addFirst("This is pull to refresh");
				isPullRefresh = false;
			}
			
			if(isPullLoadmore) {
				lists.add("This is pull to load more");
				isPullLoadmore = false;
			}
			
			mAdapter.notifyDataSetChanged();
			listView.pullToFinish();
			super.onPostExecute(result);
		}
	}  

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
