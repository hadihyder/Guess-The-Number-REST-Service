package com.mthree.BullsAndCows.service;

import com.mthree.BullsAndCows.TestApplicationConfiguration;
import com.mthree.BullsAndCows.dao.BullsAndCowsDatabaseDao;
import com.mthree.BullsAndCows.model.Game;
import com.mthree.BullsAndCows.model.Guess;
import com.mthree.BullsAndCows.model.Round;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
public class BullsAndCowsServiceTest {

    @Autowired
    private BullsAndCowsDatabaseDao dao;
    @Autowired
    private BullsAndCowsService service;

    @Before
    public void setUp() throws Exception {
        List<Game> games = dao.getAllGames();
        for(Game game: games){
            dao.deleteGamesById(game.getGameId());
            dao.deleteRounds(game.getGameId());
        }

    }

    @Test
    public void testCreateGame(){
        int testId = service.createGame();

        assertEquals(testId , 1); //There were no games prior to this game so id will be 1
    }

    @Test
    public void testGuessAnswer(){
        int testId = service.createGame();
        Guess guess = new Guess();
        String guessStr = "1234";
        guess.setGuess(guessStr);
        guess.setGameId(testId);

        Round round = service.guessAnswer(guess);

        assertEquals(round.getGameId(), testId);

    }

    @Test
    public void testGetGames(){
        service.createGame();
        List<Game> games = service.getGames();

        assertEquals(1, games.size());
    }

    @Test
    public void testGamesById(){
        int testId = service.createGame();
        Game game = service.getGameById(testId);

        assertEquals(testId, game.getGameId());
    }

    @Test
    public void testGetRoundsByGameId(){
        int testId = service.createGame();
        Guess guess = new Guess();
        String guessStr = "1234";
        guess.setGuess(guessStr);
        guess.setGameId(testId);
        Round round = service.guessAnswer(guess);

        List<Round> rounds = service.getRoundsByGameId(round.getGameId());

        assertEquals(1, rounds.size());

    }

}