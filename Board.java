/**
 * File Name: Board.java
 * Course: COMP2230 - Algorithms
 * Assessment: Assignment 1
 */
import java.util.ArrayList;

public class Board
{
    final public int rows = 6;
    final public int columns = 7;
    public int[][] boardState = new int[rows][columns];

    /*
        Purpose: Constructor for the Board class
        Pre-Condition: NONE
        Post-Condition: an instance of Board is created
     */
    public Board() {
        for (int i = 0; i < columns; i++) {
            for (int z = 0; z < rows; z++) {
                boardState[z][i] = -1;        // setting the default value to -1
            }
        }
    }

    /*
        Purpose: to get the number of moves possible on the board
        Pre-Condition: an instance of board exists within the program
        Post-Condition: the number of moves that a player can make is returned
     */
    public ArrayList<Integer> checkPossibleMoves()
    {
        ArrayList<Integer> movesPossible = new ArrayList<Integer>();

        for (int i = 0; i < columns; i++)
        {
            if (!checkIfColumnIsFull(i))
            {
                movesPossible.add(i);
            }
        }
        return movesPossible;
    }

    /*
        Purpose: undo a move that a player has made
        Pre-Condition: valid input is provided and an instance of board exists
        Post-Condition: the piece at the top of the column is removed
     */
    public void undoMove(int moveMade) {
        // check from top to down for the best move
        for (int i = 0; i < rows; i++) {
            if (boardState[i][moveMade] != -1)
            {
                boardState[i][moveMade] = -1;
                break;
            }
        }
    }

    /*
        Purpose: add a move to the board
        Pre-Condition: valid inputs are provided and an instance of board exists within the program
        Post-Condition: a move is added to the board. The piece added depends on which player is addinghte move to the board
     */
    public void addMove(int moveMade, int player)
    {
        // going from bottom to top
        for (int i = rows - 1; i >= 0; i--)
        {
            if (boardState[i][moveMade] == -1)
            {
                boardState[i][moveMade] = player;
                break;
            }
        }
    }

    /*
        Purpose: a function to check if a player has won the game.
        Pre-Condition: an instance of board exists within the program
        Post-Condition: a value is returned which depends on if a player has won. If a player has won then the value depends on which player it was
     */
    public int checkIfWon()
    {
        int currentPiece = -1;
        // checking horizontal lines from bottom up
        for (int i = rows - 1; i >=0; i--)
        {
            // going through the columns for left to right.
            for (int z = 0; z < 4; z++)
            {
                currentPiece = boardState[i][z];
                // check to see if the current piece is empty
                if (currentPiece != -1)
                {
                    // if not then we check to see the next 3 columns in the rows are the current players piece
                    if (boardState[i][z+1] == currentPiece && boardState[i][z+2] == currentPiece && boardState[i][z+3] == currentPiece)
                    {
                        return currentPiece;
                    }
                }
            }
        }

        // checking vertically
        for (int c = 0 ; c < columns; c++)
        {
            // checking from bottom to top
            for (int r = rows - 1; r >= 3; r--)
            {
                currentPiece = boardState[r][c];
                if (currentPiece != -1)
                {
                    if (boardState[r-1][c] == currentPiece && boardState[r-2][c] == currentPiece && boardState[r-3][c] == currentPiece)
                    {
                        return currentPiece;
                    }
                }
            }
        }

        // checking diagonally (positive slope)
        // an array to store the starting positions for diagonal
        int[] startpos =  {2,0, 1,0, 0,0, 0,1, 0,2, 0,3};
        int index = 0;          // a variable used as an index for the array
        for (int i = 0; i <= 5; i++)
        {
            int x = startpos[index];        // getting the starting row
            int y = startpos[index+1];      // getting the starting column
            index += 2;
            while (true)
            {
                currentPiece = boardState[x][y];
                // check to see if the current piece is null (-1)
                if (currentPiece != -1)
                {
                    // check to see if the next 3 piece are the same as the current piece
                    if (boardState[x+1][y+1] == currentPiece && boardState[x+2][y+2] == currentPiece && boardState[x+3][y+3] == currentPiece)
                    {
                        return currentPiece;
                    }
                }

                // check to see it's is possible to move to the next positon and whether there is 3 after that position
                if (x + 4 >= rows || y + 4 >= columns)
                {
                    break;
                }
                else
                {
                    x += 1;
                    y += 1;
                }
            }
        }

        // the starting position for the array
        startpos = new int[]{0,3, 0,4, 0,5, 0,6, 1,6, 2,6};
        index = 0;
        for (int i = 0; i <= 5; i++)
        {
            int x = startpos[index];        // getting the starting row
            int y = startpos[index+1];      // getting the starting column
            index += 2;
            while (true)
            {
                currentPiece = boardState[x][y];
                // check to see if the current piece is null (-1)
                if (currentPiece != -1)
                {
                    // check to see if the next 3 pieces are the same the current piece
                    if (boardState[x+1][y-1] == currentPiece && boardState[x+2][y-2] == currentPiece && boardState[x+3][y-3] == currentPiece)
                    {
                        return currentPiece;
                    }
                }

                // check to see it's is possible to move to the next positon and whether there is 3 after that position
                if (x + 4 >= rows || y - 4 < 0)
                {
                    break;
                }
                else
                {
                    x += 1;
                    y -= 1;
                }
            }
        }
        return -1;
    }

