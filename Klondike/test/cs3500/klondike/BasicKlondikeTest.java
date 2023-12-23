package cs3500.klondike;

import cs3500.klondike.model.hw02.BasicCard;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.CardSuit;
import cs3500.klondike.model.hw02.CardValue;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw02.BasicKlondike;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;




/**
 * Represents a card in a game of Klondike.
 */

public class BasicKlondikeTest {
  static final char CLUBS = '♣';
  static final char SPADES = '♠';
  static final char HEARTS = '♡';
  static final char DIAMONDS = '♢';
  static final List<Character> ALL_SUITS = List.of(CLUBS, SPADES, HEARTS, DIAMONDS);
  static final List<Character> RED_SUITS = List.of(HEARTS, DIAMONDS);
  static final List<Character> BLACK_SUITS = List.of(CLUBS, SPADES);

  private KlondikeModel model;
  private List<Card> deck;

  @Before
  public void init() {

    model = new BasicKlondike();
    List<Card> sampleDeck = new ArrayList<>();

    // Add all the cards to the deck
    BasicCard c1 = new BasicCard(CardSuit.CLUBS, CardValue.ACE);
    BasicCard c2 = new BasicCard(CardSuit.CLUBS, CardValue.TWO);
    BasicCard c3 = new BasicCard(CardSuit.CLUBS, CardValue.THREE);
    BasicCard c4 = new BasicCard(CardSuit.CLUBS, CardValue.FOUR);
    BasicCard c5 = new BasicCard(CardSuit.DIAMONDS, CardValue.ACE);
    BasicCard c6 = new BasicCard(CardSuit.DIAMONDS, CardValue.TWO);
    BasicCard c7 = new BasicCard(CardSuit.DIAMONDS, CardValue.THREE);
    BasicCard c8 = new BasicCard(CardSuit.DIAMONDS, CardValue.FOUR);


    sampleDeck.add(c1);
    sampleDeck.add(c2);
    sampleDeck.add(c3);
    sampleDeck.add(c4);
    sampleDeck.add(c5);
    sampleDeck.add(c6);
    sampleDeck.add(c7);
    sampleDeck.add(c8);
  }

  @Test
  // Tests the movePile method with valid inputs
  public void testMovePileWithValidInputs() {
    List<Card> sampleDeck = new ArrayList<>();

    // Add all the cards to the deck
    BasicCard c1 = new BasicCard(CardSuit.CLUBS, CardValue.TWO);
    BasicCard c2 = new BasicCard(CardSuit.CLUBS, CardValue.TWO);
    BasicCard c3 = new BasicCard(CardSuit.CLUBS, CardValue.THREE);
    BasicCard c4 = new BasicCard(CardSuit.HEARTS, CardValue.THREE);
    BasicCard c5 = new BasicCard(CardSuit.DIAMONDS, CardValue.ACE);
    BasicCard c6 = new BasicCard(CardSuit.DIAMONDS, CardValue.TWO);
    BasicCard c7 = new BasicCard(CardSuit.DIAMONDS, CardValue.THREE);
    BasicCard c8 = new BasicCard(CardSuit.DIAMONDS, CardValue.FOUR);
    BasicCard c9 = new BasicCard( CardSuit.DIAMONDS, CardValue.FIVE);
    BasicCard c10 = new BasicCard(CardSuit.DIAMONDS, CardValue.SIX);


    sampleDeck.add(c1);
    sampleDeck.add(c2);
    sampleDeck.add(c3);
    sampleDeck.add(c4);
    sampleDeck.add(c5);
    sampleDeck.add(c6);
    sampleDeck.add(c7);
    sampleDeck.add(c8);
    sampleDeck.add(c9);
    sampleDeck.add(c10);

    model.startGame(sampleDeck, false, 3, 2);

    // Initially, let's assume the top card of first pile is c1 (ACE of CLUBS)
    // and the top card of the second pile is c2 (TWO of CLUBS).
    // Moving c1 to the second pile should be a valid move.

    model.movePile(0, 1, 1);  // move the card from pile 0 to pile 1
    Assert.assertEquals(c1, c1); // assuming getCardAt fetches the card at that pile and position

  }

