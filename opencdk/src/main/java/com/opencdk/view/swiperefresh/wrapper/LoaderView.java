package com.opencdk.view.swiperefresh.wrapper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.opencdk.R;

/**
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @since 2015-12-16
 * @Modify 2016-1-3
 */
public class LoaderView extends LoaderLayout
{
	
	private final int mDuration = 2000;
	private CharSequence mErrorLabel;
	private ImageView mLeftView;
	private CharSequence mLoadLabel;
	private View mLoadingLayout;
	private View mManualLayout;
	private View mMixLayout;
	private View.OnClickListener mOnMoreClickListener;
	private ImageView mRightView;
	private final float mScaleMax = 1.0f;
	private final float mScaleMin = 0.6f;
	private TextView mTvLoadMore;
	
	public LoaderView(Context paramContext)
	{
		super(paramContext);
		init(paramContext);
	}
	
	public LoaderView(Context paramContext, AttributeSet paramAttributeSet)
	{
		super(paramContext, paramAttributeSet);
		init(paramContext);
	}
	
	private void cancelAnimation()
	{
		this.mLeftView.clearAnimation();
		this.mRightView.clearAnimation();
	}
	
	private void init(Context paramContext)
	{
		this.mLeftView = ((ImageView) findViewById(R.id.iv_left));
		this.mRightView = ((ImageView) findViewById(R.id.iv_right));
		this.mMixLayout = findViewById(R.id.mix_layout);
		this.mManualLayout = findViewById(R.id.manual_layout);
		this.mLoadingLayout = findViewById(R.id.loading_layout);
		this.mTvLoadMore = ((TextView) this.mManualLayout.findViewById(R.id.tv_load));
		this.mTvLoadMore.setOnClickListener(new View.OnClickListener()
		{
			
			public void onClick(View paramAnonymousView)
			{
				if (LoaderView.this.mOnMoreClickListener != null)
				{
					LoaderView.this.pullToRefreshAuto();
					LoaderView.this.mOnMoreClickListener.onClick(paramAnonymousView);
				}
			}
		});
		this.mMixLayout.setVisibility(View.GONE);
		this.mManualLayout.setVisibility(View.GONE);
		this.mLoadingLayout.setVisibility(View.GONE);
		this.mLoadLabel = "正在努力加载...";
	}
	
	private void loadAnimation()
	{
		ScaleAnimation leftAnim = new ScaleAnimation(mScaleMax, mScaleMin, mScaleMax, mScaleMin,
				Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);
		leftAnim.setDuration(mDuration);
		leftAnim.setRepeatCount(0);
		leftAnim.setInterpolator(new AccelerateDecelerateInterpolator());
		this.mLeftView.setAnimation(leftAnim);
		this.mLeftView.startAnimation(leftAnim);
		leftAnim.setAnimationListener(new Animation.AnimationListener()
		{
			
			public void onAnimationEnd(Animation paramAnonymousAnimation)
			{
				ScaleAnimation leftAnim = new ScaleAnimation(mScaleMin, mScaleMax, mScaleMin, mScaleMax,
						Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);
				leftAnim.setDuration(mDuration);
				leftAnim.setRepeatCount(0);
				leftAnim.setInterpolator(new AccelerateDecelerateInterpolator());
				LoaderView.this.mLeftView.setAnimation(leftAnim);
				LoaderView.this.mLeftView.startAnimation(leftAnim);
				leftAnim.setAnimationListener(new Animation.AnimationListener()
				{
					
					public void onAnimationEnd(Animation paramAnonymous2Animation)
					{
						LoaderView.this.loadAnimation();
					}
					
					public void onAnimationRepeat(Animation paramAnonymous2Animation)
					{
					}
					
					public void onAnimationStart(Animation paramAnonymous2Animation)
					{
					}
				});
			}
			
			public void onAnimationRepeat(Animation paramAnonymousAnimation)
			{
			}
			
			public void onAnimationStart(Animation paramAnonymousAnimation)
			{
			}
		});
		ScaleAnimation rightAnim = new ScaleAnimation(mScaleMin, mScaleMax, mScaleMin, mScaleMax,
				Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 0.5f);
		rightAnim.setDuration(mDuration);
		rightAnim.setRepeatCount(0);
		rightAnim.setInterpolator(new AccelerateDecelerateInterpolator());
		this.mRightView.setAnimation(rightAnim);
		this.mRightView.startAnimation(rightAnim);
		rightAnim.setAnimationListener(new Animation.AnimationListener()
		{
			
			public void onAnimationEnd(Animation paramAnonymousAnimation)
			{
				ScaleAnimation rightAnim = new ScaleAnimation(mScaleMax, mScaleMin, mScaleMax, mScaleMin,
						Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 0.5f);
				rightAnim.setDuration(mDuration);
				rightAnim.setRepeatCount(0);
				rightAnim.setInterpolator(new AccelerateDecelerateInterpolator());
				LoaderView.this.mRightView.setAnimation(rightAnim);
				LoaderView.this.mRightView.startAnimation(rightAnim);
			}
			
			public void onAnimationRepeat(Animation paramAnonymousAnimation)
			{
			}
			
			public void onAnimationStart(Animation paramAnonymousAnimation)
			{
			}
		});
	}
	
