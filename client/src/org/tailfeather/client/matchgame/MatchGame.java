package org.tailfeather.client.matchgame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MatchGame {

	public static enum Symbol {
		Cat, Fish, Pear, Airplane, Banana, Car, Dog, Heart, Square, Monkey, Star, Phone, Hand, Turtle
	}

	public final static int CHOICES = 4;

	private final Random random = new Random(1234);

	private final List<Symbol> allSymbols = new ArrayList<Symbol>();
	private final Map<Symbol, Symbol> matches = new HashMap<Symbol, Symbol>();

	private final List<GameStatusChangedListener> gameStatusChangedListeners = new ArrayList<GameStatusChangedListener>();
	private final List<ChoiceListener> choiceListeners = new ArrayList<ChoiceListener>();

	private final int roundDurationSeconds;
	private final int incorrectChoiceSecondsPenalty;

	private GameStatus status = GameStatus.INITIALIZED;
	private long endMillis = -1;

	// Symbols that appear on the top
	private final List<Symbol> quizSymbols = new ArrayList<Symbol>();

	// Current symbol on top
	private int quizSymbolIndex = -1;

	// Symbols that appear on the bottom
	private Symbol[] choiceSymbols = null;

	private int numCorrect;

	public MatchGame(int roundDurationSeconds, int incorrectChoiceSecondsPenalty) {
		this.roundDurationSeconds = roundDurationSeconds;
		this.incorrectChoiceSecondsPenalty = incorrectChoiceSecondsPenalty;

		for (Symbol s : Symbol.values()) {
			allSymbols.add(s);
		}
		System.out.println(allSymbols);

		matches.put(Symbol.Cat, Symbol.Fish);
		matches.put(Symbol.Pear, Symbol.Airplane);
		matches.put(Symbol.Banana, Symbol.Car);
		matches.put(Symbol.Dog, Symbol.Heart);
		matches.put(Symbol.Square, Symbol.Monkey);
		matches.put(Symbol.Star, Symbol.Phone);
		matches.put(Symbol.Hand, Symbol.Turtle);

		quizSymbols.addAll(matches.keySet());
	}

	public int getNumCorrect() {
		return numCorrect;
	}

	public int getQuizSymbolCount() {
		return quizSymbols.size();
	}

	public GameStatus getStatus() {
		if (status == GameStatus.PLAYING) {
			if (System.currentTimeMillis() > endMillis) {
				setStatus(GameStatus.LOSE);
			}
		}
		return status;
	}

	public void start() {
		quizSymbolIndex = -1;
		goToNextQuizSymbol();
		endMillis = System.currentTimeMillis() + (roundDurationSeconds * 1000);
		setStatus(GameStatus.PLAYING);
	}

	public void tick() {
		switch (status) {
		case PLAYING:
			if (System.currentTimeMillis() > endMillis) {
				setStatus(GameStatus.LOSE);
			}
			break;
		}
	}

	public void choose(int choice) {
		if (status == GameStatus.PLAYING) {
			// To 0-based index
			choice = choice - 1;

			if (choiceSymbols != null && choice >= 0 && choice <= CHOICES - 1) {
				if (choiceSymbols[choice] == matches.get(getQuizSymbol())) {
					// Correct choice
					numCorrect++;
					endMillis = System.currentTimeMillis() + roundDurationSeconds * 1000;

					goToNextQuizSymbol();

					fireChoiceEvent(true);
				} else {
					endMillis -= incorrectChoiceSecondsPenalty * 1000;
					fireChoiceEvent(false);
				}
			}
		}
	}

	private void goToNextQuizSymbol() {
		quizSymbolIndex++;
		if (quizSymbolIndex == quizSymbols.size()) {
			// Win!
			quizSymbolIndex = quizSymbols.size() - 1;
			setStatus(GameStatus.WIN);
		} else {
			calculateChoiceSymbols(getQuizSymbol());
		}
	}

	public Symbol getQuizSymbol() {
		return quizSymbols.get(quizSymbolIndex);
	}

	public Symbol[] getChoiceSymbols() {
		return choiceSymbols;
	}

	public long getTimeRemainingMillis() {
		return endMillis - System.currentTimeMillis();
	}

	private void calculateChoiceSymbols(Symbol quizSymbol) {
		List<Symbol> choices = new ArrayList<Symbol>();
		int num = CHOICES;

		// The quiz symbol must always be a choice
		choices.add(matches.get(quizSymbol));
		num--;

		// Start with all the possible symbols
		List<Symbol> allSymbolsCopy = new ArrayList<Symbol>(allSymbols);

		// Remove the quiz symbol
		allSymbolsCopy.remove(quizSymbol);
		Collections.shuffle(allSymbolsCopy, random);

		while (num-- > 0) {
			choices.add(allSymbolsCopy.remove(0));
		}

		Collections.shuffle(choices, random);
		choiceSymbols = (Symbol[]) choices.toArray(new Symbol[choices.size()]);

		System.out.print(quizSymbol + ": ");
		for (Symbol s : choiceSymbols) {
			System.out.print(s + " ");
		}
		System.out.println();
	}

	public void addStatusChangedListener(GameStatusChangedListener listener) {
		gameStatusChangedListeners.add(listener);
	}

	public void removeStatusChangedListener(GameStatusChangedListener listener) {
		gameStatusChangedListeners.remove(listener);
	}

	private void fireStatusChangedEvent(GameStatus oldStatus, GameStatus newStatus) {
		for (GameStatusChangedListener l : gameStatusChangedListeners) {
			l.onGameStatusChanged(oldStatus, newStatus);
		}
	}

	public void addChoiceListener(ChoiceListener listener) {
		choiceListeners.add(listener);
	}

	public void removeChoiceListener(ChoiceListener listener) {
		choiceListeners.remove(listener);
	}

	private void fireChoiceEvent(boolean correct) {
		for (ChoiceListener l : choiceListeners) {
			l.onChoice(correct);
		}
	}

	private void setStatus(GameStatus newStatus) {
		final GameStatus oldStatus = status;
		status = newStatus;
		if (oldStatus != newStatus) {
			fireStatusChangedEvent(oldStatus, newStatus);
		}
	}

	public float getRoundDurationSeconds() {
		return roundDurationSeconds;
	}
}
