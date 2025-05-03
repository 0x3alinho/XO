package viewmodel;

import model.GameModel;

public class GameViewModel {
    private GameModel model;

    public GameViewModel() {
        model = new GameModel();
    }

    public boolean onCellClicked(int row, int col) {
        return model.makeMove(row, col);
    }

    public String getCellValue(int row, int col) {
        return model.getBoard()[row][col];
    }

    public String getCurrentPlayer() {
        return model.getCurrentPlayer();
    }

    public String checkWinner() {
        return model.checkWinner();
    }

    public void resetGame() {
        model.reset();
    }
}
