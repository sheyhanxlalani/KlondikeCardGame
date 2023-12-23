package cs3500.klondike;


import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw04.LimitedDrawKlondike;
import cs3500.klondike.model.hw04.WhiteheadKlondike;

import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;


/**
 * Tests for the extended models and controllers.
 */
public class ExamplarExtendedModelTests {

  /**
   * Makes a new deck of cards that takes in a deck and adds given cards to the deck.
   * then returns a new deck.
   * @param ogdeck the original deck
   * @param card the card to be added
   * @param newdeck the new deck
   */
  private void deckMaker(List<Card> ogdeck, String card, List<Card> newdeck) {
    for (int i = 0; i < ogdeck.size(); i++) {
      if (ogdeck.get(i).toString().contains(card)) {
        newdeck.add(ogdeck.get(i));
      }
    }
  }

  /**
   * Makes a new card list that takes in a list of cards and a model and returns a new deck.
   * @param cards the cards to be added
   * @param km the model
   */
  public List<Card> makeCardList(List<String> cards, KlondikeModel km) {
    List<Card> deck = km.getDeck();
    List<Card> newdeck = new ArrayList<>();
    for (String card : cards) {
      deckMaker(deck, card, newdeck);
    }
    return newdeck;
  }


  // Tests for valid pile move
  @Test
  public void whiteHeadTestValidMovePile() {
    WhiteheadKlondike model = new WhiteheadKlondike();

    List<Card> newdeck =
        makeCardList(List.of("A♣", "2♣", "3♣", "A♢", "2♢", "3♢", "A♡", "2♡", "3♡"),
            new WhiteheadKlondike());

    model.startGame(newdeck, false, 3, 1);
    model.moveToFoundation(0,0);
    model.moveToFoundation(1,1);

    Assert.assertThrows(IllegalStateException.class, () -> model.movePile(1, 1, 2));
  }

  // Tests for when the draw pile is empty in a limited draw game
  @Test
  public void testLimitedEmptyDraw() {
    KlondikeModel model = new LimitedDrawKlondike(1);

    List<Card> newdeck =
        makeCardList(List.of("4♠", "3♠", "2♠", "A♠",
                "4♣", "3♣", "2♣", "A♣", "4♢", "3♢", "2♢", "A♢"),
            new LimitedDrawKlondike(1));

    model.startGame(newdeck, false, 4, 2);
    model.discardDraw();
    model.discardDraw();
    model.discardDraw();
    model.discardDraw();
    Assert.assertTrue(model.getDrawCards().isEmpty());

  }

  // tests moving a pile to an empty pile
  @Test
  public void testDiffSuitToEmptyPile() {
    WhiteheadKlondike model = new WhiteheadKlondike();

    List<Card> newdeck =
        makeCardList(List.of("2♡", "A♣", "2♣",
                "A♢", "2♢", "2♠", "A♠", "A♡"),
            new WhiteheadKlondike());


    model.startGame(newdeck, false, 3, 2);
    model.movePile(1, 1, 0);
    model.moveToFoundation(1,0);
    Assert.assertThrows(IllegalStateException.class, () -> model.movePile(0, 2, 1));

  }

  // tests WhiteHead moving a pile to an empty pile
  @Test
  public void testPileToEmptyPile() {
    WhiteheadKlondike model = new WhiteheadKlondike();

    List<Card> newdeck =
        makeCardList(List.of("2♣", "4♣", "3♣", "A♠", "2♠", "A♣", "3♠", "4♠"),
            new WhiteheadKlondike());

    model.startGame(newdeck, false, 2, 1);
    model.movePile(0, 1, 1);
    model.movePile(1, 3, 0);
    Assert.assertEquals(model.getPileHeight(0), 3);



  }

}
