package cs3500.klondike.model.hw02;

/**
 * Represents the rank of a card in a standard deck of playing cards.
 */
public enum CardValue {
  /**
   * Represents the different ranks cards can have
   * in a standard deck of playing cards.
   */
  ACE("A", 1),
  TWO("2", 2),
  THREE("3", 3),
  FOUR("4", 4),
  FIVE("5", 5),
  SIX("6", 6),
  SEVEN("7", 7),
  EIGHT("8", 8),
  NINE("9", 9),
  TEN("10", 10),
  JACK("J", 11),
  QUEEN("Q", 12),
  KING("K", 13);

  private final String name;
  private final int value;

  /**
   * Constructs a {@code Rank} object.
   *
   * @param name  the name of the rank
   * @param value the value of the rank
   */
  CardValue(String name, int value) {
    this.name = name;
    this.value = value;
  }

  /**
   * Returns the name of this rank.
   *
   * @return the name of this rank
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the value of this rank.
   *
   * @return the value of this rank
   */
  public int getValue() {
    return value;
  }
}
