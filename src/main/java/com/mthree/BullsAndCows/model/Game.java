package com.mthree.BullsAndCows.model;

public class Game {

    int gameId;
    String answer;
    boolean statusFinished;

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isStatusFinished() {
        return statusFinished;
    }

    public void setStatusFinished(boolean statusFinished) {
        this.statusFinished = statusFinished;
    }
}

