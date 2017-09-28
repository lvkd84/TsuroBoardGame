/**
 * The Tsuro Game
 * @author Kha Dinh Luong
 */

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;

public class Tsuro implements ActionListener {
  
  /**The board where players buttons on*/
  private JPanel board;
  
  /**The panel containing player one's buttons*/
  private JPanel handOne;
  
  /**The panel containing player two's buttons*/
  private JPanel handTwo;
  
  /**The button that a player is choosing*/
  private TsuroButton highlightedButton = null;
  
  /**Array to store buttons on the main board*/
  private TsuroButton[][] boardButton;
  
  /**Array to store buttons on the first player's hand*/
  private TsuroButton[] handOneButton;
  
  /**Array to store buttons on the second player's hand*/
  private TsuroButton[] handTwoButton;
  
  /**An int that store the number of turns played*/
  private int turn = 1;
  
  /**An int the store the number of buttons each player can carry at once*/
  private int handsize;
  
  /**The number of row of the board*/
  private int row;
  
  /**The number of column of the board*/
  private int column;
  
  /**Store the location of the clicked button if that button is a board's button*/
    int[] clickedBoardButtonLocation = new int[2];
  
  /**An array that store the location of the first stone 
    *Current row, current column, current button's location, next move's row, next move's column, next move's starting button's position  
    */
  private int[] stoneOneLocation = new int[6];
  
  /**An array that store the location of the second stone 
    *Current row, current column, current button's location, next move's row, next move's column, next move's starting button's position  
    */
  private int[] stoneTwoLocation = new int[6];
  
  /**Constructor that takes no input. Create a 6x6 mainboard. Two players. Each player has three buttons*/
  public Tsuro() {
    this(6,6,3);
  }
  
  /**Constructor that takes two int as input. Create a row x column mainboard. Two players. Each player has three buttons in hand
    *@param row number of rows
    *@param column number of columns 
    */
  public Tsuro(int row, int column) {
    this(row, column, 3); 
  }
  /**Constructor that takes three int as input. Create a row x column main board. Two players. Each player has handsize buttons at once 
   *@param row number of rows
   *@param column number of columns 
   *@param handsize number of buttons each player can hold
   */
  public Tsuro(int row, int column, int handsize) {
    try {
      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    }
    catch (Exception e) {
    }
    this.row = row;
    this.column = column;
    this.handsize = handsize;
    board = new JPanel(new GridLayout(row,column));
    handOne = new JPanel(new GridLayout(1,handsize));
    handTwo = new JPanel(new GridLayout(1,handsize));
    boardButton = new TsuroButton[row][column];
    for (int i = 0; i < row; i++) 
      for (int j = 0; j < column; j++) {
      boardButton[i][j] = new TsuroButton();
      boardButton[i][j].addActionListener(this);
      boardButton[i][j].setConnections(null);
      board.add(boardButton[i][j]);
    }
    handOneButton = new TsuroButton[handsize];
    for (int i = 0; i < handsize; i++) {
      handOneButton[i] = new TsuroButton();
      handOneButton[i].addActionListener(this);
      handOneButton[i].setConnections(handOneButton[i].makeRandomConnectArray());
      handOneButton[i].addStone(Color.RED,6);
      handOne.add(handOneButton[i]);
    }
    handTwoButton = new TsuroButton[handsize];
    for (int i = 0; i < handsize; i++) {
      handTwoButton[i] = new TsuroButton();
      handTwoButton[i].addActionListener(this);
      handTwoButton[i].setConnections(handTwoButton[i].makeRandomConnectArray());
      handTwoButton[i].addStone(Color.BLUE,2);
      handTwo.add(handTwoButton[i]);
    }
    JFrame boardFrame = new JFrame("Tsuro");
    Container boardContainer = boardFrame.getContentPane();
    boardContainer.add(board, "Center");
    JFrame handOneFrame = new JFrame("Player One");
    Container handOneContainer = handOneFrame.getContentPane();
    handOneContainer.add(handOne, "Center");
    JFrame handTwoFrame = new JFrame("Player Two");
    Container handTwoContainer = handTwoFrame.getContentPane();
    handTwoContainer.add(handTwo, "Center");
    boardFrame.setVisible(true);
    boardFrame.setSize(100 * column,100 * row);
    boardFrame.setLocation(100 * (handsize + 1),0);
    handOneFrame.setVisible(true);
    handOneFrame.setSize(100 * handsize,100);
    handTwoFrame.setVisible(true);
    handTwoFrame.setSize(100 * handsize,100);
    handTwoFrame.setLocation(0,150);
  }
  
