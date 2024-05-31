package PersonalAvatars;

/********************************************
 * Author: Ole
 * Version: v.4
 * Date:   20240511
 * ------------------------------------------
 * Description: personal avatar of Ole 
 ********************************************/

import java.awt.Color;
import Environment.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import AvatarInterface.SuperAvatar;

public class OleAvatar extends SuperAvatar {
    public OleAvatar(int id, int perceptionRange, Color color) {
        super(id, perceptionRange, color);
    }

    // Set to store visited spaces
    private final Set<String> visitedSpaces = new HashSet<>();
    // HashMap for all spacetypes
    private static final Map<String, Integer> TYPE = new HashMap<>();
    // spacetypes with each ranking
    static {
        TYPE.put("BAR", 0);
        TYPE.put("DANCEFLOOR", 1);
        TYPE.put("EMPTY", 2);
        TYPE.put("DJBOOTH", 3);
        TYPE.put("SEATS", 4);
        TYPE.put("AVATAR", 5);
        TYPE.put("TOILET", 6);
    }

    // My avatar is the next to move.
    // ----------------------------------
    @Override
    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {
        int[] left = checkSpace(spacesInRange, 1); // check cell to the left
        int[] above = checkSpace(spacesInRange, 3); // check cell above
        int[] bottom = checkSpace(spacesInRange, 4); // check cell down
        int[] right = checkSpace(spacesInRange, 6); // check cell to the right
        // save the space of the avatr as visited
        visitedSpace(spacesInRange);
        // merge the arrays
        int[][] directions = { left, above, bottom, right };

        // TODO: check if the place was visited before. If yes -> return stay

        // take cell as minimum
        int minValue = left[0];
        // return the target
        return makeDesicion(directions, minValue);
    }

    // Check the spaces arround the avatar
    // ----------------------------------
    // spaceNumber:
    // 0 -- 3 -- 5
    // 1 -- A -- 6
    // 2 -- 4 -- 7
    private int[] checkSpace(ArrayList<SpaceInfo> spacesInRange, int spaceNumber) {
        SpaceType type; // store the type of space
        String typeAsString; // hold the type of space as a string
        int coordX; // store x coordinate
        int coordY; // store y coordinate
        int[] obstacle = new int[3]; // integer array to store obstacle data
        if (spacesInRange.size() >= 8) {
            SpaceInfo space = spacesInRange.get(spaceNumber); // get the space information
            Coordinate coord = space.getRelativeToAvatarCoordinate(); // get the coordinates relative to the avatar
            coordX = coord.getX(); // get X
            coordY = coord.getY(); // get Y
            type = space.getType(); // get type
            typeAsString = type.name(); // convert to string
            // determine the obstacle based on the type of space and its coordinates
            obstacle = determinateObstacle(typeAsString, coordX, coordY);
            // return the determined obstacle array
            return obstacle;
        } else {
            return obstacle;
        }
    }

    // Determinate wich obstacle is in the way
    // ----------------------------------
    // return array[0] = 0 --> BAR
    // return array[0] = 1 --> DANCEFLOOR
    // return array[0] = 2 --> EMPTY
    // return array[0] = 3 --> DJBOOTH
    // return array[0] = 4 --> SEATS
    // return array[0] = 5 --> AVATAR
    // return array[0] = 6 --> TOILET
    // return array[0] = 7 --> OBSTACLE
    private int[] determinateObstacle(String typeAsString, int coordX, int coordY) {
        int[] array = new int[3];
        array[1] = coordX;
        array[2] = coordY;
        // check the obstacles around the avatar and rank them
        array[0] = TYPE.getOrDefault(typeAsString, 7);
        return array;
    }

