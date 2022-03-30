package com.mthree.BullsAndCows.dao;

import com.mthree.BullsAndCows.TestApplicationConfiguration;
import com.mthree.BullsAndCows.model.Game;
import com.mthree.BullsAndCows.model.Round;
import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
public class BullsAndCowsDatabaseDaoTest {

    @Autowired
    BullsAndCowsDatabaseDao dao;


    @Before
    public void setUp(){
        List<Game> games = dao.getAllGames();
        for(Game game: games){
            dao.deleteGamesById(game.getGameId());
            dao.deleteRounds(game.getGameId());
        }

    }


    @Test
    public void testAddGame(){
        Game game = new Game();
        game.setAnswer("1234");
        game.setStatusFinished(false);

        int retrievedGame = dao.addGame(game);

        assertEquals(1, retrievedGame);

    }
//
    @Test
    public void testMakeAGuess(){
        Game game = new Game();
        game.setAnswer("1234");
        game.setStatusFinished(false);
        dao.addGame(game);

        LocalDateTime time = LocalDateTime.now();
        Round round = new Round();
        round.setGameId(1);
        round.setGuess("1234");
        round.setRoundResult("e:4p:0");
        round.setTimestamp(time);

        Round retrievedRound = dao.getAGuess(round);

        assertEquals(retrievedRound.getRoundResult(), round.getRoundResult());
    }

    @Test
    public void testGetAllGames(){
        Game game = new Game();
        game.setAnswer("1234");
        game.setStatusFinished(false);
        dao.addGame(game);
        List<Game> games = dao.getAllGames();

        assertEquals(1, games.size());
    }

    @Test
    public void testGetAGame(){
        Game game = new Game();
        game.setAnswer("1234");
        game.setStatusFinished(false);
        int testId = dao.addGame(game);


        Game gameRetrieved = dao.getAGame(testId);

        assertEquals(gameRetrieved.getGameId(), game.getGameId());
    }

    @Test
    public void testGetAllRounds(){
        Game game = new Game();
        game.setAnswer("1234");
        game.setStatusFinished(false);
        int id = dao.addGame(game);

        LocalDateTime time = LocalDateTime.now();
        Round round = new Round();
        round.setGameId(1);
        round.setGuess("1234");
        round.setRoundResult("e:4p:0");
        round.setTimestamp(time);

        dao.getAGuess(round);

        List<Round> rounds = dao.getAllRounds(id);

        assertEquals(1, rounds.size());

    }

    @Test
    public void testGetGameFromDB(){
        Game game = new Game();
        game.setAnswer("1234");
        game.setStatusFinished(false);
        int id = dao.addGame(game);

        Game retrievedGame = dao.getGameFromDB(id);

        assertEquals(retrievedGame.isStatusFinished(), game.isStatusFinished());
    }

    @Test
    public void testUpdateGameStatus(){
        Game game = new Game();
        game.setAnswer("1234");
        game.setStatusFinished(false);
        int id = dao.addGame(game);

        dao.updateGameStatus(true, id);

        Game retrievedGame = dao.getAGame(id);

        assertNotEquals(game.isStatusFinished(), retrievedGame.isStatusFinished());
    }
}