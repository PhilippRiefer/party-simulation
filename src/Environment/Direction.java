
package Environment;

/**
 * The Direction enum represents the possible directions in the environment.
 * The directions are UP, RIGHT, DOWN, LEFT, and STAY.
 */
public enum Direction {
    UP,         // RelativeToAvatarCoordinate (0,-1)
    RIGHT,      // RelativeToAvatarCoordinate (1,0)
    DOWN,       // RelativeToAvatarCoordinate (0,1)
    LEFT,       // RelativeToAvatarCoordinate (-1,0)
    STAY        // RelativeToAvatarCoordinate (0,0)
}
