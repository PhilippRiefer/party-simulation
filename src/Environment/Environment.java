package Environment;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

public class Environment {
    private Room model;
    private SimulationGUI view;

    public Environment() {
        this.model = new Room(40, 20);

        System.out.println("Room constructed");
        this.view = new SimulationGUI();
        System.out.println("View constructed");

        paintWall();
        paintBar();
        paintDJBooth();
        paintDancefloor();
        paintSeats();
        paintToilet();
    }

    public void setSpaceType(int x, int y, SpaceType spaceType) {
        // Set the space type in the room model
        try {
            model.setSpace(new Coordinate(x, y), spaceType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void paintWall() {
        for (int x = 0; x < 40; ++x) {
            view.paintComponent(x, 0, Color.RED);
            view.paintComponent(x, 19, Color.RED);
            setSpaceType(x, 0, SpaceType.OBSTACLE);
            setSpaceType(x, 19, SpaceType.OBSTACLE);
        }
        for (int y = 1; y < 19; ++y) {
            view.paintComponent(0, y, Color.RED);
            view.paintComponent(39, y, Color.RED);
            setSpaceType(0, y, SpaceType.OBSTACLE);
            setSpaceType(39, y, SpaceType.OBSTACLE);
        }
    }
    public void paintBar() {
        for (int x = 1; x <= 2; ++x) {
            for (int y = 7; y <= 12; ++y) {
                view.paintComponent(x, y, Color.BLACK);
                setSpaceType(x, y, SpaceType.BAR);
            }
        }
        for (int x = 37; x <= 38; ++x) {
            for (int y = 7; y <= 12; ++y) {
                view.paintComponent(x, y, Color.BLACK);
                setSpaceType(x, y, SpaceType.BAR);
            }
        }
    }
    public void paintDJBooth() {
        for (int x = 14; x <= 20; ++x) {
            for (int y = 1; y <= 2; ++y) {
                view.paintComponent(x, y, Color.PINK);
                setSpaceType(x, y, SpaceType.DJBOOTH);
            }
        }
    }
    public void paintDancefloor() {
        for (int x = 13; x <= 21; ++x) {
            for (int y = 4; y <= 12; ++y) {
                view.paintComponent(x, y, Color.YELLOW);
                setSpaceType(x, y, SpaceType.DANCEFLOOR);
            }
        }
    }
    public void paintSeats() {
        Random random = new Random();
        for (int i = 0; i < 4; ++i) {
            int randomX = random.nextInt(1, 6);
            int randomY = random.nextInt(14, 19);
            view.paintComponent(randomX, randomY, Color.CYAN);
            setSpaceType(randomX, randomY, SpaceType.SEATS);

            randomX = random.nextInt(31, 38);
            randomY = random.nextInt(1, 6);
            view.paintComponent(randomX, randomY, Color.CYAN);
            setSpaceType(randomX, randomY, SpaceType.SEATS);
        }
    }
    public void paintToilet() {
        for (int x = 35; x <= 38; ++x) {
            view.paintComponent(x, 18, Color.GREEN);
            setSpaceType(x, 18, SpaceType.TOILET);
        }
    }

    /**
     * Places an avatar in the room.
     * 
     * @param avatarID the ID of the avatar to be placed
     */
    public void placeAvatar(int avatarID) {
        Coordinate avatarCoordinate = model.findPlaceForAvatar(avatarID);
        if (avatarCoordinate != null) {
            view.paintAvatar(avatarCoordinate, Color.BLUE);
        }
        System.out.println("Placed Avatar with ID " + avatarID + " at X: " + avatarCoordinate.getX() + ", Y: "
                + avatarCoordinate.getY());
    }

    /**
     * Gets the adjacent spaces to an avatar within a given perception range.
     * 
     * @param avatarId        the ID of the avatar
     * @param perceptionRange the perception range of the avatar
     * @return an ArrayList of SpaceInfo objects representing the adjacent spaces
     */
    public ArrayList<SpaceInfo> getAdjacentToAvatar(int avatarId, int perceptionRange) {
        return model.getAdjacentToAvatar(avatarId, perceptionRange);
    }

    /**
     * Moves an avatar in the specified direction.
     * 
     * @param avatarID the ID of the avatar to be moved
     * @param dir      the direction in which to move the avatar
     * @return true if the avatar was successfully moved, false otherwise
     * @throws Exception
     */
    public boolean moveAvatar(int avatarID, Direction dir, Color color) {
        Coordinate currentPos = model.getAvatarLocation(avatarID);
        if (currentPos == null) {
            throw new IllegalArgumentException("Avatar " + avatarID + " does not exist in the room.");
        }

        // Save the current position
        int oldX = currentPos.getX();
        int oldY = currentPos.getY();

        // Update the coordinates based on the direction
        switch (dir) {
            case UP -> currentPos.setY(currentPos.getY() - 1);
            case DOWN -> currentPos.setY(currentPos.getY() + 1);
            case LEFT -> currentPos.setX(currentPos.getX() - 1);
            case RIGHT -> currentPos.setX(currentPos.getX() + 1);
            default -> {
                return false;
            }
        }
        

        // Check if the new position is valid and try to place the avatar there
        if (model.tryToPlaceAvatar(avatarID, currentPos)) {
            try {
                model.setSpace(new Coordinate(oldX, oldY), SpaceType.EMPTY);
            } catch (Exception e) {
                System.out.println("Failed to set space to empty at " + oldX + ", " + oldY + ".");
            }

            // Erase the avatar from the old position
            try {
                model.setSpace(new Coordinate(oldX, oldY), SpaceType.EMPTY);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            view.eraseAvatar(new Coordinate(oldX, oldY));

            // Paint the avatar at the new position
            view.paintAvatar(currentPos, color);

            return true;
        } else {
            // If placing failed, revert the position change
            currentPos.setX(oldX);
            currentPos.setY(oldY);
            return false;
        }
    }

    // private boolean isCellOccupied(Coordinate coordinate) {
    //     int coordX = coordinate.getX();
    //     int coordY = coordinate.getY();

    //     SpaceType spaceType = model.getSpace(coordinate);
    //     return spaceType == SpaceType.DANCEFLOOR || spaceType == SpaceType.BAR || spaceType == SpaceType.TOILET;

    //     // for (int x = 13; x <= 21; ++x) {
    //     //     for (int y = 4; y <= 12; ++y) {
    //     //         if (x == coordX && y == coordY){
    //     //             return true;
    //     //         }
    //     //     }
    //     // }
    //     // return false;
    // }
}
