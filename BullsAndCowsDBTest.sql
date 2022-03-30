DROP DATABASE IF EXISTS BullsAndCowsDBTest;

CREATE DATABASE BullsAndCowsDBTest;

USE BullsAndCowsDBTest;

CREATE TABLE games(
	gameId INT PRIMARY KEY AUTO_INCREMENT,
    answer VARCHAR(4) NOT NULL,
    statusFinished BOOLEAN);

CREATE TABLE rounds(
	roundId INT PRIMARY KEY AUTO_INCREMENT,
    guess VARCHAR(4) NOT NULL,
    `timeStamp` timestamp,
    roundResult VARCHAR(10) NOT NULL,
    gameId INT NOT NULL,
    FOREIGN KEY (gameId) REFERENCES games(gameId));