package cs3500.klondike.model.hw04;

import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.KlondikeModel;

/**
 * Represents a factory for creating Klondike games.
 */
public class KlondikeCreator {

  /**
   * Creates a Klondike game of the given type.
   * @param type the type of game to create
   */
  public static KlondikeModel create(GameType type) {
    switch (type) {
      case BASIC:
        return new BasicKlondike();
      case LIMITED:
        return new LimitedDrawKlondike(2);
      case WHITEHEAD:
        return new WhiteheadKlondike();
      default:
        throw new IllegalArgumentException("Invalid game type");
    }
  }

  /**
   * Creates a Klondike game of the given type with the given number of times the player can.
   * @param type the type of game to create
   * @param numTimesRedrawAllowed the number of times the player can redraw
   */
  public static KlondikeModel create(GameType type, int numTimesRedrawAllowed) {
    switch (type) {
      case BASIC:
        return new BasicKlondike();
      case LIMITED:
        return new LimitedDrawKlondike(numTimesRedrawAllowed - 1);
      case WHITEHEAD:
        return new WhiteheadKlondike();
      default:
        throw new IllegalArgumentException("Invalid game type");
    }
  }
}
