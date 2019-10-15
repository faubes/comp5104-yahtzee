@DiceLogic
Feature: Dice Logic

  @equivalentRolls
  Scenario Outline: Equivalent rolls
    When One roll <r1>, <r2>, <r3>, <r4>, <r5>
    And Another roll <r6> <r7>, <r8>, <r9>, <r10>
    Then Roll one and roll two are equivalent

    Examples:
      | r1 | r2 | r3 | r4 | r5 | r6 | r7 | r8 | r9 | r10 |
      | 1  | 2  | 3  | 4  | 5  | 5  | 4  | 3  | 2  | 1  |
      | 1  | 2  | 3  | 4  | 5  | 3  | 4  | 5  | 2  | 1  |
      | 2  | 2  | 5  | 5  | 5  | 5  | 2  | 5  | 2  | 5  |
      | 2  | 2  | 5  | 5  | 5  | 5  | 5  | 5  | 2  | 2  |