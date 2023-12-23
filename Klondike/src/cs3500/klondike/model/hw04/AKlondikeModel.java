package cs3500.klondike.model.hw04;

import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw02.CardSuit;
import cs3500.klondike.model.hw02.CardValue;
import cs3500.klondike.model.hw02.BasicCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Represents a game of Klondike that can be a
 * basic, limited draw, or whitehead game.
 */
public class AKlondikeModel implements KlondikeModel {


  protected static final List<CardSuit> suits = List.of(CardSuit.HEARTS, CardSuit.DIAMONDS,
      CardSuit.CLUBS, CardSuit.SPADES);

  protected static final List<CardValue> ranks = List.of(CardValue.ACE,
      CardValue.TWO, CardValue.THREE, CardValue.FOUR,
      CardValue.FIVE, CardValue.SIX, CardValue.SEVEN, CardValue.EIGHT, CardValue.NINE,
      CardValue.TEN, CardValue.JACK,
      CardValue.QUEEN, CardValue.KING);

  protected boolean gameHappening;

  protected int maxDrawVisible;

  protected List<List<BasicCard>> cascadePiles;

  protected List<List<BasicCard>> foundationPiles;

  protected List<BasicCard> drawPile;

  /**
   * Constructs the BasicKlondike object.
   */
  public AKlondikeModel() {
    this.foundationPiles = new ArrayList<>();
    this.cascadePiles = new ArrayList<>();
    this.drawPile = new ArrayList<>();
  }

  /**
   * Constructs a {@code BasicKlondike} object.
   * Initializes foundation, cascade and draw piles.
   *
   * @return the deck of cards as a list
   */
  @Override
  public List<Card> getDeck() {
    // parse through all the suits and ranks to create a deck
    List<Card> deck = new ArrayList<>();
    for (CardSuit suit : suits) {
      for (CardValue rank : ranks) {
        // add a new Klondike Card to the list
        deck.add(new BasicCard(suit, rank));
      }
    }
    // return deck
    return deck;
  }

