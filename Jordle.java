
import java.io.File;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 * This class creates the game Jordle ("Lightly" based on wordle).
 * @author Payne Gumbrill
 * @version 1.0
 */
public class Jordle extends Application {

    private int width = 1200;
    private int height = 900;
    private Stage primaryStage;
    private Scene startScene;
    private Scene mainScene;
    private Scene instructionScene;
    private Scene errorScene;
    private Scene statScene;
    private GridPane grid;
    private int currentColumn;
    private int currentRow;
    private boolean gameOver = false;
    private Backend game;
    private BorderPane mainPane;
    private Label bottomText;
    private Background winBackground;
    private int gamesPlayed = 0;
    private Label playedLabel;
    private int gamesWon = 0;
    private int firstGuess = 0;
    private int secondGuess = 0;
    private int thirdGuess = 0;
    private int fourthGuess = 0;
    private int fifthGuess = 0;
    private int sixthGuess = 0;
    private double winRate;
    private Label winPercentLabel;
    private double avgGuess;
    private int streak;
    private Label streakLabel;
    private int maxStreak;
    private Label maxStreakLabel;
    private XYChart.Series<Number, String> series1;


    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        this.startScene = createStartScene();
        this.mainScene = createMainScene();
        this.instructionScene = createInstructionScene();
        this.errorScene = createErrorScene();
        this.statScene = createStatScene();
        stage.setTitle("Jordle");
        stage.setScene(startScene);
        stage.show();
    }

    /**
     * Creates the Start Scene.
     * @return the Start Scene with an image background, title, and play button
     */
    private Scene createStartScene() {
        VBox root = new VBox(125);

        VBox labelPane = new VBox(10);
        labelPane.setAlignment(Pos.CENTER);
        VBox startPane = new VBox(10);

        Label jorLabel = createTitle();
        labelPane.getChildren().addAll(jorLabel);

        Button startButton = createPlayButton();

        startPane.getChildren().add(startButton);
        startPane.setAlignment(Pos.CENTER);

        BackgroundImage background = new BackgroundImage(new Image("jordleImage.jpg"),
            BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);

        root.getChildren().addAll(labelPane, startPane);
        root.setBackground(new Background(background));
        root.setAlignment(Pos.CENTER);

        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                primaryStage.setScene(mainScene);
                playGame();
            }
        });

        return new Scene(root, width, height);
    }

    /**
     * Creates the title for the Start Scene.
     * @return "Jordle"
     */
    private Label createTitle() {
        DropShadow ds = new DropShadow();
        ds.setColor(Color.color(0.4f, 0.4f, 0.4f));
        ds.setOffsetY(3.0f);
        Label jorLabel = new Label("Jordle");
        jorLabel.setEffect(ds);
        jorLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 75));
        jorLabel.setTextFill(Color.GREEN);
        return jorLabel;
    }

    /**
     * Creates a Play Button.
     * @return a Play Button
     */
    private Button createPlayButton() {
        Label play = new Label("Play");
        play.setFont(Font.font("Helvetica", FontWeight.BOLD, 50));
        Button startButton = new Button("Play");
        startButton.setMaxSize(150, 150);
        String style = "-fx-border-color: #000000; -fx-font-size: 16pt;";
        style += "-fx-background-color: #008000; -fx-text-fill: #ffffff";
        startButton.setStyle(style);
        return startButton;
    }

    /**
     * Creates the main game scene.
     * @return main scene with title, game grid, and bottom text and buttons
     */
    private Scene createMainScene() {
        game = new Backend();
        mainPane = new BorderPane();

        winBackground = createWinBackground();

        Label topLabel = createTopLabel();
        mainPane.setTop(topLabel);
        BorderPane.setAlignment(topLabel, Pos.CENTER);
        BorderPane.setMargin(topLabel, new Insets(30, 30, 30, 30));

        HBox bottomBox = createBottomBox();
        mainPane.setBottom(bottomBox);
        BorderPane.setAlignment(bottomBox, Pos.CENTER);
        BorderPane.setMargin(bottomBox, new Insets(50, 50, 50, 50));

        grid = createGrid();

        mainPane.setCenter(grid);

        setBlackBackground(mainPane);

        Scene scene = new Scene(mainPane, width, height);

        scene.setOnKeyPressed(e -> {
            if (currentRow > 5) {
                return;
            }
            if (e.getCode() == KeyCode.BACK_SPACE && currentColumn > 0) {
                backSpace();
            } else if (e.getCode().compareTo(KeyCode.A) >= 0
                && e.getCode().compareTo(KeyCode.Z) <= 0 && currentColumn < 5) {
                letterInput(e.getCode().toString());
            } else if (e.getCode() == KeyCode.ENTER) {
                enterKey();
            }
        });
        return scene;
    }

    /**
     * Creates the win background.
     * @return win background
     */
    private Background createWinBackground() {
        Image confetti = new Image(new File("confetti.gif").toURI().toString());
        Background background = new Background(new BackgroundImage(confetti, BackgroundRepeat.REPEAT,
            BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT));
        return background;
    }

    /**
     * Creates the title for the main scene.
     * @return "Jordle"
     */
    private Label createTopLabel() {
        DropShadow ds = new DropShadow();
        ds.setColor(Color.color(0.4f, 0.4f, 0.4f));
        ds.setOffsetY(3.0f);
        Label topLabel = new Label("Jordle");
        topLabel.setEffect(ds);
        topLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 75));
        topLabel.setAlignment(Pos.CENTER);
        topLabel.setTextFill(Color.web("#6ca965"));
        return topLabel;
    }

    /**
     * Creates the bottom section of the main scene.
     * @return an HBox with a text label, restart and instruction buttons
     */
    private HBox createBottomBox() {
        HBox bottomBox = new HBox(50);
        bottomText = new Label("Try Guessing a 5 letter word!");
        bottomText.setFont(Font.font("Helvetica", FontWeight.BOLD, 31));
        bottomText.setTextFill(Color.web("#6ca965"));

        Button resetButton = new Button("Restart");
        resetButton.setFont(Font.font("Helvetica", FontWeight.BOLD, 16));
        String resetStyle = "-fx-border-color: #ffffff; -fx-font-size: 16pt;";
        resetStyle += "-fx-background-color: #787c7f; -fx-text-fill: #ffffff";
        resetButton.setStyle(resetStyle);
        resetButton.setFocusTraversable(false);
        resetButton.setOnAction(e -> {
            game.reset();
            for (int i = 0; i < 6; ++i) {
                clearRow(i);
            }
            currentColumn = 0;
            currentRow = 0;
            gameOver = false;
            bottomText.setText("Try Guessing a 5 letter word!");
            setBlackBackground(mainPane);
        });

        Button instructionButton = new Button("Instructions");
        instructionButton.setFont(Font.font("Helvetica", FontWeight.BOLD, 16));
        String style = "-fx-border-color: #ffffff; -fx-font-size: 16pt;";
        style += "-fx-background-color: #787c7f; -fx-text-fill: #ffffff";
        instructionButton.setStyle(style);
        instructionButton.setFocusTraversable(false);
        instructionButton.setOnAction(e -> {
            Stage stage = new Stage();
            stage.setTitle("How To Play");
            stage.setScene(instructionScene);
            stage.show();
        });

        Button statButton = new Button("Statistics");
        statButton.setFont(Font.font("Helvetica", FontWeight.BOLD, 16));
        String statStyle = "-fx-border-color: #ffffff; -fx-font-size: 16pt;";
        statStyle += "-fx-background-color: #787c7f; -fx-text-fill: #ffffff";
        statButton.setStyle(statStyle);
        statButton.setFocusTraversable(false);
        statButton.setOnAction(e -> {
            calculateData();
            Stage stage = new Stage();
            stage.setTitle("Player Statistics");
            stage.setScene(statScene);
            stage.show();
        });

        bottomBox.getChildren().addAll(bottomText, resetButton, instructionButton, statButton);
        bottomBox.setAlignment(Pos.CENTER);
        return bottomBox;
    }

    /**
     * Creates the main game grid.
     * @return 5x6 grid of squares
     */
    private GridPane createGrid() {
        GridPane gameGrid = new GridPane();
        gameGrid.setVgap(10);
        gameGrid.setHgap(10);
        createRow(0, gameGrid);
        createRow(1, gameGrid);
        createRow(2, gameGrid);
        createRow(3, gameGrid);
        createRow(4, gameGrid);
        createRow(5, gameGrid);

        gameGrid.setAlignment(Pos.CENTER);
        return gameGrid;
    }

    /**
     * Removes text from a text box.
     */
    private void backSpace() {
        Label set = (Label) getNodeFromGridPane(grid, currentColumn - 1, currentRow);
        set.setText("");
        currentColumn--;
    }

    /**
     * Inputs text from a Key Press.
     * @param text the letter pressed
     */
    private void letterInput(String text) {
        if (gameOver) {
            return;
        }
        Label set = (Label) getNodeFromGridPane(grid, currentColumn, currentRow);
        set.setText(text);
        currentColumn++;
    }

    /**
     * Handles the Enter Key Press. Checks if game is won, lost, or neither.
     */
    private void enterKey() {
        if (gameOver) {
            return;
        }
        String guess = getGuess();
        try {
            String checker = game.check(guess);
            if (checker.equals("ggggg")) {
                gameOver = true;
                bottomText.setText(String.format("Congratulations! %s was the word!", game.getTarget().toUpperCase()));
                mainPane.setBackground(winBackground);
                analyzeGuessCount();
                gamesPlayed++;
                gamesWon++;
                streak++;
                calculateData();
            }
            colorize(checker);
            currentRow++;
            currentColumn = 0;
            if (currentRow >= 6) {
                gameOver = true;
                bottomText.setText(String.format("Game Over. The word was %s.", game.getTarget().toUpperCase()));
                gamesPlayed++;
                streak = 0;
                calculateData();
            }
        } catch (InvalidGuessException ige) {
            currentColumn = 0;
            Stage errorStage = new Stage();
            errorStage.setTitle("Error");
            errorStage.setScene(errorScene);
            errorStage.show();
            clearRow(currentRow);
        }
    }

    /**
     * Increments the data for how many guesses were used in one game.
     */
    private void analyzeGuessCount() {
        switch (currentRow) {
        case 0:
            firstGuess++;
            break;
        case 1:
            secondGuess++;
            break;
        case 2:
            thirdGuess++;
            break;
        case 3:
            fourthGuess++;
            break;
        case 4:
            fifthGuess++;
            break;
        case 5:
            sixthGuess++;
            break;
        default:
            break;
        }
    }

    /**
     * Creates the Statistics scene.
     * @return Statistics Scene with gameplay data for this session.
     */
    private Scene createStatScene() {
        VBox root = new VBox(10);

        HBox topStats = new HBox(20);
        playedLabel = new Label(String.format("Played: %d", gamesPlayed));
        setStatLabelStyle(playedLabel);
        winPercentLabel = new Label(String.format("Win Percent: %.2f", winRate));
        setStatLabelStyle(winPercentLabel);
        streakLabel = new Label(String.format("Current Streak: %d", streak));
        setStatLabelStyle(streakLabel);
        maxStreakLabel = new Label(String.format("Max Streak: %d", maxStreak));
        setStatLabelStyle(maxStreakLabel);
        topStats.setAlignment(Pos.CENTER);

        BarChart<Number, String> guessDistribution = createBarChart();

        topStats.getChildren().addAll(playedLabel, winPercentLabel, streakLabel, maxStreakLabel);

        root.getChildren().addAll(topStats, guessDistribution);
        return new Scene(root, 475, 400);
    }

    /**
     * Sets a specific style for labels in the Statistics window.
     * @param label label to set style
     */
    private void setStatLabelStyle(Label label) {
        label.setFont(Font.font("Helvetica", FontWeight.NORMAL, 14.5));
    }

    /**
     * Calculates data needed for statistics page and updates the statistics page.
     */
    private void calculateData() {
        if (gamesPlayed != 0) {
            double percent = (double) gamesWon / (double) gamesPlayed;
            percent *= 10000;
            percent = Math.round(percent);
            percent /= 100;
            winRate = percent;
        } else {
            winRate = 0;
        }

        if (gamesWon != 0) {
            int totalGuesses = firstGuess + 2 * secondGuess + 3 * thirdGuess
                + 4 * fourthGuess + 5 * fifthGuess + 6 * sixthGuess;
            avgGuess = totalGuesses / (gamesWon);
            avgGuess *= 100;
            avgGuess = Math.round(avgGuess) / 100;
        } else {
            avgGuess = 0;
        }
        if (streak > maxStreak) {
            maxStreak = streak;
        }

        playedLabel.setText(String.format("Played: %d", gamesPlayed));
        winPercentLabel.setText(String.format("Win Percent: %.2f", winRate));
        streakLabel.setText(String.format("Current Streak: %d", streak));
        maxStreakLabel.setText(String.format("Max Streak: %d", maxStreak));
        updateBarChart();
    }

    /**
     * Creates the bar chart in the statistics page.
     * @return Bar chart with data for each number of guesses
     */
    private BarChart<Number, String> createBarChart() {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setTickUnit(50);

        CategoryAxis yAxis = new CategoryAxis();
        BarChart<Number, String> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Guess Distribution");
        xAxis.setTickLabelRotation(90);

        series1 = new XYChart.Series<Number, String>();
        series1.getData().add(new XYChart.Data<Number, String>(firstGuess, "1"));
        series1.getData().add(new XYChart.Data<Number, String>(secondGuess, "2"));
        series1.getData().add(new XYChart.Data<Number, String>(thirdGuess, "3"));
        series1.getData().add(new XYChart.Data<Number, String>(fourthGuess, "4"));
        series1.getData().add(new XYChart.Data<Number, String>(fifthGuess, "5"));
        series1.getData().add(new XYChart.Data<Number, String>(sixthGuess, "6"));

        chart.getData().add(series1);
        return chart;
    }

    /**
     * Updates the bar chart in the statistics page.
     */
    private void updateBarChart() {
        series1.getData().set(0, new XYChart.Data<Number, String>(firstGuess, "1"));
        series1.getData().set(1, new XYChart.Data<Number, String>(secondGuess, "2"));
        series1.getData().set(2, new XYChart.Data<Number, String>(thirdGuess, "3"));
        series1.getData().set(3, new XYChart.Data<Number, String>(fourthGuess, "4"));
        series1.getData().set(4, new XYChart.Data<Number, String>(fifthGuess, "5"));
        series1.getData().set(5, new XYChart.Data<Number, String>(sixthGuess, "6"));
    }

    /**
     * Creates the instruction scene.
     * @return Scene with a title and explanation of rules and colors.
     */
    private Scene createInstructionScene() {
        VBox root = new VBox(20);
        Label head = new Label("How To Play");
        head.setFont(Font.font("Helvetica", FontWeight.BOLD, 60));
        head.setTextFill(Color.WHITE);

        Label instructions = new Label("Guess a 5 letter Word!");
        instructions.setFont(Font.font("Helvetica", FontWeight.NORMAL, 18));
        instructions.setTextFill(Color.web("#ffffff"));

        Label green = new Label("If the letter is in the CORRECT PLACE, the box will turn green!");
        green.setFont(Font.font("Helvetica", FontWeight.NORMAL, 18));
        green.setTextFill(Color.web("#6ca965"));

        Label yellow = new Label("If the letter is in the word but in the WRONG PLACE, the box will turn yellow!");
        yellow.setFont(Font.font("Helvetica", FontWeight.NORMAL, 18));
        yellow.setTextFill(Color.web("#c8b653"));

        Label gray = new Label("If the letter is NOT IN THE WORD, the box will turn gray.");
        gray.setFont(Font.font("Helvetica", FontWeight.NORMAL, 18));
        gray.setTextFill(Color.web("#787c7f"));

        root.setAlignment(Pos.TOP_CENTER);

        Background background = new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY));
        root.setBackground(background);
        root.getChildren().addAll(head, instructions, green, yellow, gray);
        return new Scene(root, width - 350, height - 600);
    }

    /**
     * Creates the simple error scene when an input is invalid.
     * @return scene that alerts player of their error
     */
    private Scene createErrorScene() {
        VBox layout = new VBox(25);
        Label error = new Label("Error");
        error.setFont(Font.font("Helvetica", FontWeight.BOLD, 18));
        Label description = new Label("Your guess is invalid! Please enter a 5-letter word.");
        description.setFont(Font.font("Helvetica", FontWeight.NORMAL, 14));
        layout.getChildren().addAll(error, description);
        layout.setAlignment(Pos.CENTER);

        return new Scene(layout, 350, 150);
    }

    /**
     * Sets the background to black.
     * @param pane the pane to change the background of
     */
    private void setBlackBackground(BorderPane pane) {
        BackgroundFill backgroundFill = new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY);

        Background background = new Background(backgroundFill);
        pane.setBackground(background);
    }

    /**
     * Creates a row of Label Squares.
     * @param rowIndex the row Index to create this at for the grid
     * @param pane the grid pane to add to
     */
    private void createRow(int rowIndex, GridPane pane) {
        pane.addRow(rowIndex, createLabelBox(), createLabelBox(), createLabelBox(), createLabelBox(), createLabelBox());
    }

    /**
     * Creates a square label to input letters.
     * @return a square label
     */
    private Label createLabelBox() {
        Label text = new Label();
        text.setPrefSize(75, 75);
        text.setFont(Font.font("Helvetica", FontWeight.BOLD, 35));
        text.setAlignment(Pos.CENTER);
        text.setStyle("-fx-display-caret: false; -fx-border-color: #ffffff; -fx-text-fill: #ffffff;");
        text.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        return text;
    }

    /**
     * Initializes game values.
     */
    private void playGame() {
        currentRow = 0;
        currentColumn = 0;
    }

    /**
     * Gets the word guess from the grid pane.
     * @return the String of the guess
     */
    private String getGuess() {
        String guess = "";
        for (int i = 0; i < 5; ++i) {
            Label text = (Label) getNodeFromGridPane(grid, i, currentRow);
            if (text.getText() == null || text.getText().equals("")) {
                return guess;
            }
            guess += text.getText();
        }
        return guess;
    }

    /**
     * Colors the squares based on their accuracy.
     * @param checker the String that has values on letter accuracy
     */
    private void colorize(String checker) {
        for (int i = 0; i < 5; ++i) {
            Label label = (Label) getNodeFromGridPane(grid, i, currentRow);
            if (checker.charAt(i) == 'g') {
                label.setBackground(new Background(new BackgroundFill(Color.web("#6ca965"),
                    CornerRadii.EMPTY, Insets.EMPTY)));
            } else if (checker.charAt(i) == 'y') {
                label.setBackground(new Background(new BackgroundFill(Color.web("#c8b653"),
                    CornerRadii.EMPTY, Insets.EMPTY)));
            } else if (checker.charAt(i) == 'i') {
                label.setBackground(new Background(new BackgroundFill(Color.web("#787c7f"),
                    CornerRadii.EMPTY, Insets.EMPTY)));
            }
        }
    }

    /**
     * Clears the row of letters and colors.
     * @param rowNum the row index to clear
     */
    private void clearRow(int rowNum) {
        for (int i = 0; i < 5; ++i) {
            Label box = (Label) getNodeFromGridPane(grid, i, rowNum);
            box.setText("");
            box.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }

    /**
     * Returns the node at a point in a gridPane.
     * @param gridPane the pane to search through
     * @param col the column index
     * @param row the row index
     * @return the node at this point in the grid
     */
    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }
}
