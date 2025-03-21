Feature: Use CharacterBrowser

  Scenario: Show character list
    Given I open the app
    Then "Rick Sanchez" is shown
    Then "Morty Smith" is shown

  Scenario: Show character detail
    Given I open the app
    When I click on "Rick Sanchez"
    Then "Rick Sanchez" is shown
    Then the character is a "Human"

  Scenario: Scroll in the character list
    Given I open the app
    When I scroll to "Alien Morty"
    Then "Alien Morty" is shown

  Scenario: Scroll and show character detail
    Given I open the app
    When I scroll to "Alien Morty"
    When I click on "Alien Morty"
    Then "Alien Morty" is shown
    Then the character is a "Alien"

  Scenario: Navigate back to character list
    Given I open the app
    When I click on "Rick Sanchez"
    When I go back
    Then "Rick Sanchez" is shown
    Then "Morty Smith" is shown
    Then "Summer Smith" is shown

  Scenario: Browse, go back, scroll and show character detail
    Given I open the app
    When I click on "Rick Sanchez"
    When I go back
    When I scroll to "Alien Morty"
    When I click on "Alien Morty"
    Then "Alien Morty" is shown
    Then the character is a "Alien"
