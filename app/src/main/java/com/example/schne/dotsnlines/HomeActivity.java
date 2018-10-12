package com.example.schne.dotsnlines;

import android.animation.Animator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

/**
 * The Android Launcher Activity
 *
 * Used to allow the players to initialize their game
 */
public class HomeActivity extends AppCompatActivity {
	TextView promptView;
	NumberPicker numberPickerView;
	Button sizeButton;
	Button startButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		promptView = findViewById(R.id.prompt_text);
		numberPickerView = findViewById(R.id.num_picker);
		sizeButton = findViewById(R.id.choose_size_button);
		startButton = findViewById(R.id.start_game_button);
		
		// prepare views to be animated into position later
		promptView.animate()
				.translationY(100f)
				.setDuration(500)
				.start();
		
		numberPickerView.animate()
				.translationY(-100f)
				.setDuration(500)
				.start();
		
		startButton.animate()
				.translationY(-150f)
				.setDuration(500)
				.start();
		
		// initialize the NumberPicker
		numberPickerView.setMaxValue(10);
		numberPickerView.setMinValue(5);
		numberPickerView.setWrapSelectorWheel(false);
		numberPickerView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				return;
			}
		});
	}
	
	/**
	 * updates the UI to allow players to pick the board size
	 *
	 * @param view the start game button
	 */
	public void chooseBoardSize(View view) {
		view.animate()
				.translationY(100f)
				.alpha(0f)
				.setDuration(500)
				.setListener(new Animator.AnimatorListener() {
					@Override
					public void onAnimationStart(Animator animator) {
					
					}
					
					@Override
					public void onAnimationEnd(Animator animator) {
						promptView.animate()
								.translationY(-100f)
								.setDuration(500)
								.alpha(1f)
								.start();
						
						numberPickerView.animate()
								.translationY(100f)
								.setDuration(500)
								.alpha(1f)
								.start();
						
						startButton.animate()
								.translationY(150f)
								.setDuration(500)
								.alpha(1f)
								.start();
					}
					
					@Override
					public void onAnimationCancel(Animator animator) {
					
					}
					
					@Override
					public void onAnimationRepeat(Animator animator) {
					
					}
				})
				.start();
	}
	
	/**
	 * launches the GameActivity
	 *
	 * @param view the start game Button
	 */
	public void startGame(View view) {
		int boardSize = numberPickerView.getValue();
		final Intent intent = new Intent(getApplicationContext(), GameActivity.class);
		GameActivity.setBoardSize(boardSize);
		
		
		// allows an "exit animation"
		// TODO: improve to allow views to reappear if players return to *this* Activity
		/*
		promptView.animate()
				.translationY(100f)
				.setDuration(500)
				.alpha(0f)
				.start();
		
		numberPickerView.animate()
				.translationY(-100f)
				.setDuration(500)
				.alpha(0f)
				.start();
		
		startButton.animate()
				.translationY(-150f)
				.setDuration(500)
				.alpha(0f)
				.setListener(new Animator.AnimatorListener() {
					@Override
					public void onAnimationStart(Animator animator) {
					
					}
					
					@Override
					public void onAnimationEnd(Animator animator) {
						startActivity(intent);
					}
					
					@Override
					public void onAnimationCancel(Animator animator) {
					
					}
					
					@Override
					public void onAnimationRepeat(Animator animator) {
					
					}
				})
				.start();
				*/
		startActivity(intent);
	}
}
