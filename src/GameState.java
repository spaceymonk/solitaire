import java.awt.Point;
import java.util.ArrayList;

public class GameState {
    public static final int WIDTH = 75;
    public static final int HEIGHT = 110;

    public final ArrayList<Holder> holders = new ArrayList<>();

    public final Holder[] playHolders = new Holder[7];
    public final Holder[] collectHolders = new Holder[4];
    public final Holder closedDeck = new Holder(new Point((20 + (10 + WIDTH) * (playHolders.length - 1)), 50));
    public final Holder openedDeck = new Holder(new Point((20 + (10 + WIDTH) * (playHolders.length - 2)), 50), new Point(-WIDTH / 3, 0));

    public GameState() {
        for (int i = 0; i < 52; i++) closedDeck.place(new Card(i));
        closedDeck.shuffle();
        holders.add(closedDeck);
        holders.add(openedDeck);
        openedDeck.setHidden(true);

        for (int i = 0; i < collectHolders.length; i++) {
            collectHolders[i] = new Holder(new Point(20 + (10 + WIDTH) * i, 50));
            holders.add(collectHolders[i]);
        }
        for (int i = 0; i < playHolders.length; i++) {
            playHolders[i] = new Holder(new Point(20 + (10 + WIDTH) * i, HEIGHT + 100), new Point(0, HEIGHT / 3));
            playHolders[i].place(closedDeck.take(i + 1));
            playHolders[i].getCards().getLast().setOpen(true);
            holders.add(playHolders[i]);
        }

    }
}