    // Make a desicion based on the obstacles
    // ----------------------------------
    private Direction makeDesicion(int[][] directions, int minValue) {
        int minIndex = 0;
        List<Integer> minIndices = new ArrayList<>();
        minIndices.add(0); // start with the first element
        // run through all arrays and find the smallest number in cell [0]
        for (int i = 1; i < directions.length; i++) {
            if (directions[i][0] < minValue) {
                minValue = directions[i][0];
                minIndex = i;
                minIndices.clear(); // clear the list as we found a new minimum
                minIndices.add(i); // add the index of the new minimum
            } else if (directions[i][0] == minValue) {
                minIndices.add(i); // add index if it matches the current minimum
            }
        }
        // check if there are multiple arrays with the same minimum value
        if (minIndices.size() > 1) {
            // IMPORTANT: randomly select one of the indices with the minimum value
            Random random = new Random();
            minIndex = minIndices.get(random.nextInt(minIndices.size()));
        }
        // print target
        String[] arrayNames = { "left", "above", "bottom", "right" };
        System.out.println("The direction: " + arrayNames[minIndex]
                + " has the best ranking: " + minValue);
        // choose the target
        // BAR
        if (minValue == 0) {
            return Direction.STAY;
        } else {
            if (minIndex == 0) {
                return Direction.LEFT;
            } else if (minIndex == 1) {
                return Direction.UP;
            } else if (minIndex == 2) {
                return Direction.DOWN;
            } else {
                return Direction.RIGHT;
            }
        }

        // if (minIndex == 0) {
        //     return Direction.LEFT;
        // } else if (minIndex == 1) {
        //     return Direction.UP;
        // } else if (minIndex == 2) {
        //     return Direction.DOWN;
        // } else {
        //     return Direction.RIGHT;
        // }
    }

    // Store visited space information
    // ----------------------------------
    private void visitedSpace(ArrayList<SpaceInfo> spacesInRange) {
        int leftX; // x coordinate of the cell to the left
        int leftY; // y coordinate of the cell to the left
        int myX; // x coordinate of the avatar
        int myY; // y coordinate of the avatar
        SpaceInfo space = spacesInRange.get(1); // get spaceInfo from the left cell
        Coordinate coord = space.getRelativeToAvatarCoordinate();
        leftX = coord.getX();
        leftY = coord.getY();
        myX = leftX + 1;
        myY = leftY;
        String coordinateKey = myX + "," + myY;
        if (visitedSpaces.add(coordinateKey)) { // add to set and check if it was added for the first time
            System.out.println("Visited space at (" + myX + ", " + myY + ")");
        }
    }

    // Check if the space has already been visited
    // ----------------------------------
    private boolean isVisited(int x, int y) {
        String coordinateKey = x + "," + y;
        for (String visitedSpace : visitedSpaces) {
            if (visitedSpace.startsWith(coordinateKey)) {
                System.out.println("Space at (" + x + ", " + y + ") has already been visited");
                return true; // coordinate has already been visited
            }
        }
        return false; // coordinate has not been visited
    }

    /**
     * Gets the perception range of the avatar.
     *
     * @return the perception range of the avatar
     */
    @Override
    public int getPerceptionRange() {
        return super.getPerceptionRange(); // Assuming SuperAvatar has a method to get the perception range
    }

    /**
     * Sets the perception range of the avatar.
     *
     * @param perceptionRange the new perception range of the avatar
     */
    @Override
    public void setPerceptionRange(int perceptionRange) {
        super.setPerceptionRange(perceptionRange); // Set the perception range via the superclass method
    }
}

// TODO ??
// // filter out visited directions
// List<int[]> unvisitedDirections = new ArrayList<>();
// for (int[] direction : directions) {
// if (!isVisited(direction[1], direction[2])) {
// unvisitedDirections.add(direction);
// }
// }
// // if all directions are visited, stay in place
// if (unvisitedDirections.isEmpty()) {
// System.out.println("All spaces have been visited");
// return Direction.STAY;
// }
// // find the minimum value among unvisited directions
// int minValue = unvisitedDirections.get(0)[0];
// return makeDesicion(unvisitedDirections.toArray(new int[0][0]), minValue);