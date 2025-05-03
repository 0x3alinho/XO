package view;

import viewmodel.GameViewModel;

import javax.swing.*;
import java.awt.*;

public class GameView extends JFrame {
    private JButton[][] buttons;
    private GameViewModel viewModel;
    private JLabel statusLabel;

    public GameView() {
        viewModel = new GameViewModel();
        buttons = new JButton[3][3];
        statusLabel = new JLabel("Player X turn");

        setTitle("XO Game");
        setSize(300, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel gridPanel = new JPanel(new GridLayout(3, 3));
        Font font = new Font("Arial", Font.BOLD, 40);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                final int row = i;
                final int col = j;
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(font);
                buttons[i][j].addActionListener(e -> {
                    if (viewModel.onCellClicked(row, col)) {
                        buttons[row][col].setText(viewModel.getCellValue(row, col));
                        String winner = viewModel.checkWinner();
                        if (winner != null) {
                            statusLabel.setText("Winner: " + winner);
                            disableAllButtons();
                        } else {
                            statusLabel.setText("Player " + viewModel.getCurrentPlayer() + " turn");
                        }
                    }
                });
                gridPanel.add(buttons[i][j]);
            }
        }

        JButton resetBtn = new JButton("Reset");
        resetBtn.addActionListener(e -> {
            viewModel.resetGame();
            for (int i = 0; i < 3; i++)
                for (int j = 0; j < 3; j++)
                    buttons[i][j].setText("");
            statusLabel.setText("Player X turn");
        });

        add(statusLabel, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);
        add(resetBtn, BorderLayout.SOUTH);
    }

    private void disableAllButtons() {
        for (JButton[] row : buttons)
            for (JButton btn : row)
                btn.setEnabled(false);
    }
}
