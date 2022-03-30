package com.mthree.BullsAndCows.service;

import com.mthree.BullsAndCows.dao.BullsAndCowsDao;
import com.mthree.BullsAndCows.model.Game;
import com.mthree.BullsAndCows.model.Guess;
import com.mthree.BullsAndCows.model.Round;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class BullsAndCowsService {


    private BullsAndCowsDao dao;

    @Autowired
    public BullsAndCowsService(BullsAndCowsDao dao) {
        this.dao = dao;
    }

    public int createGame(){
        Game game = new Game();
        Random rand = new Random();
        final Set<Integer> pick = new HashSet<>();
        String randomAnswer = "";
        while (pick.size() < 4){
            int digit = rand.nextInt(9);
            if(!pick.contains(digit)){
                pick.add(digit);
                randomAnswer += String.valueOf(digit);

            }
        }

        game.setAnswer(randomAnswer);
        game.setStatusFinished(false);

        return dao.addGame(game);

    }

    public Round guessAnswer(Guess guess){

        LocalDateTime timeStamp = LocalDateTime.now();
        int gameId = guess.getGameId();
        String guessStr = guess.getGuess();
        Game game = dao.getGameFromDB(gameId);

        Round round = new Round();
        assert game != null;
        String answer = game.getAnswer();

        int exact = 0;
        int partial = 0;

        int[] digits = new int[10];
        for(int i = 0; i < guessStr.length(); i++){

            int g = Character.getNumericValue(guessStr.charAt(i));
            int ans = Character.getNumericValue((answer.charAt(i)));
            if(g == ans) exact++;
            else {
                if(digits[g] < 0) partial++;
                if(digits[g] > 0) partial++;
            }
        }

        String result = "e:" + String.valueOf(exact) + "p:" + String.valueOf(partial);

        round.setGameId(gameId);
        round.setGuess(guessStr);
        round.setTimestamp(timeStamp);
        round.setRoundResult(result);

        if(exact == 4) {
            dao.updateGameStatus(true, gameId);
        }

        return dao.getAGuess(round);


    }

    public List<Game> getGames(){
        return dao.getAllGames();
    }

    public Game getGameById(int id){
        return dao.getAGame(id);
    }

    public List<Round> getRoundsByGameId(int id){
        return dao.getAllRounds(id);
    }

}
