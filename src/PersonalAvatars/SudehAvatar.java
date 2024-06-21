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
    private int stayCounter;

    private int drink = 0;
    private int dance = 0;
    private int peeOrDrug = 0;
    private int dj = 0;
    private int rest = 0;
    private int talk = 0;

    static {
        TYPE.put("BAR", 1);
        TYPE.put("AVATAR", 2);
        TYPE.put("DANCEFLOOR", 3);
        TYPE.put("EMPTY", 4);
        TYPE.put("DJBOOTH", 5);
        TYPE.put("SEATS", 6);
        TYPE.put("TOILET", 7);
    }

    private int wander = 0;

    public SudehAvatar(int id, int perceptionRange, Color color) {
        super(id, perceptionRange, color);
        this.stayCounter = 0;
    }

    @Override
    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {
        if(stayCounter > 0){
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
        Random random = new Random();
        Direction direction = Direction.STAY;
        if (random.nextInt(100) < 60) { // 60% chance to move randomly
            int directionNumber = random.nextInt(4);
            direction = switch (directionNumber) {
                case 0 -> Direction.LEFT;
                case 1 -> Direction.RIGHT;
                case 2 -> Direction.UP;
                case 3 -> Direction.DOWN;
                default -> Direction.STAY;
            };
        } else {
            //check the spaceInrange and decide what to do
            for(SpaceInfo space : spacesInRange){
                SpaceType type = space.getType();
                switch (type){
                    case EMPTY: //wandaring 
                        setAvatarColor(new Color(10, 80, 70));
                        direction = getDirectionFromCoordinate(space.getRelativeToAvatarCoordinate());
                        return direction;

                    case AVATAR: //socializing
                        setAvatarColor(new Color(204, 0, 200));
                        //talk++;
                        stayCounter = getStayDuration(type);
                        direction = getDirectionFromCoordinate(space.getRelativeToAvatarCoordinate());
                        return direction;

                    case DANCEFLOOR: //feeling festive
                        setAvatarColor(new Color(0, 240, 200));
                        stayCounter = getStayDuration(type);
                        direction = getDirectionFromCoordinate(space.getRelativeToAvatarCoordinate());
                        return direction;

                    case BAR: //feeling thirsty
                        setAvatarColor(new Color(0, 240, 200));
                        stayCounter = getStayDuration(type);
                        direction = getDirectionFromCoordinate(space.getRelativeToAvatarCoordinate());
                        return direction;

                    case TOILET: //need to pee
                        setAvatarColor(new Color(0, 240, 200));
                        stayCounter = getStayDuration(type);
                        direction = getDirectionFromCoordinate(space.getRelativeToAvatarCoordinate());
                        return direction;
                        
                    case DJBOOTH: 
                        setAvatarColor(new Color(0, 240, 200));
                        stayCounter = getStayDuration(type);
                        direction = getDirectionFromCoordinate(space.getRelativeToAvatarCoordinate());
                        return direction;

                    case SEATS: 
                        setAvatarColor(new Color(0, 240, 200));
                        stayCounter = getStayDuration(type);
                        direction = getDirectionFromCoordinate(space.getRelativeToAvatarCoordinate());
                        return direction;
        
                    case OBSTACLE: //Found an obstacle, skipping.
                        continue;
                    default:
                        continue;
                }

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

    private int getStayDuration(SpaceType type) {
        
        switch (type) {
            case AVATAR:
                talk ++;
                return 10; // Stay for .. turns

            case DANCEFLOOR:
                dance ++;
                return 10; // Stay for .. turns

            case DJBOOTH:
                dj ++;
                return 10; // Stay for .. turns

            case TOILET:
                peeOrDrug++;
                return 10; // Stay for .. turns

            case BAR:
                drink ++;
                return 10; // Stay for .. turns

            case SEATS:
                rest ++;
                return 10; // Stay for .. turns

            default:
                return 0;
        }
    }

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
        wander++;
        
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
            writer.write("\t\t\t\t\t\t\tMap of SudehAvatr's Decisions\n");
            writer.write("-------------------------------------------------------------------------------\n");
            writer.write(" SudehAvatr was wandering in the nightclub for " + wander + " minutes.\n");
            writer.write(" then it Decided to: \n\n");
            writer.write(" buy a Drink      " + drink     + " times.\n");
            writer.write(" Dance for        " + dance     + " minutes.\n");
            writer.write(" be the DJ for    " + dj        + " rounds.\n");
            writer.write(" be social for    " + talk      + " seconds.\n");
            writer.write(" go to the Toilet " + peeOrDrug + " times, for pee or drug?.\n");
            writer.write(" chill for        " + rest      + " minutes.\n");
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
