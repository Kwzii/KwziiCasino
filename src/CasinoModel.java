import java.lang.reflect.Array;
import java.util.List;
import java.util.*;

/**
 * The main model that the GUI works off of for the Casino.
 * @author Travis Brown (Kwzii)
 */
public class CasinoModel {
    private final static String RESOURCES_DIR = "resources/PNG-cards/";
    public static Player activePlayerAccount;
    AccountData accounts = new AccountData();
    Scenes currentScene;
    HashMap<String, Player> accountMap;
    private final List<Observer<CasinoModel, String>> observers = new LinkedList<>();
    private int currentBet = 0;
    private ArrayList<PlayingCards> fullCardDeck = new ArrayList<>();
    private ArrayList<PlayingCards> currentDeck = new ArrayList<>();
    private final PlayingCards coveredCard = new PlayingCards(PlayingCards.Suit.BACK, PlayingCards.Face.NONFACE, 0, RESOURCES_DIR + "card_back.png");

    /**
     * Constructor for CasinoModel
     */
    public CasinoModel() {
        accountMap = accounts.readAccounts();
        currentScene = Scenes.STARTUP;
        this.alertObservers(null);
    }

    /**
     * Getter for the active player account
     * @return the active player account
     */
    public Player getActivePlayer() {
        return activePlayerAccount;
    }

    /**
     * Method to increase players chip amount
     * GUI calls this and this calls player.winChips()
     * @param multiplier the amount you win compared to your bet in the current game
     */
    public void winBet(double multiplier) {
        int amount = (int) (currentBet*multiplier);
        activePlayerAccount.winChips(amount);
        saveAccounts();
        this.alertObservers("YOU WON " + amount + " CHIPS!!");
    }

    /**
     * Method to place bets
     * @param amount number of chips bet
     */
    public void placeBet(int amount) {
        if (activePlayerAccount.getChips() > amount) {
            currentBet = amount;
            activePlayerAccount.betChips(amount);
            saveAccounts();
            this.alertObservers("You have bet " + amount + " chips.");
        } else {
            alertObservers("You ain't that rich buddy. Nice try though.");
        }
    }

    /**
     * Allows new users to sign up. GUI calls this and this uses the AccountData.java functions
     * @param usr username in a String
     * @param pass password in a String
     */
    public void signUp(String usr, String pass) {
        Player newPlayer = new Player(usr, pass);
        if (accountMap.containsValue(newPlayer)) {
            alertObservers("This account already exists, please login.");
        } else if (accountMap.containsKey(usr)) {
            alertObservers("This username is already taken, please try another.");
        } else {
            accounts.saveAccount(newPlayer, true);
            accountMap = accounts.readAccounts();
            login(usr, pass);
        }
    }

    /**
     * Method to return a random card from the current deck
     * @return the file name of the card that was drawn from the deck
     */
    public String hitBlackjack() {
        // TODO make this random instead of hardcoded
        int randomNum = 10; // TODO Set this to something
        String card = currentDeck.get(randomNum).getFileName();
        currentDeck.remove(randomNum);
        return card;
    }

    public String getBackCard() {
        return coveredCard.getFileName();
    }

    /**
     * Method to see if the blackjack game has been won or lost
     * @return a GameResults enum, WON if player won, LOSE if player lost, and NONE if the game is still ongoing
     */
    public GameResults checkBjWin() {
        if (currentBet > 15000) {
            return GameResults.WIN;
        } else if (currentBet > 1000 && currentBet < 15000) {
            return GameResults.LOSE;
        }
        return GameResults.NONE;
    }

    /**
     * Saves accounts to the text file with the updated chip balance.
     */
    public void saveAccounts() {
        int count = 0;
        for (Player acc : accountMap.values()) {
            accounts.saveAccount(acc, count != 0);
            count++;
        }
        alertObservers(null);
    }

    /**
     * Method to update the model and give text through the model
     * @param text the message that is to be displayed
     */
    public void updateModel(String text) {
        alertObservers(text);
    }

    /**
     * Method to create the complete full deck.
     * Used when initializing the card deck.
     * @param fullCardDeck the complete arraylist of playing cards
     */
    public void setFullCardDeck(ArrayList<PlayingCards> fullCardDeck) {
        this.fullCardDeck = fullCardDeck;
    }

    /**
     * Method to add all the lost cards back to the current deck for when resetting game or starting a different game
     */
    public void resetCardDeck() {
        currentDeck = new ArrayList<>(fullCardDeck);
    }

    /**
     * Allows GUI users to log into a previously created account. Checks with accountMap to see if it is a valid login
     * @param usr username in a String
     * @param pass password in a String
     */
    public void login(String usr, String pass) {
        if (accountMap.containsKey(usr) && accountMap.get(usr).getPassword().equals(pass)) {
            activePlayerAccount = accountMap.get(usr);
            currentScene = Scenes.HOME;
            alertObservers("You have now been signed in, " + usr + ".");
        } else {
            alertObservers("The credentials you have inputted are incorrect");
        }
    }

    /**
     * Used to change what scene the view is currently displaying
     * @param newScene the scene that the view is being updated to
     */
    public void setScene(Scenes newScene) {
        currentScene = newScene;
        resetCardDeck();
        alertObservers(null);
    }

    /**
     * The view calls this to add itself as an observer.
     * @param observer the view
     */
    public void addObserver(Observer<CasinoModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * Informs the listed observers that the model has been updated, and gives an updated model to the observers
     * update method.
     * @param data the message the model is giving to the view
     */
    private void alertObservers(String data) {
        for (var observer : observers) {
            observer.update(this, data);
        }
    }
}

