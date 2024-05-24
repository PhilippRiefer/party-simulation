package PersonalAvatars;

import java.util.ArrayList;

import AvatarInterface.SuperAvatar;
import Environment.Coordinate;
import Environment.Direction;
import Environment.SpaceInfo;
import Environment.SpaceType;

public class RobinAvatarComplex extends SuperAvatar {

    enum State {
        FIND_WALL,
        FOLLOW_WALL,
        FIND_EMPTY,
        MOVE_TO_EMPTY
    }

    enum PersonalFieldType {
        EMPTY(true, false),
        WALKED(true, false),
        WALL(false, false),
        UNKNOWN(false, true),
        REACHABLE(false, true);

        private final boolean walkable;
        private final boolean unknown;

        PersonalFieldType(boolean walkable, boolean unknown) {
            this.walkable = walkable;
            this.unknown = unknown;
        }

        public boolean isWalkable() {
            return walkable;
        }

        public boolean isUnknown() {
            return unknown;
        }

    }

    private final int environmentWidth = 20;
    private final int environmentHeight = 20;
    private Enum[][][] environment;
    private Coordinate position;
    private State state;
    private Direction lastDirection;
    private Direction lastWall;
    private Coordinate destination;
    private Direction startDirection;

    public RobinAvatarComplex(int id) {
        super(id, 1);
        environment = new Enum[2 * environmentWidth + 1][2 * environmentHeight + 1][2];
        position = new Coordinate(environmentWidth, environmentHeight);
        state = State.FIND_WALL;
        lastDirection = Direction.UP;
        destination = new Coordinate(0, 0);
        startDirection = Direction.UP;
    }

    @Override
    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {
        updateEnvironment(spacesInRange);
        switch (state) {
            case FIND_WALL:
                return findWall();
            case FOLLOW_WALL:
                return followWall();
            case FIND_EMPTY:
                return findEmpty();
            case MOVE_TO_EMPTY:
                return moveToEmpty();

            default:
                return Direction.STAY;
        }
    }

    private Direction coordinateToDirection(Coordinate coordinate) {
        if (coordinate.getY() < 0)
            return Direction.UP;
        if (coordinate.getY() > 0)
            return Direction.DOWN;
        if (coordinate.getX() < 0)
            return Direction.LEFT;
        if (coordinate.getX() > 0)
            return Direction.RIGHT;
        return Direction.STAY;
    }

    private Coordinate directionToCoordinate(Direction direction) {
        switch (direction) {
            case UP:
                return new Coordinate(0, -1);
            case RIGHT:
                return new Coordinate(1, 0);
            case DOWN:
                return new Coordinate(0, 1);
            case LEFT:
                return new Coordinate(-1, 0);
            default:
                return new Coordinate(0, 0);
        }
    }

    private Coordinate addCoordinates(Coordinate coordinate1, Coordinate coordinate2) {
        return new Coordinate(coordinate1.getX() + coordinate2.getX(), coordinate1.getY() + coordinate2.getY());
    }

    private Enum getFromEnvironment(Coordinate pos, int entry) {
        return environment[pos.getX()][pos.getY()][entry];
    }

    private Enum getFromEnvironment(Direction dir, int entry){
        return getFromEnvironment(addCoordinates(position, directionToCoordinate(dir)), entry);
    }

    private void setInEnvironment(Coordinate pos, int entry, Enum value) {
        environment[pos.getX()][pos.getY()][entry] = value;
    }

    private void setInEnvironment(Direction dir, int entry, Enum value) {
        setInEnvironment(addCoordinates(position, directionToCoordinate(dir)), entry, value);
    }

    private Direction rotate90Clkw(Direction direction, int times) {
        if (times == 0)
            return direction;
        switch (direction) {

            case UP:
                return rotate90Clkw(Direction.RIGHT, times - 1);
            case RIGHT:
                return rotate90Clkw(Direction.DOWN, times - 1);
            case DOWN:
                return rotate90Clkw(Direction.LEFT, times - 1);
            case LEFT:
                return rotate90Clkw(Direction.UP, times - 1);
            default:
                return Direction.STAY;
        }
    }

    private void updateEnvironment(ArrayList<SpaceInfo> spacesInRange) {
        for (SpaceInfo spaceInfo : spacesInRange) {
            Coordinate spaceRelPos = spaceInfo.getRelativeToAvatarCoordinate();
            Coordinate spaceAbsPos = addCoordinates(position, spaceRelPos);
            if (((PersonalFieldType) getFromEnvironment(spaceAbsPos, 1)).isUnknown()) {
                setInEnvironment(spaceAbsPos, 0, spaceInfo.getType());
                if (spaceInfo.getType() == SpaceType.EMPTY || spaceInfo.getType() == SpaceType.AVATAR) {
                    setInEnvironment(spaceAbsPos, 1, PersonalFieldType.EMPTY);
                } else {
                    setInEnvironment(spaceAbsPos, 1, PersonalFieldType.WALL);
                }
            }
        }
    }

    private Direction findWall() {
        if (getFromEnvironment(startDirection, 1) == PersonalFieldType.WALL) {
            lastWall = startDirection;
            state = State.FOLLOW_WALL;
            return followWall();
        } else {
            return startDirection;
        }
    }

    private Direction followWall() {
        for (int i = 0; i < 4; i++) {
            if(getFromEnvironment(rotate90Clkw(lastWall, i), 1) == PersonalFieldType.EMPTY){
                lastWall = rotate90Clkw(lastWall, 3+i);
                return rotate90Clkw(lastWall, i);
            }
        }
        state = State.FIND_EMPTY;
        return findEmpty();
    }

    private Direction findEmpty() {
        return Direction.STAY;
    }

    private Direction moveToEmpty() {
        return Direction.STAY;
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