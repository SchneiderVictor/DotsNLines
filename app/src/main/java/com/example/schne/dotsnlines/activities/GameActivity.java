package com.example.schne.dotsnlines.activities;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.schne.dotsnlines.R;
import com.example.schne.dotsnlines.data.Dot;
import com.example.schne.dotsnlines.data.Line;

import java.util.ArrayList;
import java.util.HashMap;

public class GameActivity extends AppCompatActivity {
	// values used to initialize the game board
	// the vertical and horizontalBias difference between rows and columns
	private static float biasDelta;
	private static int boardSize;
	
	// values used for basic tracking of current game state
	private boolean extraMove;
	private boolean player1 = false;
	private int player1Score = 0, player2Score = 0;
	
	// values used for more advanced features, such as validating moves
	private ArrayList<Dot> dots = new ArrayList<>();
	private ArrayList<Line> lines = new ArrayList<>();
	private HashMap<Button, Dot> buttonDotHashMap = new HashMap<>();
	
	// Android Views to be manipulated
	private Button button1, button2;
	private ConstraintLayout board;
	private Drawable currentPlayerDrawable;
	private View player1ScoreView, player2ScoreView, scoreDivider;
	private TextView player1ScoreLabel, player2ScoreLabel;
	private TextView notifier;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		
		Intent initializationData = getIntent();
		
		boardSize = initializationData.getIntExtra("boardSize", 5) + 1;
		biasDelta = 1f / (float) boardSize;
		
