/**
 * File Name: Interface.java
 * Course: COMP2230 - Algorithms
 * Assessment: Assignment 1
 */

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Interface
{
    public static void main(String[] args)
    {
        Board board  = new Board();         // a variable to store an instance of the  board object
        String movesMade = "";
        Scanner in = new Scanner(System.in);

        String input;
        int id = 0;                 // used to store the id of the AI
        String[] enter;             // used to store the input from the coordinator
        Boolean maximisingPlayer = false;

        boolean running = true;
        while (running)
        {
            input = in.nextLine();
            enter = input.split("\\s+");
            switch (enter[0])
            {
                case ("name"):
                    id = Integer.parseInt(enter[2]);     // storing the id of the AI
                    // if the player has id '0 meaning that its first player then it is the maximising player.
                    if (id == 0)
                    {
                        maximisingPlayer = true;
                    }
                    System.out.println("test_engine-c3256655");
                    break;

                case ("isready"):
                    try {
                        TimeUnit.MILLISECONDS.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("readyok");
                    break;

                case ("position"):
                    if (enter.length > 2)
                    {
                        // resetting the board
                        board.resetBoard();
                        movesMade = enter[2];
                        // each in the string is added to the board
                        for (int i = 0 ; i < movesMade.length(); i++)
                        {
                            int move = Character.getNumericValue(movesMade.charAt(i));
                            // the last move was made by the player one
                            if (((i+1)%2) == 1)
                            {
                                board.addMove(move,0);
                            }
                            // the last move was made by player 2
                            else
                            {
                                board.addMove(move,1);
                            }
                        }
                    }
                    break;

                case ("go"):
                    int[] bestMove = new int[2];

                    // it is the maximising player
                    if (id == 0)
                    {
                        // Put a random number generator here to make it easier for you to mark =)
//                        Random randomGenerator = new Random();
//                        ArrayList<Integer> run = board.checkPossibleMoves();
//                        int randomInt = randomGenerator.nextInt(run.size());
//                        bestMove = new int[]{run.get(randomInt), 0};

                        bestMove = minimaxStart(3,true, board);
                    }
                    // it is the minimsing player
                    else
                    {
                        bestMove = minimaxStart(3,false, board);
                    }
                    // ouputting the best move and its associated evaluation function
                    System.out.println("bestmove " + bestMove[0] + " " + bestMove[1]);
                    break;

                case ("perft"):
                    int x = Integer.parseInt(enter[1]);
                    int y = perftStart(x, true, board);
                    System.out.println("perft " + x + " " + y);
                    break;

                case ("quit"):
                    System.out.println("quitting");
                    running = false;
                    break;
            }
        }

    }

    /*
        Purpose: a function to start the minimax algorithm
        Pre-Condition: valid inputs are provided to the function
        Post-Condition: the best move and it's associated evaluation score is returned
     */
    public static int[] minimaxStart(int depth , boolean maximisingPlayer, Board board)
        {
            // checks to see how many moves the player can make
            ArrayList<Integer> numberOfMoves = board.checkPossibleMoves();

            // variables to store the best move and evaluation score
            int  bestIndex = 0;
            int  evaluation = 0;

            // the player is the maximising player
            if(maximisingPlayer)
            {
                int  bestValue = Integer.MIN_VALUE;		// negative infinity for 32 bit

                for(int i = 0; i < numberOfMoves.size(); i++)
                    {
                        // add a move to the board by the maximising player
                        board.addMove(numberOfMoves.get(i),0);
                        // calling the minimax function
                        int  tmpValue = minimax(depth - 1 , false, board);
                        // removing the move made from the board
                        board.undoMove(numberOfMoves.get(i));
                        // comparing the best value and the returned value to see which one is greater
                        if(tmpValue > bestValue)
                        {
                            // we’re  trying  to maximise , so if we get a bigger  value , update
                            bestValue = tmpValue;
                            bestIndex = numberOfMoves.get(i);       // getting the index of the best move.
                            evaluation = bestValue;
                        }
                    }
            }
            // the player is the minimising player
            else
            {
                //  minimising  player
                int  bestValue = Integer.MAX_VALUE; 	// positive infinity for 32 bit
                for(int i = 0; i < numberOfMoves.size(); i++)
                {
                    // add a move to the board by the minimising player
                    board.addMove(numberOfMoves.get(i),1);
                    // calling the minimax function
                    int tmpValue = minimax(depth - 1 , true, board);
                    // removing the move made from the board
                    board.undoMove(numberOfMoves.get(i));
                    // comparing the best value and the returned value to see which one is smaller
                    if(tmpValue < bestValue)
                    {
                        // we’re  trying  to minimise , so if we get a smaller  value , update
                        bestValue = tmpValue;
                        bestIndex = numberOfMoves.get(i);           // getting the index of the best move.
                        evaluation = bestValue;
                    }
                }
            }
            // creating an array to store best index and evaluation score which is then returned
            int[] result = {bestIndex, evaluation};
            return result;
        }

    /*
        Purpose: Main minimax algorithm which is a recursive function that calls itself until it reach the max depth set
        Pre-Condition: valid inputs are provided to the function
        Post-Condition: an evaluation score is returned to each call. Which depending on the player, an optimal move is selected
     */
    public static int  minimax(int depth , boolean maximisingPlayer, Board board)
        {
            // check moves that the player can make
            ArrayList<Integer> numberOfMoves = board.checkPossibleMoves();
            // calling the checkIfWon function to check a player has won the game
            int checkWon = board.checkIfWon();

            // check to see if any player has won the game
            // if any player has won then there is no point in generating moves after that point.
            // the minimising player has won the game
            if (checkWon == 1)
            {
                return -1000;
            }
            // the maximising player has won the game
            if (checkWon == 0)
            {
                return 1000;
            }

            // check to see if the max depth is reached
            if(depth == 0)
            {
                return board.evaluationFunction();		// sees how good the move is (chance of winning)
            }

            int value = 0;              // a variable to store the highest or lowest evaluation score (based on teh player on that depth)

            if(maximisingPlayer)
            {
                // set  the  current  evaluation  to be as  small as possible  if we wish to  maximise  it
                value = Integer.MIN_VALUE;
                for(int i = 0; i < numberOfMoves.size(); i++)
                {
                    // adding the move by the player to the board
                    board.addMove(numberOfMoves.get(i),0);
                    // if we can do better , then  set  value to the better  evaluation
                    value = Math.max(value , minimax(depth - 1,  false,board));
                    // removing the move made by the player
                    board.undoMove(numberOfMoves.get(i));
                }
            }
            else
            {
                // we are  trying  to  minimise
                // set  the  current  evaluation  to be as  large as possible  if we wish to  minimise  it
                value = Integer.MAX_VALUE;
                for(int i = 0; i < numberOfMoves.size(); i++)
                {
                    // adding the move by the player to the board
                    board.addMove(numberOfMoves.get(i),1);
                    value = Math.min(value , minimax(depth - 1, true,board));
                    // removing the move made by the player
                    board.undoMove(numberOfMoves.get(i));
                }
            }
            return  value;
        }

    /*
        Purpose: To get the number of moves possible to certain depth
        Pre-Condition: valid inputs are provided
        Post-Condition: the number of nodes to each certain depth are returned
    */
    public static int perftStart(int depth, boolean maximisingPlayer, Board board)
    {
        int value = 1;           // value is assigned the value of '1' as the root node is a node
        ArrayList<Integer> numberOfMoves = board.checkPossibleMoves();      // check to see the number of possible that are possible

        if(maximisingPlayer)
        {
            // making each move and getting it's children
            for(int i = 0; i < numberOfMoves.size(); i++)
            {
                board.addMove(numberOfMoves.get(i),0);
                value += perft(depth - 1 , false, board);
                board.undoMove(numberOfMoves.get(i));
            }
        }
        // else the player is a minimising player
        else
        {
            // making each move and getting it's children
            for(int i = 0; i < numberOfMoves.size(); i++)
            {
                board.addMove(numberOfMoves.get(i),1);
                value += perft(depth - 1 , true, board);
                board.undoMove(numberOfMoves.get(i));
            }
        }
        return value;           // returning value
    }

    /*
        Purpose: Main perft function to count the number of nodes to certain depth (a recursive function)
        Pre-Condition: valid inputs are provided to the function
        Post-Condition: the number of nodes are to the specified depth is returned
     */
    public static int perft(int depth, boolean maximisingPlayer, Board board)
    {
        int moves = 0;      // a local variable to store the amount of moves possible at this depth

        // check to see if the lowest depth is reached
        if(depth == 0)
        {
            return 1;       // returning one as it s the bottom node (leaf node)
        }
//        // check moves that it can make
        ArrayList<Integer> numberOfMoves = board.checkPossibleMoves();
        // check to see if any player has won the game
        int checkWon = board.checkIfWon();

        // check to see if any player has won the game
        // if any player has won then there is no point in generating any more chilren after this point.
        if (checkWon != -1)
        {
            return 1;
        }

        if(maximisingPlayer)
        {
            for(int i = 0; i < numberOfMoves.size(); i++)
            {
                board.addMove(numberOfMoves.get(i),0);
                moves += perft(depth - 1,  false,board);
                board.undoMove(numberOfMoves.get(i));
            }
        }
        else
        {
            for(int i = 0; i < numberOfMoves.size(); i++)
            {
                board.addMove(numberOfMoves.get(i),1);
                moves += perft(depth - 1,  true,board);
                board.undoMove(numberOfMoves.get(i));
            }
        }
        return moves + 1;       // adding one as the current node alos counts as a node in the tree
    }
}