  /*GETTER METHODS 
   *THESE METHODS ARE FOR TESTING PURPOSE ONLY  
   */
    
  /**Get number of rows on the board*/
  public int getRow() {
    return this.row; 
  }
  
  /**Get number of columns on the board*/
  public int getColumn() {
    return this.column; 
  }
  
  /**Get the number of button each player has*/
  public int getHandsize() {
    return this.handsize; 
  }
  
  /**Get the specific player's button
    *@param player 1: Player one. 2: Player two.
    */
  public TsuroButton[] getHandButton(int player) {
    if (player == 1) 
      return this.handOneButton;
    if (player == 2)
      return this.handTwoButton;
    return null;
  }
  
  /**Get the specific board button 
    *@param row on which row is the board button
    *@param column on which column is the board button
    */ 
  public TsuroButton getBoardButton(int row, int column) {
     return boardButton[row - 1][column - 1];
  }
  
  /**Get the highlightedButton*/
  public TsuroButton getHighlightedButton() {
    return this.highlightedButton; 
  }
  
  /**Get the stoneLocation
    *@param whichStone 1. return stoneOneLocation, 2. return stoneTwoLocation  
    */
  public int[] getStoneLocation(int whichStone) {
    if (whichStone == 1)
      return this.stoneOneLocation;
    if (whichStone == 2)
      return this.stoneTwoLocation;
    return null;
  }
  
  /*HELPER METHODS  
   *I made the helper methods public for testing although I think they should be private*/
  
  /**A helper method called in rotationPosition to help rotating the button
    *@param value the position of the stone on the current button 
    *@return the position of the same stone on the adjacent button
    */
  public static int rotationHelper(int position) {
    int result = -1;
    switch(position) {
      case 0: result = 2;
              break;
      case 1: result = 3;
              break;
      case 2: result = 5;
              break;
      case 3: result = 4;
              break;
      case 4: result = 6;
              break;
      case 5: result = 7;
              break;
      case 6: result = 1;
              break;
      case 7: result = 0;
      default: break;
    }
    return result;
  }
  
  /**A method that rotate the buttons in players' hands 
   *@param currentConnections the array that store the connections on the button before rotating 90 degree  
   *@return an array that stores the new connections on the button after rotating 90 degree
   */
  public int[] rotationPosition(int[] currentConnections) {     
    int[] resultConnections = new int[8];
    resultConnections[0] = rotationHelper(currentConnections[7]);
    resultConnections[1] = rotationHelper(currentConnections[6]);
    resultConnections[2] = rotationHelper(currentConnections[0]);
    resultConnections[3] = rotationHelper(currentConnections[1]);
    resultConnections[4] = rotationHelper(currentConnections[3]);
    resultConnections[5] = rotationHelper(currentConnections[2]);
    resultConnections[6] = rotationHelper(currentConnections[4]);
    resultConnections[7] = rotationHelper(currentConnections[5]);
    return resultConnections;
  }
  
  /**Highlight the chosen hand button 
    *@param b the clicked hand button  
    */
  public void highlight(TsuroButton b) {
    if (highlightedButton == null) {
      b.setBackground(Color.YELLOW);
      highlightedButton = b;
    } else if (highlightedButton != b) {
      highlightedButton.setBackground(Color.WHITE);
      b.setBackground(Color.YELLOW);
      highlightedButton = b;
    } else if (highlightedButton == b) {
      b.setConnections(rotationPosition(b.getConnections()));
    }
  }
  
