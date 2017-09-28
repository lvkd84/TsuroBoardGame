/**Test class for Tsuro 
  *@author Kha Dinh Luong
  */

import org.junit.*;
import static org.junit.Assert.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;

public class TestTsuro {
  
  /*Test constructors*/
  
  /**Test the constructor that takes no input*/
  @Test 
  public void testConstructor1() {
    Tsuro game = new Tsuro();
    assertEquals("Check row", 6.0, (double)game.getRow(), 0);
    assertEquals("Check column", 6.0, (double)game.getColumn(), 0);
    assertEquals("Check handsize", 3.0, (double)game.getHandsize(), 0);
    assertEquals("Check number of hand button: player one", 3.0, (double)game.getHandButton(1).length, 0);
    assertEquals("Check number of hand button: player two", 3.0, (double)game.getHandButton(2).length, 0);
    for (int i = 0; i < 3; i++) {
      assertNotNull("Check connections: player one", game.getHandButton(1)[i].getConnections());
      assertNotNull("Check connections: player two", game.getHandButton(2)[i].getConnections());
    }
    for (int i = 1; i < 7; i++)
      for (int j = 1; j < 7; j++)
        assertNull("Check connections: board", game.getBoardButton(i, j).getConnections());
  }
  
  /**Test the constructor that takes two inputs*/
  @Test 
  public void testConstructor2() {
    Tsuro game = new Tsuro(4,5);
    assertEquals("Check row", 4.0, (double)game.getRow(), 0);
    assertEquals("Check column", 5.0, (double)game.getColumn(), 0);
    assertEquals("Check handsize", 3.0, (double)game.getHandsize(), 0);
    assertEquals("Check number of hand button: player one", 3.0, (double)game.getHandButton(1).length, 0);
    assertEquals("Check number of hand button: player two", 3.0, (double)game.getHandButton(2).length, 0);
    for (int i = 0; i < 3; i++) {
      assertNotNull("Check connections: player one", game.getHandButton(1)[i].getConnections());
      assertNotNull("Check connections: player two", game.getHandButton(2)[i].getConnections());
    }
    for (int i = 1; i < 5; i++)
      for (int j = 1; j < 6; j++)
        assertNull("Check connections: board", game.getBoardButton(i, j).getConnections()); 
  }
  
  /**Test the constructor that takes three inputs*/
  @Test 
  public void testConstructor3() {
    Tsuro game = new Tsuro(5,7,4);
    assertEquals("Check row", 5.0, (double)game.getRow(), 0);
    assertEquals("Check column", 7.0, (double)game.getColumn(), 0);
    assertEquals("Check handsize", 4.0, (double)game.getHandsize(), 0);
    assertEquals("Check number of hand button: player one", 4.0, (double)game.getHandButton(1).length, 0);
    assertEquals("Check number of hand button: player two", 4.0, (double)game.getHandButton(2).length, 0);
    for (int i = 0; i < 4; i++) {
      assertNotNull("Check connections: player one", game.getHandButton(1)[i].getConnections());
      assertNotNull("Check connections: player two", game.getHandButton(2)[i].getConnections());
    }
    for (int i = 1; i < 6; i++)
      for (int j = 1; j < 8; j++)
        assertNull("Check connections: board", game.getBoardButton(i, j).getConnections()); 
  }
  
  /*Test helper methods*/
  
  /**Test the rotationHelper method*/
  @Test
  public void testRotationHelper() {
    assertEquals("Case input = 0", 2, Tsuro.rotationHelper(0)); 
    assertEquals("Case input = 1", 3, Tsuro.rotationHelper(1));
    assertEquals("Case input = 2", 5, Tsuro.rotationHelper(2));
    assertEquals("Case input = 3", 4, Tsuro.rotationHelper(3));
    assertEquals("Case input = 4", 6, Tsuro.rotationHelper(4));
    assertEquals("Case input = 5", 7, Tsuro.rotationHelper(5));
    assertEquals("Case input = 6", 1, Tsuro.rotationHelper(6));
    assertEquals("Case input = 7", 0, Tsuro.rotationHelper(7));
  }
  
  /**Test the rotationPosition method*/
  @Test
  public void testRotationPosition() {
    Tsuro game = new Tsuro();
    int[] currentConnections = {6, 7, 5, 4, 3, 2, 0, 1};
    int[] resultConnections = {3, 2, 1, 0, 6, 7, 4, 5};
    assertArrayEquals(resultConnections, game.rotationPosition(currentConnections));
  }
  
  /**Test the highlight method*/
  @Test
  public void testHighlight() {
    Tsuro game = new Tsuro();
    TsuroButton highlightButton = null;
    TsuroButton b1 = new TsuroButton();
    TsuroButton b2 = new TsuroButton();
    /*Case 1*/
    game.highlight(b1);
    assertEquals("highlightedButton == null", b1.getBackground(), Color.YELLOW);
    assertSame("highlightedButton == null", b1, game.getHighlightedButton());
    /*Case 2*/
    game.highlight(b2);
    assertEquals("highlightedButton != b", b1.getBackground(), Color.WHITE);
    assertEquals("highlightedButton != b", b2.getBackground(), Color.YELLOW);
    assertSame("highlightedButton != b", b2, game.getHighlightedButton());
    /*Case 3*/
    b2.setConnections(TsuroButton.makeRandomConnectArray());
    int[] newConnections = new int[8];
    newConnections = game.rotationPosition(b2.getConnections());
    game.highlight(b2);
    assertArrayEquals("highlightedButton == b", newConnections, b2.getConnections());
    assertEquals("highlightedButton == b", b2.getBackground(), Color.YELLOW);
    assertSame("highlightedButton == b", b2, game.getHighlightedButton());
  }
  
