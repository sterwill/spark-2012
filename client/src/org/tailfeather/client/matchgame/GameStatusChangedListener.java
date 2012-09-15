package org.tailfeather.client.matchgame;

public interface GameStatusChangedListener {
	void onGameStatusChanged(GameStatus oldStatus, GameStatus newStatus);
}
