package PersonalAvatars;

import AvatarInterface.*;
import Environment.*;
import java.awt.Color;
import java.util.ArrayList;

public class PhilippAvatar extends SuperAvatar {

    private Coordinate currentCoordinate = new Coordinate(0, 0);
    

    public PhilippAvatar(int id, int perceptionRange, Color color) {
        super(id, perceptionRange, color);
    }

    @Override
    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {
        // throw new UnsupportedOperationException("Not supported yet.");
        return randomDirection();
    }

    private Direction randomDirection() {
        int directionNumber = (int) (Math.random() * 4);

        return switch (directionNumber) {
            case 0 -> moveLeft();
            case 1 -> moveRight();
            case 2 -> moveUp();
            case 3 -> moveDown();
            default -> stay();
        };
    }



    private Direction stay() {
        printCoordinate(currentCoordinate);
        return Direction.STAY;
    }

    private Direction moveUp() {
        currentCoordinate = new Coordinate(currentCoordinate.getX(), currentCoordinate.getY() - 1);
        printCoordinate(currentCoordinate);
        return Direction.UP;
    }

    private Direction moveDown() {
        currentCoordinate = new Coordinate(currentCoordinate.getX(), currentCoordinate.getY() + 1);
        printCoordinate(currentCoordinate);
        return Direction.DOWN;
    }

    private Direction moveLeft() {
        currentCoordinate = new Coordinate(currentCoordinate.getX() - 1, currentCoordinate.getY());
        printCoordinate(currentCoordinate);
        return Direction.LEFT;
    }

    private Direction moveRight() {
        currentCoordinate = new Coordinate(currentCoordinate.getX() + 1, currentCoordinate.getY());
        printCoordinate(currentCoordinate);
        return Direction.RIGHT;
    }

    private void printCoordinate(Coordinate coordinate) {
        System.out.println("Coordinate: " + coordinate);
    }
}