package cs3500.klondike.model.hw04;

/**
 * Represents the type of game to play.
 */
public enum GameType {
  BASIC("basic"), LIMITED("limited"), WHITEHEAD("whitehead");

  GameType(String type) {
  }

  /**
   * Returns the game type from the given string.
   * @param type the string representing the game type
   *             (either "basic", "limited", or "whitehead")
   */
  public static GameType fromString(String type) {
    switch (type) {
      case "basic":
        return BASIC;
      case "limited":
        return LIMITED;
      case "whitehead":
        return WHITEHEAD;
      default:
        throw new IllegalArgumentException("Invalid game type");
    }
  }
}
