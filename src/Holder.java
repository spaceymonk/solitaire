import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Holder {
    private final LinkedList<Card> cards = new LinkedList<>();
    private Point cardDisplacement;
    private Point position;
    private boolean hidden;

    public Holder(Point position) {
        this.position = position;
        this.cardDisplacement = new Point();
    }

    public Holder(Point position, Point cardDisplacement) {
        this.cardDisplacement = cardDisplacement;
        this.position = position;
    }

    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }

    public void place(Card card) {
        cards.add(card);
        card.setOwner(this);
    }

    public void place(List<Card> cards) {
        cards.forEach(this::place);
    }

    public Card take() {
        Card card = cards.pollLast();
        if (card == null) throw new IllegalStateException("No more cards to take, in " + this);
        card.setOwner(null);
        return card;
    }

    public List<Card> take(int n) {
        List<Card> taken = new ArrayList<>(n);
        for (int i = 0; i < n; i++) taken.add(take());
        return taken;
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public Point getCardDisplacement() {
        return cardDisplacement;
    }

    public void setCardDisplacement(Point cardDisplacement) {
        this.cardDisplacement = cardDisplacement;
    }

    public Point getCardPosition(Card card) {
        int index = cards.indexOf(card);
        if (index < 0) throw new RuntimeException("Given " + card + " is not in " + this);
        return new Point(position.x + index * cardDisplacement.x, position.y + index * cardDisplacement.y);
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }
}
