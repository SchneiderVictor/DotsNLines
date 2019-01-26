package com.example.schne.dotsnlines.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.schne.dotsnlines.R;

public class Page6Fragment extends Fragment {
	
	public Page6Fragment() {
	}
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.fragment_instruct_6, container, false);
	}
}
