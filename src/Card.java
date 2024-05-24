import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Card {
    private final int cardNumber;
    private boolean hovered;
    private boolean open;
    private Holder owner;

    private static final Map<Card, Image> imageCache = new HashMap<>();

    public Card(int cardNumber) {
        this.cardNumber = cardNumber;
        this.hovered = false;
        this.open = false;
    }

    public Image loadAsset() {
        if (imageCache.containsKey(this)) return imageCache.get(this);
        String filename = "back.png";
        if (isOpen()) {
            filename = name().toLowerCase().replace(' ', '_') + ".png";
        }
        final File file = Path.of("src", "assets", "cards", filename).toFile();
        Image image = null;
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            throw new RuntimeException("Image file could not be load: " + file.getPath(), e);
        }
        imageCache.put(this, image);
        return image;
    }

    public int rankNumber() {
        return cardNumber % 13 + 1;
    }

    public int suiteNumber() {
        return cardNumber / 13 + 1;
    }

    public String name() {
        final int rank = rankNumber();
        final int suite = suiteNumber();
        final StringBuilder sb = new StringBuilder();

        if (rank == 1) sb.append("Ace");
        else if (rank >= 2 && rank <= 10) sb.append(rank);
        else if (rank == 11) sb.append("Jack");
        else if (rank == 12) sb.append("Queen");
        else if (rank == 13) sb.append("King");
        else throw new IllegalStateException("Rank was expected to be between 1 and 13");

        sb.append(" of ");

        if (suite == 1) sb.append("Clubs");
        else if (suite == 2) sb.append("Diamonds");
        else if (suite == 3) sb.append("Hearts");
        else if (suite == 4) sb.append("Spades");
        else throw new IllegalStateException("Suite was expected to be between 1 and 4");

        return sb.toString();
    }

    public Holder getOwner() {
        return owner;
    }

    public void setOwner(Holder owner) {
        this.owner = owner;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public boolean isHovered() {
        return hovered;
    }

    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
        imageCache.remove(this);
    }

    @Override
    public String toString() {
        return "Card{" +
                "hovered=" + hovered +
                ", cardNumber=" + cardNumber +
                ", open=" + open +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card card)) return false;
        return cardNumber == card.cardNumber && hovered == card.hovered && open == card.open && Objects.equals(owner, card.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardNumber, hovered, open, owner);
    }
}
