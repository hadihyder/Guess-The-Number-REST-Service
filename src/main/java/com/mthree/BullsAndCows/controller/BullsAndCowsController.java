package com.mthree.BullsAndCows.controller;


import com.mthree.BullsAndCows.model.Game;
import com.mthree.BullsAndCows.model.Guess;
import com.mthree.BullsAndCows.model.Round;
import com.mthree.BullsAndCows.service.BullsAndCowsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/game")
public class BullsAndCowsController {


    private final BullsAndCowsService service;

    public BullsAndCowsController(BullsAndCowsService service){
        this.service = service;
    }

    @GetMapping
    public List<Game> all() {
        return service.getGames();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public int create() {

        return service.createGame();
    }

    @PostMapping("guess")
    public Round guess(@RequestBody Guess guess){

        return service.guessAnswer(guess);
    }

    @GetMapping("{id}")
    public Game game(@PathVariable int id){
        return service.getGameById(id);

    }

    @GetMapping("rounds/{id}")
    public List<Round> rounds(@PathVariable int id){
        return service.getRoundsByGameId(id);

    }
}
