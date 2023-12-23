package cs3500.klondike.controller;

import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.view.KlondikeTextualView;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * Represents a controller for a game of Klondike. Facilitates interaction between the player
 */
public class KlondikeTextualController implements cs3500.klondike.controller.KlondikeController {
  private final Readable input;
  private final Appendable output;

  /**
   * Constructs a KlondikeTextualController.
   *
   * @param read the readable
   * @param app the appendable
   * @throws IllegalArgumentException if the readable or appendable are null
   */
  public KlondikeTextualController(Readable read, Appendable app) throws IllegalArgumentException {
    try {
      this.input = Objects.requireNonNull(read);
      this.output = Objects.requireNonNull(app);
    } catch (NullPointerException error) {
      throw new IllegalArgumentException("Readable and Appendable cannot be null");
    }
  }

  /**
   * Plays a game of solitaire using the provided model, deck, and number of piles and draw cards.
   *
   * @param model    The model to be used
   * @param deck     The deck to be used
   * @param shuffle  Whether or not the deck should be shuffled
   * @param numPiles How many piles should be in the game
   * @param numDraw  How many draw cards should be in the game
   * @throws IllegalArgumentException if the model is null
   * @throws IllegalStateException   if the game cannot be started
   */
  @Override
  public void playGame(KlondikeModel model, List<Card> deck, boolean shuffle, int numPiles,
                       int numDraw) throws IllegalArgumentException, IllegalStateException {
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null");
    }
    // IllegalStateException if the game cannot be started
    try {
      model.startGame(deck, shuffle, numPiles, numDraw);
    } catch (Exception error) {
      throw new IllegalStateException("Game cannot be started: " + error.getMessage());
    }

    // create a scanner to read user input
    Scanner scan = new Scanner(this.input);
    // create a textual view object
    KlondikeTextualView view = new KlondikeTextualView(model, this.output);
    // boolean to determine if the user has quit the game
    boolean quit = false;

    while (!model.isGameOver() && !quit) {
      try {
        view.render();
        this.output.append("Score: ")
            .append(String.valueOf(model.getScore()))
            .append("\n");

        String moveType = scan.next();
        switch (moveType) {
          case "q":
          case "Q":
            quit = true;
            break;
          case "md":
            // get valid inputs and break if 'q'/'Q' is found
            int pileFromDraw = readNextInput(scan);
            if (checkPOC(pileFromDraw)) {
              break;
            }
            // perform move
            try {
              model.moveDraw(pileFromDraw - 1);
            } catch (Exception error) {
              this.output.append("Invalid move. Play again. ").
                  append(error.getMessage()).append("\n");
            }
            break;
          case "mpp":
            int pile1 = readNextInput(scan);
            if (checkPOC(pile1)) {
              break;
            }

            int numCards = readNextInput(scan);
            if (checkPOC(numCards)) {
              break;
            }

            int pile2 = readNextInput(scan);
            if (checkPOC(pile2)) {
              break;
            }

            // perform move
            try {
              model.movePile(pile1 - 1, numCards, pile2 - 1);
            } catch (Exception error) {
              this.output.append("Invalid move. Play again. ").
                  append(error.getMessage()).append("\n");
            }
            break;
          case "mdf":
            // get valid inputs and break if 'q'/'Q' is found
            int foundationFromDraw = readNextInput(scan);
            if (checkPOC(foundationFromDraw)) {
              break;
            }
            // perform move
            try {
              model.moveDrawToFoundation(foundationFromDraw - 1);
            } catch (Exception error) {
              this.output.append("Invalid move. Play again. ").
                  append(error.getMessage()).append("\n");
            }
            break;
          case "mpf":

            int pileToFoundation = readNextInput(scan);
            if (checkPOC(pileToFoundation)) {
              break;
            }

            int foundationFromPile = readNextInput(scan);
            if (checkPOC(foundationFromPile)) {
              break;
            }


            // perform move
            try {
              model.moveToFoundation(pileToFoundation - 1,
                  foundationFromPile - 1);
            } catch (Exception error) {
              this.output.append("Invalid move. Play again. ").
                  append(error.getMessage()).append("\n");
            }
            break;
          case "dd":
            // perform move
            try {
              model.discardDraw();
            } catch (Exception error) {
              this.output.append("Invalid move. Play again. ").
                  append(error.getMessage()).append("\n");
            }
            break;
          default:
            this.output.append("Invalid move. Play again. No such move type.\n");
        }
      } catch (Exception error) {
        throw new IllegalStateException("Cannot interact with player: " + error.getMessage());
      }
    }
    // end of game message
    try {
      if (quit) {
        this.output.append("Game quit!\nState of game when quit:\n");
        view.render();
        this.output.append("Score: ").append(String.valueOf(model.getScore())).append("\n");
      } else {
        view.render();
        if (model.getScore() == deck.size()) {
          this.output.append("You win!\n");
        } else {
          this.output.append("Game over. Score: ").append(String.valueOf(model.getScore()))
              .append("\n");
        }
      }
    } catch (Exception error) {
      throw new IllegalStateException("Cannot interact with player: " + error.getMessage());
    }
  }

  public boolean checkPOC(int poc) {
    return poc == -1;
  }


  /**
   * Reads the next input from the scanner until a number or 'q'/'Q' is found.
   *
   * @param scan the scanner to read from
   * @return the next input from the scanner as an int or -1 if 'q'/'Q' is found
   */
  public int readNextInput(Scanner scan) throws IOException {
    // read next input until a number or 'q'/'Q' is found
    while (true) {
      String next = scan.next();
      if (next.equals("q") || next.equals("Q")) {
        return -1;
      } else {
        try {
          return Integer.parseInt(next);
        } catch (NumberFormatException ignored) {
          this.output.append("Unexpected value. Please enter a number or 'q'/'Q'.\n");
        }
      }
    }
  }
}



