package Environment;

/**
 * The SpaceInfo class represents information about a space in the environment.
 */
public class SpaceInfo {
    Coordinate relativeToAvatarCoordinate;
    SpaceType type;

    /**
     * Constructs a SpaceInfo object with the given absolute coordinate and space type.
     * 
     * @param relativeToAvatarCoordinate the absolute coordinate of the space to the avatar
     * @param type the type of the space
     */
    public SpaceInfo(Coordinate relativeToAvatarCoordinate, SpaceType type) {
        this.relativeToAvatarCoordinate = relativeToAvatarCoordinate;
        this.type = type;
    }
    /**
     * Returns the absolute coordinate of the space to the avatar.
     * 
     * @return the absolute coordinate of the space to the avatar
     */
    public Coordinate getRelativeToAvatarCoordinate() {
        return relativeToAvatarCoordinate;
    }
    /**
     * Returns the type of the space.
     * 
     * @return the type of the space
     */
    public SpaceType getType() {
        return type;
    }
}