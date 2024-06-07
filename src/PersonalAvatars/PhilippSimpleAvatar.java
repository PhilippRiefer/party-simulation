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

        // Implement a more sophisticated strategy using spacesInRange
        // For now, let's continue to move randomly as a placeholder
        return decideMove(spacesInRange);
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

            System.out.println("Initial position determined: (" + x + ", " + y + ")");
        } else {
            System.out.println("No spaces in range to determine initial position.");
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
        System.out.println("Updated memory:");
        for (Map.Entry<Point, SpaceInfo> entry : memory.entrySet()) {
            Point point = entry.getKey();
            SpaceInfo info = entry.getValue();
            System.out.println("Point: (" + point.x + ", " + point.y + ") - Type: " + info.getType());
        }
    }

    /**
     * Decides the next move for the avatar.
     *
     * @param spacesInRange the list of spaces within the perception range
     * @return the direction for the avatar's turn
     */
    private Direction decideMove(ArrayList<SpaceInfo> spacesInRange) {
        System.out.println("Philipp (" + getAvatarID() + ") is deciding move");

        setPerceptionRange(2);
        System.out.println("Philipp (" + getAvatarID() + ") has perception range: " + getPerceptionRange());
        System.out.println("Philipp (" + getAvatarID() + ") sees this many spaces: " + spacesInRange.size());

        int expectedSpaces = (getPerceptionRange() * 2 + 1) * (getPerceptionRange() * 2 + 1) - 1;
        if (spacesInRange.size() == expectedSpaces) {
            System.out.println("That is correct, perception range is: " + getPerceptionRange());
        } else {
            System.out.println("\n\nWrong, perception range is: " + getPerceptionRange() + ", array size is: " + spacesInRange.size() + "\n\n"); // this means I'm probably at a wall or corner
        }

        // For now, let's continue to move randomly as a placeholder
        Direction move = randomDirection();
        if (attemptMove(move, spacesInRange)) {
            return move;
        } else {
            // If the move was not successful, try a different move or stay
            return stay();
        }
    }

    /**
     * Attempts to move in the given direction and verifies if the move was successful.
     *
     * @param move           the direction to move
     * @param spacesInRange  the current perception data
     * @return true if the move was successful, false otherwise
     */
    private boolean attemptMove(Direction move, ArrayList<SpaceInfo> spacesInRange) {
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

        System.out.println("Attempting move to: (" + newX + ", " + newY + ")");

        // Simulate the move and get new perception data
        ArrayList<SpaceInfo> newPerceptionData = lookAround(newX, newY, getPerceptionRange());
        System.out.println("New perception data size: " + newPerceptionData.size());
        updateMemory(newPerceptionData);

        // Check if the avatar has moved by comparing new perception data
        boolean moved = hasMoved(newX, newY, newPerceptionData);
        System.out.println("Move " + (moved ? "successful" : "failed") + ": (" + x + ", " + y + ") -> (" + newX + ", " + newY + ")");
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

    private Direction randomDirection() {
        int directionNumber = (int) (Math.random() * 4);
        System.out.println("Philipp (" + getAvatarID() + ") is using a random direction");
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
        System.out.println("Philipp (" + getAvatarID() + ") wants to go up");
        return Direction.UP;
    }

    private Direction moveDown() {
        System.out.println("Philipp (" + getAvatarID() + ") wants to go down");
        return Direction.DOWN;
    }

    private Direction moveLeft() {
        System.out.println("Philipp (" + getAvatarID() + ") wants to go left");
        return Direction.LEFT;
    }

    private Direction moveRight() {
        System.out.println("Philipp (" + getAvatarID() + ") wants to go right");
        return Direction.RIGHT;
    }

    private Direction stay() {
        System.out.println("Philipp (" + getAvatarID() + ") wants to stay");
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
}
