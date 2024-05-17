package PersonalAvatars;

import java.util.ArrayList;

import AvatarInterface.SuperAvatar;
import Environment.Direction;
import Environment.SpaceInfo;

public class MaximSpockAvatar extends SuperAvatar {
     


    // perception Range should be higher than of a human!
    public MaximSpockAvatar(int id, int perceptionRange) {
        super(id, perceptionRange);
    }

    /**
    * Determines the direction for the avatar's next turn based on the spaces within its perception range.
    *
    * @return the direction for the avatar's next turn
    */
    @Override
    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {
        // If no logical Direction is available - move in any Direction
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
