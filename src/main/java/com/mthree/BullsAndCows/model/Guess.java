package com.mthree.BullsAndCows.model;

public class Guess {

    String guess;
    int gameId;

    public void setGuess(String guess) {
        this.guess = guess;
    }

    public String getGuess() {
        return guess;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getGameId() {
        return gameId;
    }
}
