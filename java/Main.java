import view.GameView;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new GameView().setVisible(true);
        });
    }
}
