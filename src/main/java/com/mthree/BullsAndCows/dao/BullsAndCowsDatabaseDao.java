package com.mthree.BullsAndCows.dao;

import com.mthree.BullsAndCows.model.Game;
import com.mthree.BullsAndCows.model.Round;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.List;

@Repository
public class BullsAndCowsDatabaseDao implements BullsAndCowsDao{

    private final JdbcTemplate jdbcTemplate;


    @Autowired
    public BullsAndCowsDatabaseDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public int addGame(Game game) {

        final String sql = "INSERT INTO games(answer, statusFinished) VALUES(?,?);";
//        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(sql,
                game.getAnswer(),
                game.isStatusFinished());
        int newId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()",Integer.class);

        game.setGameId(newId);

        return game.getGameId();
    }

    @Override
    @Transactional
    public Round getAGuess(Round round) {

      final String sql = "INSERT INTO rounds(`gameId`, guess, timeStamp, roundResult) VALUES(?, ?, ?, ?);";
      GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

      jdbcTemplate.update((Connection conn) -> {
          PreparedStatement stmt = conn.prepareStatement(
                  sql,
                  Statement.RETURN_GENERATED_KEYS
          );

          stmt.setInt(1, round.getGameId());
          stmt.setString(2, round.getGuess());
          stmt.setTimestamp(3, Timestamp.valueOf(round.getTimestamp()));
          stmt.setString(4, round.getRoundResult());

          return stmt;
      }, keyHolder);

      round.setRoundId(keyHolder.getKey().intValue());

      return round;
    }

    @Override
    public List<Game> getAllGames() {
        final String sql = "SELECT gameId, answer, statusFinished FROM games;";

        List<Game> games = jdbcTemplate.query(sql, new GameMapper());

        for(Game game: games ){
            if(!game.isStatusFinished()){
                game.setAnswer("Hidden");
            }

        }
        return games;

    }

    @Override
    public Game getAGame(int id) {
        final String sql = "SELECT gameId, answer, statusFinished FROM games WHERE gameId = ?;";

        Game game = jdbcTemplate.queryForObject(sql, new GameMapper(), id);
        if(!game.isStatusFinished()){
            game.setAnswer("Hidden");
        }
        return game;
    }

    @Override
    public List<Round> getAllRounds(int id) {
        final String sql = "SELECT roundId, gameId, guess, timeStamp, roundResult FROM rounds WHERE gameId = ? ORDER BY timeStamp;";
        return jdbcTemplate.query(sql, new RoundMapper(), id);
    }

    private static final class GameMapper implements RowMapper<Game> {
        @Override
        public Game mapRow(ResultSet rs, int index) throws SQLException {
            Game game = new Game();
            game.setGameId(rs.getInt("gameId"));
            game.setAnswer(rs.getString("answer"));
            game.setStatusFinished(rs.getBoolean("statusFinished"));
            return game;
        }
    }

    public Game getGameFromDB(int gameId){
        final String allGamesSql = "SELECT gameId, answer, statusFinished FROM games WHERE gameId = ?;";
        Game game = jdbcTemplate.queryForObject(allGamesSql, new GameMapper(), gameId);
        return game;
    }

    public void updateGameStatus(boolean status, int gameId){
        final String updateGameSql = "UPDATE games SET statusFinished = ? WHERE gameId = ?;";
        jdbcTemplate.update(updateGameSql, true, gameId);
    }


    @Transactional
    public void deleteGamesById(int id) {

        final String sql = "DELETE FROM games WHERE gameId = ?;";
        jdbcTemplate.update(sql, id);
        List<Game> games = this.getAllGames();
        if(games.size() == 0) {
            jdbcTemplate.execute("ALTER TABLE games AUTO_INCREMENT = 1;");
        }
    }

    @Transactional
    public void deleteRounds(int id) {

        final String sql = "DELETE FROM rounds WHERE gameId = ?;";
        jdbcTemplate.update(sql, id);
        List<Round> rounds = this.getAllRounds(id);
        if(rounds.size() == 0) {
            jdbcTemplate.execute("ALTER TABLE rounds AUTO_INCREMENT = 1;");
        }
    }

    private static final class RoundMapper implements RowMapper<Round> {
        @Override
        public Round mapRow(ResultSet rs, int index) throws SQLException {
            Round round = new Round();
            round.setRoundId(rs.getInt("roundId"));
            round.setGameId(rs.getInt("gameId"));
            round.setGuess(rs.getString("guess"));
            round.setTimestamp(rs.getTimestamp("timeStamp").toLocalDateTime());
            round.setRoundResult(rs.getString("roundResult"));
            return round;
        }
    }
}