    /*
        Purpose: a function to evaluate the state of a board
        Pre-Condition: an instance of board exists
        Post-Condition: a evaluation score is returned which is based on which based has won or is about to win
     */
    public int evaluationFunction()
    {
        // check to see if any player has won the game
        int value = checkIfWon();
        // player one has won the game
        if (value == 0)
        {
            // since player one has won (the maximising player), we return a large value number
            value = 1000;
        }
        // player 2 has won the game
        else if (value == 1) {
            // since player two has won (the minimising player), we return a large negative value number
            value = -1000;
        }
        // no one has won the game yet, only check for 3 in a row not anything more or less.
        else
        {
            value = 0;

            // we'd prefer to go in the middle as that would be the best place to put a piece and increase that chance of winning for the player
            int[] preference = {1,2,3,4,3,2,1};

            // checking vertically
            for (int c = 0; c < columns; c++)
            {
                // check to see if the column is full, if so there is no point in checking the column
                if(!checkIfColumnIsFull(c))
                {
                    // checking through the rows in the columns
                    for (int r = 0; r < 3; r++)
                    {
                        // checking the column in rows
                        value += checkMoves(new int[]{boardState[r][c], boardState[r+1][c], boardState[r+2][c], boardState[r+3][c]}) * preference[c];
                    }
                }
            }

            // checking horizontally
            for (int r = rows - 1; r >= 0; r--)
            {
                // check to see if the row if full
                if (checkIfRowIsFull(r))
                {
                    // checking through the columns in that row
                    for (int c = 0; c < 4; c++)
                    {
                        value += checkMoves(new int[]{boardState[r][c], boardState[r][c+1], boardState[r][c+2], boardState[r][c+3]});
                    }
                }
            }

            // checking diagonally (+ve slope)
            // starting position for the diagonal
            int[] startpos =  {2,0, 1,0, 0,0, 0,1, 0,2, 0,3};
            int index = 0;      // a variable used an index for startpos array
            for (int i = 0; i <= 5; i++)
            {
                int x = startpos[index];        // getting the starting row position
                int y = startpos[index+1];      // getting the starting column position
                index += 2;
                while (true)
                {
                    value += checkMoves(new int[]{boardState[x][y], boardState[x+1][y+1], boardState[x+2][y+2], boardState[x+3][y+3]});

                    // check to see it's is possible to move to the next position and whether there is 3 after that position
                    if (x + 4 >= rows || y + 4 >= columns)
                    {
                        break;
                    }
                    else
                    {
                        x += 1;
                        y += 1;
                    }
                }
            }

            // checking diagonally (-ve slope)
            startpos = new int[]{0,3, 0,4, 0,5, 0,6, 1,6, 2,6};
            index = 0;
            for (int i = 0; i <= 5; i++)
            {
                int x = startpos[index];        // getting the starting row position
                int y = startpos[index+1];      // getting the starting column position
                index += 2;
                while (true)
                {
                    value += checkMoves(new int[]{boardState[x][y], boardState[x+1][y-1], boardState[x+2][y-2],  boardState[x+3][y-3]});
                    // check to see it's is possible to move to the next positon and whether there is 3 after that position
                    if (x + 4 >= rows || y - 4 < 0)
                    {
                        break;
                    }
                    else
                    {
                        x += 1;
                        y -= 1;
                    }
                }
            }

        }
        return value;
    }

