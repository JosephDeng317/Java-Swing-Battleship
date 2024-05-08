/*
Name: Joseph Deng
Date: June 10th, 2022
Program Name: Battleship Game
Purpose: Utilize java swing to create a GUI based battleship game where the player faces an AI
*/

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.Scanner;
import java.awt.event.*;
import java.util.List;

class Main {
  public static boolean gameRunning = false;
  public static boolean leaderboardRunning = false;
  public static boolean postGameRunning = false;
  public static boolean gameWon = false;

  // Main Menu
  public static void mainMenu() {
    JFrame frame = new JFrame("Main Menu");
    JButton game = new JButton("Play Game!");
    JButton leaderboard = new JButton("Leaderboard");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(400, 400);
    frame.setLayout(null);
    frame.setVisible(true);

    // Overrides minimize button
    frame.addWindowListener(new java.awt.event.WindowAdapter() {
      @Override
      public void windowIconified(java.awt.event.WindowEvent windowEvent) {
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
      }
    });

    // button to enter leaderboard
    leaderboard.setBounds(125, 100, 150, 50);
    leaderboard.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        leaderboardRunning = true;
      }
    });

    // button to enter game
    game.setBounds(125, 200, 150, 50);
    game.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        gameRunning = true;
      }
    });
    frame.add(game);
    frame.add(leaderboard);
  }

  // Show leaderboard
  public static void showLeaderboard() {
    JFrame frame = new JFrame("Leaderboard");
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setSize(400, 400);
    frame.setLayout(null);
    frame.setVisible(true);

    // Overides default minimize and close buttons
    frame.addWindowListener(new java.awt.event.WindowAdapter() {
      @Override
      public void windowClosing(java.awt.event.WindowEvent windowEvent) {
        leaderboardRunning = false;
        gameRunning = false;
      }

      @Override
      public void windowIconified(java.awt.event.WindowEvent windowEvent) {
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
      }
    });

    // title text
    JLabel title = new JLabel("Top Scores", SwingConstants.CENTER);
    title.setOpaque(true);
    title.setBounds(50, 0, 300, 30);
    frame.add(title);

    // names and scores
    JLabel[] positions = new JLabel[11];
    JLabel[] names = new JLabel[11];
    JLabel[] scores = new JLabel[11];
    for (int i = 0; i < 11; i++) {
      positions[i] = new JLabel(i + ".");
      positions[i].setBounds(25, 80 + 24 * i, 35, 20);
      positions[i].setOpaque(true);
      frame.add(positions[i]);

      names[i] = new JLabel();
      names[i].setBounds(80, 80 + 24 * i, 200, 20);
      names[i].setOpaque(true);
      frame.add(names[i]);

      scores[i] = new JLabel();
      scores[i].setBounds(300, 80 + 24 * i, 75, 20);
      scores[i].setOpaque(true);
      frame.add(scores[i]);
    }

    // Creates table headers
    positions[0].setText("#");
    names[0].setText("Nickname");
    scores[0].setText("Attacks");

    // Grabs leaderboard data from sql database
    List<List<String>> results = Database.Select();

    // Pushes data to GUI
    int max = 10;
    if (results.size() < 10) {
      max = results.size();
    }
    for (int i = 0; i < max; i++) {
      names[i + 1].setText(results.get(i).get(0));
      scores[i + 1].setText(results.get(i).get(1));
    }
  }

  // Get Nickname After Win
  public static void getNickname(Player player) {

    int movesMade = player.aBoard.movesMade;
    TextReader frame = new TextReader();

    frame.setTitle("You Won!");

    // Overides default minimize and close buttons
    frame.addWindowListener(new java.awt.event.WindowAdapter() {
      @Override
      public void windowClosing(java.awt.event.WindowEvent windowEvent) {
        postGameRunning = false;
        leaderboardRunning = false;
      }

      @Override
      public void windowIconified(java.awt.event.WindowEvent windowEvent) {
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
      }
    });

    // Shows the player's score
    JLabel score = new JLabel("Attacks Made: " + movesMade, SwingConstants.CENTER);
    score.setBounds(0, 20, 400, 30);
    score.setOpaque(true);
    frame.add(score);

    // Prompts player to enter nickname
    JLabel message = new JLabel("Enter a Nickname", SwingConstants.CENTER);
    message.setBounds(0, 50, 400, 20);
    message.setOpaque(true);
    frame.add(message);

    // Waits for user to make valid entry, and then adds it to the database
    while (postGameRunning) {
      System.out.print("");
      if (frame.returnedText != null) {
        // Adds entry to database
        Database.Insert(frame.returnedText, movesMade);
        break;
      }
    }

    // Closes window when finished
    if (postGameRunning) {
      frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
      postGameRunning = false;
      leaderboardRunning = false;
    }
  }

  // Play battleship
  public static void playGame() {

    gameWon = false;
    AI ai = new AI();
    ai.createBoard();
    Player player = new Player();
    player.aBoard.sync(ai, player);
    GUI frame = new GUI(player);

    // Create labels for game instructions
    JLabel[] instructions = new JLabel[11];
    for (int i = 0; i < instructions.length; i++) {
      instructions[i] = new JLabel("", SwingConstants.CENTER);
      instructions[i].setOpaque(true);
      frame.add(instructions[i]);
    }
    instructions[0].setText("Place your ships here");
    instructions[0].setBounds(0, 305, 300, 20);

    instructions[1].setText("Make attacks here");
    instructions[1].setBounds(400, 305, 300, 20);

    instructions[2].setText("(Activates when all ships are placed)");
    instructions[2].setBounds(400, 325, 300, 20);

    instructions[3].setText("1-5 to choose ship");
    instructions[3].setBounds(0, 360, 300, 20);

    instructions[4].setText("Arrow Keys to move ship");
    instructions[4].setBounds(0, 380, 300, 20);

    instructions[5].setText("Q/E to rotate ship");
    instructions[5].setBounds(0, 400, 300, 20);

    instructions[6].setText("Enter to place ship");
    instructions[6].setBounds(0, 420, 300, 20);

    instructions[7].setText("BLUE = MISS");
    instructions[7].setBounds(400, 360, 300, 20);

    instructions[8].setText("ORANGE = HIT");
    instructions[8].setBounds(400, 380, 300, 20);

    instructions[9].setText("Hits Needed: " + player.hitsNeeded);
    instructions[9].setBounds(400, 400, 150, 20);

    instructions[10].setText("Ships Sunk: " + (5 - ai.shipsAlive));
    instructions[10].setBounds(550, 400, 150, 20);

    // display ships
    for (Ship ship : player.ships) {
      frame.add(ship.icon);
    }
    // display defense board
    for (Block[] row : player.dBoard.board) {
      for (Block block : row) {
        frame.add(block.label);
      }
    }
    // display attack board
    for (JButton[] row : player.aBoard.board) {
      for (JButton button : row) {
        frame.add(button);
        button.setEnabled(false);
      }
    }
    // Initializes losing message but does not add it to the frame
    JLabel lost = new JLabel("YOU LOST!!! Close the window to continue", SwingConstants.CENTER);
    lost.setBounds(0, 420, 700, 80);
    lost.setOpaque(true);

    // waits for player to finish placing ships
    while (gameRunning && !player.boardComplete()) {
      System.out.print("");
    }

    // activates attacking board once all ships are placed
    for (JButton[] row : player.aBoard.board) {
      for (JButton button : row) {
        button.setEnabled(true);
      }
    }

    // waits for player to either win or lose
    while (gameRunning && player.shipsAreAlive() && ai.shipsAreAlive()) {
      System.out.print("");
      instructions[9].setText("Hits Needed: " + player.hitsNeeded);
      instructions[10].setText("Ships Sunk: " + (5 - ai.shipsAlive));
    }

    /*
     * If the player wins, the game window will be closed
     * A new window will open showing their score and asking for them to enter a
     * nickname
     */
    if (gameWon) {
      frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
      postGameRunning = true;
      getNickname(player);
    }

    // If the player loses, it will display the losing message and wait for them to
    // close the window
    else {
      frame.add(lost);
      while (gameRunning) {
        System.out.print("");
      }
    }
    gameRunning = false;
  }

  // Main method
  public static void main(String[] args) {

    // Database.Create();

    mainMenu();

    // Program continues to run until X button in the main menu is clicked
    while (true) {
      // waits for user to either open the leaderboard or play the game, only one can
      // be open at a time
      while (!gameRunning && !leaderboardRunning) {
        System.out.print("");
      }

      if (leaderboardRunning) {
        showLeaderboard();
        while (leaderboardRunning) {
          System.out.print("");
        }
      } else if (gameRunning) {
        playGame();
      }
    }
  }
}