import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.ArrayList;

/**
 * The main GUI program for the Casino Project
 * @author Travis Brown (Kwzii)
 */
public class CasinoGUI extends Application implements Observer<CasinoModel, String> {
    private CasinoModel model;
    private final static String RESOURCES_DIR = "resources/PNG-cards/";
    private boolean loggedin;
    private final Label startScreenLabel = new Label("Please Sign Up or Login");
    private final Label homeLabel = new Label("Choose a game :)");
    private final Label loginMessage = new Label("Login");
    private final Label signupMessage = new Label("Sign up");
    private final Label bjCreditLabel = new Label("Credits: " + 0);
    private final Label homeCreditLabel = new Label("Credits: " + 0);
    private final Label blackjackAlertMessage = new Label("");
    private final Font basicFont = new Font("Ariel", 19);
    private final Font largeFont = new Font("Ariel", 24);
    private int numPlayerCards = 0;
    private int numDealerCards = 0;
    private double cardWidth = 120.5;
    private Stage mainStage;
    private Scene startupScene;
    private Scene loginScene;
    private Scene signupScene;
    private Scene homeScene;
    private Scene blackjackScene;
    private Scene rouletteScene;
    private Scene pokerScene;
    private Scene slotsScene;
    private Scene coinflipScene;
    private Scene horsebetScene;

    /**
     * In the init the GUI creates the model and adds itself as an observer
     * of it.
     */
    @Override
    public void init() {
        this.model = new CasinoModel();
        model.addObserver(this);
        populateCards();
        System.out.println("init: Initialize and connect to model!");
    }

    /**
     * Calls stages to be created and shows the main stage
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     */
    @Override
    public void start(Stage stage) {
        createMainStage(stage);
        blackjackScene = blackjack();
        /*
        try {
            Parent blackjack = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("BlackJack.fxml")));
            blackjackScene = new Scene(blackjack);
            Parent roulette = FXMLLoader.load(getClass().getResource("BlackJack.fxml"));
            rouletteScene = new Scene(roulette);
            Parent poker = FXMLLoader.load(getClass().getResource("BlackJack.fxml"));
            pokerScene = new Scene(poker);
            Parent slots = FXMLLoader.load(getClass().getResource("BlackJack.fxml"));
            slotsScene = new Scene(slots);
            Parent coinFlip = FXMLLoader.load(getClass().getResource("BlackJack.fxml"));
            coinflipScene = new Scene(coinFlip);
            Parent horseRace = FXMLLoader.load(getClass().getResource("BlackJack.fxml"));
            horsebetScene = new Scene(horseRace);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
         */

        mainStage.setTitle("Casino GUI");
        update(model, "Please Sign Up or Login");
        mainStage.setScene(startupScene);
        mainStage.sizeToScene();
        mainStage.show();
        centerScreen();
    }

