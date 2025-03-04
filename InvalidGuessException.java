/**
 * InvalidGuessException, a checked exception thrown when a Jordle guess is not valid.
 *
 * DO NOT MODIFY THIS FILE!
 *
 * @author CS 1331 TAs
 * @version 13.31
 */
public class InvalidGuessException extends Exception {

    /**
     * 1-arg constructor.
     *
     * @param guess the word guessed in the Jordle game.
     */
    public InvalidGuessException(String guess) {
        super("\"" + guess + "\" is not a valid Jordle guess.");
    }
}