    /*
        Purpose: checks a set of 4 pieces to see if any player is close to winning
        Pre-Condition: a valid input is provided and an instance of board exists
        Post-Condition: a value is returned based on whether a player is close to winning the game based on the combination provided
     */
    private int checkMoves(int[] moves)
    {
        // a variable to store the index of any freespaces in the array
        ArrayList<Integer> freeSpaces = new ArrayList<>();
        // a variable to store the number of pieces that are player one's pieces
        int playerOnePieces = 0;
        // a variable to store the number of pieces that are player two's pieces
        int playerTwoPieces = 0;

        // iterating through the aray adn counting the number pieces that belong to each player or if it is player
        for (int i = 0; i < 4; i++)
        {
            // check to see if the current position has the player one's piece
            if (moves[i] == 0)
            {
                playerOnePieces++;
            }
            // check to see if the current position has the player two's piece
            else if (moves[i] == 1)
            {
                playerTwoPieces++;
            }
            // it is a free space
            else
            {
                freeSpaces.add(i+1);
            }
        }

        // if there is an enemy piece in the array then the other player is not close to winning the game.
        // there are only player one's pieces in the array. no points are awarded if there is only one piece in the array
        if (playerOnePieces > 1 && playerTwoPieces == 0)
        {
            // there are 2 or more player one pieces in the array. So we check how many free spaces there are in the array.
            // there is only one free space in the array
            if (freeSpaces.size() == 1)
            {
                // check to see if the piece has on either end of the array
                if ((freeSpaces.get(0) - 1 == 0) || (freeSpaces.get(0) - 1 == 3))
                {
                    return 3;
                }
                // the free space is in position 1 or 2 in the array meaning that there is two player one pieces together
                else
                {
                    return 2;
                }
            }
            // there is more than one free spaces in the array
            else
            {
                int distance = (freeSpaces.get(1) - 1) - (freeSpaces.get(0) - 1);
                // check to see if the free spaces are together
                if (distance == 1)
                {
                    // checking if the two spaces are on either end of the array
                    if ((freeSpaces.get(0) - 1 == 0) || (freeSpaces.get(1) - 1 == 3))
                    {
                        // the free spaces are on ends on the array
                        return 2;
                    }
                    // else the free spaces are in the middle of the array
                    else
                    {
                        return 0;
                    }
                }
                // check to see if the pieces are separated by a player piece
                else if (distance == 2)
                {
                    return 0;
                }
                // if the program gets to this else statement then the free spaces are obviously on either ends on the array
                else
                {
                    return 2;
                }
            }
        }

        // if there is an enemy piece in the array then the other player is not close to winning the game.
        // there are only player two's pieces in the array. no points are awarded if there is only one piece in the array
        if (playerTwoPieces > 1 && playerOnePieces == 0)
        {
            // there are 2 or more player twos pieces in the array. So we check how many free spaces there are in the array.
            // there is only one free space in the array
            if (freeSpaces.size() == 1)
            {
                // check to see if the piece has on either end of the array
                if ((freeSpaces.get(0) - 1 == 0) || (freeSpaces.get(0) - 1 == 3))
                {
                    return -3;
                }
                // the free space is in position 1 or 2 in the array meaning that there is two player one pieces together
                else
                {
                    return -2;
                }
            }
            // there is more than one free spaces in the array
            else
            {
                int distance = (freeSpaces.get(1) - 1) - (freeSpaces.get(0) - 1);
                // check to see if the free spaces are together
                if (distance == 1)
                {
                    // checking if the two spaces are on either end of the array
                    if ((freeSpaces.get(0) - 1 == 0) || (freeSpaces.get(1) - 1 == 3))
                    {
                        // the free spaces are on ends on the array
                        return -2;
                    }
                    // else the free spaces are in the middle of the array
                    else
                    {
                        return 0;
                    }
                }
                // check to see if the pieces are separated by a player piece
                else if (distance == 2)
                {
                    return 0;
                }
                // if the program gets to this else statement then the free spaces are obviously on either ends on the array
                else
                {
                    return -2;
                }
            }
        }
        return 0;
    }

    /*
        Purpose: check to see if the row is full
        Pre-Condition: a valid input is provided and an instance of board exists within the program
        Post-Condition: a boolean is returned which depends on whether the row is full or not
     */
    public boolean checkIfRowIsFull(int row)
    {
        boolean full = true;
        // iterating through the columns in the row
        for (int i = 0; i < columns; i++)
        {
            // check to see if the current position is free
            if (boardState[row][i] == -1)
            {
                full = false;
                break;
            }
        }
        return full;
    }

    /*
        Purpose: a function check if a column is free
        Pre-Condition: a valid input is provided and an instance of board exists within the program
        Post-Condition: a boolean value is returned which depends on the whether the row is full or not
     */
    private boolean checkIfColumnIsFull(int column)
    {
        // iterating through the rows in the column
        for (int i = 0; i < rows; i++)
        {
            if (boardState[i][column] == -1)
            {
                return false;
            }
        }
        return true;
    }

    /*
        Purpose: a function to reset the board
        Pre-Condition: an instance of board exists within the program
        Post-Condition: all values in the 2d array are set to -1 (empty position)
     */
    public void resetBoard()
    {
        for (int r = 0; r < rows; r++)
        {
            for (int c = 0; c < columns; c++)
            {
                boardState[r][c] = -1;
            }
        }
    }
}