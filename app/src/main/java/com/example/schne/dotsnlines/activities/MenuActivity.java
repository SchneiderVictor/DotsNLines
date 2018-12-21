package com.example.schne.dotsnlines.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.schne.dotsnlines.R;
import com.example.schne.dotsnlines.fragments.GameInitializationFragment;
import com.example.schne.dotsnlines.fragments.HomeMenuFragment;
import com.example.schne.dotsnlines.fragments.HowToPlayFragment;

public class MenuActivity extends AppCompatActivity {
	private HomeMenuFragment homeMenuFragment = new HomeMenuFragment();
	private GameInitializationFragment gameInitializationFragment = new GameInitializationFragment();
	private HowToPlayFragment howToPlayFragment = new HowToPlayFragment();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
		setFragment(homeMenuFragment);
	}
	
	/**
	 * Updates the UI with the specified Fragment
	 *
	 * @param fragment the new Fragment object to update the UI with
	 */
	private void setFragment(Fragment fragment) {
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		
		// set fragment animations
		fragmentTransaction.setCustomAnimations(
				R.animator.float_in,
				R.animator.float_out,
				R.animator.fall_in,
				R.animator.fall_out);
		
		fragmentTransaction.replace(R.id.frame_menu, fragment);
		
		// manage Back Stack after replace() call to prevent unwanted animations and display
		fragmentTransaction.addToBackStack(null);
		
		fragmentTransaction.commit();
	}
	
	public void initializeGame(View view) {
		setFragment(gameInitializationFragment);
	}
	
	public void viewInstructions(View view) {
		setFragment(howToPlayFragment);
	}
	
	public void startGame(View view) {
		Intent intent = new Intent(this, GameActivity.class);
		int boardSize = gameInitializationFragment.getBoardSizePicker().getValue();
		
		intent.putExtra("boardSize", boardSize);
		
		startActivity(intent);
	}
}