  /**
   * <p>Deal a new game of Klondike.
   * The cards to be used and their order are specified by the the given deck,
   * unless the {@code shuffle} parameter indicates the order should be ignored.</p>
   *
   * <p>This method first verifies that the deck is valid. It deals cards in rows
   * (left-to-right, top-to-bottom) into the characteristic cascade shape
   * with the specified number of rows, followed by (at most) the specified number of
   * draw cards. When {@code shuffle} is {@code false}, the {@code deck} must be used in
   * order and the 0th card in {@code deck} is used as the first card dealt.
   * There will be as many foundation piles as there are Aces in the deck.</p>
   *
   * <p>A valid deck must consist cards that can be grouped into equal-length,
   * consecutive runs of cards (each one starting at an Ace, and each of a single
   * suit).</p>
   *
   * <p>This method should have no side effects other than configuring this model
   * instance, and should work for any valid arguments.</p>
   *
   * @param deck      the deck to be dealt
   * @param shuffle   if {@code false}, use the order as given by {@code deck},
   *                  otherwise use a randomly shuffled order
   * @param numPiles  number of piles to be dealt
   * @param numDraw   maximum number of draw cards available at a time
   * @throws IllegalStateException if the game has already started
   * @throws IllegalArgumentException if the deck is null or invalid,
   *                  a full cascade cannot be dealt with the given sizes,
   *                  or another input is invalid
   */
  @Override
  public void startGame(List<Card> deck, boolean shuffle, int numPiles, int numDraw)
      throws IllegalArgumentException, IllegalStateException {
    // check if game has started
    if (gameHappening) {
      throw new IllegalStateException("Game has already started");
    }
    // check for invalid inputs
    if (deck == null || deck.isEmpty()) {
      throw new IllegalArgumentException("Deck cannot be null");
    }
    // check for invalid inputs
    if (numDraw < 1) {
      throw new IllegalArgumentException("Number of draw cards must be greater than 0");
    }
    // check for invalid inputs
    if (numPiles < 1 || ((numPiles * (numPiles + 1)) / 2) > deck.size()) {
      throw new IllegalArgumentException("Number of piles must work with deck size");
    }

    // check for null card
    for (Card card : deck) {
      BasicCard c = (BasicCard) card;
      if (c == null) {
        throw new IllegalArgumentException("Deck contains null card");
      }
    }

    // get aces in deck
    List<Card> loa = deck.stream().filter(card -> ((BasicCard) card).isAce())
        .collect(Collectors.toList());

    // throw exception if deck does not contain aces
    if (loa.isEmpty()) {
      throw new IllegalArgumentException("Deck must contain at least one ace");
    }

    // check if deck contains all cards in a run
    List<Card> dupDeck = new ArrayList<>(deck);

    // get highest value in deck
    int highestValue = 1;
    for (Card card : dupDeck) {
      BasicCard c = (BasicCard) card;
      if (c.getValue() > highestValue) {
        highestValue = c.getValue();
      }
    }

    dupDeck.removeAll(loa);

    // check if deck contains all cards in a run
    for (Card card : loa) {
      BasicCard c = (BasicCard) card;
      for (int cval = 2; cval <= highestValue; cval++) {
        if (!dupDeck.remove(new BasicCard(c.getSuit(), ranks.get(cval - 1)))) {
          throw new IllegalArgumentException("Deck does not contain all cards in a run");
        }
      }
    }

    if (!dupDeck.isEmpty()) {
      throw new IllegalArgumentException("Invalid deck");
    }

    // shuffle deck if shuffle is true
    if (shuffle) {
      Collections.shuffle(deck);
    }

    // check for number of aces in deck
    int aceCount = 0;
    for (Card c : deck) {
      BasicCard card = (BasicCard) c;
      if (c == null) {
        throw new IllegalArgumentException("Deck contains null card");
      }
      if (((BasicCard) c).isAce()) {
        aceCount++;
      }
    }
    // for every ace in deck, add a foundation pile
    for (int i = 0; i < aceCount; i++) {
      this.foundationPiles.add(new ArrayList<>());
    }

    // deal the cards and flip the last card in each pile
    for (int i = 0; i < numPiles; i++) {
      this.cascadePiles.add(new ArrayList<>());
    }
    List<Card> dupeDeck = new ArrayList<>(deck);
    for (int i = 0; i < numPiles; i++) {
      for (int j = 0; j < numPiles; j++) {
        if (j >= i) {
          this.cascadePiles.get(j).add((BasicCard) dupeDeck.remove(0));
        }
      }
      this.cascadePiles.get(i).get(this.cascadePiles.get(i).size() - 1).flipFaceUp();
    }

    // add the rest of the cards to the draw pile
    for (Card c : dupeDeck) {
      this.drawPile.add((BasicCard) c);
    }
    //
    if (this.drawPile.size() > numDraw) {
      for (int i = 0; i < numDraw; i++) {
        this.drawPile.get(i).flipFaceUp();
      }
    } else {
      //
      for (BasicCard c : this.drawPile) {
        c.flipFaceUp();
      }
    }

    // set gameHappening to true and set maxDrawVisible
    this.gameHappening = true;
    this.maxDrawVisible = numDraw;
  }


