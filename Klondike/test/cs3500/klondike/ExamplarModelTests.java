package cs3500.klondike;

import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a card in a game of Klondike.
 */
public class ExamplarModelTests {

  private KlondikeModel model;
  private List<Card> deck;

  @Before
  public void init() {
    model = new BasicKlondike();
    deck = new ArrayList<>();
    deck.add(getCard("A♠"));
    deck.add(getCard("A♣"));
    deck.add(getCard("A♡"));
    deck.add(getCard("A♢"));


  }

  /**
   * Tests that the deck is created correctly.
   */
  public Card getCard(String card) {
    List<Card> onlyAces = new BasicKlondike().getDeck();
    List<Card> certainCards = onlyAces.stream().filter(c -> c.toString().equals(card))
            .collect(Collectors.toList());
    return certainCards.get(0);
  }



  @Test
  //When there are not enough cards to move from pile to pile
  public void testsNotEnoughCards() {
    BasicKlondike game = new BasicKlondike();
    game.startGame(game.getDeck(), true, 2, 1);
    Assert.assertThrows(IllegalArgumentException.class, () -> game.movePile(0, 2, 1));

  }

  @Test
  //Tests if the game has not been started yet
  public void testGameStarted() {

    BasicKlondike game = new BasicKlondike();
    game.startGame(game.getDeck(), false, 1, 1);
    game.discardDraw();
    Assert.assertThrows(IllegalStateException.class, () -> game.moveDraw(0));

  }

  @Test
  //tests if there are no available draws
  public void testNoDraws() {
    BasicKlondike game = new BasicKlondike();
    game.startGame(game.getDeck(), true, 1, 1);
    for (Card drawCard : game.getDrawCards()) {
      game.discardDraw();
    }
    Assert.assertThrows(IllegalStateException.class, () -> game.moveDraw(0));

  }





  @Test
  //tests if there are no cards to move draw
  public void testNoCardsToMoveDraw() {
    BasicKlondike game = new BasicKlondike();
    game.startGame(game.getDeck(), true, 2, 1);
    for (Card drawCard : game.getDrawCards()) {
      game.discardDraw();
    }
    Assert.assertThrows(IllegalStateException.class, () -> game.moveDraw(0));

  }

  @Test
  //tests when the foundation pile is invalid
  public void testNoCardForFoundation() {
    BasicKlondike game = new BasicKlondike();
    game.startGame(game.getDeck(), true, 2, 1);
    for (Card drawCard : game.getDrawCards()) {
      game.discardDraw();
    }
    Assert.assertThrows(IllegalStateException.class, () -> game.moveToFoundation(0, 1));

  }

  @Test
  //tests when cards are moved from draw to foundation and the foundation pile is invalid
  public void testInvalidFoundationPile() {
    BasicKlondike game = new BasicKlondike();
    game.startGame(game.getDeck(), true, 2, 1);
    Assert.assertThrows(IllegalArgumentException.class, () -> game.moveDrawToFoundation(4));

  }




  @Test
  //tests if there are no cards to move to the pile
  public void testNoCardsToPile() {
    BasicKlondike game = new BasicKlondike();
    game.startGame(game.getDeck(), true, 2, 1);
    for (Card drawCard : game.getDrawCards()) {
      game.discardDraw();
    }
    Assert.assertThrows(IllegalStateException.class, () -> game.movePile(1, 1, 0));

  }

  @Test(expected = IllegalArgumentException.class)
  public void testMovePileToSamePile() {
    model.startGame(model.getDeck(), false, 4, 2);
    model.movePile(1, 1, 1);

  }


  @Test
  //test if the pile is non existant
  public void testNoneExistantPile() {
    List<Card> deck = this.model.getDeck();
    this.model.startGame(deck, false, 4, 4);
    Assert.assertThrows(IllegalStateException.class,
        () -> this.model.movePile(0, 1, 1));
  }


  @Test
  //tests if the suit is red to red or black to black
  public void testWrongSuit() {
    BasicKlondike game = new BasicKlondike();
    game.startGame(game.getDeck(), true, 2, 1);
    String openCardValueInPile2 = game.getCardAt(1,1).toString();
    boolean redOpenCard = openCardValueInPile2.contains("♡") || openCardValueInPile2.contains("♢");
    boolean blackOpenCard =
        openCardValueInPile2.contains("♣") || openCardValueInPile2.contains("♠");
    for (Card drawCard: game.getDrawCards()) {
      boolean redDrawCard = drawCard.toString().contains("♡") || drawCard.toString().contains("♢");
      boolean blackDrawCard =
          drawCard.toString().contains("♣") || drawCard.toString().contains("♠");
      if ((redOpenCard && redDrawCard) || (blackDrawCard && blackOpenCard)) {
        break;
      } else {
        game.discardDraw();
      }
    }
    Assert.assertThrows(IllegalStateException.class, () -> game.moveDraw(1));

  }



  @Test
  //make sure pile is reducing by number
  public void testPileReduceByNumber() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = new ArrayList<>();
    deck.add(findCard("A♣"));
    deck.add(findCard("2♣"));
    deck.add(findCard("2♡"));
    deck.add(findCard("2♢"));
    deck.add(findCard("2♠"));
    deck.add(findCard("A♡"));
    deck.add(findCard("A♢"));
    deck.add(findCard("A♠"));

    model.startGame(deck, false, 2, 1);
    model.movePile(0, 1, 1);
    Assert.assertEquals(0, model.getPileHeight(0));

  }

  @Test
  //tests invalid move
  public void testInvalidPileMove() {
    KlondikeModel model = new BasicKlondike();
    List<Card> deck = model.getDeck();
    model.startGame(deck, false, 7, 5);
    Assert.assertThrows(IllegalArgumentException.class,
        () -> model.moveToFoundation(2, 5));
  }

  @Test
  //test move to foundation from empty pile
  public void testFoundationFromEmptyPile() {
    KlondikeModel model = new BasicKlondike();
    List<Card> deck = model.getDeck();
    model.startGame(deck, true, 6, 10);
    Assert.assertThrows(IllegalStateException.class,
        () -> model.moveToFoundation(0, 0));
  }


  @Test
  //checks if the pile is consistent throughout the game
  public void testPileConsistancy() {
    model.startGame(model.getDeck(), true, 6, 7);
    int cardSize = this.model.getDrawCards().size();

    for (int i = 0; i < deck.size(); i++) {
      this.model.discardDraw();
    }
    Assert.assertEquals(cardSize, model.getDrawCards().size());
  }


  /**
   * Finds the card in the deck.
   * @param name
   * represents the name of the card
   */
  public Card findCard(String name) {
    List<Card> deck = new BasicKlondike().getDeck();
    List<Card> actualCard = deck.stream().filter(c -> c.toString().equals(name))
            .collect(Collectors.toList());
    return actualCard.get(0);

  }





}




