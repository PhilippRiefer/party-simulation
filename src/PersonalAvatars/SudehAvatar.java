package PersonalAvatars;

import AvatarInterface.SuperAvatar;
import Environment.Coordinate;
import Environment.Direction;
import Environment.SpaceInfo;
import Environment.SpaceType;

import java.awt.Color;
import java.util.ArrayList;


public class SudehAvatar extends SuperAvatar {

    

    public SudehAvatar(int id, int perceptionRange, Color color) {
        super(id, perceptionRange, color); // leverage the super class to handle ID and perceptionRange
        
    }
    public static void wait(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {
    
        setAvatarColor(Color.GRAY);

        // Check the spaces in range and decide what to do
        for (SpaceInfo space : spacesInRange) {
            SpaceType type = space.getType();
            Coordinate coordinate = space.getRelativeToAvatarCoordinate();
            System.out.println("Checking space at " + coordinate + " with type " + type);
            switch (type) {
               
                case OBSTACLE:
                    continue;

                // case AVATAR:
                // setAvatarColor(Color.ORANGE);
                //wait(100);

                case DANCEFLOOR:
                    setAvatarColor(Color.MAGENTA);
                    //System.out.println("***************DANCEFLOOR!");
                    wait(100);
                    return getDirectionFromCoordinate(space.getRelativeToAvatarCoordinate());

                case DJBOOTH:
                    wait(100);
                    setAvatarColor(Color.BLUE);
                    //System.out.println("*********************DJBOOTH!");
                    //wait(100);
                    return getDirectionFromCoordinate(space.getRelativeToAvatarCoordinate());

                case TOILET:
                    //System.out.println("*******************TOILET!");
                    setAvatarColor(Color.ORANGE);
                    wait(1000);
                    return getDirectionFromCoordinate(space.getRelativeToAvatarCoordinate());

                case BAR:
                    //System.out.println("***************BAR!");
                    setAvatarColor(Color.RED);
                    wait(100);
                    return getDirectionFromCoordinate(space.getRelativeToAvatarCoordinate());

                case SEATS:
                    //System.out.println("***************SEATS area!");
                    setAvatarColor(Color.PINK);
                    wait(500);
                    return getDirectionFromCoordinate(space.getRelativeToAvatarCoordinate());

                default:
                    //System.out.println("Unknown space type, skipping.");
                    continue;
            }
        }
        
        // If no suitable action was found, move randomly
        Direction randomDirection = randomDirection();
        System.out.println("Moving randomly in direction: " + randomDirection);
        return randomDirection;
    }

    /**
     * Utility method to get the direction based on the relative coordinate.
     *
     * @param coordinate the relative coordinate
     * @return the direction to move
     */
    private Direction getDirectionFromCoordinate(Coordinate coordinate) {
        int x = coordinate.getX();
        int y = coordinate.getY();

        if (x == 1) {
            return Direction.RIGHT;
        } else if (x == - 1) {
            return Direction.LEFT;
        } else if (y == 1) {
            return Direction.DOWN;
        } else if (y == - 1) {
            return Direction.UP;
        }

        return randomDirection();
    }

    /**
     * Generates a random direction for the avatar's turn.
     *
     * @return a random direction
     */
    private Direction randomDirection() {
        int directionNumber = (int) (Math.random() * 5);

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
                return Direction.STAY; 
        }
    }

    
    @Override
    public int getPerceptionRange() {
        return super.getPerceptionRange(); // Assuming SuperAvatar has a method to get the perception range
    }

    @Override
    public void setPerceptionRange(int perceptionRange) {
        super.setPerceptionRange(perceptionRange); // Set the perception range via the superclass method
    }
}