    /**
     * Creates the main Stage that the initial view will show
     * @param stage the primary stage for this application, onto which
     *  the application scene can be set.
     *  Applications may create other stages, if needed, but they will not be
     *  primary stages.
     */
    public void createMainStage(Stage stage) {
        mainStage = stage;
        TextField loginUsrField = new TextField();
        PasswordField loginPassField = new PasswordField();
        TextField signupUsrField = new TextField();
        PasswordField signupPassField = new PasswordField();

        // LABEL SECTION
        homeCreditLabel.setFont(basicFont);
        homeCreditLabel.setAlignment(Pos.CENTER);

        startScreenLabel.setFont(basicFont);
        startScreenLabel.setAlignment(Pos.TOP_CENTER);

        signupMessage.setFont(basicFont);
        signupMessage.setAlignment(Pos.TOP_CENTER);

        loginMessage.setFont(basicFont);
        loginMessage.setAlignment(Pos.TOP_CENTER);

        homeLabel.setFont(basicFont);
        homeLabel.setAlignment(Pos.CENTER);

        loginUsrField.setPromptText("Enter your Username");
        loginUsrField.setAlignment(Pos.CENTER);

        loginPassField.setPromptText("Enter your Password");
        loginPassField.setAlignment(Pos.CENTER);

        signupUsrField.setPromptText("Enter your Username");
        signupUsrField.setAlignment(Pos.CENTER);

        signupPassField.setPromptText("Enter your Password");
        signupPassField.setAlignment(Pos.CENTER);


        // BUTTON SECTION
        // Startup screen buttons
        Button signUpButton = new Button("Sign up");
        signUpButton.setMinSize(200, 150);
        signUpButton.setFont(basicFont);
        signUpButton.setAlignment(Pos.CENTER);
        signUpButton.setTextAlignment(TextAlignment.CENTER);
        signUpButton.setOnAction(event -> model.setScene(Scenes.SIGNUP));
        signUpButton.setFocusTraversable(false);

        Button logInButton = new Button("Login");
        logInButton.setMinSize(200, 150);
        logInButton.setFont(basicFont);
        logInButton.setAlignment(Pos.CENTER);
        logInButton.setTextAlignment(TextAlignment.CENTER);
        logInButton.setOnAction(event -> model.setScene(Scenes.LOGIN));
        logInButton.setFocusTraversable(false);

        // Create new account buttons
        Button signupBackButton = new Button("Back");
        signupBackButton.setMinSize(125, 50);
        signupBackButton.setFont(basicFont);
        signupBackButton.setAlignment(Pos.CENTER);
        signupBackButton.setTextAlignment(TextAlignment.CENTER);

        Button signupSubmitButton = new Button("Submit");
        signupSubmitButton.setMinSize(125, 50);
        signupSubmitButton.setFont(basicFont);
        signupSubmitButton.setAlignment(Pos.CENTER);
        VBox signupVBox = new VBox(signupMessage, signupUsrField,
                signupPassField, signupSubmitButton, signupBackButton);
        signupSubmitButton.setOnAction(e -> {
            if ((signupUsrField.getText() != null && !signupPassField.getText().isEmpty())) {
                model.updateModel(signupUsrField.getText() + ", thank you for signing up!");
                model.signUp(signupUsrField.getText(), signupPassField.getText());
            } else {
                model.updateModel("You have not entered the required fields.");
            }
        });
        signupVBox.setSpacing(10);
        signupVBox.setPadding(new Insets(10, 50, 10, 50));
        signupVBox.setAlignment(Pos.CENTER);
        signupScene = new Scene(signupVBox, 500, 250);

        // Log into previously existing account screen
        Button loginBackButton = new Button("Back");
        loginBackButton.setMinSize(125, 50);
        loginBackButton.setFont(basicFont);
        loginBackButton.setAlignment(Pos.CENTER);
        loginBackButton.setTextAlignment(TextAlignment.CENTER);

        Button loginSubmitButton = new Button("Submit");
        loginSubmitButton.setMinSize(125, 50);
        loginSubmitButton.setFont(basicFont);
        loginSubmitButton.setAlignment(Pos.CENTER);
        VBox loginVBox = new VBox(loginMessage, loginUsrField, loginPassField,
                loginSubmitButton, loginBackButton);
        loginSubmitButton.setOnAction(e -> {
            if ((loginUsrField.getText() != null && !loginPassField.getText().isEmpty())) {
                model.updateModel(loginUsrField.getText() + ", thank you for logging in!");
                model.login(loginUsrField.getText(), loginPassField.getText());
            } else {
                model.updateModel("You have not entered the required fields.");
            }
        });
        loginVBox.setSpacing(10);
        loginVBox.setPadding(new Insets(10, 50, 10, 50));
        loginVBox.setAlignment(Pos.CENTER);
        loginScene = new Scene(loginVBox, 500, 250);

        // Game Home Buttons
        Button blackjackButton = new Button("Blackjack");
        blackjackButton.setMinSize(150, 100);
        blackjackButton.setFont(basicFont);
        blackjackButton.setAlignment(Pos.CENTER);
        blackjackButton.setOnAction(event -> model.setScene(Scenes.BLACKJACK));

        Button rouletteButton = new Button("Roulette");
        rouletteButton.setMinSize(150, 100);
        rouletteButton.setFont(basicFont);
        rouletteButton.setAlignment(Pos.CENTER);

        Button pokerButton = new Button("Poker");
        pokerButton.setMinSize(150, 100);
        pokerButton.setFont(basicFont);
        pokerButton.setAlignment(Pos.CENTER);

        Button slotsButton = new Button("Slots");
        slotsButton.setMinSize(150, 100);
        slotsButton.setFont(basicFont);
        slotsButton.setAlignment(Pos.CENTER);

        Button coinFlipButton = new Button("Coin Flips");
        coinFlipButton.setMinSize(150, 100);
        coinFlipButton.setFont(basicFont);
        coinFlipButton.setAlignment(Pos.CENTER);

        Button horsesButton = new Button("Horse Betting");
        horsesButton.setMinSize(150, 100);
        horsesButton.setFont(basicFont);
        horsesButton.setAlignment(Pos.CENTER);

        // Game Home Scene
        GridPane homeGameGrid = new GridPane();
        homeGameGrid.setPadding(new Insets(10, 10, 10, 10));
        homeGameGrid.setVgap(15);
        homeGameGrid.setHgap(15);
        homeGameGrid.setAlignment(Pos.CENTER);
        GridPane.setConstraints(homeCreditLabel, 4, 0);
        homeGameGrid.getChildren().add(homeCreditLabel);
        GridPane.setConstraints(blackjackButton, 0, 1);
        homeGameGrid.getChildren().add(blackjackButton);
        GridPane.setConstraints(rouletteButton, 1, 1);
        homeGameGrid.getChildren().add(rouletteButton);
        GridPane.setConstraints(pokerButton, 2, 1);
        homeGameGrid.getChildren().add(pokerButton);
        GridPane.setConstraints(slotsButton, 0, 2);
        homeGameGrid.getChildren().add(slotsButton);
        GridPane.setConstraints(coinFlipButton, 1, 2);
        homeGameGrid.getChildren().add(coinFlipButton);
        GridPane.setConstraints(horsesButton, 2, 2);
        homeGameGrid.getChildren().add(horsesButton);

        VBox homeVBox = new VBox(homeLabel, homeGameGrid);
        homeVBox.setPadding(new Insets(20, 0, 20, 0));
        homeVBox.setAlignment(Pos.CENTER);
        homeScene = new Scene(homeVBox, 800, 450);

        HBox startScreenHBox = new HBox(signUpButton, logInButton);
        startScreenHBox.setAlignment(Pos.CENTER);
        startScreenHBox.setSpacing(10);

        VBox startScreenVBox = new VBox(startScreenLabel, startScreenHBox);
        startScreenVBox.setAlignment(Pos.CENTER);

        startScreenVBox.setSpacing(30);
        startScreenVBox.setPadding(new Insets(20, 35, 20, 35));

        startupScene = new Scene(startScreenVBox, 500, 250);
        signupBackButton.setOnAction(event -> model.setScene(Scenes.STARTUP));
        loginBackButton.setOnAction(event -> model.setScene(Scenes.STARTUP));
    }

