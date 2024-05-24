import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AppPanel extends JPanel {
    public static final int ARC = 10;
    public static final Color HOVER_COLOR = new Color(0f, 0f, 0f, 0.1f);
    public static final Color BG_COLOR = new Color(50, 120, 70);

    private final GameState gameState;

    public AppPanel(GameState gameState) {
        super();
        this.gameState = gameState;

        setPreferredSize(new Dimension(635, 800));
        final MouseAdapter mouseAdapter = new AppMouseHandler(this);
        addMouseMotionListener(mouseAdapter);
        addMouseListener(mouseAdapter);
        addMouseWheelListener(mouseAdapter);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics.create();
        RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.addRenderingHints(renderingHints);
        graphics2D.setColor(BG_COLOR);
        graphics2D.fillRect(0, 0, getWidth(), getHeight());

        graphics2D.setFont(new Font(Font.SERIF, Font.BOLD, 24));
        FontMetrics fontMetrics = graphics2D.getFontMetrics();
        final int h = fontMetrics.getDescent();
        for (Holder holder : gameState.holders) {
            paintHolder(holder, graphics2D);
            if (!holder.isHidden()) {
                String s = String.valueOf(holder.getCards().size());
                int w = fontMetrics.stringWidth(s);
                graphics2D.setColor(Color.BLACK);
                graphics2D.drawString(s, holder.getPosition().x + (GameState.WIDTH - w) / 2, holder.getPosition().y - h);
            }
        }

        graphics.dispose();
    }

    private void paintHolder(Holder holder, Graphics2D graphics2D) {
        if (!holder.isHidden()) {
            Point holderPosition = holder.getPosition();
            graphics2D.setColor(Color.GRAY);
            graphics2D.drawRoundRect(holderPosition.x, holderPosition.y, GameState.WIDTH, GameState.HEIGHT, ARC, ARC);
        }
        for (Card card : holder.getCards()) {
            paintCard(card, holder.getCardPosition(card), graphics2D);
        }
    }

    private void paintCard(Card card, Point position, Graphics2D graphics2D) {
        graphics2D.setColor(Color.WHITE);
        graphics2D.fillRoundRect(position.x, position.y, GameState.WIDTH, GameState.HEIGHT, ARC, ARC);
        Image image = card.loadAsset().getScaledInstance(GameState.WIDTH - 6, GameState.HEIGHT - 6, Image.SCALE_SMOOTH);
        graphics2D.drawImage(image, position.x + 3, position.y + 3, this);
        graphics2D.setColor(Color.LIGHT_GRAY);
        graphics2D.drawRoundRect(position.x, position.y, GameState.WIDTH, GameState.HEIGHT, ARC, ARC);

        if (card.isHovered()) {
            graphics2D.setColor(HOVER_COLOR);
            graphics2D.fillRoundRect(position.x, position.y, GameState.WIDTH, GameState.HEIGHT, ARC, ARC);
        }
    }

    private class AppMouseHandler extends MouseAdapter {
        public static final Cursor HOVER_CURSOR = new Cursor(Cursor.HAND_CURSOR);

        private final AppPanel appPanel;
        private Card lastHoveredCard = null;

        public AppMouseHandler(AppPanel appPanel) {
            this.appPanel = appPanel;
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            Card hoveredCard = getHoveredCard(e.getPoint());
            if (hoveredCard == null) {
                if (lastHoveredCard != null) {
                    lastHoveredCard.setHovered(false);
                    appPanel.repaint();
                    appPanel.setCursor(Cursor.getDefaultCursor());
                }
            } else if (!hoveredCard.equals(lastHoveredCard)) {
                if (lastHoveredCard != null) {
                    lastHoveredCard.setHovered(false);
                }
                hoveredCard.setHovered(true);
                lastHoveredCard = hoveredCard;
                appPanel.repaint();
                appPanel.setCursor(HOVER_CURSOR);
            }
        }

        private Card getHoveredCard(Point point) {
            for (Holder holder : gameState.holders) {
                for (Card card : holder.getCards().reversed()) {
                    Point cardPosition = card.getOwner().getCardPosition(card);
                    if (point.x >= cardPosition.x && point.x <= cardPosition.x + GameState.WIDTH &&
                            point.y >= cardPosition.y && point.y <= cardPosition.y + GameState.HEIGHT) {
                        return card;
                    }
                }
            }
            return null;
        }
    }

}
