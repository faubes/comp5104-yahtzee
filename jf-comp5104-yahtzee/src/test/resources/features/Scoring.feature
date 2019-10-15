@Scoring
Feature: Scoring rolls into categories

  Background:
    Given Player has a roll to score

  @ScoreFirstSixCategories
  Scenario Outline: Player scoring the upper categories (1-6)
    When Player scores <val1>, <val2>, <val3>, <val4>, <val5> into category <category>
    Then Player gets <score> in category <category>

    Examples:
      | val1 | val2 | val3 | val4 | val5 | category | score |
      # rolls worth points
      | 1    | 1    | 1    | 1    | 1    | 1        | 5     |
      | 2    | 2    | 2    | 1    | 1    | 2        | 6     |
#      | 1    | 1    | 1    | 3    | 1    | 3        | 3     |
#      | 1    | 1    | 4    | 4    | 1    | 4        | 8     |
#      | 5    | 5    | 5    | 1    | 5    | 5        | 20    |
      | 6    | 6    | 6    | 6    | 6    | 6        | 30    |
      # rolls worth 0
      | 2    | 2    | 2    | 2    | 2    | 1        | 0     |
      | 1    | 1    | 1    | 1    | 1    | 2        | 0     |
#      | 1    | 1    | 1    | 1    | 1    | 3        | 0     |
#      | 1    | 1    | 1    | 1    | 1    | 4        | 0     |
#      | 1    | 1    | 1    | 1    | 1    | 5        | 0     |
#      | 1    | 1    | 1    | 1    | 1    | 6        | 0     |
      # permutations have no effect
      | 1    | 1    | 2    | 3    | 4    | 1        | 2     |
      | 2    | 3    | 4    | 1    | 1    | 1        | 2     |
 #     | 1    | 3    | 4    | 1    | 2    | 1        | 2     |


  @ScoreThreeOfAKind
  Scenario Outline: Player scoring Three of a Kind
    When Player scores <val1>, <val2>, <val3>, <val4>, <val5> into category 7
    Then Player gets <score> in category 7

    Examples:
      | val1 | val2 | val3 | val4 | val5 | score |
      # rolls worth points
      | 1    | 1    | 1    | 1    | 1    | 5     |
      | 2    | 2    | 2    | 1    | 1    | 8     |
      | 3    | 1    | 3    | 1    | 3    | 11    |
      # rolls worth 0
      | 1    | 1    | 2    | 2    | 3    | 0     |
      # permutations have no effect
      | 2    | 1    | 3    | 2    | 1    | 0     |
      | 1    | 3    | 3    | 1    | 3    | 11    |
      | 1    | 3    | 3    | 1    | 3    | 11    |

  @ScoreFourOfAKind
  Scenario Outline: Player scoring Four of a Kind
    When Player scores <val1>, <val2>, <val3>, <val4>, <val5> into category 8
    Then Player gets <score> in category 8

    Examples:
      | val1 | val2 | val3 | val4 | val5 | score |
      # rolls worth points
      | 1    | 1    | 1    | 1    | 1    | 5     |
      | 2    | 1    | 1    | 1    | 1    | 6     |
      | 5    | 5    | 3    | 5    | 5    | 23    |
      # rolls worth 0
      | 1    | 1    | 2    | 2    | 3    | 0     |
      # permutations have no effect
      | 2    | 1    | 3    | 2    | 1    | 0     |
      | 1    | 2    | 1    | 1    | 1    | 6     |
      | 5    | 5    | 3    | 5    | 5    | 23    |

  @ScoreFullHouse
  Scenario Outline: Player scoring Full House
    When Player scores <val1>, <val2>, <val3>, <val4>, <val5> into category 9
    Then Player gets <score> in category 9

    Examples:
      | val1 | val2 | val3 | val4 | val5 | score |
      # rolls worth points
      | 1    | 1    | 1    | 1    | 1    | 25    |
      | 2    | 1    | 1    | 1    | 2    | 25    |
      | 5    | 5    | 3    | 5    | 3    | 25    |
      # rolls worth 0
      | 1    | 1    | 2    | 2    | 3    | 0     |
      | 1    | 6    | 6    | 6    | 6    | 0     |
      # permutations have no effect
      | 3    | 5    | 5    | 5    | 3    | 25    |
      | 5    | 5    | 5    | 3    | 3    | 25    |

  @ScoreSmallStraight
  Scenario Outline: Player scoring Small Straight
    When Player scores <val1>, <val2>, <val3>, <val4>, <val5> into category 10
    Then Player gets <score> in category 10

    Examples:
      | val1 | val2 | val3 | val4 | val5 | score |
      # rolls worth points
      | 1    | 2    | 3    | 4    | 1    | 30    |
      | 4    | 3    | 2    | 1    | 2    | 30    |
      | 6    | 5    | 4    | 3    | 2    | 30    |
      | 5    | 3    | 4    | 2    | 5    | 30    |
      # rolls worth 0
      | 1    | 1    | 2    | 2    | 3    | 0     |
      | 1    | 6    | 6    | 6    | 6    | 0     |

  @ScoreSmallStraight
  Scenario Outline: Player scoring Large Straight
    When Player scores <val1>, <val2>, <val3>, <val4>, <val5> into category 11
    Then Player gets <score> in category 11

    Examples:
      | val1 | val2 | val3 | val4 | val5 | score |
      # rolls worth points
      | 1    | 2    | 3    | 4    | 5    | 40    |
      | 5    | 4    | 3    | 2    | 1    | 40    |
      | 6    | 4    | 5    | 3    | 2    | 40    |
      | 1    | 3    | 4    | 2    | 5    | 40    |
      # rolls worth 0
      | 1    | 2    | 3    | 4    | 6    | 0     |
      | 1    | 1    | 2    | 2    | 3    | 0     |
      | 1    | 6    | 6    | 6    | 6    | 0     |


  @ScoreChance
  Scenario Outline: Player scoring Chance
    When Player scores <val1>, <val2>, <val3>, <val4>, <val5> into category 12
    Then Player gets <score> in category 12

    Examples:
      | val1 | val2 | val3 | val4 | val5 | score |
      # rolls worth points
      | 1    | 2    | 3    | 4    | 5    | 15    |
      | 1    | 1    | 1    | 1    | 1    | 5     |

  @ScoreYahtzee
  Scenario Outline: Player scoring Yahtzee
    When Player scores <val1>, <val2>, <val3>, <val4>, <val5> into category 13
    Then Player gets <score> in category 13

    Examples:
      | val1 | val2 | val3 | val4 | val5 | score |
      # rolls worth points
      | 1    | 1    | 1    | 1    | 1    | 50    |
      | 6    | 6    | 6    | 6    | 6    | 50    |
      # rolls worth 0
      | 1    | 2    | 3    | 4    | 6    | 0     |
      | 1    | 1    | 2    | 2    | 3    | 0     |
      | 1    | 6    | 6    | 6    | 6    | 0     |

    @ScoreYahtzeeBonus
    Scenario:
      Given Player has already scored one Yahtzee
      When Player scores another Yahtzee
      Then Player gets bonus points