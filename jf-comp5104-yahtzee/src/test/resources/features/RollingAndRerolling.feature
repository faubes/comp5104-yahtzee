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

  @firstRoll
  Scenario: The current player can roll.
    Given That a game has started
    And You are the current player
    And You have not yet rolled
    When You roll
    Then You get a new set of 5 dice

  @rerollByHoldIndex
  Scenario Outline: The current player, having rolled once,
    can hold dice and reroll others.

    Given That a game has started
    And You are the current player
    And You have rolled at least once
    And You have not already rerolled twice 
    When You hold some dice
    Then You reroll the others
