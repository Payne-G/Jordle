import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Backend for a Jordle game.
 *
 * Use this class to implement your Jordle application for HW09!
 *
 * DO NOT MODIFY THIS FILE!
 *
 * @author CS 1331 TAs
 * @version 13.31
 */
public class Backend {

    private static final int WORD_LENGTH = 5;
    private static final char GREEN = 'g';
    private static final char YELLOW = 'y';
    private static final char INCORRECT = 'i';
    private final Random rand;
    private final List<String> words;
    private String target;

    /**
     * Constructor for a Jordle Backend.
     *
     * This constructor initializes a random target word from the words.txt word bank.
     */
    public Backend() {
        words = getWords();
        rand = new Random();
        reset();
    }

    /**
     * Reads in the words from the words.txt word bank.
     *
     * @return a list of five letter words as strings, or a list only containing "adieu" if
     *         words.txt could not be found.
     *
     */
    private List<String> getWords() {
        List<String> lines = new ArrayList<>();
        File file = new File("words.txt");
        Scanner scan = null;
        try {
            scan = new Scanner(file);
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                if (line.length() == WORD_LENGTH) {
                    lines.add(line.toLowerCase());
                }
            }
        } catch (FileNotFoundException fnfe) {
            System.out.println("Error in reading words.txt: " + fnfe.getMessage());
            lines.add("adieu");
        } finally {
            if (scan != null) {
                scan.close();
            }
        }
        return lines;
    }

    /**
     * This method sets the target word and is called once upon initializing a Backend.
     * You should call this method whenever you need to reset a Jordle game.
     */
    public void reset() {
        target = words.get(rand.nextInt(words.size()));
    }

    /**
     * Method to check the correctness of your Jordle guesses. This method is case-insensitive.
     *
     * @param word the word to guess
     * @return a five-letter String made up of 'g', 'y', or 'i's corresponding to the
     *         correctness of the guess against the target.
     *         A 'g' indicates that the correct letter is in the correct position.
     *         A 'y' indicates that a correct letter is in the incorrect position.
     *         A 'i' indicates that the letter is not in the target word.
     * @throws InvalidGuessException if the word is not a valid Jordle guess.
     *
     */
    public String check(String word) throws InvalidGuessException {
        if (word == null || word.length() != WORD_LENGTH || word.isBlank()) {
            throw new InvalidGuessException(word);
        }
        word = word.toLowerCase();
        char[] targetArray = target.toCharArray();
        char[] result = new char[WORD_LENGTH];

        for (int i = 0; i < WORD_LENGTH; i++) {
            result[i] = INCORRECT;
        }

        for (int i = 0; i < WORD_LENGTH; i++) {
            if (word.charAt(i) == targetArray[i]) {
                result[i] = GREEN;
                targetArray[i] = 0;
            }
        }

        for (int i = 0; i < WORD_LENGTH; i++) {
            if (result[i] == INCORRECT) {
                for (int j = 0; j < WORD_LENGTH; j++) {
                    if (word.charAt(i) == targetArray[j]) {
                        result[i] = YELLOW;
                        targetArray[j] = 0;
                        break;
                    }
                }
            }
        }

        return String.valueOf(result);
    }

    /**
     * Getter for the target word.
     *
     * @return the target word
     */
    public String getTarget() {
        return target;
    }

}