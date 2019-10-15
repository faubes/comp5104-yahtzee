@DiceLogic
Feature: Dice Logic

  @equivalentRolls
  Scenario Outline: Equivalent rolls
    Given One roll <first>, <second>, <third>, <fourth>, <fifth>
    And Another roll <sixth>, <seventh>, <eighth>, <ninth>, <tenth>
    Then Roll one and roll two are equivalent

    Examples:
      | first | second | third | fourth | fifth | sixth | seventh | eighth | ninth | tenth |
      | 1     | 2      | 3     | 4      | 5     | 5     | 4       | 3      | 2     | 1     |
      | 1     | 2      | 3     | 4      | 5     | 3     | 4       | 5      | 2     | 1     |
      | 2     | 2      | 5     | 5      | 5     | 5     | 2       | 5      | 2     | 5     |
      | 2     | 2      | 5     | 5      | 5     | 5     | 5       | 5      | 2     | 2     |

  @countMultiples
  Scenario Outline: Count multiples
    Given One roll <first>, <second>, <third>, <fourth>, <fifth>
    Then There are <num> <val>'s
    Examples:
      | first | second | third | fourth | fifth | num | val |
      | 1     | 1      | 1     | 1      | 1     | 5   | 1   |
      | 1     | 1      | 1     | 1      | 1     | 0   | 6   |
      | 4     | 2      | 3     | 4      | 4     | 3   | 4   |

