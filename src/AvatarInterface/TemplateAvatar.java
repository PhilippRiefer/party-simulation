package AvatarInterface;

import java.util.ArrayList;
import Environment.*;

/**
 * This class represents a template avatar that extends the SuperAvatar class and implements the AvatarInterface.
 * It provides a basic implementation of the avatar's behavior and perception range.
 */
public class TemplateAvatar extends SuperAvatar { // implements AvatarInterface

    /**
     * Constructs a TemplateAvatar object with the specified ID and perception range.
     *
     * @param id              the ID of the avatar
     * @param perceptionRange the perception range of the avatar
     */
    public TemplateAvatar(int id, int perceptionRange) {
        super(id, perceptionRange);
    }

    /**
     * Determines the direction for the avatar's next turn based on the spaces within its perception range.
     * This method can be overridden to implement a more sophisticated strategy.
     *
     * @param spacesInRange the list of spaces within the avatar's perception range
     * @return the direction for the avatar's next turn
     */
    @Override
    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {
        // Implement a more sophisticated strategy using spacesInRange
        // For now, let's continue to move randomly as a placeholder
        return randomDirection();
    }

    /**
     * Generates a random direction for the avatar to move.
     *
     * @return a random direction
     */
    private Direction randomDirection() {
        int directionNumber = (int) (Math.random() * 4);

        switch (directionNumber) {
            case 0:
                return Direction.LEFT;
            case 1:
                return Direction.RIGHT;
            case 2:
                return Direction.UP;
            case 3:
                return Direction.DOWN;
            default:
                return Direction.STAY; // Safety net, though unnecessary as directionNumber is bound by 0-3
        }
    }

    /**
     * Returns the perception range of the avatar.
     *
     * @return the perception range
     */
    @Override
    public int getPerceptionRange() {
        return super.getPerceptionRange(); // Assuming SuperAvatar has a method to get the perception range
    }

    /**
     * Sets the perception range of the avatar.
     *
     * @param perceptionRange the perception range to set
     */
    @Override
    public void setPerceptionRange(int perceptionRange) {
        super.setPerceptionRange(perceptionRange); // Set the perception range via the superclass method
    }
}
