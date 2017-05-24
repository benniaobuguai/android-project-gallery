package com.wo2b.wrapper.component.user;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.opencdk.util.log.Log;
import com.opencdk.view.swiperefresh.RecyclerViewAdapter.RecyclerViewHolder;
import com.wo2b.wrapper.R;
import com.wo2b.wrapper.app.BaseFragmentListActivity;
import com.wo2b.wrapper.app.support.SimpleParams;
import com.wo2b.wrapper.app.support.XModel;
import com.wo2b.xxx.webapp.manager.user.Grade;
import com.wo2b.xxx.webapp.manager.user.GradeManager;
import com.wo2b.xxx.webapp.manager.user.User;
import com.wo2b.xxx.webapp.manager.user.UserManager;

/**
 * 用户所有等级信息
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * 
 */
public class UserGradeListActivity extends BaseFragmentListActivity<Grade> implements View.OnClickListener
{
	
	private UserManager mUserManager;
	
	private int mCurrentExp = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		Log.I("info", "UserGradeListActivity-->onCreate1()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wrapper_user_grade_list);
		Log.I("info", "UserGradeListActivity-->onCreate2()");
		
		setActionBarTitle(R.string.wo2b_grade);
		
		mUserManager = UserManager.getInstance();
		if (mUserManager.isLogin())
		{
			User user = mUserManager.getMemoryUser();
			mCurrentExp = (int) user.getExp();
		}
		
		SimpleParams params = new SimpleParams();
		realExecuteFirstTime(params);
	}
	
	@Override
	public void onClick(View v)
	{
		// 增加获取积分的显示
	}
	
	@Override
	public View realInflate(ViewGroup parent, int viewType)
	{
		return getLayoutInflater().inflate(R.layout.wrapper_user_grade_list_item, parent, false);
	}
	
	@Override
	public void realOnBindViewHolder(RecyclerViewHolder holder, int position)
	{
		ImageView iv_level_icon = holder.findViewById(R.id.iv_level_icon);
		TextView tv_grade_name = holder.findViewById(R.id.tv_grade_name);
		TextView tv_point_range = holder.findViewById(R.id.tv_point_range);
		ProgressBar pb_exp = holder.findViewById(R.id.pb_exp);
		
		Grade grade = realGetItem(position);
		String range_string = getString(R.string.grade_exp_range, grade.getOffset(), grade.getEnd());
		int level_icon = getResources().getIdentifier("level_" + (++position), "drawable", this.getPackageName());
		iv_level_icon.setImageResource(level_icon);
		
		tv_grade_name.setText(grade.getGradeName());
		tv_point_range.setText(range_string);
		
		if (mCurrentExp >= grade.getOffset() && mCurrentExp <= grade.getEnd())
		{
			pb_exp.setVisibility(View.VISIBLE);
			pb_exp.setMax(grade.getEnd() - grade.getOffset());
			pb_exp.setProgress(mCurrentExp - grade.getOffset());
			
			tv_grade_name.getPaint().setFakeBoldText(true);
		}
		else
		{
			pb_exp.setVisibility(View.GONE);
			
			tv_grade_name.getPaint().setFakeBoldText(false);
		}
		
	}
	
	@Override
	protected XModel<Grade> realOnPullDown(SimpleParams params)
	{
		return XModel.list(GradeManager.getGradeList());
	}
	
	@Override
	protected XModel<Grade> realOnPullUp(SimpleParams params)
	{
		return null;
	}
	
}