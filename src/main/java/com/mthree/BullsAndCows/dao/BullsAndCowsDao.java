package com.mthree.BullsAndCows.dao;

import com.mthree.BullsAndCows.model.Game;
import com.mthree.BullsAndCows.model.Round;

import java.util.List;

public interface BullsAndCowsDao {

     public int addGame(Game game);

     public Round getAGuess(Round round);

     public List<Game> getAllGames();

     public Game getAGame(int id);

     public List<Round> getAllRounds(int id);

     public Game getGameFromDB(int id);

     public void updateGameStatus(boolean status, int gameId);

}