  /**Set the initial value for the array that store the location of the specific on the board 
    *@param b the clicked board button that is the first move of a player  
    *@param turn the current turn 
    *@param clickedBoardButtonLocation the location of the clicked board button 
    */
  /*This method does not need parameters because it can refer directly to the class fields. However, parameters make it easier to test with JUnits*/
  public int[] initialStoneLocation(TsuroButton b, int turn, int[] clickedBoardButtonLocation) {
    int[] result = new int[6];
    if (turn == 1) {
      b.addStone(Color.RED, b.getConnections()[6]);
      result[0] = clickedBoardButtonLocation[0];
      result[1] = 1;
      result[2] = b.getConnections()[6];
    }
    if (turn == 2) {
      b.addStone(Color.BLUE, b.getConnections()[2]);
      result[0] = clickedBoardButtonLocation[0];
      result[1] = column;
      result[2] = b.getConnections()[2];
    }
    switch(result[2]) {
      case 0: result[3] = clickedBoardButtonLocation[0] - 1;
              result[4] = clickedBoardButtonLocation[1];
              result[5] = 4;
              break;
      case 1: result[3] = clickedBoardButtonLocation[0] - 1;
              result[4] = clickedBoardButtonLocation[1];
              result[5] = 5;
              break;
      case 2: result[3] = clickedBoardButtonLocation[0];
              result[4] = clickedBoardButtonLocation[1] + 1;
              result[5] = 6;
              break;
      case 3: result[3] = clickedBoardButtonLocation[0];
              result[4] = clickedBoardButtonLocation[1] + 1;
              result[5] = 7;
              break;
      case 4: result[3] = clickedBoardButtonLocation[0] + 1;
              result[4] = clickedBoardButtonLocation[1];
              result[5] = 0;
              break;
      case 5: result[3] = clickedBoardButtonLocation[0] + 1;
              result[4] = clickedBoardButtonLocation[1];
              result[5] = 1;
              break;
      case 6: result[3] = clickedBoardButtonLocation[0];
              result[4] = clickedBoardButtonLocation[1] - 1;
              result[5] = 2;
              break;
      case 7: result[3] = clickedBoardButtonLocation[0];
              result[4] = clickedBoardButtonLocation[1] - 1;
              result[5] = 3;
      default: break;
    }
    return result;
  }
  
  /**Set the new value to the arrays that store the location of stones after each move  
   *@param stoneLocation the array that store the location of a specific stone  
   */
  public void stoneLocation(int[] stoneLocation, TsuroButton[][] boardButton) {
    stoneLocation[0] = stoneLocation[3];
    stoneLocation[1] = stoneLocation[4];
    stoneLocation[2] = boardButton[stoneLocation[3] - 1][stoneLocation[4] - 1].getConnections()[stoneLocation[5]];
    switch(boardButton[stoneLocation[3] - 1][stoneLocation[4] - 1].getConnections()[stoneLocation[5]]) {
    case 0: stoneLocation[3]--;
            stoneLocation[5] = 4;
            break;
    case 1: stoneLocation[3]--;
            stoneLocation[5] = 5;
            break;
    case 2: stoneLocation[4]++;
            stoneLocation[5] = 6;
            break;
    case 3: stoneLocation[4]++;
            stoneLocation[5] = 7;
            break;
    case 4: stoneLocation[3]++;
            stoneLocation[5] = 0;
            break;
    case 5: stoneLocation[3]++;
            stoneLocation[5] = 1;
            break;
    case 6: stoneLocation[4]--;
            stoneLocation[5] = 2;
            break;
    case 7: stoneLocation[4]--;
            stoneLocation[5] = 3;
            break;
    default: break;
    }
  }
  
