package Environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages the room environment for avatars in a simulation.
 * This includes tracking and managing space occupancy, avatar locations, and
 * room dimensions.
 */
public class Room {

    // Dictionary to store avatar locations by ID.
    private HashMap<Integer, Coordinate> avatarsLocations = new HashMap<>();
    // 2D list representing the type of space at each coordinate.
    private ArrayList<ArrayList<SpaceType>> cellsOccupancy;
    private int numRows;
    private int numCols;

    /**
     * Constructs a Room with specified dimensions.
     * Initializes all spaces to EMPTY.
     *
     * @param numCols the number of columns in the room
     * @param numRows the number of rows in the room
     */
    public Room(int numCols, int numRows) {

        this.numRows = numRows;
        this.numCols = numCols;
        cellsOccupancy = new ArrayList<>(numRows);
        // Initialize the rows
        for (int i = 0; i < numRows; i++) {
            cellsOccupancy.add(new ArrayList<>(numCols));
            // Initialize the columns within each row
            for (int j = 0; j < numCols; j++) {
                cellsOccupancy.get(i).add(SpaceType.EMPTY); // SpaceType.EMPTY is the default value for the moment
            }
        }
    }

    /**
     * Retrieves the space type at a specified coordinate.
     *
     * @param x the x-coordinate (row index)
     * @param y the y-coordinate (column index)
     * @return the SpaceType at the specified coordinate, or null if coordinates are
     *         invalid
     */
    public SpaceType getSpace(Coordinate position) {
        if (isValidCoordinate(position)) {
            return cellsOccupancy.get(position.getX()).get(position.getY());
        } else {
            return null; // Or throw an exception?
        }
    }

    /**
     * Retrieves the coordinates of an avatar by its ID.
     * 
     * @param avatarID the ID of the avatar
     * @return the coordinates of the avatar
     * @throws IllegalArgumentException if the avatar ID is not found
     */
    public Coordinate getAvatarLocation(int avatarID) {
        Coordinate location = avatarsLocations.get(avatarID);
        if (location != null) {
            return location;
        } else {
            throw new IllegalArgumentException("Avatar with ID " + avatarID + " not found.");
        }
    }

    /**
     * Sets the number of rows in the room.
     *
     * @param numRows the new number of rows
     */
    public void setNumRows(int numRows) {
        this.numRows = numRows;
    }

    /**
     * Gets the number of rows in the room.
     *
     * @return the number of rows
     */
    public int getNumRows() {
        return numRows;
    }

    /**
     * Sets the number of columns in the room.
     *
     * @param numCols the new number of columns
     */
    public void setNumCols(int numCols) {
        this.numCols = numCols;
    }

    /**
     * Gets the number of columns in the room.
     *
     * @return the number of columns
     */
    public int getNumCols() {
        return numCols;
    }

    /**
     * Sets the type of space at a specified coordinate.
     *
     * @param x         the x-coordinate (row index)
     * @param y         the y-coordinate (column index)
     * @param spaceType the new space type to set at the coordinate
     * @throws Exception if the coordinates are invalid
     */
    public void setSpace(Coordinate position, SpaceType spaceType) throws Exception {
        if (isValidCoordinate(position)) {
            cellsOccupancy.get(position.getX()).set(position.getY(), spaceType);
        } else {
            throw new Exception("Cannot set space " + spaceType.toString() + " because the coodinate X: "
                    + position.getX() + ", Y: " + position.getY() + " is not valid.");
        }
    }

    /**
     * Checks if the specified coordinates are within the room's bounds.
     *
     * @param x the x-coordinate (row index)
     * @param y the y-coordinate (column index)
     * @return true if the coordinates are valid, false otherwise
     */
    public boolean isValidCoordinate(Coordinate position) {
        int x = position.getX();
        int y = position.getY();
        return x >= 0
                && x < numRows
                && y >= 0
                && y < numCols;
    }

    /**
     * Finds a random unoccupied location in the room for a new avatar.
     *
     * @param avatarID the ID of the avatar to place
     * @return the coordinates where the avatar was placed
     */
    public Coordinate findPlaceForAvatar(int avatarID) {
        Coordinate randomCoordinate;
        do {
            int randomX = (int) (Math.random() * getNumCols());
            int randomY = (int) (Math.random() * getNumRows());
            randomCoordinate = new Coordinate(randomX, randomY);
        } while (isOccupied(randomCoordinate));

        placeAvatar(avatarID, randomCoordinate);
        return randomCoordinate;
    }

    /**
     * Places an avatar at a specified coordinate, updating the room's occupancy
     * data.
     *
     * @param avatarID         the ID of the avatar
     * @param targetCoordinate the coordinate at which to place the avatar
     */
    private void placeAvatar(int avatarID, Coordinate targetCoordinate) {
        // Set the SpaceType of the cell at coordinate targetCoordinate to AVATAR
        cellsOccupancy.get(targetCoordinate.getX()).set(targetCoordinate.getY(), SpaceType.AVATAR);

        // Update HashMap avatarsLocations with key avatarID and value targetCoordinate
        avatarsLocations.put(avatarID, targetCoordinate);
        
    }
    // public boolean placeAvatar(int avatarID, Coordinate targetCoordinate){
    //     if (isValidCoordinate(targetCoordinate)) { // Check if the new position is valid
    //         Coordinate currentPos = avatarsLocations.get(avatarID); // Get current position
    //         if (currentPos != null) {
    //             avatarsLocations.put(avatarID, targetCoordinate); // Update the avatar's position in the model
    //             return true;
    //         }
    //     }

