package com.myksb1223.pulltorefesh;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class KSBRefreshListView extends ListView implements OnScrollListener {
	private static String TAG = "KSBRefreshListView";
	private int pullType;
	private View header, footer;
	private int scrollState, prevState, prevVisibleCount;	
	private OnPullListener mListener;
	private Context mCfx;
	private boolean isRefresh, isLoadmore, isOnScrolled;
	
	public KSBRefreshListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initialized(context, null);
	}

	public KSBRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initialized(context, attrs);
	}
	
	public KSBRefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initialized(context, attrs);
	}
	
	public void initialized(Context context, AttributeSet attrs) {
		isOnScrolled = true;
		isRefresh = false;
		isLoadmore= false;
		mCfx = context;
		setOnScrollListener(this);		
    if(attrs != null){
    	TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.KSBRefreshListView);
    	pullType = a.getInteger(R.styleable.KSBRefreshListView_pullType, -1);
    	a.recycle();
    }
    else{
    	pullType = -1;
    }
		
    if(pullType != -1) {
    	LayoutInflater mInflater = LayoutInflater.from(context);
  		header = mInflater.inflate(R.layout.header, null, false);
  		footer = mInflater.inflate(R.layout.footer, null, false);

  		addHeaderView(header);
  		addFooterView(footer);
  		
  		showHeaderFooter(pullType);
    }		
    
    setHeaderDividersEnabled(false);    
    setFooterDividersEnabled(false);
