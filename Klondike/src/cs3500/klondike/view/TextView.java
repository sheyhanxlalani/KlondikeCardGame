package cs3500.klondike.view;

import java.io.IOException;

/** A marker interface for all text-based views, to be used in the Klondike game. */
public interface TextView {
  /**
   * Renders the game state to the player.
   *
   * @throws IllegalStateException if the view cannot render the game state
   */
  void render() throws IOException;
}
