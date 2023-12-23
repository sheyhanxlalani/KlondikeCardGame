
package cs3500.klondike.model.hw02;

/**
 * Represents the suit of a card in a standard deck of playing cards.
 */
public enum CardSuit {
  /**
   * Represents the different suits cards can have
   * in a standard deck of playing cards.
   */

  HEARTS("♡", "red"),
  DIAMONDS("♢", "red"),
  CLUBS("♣", "black"),
  SPADES("♠", "black");

  private final String suit;
  private final String color;

  /**
   * Constructs a {@code Suit} object.
   *
   * @param suit the symbol of the suit
   * @param color  the color of the suit
   */
  CardSuit(String suit, String color) {
    this.suit = suit;
    this.color = color;
  }

  /**
   * Returns the symbol of this suit.
   *
   * @return the symbol of this suit
   */
  public String getSymbol() {
    return suit;
  }

  /**
   * Returns the color of this suit.
   *
   * @return the color of this suit
   */
  public String getColor() {
    return color;
  }
}

