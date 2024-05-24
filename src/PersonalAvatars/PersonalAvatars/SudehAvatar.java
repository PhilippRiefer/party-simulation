package PersonalAvatars;
/*
 * Author: Soodeh
 * Date: 20240516
 * Version: 0.1
 * 
 * Description: personal avatar class which extends SuperAvatar...
 */

import AvatarInterface.SuperAvatar;
import Environment.Direction;
import Environment.SpaceInfo;
import java.util.ArrayList;

public class SudehAvatar extends SuperAvatar {

    public SudehAvatar(int id, int perceptionRange) {
        super(id, perceptionRange);
        
    }

    // @Override
    // public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {
    //     // TODO implement an algorithem to move around your avatar:
    //     //A possible strategy:
    //     //a loop that iterates over available dorections at enum DIRECTION (UP,RIGHT,DOWN,LEFT,STAY),
    //     //it checks for the free cells
    //     //and then choose a random free direction 
    //     //move there and its location get updated
    //     //and return the selected direction?

    //     //change this to an appropriate return
    //     return Direction.LEFT;
    // }
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
                System.out.println("Sudeh (" + getAvatarID() + ") wants to go left");
                return Direction.LEFT;
            case 1:
                System.out.println("Sudeh (" + getAvatarID() + ") wants to go right");
                return Direction.RIGHT;
            case 2:
                System.out.println("Sudeh (" + getAvatarID() + ") wants to go up");
                return Direction.UP;
            case 3:
                System.out.println("Sudeh (" + getAvatarID() + ") wants to go down");
                return Direction.DOWN;
            default:
                System.out.println("Sudeh (" + getAvatarID() + ") wants to stay");
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
