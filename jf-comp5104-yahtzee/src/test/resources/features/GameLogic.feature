
@GameLogic
Feature: Game Rules
  The current player can roll and reroll a hand of dice (5) for Yahtzee
  Each player has a roll and can reroll (some) times per turn.
  Each player ends turn by scoring in one of 13 categories.
  The game ends after 13 rounds.

  Background: 
    Given A game with 3 players has started

  @firstRoll
  Scenario: Player starts turn by rolling.
    Given It is player's turn
    And Player has not yet rolled
    When Player rolls
    Then Player gets a new set of 5 dice

  @secondRoll
  Scenario: Player continues turn by re-rolling.
    Given It is player's turn
    And Player has rolled once
    Then Player may reroll

  @thirdRoll
  Scenario: Player re-rolls third time.
    Given It is player's turn
    And Player has rolled twice
    Then Player may reroll

  @noFourthRoll
  Scenario: Player can only roll three times
    Given It is player's turn
    And Player has rolled thrice
    Then Player cannot reroll

    @oneRound
    Scenario: Three players can play a round
      When Every player has had a turn
      Then Round 2 begins