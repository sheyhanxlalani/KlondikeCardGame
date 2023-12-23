package cs3500.klondike.model.hw02;


import java.util.Objects;

/**
 * Represents a card in a standard deck of playing cards.
 */
public class BasicCard implements Card {
  private final CardSuit suit;

  private final CardValue rank;

  public boolean isShowing;

  /**
   * Constructs a {@code Card} object.
   * Initializes the suit and rank of the card sets the card to be face down.
   *
   * @param suit the suit of the card
   * @param rank the rank of the card
   */
  public BasicCard(CardSuit suit, CardValue rank) {
    this.suit = suit;
    this.rank = rank;
    this.isShowing = false;
  }


  @Override
  public String toString() {
    return rank.getName() + suit.getSymbol();
  }


  @Override
  public boolean equals(Object other) {
    return other instanceof BasicCard
        && ((BasicCard) other).suit.getSymbol().equals(suit.getSymbol())
        && ((BasicCard) other).rank.getValue() == rank.getValue();
  }



  @Override
  public int hashCode() {
    return Objects.hash(suit, rank);
  }


  public int getValue() {
    return this.rank.getValue();
  }

  public CardSuit getSuit() {
    return this.suit;
  }

  public boolean stacksBelow(BasicCard other) {
    return rank.getValue() == other.rank.getValue() - 1;
  }

  public boolean stacksAbove(BasicCard other) {
    return rank.getValue() == other.getValue() + 1;
  }

  public boolean sameColor(BasicCard other) {
    return suit.getColor().equals(other.suit.getColor());
  }

  public boolean isAce() {
    return rank == CardValue.ACE;
  }

  public void flipFaceUp() {
    this.isShowing = true;
  }

  public boolean isKing() {
    return rank == CardValue.KING;
  }

  public void flipFaceDown() {
    this.isShowing = false;
  }

}


