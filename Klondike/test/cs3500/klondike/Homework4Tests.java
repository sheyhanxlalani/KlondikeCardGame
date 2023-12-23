package cs3500.klondike;


import cs3500.klondike.controller.KlondikeTextualController;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw04.LimitedDrawKlondike;
import cs3500.klondike.model.hw04.WhiteheadKlondike;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests for the extended models and controllers.
 */
public class Homework4Tests {
  // tests for LimitedDrawKlondike

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


  @Test
  public void testDiscardDrawForLimitedKlondike() {
    KlondikeModel model = new LimitedDrawKlondike(1);
    List<Card> newdeck = makeCardList(List.of("A♣", "2♣", "3♣", "A♢",
        "2♢", "3♢", "A♡", "2♡", "3♡"), new LimitedDrawKlondike(1));
    model.startGame(newdeck, false, 3, 1);
    model.discardDraw();
    model.discardDraw();
    model.discardDraw();
    model.discardDraw();
    model.discardDraw();
    model.discardDraw();

    Assert.assertEquals(0, ((LimitedDrawKlondike) model).getNumDrawCards());

  }

  @Test
  public void testDiscardDrawForLimitedKlondikeWhereCardsHaveTwoRedraws() {
    KlondikeModel model = new LimitedDrawKlondike(2);
    List<Card> newdeck = makeCardList(List.of("A♣", "2♣", "3♣", "A♢",
        "2♢", "3♢", "A♡", "2♡", "3♡"), new LimitedDrawKlondike(2));
    model.startGame(newdeck, false, 3, 1);
    model.discardDraw();
    model.discardDraw();
    model.discardDraw();
    model.discardDraw();
    model.discardDraw();
    model.discardDraw();
    Assert.assertEquals(3, ((LimitedDrawKlondike) model).getNumDrawCards());

  }

  // tests for WhiteheadKlondike

  @Test
  public void testMovePileForWhiteHeadKlondike() {
    WhiteheadKlondike model = new WhiteheadKlondike();
    Readable read = new StringReader("mpp 1 1 2\nq");
    Appendable app = new StringBuilder();
    KlondikeTextualController controller = new KlondikeTextualController(read, app);
    List<Card> newdeck = makeCardList(List.of("5♣", "7♣", "4♣", "6♣", "2♢", "3♢", "A♡", "2♡",
        "3♡", "7♡", "5♢", "4♢", "6♡", "7♢", "5♡", "4♡", "6♢", "2♣", "3♣", "A♣", "A♢"),
        new WhiteheadKlondike());
    controller.playGame(model, newdeck, false, 3, 1);
    Assert.assertEquals(app.toString(),
                 "Draw: A♡\n"
                + "Foundation: <none>, <none>, <none>\n"
                + " 5♣ 7♣ 4♣\n"
                + "    6♣ 2♢\n"
                + "       3♢\n"
                + "Score: 0\n"
                + "Draw: A♡\n"
                + "Foundation: <none>, <none>, <none>\n"
                + "  X 7♣ 4♣\n"
                + "    6♣ 2♢\n"
                + "    5♣ 3♢\n"
                + "Score: 0\n"
                + "Game quit!\n"
                + "State of game when quit:\n"
                + "Draw: A♡\n"
                + "Foundation: <none>, <none>, <none>\n"
                + "  X 7♣ 4♣\n"
                + "    6♣ 2♢\n"
                + "    5♣ 3♢\n"
                + "Score: 0\n"

    );
  }

  @Test
  public void testMovingAnyCardToEmptyPileForWhiteHead() {
    WhiteheadKlondike model = new WhiteheadKlondike();
    Readable read = new StringReader("mpp 1 1 2 mpp 3 1 1\nq");
    Appendable app = new StringBuilder();
    KlondikeTextualController controller = new KlondikeTextualController(read, app);
    List<Card> newdeck = makeCardList(List.of("5♣", "7♣", "4♣", "6♣",
        "2♢", "3♢", "A♡", "2♡", "3♡",
        "7♡", "5♢", "4♢", "6♡", "7♢", "5♡", "4♡", "6♢", "2♣", "3♣", "A♣", "A♢"),
        new WhiteheadKlondike());

    controller.playGame(model, newdeck, false, 3, 1);
    Assert.assertTrue(app.toString().contains(
        "Draw: A♡\n"
            + "Foundation: <none>, <none>, <none>\n"
            + " 3♢ 7♣ 4♣\n"
            + "    6♣ 2♢\n"
            + "    5♣   \n"
            + "Score: 0"
        )
    );

  }

  @Test
  public void testMovingMultipleCards() {
    WhiteheadKlondike model = new WhiteheadKlondike();
    Readable read = new StringReader("mpp 3 3 1\nq");
    Appendable app = new StringBuilder();
    KlondikeTextualController controller = new KlondikeTextualController(read, app);
    List<Card> newdeck = makeCardList(List.of("5♣", "7♣", "4♣", "6♣", "2♣", "3♣", "A♡", "2♡", "3♡",
        "7♡", "5♢", "4♢", "6♡", "7♢", "5♡", "4♡", "6♢", "2♢", "3♢", "A♣", "A♢"),
        new WhiteheadKlondike());

    controller.playGame(model, newdeck, false, 3, 1);
    Assert.assertTrue(app.toString().contains(
            "Draw: A♡\n"
                + "Foundation: <none>, <none>, <none>\n"
                + " 5♣ 7♣  X\n"
                + " 4♣ 6♣   \n"
                + " 2♣      \n"
                + " 3♣      \n"
                + "Score: 0"

    ));
  }

}
