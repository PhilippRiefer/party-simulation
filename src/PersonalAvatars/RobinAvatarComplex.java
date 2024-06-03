package PersonalAvatars;

import java.util.ArrayList;

import AvatarInterface.SuperAvatar;
import Environment.Coordinate;
import Environment.Direction;
import Environment.SpaceInfo;
import Environment.SpaceType;
import java.awt.Color;

public class RobinAvatarComplex extends SuperAvatar {

    enum State {
        FIND_WALL,
        FOLLOW_WALL,
        FIND_EMPTY,
        MOVE_TO_EMPTY,
        FULLY_EXPLORED
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

    private final int environmentWidth = 40;
    private final int environmentHeight = 20;
    private int[][][] environment;
    private Coordinate position;
    private State state;
    private Direction lastDirection;
    private Direction lastWall;
    private Coordinate destination;
    private boolean destValid;
    private Direction startDirection;
    private Coordinate extPosition;
    private boolean extPositionValid;
    private PersonalFieldType[] PFTValues = PersonalFieldType.values();
    private int uncheckedSpaces;

    public RobinAvatarComplex(int id, int perceptionRange, Color color) {
        super(id, perceptionRange, color);
        environment = new int[2 * environmentWidth + 1][2 * environmentHeight + 1][3];
        position = new Coordinate(environmentWidth, environmentHeight);
        state = State.FIND_WALL;
        lastDirection = Direction.UP;
        destination = new Coordinate(0, 0);
        destValid = false;
        startDirection = Direction.UP;
        extPosition = new Coordinate(0, 0);
        extPositionValid = false;
        uncheckedSpaces = 0;
        initializeEnvironment();
    }

    @Override
    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {
        updatePosition();
        updateEnvironment(spacesInRange);
        // printEnv();
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
                System.out.println("done");
                return Direction.STAY;
        }
    }

    private void initializeEnvironment() {
        for (int row = 0; row < environment[0].length; row++) {
            for (int col = 0; col < environment.length; col++) {
                environment[col][row][2] = 0;
                environment[col][row][1] = PersonalFieldType.UNKNOWN.ordinal();
                environment[col][row][0] = SpaceType.EMPTY.ordinal();
            }
        }
    }