  /**Check if the clicked board button is the valid button to place a hand button on 
   *@param highlightedButton the class field highlightedButton 
   *@param turn the current turn of the game
   *@param clickedBoardButtonLocation the location of the clicked board button
   */
  /*This method does not need parameters because it can refer directly to the class fields. However, parameters make it easier to test with JUnits*/
  public boolean validBoardMove(TsuroButton highlightedButton, int turn, int[] clickedBoardButtonLocation, int[] stoneOneLocation, int[] stoneTwoLocation) {
    /*Boolean to store whether the clicked board button is valid or not*/
    boolean conditionValid = false;
    /*Check conditionValid*/
    if (highlightedButton != null) {
      if (turn < 3) {
        if (turn == 1) {
          if (clickedBoardButtonLocation[1] == 1)
            conditionValid = true;
        }
        if (turn == 2) {
          if (clickedBoardButtonLocation[1] == column)
            conditionValid = true;
        }
      }
      if (turn > 2) {
        if (turn % 2 == 1) {
          if ((clickedBoardButtonLocation[0] == stoneOneLocation[3]) && (clickedBoardButtonLocation[1] == stoneOneLocation[4]))
            conditionValid = true;
        }
        if (turn % 2 == 0) {
          if ((clickedBoardButtonLocation[0] == stoneTwoLocation[3]) && (clickedBoardButtonLocation[1] == stoneTwoLocation[4]))
            conditionValid = true; 
        }
      }
    }
    return conditionValid;
  }
  
  /**A method that helps detemining if two stone have met each other or not 
   *@param stoneOnePosition an array that represents the first stone position 
   *@param stoneTwoPosition an array that represents the second stone position 
   *@return true if two stone have met. Return false otherwise. 
   */
  public static boolean checkStonesMeet (int[] stoneOnePosition, int[] stoneTwoPosition) {
    if ((stoneOnePosition[3] == stoneTwoPosition[0]) && (stoneOnePosition[4] == stoneTwoPosition[1])) {
      if (stoneOnePosition[5] == stoneTwoPosition[2]) 
        return true;
    }
    return false;
  }
  
  /**Move the stones on the board after placing a new button on the board 
   *@param b the clicked board button
   */
  public void boardMove(TsuroButton b) {
    /*The main game activities on the board when both conditionYellow and conditionValid are true*/
      if (validBoardMove(highlightedButton, turn, clickedBoardButtonLocation, stoneOneLocation, stoneTwoLocation)) {
        /*Check if stone one has moved in this game loop or not*/
        boolean stoneOneMoved = false;
        /*Check if stone two has moved in this game loop or not*/
        boolean stoneTwoMoved = false;
        /*Add the highlighted player's button to the clicked board button, then set new connections to the highlighted button and unhighlight that button*/
        b.setConnections(highlightedButton.getConnections());
        highlightedButton.setConnections(highlightedButton.makeRandomConnectArray());
        highlightedButton.setBackground(Color.WHITE);
        highlightedButton = null;
        /*Set up the initial values for stone one on the first move of player one*/
        if (turn == 1) {
          stoneOneMoved = true;
          stoneOneLocation = initialStoneLocation(b, turn, clickedBoardButtonLocation);
        }
        /*Set up the initial values for stone two on the first move of player two*/
        if (turn == 2) {
          stoneTwoMoved = true;
          stoneTwoLocation = initialStoneLocation(b, turn, clickedBoardButtonLocation);
        }
        /*In case the two stone have to move on the same line, for ex: stone one moves from 0 to 7 and stone two moves from 7 to 0, 
         * stoneOneTurn and stoneTwoTurn help determining which stone will moves first based on whose turn it is.
         * This help determining who is the winner after that step.*/
        int stoneOneTurn = -1;
        int stoneTwoTurn = -1;
        if (turn % 2 == 1) {
          stoneOneTurn = 1;
          stoneTwoTurn = 0;
        } else {
          stoneOneTurn = 0;
          stoneTwoTurn = 1;
        }
        /*The main game loop that moves stones on the board*/
        while ((stoneOneTurn < 2) || (stoneTwoTurn < 2)) {
          while ((stoneOneLocation[3] > 0) && (stoneOneLocation[3] < row + 1) && (stoneOneLocation[4] > 0) && (stoneOneLocation[4] < column + 1) && (boardButton[stoneOneLocation[3] - 1][stoneOneLocation[4] - 1].getConnections() != null) && (checkStonesMeet(stoneOneLocation, stoneTwoLocation) == false) && (stoneOneTurn == 1)) {
            stoneOneMoved = true;
            boardButton[stoneOneLocation[0] - 1][stoneOneLocation[1] - 1].removeStone(stoneOneLocation[2]);
            boardButton[stoneOneLocation[3] - 1][stoneOneLocation[4] - 1].addStone(Color.RED, boardButton[stoneOneLocation[3] - 1][stoneOneLocation[4] - 1].getConnections()[stoneOneLocation[5]]);
            stoneLocation(stoneOneLocation, boardButton);
          }
          while ((stoneTwoLocation[3] > 0) && (stoneTwoLocation[3] < row + 1) && (stoneTwoLocation[4] > 0) && (stoneTwoLocation[4] < column + 1) && (boardButton[stoneTwoLocation[3] - 1][stoneTwoLocation[4] - 1].getConnections() != null) && (checkStonesMeet(stoneTwoLocation, stoneOneLocation) == false) && (stoneTwoTurn == 1)) {
            stoneTwoMoved = true;
            boardButton[stoneTwoLocation[0] - 1][stoneTwoLocation[1] - 1].removeStone(stoneTwoLocation[2]);
            boardButton[stoneTwoLocation[3] - 1][stoneTwoLocation[4] - 1].addStone(Color.BLUE, boardButton[stoneTwoLocation[3] - 1][stoneTwoLocation[4] - 1].getConnections()[stoneTwoLocation[5]]);
            stoneLocation(stoneTwoLocation, boardButton);
          }
          stoneOneTurn++;
          stoneTwoTurn++;
        }
        /*Set the new position for the stones on player one's hand if stone one moved*/
        if (stoneOneMoved) {
          for (int i = 0; i < handsize; i++) {
            for (int j = 0; j < 8; j++)
              handOneButton[i].removeStone(j);
            handOneButton[i].addStone(Color.RED, stoneOneLocation[5]);
          }
        }
        /*Set the new position for the stones on player two's hand if stone two moved*/
        if (stoneTwoMoved) {
          for (int i = 0; i < handsize; i++) {
            for (int j = 0; j < 8; j++)
              handTwoButton[i].removeStone(j);
            handTwoButton[i].addStone(Color.BLUE, stoneTwoLocation[5]);
          }
        }
        /*Increase turn*/
        turn++;
      }
  }
  
