// Board class with subclases defenseBoard and attackBoard, each player will have one of each

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;  

public class Board {
  static final int size = 10;
  Block[][] board = new Block[size][size];
  
  // Constructor
  public Board() {
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        board[i][j] = new Block(false, i, j);
      }
    }
  }

  // changes status of block in specified coordinate
  public void changeBlockStatus(int x, int y) {
    board[x][y].changeStatus();
  }
}

class attackBoard extends Board{
  int movesMade = 0;
  JButton[][] board = new JButton[size][size];
  boolean[][] trackerBoard = new boolean[size][size];

  // constructor method creates the buttons to be displayed
  public attackBoard() {
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        board[i][j] = new JButton();
        board[i][j].setBounds(i * 30 + 400, j * 30, 24, 24); 
      }
    }
    for (boolean[] row : trackerBoard) {
      Arrays.fill(row, false);
    }
  }

  
/*
syncs up attack board with the enemy's defense board so that the 
buttons will change to their respective colors when pressed, depending on 
if the block is occupied or not
*/
  public void sync(AI enemy, Player player) {
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        JButton button = board[i][j];

        // initializes button that "contains a ship"
        if (enemy.dBoard.board[j][i].hasShip) {
          Block block = enemy.dBoard.board[j][i];
          button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              button.setBackground(Color.orange);
              enemy.makeMove(player.dBoard);
              button.setEnabled(false);
              block.hasShip = false;
              movesMade += 1;
              player.hitsNeeded -= 1;
              
              if (!enemy.shipsAreAlive()) {
                Main.gameWon = true;
              }
            }
          });
        }

        // initializes button that "is empty"
        else {
          button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              button.setBackground(Color.blue);
              enemy.makeMove(player.dBoard);
              button.setEnabled(false);
              movesMade += 1;
            }
          });
        }
      }
    }
  }
}

// subclass currently does not contain any extra features
class defenseBoard extends Board {
  public defenseBoard() {
    super();
  }
}