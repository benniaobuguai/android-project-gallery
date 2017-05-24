package com.wo2b.gallery.global.provider;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.wo2b.gallery.business.base.DatabaseHelper;
import com.wo2b.gallery.business.image.AlbumBiz;
import com.wo2b.gallery.business.image.Module;
import com.wo2b.gallery.model.image.AlbumInfo;
import com.wo2b.wrapper.app.support.GroupExt;

/**
 * Data Provider.
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * 
 */
public final class DataProvider
{
	
	private static final String TAG = "Data.DataProvider";
	
	// ----------------------------------------------------------------------------------------
	// 针对数据进行优化的方案
	// 1. 加载首页相册数据, 全部缓存起来
	// 2. 加载广场数据, 全部缓存起来
	// 3. 加载本地相册, 全部缓存, 并且监听媒体数据变化, 实时后台更新数据
	// 4. 插件库, 暂时选择的方案是每次查询的时候就进行遍历
	// 5.
	// ----------------------------------------------------------------------------------------
	
	private static final byte[] mLock = new byte[0];
	private static final byte[] mCategoryLock = new byte[0];
	private static final byte[] mChapterLock = new byte[0];
	
	private static volatile DataProvider mInstance = null;
	
	private Context mContext;
	private List<AlbumInfo> mCategoryList;
	
	// ----------------------------------------------------------------------------------------
	// &&&&&&&&&&&&&& 章节内容
	
	private List<GroupExt<AlbumInfo>> mChapterContentList;
	// ----------------------------------------------------------------------------------------
	
	/**
	 * 私有构造函数
	 * 
	 * @param application
	 */
	private DataProvider()
	{
		mCategoryList = new ArrayList<AlbumInfo>();
		mChapterContentList = new ArrayList<GroupExt<AlbumInfo>>();
	}

	/**
	 * 返回DataProvider对象
	 * 
	 * @param context
	 * @return
	 */
	public static DataProvider getInstance()
	{
		if (mInstance == null)
		{
			synchronized (mLock)
			{
				if (mInstance == null)
				{
					mInstance = new DataProvider();
				}
			}
		}
		
		return mInstance;
	}
	
	public void init(Context application)
	{
		this.mContext = application;
	}
	
	/**
	 * 加载首页分类
	 */
	public void loadCategoryList(Context context)
	{
		synchronized (mCategoryLock)
		{
			// Log.I(TAG, "Load category start...");
			// SystemClock.sleep(10 * 1000);
			AlbumBiz albumBiz = new AlbumBiz(DatabaseHelper.getDatabaseHelper(context));
			mCategoryList = albumBiz.queryByModule(Module.A);
			// Log.I(TAG, "Load category end...");
		}
	}
	
	/**
	 * 返回分类列表
	 * 
	 * @return
	 */
	public List<AlbumInfo> getCategoryList(Context context)
	{
		if (mCategoryList == null || mCategoryList.isEmpty())
		{
			loadCategoryList(context);
		}
		synchronized (mCategoryLock)
		{
			return mCategoryList;
		}
	}
	