//  	setScrollbarFadingEnabled(true);
	}
	
	public void showHeaderFooter(int type) {		
		if(type != pullType) {
			pullType = type;
		}
		
		if(pullType == 0) {			
  		header.findViewById(R.id.wrap_layout).setVisibility(View.VISIBLE);
  		footer.findViewById(R.id.wrap_layout).setVisibility(View.GONE);
  	}
  	else if(pullType == 1) {
  		header.findViewById(R.id.wrap_layout).setVisibility(View.GONE);
  		footer.findViewById(R.id.wrap_layout).setVisibility(View.VISIBLE);
  	}
  	else if(pullType == 2) {
  		header.findViewById(R.id.wrap_layout).setVisibility(View.VISIBLE);
  		footer.findViewById(R.id.wrap_layout).setVisibility(View.VISIBLE);
  	}  			
		else {
  		header.findViewById(R.id.wrap_layout).setVisibility(View.GONE);
  		footer.findViewById(R.id.wrap_layout).setVisibility(View.GONE);			
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
				
		if (view.getCount() <= 2) {
			return;
		}
				
		if(pullType == -1) {
			return;
		}
		
		if(!isOnScrolled) {
			return;
		}
		
		if(isLoadmore || isRefresh) {
			return;
		}			

		Log.d(TAG, "offset VISIBLECOUNT : " + visibleItemCount + " first : " + firstVisibleItem + " total : " + totalItemCount);		
		Log.d(TAG, "offset footer prevState == " + prevState + " afterState == " + scrollState); 
		
		if(prevState == 0 && scrollState == 1 || prevState == 2 && scrollState == 1) {
			if(pullType != 1) {
				if(firstVisibleItem < 1) {
//					if(visibleItemCount != prevVisibleCount) {
//						prevVisibleCount = visibleItemCount;
						header.findViewById(R.id.wrap_layout).setVisibility(View.VISIBLE);
//					}
//					else {
//						header.findViewById(R.id.wrap_layout).setVisibility(View.GONE);
//					}
				}
				else {
					header.findViewById(R.id.wrap_layout).setVisibility(View.GONE);
/*					if(pullType == 2) {
						footer.findViewById(R.id.wrap_layout).setVisibility(View.GONE);
					}*/
				}
			}
			
			if(pullType != 0) {				
				if((visibleItemCount + firstVisibleItem) <= totalItemCount) {
					if(pullType == 2) {
						if(!isRefresh) {
							footer.findViewById(R.id.wrap_layout).setVisibility(View.VISIBLE);
						}
					}
					else {
						footer.findViewById(R.id.wrap_layout).setVisibility(View.VISIBLE);
					}
//					if(visibleItemCount == prevVisibleCount) {
//						prevVisibleCount = visibleItemCount;
					Log.d(TAG, "offset footer VISIBLE not load ");
										
//					}
//					else {
//						footer.findViewById(R.id.wrap_layout).setVisibility(View.GONE);
//					}						
				}
				else {
					Log.d(TAG, "offset footer GONE not load");
					footer.findViewById(R.id.wrap_layout).setVisibility(View.GONE);
/*					if(pullType == 2) {
						header.findViewById(R.id.wrap_layout).setVisibility(View.GONE);
					}*/

				}
			}
		}
		
		if(prevState == 1 && scrollState == 0) {
			if(pullType != 1) {
				if(firstVisibleItem < 1) {
//					if(visibleItemCount != prevVisibleCount) {
//						prevVisibleCount = visibleItemCount;
						Log.d(TAG, "offet refresh!");
						isRefresh = true;
						header.findViewById(R.id.wrap_layout).setVisibility(View.VISIBLE);
						if(pullType == 2) {
							footer.findViewById(R.id.wrap_layout).setVisibility(View.GONE);
						}

						ImageView v = (ImageView)((LinearLayout)(((RelativeLayout) header).getChildAt(0))).getChildAt(0);
						Animation animation = AnimationUtils.loadAnimation(mCfx, R.anim.rotate_refresh);
						v.startAnimation(animation);
						mListener.pullToRefresh(isRefresh);
//					}
//					else {
//						header.findViewById(R.id.wrap_layout).setVisibility(View.GONE);
//					}
				}					
			}
			
			if(pullType != 0) {
				if((visibleItemCount + firstVisibleItem) <= totalItemCount) {
//					Log.d(TAG, "offset visibleCount == " + visibleCount + " prevVisibleCount == " + prevVisibleCount);					
//					if(visibleItemCount != prevVisibleCount) {
//						prevVisibleCount = visibleItemCount;
					if(pullType == 2) {
						if(!isRefresh) {
							isLoadmore = true;
							setSelection(totalItemCount-1);
							footer.findViewById(R.id.wrap_layout).setVisibility(View.VISIBLE);
							if(pullType == 2) {
								header.findViewById(R.id.wrap_layout).setVisibility(View.GONE);
							}
							ImageView v = (ImageView)((LinearLayout)(((RelativeLayout) footer).getChildAt(0))).getChildAt(0);
							Animation animation = AnimationUtils.loadAnimation(mCfx, R.anim.rotate_refresh);
							v.startAnimation(animation);
							mListener.pullToLoadmore(isLoadmore);																				
						}
					}
					else {
						Log.d(TAG, "offset footer loadmore");
						isLoadmore = true;
						setSelection(totalItemCount-1);
						footer.findViewById(R.id.wrap_layout).setVisibility(View.VISIBLE);
						if(pullType == 2) {
							header.findViewById(R.id.wrap_layout).setVisibility(View.GONE);
						}

						ImageView v = (ImageView)((LinearLayout)(((RelativeLayout) footer).getChildAt(0))).getChildAt(0);
						Animation animation = AnimationUtils.loadAnimation(mCfx, R.anim.rotate_refresh);
						v.startAnimation(animation);
						mListener.pullToLoadmore(isLoadmore);													
//					}
//					else {
//						footer.findViewById(R.id.wrap_layout).setVisibility(View.GONE);
//					}
					}
				}
			}	
		}
		
		if(prevState == 2 && scrollState == 0) {
			Log.d(TAG, "offset footer GONE not load");
			if(pullType != 1) {
				header.findViewById(R.id.wrap_layout).setVisibility(View.GONE);
			}
			
			if(pullType != 0) {
				footer.findViewById(R.id.wrap_layout).setVisibility(View.GONE);
			}
		}

		if(prevState == 1 && scrollState == 1) {
			if(pullType != 1) {
				header.findViewById(R.id.wrap_layout).setVisibility(View.VISIBLE);				
			}
//			Log.d(TAG, "offset footer VISIBLE not load");
			if(pullType != 0) {
				footer.findViewById(R.id.wrap_layout).setVisibility(View.VISIBLE);
			}
		}
		
		if(scrollState == 2) {
			if(pullType != 1) {
				header.findViewById(R.id.wrap_layout).setVisibility(View.GONE);
			}
			
			if(pullType != 0) {
				Log.d(TAG, "offset footer GONE not load");
				footer.findViewById(R.id.wrap_layout).setVisibility(View.GONE);
			}
		}		
		

		if(prevState == 0 && scrollState == 0) {
//			prevVisibleCount = visibleItemCount;
			if(pullType != 1) {
				header.findViewById(R.id.wrap_layout).setVisibility(View.GONE);
			}
		}				

		isOnScrolled = false;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
		if(isOnScrolled) {
			return;
		}
		
		this.prevState = this.scrollState;
		this.scrollState = scrollState;

		isOnScrolled = true;
		
		onScroll(this, this.getFirstVisiblePosition(), this.getLastVisiblePosition()-this.getFirstVisiblePosition()+1, this.getCount());	
/*		if(prevState != 0) {
			if(this.scrollState == 0) {
				onScroll(this, this.getFirstVisiblePosition(), this.getLastVisiblePosition()-this.getFirstVisiblePosition()+1, this.getCount());
			}
		}
		else {
			onScroll(this, this.getFirstVisiblePosition(), this.getLastVisiblePosition()-this.getFirstVisiblePosition()+1, this.getCount());			
		}*/
		
		Log.d(TAG, "offset prevState : " + prevState + " afterState : " + this.scrollState);
		
	}

	public void setPullType(int type) {
		showHeaderFooter(type);
	}
	
	public void setOnPullListener(OnPullListener listener) {
		mListener = listener;
	}
	
	public OnPullListener getOnPullListener() {
		return mListener;
	}	
	
	public void pullToFinish() {
		if(isRefresh) {
			isRefresh = false;
			header.findViewById(R.id.wrap_layout).postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					ImageView v = (ImageView)((LinearLayout)(((RelativeLayout) header).getChildAt(0))).getChildAt(0);
					v.clearAnimation();					
					header.findViewById(R.id.wrap_layout).setVisibility(View.GONE);
					requestLayout();
				}
				
			}, 100);					

		}
		else {
			isLoadmore = false;
			footer.findViewById(R.id.wrap_layout).postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					ImageView v = (ImageView)((LinearLayout)(((RelativeLayout) footer).getChildAt(0))).getChildAt(0);
					v.clearAnimation();
					footer.findViewById(R.id.wrap_layout).setVisibility(View.GONE);
					requestLayout();
				}
				
			}, 100);					

		}
	}
}
