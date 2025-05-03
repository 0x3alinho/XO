package model;

public class GameModel {
    private String[][] board;
    private String currentPlayer;

    public GameModel() {
        board = new String[3][3];
        currentPlayer = "X";
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean makeMove(int row, int col) {
        if (board[row][col] == null) {
            board[row][col] = currentPlayer;
            currentPlayer = currentPlayer.equals("X") ? "O" : "X";
            return true;
        }
        return false;
    }

    public String checkWinner() {
        // check rows, columns, and diagonals
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != null &&
                board[i][0].equals(board[i][1]) &&
                board[i][1].equals(board[i][2])) return board[i][0];

            if (board[0][i] != null &&
                board[0][i].equals(board[1][i]) &&
                board[1][i].equals(board[2][i])) return board[0][i];
        }

        if (board[0][0] != null &&
            board[0][0].equals(board[1][1]) &&
            board[1][1].equals(board[2][2])) return board[0][0];

        if (board[0][2] != null &&
            board[0][2].equals(board[1][1]) &&
            board[1][1].equals(board[2][0])) return board[0][2];

        return null;
    }

    public void reset() {
        board = new String[3][3];
        currentPlayer = "X";
    }

    public String[][] getBoard() {
        return board;
    }
}
