package PersonalAvatars;

import AvatarInterface.*;
import Environment.*;
import java.awt.Color;
import java.util.ArrayList;

/**
 * Represents Philipps personal Avatar, a type of avatar that extends the SuperAvatar class.
 */
public class PhilippAvatar extends SuperAvatar {

    /**
     * Constructs a PhilippAvatar object with the specified ID and perception range.
     *
     * @param id              the ID of the avatar
     * @param perceptionRange the perception range of the avatar
     */
    public PhilippAvatar(int id, int perceptionRange, Color color) {
        super(id, perceptionRange, color); // leverage the super class to handle ID and perceptionRange
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
        return randomDirection();
    }

    /**
     * Generates a random direction for the avatar's turn.
     *
     * @return a random direction
     */
    private Direction randomDirection() {
        int directionNumber = (int) (Math.random() * 4);

        switch (directionNumber) {
            case 0:
                System.out.println("Philipp (" + getAvatarID() + ") wants to go left");
                return Direction.LEFT;
            case 1:
                System.out.println("Philipp (" + getAvatarID() + ") wants to go right");
                return Direction.RIGHT;
            case 2:
                System.out.println("Philipp (" + getAvatarID() + ") wants to go up");
                return Direction.UP;
            case 3:
                System.out.println("Philipp (" + getAvatarID() + ") wants to go down");
                return Direction.DOWN;
            default:
                System.out.println("Philipp (" + getAvatarID() + ") wants to stay");
                return Direction.STAY; // Safety net, though unnecessary as directionNumber is bound by 0-3
        }
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