  /**
   * Returns the card at the specified coordinates. If the specified card is not
   * available (e.g. it's covered by another card, or it's out of bounds),
   * @param srcPile  the 0-based index (from the left) of the source pile
   * @param numCards the 0-based index of the card to be moved from the source pile
   * @param destPile the 0-based index (from the left) of the destination pile
   * @throws IllegalStateException if the game hasn't been started yet
   * @throws IllegalArgumentException if any pile number is invalid
   * @throws IllegalStateException if the move is not allowable
   */
  @Override
  public void movePile(int srcPile, int numCards, int destPile)
      throws IllegalArgumentException, IllegalStateException {
    // check if game has started
    if (!gameHappening) {
      throw new IllegalStateException("Game has not yet started");
    }

    // check if srcPile is valid
    if (srcPile < 0 || srcPile >= this.cascadePiles.size()) {
      throw new IllegalArgumentException("The source pile number is invalid");
    } else if (srcPile == destPile) {
      throw new IllegalArgumentException("Destination pile cannot be the same as source pile");
    } else if (destPile < 0 || destPile >= this.cascadePiles.size()) {
      throw new IllegalArgumentException("Destination pile number is invalid");
    }
    else if (numCards < 1 || numCards > this.cascadePiles.get(srcPile).size()) {
      throw new IllegalArgumentException("Invalid number of cards to move");
    }

    // check if there are enough face up cards in srcPile
    int cardShownCount = 0;
    for (BasicCard c : this.cascadePiles.get(srcPile)) {
      if (c.isShowing) {
        cardShownCount++;
      }
    }
    if (cardShownCount < numCards) {
      throw new IllegalArgumentException("There are not enough face up cards in the source pile");
    }

    // check if top card of srcPile can be moved to destPile
    BasicCard topCard = this.cascadePiles.get(srcPile)
        .get(this.cascadePiles.get(srcPile).size() - numCards);

    // check if destPile is empty and if top card is a king
    if (!topCard.isKing() && this.cascadePiles.get(destPile).isEmpty()) {
      throw new IllegalStateException("You can only move a king to an empty pile");
    } // check if destPile is not empty and if top card can be moved to destPile
    else if (!this.cascadePiles.get(destPile).isEmpty()) {
      BasicCard destCard = this.cascadePiles.get(destPile)
          .get(this.cascadePiles.get(destPile).size() - 1);
      // check if top card is of different color and if top card stacks below destCard
      if (topCard.sameColor(destCard) || !topCard.stacksBelow(destCard)) {
        throw new IllegalStateException("Unable to move cards from source pile "
            + "to destination pile");
      }
    }

    // move cards from srcPile to destPile
    List<BasicCard> pileMove = this.cascadePiles.get(srcPile)
        .subList(this.cascadePiles.get(srcPile).size() - numCards,
            this.cascadePiles.get(srcPile).size());

    // flip the last card in srcPile if it is not empty
    this.cascadePiles.get(destPile).addAll(pileMove);
    // remove appropriate cards from srcPile
    this.cascadePiles.get(srcPile).removeAll(pileMove);
    // flip the last card in srcPile if it is not empty
    if (!this.cascadePiles.get(srcPile).isEmpty()) {
      this.cascadePiles.get(srcPile).get(this.cascadePiles.get(srcPile).size() - 1).flipFaceUp();
    }

  }


  /**
   * Returns the card at the specified coordinates. If the specified card is not
   * available (e.g. it's covered by another card, or it's out of bounds).
   * @param destPile the 0-based index (from the left) of the pile
   *                from which to draw the card
   * @throws IllegalStateException if the game hasn't been started yet
   * @throws IllegalArgumentException if the pile number is invalid
   */
  @Override
  public void moveDraw(int destPile) throws IllegalArgumentException, IllegalStateException {
    // check if game has started
    if (!gameHappening) {
      throw new IllegalStateException("Game has not yet started");
    }

    // check if destPile is valid and if there are any draw cards left
    if (this.drawPile.isEmpty()) {
      throw new IllegalArgumentException("No draw cards left");
    } // check if destPile is valid
    else if (destPile >= this.cascadePiles.size() || destPile < 0) {
      throw new IllegalStateException("Destination pile number is invalid");
    }

    // check if top card of drawPile can be moved to destPile
    BasicCard cardDrawn = this.drawPile.get(0);
    // check if destPile is empty and if top card is a king
    if (!cardDrawn.isKing() && this.cascadePiles.get(destPile).isEmpty()) {
      throw new IllegalStateException("Only king can be moved to empty pile");
    } else if (!this.cascadePiles.get(destPile).isEmpty()) {
      //
      BasicCard destCard = this.cascadePiles.get(destPile)
          .get(this.cascadePiles.get(destPile).size() - 1);
      if (!cardDrawn.stacksBelow(destCard) || cardDrawn.sameColor(destCard)) {
        throw new IllegalStateException("Cannot move draw card to destination pile");
      }
    }

    // move card from drawPile to destPile
    this.drawPile.remove(0);
    // flip the last card in drawPile if it is not empty
    if (this.drawPile.size() >= this.maxDrawVisible) {
      this.drawPile.get(this.maxDrawVisible - 1).flipFaceUp();
    }
    this.cascadePiles.get(destPile).add(cardDrawn);
  }

