import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.*;

public class Ship {
  boolean isAlive = true;
  int length;
  /*
  0 = UP
  1 = RIGHT
  2 = DOWN
  3 = LEFT
  */
  int orientation;
  boolean isPlaced = false;
  Coordinate coords = new Coordinate(0, 0);
  String name = "Wildcard";
  JLabel icon = new JLabel();
  int width = 24;
  int gap = 6;
  Block[] shipBlocks;
  
  // Constructor 
  public Ship(int shipLength, int xCoord, int yCoord, int shipOrientation) {
    length = shipLength;
    shipBlocks = new Block[length];
    orientation = shipOrientation;
    coords.x = xCoord;
    coords.y = yCoord;

    // Initializes icon that will be displayed on the GUI
    icon.setBackground(Color.red);
    icon.setBounds(0, 0, width, (length * width) + (length - 1) * gap);
    icon.setOpaque(true);
    icon.setLocation(coords.x * (width + gap), (coords.y - length + 1) * (width + gap));
    icon.setHorizontalAlignment(SwingConstants.CENTER);
    icon.setVerticalAlignment(SwingConstants.CENTER);
    
    // Initializes name of ship based on length
    if (length == 2) {
      name = "Destroyer";
    }
    else if (length == 3) {
      name = "Submarine";
    }
    else if (length == 4) {
      name = "Battleship";
    }
    else if (length == 5) {
      name = "Carrier";
    }
  }


/*
method returns true if ship is within the board boundaries,
returns false if the ship is not
*/ 
  public boolean isValid(int xCoord, int yCoord, int shipOrientation) {
    // Check to make sure origin is located within the board
    if (xCoord < 0 || yCoord < 0 || xCoord > Board.size - 1 || yCoord > Board.size - 1) {
      return false;
    }
    if (shipOrientation == 0) {
      if (yCoord - length + 1 >= 0) {
        return true;
      }
    }
      
    else if (shipOrientation == 1) {
      if (xCoord + length <= Board.size) {
        return true;
      }
    }
    else if (shipOrientation == 2) {
      if (yCoord + length <= Board.size) {
        return true;
      }
    }
    else if (shipOrientation == 3) {
      if (xCoord - length + 1 >= 0) {
        return true;
      }
    }
    return false;
  }

// updates the ships position on the GUI
  public void updateIcon() {
    if (orientation == 0) {
      icon.setBounds(coords.x * (width + gap), (coords.y - length + 1) * (width + gap), width, (length * width) + (length - 1) * gap);
    }
    else if (orientation == 1) {
      icon.setBounds(coords.x * (width + gap), coords.y * (width + gap), (length * width) + (length - 1) * gap, width);
    }
    else if (orientation == 2) {
      icon.setBounds(coords.x * (width + gap), coords.y * (width + gap), width, (length * width) + (length - 1) * gap);
    }
    else if (orientation == 3) {
      icon.setBounds((coords.x - length + 1) * (width + gap), coords.y * (width + gap), (length * width) + (length - 1) * gap, width);
    }
  }

  
// move ship functions
  public void moveY(int amount) {
    if (isValid(coords.x, coords.y + amount, orientation)) {
      coords.y += amount;
      updateIcon();
    }
  }
  public void moveX(int amount) {
    if (isValid(coords.x + amount, coords.y, orientation)) {
      coords.x += amount;
      updateIcon();
    }
  }
  public void rotate(int direction) {
    if (isValid(coords.x, coords.y, (orientation + direction + 4) % 4)) {
      orientation = (orientation + direction + 4) % 4;
      updateIcon();
    }
    else {
      System.out.println("cant rotate");  
    }
  }


// places ship down on the board, updates occupied blocks within the board object
  public boolean placeShip(defenseBoard board) {
    Block block;
    
    // gets rid of number on ship once placed
    icon.setText("");

    // figures out which blocks must be changed
    for (int i = 0; i < length; i++) {
      if (orientation == 0) {
        block = board.board[coords.x][coords.y - i];
        if (!block.hasShip) {
          shipBlocks[i] = block;
        }
        else {
          return false;
        }
      }
      else if (orientation == 1) {
        block = board.board[coords.x + i][coords.y];
        if (!block.hasShip) {
          shipBlocks[i] = block;
        }
        else {
          return false;
        }
      }
      else if (orientation == 2) {
        block = board.board[coords.x][coords.y + i];
        if (!block.hasShip) {
          shipBlocks[i] = block;
        }
        else {
          return false;
        }
      }
      else if (orientation == 3) {
        block = board.board[coords.x - i][coords.y];
        if (!block.hasShip) {
          shipBlocks[i] = block;
        }
        else {
          return false;
        }
      }
    }

    // updates the status of the blocks
    for (Block blocksToChange : shipBlocks) {
      blocksToChange.changeStatus();
    }
    isPlaced = true;
    icon.setBackground(Color.black);
    return true;
  }
}