  /**Actions performed when a button is clicked 
   *@param e the event of clicking buttons 
   */
  @Override 
  public void actionPerformed(ActionEvent e) {
    TsuroButton b = (TsuroButton)e.getSource();
    /*Identify what is the clicked button. Equals 1 if player one's button. Equals 2 if player's two button. Equals 3 if board's button*/
    int identification = 0;
    /*Check if the clicked button is player one's or player two's button*/
    for (int i = 0; i < handsize; i++) {
      if (b == handOneButton[i]) {
        identification = 1;
      }
      if (b == handTwoButton[i]) {
        identification = 2;
      }
    }
    /*Check if the clicked button is a board's button*/
    for (int i = 0; i < row; i++)
      for (int j = 0; j < column; j++) {
      if (b == boardButton[i][j]) {
        identification = 3;
        clickedBoardButtonLocation[0] = i + 1;
        clickedBoardButtonLocation[1] = j + 1; 
      }
    }
    /*Tasks to do if the clicked button is a player's button*/
    if (((identification == 1) && (turn % 2 == 1)) || ((identification == 2) && (turn % 2 == 0))) 
      highlight(b);
    /*Tasks to do if the clicked button is a board's button*/
    if (identification == 3)
      boardMove(b);
  }
  
  /**The main method 
    */ 
  public static void main(String[] args) {
    try {
      boolean valid = true;
      if (args.length >= 4) 
        System.out.println("Invalid input. Need zero, two or three integers.");
      if (args.length == 1)
        System.out.println("Invalid input. Need zero, two or three integers.");
      for (int i = 0; i < args.length; i++) {
        if (Integer.parseInt(args[i]) <= 0) {
          System.out.println("Invalid input. Need integers larger or equal to 1."); 
          valid = false;
        }
      }
      if (valid) {
        if (args.length == 0)
          new Tsuro();
        if (args.length == 2)
          new Tsuro(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        if (args.length == 3)
          new Tsuro(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
      }
    } catch (NumberFormatException e) {
      System.out.println("Invalid input. Need integers."); 
    }
  }
}