  /**
   * Moves the topmost draw-card directly to a foundation pile.
   * @param srcPile        the 0-based index (from the left) of the source pile
   * @param foundationPile the 0-based index (from the left) of the foundation
   *                       pile to place the card
   * @throws IllegalStateException the game hasn't been started yet
   * @throws IllegalArgumentException pile number is invalid
   * @throws IllegalStateException source pile is empty or if the move is not
   *                               allowable
   */
  @Override
  public void moveToFoundation(int srcPile, int foundationPile)
      throws IllegalArgumentException, IllegalStateException {
    // check if game has started
    if (!gameHappening) {
      throw new IllegalStateException("Game has not started");
    }

    // check if srcPile is valid
    if (foundationPile < 0 || foundationPile >= this.foundationPiles.size()) {
      throw new IllegalArgumentException("Foundation pile number is invalid");
    } else if (srcPile < 0 || srcPile >= this.cascadePiles.size()) {
      throw new IllegalArgumentException("Source pile number is invalid");
    } else if (this.cascadePiles.get(srcPile).isEmpty()) {
      throw new IllegalStateException("Source pile is empty");
    }

    // check if top card of srcPile can be moved to foundationPile
    BasicCard topCard = this.cascadePiles.get(srcPile)
        .get(this.cascadePiles.get(srcPile).size() - 1);
    checkMove(foundationPile, topCard);

    // move card from srcPile to foundationPile
    this.cascadePiles.get(srcPile).remove(this.cascadePiles.get(srcPile).size() - 1);
    if (!this.cascadePiles.get(srcPile).isEmpty()) {
      this.cascadePiles.get(srcPile).get(this.cascadePiles.get(srcPile).size() - 1).flipFaceUp();
    }
    this.foundationPiles.get(foundationPile).add(0, topCard);
  }

  /*
   * Helper method to check if a card can be moved to a foundation pile.
   * @param foundationPile the 0-based index (from the left) of the foundation pile to
   *                      place the card
   * @param topCard the card to be moved
   * @throws IllegalStateException if the move is not allowable
   * @throws IllegalArgumentException if the move is not allowable
   * @throws IllegalStateException if the move is not allowable
   *
   */
  private void checkMove(int foundationPile, BasicCard topCard) {
    if (this.foundationPiles.get(foundationPile).isEmpty() && !topCard.isAce()) {
      throw new IllegalStateException("Only ace can be moved to empty foundation pile");
    } else if (!this.foundationPiles.get(foundationPile).isEmpty()
        && !topCard.stacksAbove(this.foundationPiles.get(foundationPile).get(0))) {
      throw new IllegalStateException("Card is of wrong rank");
    } else if (!this.foundationPiles.get(foundationPile).isEmpty()
        && !topCard.getSuit().getSymbol().equals(this.foundationPiles
        .get(foundationPile).get(0).getSuit().getSymbol())) {
      throw new IllegalStateException("Card is of wrong suit");
    }
  }

  private void checkValidity(int pileNum, int card) {
    if (!this.gameHappening) {
      throw new IllegalStateException("Game has not started");
    }

    // check if pileNum is valid
    if (pileNum < 0 || pileNum >= this.cascadePiles.size()) {
      throw new IllegalArgumentException("Invalid Pile Index");
    }

    // check if card index is valid
    if (card < 0 || card >= this.cascadePiles.get(pileNum).size()) {
      throw new IllegalArgumentException("Invalid Card Index");
    }
  }

