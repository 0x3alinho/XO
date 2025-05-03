package multiplayer;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class Client extends JFrame {
    private JButton[][] buttons = new JButton[3][3];
    private String mySymbol;
    private String currentTurn = "X";
    private DataOutputStream out;
    private DataInputStream in;

    public Client(String serverAddress) {
        try {
            Socket socket = new Socket(serverAddress, 12345);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            mySymbol = in.readUTF();
            setTitle("You are Player " + mySymbol);

            setSize(300, 350);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLayout(new GridLayout(3, 3));
            Font font = new Font("Arial", Font.BOLD, 40);

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    final int row = i;
                    final int col = j;
                    buttons[i][j] = new JButton("");
                    buttons[i][j].setFont(font);
                    buttons[i][j].addActionListener(e -> {
                        if (buttons[row][col].getText().equals("") && mySymbol.equals(currentTurn)) {
                            buttons[row][col].setText(mySymbol);
                            try {
                                out.writeUTF(row + "," + col);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            currentTurn = mySymbol.equals("X") ? "O" : "X";
                            if (checkWinner(mySymbol)) {
                                JOptionPane.showMessageDialog(this, "You win!");
                                resetGame();
                            } else if (checkDraw()) {
                                JOptionPane.showMessageDialog(this, "It's a draw!");
                                resetGame();
                            }
                        }
                    });
                    add(buttons[i][j]);
                }
            }

            new Thread(() -> {
                try {
                    while (true) {
                        String msg = in.readUTF();
                        String[] parts = msg.split(",");
                        int row = Integer.parseInt(parts[0]);
                        int col = Integer.parseInt(parts[1]);
                        String opponentSymbol = mySymbol.equals("X") ? "O" : "X";
                        buttons[row][col].setText(opponentSymbol);
                        currentTurn = mySymbol;
                        if (checkWinner(opponentSymbol)) {
                            JOptionPane.showMessageDialog(this, "You lose!");
                            resetGame();
                        } else if (checkDraw()) {
                            JOptionPane.showMessageDialog(this, "It's a draw!");
                            resetGame();
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkWinner(String symbol) {
        // Check rows and columns
        for (int i = 0; i < 3; i++) {
            if (buttons[i][0].getText().equals(symbol) &&
                buttons[i][1].getText().equals(symbol) &&
                buttons[i][2].getText().equals(symbol)) return true;

            if (buttons[0][i].getText().equals(symbol) &&
                buttons[1][i].getText().equals(symbol) &&
                buttons[2][i].getText().equals(symbol)) return true;
        }

        // Check diagonals
        if (buttons[0][0].getText().equals(symbol) &&
            buttons[1][1].getText().equals(symbol) &&
            buttons[2][2].getText().equals(symbol)) return true;

        if (buttons[0][2].getText().equals(symbol) &&
            buttons[1][1].getText().equals(symbol) &&
            buttons[2][0].getText().equals(symbol)) return true;

        return false;
    }

    private boolean checkDraw() {
        // Check if there are any empty cells
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().equals("")) {
                    return false; // There are still empty cells, not a draw
                }
            }
        }
        return true; // All cells are filled, so it's a draw
    }

    private void resetGame() {
        // Reset the board
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
        currentTurn = "X"; // Reset to player X
    }

    public static void main(String[] args) {
        String serverIP = JOptionPane.showInputDialog("Enter Server IP:");
        Client client = new Client(serverIP);
        client.setVisible(true);
    }
}
