package com.example.schne.dotsnlines.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.schne.dotsnlines.R;
import com.example.schne.dotsnlines.fragments.Page1Fragment;
import com.example.schne.dotsnlines.fragments.Page2Fragment;
import com.example.schne.dotsnlines.fragments.Page3Fragment;
import com.example.schne.dotsnlines.fragments.Page4Fragment;
import com.example.schne.dotsnlines.fragments.Page5Fragment;
import com.example.schne.dotsnlines.fragments.Page6Fragment;

/**
 * FragmentPagerAdapter used for the Resume page
 */
public class HowToPlayFragmentPager extends FragmentPagerAdapter {
	
	private Context mainContext;
	
	public HowToPlayFragmentPager(FragmentManager fragmentManager, Context context) {
		super(fragmentManager);
		mainContext = context;
	}
	
	/**
	 * This determines the fragment for each tab
	 *
	 * @param i tab index
	 * @return Fragment represented by tab i
	 */
	@Override
	public Fragment getItem(int i) {
		switch (i) {
			case 0:
				return new Page1Fragment();
			case 1:
				return new Page2Fragment();
			case 2:
				return new Page3Fragment();
			case 3:
				return new Page4Fragment();
			case 4:
				return new Page5Fragment();
			case 5:
				return new Page6Fragment();
			default:
				throw new IndexOutOfBoundsException();
		}
	}
	
	@Override
	public int getCount() {
		return 6;
	}
}
