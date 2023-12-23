package cs3500.klondike.view;

import java.io.IOException;

/**
 * A marker interface for all textual views, to be used in the Klondike game.
 */
public interface TextualView {
  /**
   * Renders the game state to the player.
   *
   * @throws IOException if the view cannot render the game state
   */
  void render() throws IOException;
}