  /**Test the initialStoneLocation method*/
  @Test
  public void testInitialStoneLocation() {
    TsuroButton b = new TsuroButton();
    Tsuro game = new Tsuro();
    int[] connections = {5, 2, 1, 7, 6, 0, 4, 3};
    b.setConnections(connections);
    int[] clickedBoardButtonLocation = {3, 1};
    int[] result = {3, 1, 4, 4, 1, 0};
    /*Case 1*/
    assertArrayEquals("Turn = 1", result, game.initialStoneLocation(b, 1, clickedBoardButtonLocation));
    /*Case 2*/
    clickedBoardButtonLocation[0] = 4;
    clickedBoardButtonLocation[1] = 6;
    int[] result2 = {4, 6, 1, 3, 6, 5};
    assertArrayEquals("Turn = 2", result2, game.initialStoneLocation(b, 2, clickedBoardButtonLocation));
  }
  
  /**Test the stoneLocation method*/
  @Test
  public void testStoneLocation() {
    Tsuro game = new Tsuro();
    int[] stoneLocation = {2, 3, 1, 1, 3, 5};
    TsuroButton[][] boardButton = new TsuroButton[6][6];
    int[] connections = {7, 4, 3, 2, 1, 6, 5, 0};
    int[] resultLocation = {1, 3, 6, 1, 2, 2};
    boardButton[0][2] = new TsuroButton();
    boardButton[0][2].setConnections(connections);
    game.stoneLocation(stoneLocation, boardButton);
    assertArrayEquals(resultLocation, stoneLocation);
  }
  
  /**Test the validBoardMove method*/
  @Test 
  public void testValidBoardMove() {
    Tsuro game = new Tsuro();
    TsuroButton highlightedButton = null;
    int turn = 1;
    int[] clickedBoardButtonLocation = {3, 0};
    int[] stoneOneLocation = {0, 0, 0, 3, 6, 0};
    int[] stoneTwoLocation = {0, 0, 0, 3, 6, 0}; 
    /*Case 1*/
    assertFalse("Case: highlightedButton = null", game.validBoardMove(highlightedButton, turn, clickedBoardButtonLocation, stoneOneLocation, stoneTwoLocation));
    highlightedButton = new TsuroButton();
    /*Case 2*/
    assertFalse("Case: highlightedButton != null, turn = 1, clickedBoardButtonLocation[1] != 1", game.validBoardMove(highlightedButton, turn, clickedBoardButtonLocation, stoneOneLocation, stoneTwoLocation));
    clickedBoardButtonLocation[1] = 1;
    /*Case 3*/
    assertTrue("Case: highlightedButton != null, turn = 1, clickedBoardButtonLocation[1] = 1", game.validBoardMove(highlightedButton, turn, clickedBoardButtonLocation, stoneOneLocation, stoneTwoLocation));
    turn = 2;
    /*Case 4*/
    assertFalse("Case: highlightedButton != null, turn = 2, clickedBoardButtonLocation[1] != column", game.validBoardMove(highlightedButton, turn, clickedBoardButtonLocation, stoneOneLocation, stoneTwoLocation));
    clickedBoardButtonLocation[1] = 6; 
    /*Case 5*/
    assertTrue("Case: highlightedButton != null, turn = 2, clickedBoardButtonLocation[1] = column", game.validBoardMove(highlightedButton, turn, clickedBoardButtonLocation, stoneOneLocation, stoneTwoLocation));
    turn = 3;
    /*Case 6*/
    assertTrue("Case: highlightedButton != null, turn % 2 = 1, clickedBoardButtonLocation[0] = stoneOneLocation[3], clickedBoardButtonLocation[1] = stoneOneLocation[4]", game.validBoardMove(highlightedButton, turn, clickedBoardButtonLocation, stoneOneLocation, stoneTwoLocation));
    stoneOneLocation[4] = 5;
    /*Case 7*/
    assertFalse("Case: highlightedButton != null, turn % 2 = 1, clickedBoardButtonLocation[0] = stoneOneLocation[3], clickedBoardButtonLocation[1] != stoneOneLocation[4]", game.validBoardMove(highlightedButton, turn, clickedBoardButtonLocation, stoneOneLocation, stoneTwoLocation));
    turn = 4;
    /*Case 8*/
    assertTrue("Case: highlightedButton != null, turn % 2 = 0, clickedBoardButtonLocation[0] = stoneOneLocation[3], clickedBoardButtonLocation[1] = stoneOneLocation[4]", game.validBoardMove(highlightedButton, turn, clickedBoardButtonLocation, stoneOneLocation, stoneTwoLocation));
    stoneTwoLocation[4] = 5;
    /*Case 9*/
    assertFalse("Case: highlightedButton != null, turn % 2 = 1, clickedBoardButtonLocation[0] = stoneOneLocation[3], clickedBoardButtonLocation[1] != stoneOneLocation[4]", game.validBoardMove(highlightedButton, turn, clickedBoardButtonLocation, stoneOneLocation, stoneTwoLocation));
  }
  
  /**Test the checkStonesMeet method*/ 
  @Test
  public void testCheckStonesMeet() {
    int[] stoneOneLocation = {3,5,4,4,5,0};
    int[] stoneTwoLocation = {4,5,0,3,5,4};
    assertTrue("Case: stones meet", Tsuro.checkStonesMeet(stoneOneLocation, stoneTwoLocation));
    stoneOneLocation[2] = 5;
    stoneOneLocation[5] = 1;
    assertFalse("Case: stones do not meet", Tsuro.checkStonesMeet(stoneOneLocation, stoneTwoLocation));
  }
}