    private void printEnv() {
        for (int row = 0; row < environment[0].length; row++) {
            for (int col = 0; col < environment.length; col++) {
                System.out.print(environment[col][row][1]);
            }
            System.out.print("\n");
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

    private Coordinate subCoordinates(Coordinate coordinate1, Coordinate coordinate2) {
        return new Coordinate(coordinate1.getX() - coordinate2.getX(), coordinate1.getY() - coordinate2.getY());
    }

    private int getFromEnvironment(Coordinate pos, int entry) {
        return environment[pos.getX()][pos.getY()][entry];
    }

    private int getFromEnvironment(Direction dir, int entry) {
        return getFromEnvironment(addCoordinates(position, directionToCoordinate(dir)), entry);
    }

    private void setInEnvironment(Coordinate pos, int entry, int value) {
        environment[pos.getX()][pos.getY()][entry] = value;
    }

    private void setInEnvironment(Direction dir, int entry, int value) {
        setInEnvironment(addCoordinates(position, directionToCoordinate(dir)), entry, value);
    }

    private void findExternalCoordinate(ArrayList<SpaceInfo> spacesInRange) {
        int minX = environmentWidth;
        int maxX = 0;
        int minY = environmentHeight;
        int maxY = 0;
        for (SpaceInfo spaceInfo : spacesInRange) {
            int x = spaceInfo.getRelativeToAvatarCoordinate().getX();
            int y = spaceInfo.getRelativeToAvatarCoordinate().getY();
            if (x < minX)
                minX = x;
            if (x > maxX)
                maxX = x;
            if (y < minY)
                minY = y;
            if (y > maxY)
                maxY = y;
        }
        extPosition = new Coordinate((maxX + minX) / 2, (maxY + minY) / 2);
        extPositionValid = true;
    }

    private Coordinate absToRelPos(Coordinate absPos) {
        return subCoordinates(absPos, extPosition);
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

    private void updateReachable(Coordinate pos) {
        if (!(PFTValues[getFromEnvironment(pos, 1)].isWalkable()))
            return;
        for (Direction dir : Direction.values()) {
            Coordinate spaceAbsPos = addCoordinates(pos, directionToCoordinate(dir));
            if (PFTValues[getFromEnvironment(spaceAbsPos, 1)].isUnknown()) {
                setInEnvironment(spaceAbsPos, 1, PersonalFieldType.REACHABLE.ordinal());
            }
        }
    }

    private void updateEnvironment(ArrayList<SpaceInfo> spacesInRange) {
        if (!extPositionValid)
            findExternalCoordinate(spacesInRange);
        for (SpaceInfo spaceInfo : spacesInRange) {
            Coordinate spaceRelPos = absToRelPos(spaceInfo.getRelativeToAvatarCoordinate());
            Coordinate spaceAbsPos = addCoordinates(position, spaceRelPos);
            if (PFTValues[getFromEnvironment(spaceAbsPos, 1)].isUnknown()) {
                setInEnvironment(spaceAbsPos, 0, spaceInfo.getType().ordinal());
                if (spaceInfo.getType() != SpaceType.OBSTACLE) {
                    setInEnvironment(spaceAbsPos, 1, PersonalFieldType.EMPTY.ordinal());
                } else {
                    setInEnvironment(spaceAbsPos, 1, PersonalFieldType.WALL.ordinal());
                }
                updateReachable(spaceAbsPos);
            }
        }
    }

    private void updatePosition() {
        if (getCouldMove()) {
            position = addCoordinates(position, directionToCoordinate(lastDirection));
            extPosition = addCoordinates(extPosition, directionToCoordinate(lastDirection));
        }
    }

    private Direction findWall() {
        if (getFromEnvironment(startDirection, 1) == PersonalFieldType.WALL.ordinal()) {
            lastWall = startDirection;
            state = State.FOLLOW_WALL;
            return followWall();
        } else {
            lastDirection = startDirection;
            return startDirection;
        }
    }

    private Direction followWall() {
        setInEnvironment(position, 1, PersonalFieldType.WALKED.ordinal());
        for (int i = 0; i < 4; i++) {
            if (getFromEnvironment(rotate90Clkw(lastWall, i), 1) == PersonalFieldType.EMPTY.ordinal()) {
                lastDirection = rotate90Clkw(lastWall, i);
                lastWall = rotate90Clkw(lastWall, 3 + i);
                return lastDirection;
            }
        }
        // printEnv();
        state = State.FIND_EMPTY;
        return findEmpty();
    }

    private Direction findEmpty() {
        printEnv();
        for (int row = 0; row < environment[0].length; row++) {
            for (int col = 0; col < environment.length; col++) {
                if (environment[col][row][1] == PersonalFieldType.REACHABLE.ordinal()) {
                    state = State.MOVE_TO_EMPTY;
                    return moveToEmpty();
                }
            }
        }
        state = State.FULLY_EXPLORED;
        return Direction.STAY;
    }

    private Direction moveToEmpty() {
        System.out.println("move to empty");
        return Direction.STAY;
    }

    private void findPath(int goal, int goalType){
        destValid = false;
        for (int row = 0; row < environment[0].length; row++) {
            for (int col = 0; col < environment.length; col++) {
                environment[col][row][2] = 0;
            }
        }
        setInEnvironment(position, 2, 1);
        if(fillAround(position, 2, goal, goalType)){
            for (int i = 0; i <= environmentHeight * environmentWidth; i++) {
                if(destValid)
                    break;
                boolean keepGoing = false;
                for (int row = 0; row < environment[0].length; row++) {
                    for (int col = 0; col < environment.length; col++) {
                        if(environment[col][row][2] == i)
                            if(fillAround(new Coordinate(col, row), i+1, goal, goalType))
                                keepGoing = true;
                    }
                }
            }
        }


    }

    private boolean fillAround(Coordinate center, int value, int goal, int goalType){
        boolean retVal = false;
        for (int i = 0; i < 4; i++) {
            Coordinate space = addCoordinates(center, directionToCoordinate(rotate90Clkw(Direction.UP, i)));
            if(getFromEnvironment(space, goalType) == goal){
                destination = space;
                return true;
            }
            if(getFromEnvironment(space, 2) == 0 &&  PFTValues[getFromEnvironment(space, 1)].walkable){
                setInEnvironment(space, 2, value);
                retVal = true;
            }
        }
        return retVal;
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