		initializeViews();
		initializeBoard();
	}
	
	private void initializeViews() {
		board = findViewById(R.id.board_layout);
		player1ScoreView = findViewById(R.id.player1_score);
		player2ScoreView = findViewById(R.id.player2_score);
		scoreDivider = findViewById(R.id.score_divider);
		player1ScoreLabel = findViewById(R.id.player1_score_label);
		player2ScoreLabel = findViewById(R.id.player2_score_label);
		notifier = findViewById(R.id.notifier);
	}
	
	private void initializeBoard() {
		populateButtons();
		populateDots();
	}
	
	/**
	 * Populates the board with Buttons, while mapping Buttons to
	 * Dot objects
	 */
	private void populateButtons() {
		for (int row = 1; row < boardSize; row += 1) {
			for (int col = 1; col < boardSize; col += 1) {
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
	
	/**
	 * Populates each Dot object with its neighbours:
	 * above, below, left and right
	 */
	private void populateDots() {
		for (int i = 0; i < dots.size(); i++) {
			Dot neighbor;
			
			// dots.get(i - (boardSize - 1)) works out to be the Dot above dots.get(i),
			// so on and so forth...
			for (int index : new int[]{i - (boardSize - 1), i + (boardSize - 1), i - 1, i + 1}) {
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
	
	/**
	 * Helper method for the animation when tapping a Button
	 *
	 * @param button     the button that was tapped
	 * @param finalState the Drawable which we want to describe the button at the end of the animation
	 */
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
		// If no start button has been selected for this move...
		if (button1 == null) {
			button1 = (Button) view;
			
			updateDot(button1, getDrawable(R.drawable.selected_dot));
		} else if (view == button1) {
			// if the start button is selected again, we count that as deselecting it
			button1.setBackground(getDrawable(R.drawable.dots_background));
			button1 = null;
		} else if (button2 == null && isValidMove((Button) view)) {
			// a valid end button was selected...
			button2 = (Button) view;
			
			makeMove();
			
			button1.setBackground(getDrawable(R.drawable.dots_background));
			button1 = null;
			button2 = null;
		} else {
			// deselect the start button
			selectDot(button1);
		}
	}
	
	/**
	 * completes a move
	 * <p>
	 * if the move has not been made, the line is drawn
	 * and the score is updated (as well as the visual representation)
	 */
	private void makeMove() {
		int[] pos1 = getButtonPosition(button1);
		int[] pos2 = getButtonPosition(button2);
		Line newLine = new Line(pos1, pos2, boardSize);
		int completeSquare;
		
		// if the move has not already been made...
		if (!lines.contains(newLine)) {
			drawLineBetweenDots();
			lines.add(newLine);
			
			completeSquare = newLine.getCompleteSquare(lines);
			extraMove = addScoreDot(completeSquare);
			
			if (completeSquare > 0) {
				updateScoreDivision();
			}
			
			updateNotifier();
		}
	}
	
	/**
	 * Draws a colored Button to represent who won the point.
	 * The Button is placed within the corresponding square
	 *
	 * @param row row index at which the Button will need to be placed
	 * @param col column index at which the Button will need to be placed
	 */
	private void drawScoreDot(float row, float col) {
		ConstraintLayout newDotLayout = (ConstraintLayout) View.inflate(board.getContext(), R.layout.dot_layout, null);
		Button newButton = newDotLayout.findViewById(R.id.dot);
		newDotLayout.removeView(newButton);
		
		ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) newButton.getLayoutParams();
		params.verticalBias = (biasDelta * row);
		params.horizontalBias = (biasDelta * col);
		newButton.setLayoutParams(params);
		
		newButton.setAlpha(0f);
		
		// the score Button (Dot) should not react to being tapped
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
	
	
	/**
	 * calculates the row and column indices of score dots,
	 * then draws them onto the board
	 * <p>
	 * completeSquares == 0 --> no score dot
	 * completeSquares == 1 --> score dot above(or to the left) of the drawn line
	 * completeSquares == 2 --> score dot below(or to the right) of the drawn line
	 * completeSquares == 3 --> both score dots
	 *
	 * @param completeSquares integer code representing which, if any, squares have been completed
	 * @return true iff a point has been awarded/a score dot was drawn
	 */
	private boolean addScoreDot(int completeSquares) {
		if (completeSquares == 0) {
			return false;
		}
		int[] pos1 = getButtonPosition(button1);
		int[] pos2 = getButtonPosition(button2);
		float row, col;
		
		// for when the drawn line is vertical
		if (pos1[0] == pos2[0]) {
			row = Math.min(pos1[1], pos2[1]) + 0.5f;
			
			// draw a score dot for completeSquares == 1 and completeSquares == 3
			// score dot drawn to the left
			if (completeSquares % 2 == 1) {
				col = pos1[0] - 0.5f;
				drawScoreDot(row, col);
			}
			
			// draw a score dot for completeSquares == 2 and completeSquares == 3
			// score dot drawn to the right
			if (completeSquares - 1 > 0) {
				col = pos1[0] + 0.5f;
				drawScoreDot(row, col);
			}
		} else {
			// for when the drawn line is vertical
			col = Math.min(pos1[0], pos2[0]) + 0.5f;
			
			// draw a score dot for completeSquares == 1 and completeSquares == 3
			// score dot drawn above
			if (completeSquares % 2 == 1) {
				row = pos1[1] - 0.5f;
				drawScoreDot(row, col);
			}
			
			// draw a score dot for completeSquares == 2 and completeSquares == 3
			// score dot drawn below
			if (completeSquares - 1 > 0) {
				row = pos1[1] + 0.5f;
				drawScoreDot(row, col);
			}
		}
		
		return true;
	}
	
	/**
	 * returns an int[] representing the Button's coordinates as (x, y)
	 *
	 * @param button tapped Button
	 * @return Button's coordinates
	 */
	private int[] getButtonPosition(Button button) {
		ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) button.getLayoutParams();
		int xAxis = (int) (params.horizontalBias * boardSize);
		int yAxis = (int) (params.verticalBias * boardSize);
		return new int[]{xAxis, yAxis};
	}
	
	/**
	 * Positions the newLine based on which buttons were tapped
	 *
	 * @param newLine the new View that was created
	 */
	private void setNewLineParams(View newLine) {
		int[] button1Pos = getButtonPosition(button1);
		int[] button2Pos = getButtonPosition(button2);
		ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) newLine.getLayoutParams();
		
		// for a vertical line...
		if (button1Pos[0] == button2Pos[0]) {
			params.verticalBias = (biasDelta / 2f) + (biasDelta * Math.min(button1Pos[1], button2Pos[1]));
			params.horizontalBias = (biasDelta * button1Pos[0]);
			
			params.width = 6;
			params.height = (int) Math.abs(button1.getY() - button2.getY());
		} else {
			// for a horizontal line...
			params.verticalBias = (biasDelta * button1Pos[1]);
			params.horizontalBias = (biasDelta / 2f) + (biasDelta * Math.min(button1Pos[0], button2Pos[0]));
			
			params.width = (int) Math.abs(button1.getX() - button2.getX());
			params.height = 6;
		}
		
		
		newLine.setLayoutParams(params);
	}
	
	
	/**
	 * Draws and colors a line between two selected Buttons
	 */
	private void drawLineBetweenDots() {
		ConstraintLayout newLineLayout = (ConstraintLayout) View.inflate(this, R.layout.line_layout, null);
		View newLine = newLineLayout.findViewById(R.id.line);
		newLineLayout.removeView(newLine);
		board.addView(newLine);
		
		setNewLineParams(newLine);
		
		// player switching is done BEFORE choosing the color simply
		// because player1 was initialized as false (indicating player 2)
		if (!extraMove) {
			player1 = !player1;
		}
		int currentColor = player1 ? getColor(R.color.colorPlayer1) : getColor(R.color.colorPlayer2);
		currentPlayerDrawable = player1 ? getDrawable(R.drawable.score_dot1) : getDrawable(R.drawable.score_dot2);
		
		newLine.setBackgroundColor(currentColor);
		
		extraMove = false;
	}
	
	/**
	 * Updates the visual representation of the score difference if a point has been earned
	 */
	private void updateScoreDivision() {
		if (player1Score + player2Score == 0) {
			return;
		}
		ConstraintSet newState = new ConstraintSet();
		float finalPosition = (float) player1Score / ((float) player1Score + (float) player2Score);
		
		player1ScoreLabel = findViewById(R.id.player1_score_label);
		player2ScoreLabel = findViewById(R.id.player2_score_label);
		
		player1ScoreLabel.setText(Integer.toString(player1Score));
		player2ScoreLabel.setText(Integer.toString(player2Score));
		
		if (finalPosition == 0f || finalPosition == 1f) {
			finalPosition = 0.5f;
			int currentPlayerColor = player1 ? R.color.colorPlayer1 : R.color.colorPlayer2;
			player1ScoreView.setBackgroundTintList(getColorStateList(currentPlayerColor));
			player2ScoreView.setBackgroundTintList(getColorStateList(currentPlayerColor));
		} else {
			player1ScoreView.setBackgroundTintList(getColorStateList(R.color.colorPlayer1));
			player2ScoreView.setBackgroundTintList(getColorStateList(R.color.colorPlayer2));
		}
		
		newState.clone((ConstraintLayout) findViewById(R.id.score_layout));
		newState.setHorizontalBias(R.id.score_divider, finalPosition);
		
		TransitionManager.beginDelayedTransition((ConstraintLayout) findViewById(R.id.score_layout));
		newState.applyTo((ConstraintLayout) findViewById(R.id.score_layout));
	}
	
	private void updateNotifier() {
		if (player1Score + player2Score == (boardSize - 2) * (boardSize - 2)) {
			if (player1Score > player2Score) {
				notifier.setText(R.string.player_1_wins);
				notifier.setTextColor(getColor(R.color.colorPlayer1));
			} else if (player2Score > player1Score) {
				notifier.setText(R.string.player_2_wins);
				notifier.setTextColor(getColor(R.color.colorPlayer2));
			} else {
				notifier.setText(R.string.tie_game);
				notifier.setTextColor(getColor(R.color.colorAccent));
			}
		} else if (player1 == extraMove) {
			notifier.setText(R.string.player_1_turn);
			notifier.setTextColor(getColor(R.color.colorPlayer1));
		} else {
			notifier.setText(R.string.player_2_turn);
			notifier.setTextColor(getColor(R.color.colorPlayer2));
		}
	}
}