    /**
     * Creates the blackjack scene and all of its actions
     * @return the created blackjack scene
     */
    public Scene blackjack() {
        Label dealerLabel = new Label("Dealer");
        dealerLabel.setFont(largeFont);
        Label playerLabel = new Label("Player");
        playerLabel.setFont(largeFont);
        playerLabel.setFont(largeFont);

        blackjackAlertMessage.setFont(basicFont);

        HBox playerHand = new HBox();
//        playerHand.setAlignment(Pos.CENTER);  // TODO animations do not work if HBox's are centered :(
        playerHand.setMinHeight(175);

        HBox dealerHand = new HBox();
//        dealerHand.setAlignment(Pos.CENTER);  // TODO. figure something out about this. it looks bad not centered
        dealerHand.setMinHeight(175);

        Button hitButton = new Button("HIT");
        hitButton.setMinSize(100, 75);
        hitButton.setFont(basicFont);
        hitButton.setAlignment(Pos.CENTER);
        hitButton.setTextAlignment(TextAlignment.CENTER);

        Button stayButton = new Button("STAY");
        stayButton.setMinSize(100, 75);
        stayButton.setFont(basicFont);
        stayButton.setAlignment(Pos.CENTER);
        stayButton.setTextAlignment(TextAlignment.CENTER);

        Button submitBjBetButton = new Button("Submit Bet");
        submitBjBetButton.setMinSize(100, 75);
        submitBjBetButton.setFont(basicFont);
        submitBjBetButton.setAlignment(Pos.CENTER);
        submitBjBetButton.setTextAlignment(TextAlignment.CENTER);

        Button playAgainBjButton = new Button("Play Again?");
        playAgainBjButton.setMinSize(100, 75);
        playAgainBjButton.setFont(basicFont);
        playAgainBjButton.setAlignment(Pos.CENTER);
        playAgainBjButton.setTextAlignment(TextAlignment.CENTER);

        TextField enterBetField = new TextField();
        enterBetField.setPromptText("Enter your bet");
        enterBetField.setAlignment(Pos.CENTER);

        ImageView backCard = new ImageView(new Image("file:" + model.getBackCard()));
        backCard.setFitHeight(175);
        backCard.setPreserveRatio(true);

        ImageView backCardStack = new ImageView(new Image("file:" + model.getBackCard()));
        backCardStack.setFitHeight(175);
        backCardStack.setPreserveRatio(true);

        HBox buttonHBox = new HBox(enterBetField, submitBjBetButton);
        buttonHBox.setAlignment(Pos.CENTER);
        buttonHBox.setSpacing(10);

        submitBjBetButton.setOnAction(e -> {
            if (!enterBetField.getText().isEmpty()) {
                try {
                    if (model.placeBet(Integer.parseInt(enterBetField.getText())) == 0) {
                        blackjackAlertMessage.setText("");
                        buttonHBox.getChildren().removeAll(enterBetField, submitBjBetButton);

                        ImageView dealer1 = new ImageView(new Image("file:" + model.dealerHitBlackjack()));
                        dealer1.setFitWidth(cardWidth);
                        dealer1.setPreserveRatio(true);
                        dealerHand.getChildren().add(dealer1);
                        dealer1.setVisible(false);

                        ImageView dealer2 = new ImageView(new Image("file:" + model.getBackCard()));
                        dealer2.setFitHeight(175);
                        dealer2.setPreserveRatio(true);
                        dealerHand.getChildren().add(dealer2);
                        dealer2.setVisible(false);

                        ImageView player1 = new ImageView(new Image("file:" + model.playerHitBlackjack()));
                        player1.setFitWidth(cardWidth);
                        player1.setPreserveRatio(true);
                        playerHand.getChildren().add(player1);
                        player1.setVisible(false);

                        ImageView player2 = new ImageView(new Image("file:" + model.playerHitBlackjack()));
                        player2.setFitWidth(cardWidth);
                        player2.setPreserveRatio(true);
                        playerHand.getChildren().add(player2);
                        player2.setVisible(false);

//                        FadeTransition fd = placeCard(dealer1, backCard, 'D');
//                        fd.setOnFinished(event -> {
//                            FadeTransition fd1 = placeCard(dealer2, backCard, 'D');
//                            fd1.setOnFinished(event1 -> {
//                                FadeTransition fd2 = placeCard(player1, backCard, 'P');
//                                fd2.setOnFinished(event2 -> {
//                                    FadeTransition fd3 = placeCard(player2, backCard, 'P');
//                                });
//                            });
//                        });


                        FadeTransition finalTrans = dealStartingCards(dealer1, dealer2, player1, player2, backCard);
                        finalTrans.setOnFinished(event -> {
                            buttonHBox.getChildren().addAll(hitButton, stayButton);
                            if (model.initialCheckBjWin() == GameResults.WIN) {
                                buttonHBox.getChildren().removeAll(hitButton, stayButton);
                                buttonHBox.getChildren().addAll(playAgainBjButton);
                            }
                        });
                    }
                } catch (NumberFormatException nfe) {
                    model.updateModel("Please enter a number");
                }
            } else {
                model.updateModel("You have not entered a bet");
            }
        });

        playAgainBjButton.setOnAction(e -> {
            buttonHBox.getChildren().remove(playAgainBjButton);
            buttonHBox.getChildren().addAll(enterBetField, submitBjBetButton);
            model.resetCardDeck();
            playerHand.getChildren().clear();
            dealerHand.getChildren().clear();
            numPlayerCards = 0;
            numDealerCards = 0;
            enterBetField.setText("");
        });
        hitButton.setOnAction(event -> {
            ImageView temp = new ImageView(new Image("file:" + model.playerHitBlackjack()));
            temp.setFitWidth(cardWidth);
            temp.setPreserveRatio(true);
            playerHand.getChildren().add(temp);
            temp.setVisible(false);
            placeCard(temp, backCard, 'P');
            if (model.midCheckBjWin() != GameResults.NONE) {
                dealerHand.getChildren().remove(1);
                numDealerCards--;
                ImageView dealer1 = new ImageView(new Image("file:" + model.dealerHitBlackjack()));
                dealer1.setFitWidth(cardWidth);
                dealer1.setPreserveRatio(true);
                dealerHand.getChildren().add(dealer1);
                buttonHBox.getChildren().removeAll(hitButton, stayButton);
                buttonHBox.getChildren().addAll(playAgainBjButton);
            }
        });

        stayButton.setOnAction(event -> {
            dealerHand.getChildren().remove(1);
            numDealerCards--;
            ImageView dealer1 = new ImageView(new Image("file:" + model.dealerHitBlackjack()));
            dealer1.setFitWidth(cardWidth);
            dealer1.setPreserveRatio(true);
            dealerHand.getChildren().add(dealer1);
//            dealer1.setVisible(false);
//            placeCard(dealer1, backCard, 'D');
            while (model.bjGetHandTotal('D') < 17) {
                ImageView temp = new ImageView(new Image("file:" + model.dealerHitBlackjack()));
                temp.setFitWidth(cardWidth);
                temp.setPreserveRatio(true);
                dealerHand.getChildren().add(temp);
//                temp.setVisible(false);
//                placeCard(temp, backCard, 'D');
            }
            model.finalCheckBjWin();
            buttonHBox.getChildren().removeAll(hitButton, stayButton);
            buttonHBox.getChildren().addAll(playAgainBjButton);
        });

        Button gameBackButton = new Button("Back");
        gameBackButton.setMinSize(125, 50);
        gameBackButton.setFont(basicFont);
        gameBackButton.setAlignment(Pos.CENTER);
        gameBackButton.setTextAlignment(TextAlignment.CENTER);
        gameBackButton.setOnAction(e ->{
            model.resetCardDeck();
            playerHand.getChildren().clear();
            dealerHand.getChildren().clear();
            numPlayerCards = 0;
            numDealerCards = 0;
            if(buttonHBox.getChildren().contains(hitButton)) {
                buttonHBox.getChildren().removeAll(hitButton, stayButton);
                buttonHBox.getChildren().addAll(enterBetField, submitBjBetButton);
            } else if (buttonHBox.getChildren().contains(playAgainBjButton)) {
                buttonHBox.getChildren().remove(playAgainBjButton);
                buttonHBox.getChildren().addAll(enterBetField, submitBjBetButton);
            }
            model.placeBet(0);
            enterBetField.setText("");
            model.setScene(Scenes.HOME);
        });

        bjCreditLabel.setFont(basicFont);
        bjCreditLabel.setAlignment(Pos.BOTTOM_CENTER);
        HBox topHBox = new HBox(bjCreditLabel, gameBackButton);
        topHBox.setAlignment(Pos.TOP_RIGHT);
        topHBox.setSpacing(45);

        StackPane deckPane = new StackPane(backCard, backCardStack);

        VBox centerVBox = new VBox(dealerLabel, dealerHand, playerLabel, playerHand);
        centerVBox.setAlignment(Pos.CENTER);
        centerVBox.setSpacing(30);
        centerVBox.setPadding(new Insets(20, 35, 20, 35));

        VBox bottomVBox = new VBox(blackjackAlertMessage, buttonHBox);
        bottomVBox.setAlignment(Pos.CENTER);
        bottomVBox.setSpacing(30);
        bottomVBox.setPadding(new Insets(20, 35, 20, 35));

        BorderPane bPane = new BorderPane();
        bPane.setTop(topHBox);
        bPane.setCenter(centerVBox);
        bPane.setRight(deckPane);
        bPane.setBottom(bottomVBox);
        bPane.setPadding(new Insets(20, 35, 20, 35));
        return new Scene(bPane, 1200, 800);
    }

