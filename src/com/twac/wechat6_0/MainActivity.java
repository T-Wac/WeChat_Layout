package com.twac.wechat6_0;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.Window;

public class MainActivity extends FragmentActivity implements OnClickListener,
		OnPageChangeListener {

	private ViewPager mViewPager;
	private List<Fragment> mTabs = new ArrayList<Fragment>();
	private String[] mTitles = new String[] { "First Fragment",
			"Second Fragment", "Third Fragment", "Fourth Fragment" };
	private FragmentPagerAdapter mAdapter;
	private List<ChangeColorIconWithText> mTabIndicator = new ArrayList<ChangeColorIconWithText>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		setOverflowButtonAlways();
		getActionBar().setDisplayShowHomeEnabled(false);
		
		initView();
		initDatas();
		initEvent();
		
		mViewPager.setAdapter(mAdapter);
	}

	// 初始化所有事件
	private void initEvent() {
		mViewPager.setOnPageChangeListener(this);
	}

	private void initDatas() {
		for (String title : mTitles) {
			TabFragment tabFragment = new TabFragment();
			Bundle bundle = new Bundle();
			bundle.putString(TabFragment.TITLE, title);
			tabFragment.setArguments(bundle);
			mTabs.add(tabFragment);
		}

		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {

				return mTabs.size();
			}

			@Override
			public Fragment getItem(int position) {

				return mTabs.get(position);
			}
		};
	}

	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

		ChangeColorIconWithText one = (ChangeColorIconWithText) findViewById(R.id.indicator_1);
		mTabIndicator.add(one);
		ChangeColorIconWithText two = (ChangeColorIconWithText) findViewById(R.id.indicator_2);
		mTabIndicator.add(two);
		ChangeColorIconWithText three = (ChangeColorIconWithText) findViewById(R.id.indicator_3);
		mTabIndicator.add(three);
		ChangeColorIconWithText four = (ChangeColorIconWithText) findViewById(R.id.indicator_4);
		mTabIndicator.add(four);

		one.setOnClickListener(this);
		two.setOnClickListener(this);
		three.setOnClickListener(this);
		four.setOnClickListener(this);

		one.setIconAlpha(1.0f);
	}

	private void setOverflowButtonAlways() {

		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKey = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			menuKey.setAccessible(true);
			menuKey.setBoolean(config, false);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 设置menu显示icon

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {

		if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
			if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
				try {
					Method m = menu.getClass().getDeclaredMethod(
							"setOptionalIconsVisible", Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu, true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return super.onMenuOpened(featureId, menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	
	@Override
	public void onClick(View v) {
		clickTabs(v);
		
	}

	private void clickTabs(View v) {
		resetOtherTabs();
		switch (v.getId()) {
		case R.id.indicator_1:
			mTabIndicator.get(0).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(0, false);
			break;
		case R.id.indicator_2:
			mTabIndicator.get(1).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(1, false);
			break;
		case R.id.indicator_3:
			mTabIndicator.get(2).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(2, false);
			break;
		case R.id.indicator_4:
			mTabIndicator.get(3).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(03, false);
			break;
		default:
			break;
		}
		
	}

	// 重置其他Tab Indicator的颜色
	private void resetOtherTabs() {
		for (int i = 0; i < mTabIndicator.size(); i++) {
			mTabIndicator.get(i).setIconAlpha(0);
		}
	}

	@Override
	public void onPageScrollStateChanged(int position) {

	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		// Log.e("TAG", "position = " + position + "positionOffset = "
		// + positionOffset);
		if (positionOffset > 0) {
			ChangeColorIconWithText left = mTabIndicator.get(position);
			ChangeColorIconWithText right = mTabIndicator.get(position + 1);

			left.setIconAlpha(1 - positionOffset);
			right.setIconAlpha(positionOffset);
		}

	}

	@Override
	public void onPageSelected(int position) {

	}

}