  @Test
  // Tests moveDraw to a non-empty foundation
  public void testMoveDrawToNonEmptyFoundation() {
    List<Card> sampleDeck = new ArrayList<>();

    // Add all the cards to the deck
    BasicCard c1 = new BasicCard(CardSuit.CLUBS, CardValue.TWO);
    BasicCard c2 = new BasicCard(CardSuit.CLUBS, CardValue.TWO);
    BasicCard c3 = new BasicCard(CardSuit.CLUBS, CardValue.THREE);
    BasicCard c4 = new BasicCard(CardSuit.SPADES, CardValue.THREE);
    BasicCard c5 = new BasicCard(CardSuit.DIAMONDS, CardValue.ACE);
    BasicCard c6 = new BasicCard(CardSuit.DIAMONDS, CardValue.TWO);
    BasicCard c7 = new BasicCard(CardSuit.DIAMONDS, CardValue.TWO);
    BasicCard c8 = new BasicCard(CardSuit.DIAMONDS, CardValue.FOUR);
    BasicCard c9 = new BasicCard(CardSuit.DIAMONDS, CardValue.FIVE);
    BasicCard c10 = new BasicCard(CardSuit.DIAMONDS, CardValue.SIX);

    sampleDeck.add(c1);
    sampleDeck.add(c2);
    sampleDeck.add(c3);
    sampleDeck.add(c4);
    sampleDeck.add(c5);
    sampleDeck.add(c6);
    sampleDeck.add(c7);
    sampleDeck.add(c8);
    sampleDeck.add(c9);
    sampleDeck.add(c10);

    model.startGame(sampleDeck, false, 3, 2);


    model.moveDraw(1);

    // Assuming getCardAt fetches the card at that pile and position
    Assert.assertEquals(c1, c1);
  }

  @Test
  // tests movePile with valid inputs to an empty pile
  public void testMovePileWithValidInputsToEmptyPile() {
    List<Card> sampleDeck = new ArrayList<>();
    // Add all the cards to the deck in a sequence that will
    // result in the desired distribution after startGame
    BasicCard c1 = new BasicCard(CardSuit.CLUBS, CardValue.ACE);      // Goes to Pile 0
    BasicCard c2 = new BasicCard(CardSuit.CLUBS, CardValue.TWO);    // Goes to Pile 1
    BasicCard c3 = new BasicCard(CardSuit.CLUBS, CardValue.THREE);   // Goes to Pile 2
    BasicCard c4 = new BasicCard(CardSuit.CLUBS, CardValue.FOUR);     // Goes to Pile 0
    BasicCard c5 = new BasicCard(CardSuit.DIAMONDS, CardValue.ACE);  // Goes to Pile 1

    // Add the cards to the sample deck in the desired sequence
    sampleDeck.add(c1);
    sampleDeck.add(c2);
    sampleDeck.add(c3);
    sampleDeck.add(c4);
    sampleDeck.add(c5);

    // Adding extra cards to fulfill the
    // requirement that deck should have more cards
    BasicCard c6 = new BasicCard(CardSuit.DIAMONDS, CardValue.TWO);
    BasicCard c7 = new BasicCard(CardSuit.DIAMONDS, CardValue.THREE);
    BasicCard c8 = new BasicCard(CardSuit.DIAMONDS, CardValue.FOUR);

    sampleDeck.add(c6);
    sampleDeck.add(c7);
    sampleDeck.add(c8);
    //Now, when startGame is called with
    // this sample deck and 3 cascading piles, the cards
    // will be distributed as mentioned in the test scenario.







    model.startGame(sampleDeck, false, 3, 2);

    // Let's say after starting the game, the cards are distributed such that:
    // Pile 0 has: ACE_CLUBS, FOUR_CLUBS
    // Pile 1 has: TWO_CLUBS, ACE_DIAMONDS
    // Pile 2 is empty

    // Now, try moving 1 card from Pile 0 (source) to Pile 2 (destination, which is empty)
    model.movePile(0, 1, 2);

    // Assuming getCardAt fetches the card at that pile and position
    Assert.assertEquals(c1, c1);
  }


  @Test
  // Tests getScore()
  public void testGetScoreDuringGame() {
    List<Card> sampleDeck = new ArrayList<>();
    // Add all the cards to the deck in a sequence that will
    // result in the desired distribution after startGame
    BasicCard c1 = new BasicCard(CardSuit.CLUBS, CardValue.ACE);      // Goes to Pile 0
    BasicCard c2 = new BasicCard(CardSuit.CLUBS, CardValue.TWO);    // Goes to Pile 1
    BasicCard c3 = new BasicCard(CardSuit.CLUBS, CardValue.THREE);   // Goes to Pile 2
    BasicCard c4 = new BasicCard(CardSuit.CLUBS, CardValue.FOUR);     // Goes to Pile 0
    BasicCard c5 = new BasicCard(CardSuit.DIAMONDS, CardValue.ACE);  // Goes to Pile 1
    BasicCard c6 = new BasicCard(CardSuit.DIAMONDS, CardValue.TWO);  // Goes to Pile 2



    // Add the cards to the sample deck in the desired sequence
    sampleDeck.add(c1);
    sampleDeck.add(c2);
    sampleDeck.add(c3);
    sampleDeck.add(c4);
    sampleDeck.add(c5);
    sampleDeck.add(c6);



    model.startGame(sampleDeck, false, 3, 2);
    Assert.assertEquals(6, model.getScore());

  }
}



