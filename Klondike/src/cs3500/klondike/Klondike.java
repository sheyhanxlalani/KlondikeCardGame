package cs3500.klondike;

import cs3500.klondike.controller.KlondikeController;
import cs3500.klondike.controller.KlondikeTextualController;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw04.GameType;
import cs3500.klondike.model.hw04.KlondikeCreator;

import java.io.InputStreamReader;


/**
 * Represents the main class for the Klondike game.
 */
public final class Klondike {
  /**
   * Runs the Klondike game.
   *
   * @param args the arguments for the game
   */
  public static void main(String[] args) {
    if (args.length == 0) {
      throw new IllegalArgumentException("No arguments provided");
    }

    KlondikeController controller =
        new KlondikeTextualController(new InputStreamReader(System.in), System.out);
    int numPiles = 7;
    int numDraw = 3;
    GameType type = GameType.fromString(args[0]);
    switch (type) {
      case BASIC:
      case WHITEHEAD:
        runBasicOrWhitehead(args, controller, numPiles, numDraw, type);
        break;
      case LIMITED:
        if (args.length < 2) {
          throw new IllegalArgumentException("Must provide number of draws");
        } else {
          int numTimesRedrawAllowed = Integer.parseInt(args[1]);
          try {
            KlondikeModel game = KlondikeCreator.create(type, numTimesRedrawAllowed);
            if (args.length >= 3) {
              numPiles = Integer.parseInt(args[2]);
              if (args.length >= 4) {
                numDraw = Integer.parseInt(args[3]);
              }
            }
            controller.playGame(game, game.getDeck(), true, numPiles, numDraw);
          } catch (IllegalArgumentException e) {
            System.out.println("Invalid inputs");
          }
        }
        break;
      default:
        throw new IllegalArgumentException("Invalid game type");
    }
  }

  private static void
      runBasicOrWhitehead(String[] args, KlondikeController controller,
                          int numPiles, int numDraw, GameType type) {
    if (args.length < 1) {
      throw new IllegalArgumentException("Must provide number of draws");
    }
    try {
      KlondikeModel game = KlondikeCreator.create(type);
      if (args.length >= 2) {
        numPiles = Integer.parseInt(args[1]);
        if (args.length == 3) {
          numDraw = Integer.parseInt(args[2]);
        }
      }
      controller.playGame(game, game.getDeck(), true, numPiles, numDraw);
    } catch (IllegalArgumentException e) {
      System.out.println("Invalid inputs");
    }
  }
}