  /**
   * Moves the topmost draw-card to a foundation pile.
   * @param foundationPile the 0-based index (from the left) of the foundation
   * @throws IllegalStateException if the game hasn't been started yet
   * @throws IllegalArgumentException if the foundation pile number is invalid
   * @throws IllegalStateException if there are no draw cards or if the move is not
   *                               allowable
   */
  @Override
  public void moveDrawToFoundation(int foundationPile)
      throws IllegalArgumentException, IllegalStateException {
    // check if game has started
    if (!gameHappening) {
      throw new IllegalStateException("Game has not started");
    }

    // check if foundation pile is valid
    if (foundationPile < 0 || foundationPile >= this.foundationPiles.size()) {
      throw new IllegalArgumentException("Foundation pile number is invalid");
    } else if (this.drawPile.isEmpty()) {
      throw new IllegalStateException("Draw pile is empty");
    }

    // check if top card of drawPile can be moved to foundationPile
    BasicCard cardDrawn = this.drawPile.get(0);
    checkMove(foundationPile, cardDrawn);

    // move card from drawPile to foundationPile
    this.drawPile.remove(0);
    // flip the last card in drawPile if it is not empty
    if (this.drawPile.size() >= this.maxDrawVisible) {
      this.drawPile.get(this.maxDrawVisible - 1).flipFaceUp();
    }
    this.foundationPiles.get(foundationPile).add(0, cardDrawn);
  }

  /**
   * Discards the topmost draw-card.
   * @throws IllegalStateException if the game hasn't been started yet
   * @throws IllegalStateException if move is not allowable
   */
  @Override
  public void discardDraw() throws IllegalStateException {
    // check if game has started or if draw pile is empty
    if (!this.gameHappening) {
      throw new IllegalStateException("Game has not started");
    } else if (this.drawPile.isEmpty()) {
      throw new IllegalStateException("Draw pile is empty");
    }

    // send top card to back of draw pile
    Collections.rotate(this.drawPile, -1);

    // flip the last card in drawPile if it is not empty
    if (this.drawPile.size() > this.maxDrawVisible) {
      this.drawPile.get(this.drawPile.size() - 1).isShowing = false;
      this.drawPile.get(this.maxDrawVisible - 1).flipFaceUp();
    }
  }

  /**
   * Signal if the game is over or not.  A game is over if there are no more
   * possible moves to be made, or draw cards to be used (or discarded).
   *
   * @return true if game is over, false otherwise
   * @throws IllegalStateException if the game hasn't been started yet
   */
  @Override
  public boolean isGameOver() throws IllegalStateException {
    // check if game has started or if draw pile is empty
    if (!this.gameHappening) {
      throw new IllegalStateException("Game has not started");
    }

    return this.drawPile.isEmpty();
  }


  /**
   * Returns the number of piles.
   * @return the number of piles
   * @throws IllegalStateException if the game hasn't been started yet
   */
  @Override
  public int getNumPiles() throws IllegalStateException {
    // check if game has started or if draw pile is empty
    if (!this.gameHappening) {
      throw new IllegalStateException("Game has not started");
    }

    return this.cascadePiles.size();
  }

  /**
   * Returns the number of rows in the current table of cards (i.e. the number of
   * cascade piles).
   * @return the height of the current table of cards
   * @throws IllegalStateException if the game hasn't been started yet
   */
  @Override
  public int getNumRows() throws IllegalStateException {
    // check if game has started or if draw pile is empty
    if (!this.gameHappening) {
      throw new IllegalStateException("Game has not started");
    }

    //
    return this.cascadePiles.stream().mapToInt(List::size).max().orElse(0);
  }

  /**
   * Returns the maximum number of visible cards in the draw pile.
   *
   * @return the number of visible cards in the draw pile
   * @throws IllegalStateException if the game hasn't been started yet
   */
  @Override
  public int getNumDraw() throws IllegalStateException {
    // check if game has started or if draw pile is empty
    if (!this.gameHappening) {
      throw new IllegalStateException("Game has not started");
    }

    // return the maximum number of visible draw cards
    return this.maxDrawVisible;
  }
  /**
   * Return the current score, which is the sum of the values of the topmost cards
   * in the foundation piles.
   * @return the score
   * @throws IllegalStateException if the game hasn't been started yet
   */

  @Override
  public int getScore() throws IllegalStateException {
    // check if game has started or if draw pile is empty
    if (!this.gameHappening) {
      throw new IllegalStateException("Game has not started");
    }

    // return total number of cards in foundation piles
    return this.foundationPiles.stream().mapToInt(List::size).sum();
  }