	/**
	 * 加载广场相册
	 */
	public void loadChapterList(Context context)
	{
		AlbumBiz albumBiz = new AlbumBiz(DatabaseHelper.getDatabaseHelper(context));
		// 获取所有组名, 去重复, 得到实质分组.
		List<AlbumInfo> albumList = albumBiz.queryAllCategory(Module.H);
		
		// 迭代所有分组的ID
		
		int count = albumList.size();
		AlbumInfo albumInfo = null;
		List<AlbumInfo> tempList = null;
		
		GroupExt<AlbumInfo> groupExt = null;
		for (int i = 0; i < count; i++)
		{
			albumInfo = albumList.get(i);
			
			groupExt = new GroupExt<AlbumInfo>();
			groupExt.setId(i);
			groupExt.setGroupName(albumInfo.getCategory());
			groupExt.setGroup(true);
			mChapterContentList.add(groupExt);
			
			tempList = albumBiz.queryByCategory(albumInfo.getCategory());
			if (tempList == null || tempList.isEmpty())
			{
				continue;
			}
			
			int itemCount = tempList.size();
			
			// just test code.
//				itemCount = 2;
			for (int j = 0; j < itemCount; j++)
			{
				albumInfo = tempList.get(j);
				
				// just test code.
				// albumInfo.setName(albumInfo.getName() + "_" + j);
				
				
				groupExt = new GroupExt<AlbumInfo>();
				groupExt.setId(j);
				groupExt.setGroup(false);
				groupExt.setValue(albumInfo);
				
				mChapterContentList.add(groupExt);
			}
			
			
//				// TODO: test code.				
//				groupExt = new GroupExt<AlbumInfo>();
//				groupExt.setId(i);
//				groupExt.setGroupName("分组二");
//				groupExt.setGroup(true);
//				mChapterContentList.add(groupExt);
//				
//				itemCount = 3;
//				
//				for (int j = 0; j < itemCount; j++)
//				{
//					albumInfo = tempList.get(j);
//					
//					groupExt = new GroupExt<AlbumInfo>();
//					groupExt.setId(j);
//					groupExt.setGroup(false);
//					groupExt.setValue(albumInfo);
//					
//					mChapterContentList.add(groupExt);
//				}
//				
//				groupExt = new GroupExt<AlbumInfo>();
//				groupExt.setId(i);
//				groupExt.setGroupName("分组三");
//				groupExt.setGroup(true);
//				mChapterContentList.add(groupExt);
//
//				for (int j = 0; j < itemCount; j++)
//				{
//					albumInfo = tempList.get(j);
//					
//					groupExt = new GroupExt<AlbumInfo>();
//					groupExt.setId(j);
//					groupExt.setGroup(false);
//					groupExt.setValue(albumInfo);
//					
//					mChapterContentList.add(groupExt);
//				}
			
			
		}
	}
	
	/**
	 * 返回广场相册
	 * 
	 * @return
	 */
	public List<GroupExt<AlbumInfo>> getChapterContentList(Context context)
	{
		synchronized (mChapterLock)
		{
			if (mChapterContentList == null || mChapterContentList.isEmpty())
			{
				loadChapterList(context);
			}
			
			return mChapterContentList;
		}
	}
	
	/**
	 * 返回广场相册
	 * 
	 * @return
	 */
	public List<GroupExt<AlbumInfo>> getChapterContentListSington(Context context)
	{
		synchronized (mChapterLock)
		{
			GroupExt<AlbumInfo> groupExt = new GroupExt<AlbumInfo>();
			groupExt.setId(123);
			groupExt.setGroupName("分组T");
			groupExt.setGroup(true);
			
			List<GroupExt<AlbumInfo>> mChapterContentList = new ArrayList<GroupExt<AlbumInfo>>();
			mChapterContentList.add(groupExt);
			
			AlbumBiz albumBiz = new AlbumBiz(DatabaseHelper.getDatabaseHelper(context));
			// 获取所有组名, 去重复, 得到实质分组.
			List<AlbumInfo> albumList = albumBiz.queryAllCategory(Module.H);
			
			// 迭代所有分组的ID
			int count = albumList.size();
			AlbumInfo albumInfo = null;
			List<AlbumInfo> tempList = null;
			
			for (int i = 0; i < count; i++)
			{
				albumInfo = albumList.get(i);
				
				tempList = albumBiz.queryByCategory(albumInfo.getCategory());
				if (tempList == null || tempList.isEmpty())
				{
					continue;
				}
				
				int itemCount = tempList.size();
				
				// just test code.
				 itemCount = 3;
				
				for (int j = 0; j < itemCount; j++)
				{
					albumInfo = tempList.get(j);
					
					// just test code.
					albumInfo.setName(albumInfo.getName() + "_" + j);
					
					
					groupExt = new GroupExt<AlbumInfo>();
					groupExt.setId(j);
					groupExt.setGroup(false);
					groupExt.setValue(albumInfo);
					
					mChapterContentList.add(groupExt);
				}
			}
			
			return mChapterContentList;
		}
	}
	
}