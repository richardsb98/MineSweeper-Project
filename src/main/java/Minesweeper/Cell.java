package Minesweeper;

public class Cell {                     // Cell class defines if the cell is a bomb, is flagged, is revealed, and how many neighbouring bombs there are nearby.

    /////////////////////////////// Attributes ///////////////////////////////
    private boolean isBomb;             //True if this cell is a bomb and show be avoided when revealing.

    private boolean isRevealed;         //Indicates if the true value of this cell should be shown instead of a mystery.

    private int neighbours;             //Indicates the number of nearby bombs in +/- 1 unit of this cell. It is set by the nearby cells from a addNeighbour() call when a bomb is placed.

    private boolean isFlagged;          //Indicates if the cell is flagged by the player.


    /////////////////////////////// Constructor ///////////////////////////////
    public Cell() {                              // Resets cell back to default with all variables set to 0 or false.
        resetCell();
    }

    /////////////////////////////// Methods ///////////////////////////////
    public void resetCell() {               //Resets cell back to default with all variables set to false.
        isBomb = false;
        isRevealed = false;
        neighbours = 0;
        isFlagged = false;
    }

    public void setAsBomb() {                   // Sets this cell as a bomb.

        isBomb = true;
    }

    public boolean getIsBomb() {                // Gets if this cell is a bomb. Return true if IsBomb

        return isBomb;
    }

    public void reveal() {         // Sets the cell to be revealed.

        isRevealed = true;
    }

    public boolean getIsRevealed() {            // Gets if this cell is revealed, return true if cell is revealed

        return isRevealed;
    }

    public void addNeighbour() {                    // Increases mine proximity count by one. Used when a bomb is placed for neighbors to count

        neighbours++;
    }

    public int getNeighbours() {                 // Gets the number of nearby neighbours. 0 indicates no bombs neighboring

        return neighbours;
    }

    public boolean getIsFlagged() {                 // Gets if cell flagged, returns true if cell is flagged.

        return isFlagged;
    }

    public void toggleIsFlagged() {                   // toggles between isFlagged and not flagged

        isFlagged = !isFlagged;
    }

    public String toString() {                      // defines cells by letters. If cell contains bomb: B. If cell contains flag: F, if unrevealed: *
        if (getIsRevealed()) {
            if (getIsBomb()) {
                return "B";
            } else {
                return "" + neighbours;
            }
        } else if (isFlagged) {
            return "F";
        } else {
            return "*";
        }
    }

}