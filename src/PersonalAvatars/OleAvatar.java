package PersonalAvatars;

/********************************************
 * Author: Ole
 * Version: v.1
 * Date:   20240511
 * ------------------------------------------
 * Description: personal avatar of Ole 
 ********************************************/

import Environment.*;
import java.util.ArrayList;
import AvatarInterface.SuperAvatar;

public class OleAvatar extends SuperAvatar {

    // Coordinates
    // ==================================
    Coordinate Up = new Coordinate(0, -1);
    Coordinate Right = new Coordinate(1, 0);
    Coordinate Down = new Coordinate(0, 1);
    Coordinate Left = new Coordinate(-1, 0);

    // Object with personal avatar id and perception range of the avatar
    // ==================================
    public OleAvatar(int id, int perceptionRange) {
        super(id, perceptionRange);
    }

    // My avatar is the next to move.
    // ----------------------------------
    // 1. get an idea of wich objects are around and rank them
    // (lookArroundAndRank())
    // 2. try to get the cell of the wanted target (makeDesicion())
    // 3. return -> direction (for now randomDesicion())
    // ==================================
    @Override
    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {

        // Step 1.
        int[] array = lookArroundAndRank(spacesInRange);
        // Step 2.
        //makeDesicion(array);
        // Step 3.
        // Direction move = goInTargetedDirection(target);
        // return move;

        return randomDesicion();
    }

    // Step 1.
    // ----------------------------------
    // get a hang of wich objects are around the avatar
    // - wich object in wich direction?
    // - direction is important
    // EMPTY, -> 1
    // OBSTACLE, -> 8
    // AVATAR, -> 7 (2)
    // DANCEFLOOR, -> 5
    // DJBOOTH, -> 3
    // TOILET, -> 6
    // BAR -> 2
    // SEATS -> 4
    // ==================================
    // Array mit 3 werten zurückgeben
    // 1: type
    // 2: X
    // 3: Y
    private int[] lookArroundAndRank(ArrayList<SpaceInfo> spacesInRange) {
        // Get each Spacetype with the belonging X and Y
        SpaceType type;
        String typeAsString;
        int[] array = new int[3];
        int coordX;
        int coordY;

        for (SpaceInfo space : spacesInRange) {
            Coordinate coord = space.getRelativeToAvatarCoordinate();
            coordX = coord.getX();
            coordY = coord.getY();
            type = space.getType();
            typeAsString = type.name();

            if (typeAsString == "EMPTY") {
                System.out.println("Space is Empty, i can go there.");
                array[0] = 1;
                array[1] = coordX;
                array[2] = coordY;
                return array;
            } else if (typeAsString == "AVATAR") {
                System.out.println("Space is blocked by an avatar, i can't go there.");
                array[0] = 2;
                array[1] = coordX;
                array[2] = coordY;
            }
        }
        return array;
    }

    // Step 2.
    // ----------------------------------
    // on the basis of ranked, try to find the cell with the wanted target
    // ==================================
    private int makeDesicion(int[] array) {

        
        return 49;
    }

    // Step 2. and 3.
    // ----------------------------------
    // generate a random desicion
    // only for testing
    // ==================================
    private Direction randomDesicion() {
        int random = (int) (Math.random() * 4);
        switch (random) {
            case 0:
                System.out.println("(ID: " + getAvatarID() + ") Ole -> left");
                return Direction.LEFT;
            case 1:
                System.out.println("(ID: " + getAvatarID() + ") Ole -> right");
                return Direction.RIGHT;
            case 2:
                System.out.println("(ID: " + getAvatarID() + ") Ole -> up");
                return Direction.UP;
            case 3:
                System.out.println("(ID: " + getAvatarID() + ") Ole -> down");
                return Direction.DOWN;
            default:
                System.out.println("(ID: " + getAvatarID() + ") Ole -> stay");
                return Direction.STAY;
        }
    }

    // Step 3.
    // ----------------------------------
    // Move the Avatar in the targeted Direction.
    // ==================================
    private Direction goInTargetedDirection() {
        return null;
    }

    // check the space in the avatars range and make a ranking
    // 1: BAR, 2: EMPTY, 3: DJBOOTH, 4: SEATS,
    // 5: DANCEFLOOR, 6: TOILET, 7: AVATAR, 8: OBSTACLE
    // ------------------------------------
    // @SuppressWarnings("unlikely-arg-type")
    // private int checkSpace(ArrayList<SpaceInfo> spacesInRange) {
    // if (spacesInRange.equals("EMPTY")) {
    // System.out.println("Desired place is empty. I would like to move there.");
    // return 2;
    // } else if (spacesInRange.equals("OBSTACLE")) {
    // System.out.println("There is an obstacle in the targeted area. I cannot move
    // there.");
    // return 8;
    // } else if (spacesInRange.equals("AVATAR")) {
    // System.out.println("There is an other avatar in the targeted area. I cannot
    // move there.");
    // return 7;
    // } else if (spacesInRange.equals("DANCEFLOOR")) {
    // System.out.println("It looks as if there is a dance floor there. Get out of
    // here quickly.");
    // return 5;
    // } else if (spacesInRange.equals("DJBOOTH")) {
    // System.out.println("The music gets louder. There must be a DJ here.");
    // return 3;
    // } else if (spacesInRange.equals("TOILET")) {
    // System.out.println("No, I don't have to yet.");
    // return 6;
    // } else if (spacesInRange.equals("BAR")) {
    // System.out.println("Yes, I like it here.");
    // return 1;
    // } else if (spacesInRange.equals("SEATS")) {
    // System.out.println("My legs are tired, i have to sit.");
    // return 4;
    // } else{
    // return 9;
    // }
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