    /**
     * Method to animate cards being placed on the table
     * @param imgV image view that is being replaced by the card
     */
    public FadeTransition placeCard(ImageView imgV, ImageView backCard, char who) {
        Bounds startCoords = backCard.localToScene(backCard.getBoundsInLocal());
        Bounds endCoords = imgV.localToScene(imgV.getBoundsInLocal());
        TranslateTransition translate = new TranslateTransition();
        translate.setNode(backCard);

        if (who == 'D') {
            double xDist = (endCoords.getMaxX() + (cardWidth * numDealerCards)) - startCoords.getMaxX();
            translate.setDuration(Duration.millis(-xDist*1.75));
            translate.setByX(xDist);
            numDealerCards++;
        }
        else if (who == 'P'){
            double xDist = ((endCoords.getMaxX() + (cardWidth * numPlayerCards)) - startCoords.getMaxX());
            translate.setDuration(Duration.millis(-xDist*1.75));
            translate.setByX(xDist);
            numPlayerCards++;
        }
        translate.setByY(endCoords.getMinY() - startCoords.getMinY());
        translate.play();

        FadeTransition fade = new FadeTransition();
        fade.setNode(backCard);
        fade.setDuration(Duration.millis(700));
        fade.setInterpolator(Interpolator.LINEAR);
        fade.setFromValue(1);
        fade.setToValue(0);

        translate.setOnFinished(event -> {
            imgV.setVisible(true);
            fade.play();
        });
        FadeTransition fadeBack = new FadeTransition();
        fadeBack.setNode(backCard);
        fadeBack.setDuration(Duration.millis(1));
        fadeBack.setInterpolator(Interpolator.LINEAR);
        fadeBack.setFromValue(0);
        fadeBack.setToValue(1);

        TranslateTransition transitionBack = new TranslateTransition();
        transitionBack.setNode(backCard);

        transitionBack.setDuration(Duration.millis(10));
        if (who == 'D') {
            transitionBack.setByX((startCoords.getMaxX() - cardWidth*(numDealerCards-1)) - (endCoords.getMaxX()));
        }
        else if (who == 'P'){
            transitionBack.setByX((startCoords.getMaxX() - cardWidth*(numPlayerCards-1)) - (endCoords.getMaxX()));
        }
        transitionBack.setByY(startCoords.getMinY() - endCoords.getMinY());

        fade.setOnFinished(event -> transitionBack.play());
        transitionBack.setOnFinished(event -> fadeBack.play());
        return fadeBack;
    }


