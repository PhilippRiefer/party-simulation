// SudehAvatar.java
package PersonalAvatars;

import java.awt.Color;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
//import java.util.List;
import java.util.Map;
import java.util.Random;

import Environment.Coordinate;
import Environment.Direction;
import Environment.SpaceInfo;
import Environment.SpaceType;
import AvatarInterface.SuperAvatar;

public class SudehAvatar extends SuperAvatar {
    private int[][] seenEnvironment = new int[20][40];
    private int[][] visitedCells = new int[20][40];
    private static final Map<String, Integer> TYPE = new HashMap<>();

    static {
        TYPE.put("BAR", 1);
        TYPE.put("AVATAR", 2);
        TYPE.put("DANCEFLOOR", 3);
        TYPE.put("EMPTY", 4);
        TYPE.put("DJBOOTH", 5);
        TYPE.put("SEATS", 6);
        TYPE.put("TOILET", 7);
    }

    private int run = 0;

    public SudehAvatar(int id, int perceptionRange, Color color) {
        super(id, perceptionRange, color);
    }

    @Override
    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {
        Random random = new Random();
        Direction direction = Direction.STAY;
        setAvatarColor(new Color(0, 77, 64));
        // Simulate random movement
        if (random.nextInt(100) < 80) { // 80% chance to move randomly
            int directionNumber = random.nextInt(4);
            direction = switch (directionNumber) {
                case 0 -> Direction.LEFT;
                case 1 -> Direction.RIGHT;
                case 2 -> Direction.UP;
                case 3 -> Direction.DOWN;
                default -> Direction.STAY;
            };
        } else {
            // Choose action based on feelings
            int feeling = random.nextInt(4);
            switch (feeling) {
                case 0 -> direction = moveToType(spacesInRange, 
                                                 SpaceType.DANCEFLOOR, 5, 
                                                 new Color(0, 77, 64 ), 
                                                 new Color(0, 204, 200 ));
                case 1 -> direction = moveToType(spacesInRange, 
                                                 SpaceType.BAR, 4, 
                                                 new Color(0, 77, 64 ), 
                                                 new Color(0, 204, 200 ));
                case 2 -> direction = moveToType(spacesInRange, 
                                                 SpaceType.TOILET, 2, 
                                                 new Color(0, 77, 64 ), 
                                                 new Color(0, 204, 200 ));
                case 3 -> direction = moveToType(spacesInRange, 
                                                 SpaceType.SEATS, 3, 
                                                 new Color(0, 77, 64 ), 
                                                 new Color(0, 204, 200 ));
            }
        }

        // Save visited spaces
        int[] left = checkSpace(spacesInRange, 1); // check cell to the left
        int[] above = checkSpace(spacesInRange, 3); // check cell above
        int[] bottom = checkSpace(spacesInRange, 4); // check cell down
        int[] right = checkSpace(spacesInRange, 6); // check cell to the right
        int[][] directions = { left, above, bottom, right };
        visitedSpaces(directions);

        return direction;
    }

    private Direction moveToType(ArrayList<SpaceInfo> spacesInRange, SpaceType targetType, int stayRounds, Color activeColor, Color passiveColor) {
        for (SpaceInfo space : spacesInRange) {
            if (space.getType() == targetType) {
                setAvatarColor(activeColor);
                run += stayRounds;
                return Direction.STAY;
            }
        }
        setAvatarColor(passiveColor);
        return randomDirection();
    }

    private int[] checkSpace(ArrayList<SpaceInfo> spacesInRange, int spaceNumber) {
        SpaceType type;
        String typeAsString;
        int[] array = new int[3];
        if (spacesInRange.size() >= 8) {
            SpaceInfo space = spacesInRange.get(spaceNumber);
            Coordinate coord = space.getRelativeToAvatarCoordinate();
            type = space.getType();
            typeAsString = type.name();
            array = determinateObstacle(typeAsString, coord.getX(), coord.getY());
            return array;
        } else {
            return array;
        }
    }

    private int[] determinateObstacle(String typeAsString, int coordX, int coordY) {
        int[] array = new int[3];
        array[1] = coordX;
        array[2] = coordY;
        array[0] = TYPE.getOrDefault(typeAsString, 8);
        return array;
    }

    private void visitedSpaces(int[][] directions) {
        int myX = directions[0][1] + 1;
        int myY = directions[0][2];
        visitedCells[myY][myX] = 99;
        run++;
        for (int i = 0; i < directions.length; i++) {
            int y = directions[i][2];
            int x = directions[i][1];
            int type = directions[i][0];
            seenEnvironment[y][x] = type;
        }
        createTxtFile();
    }

    private void createTxtFile() {
        try (FileWriter writer = new FileWriter("SudehVisitedSpaces.txt")) {
            writer.write("+=============================================================================+\n");
            writer.write("\t\t\t\t\t\t\tMap of SudehAvatr's Movements\n");
            writer.write("-------------------------------------------------------------------------------\n");
            writer.write(" The avatar has moved " + run + " times.\n");
            writer.write("+=============================================================================+\n");
            for (int i = 0; i < seenEnvironment.length; i++) {
                for (int j = 0; j < seenEnvironment[i].length; j++) {
                    char cellSymbol;
                    switch (seenEnvironment[i][j]) {
                        case 0 -> cellSymbol = '.';
                        case 1 -> cellSymbol = 'B';
                        case 2 -> cellSymbol = 'A';
                        case 3 -> cellSymbol = '/';
                        case 4 -> cellSymbol = ' ';
                        case 5 -> cellSymbol = 'D';
                        case 6 -> cellSymbol = 'S';
                        case 7 -> cellSymbol = 'T';
                        case 8 -> cellSymbol = 'X';
                        default -> cellSymbol = '?';
                    }
                    writer.write(cellSymbol + " ");
                }
                writer.write("\n");
            }
            writer.write("+=============================================================================+\n");
            writer.write("\n\t  Legend\n");
            writer.write("------------------\n");
            writer.write("1 -> B:\tBAR\n");
            writer.write("2 -> A:\tAVATAR\n");
            writer.write("3 -> /:\tDANCEFLOOR\n");
            writer.write("4 ->  :\tEMPTY\n");
            writer.write("5 -> D:\tDJBOOTH\n");
            writer.write("6 -> S:\tSEATS\n");
            writer.write("7 -> T:\tTOILET\n");
            writer.write("8 -> X:\tOBSTACLE\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Direction randomDirection() {
        int directionNumber = (int) (Math.random() * 4);
        return switch (directionNumber) {
            case 0 -> Direction.LEFT;
            case 1 -> Direction.RIGHT;
            case 2 -> Direction.UP;
            case 3 -> Direction.DOWN;
            default -> Direction.STAY;
        };
    }

    @Override
    public int getPerceptionRange() {
        return super.getPerceptionRange();
    }

    @Override
    public void setPerceptionRange(int perceptionRange) {
        super.setPerceptionRange(perceptionRange);
    }
}
