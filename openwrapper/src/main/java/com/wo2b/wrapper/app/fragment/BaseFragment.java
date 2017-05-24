package com.wo2b.wrapper.app.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.opencdk.bus.CDKBus;
import com.opencdk.util.log.Log;
import com.wo2b.wrapper.R;
import com.wo2b.wrapper.app.BaseFragmentActivity;
import com.wo2b.wrapper.view.dialog.XToast;

/**
 * BaseFragment
 * 
 * @author 笨鸟不乖
 * 
 */
public class BaseFragment extends Fragment
{
	
	private static final String TAG = "Wrapper.Fragment";
	
	// --------------------------------------------------------------------
	// ------------------------- Common message code ----------------------
	
	// --------------------------------------------------------------------
	
	protected Context mContext;
	
	private XToast mXToast;
	
	/** Ui Handler. */
	private Handler mUiHandler;
	
	/** Sub/Worker Handler. */
	private Handler mSubHandler;
	
	/** Sub/Worker Thread. */
	private HandlerThread mSubThread;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		
		mSubThread = new HandlerThread(this.getClass().getName());
		// mSubThread.setPriority(Thread.MIN_PRIORITY); //设置线程优先级
		mSubThread.start();
		
		mUiHandler = new Handler(getActivity().getMainLooper(), mUiHandlerCallback);
		
