package cs3500.klondike.model.hw04;

import cs3500.klondike.model.hw02.BasicCard;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;

import java.util.List;


/**
 * This is a stub implementation of the {@link KlondikeModel} interface.
 * You may assume that the actual implementation of WhiteheadKlondike will have a zero-argument
 * (i.e. default) constructor, and that all the methods below will be implemented.  You may not make
 * any other assumptions about the implementation of this class (e.g. what fields it might have,
 * or helper methods, etc.).
 */
public class WhiteheadKlondike extends AKlondikeModel implements KlondikeModel {

  public WhiteheadKlondike() {
    super();
  }

  @Override
  public void startGame(List<Card> deck, boolean shuffle, int numPiles, int numDraw)
      throws IllegalArgumentException {
    super.startGame(deck, shuffle, numPiles, numDraw);

    //makes all the cards in cascade piles visible
    for (int i = 0; i < numPiles; i++) {
      List<BasicCard> pile = this.cascadePiles.get(i);
      for (BasicCard basicCard : pile) {
        basicCard.flipFaceUp();
      }
    }
  }

  @Override
  public void movePile(int srcPile, int numCards, int destPile) throws IllegalStateException {
    // check if game has started
    if (!gameHappening) {
      throw new IllegalStateException("Game has not yet started");
    }

    // check if srcPile is valid
    if (srcPile < 0 || srcPile >= this.cascadePiles.size()) {
      throw new IllegalArgumentException("The source pile number is invalid");
    } else if (srcPile == destPile) {
      throw new IllegalArgumentException("Destination pile cannot be the same as source pile");
    } else if (destPile < 0 || destPile >= this.cascadePiles.size()) {
      throw new IllegalArgumentException("Destination pile number is invalid");
    }
    else if (numCards < 1 || numCards > this.cascadePiles.get(srcPile).size()) {
      throw new IllegalArgumentException("Invalid number of cards to move");
    }

    // check if there are enough face up cards in srcPile
    int cardShownCount = 0;
    for (BasicCard c : this.cascadePiles.get(srcPile)) {
      if (c.isShowing) {
        cardShownCount++;
      }
    }
    if (cardShownCount < numCards) {
      throw new IllegalArgumentException("There are not enough face up cards in the source pile");
    }

    // check if top card of srcPile can be moved to destPile
    BasicCard topCard = this.cascadePiles.get(srcPile)
        .get(this.cascadePiles.get(srcPile).size() - numCards);

    if (!this.cascadePiles.get(destPile).isEmpty()) {
      BasicCard destCard = this.cascadePiles.get(destPile)
          .get(this.cascadePiles.get(destPile).size() - 1);
      // check if top card is of different color and if top card stacks below destCard
      if (!topCard.sameColor(destCard) || !topCard.stacksBelow(destCard)) {
        throw new IllegalStateException("Unable to move cards from source pile "
            + "to destination pile test");
      }
    }

    // move cards from srcPile to destPile
    List<BasicCard> pileMove = this.cascadePiles.get(srcPile)
        .subList(this.cascadePiles.get(srcPile).size() - numCards,
            this.cascadePiles.get(srcPile).size());


    // flip the last card in srcPile if it is not empty
    this.cascadePiles.get(destPile).addAll(pileMove);
    // remove appropriate cards from srcPile
    this.cascadePiles.get(srcPile).removeAll(pileMove);
    // flip the last card in srcPile if it is not empty
    if (!this.cascadePiles.get(srcPile).isEmpty()) {
      this.cascadePiles.get(srcPile).get(this.cascadePiles.get(srcPile).size() - 1).flipFaceUp();
    }

  }

  @Override
  public void moveDraw(int destPile) throws IllegalArgumentException, IllegalStateException {
    // check if game has started
    if (!gameHappening) {
      throw new IllegalStateException("Game has not yet started");
    }

    // check if destPile is valid and if there are any draw cards left
    if (this.drawPile.isEmpty()) {
      throw new IllegalArgumentException("No draw cards left");
    } // check if destPile is valid
    else if (destPile >= this.cascadePiles.size() || destPile < 0) {
      throw new IllegalArgumentException("Destination pile number is invalid");
    }

    // check if top card of drawPile can be moved to destPile
    BasicCard cardDrawn = this.drawPile.get(0);


    if (!this.cascadePiles.get(destPile).isEmpty()) {
      //
      BasicCard destCard = this.cascadePiles.get(destPile)
          .get(this.cascadePiles.get(destPile).size() - 1);
      if (!cardDrawn.stacksBelow(destCard) || !cardDrawn.sameColor(destCard)) {
        throw new IllegalStateException("Cannot move draw card to destination pile");
      }
    }

    // move card from drawPile to destPile
    this.drawPile.remove(0);
    // flip the last card in drawPile if it is not empty
    if (this.drawPile.size() >= this.maxDrawVisible) {
      this.drawPile.get(this.maxDrawVisible - 1).flipFaceUp();
    }

    this.cascadePiles.get(destPile).add(cardDrawn);

  }

}
