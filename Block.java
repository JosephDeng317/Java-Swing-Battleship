// Block class, defenseBoard class is composed of a 2d array of block objects

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.*;

public class Block {
  boolean hasShip;
  JLabel label;
  Coordinate coords = new Coordinate(-1, -1);

  // constructor
  public Block(boolean occupied, int xValue, int yValue) {
    hasShip = occupied;
    coords.y = yValue;
    coords.x = xValue;
    
    // creates label to be displayed on GUI
    label = new JLabel();
    if (occupied) {
      label.setBackground(Color.red);
    }
    else {
      label.setBackground(Color.lightGray);
    }
    label.setBounds(xValue * 30, yValue * 30, 24, 24);
    label.setOpaque(true);
  }

  // changes status of block
  public void changeStatus() {
    hasShip = !hasShip;
  }
  
}