package cs3500.klondike.model.hw04;

import cs3500.klondike.model.hw02.BasicCard;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a stub implementation of the {@link KlondikeModel} interface.
 * You may assume that the actual implementation of LimitedDrawKlondike will have a one-argument
 * constructor, and that all the methods below will be implemented.  You may not make
 * any other assumptions about the implementation of this class (e.g. what fields it might have,
 * or helper methods, etc.).
 */
public class LimitedDrawKlondike extends AKlondikeModel implements KlondikeModel {

  private int numTimesRedrawAllowed;

  private final Map<Card, Integer> cardDrawMap;

  /**
   * Initializes foundation, cascade and draw piles.
   * @param numTimesRedrawAllowed the number of times a player can redraw a card.
   */
  public LimitedDrawKlondike(int numTimesRedrawAllowed) {
    super();
    this.numTimesRedrawAllowed = numTimesRedrawAllowed;
    this.cardDrawMap = new HashMap<>();
    if (numTimesRedrawAllowed < 0) {
      throw new IllegalArgumentException("Cannot have a negative number of times to redraw.");
    }
  }

  /**
   * <p>Deal a new game of LimitedDrawKlondike.
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
      throws IllegalArgumentException {
    super.startGame(deck, shuffle, numPiles, numDraw);
    for (int i = 0; i < drawPile.size(); i++) {
      cardDrawMap.put(drawPile.get(i), numTimesRedrawAllowed);
    }

  }

  /**
   * Discards the topmost draw-card.
   * @throws IllegalStateException if the game hasn't been started yet
   * @throws IllegalStateException if move is not allowable
   */
  @Override
  public void discardDraw() throws IllegalStateException {
    if (!gameHappening) {
      throw new IllegalStateException("Game has not started yet.");
    }

    if (drawPile.isEmpty()) {
      throw new IllegalStateException("Draw pile is empty.");
    } else {
      discardCard();
    }
  }


  /**
   * Discards the topmost draw-card.
   * @throws IllegalStateException if the game hasn't been started yet
   * @throws IllegalStateException if move is not allowable
   */
  public void discardCard() throws IllegalStateException {
    Card topCard = drawPile.get(0);
    int numTimesRedraw = cardDrawMap.get(topCard);
    if (numTimesRedraw == 0) {
      deleteOrRecycleCard(true);
    } else {
      deleteOrRecycleCard(false);
      int newNumTimesRedraw = numTimesRedraw - 1;
      cardDrawMap.replace(topCard, newNumTimesRedraw);
    }
  }

  private void deleteOrRecycleCard(boolean permanent) {
    BasicCard topCard = drawPile.remove(0);
    topCard.flipFaceDown();
    if (!permanent) {
      drawPile.add(topCard);
    }
    if (drawPile.size() >= getNumDraw()) {
      for (int i = 0; i < getNumDraw(); i++) {
        drawPile.get(i).flipFaceUp();
      }
    }
  }

  public int getNumDrawCards() {
    return this.drawPile.size();
  }


}

