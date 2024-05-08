// GUI for main game window

import java.awt.event.*;
import javax.swing.JLabel;
import javax.swing.JFrame;
import java.awt.color.*;
import javax.swing.*;
import java.awt.*;


public class GUI extends JFrame implements KeyListener {
  Player player;
  int shipChoice = 0;

  // constructor, takes parameter of player to link the user with the GUI
  GUI(Player user){
    player = user;
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.setSize(700, 500);
    this.setLayout(null);
    this.addKeyListener(this);
    this.setVisible(true);
    this.setTitle("Battleship");

    // Overrides minimize and close buttons
    this.addWindowListener(new java.awt.event.WindowAdapter() {
      @Override
      public void windowClosing(java.awt.event.WindowEvent windowEvent) {
        Main.gameRunning = false;
        Main.leaderboardRunning = false;
      }

      @Override
      public void windowIconified(java.awt.event.WindowEvent windowEvent) {
        Main.gameRunning = false;
        Main.leaderboardRunning = false;
       }
    });
  }


  // Key Listener Functions
  @Override
	public void keyTyped(KeyEvent e) {
		//keyTyped = Invoked when a key is typed. Uses KeyChar, char output
	}
		

	@Override
	public void keyPressed(KeyEvent e) {
    // Keys only work if player has not placed all the ships
    if (!player.boardComplete()) {
      if (e.getKeyCode() > 48 && e.getKeyCode() < 54) {
        shipChoice = e.getKeyCode() - 49;
      }
      if (!player.ships[shipChoice].isPlaced) {
        if (shipChoice >= 0) {
          Ship ship = player.ships[shipChoice];
          if (e.getKeyCode() == 37) {
            ship.moveX(-1);
          }
          if (e.getKeyCode() == 38) {
            ship.moveY(-1);
          }
          if (e.getKeyCode() == 39) {
            ship.moveX(1);
          }
          if (e.getKeyCode() == 40) {
            ship.moveY(1);
          }
          if (e.getKeyCode() == 69) {
            ship.rotate(1);
          }
          if (e.getKeyCode() == 81) {
            ship.rotate(-1);
          }
          if (e.getKeyCode() == 10) {
            ship.placeShip(player.dBoard);
            for (int i = 0; i < player.ships.length; i++) {
              if (!player.ships[i].isPlaced) {
                shipChoice = i;
                break;
              }
            }
          }
        }
      } 
    }
	}

	@Override
	public void keyReleased(KeyEvent e) {
		//keyReleased = called whenever a button is released
	}
}