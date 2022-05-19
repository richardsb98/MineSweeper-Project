package Minesweeper;
import java.util.*;

public class Board {

    /////////////////////////////// Attributes ///////////////////////////////
    private final Cell[][] cells;             // 2d grid of cells


    private final int width;                  // Number of cells on horizontal axis


    private final int height;                 // Number of cells on vertical axis


    private int bombCount;             // Number of bombs on the board.


    private int revealedTotal;          // Number of cells been revealed.

    
    private int flagCount;           //Number of cells marked with a flag to block revealing as they are suspected to be bombs


    /////////////////////////////// Constructor ///////////////////////////////
    public Board(int width, int height) {               // Creates board with defined width and height
        this.width = width;
        this.height = height;
        bombCount = 20;
        cells = new Cell[width][height];                // All cells set to default initially after creation
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                cells[x][y] = new Cell();
            }
        }
        revealedTotal = 0;
        flagCount = 0;
    }

    /////////////////////////////// Methods ///////////////////////////////
    public void printBoard() {                          // Prints board to user in terminal

        for(int y = 0; y < height; y++) {               // Print the cells
            for(int x = 0; x < width; x++) {
                System.out.print(cells[x][y].getColouredString() + "  ");
            }


            System.out.println("|" + (y + 1));          // Prints numbers for this row at the end
        }


        for(int x = 0; x < width; x++) {                // Filler line
            System.out.print("_  ");
        }

        System.out.println();


        for(int x = 0; x < width; x++) {                // Numbers for each column
            System.out.print((x+1) + " ");
            if(x+1 < 10)
                System.out.print(" ");
        }
        System.out.println();
    }


    public void printStatus() {                                                 // Prints current status of game

        System.out.println(revealedTotal + " revealed of " + (height*width)
                + " with " + bombCount + " bombs! Flagged: " + flagCount);
    }



    public boolean isCellBomb(Position position) {                              // Gets if cell at chosen position is a bomb, returns true if so

        return cells[position.x][position.y].getIsBomb();
    }

    public boolean isCellRevealed(Position position) {                          // Gets if cell at chosen position is revealed, returns true if so

        return cells[position.x][position.y].getIsRevealed();
    }
    
    public boolean isCellFlagged(Position position) {                               // Gets if cell at chosen position is flagged, returns true if so

        return cells[position.x][position.y].getIsFlagged();
    }

    public Cell getCellAt(Position position) {                                      // Gets cell at chosen position, returns cell object at x,y value

        return cells[position.x][position.y];
    }

    public void revealCell(Position position) {                                     // Shows cell at x,y. If cell has no neighboring bombs, 
        if(cells[position.x][position.y].getNeighbours() != 0) {                    // flood fill used to reveal all 0s adjacent and then all surrounding numbers on it's border
            revealedTotal++;                                                        // reveals the cell
            cells[position.x][position.y].reveal();
        } else {
            List<Position> revealedCells = floodFillReveal(position);                       // Flood fill reveals all cells adjacent with 0s.
            List<Position> borderRevealedCells = revealAroundListOfPoints(revealedCells);   // Reveal all cells bordering with the cells that were just revealed.
            revealedTotal += revealedCells.size() + borderRevealedCells.size();
        }
    }

    public void flagCell(Position position) {                               // Toggle the cell's flagged state at x,y.

        getCellAt(position).toggleIsFlagged();                              // Keep count of the number of flagged cells

        if(getCellAt(position).getIsFlagged()){
            flagCount++;
        }
        else {
            flagCount--;
        }
    }



    public void spawnBombs(int maxBombs) {                      // Spawns bombs up to maximum value of maxBomb
        Random rand = new Random();
        for(int i = 0; i < maxBombs; i++) {
            addBomb(new Position(rand.nextInt(width), rand.nextInt(height)));
        }
    }


    public boolean isWon() {                                    //Checks if all cells have been revealed except for bomb locations, returns true if won
        return revealedTotal + bombCount == width*height;
    }


    public void revealAll() {                                   // Reveals all cells in case of victory
        for(int y = 0; y < cells.length; y++) {
            for (int x = 0; x < cells[0].length; x++) {
                cells[x][y].reveal();
            }
        }
    }


    private boolean addBomb(Position position) {                        // Adds bomb at x,y

        if(getCellAt(position).getIsBomb()) return false;               // If already a bomb, returns false


        int minX = Math.max(0,position.x-1);                            // Gets boundaries of cells +/-1 around x,y
        int maxX = Math.min(width-1,position.x+1);
        int minY = Math.max(0,position.y-1);
        int maxY = Math.min(height-1,position.y+1);

        for(int y1 = minY; y1 <= maxY; y1++) {                          // Increase neighbours count for all surrounding cells, telling them they have a bomb in their vicinity
            for (int x1 = minX; x1 <= maxX; x1++) {
                cells[x1][y1].addNeighbour();
            }
        }

        getCellAt(position).setAsBomb();                                //if successful bomb placement, return true
        bombCount++;
        return true;
    }

    public boolean validPosition(Position position)                     //Checks to see if x,y values are within range
    {
        return position.x >= 0 && position.y >= 0 && position.x < width && position.y < height;       // x,y must be between 1 and 10
    }

    private List<Position> floodFillReveal(Position position)               // Flood fills starting at x,y revealing all cells with 0 neighbours that are adjacent. Allows start of game.
    {
        int[][] vis =new int[width][height];                                // Visiting array

        for(int x=0;x<width;x++){                                           // Initiating all to zero
            for(int y=0;y<height;y++){
                vis[x][y]=0;
            }
        }
        List<Position> changedPoints =  new ArrayList<>();                  // List of points that have been flood filled to be returned

        Queue<Position> positionQueue = new LinkedList<>();                 // Creating queue for breadth first search

        positionQueue.add(position);                                        // Adds the selected position as the first to evaluate
        vis[position.x][position.y] = 1;

        while (!positionQueue.isEmpty())                                    // Until queue is empty
        {

            Position positionToReveal = positionQueue.remove();             // Extracting front position from the queue.
            getCellAt(positionToReveal).reveal();
            changedPoints.add(positionToReveal);

            checkFloodFillToCell(new Position(positionToReveal.x+1,positionToReveal.y),vis,positionQueue);           // For upper cell
            checkFloodFillToCell(new Position(positionToReveal.x-1,positionToReveal.y),vis,positionQueue);           // For lower Cell
            checkFloodFillToCell(new Position(positionToReveal.x,positionToReveal.y+1),vis,positionQueue);           // For Right side cell
            checkFloodFillToCell(new Position(positionToReveal.x,positionToReveal.y-1),vis,positionQueue);           // For Left side cell
        }
        return changedPoints;
    }



    private void checkFloodFillToCell(Position position, int[][] vis, Queue<Position> positionQueue) {       // Method to help floodFillReveal. Tests cell at p. If cell is valid coordinate, it has not been visited yet and is a valid element for floodfill to reveal all neighbors with 0 mine proximity
        if (validPosition(position)) {                                                                       // It will add the cell as a new Cell to check its neighbours and mark it as visited.
            if (vis[position.x][position.y] == 0                                                             // vis is the visited matrix indicating the cells that have been checked
                    && !getCellAt(position).getIsRevealed()
                    && getCellAt(position).getNeighbours() == 0) {
                positionQueue.add(position);                                                                 // Queue of cell positions to still check
            }
            vis[position.x][position.y] = 1;
        }
    }


    private List<Position> revealAroundListOfPoints(List<Position> points) {                                // Takes list of points and iterates through all coordinates to perform reveal of all cells  up to 1 unit away from target cell
        List<Position> changedCells = new ArrayList<>();
        for(Position p : points) {                                                                          // 'points' indicates the list of points to reveal borders around
            List<Position> revealedCells = revealAllAroundPoint(p);
            changedCells.addAll(revealedCells);
        }
        return changedCells;                                                                                // returns list of revealed cells
    }


    private List<Position> revealAllAroundPoint(Position position) {                                    // Reveals all cells up to 1 cell away from target position. Skips cells that have 0 neighboring bombs.
        List<Position> changedCells = new ArrayList<>();                                                // The position to reveal a border around.

        int minX = Math.max(0,position.x -1);                                                           // Get bounds of cells +/- 1 around x,y
        int maxX = Math.min(width-1,position.x +1);
        int minY = Math.max(0,position.y -1);
        int maxY = Math.min(height-1,position.y +1);

        for(int y1 = minY; y1 <= maxY; y1++) {                                                          // Iterate through all surrounding cells
            for (int x1 = minX; x1 <= maxX; x1++) {
                if(!cells[x1][y1].getIsRevealed() && cells[x1][y1].getNeighbours()>0) {                 // Not already revealed and not the start of another empty area.
                    changedCells.add(new Position(x1,y1));
                    cells[x1][y1].reveal();
                }
            }
        }
        return changedCells;
    }
}
