import java.lang.Math;
import java.awt.*;

public class Player {
  int hitsNeeded = 16;
  int shipsAlive = 5;
  attackBoard aBoard = new attackBoard();
  defenseBoard dBoard = new defenseBoard();
  Ship[] ships = new Ship[5];

  // Constructor
  public Player() {
    ships[0] = new Ship(2, 0, 1, 0);
    ships[1] = new Ship(2, 2, 1, 0);
    ships[2] = new Ship(3, 4, 2, 0);
    ships[3] = new Ship(4, 6, 3, 0);
    ships[4] = new Ship(5, 8, 4, 0);
    for (int i = 1; i < ships.length + 1; i++) {
      ships[i-1].icon.setText("" + i);
    }
  }

  // checks if all ships are placed
  public boolean boardComplete() {
    for (Ship ship : ships) {
      if (!ship.isPlaced) {
        return false;
      }
    }
    return true;
  }
  
  // checks to see if the player still has ships afloat
  public boolean shipsAreAlive() {
    for (Ship ship : ships) {
      
      // updates the status of each ship
      if (ship.isAlive) {
        
        // loop is broken if any block is still "alive"
        found: {
          for (Block block : ship.shipBlocks) {
          if (block.hasShip) {
            break found;
          }
        }
          ship.isAlive = false;
          ship.icon.setBackground(Color.orange);
          shipsAlive -= 1;
        }
      }
    }

    // checks each ship's status, only returns true if all ships are dead
    for (Ship ship : ships) {
      if (ship.isAlive) {
        return true;
      }
    }
    return false;
  }
}

// subclass of player for the AI
class AI extends Player {

  // Generates random board layout 
  public void createBoard() {
    for (Ship ship : ships) {
      while (true) {
        ship.coords.x = (int) (Math.random() * Board.size);
        ship.coords.y = (int) (Math.random() * Board.size);
        ship.orientation = (int) (Math.random() * 4);
        if (ship.isValid(ship.coords.x, ship.coords.y, ship.orientation) && ship.placeShip(dBoard)) {
          break;
        }
      }
    }
    /*
    Displays completed board in console, uncomment to activate

    for (Block[] blockRow : dBoard.board) {
      for (Block block : blockRow) {
        if (block.hasShip) {
          System.out.print("X ");
        }
        else {System.out.print("_ ");}
      }
      System.out.println();
    }
    */
  }

  // Picks random open square to attack
  public Coordinate pickRandom() {
    int seed = (int) (Math.random() * (Board.size * Board.size));
    while (true) {
      for (int i = 0; i < Board.size; i++) {
        for (int j = 0; j < Board.size; j++) {
          if (!aBoard.trackerBoard[i][j]) {
            seed -= 1;
          if (seed == -1) {
            return new Coordinate(i, j);
          }
          }
        }
      }
    }
  }

  // Updates various things relating to the move made
  public void confirmMove(defenseBoard enemyBoard, int x, int y) {
    aBoard.trackerBoard[x][y] = true;
    Block block = enemyBoard.board[x][y];
    if (block.hasShip) {
      // hit ship
      block.label.setBackground(Color.orange);
    }
    else {
      // miss ship
      block.label.setBackground(Color.blue);
    }
    block.hasShip = false;
  }

  // Makes move
  public void makeMove(defenseBoard enemyBoard) {
    Coordinate coord = pickRandom();
    confirmMove(enemyBoard, coord.x, coord.y);
  }
}