    /**
     * Method to animate the initial 4 cards being placed on the table
     * Special method needed to stop glitches in the animation process
     * @param imgD1 image view of the 1st card the dealer starts with
     * @param imgD2 image view of the 2nd card the dealer starts with
     * @param imgP1 image view of the 1st card the player starts with
     * @param imgP2 image view of the 2nd card the player starts with
     * @param backCard the image view of the back card that is being the "deck of cards" we are pulling from
     */
    public FadeTransition dealStartingCards(ImageView imgD1, ImageView imgD2, ImageView imgP1, ImageView imgP2, ImageView backCard) {
        Bounds startCoords = backCard.localToScene(backCard.getBoundsInLocal());
        Bounds endCoords = imgD1.localToScene(imgD1.getBoundsInLocal());
        TranslateTransition translate1 = new TranslateTransition();
        translate1.setNode(backCard);

        double xDist = (endCoords.getMaxX() + (cardWidth * numDealerCards)) - startCoords.getMaxX();
        translate1.setDuration(Duration.millis(-xDist * 1.75));
        translate1.setByX(xDist);
        numDealerCards++;
        translate1.setByY(endCoords.getMinY() - startCoords.getMinY());
        translate1.play();

        FadeTransition fade1 = new FadeTransition();
        fade1.setNode(backCard);
        fade1.setDuration(Duration.millis(700));
        fade1.setInterpolator(Interpolator.LINEAR);
        fade1.setFromValue(1);
        fade1.setToValue(0);

        translate1.setOnFinished(event -> {
            imgD1.setVisible(true);
            fade1.play();
        });

        FadeTransition fadeBack1 = new FadeTransition();
        fadeBack1.setNode(backCard);
        fadeBack1.setDuration(Duration.millis(1));
        fadeBack1.setInterpolator(Interpolator.LINEAR);
        fadeBack1.setFromValue(0);
        fadeBack1.setToValue(1);

        TranslateTransition transitionBack1 = new TranslateTransition();
        transitionBack1.setNode(backCard);

        transitionBack1.setDuration(Duration.millis(10));
        transitionBack1.setByX((startCoords.getMaxX() - cardWidth*(numDealerCards-1)) - (endCoords.getMaxX()));

        transitionBack1.setByY(startCoords.getMinY() - endCoords.getMinY());

        fade1.setOnFinished(event -> transitionBack1.play());
        transitionBack1.setOnFinished(event -> fadeBack1.play());

        Bounds startCoords2 = backCard.localToScene(backCard.getBoundsInLocal());
        Bounds endCoords2 = imgD2.localToScene(imgD2.getBoundsInLocal());


        TranslateTransition translate2 = new TranslateTransition();
        translate2.setNode(backCard);

        double xDist2 = (endCoords2.getMaxX() + (cardWidth * numDealerCards)) - startCoords2.getMaxX();
        translate2.setDuration(Duration.millis(-xDist2 * 1.75));
        translate2.setByX(xDist2);
        numDealerCards++;

        translate2.setByY(endCoords2.getMinY() - startCoords2.getMinY());

        FadeTransition fade2 = new FadeTransition();
        fade2.setNode(backCard);
        fade2.setDuration(Duration.millis(1000));
        fade2.setInterpolator(Interpolator.LINEAR);
        fade2.setFromValue(1);
        fade2.setToValue(0);

        translate2.setOnFinished(event -> {
            imgD2.setVisible(true);
            fade2.play();
        });

        FadeTransition fadeBack2 = new FadeTransition();
        fadeBack2.setNode(backCard);
        fadeBack2.setDuration(Duration.millis(1));
        fadeBack2.setInterpolator(Interpolator.LINEAR);
        fadeBack2.setFromValue(0);
        fadeBack2.setToValue(1);

        TranslateTransition transitionBack2 = new TranslateTransition();
        transitionBack2.setNode(backCard);

        transitionBack2.setDuration(Duration.millis(10));

        transitionBack2.setByX((startCoords2.getMaxX() - cardWidth*(numDealerCards-1)) - (endCoords2.getMaxX()));

        transitionBack2.setByY(startCoords2.getMinY() - endCoords2.getMinY());

        fade2.setOnFinished(event -> transitionBack2.play());
        transitionBack2.setOnFinished(event -> fadeBack2.play());

        fadeBack1.setOnFinished(event -> translate2.play());


        Bounds startCoords3 = backCard.localToScene(backCard.getBoundsInLocal());
        Bounds endCoords3 = imgP1.localToScene(imgP1.getBoundsInLocal());
        TranslateTransition translate3 = new TranslateTransition();
        translate3.setNode(backCard);

        double xDist3 = (endCoords3.getMaxX() + (cardWidth * numPlayerCards)) - startCoords3.getMaxX();
        translate3.setDuration(Duration.millis(-xDist3 * 1.75));
        translate3.setByX(xDist3);
        numPlayerCards++;
        translate3.setByY(endCoords3.getMinY() - startCoords3.getMinY());

        FadeTransition fade3 = new FadeTransition();
        fade3.setNode(backCard);
        fade3.setDuration(Duration.millis(700));
        fade3.setInterpolator(Interpolator.LINEAR);
        fade3.setFromValue(1);
        fade3.setToValue(0);

        translate3.setOnFinished(event -> {
            imgP1.setVisible(true);
            fade3.play();
        });

        FadeTransition fadeBack3 = new FadeTransition();
        fadeBack3.setNode(backCard);
        fadeBack3.setDuration(Duration.millis(1));
        fadeBack3.setInterpolator(Interpolator.LINEAR);
        fadeBack3.setFromValue(0);
        fadeBack3.setToValue(1);

        TranslateTransition transitionBack3 = new TranslateTransition();
        transitionBack3.setNode(backCard);

        transitionBack3.setDuration(Duration.millis(10));
        transitionBack3.setByX((startCoords3.getMaxX() - cardWidth*(numPlayerCards-1)) - (endCoords3.getMaxX()));

        transitionBack3.setByY(startCoords3.getMinY() - endCoords3.getMinY());

        fade3.setOnFinished(event -> transitionBack3.play());
        transitionBack3.setOnFinished(event -> fadeBack3.play());

        Bounds startCoords4 = backCard.localToScene(backCard.getBoundsInLocal());
        Bounds endCoords4 = imgP2.localToScene(imgP2.getBoundsInLocal());


        TranslateTransition translate4 = new TranslateTransition();
        translate4.setNode(backCard);

        double xDist4 = (endCoords4.getMaxX() + (cardWidth * numPlayerCards)) - startCoords4.getMaxX();
        translate4.setDuration(Duration.millis(-xDist4 * 1.75));
        translate4.setByX(xDist4);
        numPlayerCards++;

        translate4.setByY(endCoords4.getMinY() - startCoords4.getMinY());

        FadeTransition fade4 = new FadeTransition();
        fade4.setNode(backCard);
        fade4.setDuration(Duration.millis(1000));
        fade4.setInterpolator(Interpolator.LINEAR);
        fade4.setFromValue(1);
        fade4.setToValue(0);

        translate4.setOnFinished(event -> {
            imgP2.setVisible(true);
            fade4.play();
        });

        FadeTransition fadeBack4 = new FadeTransition();
        fadeBack4.setNode(backCard);
        fadeBack4.setDuration(Duration.millis(1));
        fadeBack4.setInterpolator(Interpolator.LINEAR);
        fadeBack4.setFromValue(0);
        fadeBack4.setToValue(1);
        TranslateTransition transitionBack4 = new TranslateTransition();
        transitionBack4.setNode(backCard);

        transitionBack4.setDuration(Duration.millis(10));

        transitionBack4.setByX((startCoords4.getMaxX() - cardWidth*(numPlayerCards-1)) - (endCoords4.getMaxX()));

        transitionBack4.setByY(startCoords4.getMinY() - endCoords4.getMinY());

        fade4.setOnFinished(event -> transitionBack4.play());
        transitionBack4.setOnFinished(event -> fadeBack4.play());

        fadeBack3.setOnFinished(event -> translate4.play());
        fadeBack2.setOnFinished(event -> translate3.play());

        return fadeBack4;
    }