		mSubHandler = new Handler(mSubThread.getLooper(), mSubHandlerCallback);
	}
	
	// &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& ActionBar Handler &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
	
	/**
	 * 返回ActionBarActivity
	 * 
	 * @return
	 */
	public ActionBarActivity getActionBarActivity()
	{
		return ((ActionBarActivity) getActivity());
	}
	
	/**
	 * 设置标题
	 * 
	 * @param resId
	 */
	public void setActionBarTitle(int resId)
	{
		getActionBarActivity().getSupportActionBar().setTitle(resId);
	}
	
	/**
	 * 设置标题
	 * 
	 * @param title
	 */
	public void setActionBarTitle(CharSequence title)
	{
		getActionBarActivity().getSupportActionBar().setTitle(title);
	}
	
	/**
	 * 设置Home图标
	 * 
	 * @param resId
	 */
	public void setActionBarIcon(int resId)
	{
		getActionBarActivity().getSupportActionBar().setIcon(resId);
	}
	
	/**
	 * 设置Home图标
	 * 
	 * @param icon
	 */
	public void setActionBarIcon(Drawable icon)
	{
		getActionBarActivity().getSupportActionBar().setIcon(icon);
	}
	
	/**
	 * 设置显示方式
	 * 
	 * @param options
	 * @param mask
	 */
	public void setActionBarDisplayOptions(int options, int mask)
	{
		getActionBarActivity().getSupportActionBar().setDisplayOptions(options, mask);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == android.R.id.home)
		{
			// 默认对Home区域做"返回"处理.
			onActionBarBackClick();
		}
		else if (item.getItemId() == R.id.setting)
		{
			onActionBarSettingClick();
		}
		else if (item.getItemId() == R.id.add)
		{
			onActionBarAddClick();
		}
		else if (item.getItemId() == R.id.edit)
		{
			onActionBarEditClick();
		}
		else if (item.getItemId() == R.id.ok)
		{
			onActionBarOkClick();
		}
		else if (item.getItemId() == R.id.menu)
		{
			onActionBarMenuClick();
		}
		else if (item.getItemId() == R.id.search)
		{
			onActionBarSearchClick();
		}
		
		return super.onOptionsItemSelected(item);
	}

	/**
	 * ActionBar的返回操作, 依赖于是否已经重写onActionBarHomeClick().
	 * onActionBarHomeClick()返回true, 则表示已经消费引事件, 反之, 则由父类统一处理.
	 */
	protected void onActionBarBackClick()
	{
		if (!onActionBarHomeClick())
		{
			getActionBarActivity().finish();
		}
	}

	/**
	 * 点击Home
	 */
	protected boolean onActionBarHomeClick()
	{
		return false;
	}

	/**
	 * 点击设置
	 */
	protected void onActionBarSettingClick()
	{

	}

	/**
	 * 点击添加
	 */
	protected void onActionBarAddClick()
	{

	}

	/**
	 * 点击编辑
	 */
	protected void onActionBarEditClick()
	{

	}

	/**
	 * 点击确认
	 */
	protected void onActionBarOkClick()
	{

	}

	/**
	 * 点击菜单
	 */
	protected void onActionBarMenuClick()
	{

	}

	/**
	 * 点击搜索
	 */
	protected void onActionBarSearchClick()
	{
		
	}
	
	/**
	 * 是否需要显示ActionBar, hide()后会延时一会再完全消失.
	 * 
	 * @return
	 */
	protected boolean hasActionBar()
	{
		return true;
	}
	
	/**
	 * Common Resource会包含子系统的通用功能模块, 需要获取子应用的预先设定的图标; 如果子系统没有设定好图标, 则使用默认Logo.
	 * 
	 * @return
	 */
	public int getApplicationActionBarIcon()
	{
		int drawableId = -1;
		try
		{
			drawableId = this.getResources().getIdentifier("actionbar_icon", "drawable", getActivity().getPackageName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			drawableId = R.drawable.wo2b_logo;
		}

		return drawableId;
	}

	// &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& ActionBar Handler &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

	@Override
	public void onResume()
	{
		super.onResume();

		if (busEventEnable())
		{
			try
			{
				CDKBus.register(this);
			}
			catch (IllegalArgumentException e)
			{
				// 避免重复注册时会发生异常
				// e.printStackTrace();
			}
		}
	}

	@Override
	public void onPause()
	{
		super.onPause();
	}

	@Override
	public void onDestroyView()
	{
		super.onDestroyView();
	}

	@Override
	public void onDestroy()
	{
		// POINTAT: 待实质验证
		if (busEventEnable())
		{
			CDKBus.unregister(this);
		}
		
		if (mSubThread != null)
		{
			mSubThread.quit();
			mSubThread = null;
		}
		
		super.onDestroy();
	}
	
	/**
	 * 是否启用事件总线, 默认不启用
	 * 
	 * @return
	 */
	protected boolean busEventEnable()
	{
		return false;
	}
	
	/**
	 * 返回对应的Activity实例, , 对应于 {@link ActionBarActivity}
	 * 
	 * @return
	 */
	public final ActionBarActivity getSupportActivity()
	{
		return (ActionBarActivity) getActivity();
	}
	
	public Context getContext()
	{
		return mContext;
	}
	
	protected Handler getUiHandler()
	{
		return mUiHandler;
	}
	
	protected Handler getSubHandler()
	{
		return mSubHandler;
	}
	
	private Handler.Callback mUiHandlerCallback = new Handler.Callback()
	{

		@Override
		public boolean handleMessage(Message msg)
		{
			return uiHandlerCallback(msg);
		}
	};

	private Handler.Callback mSubHandlerCallback = new Handler.Callback()
	{

		@Override
		public boolean handleMessage(Message msg)
		{
			return subHandlerCallback(msg);
		}
	};

	/**
	 * UI更新请求消息处理函数
	 * 
	 * 子类可重载此函数，已处理自己的私有消息
	 * 
	 * @param msg
	 * @return
	 */
	protected boolean uiHandlerCallback(Message msg)
	{
		Log.D(TAG, "uiHandlerCallback");
		return false;
	}

	/**
	 * 与界面无关的操作消息处理函数
	 * 
	 * 子类可重载此函数，已处理自己的私有消息
	 * 
	 * @param msg
	 * @return
	 */
	protected boolean subHandlerCallback(Message msg)
	{
		Log.D(TAG, "subHandlerCallback");
		return false;
	}

	/**
	 * Init view.
	 */
	protected void initView(View view)
	{

	}

	/**
	 * Bind events to target view.
	 */
	protected void bindEvents()
	{

	}

	public void showToast(String info)
	{
		if (getActivity() == null || !isAdded())
		{
			return;
		}
		
		if (!(getActionBarActivity() instanceof BaseFragmentActivity))
		{
			Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT).show();
			
			return;
		}
		
		if (mXToast == null)
		{
			mXToast = XToast.makeToast(getActivity(), info, XToast.LENGTH_SHORT);
		}
		else
		{
			mXToast.setText(info);
		}
		
		// XXX: 后续根据实际使用场景进行扩展, toolbar处于隐藏的状态下, 如何控制等问题
		Toolbar toolbar = ((BaseFragmentActivity) getActionBarActivity()).getToolbar();
		if (toolbar != null/* &&toolbar.isShown() */)
		{
			mXToast.show(toolbar);
		}
		else
		{
			Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT).show();
		}
	}
	
	public void showToast(int resId)
	{
		showToast(getActivity().getResources().getString(resId));
	}
	
	public void showToastOnUiThread(final String info)
	{
		if (getActivity() == null || !isAdded())
		{
			return;
		}
		
		getActivity().runOnUiThread(new Runnable()
		{
			
			@Override
			public void run()
			{
				showToast(info);
			}
		});
	}
	
	public void showToastOnUiThread(final int resId)
	{
		showToastOnUiThread(getActivity().getResources().getString(resId));
	}
	
	public void showToastOnUiThread(final int code, final String message)
	{
		showToastOnUiThread("[" + code + ", " + message + "]");
	}

	// --------------- Normal Functions End ---------------------------------
	
	
	// --------------- System Override Start ---------------------------------
	/**
	 * 扩展Android系统内部的部分方法, 方便编码.
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends View> T findViewByIdExt(View view, int id)
	{
		return (T) view.findViewById(id);
	}
	
	// --------------- System Override End ---------------------------------

}
