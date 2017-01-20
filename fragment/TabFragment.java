package com.example.helloworld.ouhkfyp.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.helloworld.ouhkfyp.R;
import com.example.helloworld.ouhkfyp.adapter.TabFragmentPagerAdapter;
import com.example.helloworld.ouhkfyp.tab.BaseFragment;
import com.example.helloworld.ouhkfyp.tab.SlidingTabLayout;

import java.util.LinkedList;

public class TabFragment extends Fragment {

	private SlidingTabLayout tabs;
	private ViewPager pager;
	private FragmentPagerAdapter adapter;
	
	public static Fragment newInstance(){
		TabFragment f = new TabFragment();
		return f;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.frg_tab, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		//adapter
		final LinkedList<BaseFragment> fragments = getFragments();
		adapter = new TabFragmentPagerAdapter(getFragmentManager(), fragments);
		//pager
		pager = (ViewPager) view.findViewById(R.id.pager);
		pager.setAdapter(adapter);
		//tabs
		tabs = (SlidingTabLayout) view.findViewById(R.id.tabs);
		tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
			
			@Override
			public int getIndicatorColor(int position) {
				return fragments.get(position).getIndicatorColor();
			}
			
			@Override
			public int getDividerColor(int position) {
				return fragments.get(position).getDividerColor();
			}
		});
		tabs.setBackgroundResource(R.color.color_primary);
		tabs.setCustomTabView(R.layout.tab_title, R.id.txtTabTitle, R.id.imgTabIcon);
		tabs.setViewPager(pager);
		
	}
	
	private LinkedList<BaseFragment> getFragments(){
		int indicatorColor = Color.parseColor(this.getResources().getString(R.color.color_accent));
		int dividerColor = Color.TRANSPARENT;
		
		LinkedList<BaseFragment> fragments = new LinkedList<BaseFragment>();
		fragments.add(FreshFoodFragment.newInstance("Fresh Food", indicatorColor, dividerColor, R.drawable.freshfood));
		fragments.add(FrozenFoodFragment.newInstance("Frozen Food", Color.BLACK, dividerColor, R.drawable.frozenfood));
		fragments.add(GroceryFragment.newInstance("Grocery", Color.GREEN, dividerColor, R.drawable.grocery));
		fragments.add(DrinkFragment.newInstance("Drink", Color.GREEN, dividerColor, R.drawable.drink));
		// fragments.add(CookFragment.newInstance("Cook", Color.GREEN, dividerColor, android.R.drawable.ic_dialog_map));
		// fragments.add(FoodFragment.newInstance("Food", Color.RED, dividerColor, android.R.drawable.ic_dialog_email));
		// fragments.add(GoodFragment.newInstance("Good", Color.BLUE, dividerColor, android.R.drawable.ic_lock_power_off));
		// fragments.add(LookFragment.newInstance("Look", Color.CYAN, dividerColor, android.R.drawable.ic_dialog_dialer));
		// fragments.add(WoodFragment.newInstance("Wood", Color.MAGENTA, dividerColor, android.R.drawable.ic_media_play));
		return fragments;
	}
	
}
