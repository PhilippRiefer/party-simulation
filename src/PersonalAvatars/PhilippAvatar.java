package PersonalAvatars;

import AvatarInterface.*;
import Environment.*;
import java.awt.Color;
import java.util.ArrayList;

/**
 * Represents Philipps personal Avatar, a type of avatar that extends the SuperAvatar class.
 */
public class PhilippAvatar extends SuperAvatar {

    private ArrayList<SpaceInfo> spaces;
    private Color color;
    private int perceptionRange;

    /**
     * Constructs a PhilippAvatar object with the specified ID and perception range.
     *
     * @param id              the ID of the avatar
     * @param perceptionRange the perception range of the avatar
     * @param color           the color of the avatar
     */
    public PhilippAvatar(int id, int perceptionRange, Color color) {
        super(id, perceptionRange, color); // leverage the super class to handle ID, perceptionRange and color
    }

    /**
     * Determines the direction for the avatar's turn based on the spaces in range.
     *
     * @param spacesInRange the list of spaces within the perception range
     * @return the direction for the avatar's turn
     */
    @Override
    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {
        // Implement a more sophisticated strategy using spacesInRange
        // For now, let's continue to move randomly as a placeholder
        return decideMove(spacesInRange);
    }

    /**
     * Generates a random direction for the avatar's turn.
     *
     * @return a random direction
     */
    private Direction randomDirection() {
        int directionNumber = (int) (Math.random() * 4);
        System.out.println("Philipp (" + getAvatarID() + ") is using a random direction");
        switch (directionNumber) {
            case 0 -> {
                return moveUp();
            }
            case 1 -> {
                return moveDown();
            }
            case 2 -> {
                return moveLeft();
            }
            case 3 -> {
                return moveRight();
            }
            default -> {
                return stay(); // Safety net, though unnecessary as directionNumber is bound by 0-3
            }
        }
    }

    private Direction decideMove(ArrayList<SpaceInfo> spacesInRange) {
        System.out.println("Philipp (" + getAvatarID() + ") is deciding move");
        
        setPerceptionRange(4);
        System.out.println("Philipp (" + getAvatarID() + ") has perception range: " + getPerceptionRange());

        addSpaceInfos(spacesInRange);
        System.out.println("Philipp (" + getAvatarID() + ") added SpaceInfos to this.spaces");
        System.out.println("Philipp (" + getAvatarID() + ") sees this many spaces: " + spacesInRange.size());

        // For now, let's continue to move randomly as a placeholder
        return randomDirection();
    }

    public static int calculateTheoreticallyVisibleFields(int perceptionRange) {
        int sideLength = 2 * perceptionRange + 1;
        int totalFields = sideLength * sideLength;
        int visibleFields = totalFields - 1; // Subtracting the field the character stands on

        return visibleFields;
    }

    private Direction findUpperLeftCorner(ArrayList<SpaceInfo> spacesInRange) {
        System.out.println("Philipp (" + getAvatarID() + ") wants to go to the upper left corner");
        for (SpaceInfo spaceInfo : spacesInRange) {
            // this.spaces = spacesInRange;
        }
        return randomDirection();
    }

    private ArrayList<SpaceInfo> addSpaceInfos(ArrayList<SpaceInfo> spacesInRange) {
        // TODO correct saving of spacesInRange in spaces
        return spacesInRange;
    }

    private Direction moveUp() {
        System.out.println("Philipp (" + getAvatarID() + ") wants to go up");
        return Direction.UP;
    }

    private Direction moveDown() {
        System.out.println("Philipp (" + getAvatarID() + ") wants to go down");
        return Direction.DOWN;
    }

    private Direction moveLeft() {
        System.out.println("Philipp (" + getAvatarID() + ") wants to go left");
        return Direction.LEFT;
    }

    private Direction moveRight() {
        System.out.println("Philipp (" + getAvatarID() + ") wants to go right");
        return Direction.RIGHT;
    }

    private Direction stay() {
        System.out.println("Philipp (" + getAvatarID() + ") wants to stay");
        return Direction.STAY;
    }

}