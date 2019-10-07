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
@RollsAndRerolls
Feature: The current player can roll and reroll a hand of dice (5) for Yahtzee
  Each player has a roll and can reroll (some) times per turn.

  Background: 
    Given You start a game

  @firstRoll
  Scenario: Player starts turn by rolling.
    Given You have not yet rolled
    When You roll
    Then You get a new set of dice values

  @secondRoll
  Scenario: Player continues turn by re-rolling.
    Given The player has rolled once
    When You hold dice 1, 2, 4
    Then You hold 1, 2, 4 and reroll dice 3, 5

  @thirdRoll
  Scenario: Player re-rolls third time
    Given The player has rolled twice
    When You reroll 1, 2, 3
    Then You get a new set of dice values