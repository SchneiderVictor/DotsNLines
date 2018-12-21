package com.example.schne.dotsnlines.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.example.schne.dotsnlines.R;

public class GameInitializationFragment extends Fragment implements View.OnClickListener {
	private NumberPicker boardSizePicker;
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_game_initialization, container, false);
		
		boardSizePicker = view.findViewById(R.id.board_size_picker);
		
		// initialize the NumberPicker
		boardSizePicker.setMinValue(5);
		boardSizePicker.setMaxValue(10);
		boardSizePicker.setWrapSelectorWheel(false);
		boardSizePicker.setOnClickListener(this);
		
		return view;
	}
	
	@Override
	public void onClick(View view) {
	
	}
	
	public NumberPicker getBoardSizePicker() {
		return boardSizePicker;
	}
}
