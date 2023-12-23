package cs3500.klondike.view;

import java.io.IOException;
import java.util.Objects;

import cs3500.klondike.model.hw02.BasicCard;
import cs3500.klondike.model.hw02.KlondikeModel;

/**
 * A simple text-based rendering of the Klondike game.
 */
public class KlondikeTextualView implements TextualView {
  private final KlondikeModel klondike;
  private Appendable app;


  /**
   * Constructs a {@code KlondikeTextualView} object.
   *
   * @param model the model to be rendered
   */
  public KlondikeTextualView(KlondikeModel model) {
    this.klondike = Objects.requireNonNull(model);
  }


  /**
   * Constructs a {@code KlondikeTextualView} object.
   *
   * @param model the model to be rendered
   * @param ap    the appendable to which the view will be appended
   */
  public KlondikeTextualView(KlondikeModel model, Appendable ap) {
    this.klondike = Objects.requireNonNull(model);
    this.app = Objects.requireNonNull(ap);
  }

  /**
   * Renders the textual view game state and appends it to the appendable
   * specified in the constructor.
   */
  public void render() throws IOException {
    try {
      this.app.append(this.toString());
    } catch (IOException e) {
      throw new IOException("Append failed");
    }
  }

  /**
   * Creates a string representation of the game state.
   *
   * @return a string representation of the game state
   */
  public String toString() {
    // create a StringBuilder to build the view
    StringBuilder klondikeView = new StringBuilder();

    // Build Draw Pile
    drawPile(klondikeView);

    // Build Foundation Piles
    buildFoundations(klondikeView);
    klondikeView.append("\n");

    // Build cascade piles
    buildCascadePiles(klondikeView);

    // return viewable version of the game as a string
    return klondikeView.toString();
  }

  /**
   * Builds the draw pile.
   *
   * @param klondikeView the StringBuilder to which the draw pile will be appended
   */
  public void drawPile(StringBuilder klondikeView) {
    klondikeView.append("Draw: ");
    if (!this.klondike.getDrawCards().isEmpty()) {
      for (int i = 0; i < this.klondike.getDrawCards().size(); i++) {
        if (i == this.klondike.getDrawCards().size() - 1) {
          klondikeView.append(this.klondike.getDrawCards().get(i).toString());
        } else {
          klondikeView.append(this.klondike.getDrawCards().get(i).toString()).append(", ");
        }
      }
    }
    klondikeView.append("\n");
  }

  /**
   * Builds the foundation piles.
   *
   * @param klondikeView the StringBuilder to which the foundation piles will be appended
   */
  public void buildFoundations(StringBuilder klondikeView) {
    klondikeView.append("Foundation: ");
    for (int i = 0; i < this.klondike.getNumFoundations(); i++) {
      if (this.klondike.getCardAt(i) == null) {
        klondikeView.append("<none>");
      } else {
        klondikeView.append(this.klondike.getCardAt(i).toString());
      }
      if (i != this.klondike.getNumFoundations() - 1) {
        klondikeView.append(", ");
      }
    }
  }

  /**
   * Builds the cascade piles.
   *
   * @param klondikeView the StringBuilder to which the cascade piles will be appended
   */
  public void buildCascadePiles(StringBuilder klondikeView) {
    for (int i = 0; i < this.klondike.getNumRows(); i++) {
      for (int j = 0; j < this.klondike.getNumPiles(); j++) {
        try {
          BasicCard card = (BasicCard) this.klondike.getCardAt(j, i);
          if (card.toString().length() == 3) {
            klondikeView.append(card.toString());
          } else {
            klondikeView.append(" ").append(card.toString());
          }
        } catch (IllegalArgumentException error) {
          if (error.getMessage().equals("Invalid Pile Index")
              || error.getMessage().equals("Invalid Card Index")) {
            if (i == 0) {
              klondikeView.append("  X");
            } else {
              klondikeView.append("   ");
            }
          } else if (error.getMessage().equals("Card is not visible")) {
            klondikeView.append("  ?");
          }
        }
      }
      klondikeView.append("\n");
    }
  }



}
