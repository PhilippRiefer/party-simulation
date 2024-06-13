package PersonalAvatars;

import AvatarInterface.SuperAvatar;
import Environment.Coordinate;
import Environment.Direction;
import Environment.SpaceInfo;
import Environment.SpaceType;

import java.awt.Color;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SudehAvatar extends SuperAvatar {

    private Random random;
    private int stayCounter;
    private int moveCounter;
    private int[][] seenEnvironment = new int[20][40];
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

    public SudehAvatar(int id, int perceptionRange, Color color) {
        super(id, perceptionRange, color); // leverage the super class to handle ID and perceptionRange
        this.random = new Random();
        this.stayCounter = 0;
        this.moveCounter = 0;
    }

    @Override
    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {
        if (stayCounter > 0) {
            stayCounter--;
            if (stayCounter == 0) {
                //System.out.println("Stay duration over. Resuming movement.");
                // Reset stayCounter and switch to movement mode
                stayCounter = 0;
                return randomDirection();
            } else {
                //System.out.println("Staying in place for " + stayCounter + " turns.");
            }
            return Direction.STAY;
        }
        setAvatarColor(new Color(0, 77, 64));

        // Check the spaces in range and decide what to do
        for (SpaceInfo space : spacesInRange) {
            SpaceType type = space.getType();
            switch (type) {
                case EMPTY:
                    return getDirectionFromCoordinate(space.getRelativeToAvatarCoordinate());

                case OBSTACLE:
                    //System.out.println("Found an obstacle, skipping.");
                    continue;

                case AVATAR:
                    setAvatarColor(new Color(215, 204, 200 ));
                    stayCounter = getStayDuration(type);

                case DANCEFLOOR:
                    setAvatarColor(new Color(128, 203, 196));
                    stayCounter = getStayDuration(type);
                    return getDirectionFromCoordinate(space.getRelativeToAvatarCoordinate());

                case DJBOOTH:
                    setAvatarColor(new Color(128, 203, 196));
                    stayCounter = getStayDuration(type);
                    return getDirectionFromCoordinate(space.getRelativeToAvatarCoordinate());

                case TOILET:
                    setAvatarColor(new Color(128, 203, 196));
                    stayCounter = getStayDuration(type);
                    return getDirectionFromCoordinate(space.getRelativeToAvatarCoordinate());

                case BAR:
                    setAvatarColor(new Color(128, 203, 196));
                    stayCounter = getStayDuration(type);
                    return getDirectionFromCoordinate(space.getRelativeToAvatarCoordinate());

                case SEATS:
                    setAvatarColor(new Color(128, 203, 196));
                    stayCounter = getStayDuration(type);
                    return getDirectionFromCoordinate(space.getRelativeToAvatarCoordinate());

                default:
                    //System.out.println("Unknown space type, skipping.");
                    continue;
            }
        }
        //save visited spaces and create text file
         visitedSpaces(spacesInRange);
         createTxtFile();
         

        // If no suitable action was found, move randomly
        //Direction randomDirection = randomDirection();
        //System.out.println("Moving randomly in direction: " + randomDirection);

        // If no suitable action was found, move randomly
        return randomDirection();
    }

    private void visitedSpaces(ArrayList<SpaceInfo> spacesInRange) {
        for (SpaceInfo space : spacesInRange) {
            int x = space.getRelativeToAvatarCoordinate().getX();
            int y = space.getRelativeToAvatarCoordinate().getY();
            String typeAsString = space.getType().name();
            int type = TYPE.getOrDefault(typeAsString, 8);
            seenEnvironment[y][x] = type;
        }
        // Mark the current position of the avatar
        seenEnvironment[10][20] = 9;  // Assuming the avatar is in the middle of the map
    }

    /**
     * Utility method to get the direction based on the relative coordinate.
     *
     * @param coordinate the relative coordinate
     * @return the direction to move
     */
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

    /**
     * Generates a random direction for the avatar's turn.
     *
     * @return a random direction
     */
    private Direction randomDirection() {
        int directionNumber = random.nextInt(4); // Random number between 0 and 3

        switch (directionNumber) {
            case 0:
                return Direction.LEFT;
            case 1:
                return Direction.RIGHT;
            case 2:
                return Direction.UP;
            case 3:
                return Direction.DOWN;
            default:
                return Direction.STAY; // Safety net, though unnecessary as directionNumber is bound by 0-3
        }
    }

    /**
     * Determines the number of turns to stay in the current space type.
     *
     * @param type the space type
     * @return the number of turns to stay
     */
    private int getStayDuration(SpaceType type) {
        switch (type) {
            case AVATAR:
                return 2; // Stay for .. turns
            case DANCEFLOOR:
                return 2; // Stay for .. turns
            case DJBOOTH:
                return 5; // Stay for .. turns
            case TOILET:
                return 5; // Stay for .. turns
            case BAR:
                return 5; // Stay for .. turns
            case SEATS:
                return 5; // Stay for .. turns
            default:
                return 0;
        }
    }

    @Override
    public int getPerceptionRange() {
        return super.getPerceptionRange(); // Assuming SuperAvatar has a method to get the perception range
    }

    @Override
    public void setPerceptionRange(int perceptionRange) {
        super.setPerceptionRange(perceptionRange); // Set the perception range via the superclass method
    }

    //to create the map of the vatar's movements in a .txt file
    private void createTxtFile(){
        String filePath = "SudehAvatarMap.txt";
        try(FileWriter writer2 = new FileWriter(filePath)){
            writer2.write("+=============================================================================+\n");
            writer2.write("\t\t\t\t\t\t\tMap of SudehAvatar Activiies:\n");
            writer2.write("-------------------------------------------------------------------------------\n");

            writer2.write(" The avatar has moved " + moveCounter + " times.\n");
            writer2.write("+=============================================================================+\n");

            for(int i= 0; i< seenEnvironment.length; i++){
                for(int j = 0; j< seenEnvironment[i].length; j++){
                    char cellSymbol;
                    switch (seenEnvironment[i][j]) {
                        case 0:
                            cellSymbol = '.';
                            break;
                        case 1:
                            cellSymbol = 'B';
                            break;
                        case 2:
                            cellSymbol = 'A';
                            break;
                        case 3:
                            cellSymbol = 'F';
                            break;
                        case 4:
                            cellSymbol = ' ';
                            break;
                        case 5:
                            cellSymbol = 'D';
                            break;
                        case 6:
                            cellSymbol = 'S';
                            break;
                        case 7:
                            cellSymbol = 'T';
                            break;
                        case 8:
                            cellSymbol = 'X';
                            break;
                        case 9:
                            cellSymbol = 'O';  // Avatar's current position
                            break;
                        default:
                            cellSymbol = '?';
                            break;
                    }
                    writer2.write(cellSymbol + " ");
                } 
                writer2.write("\n");             
            }
            writer2.write("+=============================================================================+\n");
            writer2.write("\n\t  Legend\n");
            writer2.write("------------------\n");
            writer2.write("1 -> B:\tBAR\n");
            writer2.write("2 -> A:\tAVATAR\n");
            writer2.write("3 -> F:\tDANCEFLOOR\n");
            writer2.write("4 ->  :\tEMPTY\n");
            writer2.write("5 -> D:\tDJBOOTH\n");
            writer2.write("6 -> S:\tSEATS\n");
            writer2.write("7 -> T:\tTOILET\n");
            writer2.write("8 -> X:\tOBSTACLE\n");
            
        }catch (IOException e){
            System.err.println("Error writing to file: " + filePath);
            e.printStackTrace();
        }
    }
}
