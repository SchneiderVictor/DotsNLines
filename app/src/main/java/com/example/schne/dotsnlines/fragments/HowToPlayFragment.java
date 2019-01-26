package com.example.schne.dotsnlines.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.schne.dotsnlines.R;
import com.example.schne.dotsnlines.listeners.OnViewPagerReadyListener;

public class HowToPlayFragment extends Fragment {
	private ViewPager viewPager;
	private OnViewPagerReadyListener listener;
	
	public HowToPlayFragment() {
	}
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_how_to_play, container, false);
		
		this.viewPager = view.findViewById(R.id.instructions_view_pager);
		
		this.listener.onViewPagerReady(getChildFragmentManager());
		
		return view;
	}
	
	public void setListener(OnViewPagerReadyListener listener) {
		this.listener = listener;
	}
	
	public ViewPager getViewPager() {
		return viewPager;
	}
}
