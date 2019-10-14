#Author: joelfaubert@gmail.com
#Keywords Summary : dice roll rolling random
#Feature: List of scenarios.
#Scenario: Business rule through list of steps with arguments.
#Given: Some precondition step
#When: Some key actions
#Then: To observe outcomes or validation
#And,But: To enumerate more Given,When,Then steps
#Scenario Outline: List of steps for data-driven as an Examples and <placeholder>
#Examples: Container for s table
#Background: List of steps run before each of the scenarios
#""" (Doc Strings)
#| (Data Tables)
#@ (Tags/Labels):To group Scenarios
#<> (placeholder)
#""
## (Comments)
#Sample Feature Definition Template
@GameLogic
Feature: Game Rules
  The current player can roll and reroll a hand of dice (5) for Yahtzee
  Each player has a roll and can reroll (some) times per turn.
  Each player ends turn by scoring in one of 13 categories.
  The game ends after 13 rounds.

  Background: 
    Given A game has started
    Given It is player's turn

  @firstRoll
  Scenario: Player starts turn by rolling.
    Given Player has not yet rolled
    When Player rolls
    Then Player gets a new set of 5 dice

  @secondRoll
  Scenario: Player continues turn by re-rolling.
    Given Player has rolled once
    Then Player may reroll

  @thirdRoll
  Scenario: Player re-rolls third time.
    Given Player has rolled twice
    Then Player may reroll

  @noFourthRoll
  Scenario: Player can only roll three times
    Given Player has rolled thrice
    Then Player cannot reroll