    //     return false;
    // }

    /**
     * Checks if a coordinate is occupied by either an OBSTACLE or another AVATAR.
     *
     * @param coordinate the coordinate to check
     * @return true if the coordinate is occupied, false otherwise
     */
    private boolean isOccupied(Coordinate coordinate) {
        // search the internal 2D array to see what is there inside cell with
        // randomCoordinate
        SpaceType cellInfo = cellsOccupancy.get(coordinate.getX()).get(coordinate.getY());
        // check if it is of SpaceType OBSTACLE or AVATAR
        if (cellInfo == SpaceType.OBSTACLE || cellInfo == SpaceType.AVATAR) {// does the bar not also qualify as an
                                                                             // obstacle? I would not stand on the bar
                                                                             // at a club (depends on alcohol level)
            return true;
        }
        return false;
    }

    /**
     * Retrieves a list of spaces adjacent to a specified avatar's location, within
     * a given perception range.
     *
     * @param avatarID        the ID of the avatar
     * @param perceptionRange the range within which to check for adjacent spaces
     * @return a list of SpaceInfo objects representing the adjacent spaces
     */
    public ArrayList<SpaceInfo> getAdjacentToAvatar(int avatarID, int perceptionRange) {
        ArrayList<SpaceInfo> adjacentToAvatar = new ArrayList<>();

        // Get the avatar's current coordinates from the avatarsLocations map
        Coordinate avatarCoordinate = avatarsLocations.get(avatarID);
        if (avatarCoordinate == null) {
            return adjacentToAvatar; // Return empty list if the avatar's coordinate is not found
        }

        int currentX = avatarCoordinate.getX();
        int currentY = avatarCoordinate.getY();

        // Calculate the coordinates of the adjacent cells within the perception range
        for (int i = currentX - perceptionRange; i <= currentX + perceptionRange; i++) {
            for (int j = currentY - perceptionRange; j <= currentY + perceptionRange; j++) {
                if (i == currentX && j == currentY) {
                    continue; // Skip the current cell (avatar's position)
                }
                if (isValidCoordinate(new Coordinate(i, j))) {
                    SpaceType space = cellsOccupancy.get(i).get(j);
                    adjacentToAvatar.add(new SpaceInfo(new Coordinate(i, j), space));
                }
            }
        }

        return adjacentToAvatar; // Return the list of SpaceInfo for adjacent spaces
    }

    /**
     * Updates the dimensions of the room and adjusts the occupancy accordingly.
     * If the new dimensions are smaller than the current dimensions, avatars are
     * relocated to fit within the new boundaries.
     * If the new dimensions are larger than the current dimensions, empty spaces
     * are added to accommodate the new size.
     *
     * @param newNumCols the new number of columns in the room
     * @param newNumRows the new number of rows in the room
     */
    public void updateRoom(int newNumCols, int newNumRows) {
        // Handle downsizing
        if (newNumCols < numCols || newNumRows < numRows) {
            relocateAvatarsOutOfBound(newNumCols, newNumRows);
        }

        // Update rows
        while (cellsOccupancy.size() > newNumRows) {
            cellsOccupancy.remove(cellsOccupancy.size() - 1);
        }
        while (cellsOccupancy.size() < newNumRows) {
            ArrayList<SpaceType> newRow = new ArrayList<>();
            for (int i = 0; i < numCols; i++) {
                newRow.add(SpaceType.EMPTY);
            }
            cellsOccupancy.add(newRow);
        }

        // Update columns
        for (ArrayList<SpaceType> row : cellsOccupancy) {
            while (row.size() > newNumCols) {
                row.remove(row.size() - 1);
            }
            while (row.size() < newNumCols) {
                row.add(SpaceType.EMPTY);
            }
        }

        // Update class fields
        numCols = newNumCols;
        numRows = newNumRows;
    }

    /**
     * Relocates avatars that are out of bounds in the room.
     * If an avatar's coordinate is outside the specified number of columns or rows,
     * it is removed from the avatarsLocations map and a new place is found for it.
     *
     * @param newNumCols the new number of columns in the room
     * @param newNumRows the new number of rows in the room
     */
    private void relocateAvatarsOutOfBound(int newNumCols, int newNumRows) {
        ArrayList<Integer> avatarsToRemove = new ArrayList<>();
        for (Map.Entry<Integer, Coordinate> entry : avatarsLocations.entrySet()) {
            Coordinate coord = entry.getValue();
            if (coord.getX() >= newNumCols || coord.getY() >= newNumRows) {
                avatarsToRemove.add(entry.getKey());
            }
        }

        for (Integer avatarID : avatarsToRemove) {
            avatarsLocations.remove(avatarID);
            findPlaceForAvatar(avatarID);
        }
    }

    
    /**
     * Tries to place an avatar in the room at the specified position.
     * 
     * @param avatarID the ID of the avatar to be placed
     * @param newPos the new position where the avatar should be placed
     * @return true if the avatar was successfully placed, false otherwise
     */
    public boolean tryToPlaceAvatar(int avatarID, Coordinate newPos) {
        if (isValidCoordinate(newPos) &&
                isOccupied(newPos) == false) {
            placeAvatar(avatarID, newPos);
            return true;
        }

        return false;
    }
}