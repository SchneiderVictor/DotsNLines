package com.example.schne.dotsnlines;

import android.animation.Animator;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.Explode;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;

public class GameActivity extends AppCompatActivity {
	private static int boardSize;
	private float biasDelta = 1f / boardSize;
	private Button button1, button2;
	private ConstraintLayout board;
	private View player1ScoreView, player2ScoreView, scoreDivider;
	private boolean player1 = false;
	private ArrayList<Dot> dots = new ArrayList<>();
	private ArrayList<Line> lines = new ArrayList<>();
	private HashMap<Button, Dot> buttonDotHashMap = new HashMap<>();
	private int currentColor;
	private Drawable currentPlayerDrawable;
	private boolean extraMove;
	private int player1Score = 0, player2Score = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
		setContentView(R.layout.activity_game);
		board = findViewById(R.id.board_layout);
		player1ScoreView = findViewById(R.id.player1_score);
		player2ScoreView = findViewById(R.id.player2_score);
		scoreDivider = findViewById(R.id.score_divider);
		
//		getWindow().setEnterTransition(new Explode());
		
		ViewGroup scoreLayout = findViewById(R.id.score_layout);
		AutoTransition transition = new AutoTransition();
		transition.setDuration(1000);
		
		initializeBoard();
		
		TransitionManager.beginDelayedTransition(scoreLayout, transition);
	}
	
	public static void setBoardSize(int size) {
		boardSize = size;
	}
	
	private void initializeBoard() {
		populateButtons();
		populateDots();
	}
	
	private void populateButtons() {
		for (int row = 1; row < boardSize; row+= 1) {
			for (int col = 1; col < boardSize; col+= 1) {
				ConstraintLayout newDotLayout = (ConstraintLayout) View.inflate(board.getContext(), R.layout.dot_layout, null);
				Button newButton = newDotLayout.findViewById(R.id.dot);
				newDotLayout.removeView(newButton);
				
				ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) newButton.getLayoutParams();
				params.verticalBias = (biasDelta * row);
				params.horizontalBias = (biasDelta * col);
				newButton.setLayoutParams(params);
				board.addView(newButton);
				
				Dot newDot = new Dot();
				buttonDotHashMap.put(newButton, newDot);
				dots.add(newDot);
			}
		}
	}
	
	private void populateDots() {
		for (int i = 0; i < dots.size(); i++) {
			Dot neighbor;
			
			for (int index : new int[] {i - (boardSize - 1), i + (boardSize - 1), i - 1, i + 1}) {
				if (index >= 0 && index < dots.size()) {
					neighbor = dots.get(index);
					dots.get(i).addNeighbor(neighbor);
				}
			}
		}
	}
	
	private boolean isValidMove(Button button) {
		Dot dot1 = buttonDotHashMap.get(button1);
		Dot dot2 = buttonDotHashMap.get(button);
		
		return dot1.isValidNeighbor(dot2);
	}
	
	private void updateDot(final Button button, Drawable finalState) {
		button.setBackground(finalState);
		button.animate()
				.alpha(1f)
				.setDuration(250)
				.scaleX(1f)
				.scaleY(1f)
				.setListener(new Animator.AnimatorListener() {
					@Override
					public void onAnimationStart(Animator animator) {
					
					}
					
					@Override
					public void onAnimationEnd(Animator animator) {
						button.clearAnimation();
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
	
	public void selectDot(View view) {
		if (button1 == null) {
			button1 = (Button) view;
			
			updateDot(button1, getDrawable(R.drawable.selected_dot));
		} else if (view == button1) {
			button1.setBackground(getDrawable(R.drawable.dots_background));
			button1 = null;
		} else if (button2 == null && isValidMove((Button) view)) {
			button2 = (Button) view;
			int[] pos1 = getButtonPosition(button1);
			int[] pos2 = getButtonPosition(button2);
			Line newLine = new Line(pos1, pos2, boardSize);
			
			if (!lines.contains(newLine)) {
				drawLineBetweenDots();
				lines.add(newLine);
				
				int completeSquare = newLine.getCompleteSquare(lines);
				extraMove = addScoreDot(completeSquare);
				
				updateScoreDivision();
			}
			
			button1.setBackground(getDrawable(R.drawable.dots_background));
			
			button1 = null;
			button2 = null;
		}
	}
	
	private void drawScoreDot(float row, float col) {
		ConstraintLayout newDotLayout = (ConstraintLayout) View.inflate(board.getContext(), R.layout.dot_layout, null);
		Button newButton = newDotLayout.findViewById(R.id.dot);
		newDotLayout.removeView(newButton);
		
		ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) newButton.getLayoutParams();
		params.verticalBias = (biasDelta * row);
		params.horizontalBias = (biasDelta * col);
		newButton.setLayoutParams(params);
		
		newButton.setAlpha(0f);
		
		newButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
			}
		});
		
		updateDot(newButton, currentPlayerDrawable);
		
		board.addView(newButton);
		
		if (player1) {
			player1Score++;
		} else {
			player2Score++;
		}
	}
	
	private boolean addScoreDot(int completeSquares) {
		if (completeSquares == 0) {
			return false;
		}
		int[] pos1 = getButtonPosition(button1);
		int[] pos2 = getButtonPosition(button2);
		float row, col;
		
		if (pos1[0] == pos2[0]) {
			row = Math.min(pos1[1], pos2[1]) + 0.5f;
			
			if (completeSquares % 2 == 1) {
				col = pos1[0] - 0.5f;
				drawScoreDot(row, col);
			}
			if (completeSquares - 1 > 0) {
				col = pos1[0] + 0.5f;
				drawScoreDot(row, col);
			}
		} else {
			col = Math.min(pos1[0], pos2[0]) + 0.5f;
			
			if (completeSquares % 2 == 1) {
				row = pos1[1] - 0.5f;
				drawScoreDot(row, col);
			}
			if (completeSquares - 1 > 0) {
				row = pos1[1] + 0.5f;
				drawScoreDot(row, col);
			}
		}
		
		return true;
	}
	
	private int[] getButtonPosition(Button button) {
		ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) button.getLayoutParams();
		int xAxis = (int) (params.horizontalBias * boardSize);
		int yAxis = (int) (params.verticalBias * boardSize);
		return new int[] {xAxis, yAxis};
	}
	
	private void setNewLineParams(View newLine) {
		int[] button1Pos = getButtonPosition(button1);
		int[] button2Pos = getButtonPosition(button2);
		ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) newLine.getLayoutParams();
		
		if (button1Pos[0] == button2Pos[0]) {
			params.verticalBias = (biasDelta / 2f) + (biasDelta * Math.min(button1Pos[1], button2Pos[1]));
			params.horizontalBias = (biasDelta * button1Pos[0]);
			
			params.width = 6;
			params.height = (int) Math.abs(button1.getY() - button2.getY());
		} else {
			params.verticalBias = (biasDelta * button1Pos[1]);
			params.horizontalBias = (biasDelta / 2f) + (biasDelta * Math.min(button1Pos[0], button2Pos[0]));
			
			params.width = (int) Math.abs(button1.getX() - button2.getX());
			params.height = 6;
		}
		
		newLine.setLayoutParams(params);
	}
	
	private void drawLineBetweenDots() {
		ConstraintLayout newLineLayout = (ConstraintLayout) View.inflate(board.getContext(), R.layout.line_layout, null);
		View newLine = newLineLayout.findViewById(R.id.line);
		newLineLayout.removeView(newLine);
		
		setNewLineParams(newLine);
		
		if (!extraMove) {
			player1 = !player1;
		}
		currentColor = player1 ? getColor(R.color.colorPlayer1) : getColor(R.color.colorPlayer2);
		currentPlayerDrawable = player1 ? getDrawable(R.drawable.score_dot1) : getDrawable(R.drawable.score_dot2);
		
		newLine.setBackgroundColor(currentColor);
		
		board.addView(newLine);
		
		extraMove = false;
	}
	
	private void updateScoreDivision() {
		if (player1Score + player2Score == 0) {
			return;
		}
		ConstraintSet newState = new ConstraintSet();
		float finalPosition = (float) player1Score / ((float) player1Score + (float) player2Score);
		
		if (player1ScoreView.getAlpha() == 0f) {
			player1ScoreView.animate()
					.alpha(1f)
					.setDuration(1000)
					.start();
			
			player2ScoreView.animate()
					.alpha(1f)
					.setDuration(1000)
					.start();
		}
		
		newState.clone((ConstraintLayout) findViewById(R.id.score_layout));
		newState.setHorizontalBias(R.id.score_divider, finalPosition);
		newState.applyTo((ConstraintLayout) findViewById(R.id.score_layout));
	}
}