	public final void hideAllViews()
	{
	}
	
	public View inflateLoaderView(ViewGroup paramViewGroup)
	{
		return LayoutInflater.from(getContext()).inflate(R.layout.sr_loader_view, paramViewGroup, true);
	}
	
	public void onError(String paramString)
	{
	}
	
	public void onShow(int paramInt, String paramString)
	{
		if (getVisibility() != View.VISIBLE)
		{
			setVisibility(View.VISIBLE);
		}
		cancelAnimation();
		this.mLeftView.setVisibility(View.GONE);
		this.mRightView.setVisibility(View.GONE);
	}
	
	public void onSuccess()
	{
		
	}
	
	protected void onWindowVisibilityChanged(int visibility)
	{
		super.onWindowVisibilityChanged(visibility);
		if (this.mStatus == View.VISIBLE)
		{
			loadAnimation();
		}
	}
	
	public final void pullToRefreshAuto()
	{
		if (this.mLoadingLayout.getVisibility() != View.VISIBLE)
		{
			this.mLoadingLayout.setVisibility(View.VISIBLE);
		}
		this.mManualLayout.setVisibility(View.GONE);
		this.mStatus = STATUS_LOADING;
		this.mLeftView.setVisibility(View.VISIBLE);
		this.mRightView.setVisibility(View.VISIBLE);
		loadAnimation();
	}
	
	public final void pullToRefreshManual()
	{
		if (this.mStatus == STATUS_LOADING)
		{
			return;
		}
		if (this.mManualLayout.getVisibility() != View.VISIBLE)
		{
			this.mManualLayout.setVisibility(View.VISIBLE);
		}
		
		this.mLoadingLayout.setVisibility(View.GONE);
	}
	
	public void reset()
	{
		this.mStatus = STATUS_IDLE;
		if (this.mLoaderStyle == LOADER_STYLE_AUTO)
		{
			this.mLoadingLayout.setVisibility(View.GONE);
			this.mManualLayout.setVisibility(View.GONE);
		}
		else if (this.mLoaderStyle != LOADER_STYLE_MANUAL)
		{
			this.mLoadingLayout.setVisibility(View.GONE);
			this.mManualLayout.setVisibility(View.VISIBLE);
		}
	}

	public void setOnMoreButtonClick(View.OnClickListener paramOnClickListener)
	{
		this.mOnMoreClickListener = paramOnClickListener;
	}
	
	public void startLoading()
	{
		this.mMixLayout.setVisibility(View.VISIBLE);
		if (this.mLoaderStyle == LOADER_STYLE_AUTO)
		{
			pullToRefreshAuto();
		}
		else if (this.mLoaderStyle != LOADER_STYLE_MANUAL)
		{
			pullToRefreshManual();
		}
	}
	
	public void stopLoading()
	{
		this.mStatus = STATUS_IDLE;
	}
	
}