package PersonalAvatars;

import AvatarInterface.*;
import Environment.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents Philipps personal Avatar, a type of avatar that extends the SuperAvatar class.
 */
public class PhilippAvatar extends SuperAvatar {

    private Map<Point, SpaceInfo> memory;
    private Color color;
    private int perceptionRange;
    private boolean initialPositionKnown;
    private int x, y; // Current position of the avatar
    private Point target; // Target position for navigation

    private int movesCount;
    private int sobriety;
    private int bladder;
    private boolean atBar;
    private boolean atToilet;
    private int stayCount;

    /**
     * Constructs a PhilippAvatar object with the specified ID and perception range.
     *
     * @param id              the ID of the avatar
     * @param perceptionRange the perception range of the avatar
     * @param color           the color of the avatar
     */
    public PhilippAvatar(int id, int perceptionRange, Color color) {
        super(id, perceptionRange, color); // leverage the super class to handle ID, perceptionRange and color
        this.memory = new HashMap<>();
        this.initialPositionKnown = false; // Initial position is unknown
        this.target = null; // No target initially

        this.movesCount = 0;
        this.sobriety = 0;
        this.bladder = 0;
        this.atBar = false;
        this.atToilet = false;
        this.stayCount = 0;
    }

    /**
     * Determines the direction for the avatar's turn based on the spaces in range.
     *
     * @param spacesInRange the list of spaces within the perception range
     * @return the direction for the avatar's turn
     */
    @Override
    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {
        // Establish initial position if unknown
        if (!initialPositionKnown) {
            determineInitialPosition(spacesInRange);
        }

        // Update the avatar's memory with the new perception data
        updateMemory(spacesInRange);

        // Handle stats and states
        handleStats();

        // Print stats
        //System.out.println("Perception range: " + perceptionRange);
        //System.out.println("Sobriety: " + sobriety);
        //System.out.println("Bladder: " + bladder);

        // Implement a more sophisticated strategy using spacesInRange
        // For now, let's move towards the target using a simple pathfinding algorithm
        return decideMove();
    }

    /**
     * Determines the initial position of the avatar using the perception data.
     *
     * @param spacesInRange the list of spaces within the perception range
     */
    private void determineInitialPosition(ArrayList<SpaceInfo> spacesInRange) {
        // Determine the initial position based on relative coordinates of known spaces
        if (!spacesInRange.isEmpty()) {
            SpaceInfo firstSpace = spacesInRange.get(0);
            Coordinate relativeCoord = firstSpace.getRelativeToAvatarCoordinate();
            // Assuming the avatar is at (0, 0) initially
            this.x = -relativeCoord.getX();
            this.y = -relativeCoord.getY();
            this.initialPositionKnown = true;

            //System.out.println("Initial position determined: (" + x + ", " + y + ")");
        } else {
            //System.out.println("No spaces in range to determine initial position.");
        }
    }

    /**
     * Updates the avatar's memory with the new perception data.
     *
     * @param spacesInRange the list of spaces within the perception range
     */
    private void updateMemory(ArrayList<SpaceInfo> spacesInRange) {
        int perceptionRange = getPerceptionRange();
        int index = 0;
        for (int i = -perceptionRange; i <= perceptionRange; i++) {
            for (int j = -perceptionRange; j <= perceptionRange; j++) {
                if (i == 0 && j == 0) continue; // Skip the avatar's own position
                int relativeX = x + i;
                int relativeY = y + j;
                if (index < spacesInRange.size()) {
                    SpaceInfo spaceInfo = spacesInRange.get(index);
                    Point point = new Point(relativeX, relativeY);
                    memory.put(point, spaceInfo);
                    index++;
                } else {
                    Point point = new Point(relativeX, relativeY);
                    memory.put(point, new SpaceInfo(new Coordinate(relativeX, relativeY), SpaceType.EMPTY)); // Default to EMPTY if data is missing
                }
            }
        }

        // Print memory for debugging
        //System.out.println("Updated memory:");
        for (Map.Entry<Point, SpaceInfo> entry : memory.entrySet()) {
            Point point = entry.getKey();
            SpaceInfo info = entry.getValue();
            //System.out.println("Point: (" + point.x + ", " + point.y + ") - Type: " + info.getType());
        }
    }

    /**
     * Sets a target position based on the memory. For example, navigate towards a BAR.
     */
    private void setTarget() {
        if (bladder >= 20) {
            for (Map.Entry<Point, SpaceInfo> entry : memory.entrySet()) {
                if (entry.getValue().getType() == SpaceType.TOILET) {
                    target = entry.getKey();
                    //System.out.println("Target set to toilet: (" + target.x + ", " + target.y + ")");
                    break;
                }
            }
        } else {
            for (Map.Entry<Point, SpaceInfo> entry : memory.entrySet()) {
                if (entry.getValue().getType() == SpaceType.BAR) {
                    target = entry.getKey();
                    //System.out.println("Target set to bar: (" + target.x + ", " + target.y + ")");
                    break;
                }
            }
        }
    }

