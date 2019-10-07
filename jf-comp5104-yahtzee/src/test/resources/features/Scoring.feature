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
Feature: Rolls can be score into categories

  Background:
    Given Player has a roll to score

  @firstRoll
  Scenario Outline: Player scoring the upper categories (1-6)
    When You score <val1>, <val2>, <val3>, <val4>, <val5> into category <category>
    Then You get <score> in category <category>

    Examples:
      | val1 | val2 | val3 | val4 | val5 | category | score |
      | 1    | 1    | 1    | 1    | 1    | 1        | 5     |
      | 2    | 3    | 4    | 5    | 6    | 1        | 0     |
      | 1    | 1    | 2    | 3    | 4    | 1        | 2     |
      | 2    | 3    | 4    | 1    | 1    | 1        | 2     |

