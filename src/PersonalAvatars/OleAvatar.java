package PersonalAvatars;

/********************************************
 * Author: Ole
 * Version: v.1
 * Date:   20240511
 * ------------------------------------------
 * Description: personal avatar of Ole 
 ********************************************/

 import java.awt.Color;
import Environment.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;

import AvatarInterface.SuperAvatar;

public class OleAvatar extends SuperAvatar {
    public OleAvatar(int id, int perceptionRange, Color color) {
        super(id, perceptionRange, color);
    }
    // create a border for the avatar
    private static final int MIN_BORDER_X = 0;
    private static final int MIN_BORDER_Y = 0;
    private static final int MAX_BORDER_X = 39;
    private static final int MAX_BORDER_Y = 19;
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
        // merge the arrays
        int[][] directions = { left, above, bottom, right };
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
    // return array[0] = 8 --> Wall
    private int[] determinateObstacle(String typeAsString, int coordX, int coordY) {
        int[] array = new int[3];
        array[1] = coordX;
        array[2] = coordY;
        // check the obstacles around the avatar and rank them 
        array[0] = TYPE.getOrDefault(typeAsString, 7);
        // if the avatar is out of bounds -> give bad ranking
        if (isOutOfBounds(coordX, coordY)) {
            array[0] = 8;
        }
        return array;
    }
    // check if the avatar is out of bounds
    // ----------------------------------
    private boolean isOutOfBounds(int x, int y) {
        return x <= MIN_BORDER_X || x >= MAX_BORDER_X || y <= MIN_BORDER_Y || y >= MAX_BORDER_Y;
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
            System.out.println("Multiple directions have the same minimum value: " + minValue);
            for (int index : minIndices) {
                System.out.println("Direction index: " + index);
            }
            // IMPORTANT: randomly select one of the indices with the minimum value
            Random random = new Random();
            minIndex = minIndices.get(random.nextInt(minIndices.size()));
        }
        // print target
        String[] arrayNames = { "left", "above", "bottom", "right" };
        System.out.println("in the direction: " + arrayNames[minIndex]
                + " has the best ranking: " + minValue);
        // choose the target
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
    }

    // NOT USED
    // --------------------------------------------------------------------------
    // generate a random desicion
    // ==================================
    // private Direction randomDesicion() {
    // int random = (int) (Math.random() * 4);
    // switch (random) {
    // case 0:
    // System.out.println("(ID: " + getAvatarID() + ") Ole -> left");
    // return Direction.LEFT;
    // case 1:
    // System.out.println("(ID: " + getAvatarID() + ") Ole -> right");
    // return Direction.RIGHT;
    // case 2:
    // System.out.println("(ID: " + getAvatarID() + ") Ole -> up");
    // return Direction.UP;
    // case 3:
    // System.out.println("(ID: " + getAvatarID() + ") Ole -> down");
    // return Direction.DOWN;
    // default:
    // System.out.println("(ID: " + getAvatarID() + ") Ole -> stay");
    // return Direction.STAY;
    // }
    // }

    // // check wich coordinates are mine
    // // ==================================
    // private int[] determineMyCoordinates(ArrayList<SpaceInfo> spacesInRange) {
    // int[] knownX = new int[8];
    // int[] knownY = new int[8];
    // int[] myCoordinates = new int[2];
    // int i = 0;
    // for (SpaceInfo space : spacesInRange) {
    // Coordinate relativeCoord = space.getRelativeToAvatarCoordinate();
    // knownX[i] = relativeCoord.getX();
    // knownY[i] = relativeCoord.getY();
    // ++i;
    // }
    // int avatarX = knownX[1] + 1;
    // int avatarY = knownY[1];
    // myCoordinates[0] = avatarX;
    // myCoordinates[1] = avatarY;
    // return myCoordinates;
    // }

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