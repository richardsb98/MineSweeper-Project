package Minesweeper;

import java.util.Scanner;

public class Game {

    /////////////////////////////// Attributes ///////////////////////////////
    private Board board;

    private Scanner scan;


    /////////////////////////////// Constructor ///////////////////////////////

    public Game() {                                // Initialises the game with a shared Scanner. Creates a board with size 10,10 and spawns up to 10 bombs on it.
        scan = new Scanner(System.in);
        board = new Board(10,10);
        board.spawnBombs(10);
    }

    /////////////////////////////// Methods ///////////////////////////////
    /*
      Handles the game loop, swapping between printing the board and handling input to reach the next state.
      If a position is typed in followed by "flag" the target cell will be flagged.
      Otherwise the position will be used to reveal that target cell.
      Once the game reaches either a win state where all non-bombs have been revealed or a bomb has been revealed, the game ends.
      When the game ends, the full board is revealed and a victory/defeat message is shown.
     */
    public void startGame() {                                               // Initiates start game from main class
        String extraInput;                                                // Used to allow flag input following coordinates input.
        Position inputPosition;

        do {
            board.printBoard();                                             // This function will print the board and allow user to play while the below conditions are met.
            board.printStatus();
            inputPosition = getPositionInput();
            extraInput = getStringOrQuit(scan).trim();
            if(extraInput.equalsIgnoreCase("flag")) {                     // if input followed by 'flag' places F in chosen cell
                board.flagCell(inputPosition);
            } else if(board.isCellFlagged(inputPosition)) {                             // if flagged cell chosen, prints out line
                System.out.println("You need to un-flag that cell first.");
            } else {                                                                    // reveals chosen cell
                board.revealCell(inputPosition);
            }
        } while(!board.isWon() && (extraInput.equalsIgnoreCase("flag") || !board.isCellBomb(inputPosition)));                     //Above function runs as long as: game is not in a win state or, user wants to place a flag or, cell chosen is a bomb
        board.revealAll();
        board.printBoard();
        if(board.isWon()) {
            System.out.println("WINNER!, you found all the bombs!");                 // Once revealedTotal + bombCount == width*height, game is over and user has won
        } else {
            System.out.println("       YOU HIT A MINE! \n--------- GAME OVER ---------");      // choosing mine will display game over line
        }
    }

    public Position getPositionInput() {                                        // Method to loop input until valid coordinates received
        Position input = new Position(0,0);
        do {
            System.out.println("Enter Horizontal Coordinate Then Vertical Coordinate Seperated By A Space. To Place A Flag, Enter Coordinates Followed By flag e.g: 3 3 flag");
            if(!scan.hasNextInt()) {
                getStringOrQuit(scan);
                System.out.println("Invalid X coordinate.");
                continue;
            }
            input.x = scan.nextInt();
            if(!scan.hasNextInt()) {
                getStringOrQuit(scan);
                System.out.println("Invalid Y coordinate.");
                continue;
            }
            input.y = scan.nextInt();                                               // The values are stored into targetX and targetY with adjusted value to take them from the (1 -> n) range to the (0 -> n-1) range.
            input.x--;
            input.y--;
        } while(!isPositionInputValid(input));
        return input;
    }


    private boolean isPositionInputValid(Position position) {                               // Checks if given values are within boundaries of board and if cell is already revealed yet or not.
        if(!board.validPosition(position)) {
            System.out.println("Coordinate not inside the play space!");
            return false;
        }
        if(board.isCellRevealed(position)) {
            System.out.println("That cell is already revealed!");
            return false;
        }
        return true;                                                                        // returns true if coordinates is inside board space and above conditions are met
    }

    public static String getStringOrQuit(Scanner scan) {                                    // Method to allow user to quit game
        String input = scan.nextLine();
        if(input.equalsIgnoreCase("quit"))
         {
            System.out.println("Thanks for playing! Goodbye!");
            System.exit(0);
        }
        return input;
    }
}