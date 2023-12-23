package cs3500.klondike;

import cs3500.klondike.controller.KlondikeController;
import cs3500.klondike.controller.KlondikeTextualController;

import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;


import cs3500.klondike.view.KlondikeTextualView;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;


/**
 * Represents a card in a game of Klondike.
 */
public class ExamplarControllerTests {
  KlondikeModel model = new BasicKlondike();
  List<Card> deck = model.getDeck();
  StringBuilder appendable = new StringBuilder();
  StringBuilder output = new StringBuilder();


  List<Card> exampleCards;

  List<Card> allCards;
  BasicKlondike bk;

  @Before
  public void init() {
    bk = new BasicKlondike();
    exampleCards = new ArrayList<Card>();
    allCards = bk.getDeck();

  }

  @Test
  public void testMoveDrawInput() {
    StringReader r = new StringReader("md 4\nq");
    StringBuilder gameLog = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(r, gameLog);
    controller.playGame(model, deck, false, 2, 1);
    String[] lines = gameLog.toString().split("\n");
    Assert.assertTrue(lines.length >= 1);
  }


  @Test
  public void testInvalidInputCommands() {
    StringReader r = new StringReader("mpp 1 2 2\nmdf abc\nmpp 3 2 4\nq");
    StringBuilder gameLog = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(r, gameLog);

    // We expect the controller to handle invalid input and continue the game.
    controller.playGame(model, deck, false, 2, 1);

    // Check if the output contains messages for invalid input.
    String output = gameLog.toString();
    Assert.assertTrue(output.contains("Invalid move. Play again."));
    Assert.assertTrue(output.contains("Invalid move. Play again."));

  }

  @Test
  public void testQuit() {
    KlondikeModel klondike = new BasicKlondike();
    Readable r = new StringReader("q");
    Appendable a = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(r, a);
    controller.playGame(klondike, deck, false, 4, 5);
    String output = a.toString();
    KlondikeTextualView view = new KlondikeTextualView(klondike);
    Assert.assertTrue(output.contains("Game quit!\n" +
            "State of game when quit:\n" +
            view.toString() + "\n" +
            "Score: 0"));
  }

  @Test
  public void testMovePile() {
    KlondikeModel klondike = new BasicKlondike();
    List<Card> deck = klondike.getDeck();
    ExamplarControllerTests controller = new ExamplarControllerTests();
    deck = controller.orderedDeck(deck);
    Appendable a = new StringBuilder();
    Readable r = new StringReader("mpp 3 1 5 q");
    KlondikeController c1 = new KlondikeTextualController(r, a);
    c1.playGame(klondike, deck, false, 5, 5);
    String output = a.toString();
    Assert.assertTrue(output.contains("2♡"));

  }

  @Test
  public void testMoveDraw() {
    KlondikeModel klondike = new BasicKlondike();
    List<Card> deck = klondike.getDeck();
    ExamplarControllerTests controller = new ExamplarControllerTests();
    deck = controller.validDeck(deck);
    Appendable a = new StringBuilder();
    Readable r = new StringReader("md 2 q");
    KlondikeController c1 = new KlondikeTextualController(r, a);
    c1.playGame(klondike, deck, false, 5, 5);
    String output = a.toString();
    Assert.assertFalse(output.contains("Invalid"));


  }

  /**
   * Checks if the input is 'q' or 'Q' and returns true if so.
   * @param d the input
   * @return true if the input is 'q' or 'Q'
   */
  public List<Card> orderedDeck(List<Card> d) {
    List<String> values = new ArrayList<>(Arrays.asList("A♣", "A♠", "A♡", "A♢", "2♣", "2♠", "2♡",
            "2♢", "3♣", "3♠", "3♡", "3♢", "4♣", "4♠", "4♡", "4♢", "5♣", "5♠", "5♡", "5♢", "6♣",
            "6♠",
            "6♡", "6♢", "7♣", "7♠", "7♡", "7♢", "8♣", "8♠", "8♡", "8♢", "9♣", "9♠",
            "9♡", "9♢", "10♣",
            "10♠", "10♡", "10♢", "J♣", "J♠", "J♡", "J♢", "Q♣", "Q♠", "Q♡", "Q♢", "K♣", "K♠", "K♡",
            "K♢"));

    List<Card> newCards = new ArrayList<>();
    for (int i = 0; i < values.size(); i++) {
      for (Card c : d) {
        if (c.toString().equals(values.get(i))) {
          newCards.add(c);
        }
      }
    }
    return newCards;


  }

  /**
   * Checks if the input is 'q' or 'Q' and returns true if so.
   * @param d  the input
   * @return  true if the input is 'q' or 'Q'
   */
  public List<Card> validDeck(List<Card> d) {
    List<String> values = new ArrayList<>(Arrays.asList("A♣", "A♠", "4♢", "A♢", "2♣", "2♠", "2♡",
        "2♢", "3♣", "3♠", "3♡", "3♢", "4♣", "4♠", "4♡", "A♡", "5♣", "5♠", "5♡", "5♢", "6♣", "6♠",
        "6♡", "6♢", "7♣", "7♠", "7♡", "7♢", "8♣", "8♠", "8♡", "8♢", "9♣", "9♠", "9♡", "9♢", "10♣",
        "10♠", "10♡", "10♢", "J♣", "J♠", "J♡", "J♢", "Q♣", "Q♠", "Q♡", "Q♢", "K♣", "K♠", "K♡",
        "K♢"));

    List<Card> newCards = new ArrayList<>();
    for (int i = 0; i < values.size(); i++) {
      for (Card c : d) {
        if (c.toString().equals(values.get(i))) {
          newCards.add(c);
        }
      }
    }
    return newCards;


  }


}


