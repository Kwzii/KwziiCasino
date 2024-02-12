import java.util.Objects;

/**
 * Player saves the data of the current gambler.
 * May at some point be implemented into a login/sign up system where passwords and other things are saved
 * By Travis Brown (Kwzii)
 */

public class Player {
    private final String username;

    private int chips;


    public Player(String username) {
        this.username = username;
        this.chips = 150;
    }

    public void setChips(int inc) {
        chips += inc;
    }

    public int getChips() {
        return chips;
    }

    @Override
    public String toString() {
        return "Player{" +
                "username='" + username + '\'' +
                ", chips=" + chips +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return chips == player.chips && Objects.equals(username, player.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, chips);
    }
}