    /**
     * Method to create all playing cards from text file to use for card games
     */
    public void populateCards() {
        String fileName = "data/playingCardsPNG.txt";
        File file = new File(fileName);
        ArrayList<PlayingCards> pcards = new ArrayList<>();
        try (FileReader fr = new FileReader(file)) {
            BufferedReader br = new BufferedReader(fr);
            String line;
            PlayingCards.Face face;
            PlayingCards.Suit suitVal = null;
            while ((line = br.readLine()) != null) {
                int val = 10;
                String[] info = line.strip().split("_");
                if (line.equals("card_back.png")) {
                    continue;
                }
                switch (info[0]) {
                    case "ace" -> {
                        face = PlayingCards.Face.ACE;
                        val = 11;
                    }
                    case "jack" -> face = PlayingCards.Face.JACK;
                    case "king" -> face = PlayingCards.Face.KING;
                    case "queen" -> face = PlayingCards.Face.QUEEN;
                    default -> {
                        face = PlayingCards.Face.NONFACE;
                        val = Integer.parseInt(info[0]);
                    }
                }
                switch (info[2]) {
                    case "hearts.png" -> suitVal = PlayingCards.Suit.HEARTS;
                    case "spades.png" -> suitVal = PlayingCards.Suit.SPADES;
                    case "diamonds.png" -> suitVal = PlayingCards.Suit.DIAMONDS;
                    case "clubs.png" -> suitVal = PlayingCards.Suit.CLUBS;
                }
                PlayingCards card = new PlayingCards(suitVal, face, val, RESOURCES_DIR + line);
                pcards.add(card);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        model.setFullCardDeck(pcards);
    }

    /**
     * Centers the application on the users screen
     */
    void centerScreen() {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        mainStage.setX((screenBounds.getWidth() - mainStage.getWidth()) / 2);
        mainStage.setY((screenBounds.getHeight() - mainStage.getHeight()) / 2);
    }

    /**
     * Update method to update the application window to the new information
     * @param casinoModel the object that wishes to inform this object
     *                about something that has happened.
     * @param text optional data the server.model can send to the observer
     *
     */
    @Override
    public void update(CasinoModel casinoModel, String text) {
        model = casinoModel;
        switch (casinoModel.currentScene) {
            case LOGIN -> mainStage.setScene(loginScene);

            case SIGNUP -> mainStage.setScene(signupScene);

            case STARTUP -> mainStage.setScene(startupScene);

            case HOME -> {
                mainStage.setScene(homeScene);
                loggedin = true;
            }

            case BLACKJACK -> mainStage.setScene(blackjackScene);

            case ROULETTE -> mainStage.setScene(rouletteScene);

            case POKER -> mainStage.setScene(pokerScene);

            case SLOTS -> mainStage.setScene(slotsScene);

            case COINFLIP -> mainStage.setScene(coinflipScene);

            case HORSEBETTING -> mainStage.setScene(horsebetScene);
        }
        centerScreen();

        if (text != null) {
            switch (casinoModel.currentScene) {
                case LOGIN ->  loginMessage.setText(text);

                case SIGNUP -> signupMessage.setText(text);

                case STARTUP -> startScreenLabel.setText(text);

                case HOME -> homeLabel.setText(text);

                case BLACKJACK -> blackjackAlertMessage.setText(text);
            }
        }
        // Set the credits to their proper value
        if(loggedin) {
            bjCreditLabel.setText("Credits: " + model.getActivePlayer().getChips());
            homeCreditLabel.setText("Credits: " + model.getActivePlayer().getChips());
        }
    }
}
