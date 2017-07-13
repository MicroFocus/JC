@TID4002REV0.2.0
@BLABLA
Feature: hello world

  Background: hello background
    this is a long description of the background!!!
    Given I am logged in

  Scenario Outline: This is the <num> line
    Given I am in the "<num>" line
    When when I do "<thing>"
    Then I can "<thing>" to "<num>"

    Examples:
    | num    | thing |
    | first  | jump  |
    | second | walk  |