  /**
   * Returns the number of cards in the specified pile.
   * @param pileNum the 0-based index (from the left) of the pile
   * @return the number of cards in the specified pile
   * @throws IllegalStateException if the game hasn't been started yet
   * @throws IllegalArgumentException if pile number is invalid
   */
  @Override
  public int getPileHeight(int pileNum) throws IllegalArgumentException, IllegalStateException {
    // check if game has started or if draw pile is empty
    if (!this.gameHappening) {
      throw new IllegalStateException("Game has not started");
    }

    // check if pileNum is valid
    if (pileNum >= this.cascadePiles.size() || pileNum < 0) {
      throw new IllegalArgumentException("Invalid Pile Number");
    }

    // return pile height
    return this.cascadePiles.get(pileNum).size();
  }

  /**
   * Returns whether the card at the specified coordinates is face-up or not.
   * @param pileNum  column of the desired card (0-indexed from the left)
   * @param card     row of the desired card (0-indexed from the top)
   * @return whether the card at the given position is face-up or not
   * @throws IllegalStateException if the game hasn't been started yet
   * @throws IllegalArgumentException if the coordinates are invalid
   */
  @Override
  public boolean isCardVisible(int pileNum, int card)
      throws IllegalArgumentException, IllegalStateException {
    // check if game has started or if draw pile is empty
    checkValidity(pileNum, card);

    return this.cascadePiles.get(pileNum).get(card).isShowing;
  }

  /**
   * Returns the card at the top of the specified foundation pile.
   * @param foundationPile 0-based index (from the left) of the foundation pile
   * @return the card at the given position, or <code>null</code> if no card is there
   * @throws IllegalStateException if the game hasn't been started yet
   * @throws IllegalArgumentException if the foundation pile number is invalid
   */
  @Override
  public Card getCardAt(int foundationPile)
      throws IllegalArgumentException, IllegalStateException {
    // check if game has started or if draw pile is empty
    if (!this.gameHappening) {
      throw new IllegalStateException("Game has not started");
    }

    // check if foundationPile is valid
    if (foundationPile < 0 || foundationPile >= this.foundationPiles.size()) {
      throw new IllegalArgumentException("Invalid Foundation Pile Index");
    }

    // check if foundationPile is empty
    if (this.foundationPiles.get(foundationPile).isEmpty()) {
      return null;
    }

    return this.foundationPiles.get(foundationPile).get(0);
  }

  /**
   * Returns the card at the specified coordinates, if it is visible.
   * @param pileNum  column of the desired card (0-indexed from the left)
   * @param card     row of the desired card (0-indexed from the top)
   * @return the card at the given position, or <code>null</code> if no card is there
   * @throws IllegalStateException if the game hasn't been started yet
   * @throws IllegalArgumentException if the coordinates are invalid
   */
  @Override
  public Card getCardAt(int pileNum, int card)
      throws IllegalArgumentException, IllegalStateException {
    // check if game has started or if draw pile is empty
    checkValidity(pileNum, card);

    // check if card is face up
    if (!this.cascadePiles.get(pileNum).get(card).isShowing) {
      throw new IllegalArgumentException("Card is not visible");
    }

    // return null if on original board but not now

    return this.cascadePiles.get(pileNum).get(card);
  }

  /**
   * Returns the currently available draw cards.
   * There should be at most {@link KlondikeModel} cards (the number
   * specified when the game started) -- there may be fewer, if cards have been removed.
   * NOTE: Users of this method should not modify the resulting list.
   * @return the ordered list of available draw cards (i.e. first element of this list
   *         is the first one to be drawn)
   * @throws IllegalStateException if the game hasn't been started yet
   */
  @Override
  public List<Card> getDrawCards() throws IllegalStateException {
    // check if game has started or if draw pile is empty
    if (!this.gameHappening) {
      throw new IllegalStateException("Game has not started");
    }


    return this.drawPile.stream().filter(card -> card.isShowing).collect(Collectors.toList());
  }

  /**
   * Return the number of foundation piles in this game.
   * @return the number of foundation piles
   * @throws IllegalStateException if the game hasn't been started yet
   */
  @Override
  public int getNumFoundations() throws IllegalStateException {
    // check if game has started or if draw pile is empty
    if (!this.gameHappening) {
      throw new IllegalStateException("Game has not started");
    }

    return this.foundationPiles.size();
  }






}