    /**
     * Decides the next move for the avatar.
     *
     * @return the direction for the avatar's turn
     */
    private Direction decideMove() {
        //System.out.println("Philipp (" + getAvatarID() + ") is deciding move");

        if (atBar || atToilet) {
            return stay();
        }

        if (target == null) {
            setTarget();
        }

        if (target == null) {
            //System.out.println("No target set. Moving randomly.");
            return randomDirection();
        }

        int dx = target.x - x;
        int dy = target.y - y;

        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0 && canMove(x + 1, y)) return moveRight();
            if (dx < 0 && canMove(x - 1, y)) return moveLeft();
        } else {
            if (dy > 0 && canMove(x, y + 1)) return moveDown();
            if (dy < 0 && canMove(x, y - 1)) return moveUp();
        }

        return randomDirection();
    }

    /**
     * Attempts to move in the given direction and verifies if the move was successful.
     *
     * @param move the direction to move
     * @return true if the move was successful, false otherwise
     */
    private boolean attemptMove(Direction move) {
        int newX = x, newY = y;

        switch (move) {
            case UP -> newY -= 1;
            case DOWN -> newY += 1;
            case LEFT -> newX -= 1;
            case RIGHT -> newX += 1;
            case STAY -> {
                return true; // No move, so always successful
            }
        }

        //System.out.println("Attempting move to: (" + newX + ", " + newY + ")");

        if (!canMove(newX, newY)) {
            return false;
        }

        // Simulate the move and get new perception data
        ArrayList<SpaceInfo> newPerceptionData = lookAround(newX, newY, getPerceptionRange());
        //System.out.println("New perception data size: " + newPerceptionData.size());
        updateMemory(newPerceptionData);

        // Check if the avatar has moved by comparing new perception data
        boolean moved = hasMoved(newX, newY, newPerceptionData);
        //System.out.println("Move " + (moved ? "successful" : "failed") + ": (" + x + ", " + y + ") -> (" + newX + ", " + newY + ")");
        return moved;
    }

    private boolean hasMoved(int newX, int newY, ArrayList<SpaceInfo> newPerceptionData) {
        // Compare new perception data with expected data to determine if move was successful
        // Here we check if the new position is different from the old position
        if (newX != x || newY != y) {
            this.x = newX;
            this.y = newY;
            return true;
        }
        return false;
    }

    private boolean canMove(int newX, int newY) {
        Point point = new Point(newX, newY);
        SpaceInfo spaceInfo = memory.get(point);
        return spaceInfo != null && (spaceInfo.getType() == SpaceType.EMPTY || spaceInfo.getType() == SpaceType.DANCEFLOOR);
    }

    private Direction randomDirection() {
        int directionNumber = (int) (Math.random() * 4);
        //System.out.println("Philipp (" + getAvatarID() + ") is using a random direction");
        switch (directionNumber) {
            case 0 -> {
                return moveUp();
            }
            case 1 -> {
                return moveDown();
            }
            case 2 -> {
                return moveLeft();
            }
            case 3 -> {
                return moveRight();
            }
            default -> {
                return stay(); // Safety net, though unnecessary as directionNumber is bound by 0-3
            }
        }
    }

    private Direction moveUp() {
        //System.out.println("Philipp (" + getAvatarID() + ") wants to go up");
        if (attemptMove(Direction.UP)) return Direction.UP;
        return stay();
    }

    private Direction moveDown() {
        //System.out.println("Philipp (" + getAvatarID() + ") wants to go down");
        if (attemptMove(Direction.DOWN)) return Direction.DOWN;
        return stay();
    }

    private Direction moveLeft() {
        //System.out.println("Philipp (" + getAvatarID() + ") wants to go left");
        if (attemptMove(Direction.LEFT)) return Direction.LEFT;
        return stay();
    }

    private Direction moveRight() {
        //System.out.println("Philipp (" + getAvatarID() + ") wants to go right");
        if (attemptMove(Direction.RIGHT)) return Direction.RIGHT;
        return stay();
    }

    private Direction stay() {
        //System.out.println("Philipp (" + getAvatarID() + ") wants to stay");
        return Direction.STAY;
    }

    private ArrayList<SpaceInfo> lookAround(int newX, int newY, int perceptionRange) {
        // Simulate getting perception data from the environment
        // This method should be implemented to interact with your environment
        // Here, returning a filled list with dummy data as a placeholder
        int size = (perceptionRange * 2 + 1) * (perceptionRange * 2 + 1) - 1;
        ArrayList<SpaceInfo> perceptionData = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            perceptionData.add(new SpaceInfo(new Coordinate(newX, newY), SpaceType.EMPTY));
        }
        return perceptionData;
    }

    /**
     * Helper class to represent a point in the grid.
     */
    private static class Point {
        int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x && y == point.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    /**
     * Handles the stats and updates the perception range and other behaviors.
     */
    private void handleStats() {
        movesCount++;

        if (movesCount % 10 == 0) {
            sobriety++;
            perceptionRange++;
            setPerceptionRange(perceptionRange);
        }

        if (movesCount % 20 == 0) {
            bladder++;
        }

        if (atBar) {
            if (stayCount < 10) {
                stayCount++;
                sobriety = 0;
                bladder += 2;
                //System.out.println("At the bar. Sobriety: " + sobriety + ", Bladder: " + bladder);
            } else {
                atBar = false;
                stayCount = 0;
                perceptionRange = 1;
                setPerceptionRange(perceptionRange);
                target = null;
                setTarget();
            }
            return;
        }

        if (atToilet) {
            if (stayCount < 5) {
                stayCount++;
                //System.out.println("At the toilet. Sobriety: " + sobriety + ", Bladder: " + bladder);
            } else {
                atToilet = false;
                stayCount = 0;
                bladder = 0;
                perceptionRange = 1;
                setPerceptionRange(perceptionRange);
                target = null;
                setTarget();
            }
            return;
        }

        if (target != null && target.x == x && target.y == y) {
            if (memory.get(target).getType() == SpaceType.BAR) {
                atBar = true;
                //System.out.println("Reached the bar. Sobriety: " + sobriety + ", Bladder: " + bladder);
            } else if (memory.get(target).getType() == SpaceType.TOILET) {
                atToilet = true;
                //System.out.println("Reached the toilet. Sobriety: " + sobriety + ", Bladder: " + bladder);
            }
        }
    }
}
