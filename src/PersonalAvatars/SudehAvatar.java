package PersonalAvatars;

import AvatarInterface.SuperAvatar;
import Environment.Coordinate;
import Environment.Direction;
import Environment.SpaceInfo;
import Environment.SpaceType;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class SudehAvatar extends SuperAvatar {

    private Random random;
    private int stayCounter;

    public SudehAvatar(int id, int perceptionRange, Color color) {
        super(id, perceptionRange, color); // leverage the super class to handle ID and perceptionRange
        this.random = new Random();
        this.stayCounter = 0;
    }

    @Override
    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {
        if (stayCounter > 0) {
            stayCounter--;
            if (stayCounter == 0) {
                System.out.println("Stay duration over. Resuming movement.");
                // Reset stayCounter and switch to movement mode
                stayCounter = 0;
                return randomDirection();
            } else {
                System.out.println("Staying in place for " + stayCounter + " turns.");
            }
            return Direction.STAY;
        }
        setAvatarColor(new Color(0, 77, 64));

        // Check the spaces in range and decide what to do
        for (SpaceInfo space : spacesInRange) {
            SpaceType type = space.getType();
            switch (type) {
                case EMPTY:
                    return getDirectionFromCoordinate(space.getRelativeToAvatarCoordinate());

                case OBSTACLE:
                    System.out.println("Found an obstacle, skipping.");
                    continue;

                case AVATAR:
                    setAvatarColor(new Color(215, 204, 200 ));
                    stayCounter = getStayDuration(type);

                case DANCEFLOOR:
                    setAvatarColor(new Color(128, 203, 196));
                    stayCounter = getStayDuration(type);
                    return getDirectionFromCoordinate(space.getRelativeToAvatarCoordinate());

                case DJBOOTH:
                    setAvatarColor(new Color(128, 203, 196));
                    stayCounter = getStayDuration(type);
                    return getDirectionFromCoordinate(space.getRelativeToAvatarCoordinate());

                case TOILET:
                    setAvatarColor(new Color(128, 203, 196));
                    stayCounter = getStayDuration(type);
                    return getDirectionFromCoordinate(space.getRelativeToAvatarCoordinate());

                case BAR:
                    setAvatarColor(new Color(128, 203, 196));
                    stayCounter = getStayDuration(type);
                    return getDirectionFromCoordinate(space.getRelativeToAvatarCoordinate());

                case SEATS:
                    setAvatarColor(new Color(128, 203, 196));
                    stayCounter = getStayDuration(type);
                    return getDirectionFromCoordinate(space.getRelativeToAvatarCoordinate());

                default:
                    System.out.println("Unknown space type, skipping.");
                    continue;
            }
        }
        // If no suitable action was found, move randomly
        Direction randomDirection = randomDirection();
        System.out.println("Moving randomly in direction: " + randomDirection);

        // If no suitable action was found, move randomly
        return randomDirection();
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
        int directionNumber = random.nextInt(4); // Random number between 0 and 3

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
     * Determines the number of turns to stay in the current space type.
     *
     * @param type the space type
     * @return the number of turns to stay
     */
    private int getStayDuration(SpaceType type) {
        switch (type) {
            case AVATAR:
                return 2; // Stay for .. turns
            case DANCEFLOOR:
                return 50; // Stay for .. turns
            case DJBOOTH:
                return 50; // Stay for .. turns
            case TOILET:
                return 50; // Stay for .. turns
            case BAR:
                return 50; // Stay for .. turns
            case SEATS:
                return 50; // Stay for .. turns
            default:
                return 0;
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
