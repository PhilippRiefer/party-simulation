package PersonalAvatars;

/********************************************
<<<<<<< Updated upstream
 * Author: Ole
 * Version: v.1
 * Date:   20240511
=======
 * Author:  Ole
 * Version: 0.7
 * Date:    20240511
>>>>>>> Stashed changes
 * ------------------------------------------
 * Description: personal avatar of Ole 
 ********************************************/

<<<<<<< Updated upstream
 import java.awt.Color;
=======
import java.awt.Color;
import java.io.FileWriter;
import java.io.IOException;

>>>>>>> Stashed changes
import Environment.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
<<<<<<< Updated upstream
=======
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
>>>>>>> Stashed changes
import java.util.HashMap;
import java.util.Map;

import AvatarInterface.SuperAvatar;

public class OleAvatar extends SuperAvatar {
    public OleAvatar(int id, int perceptionRange, Color color) {
        super(id, perceptionRange, color);
    }
<<<<<<< Updated upstream
    // create a border for the avatar
    private static final int MIN_BORDER_X = 0;
    private static final int MIN_BORDER_Y = 0;
    private static final int MAX_BORDER_X = 39;
    private static final int MAX_BORDER_Y = 19;
=======

    // arrays to store visited spaces
    // ----------------------------------
    private int[][] seenEnvironment = new int[20][40];
    private int[][] visitedCells = new int[20][40];
>>>>>>> Stashed changes
    // HashMap for all spacetypes
    // ----------------------------------
    private static final Map<String, Integer> TYPE = new HashMap<>();
<<<<<<< Updated upstream
    // spacetypes with each ranking 
=======
    // spacetypes with each ranking
    // ----------------------------------
>>>>>>> Stashed changes
    static {
        TYPE.put("BAR", 1);
        TYPE.put("AVATAR", 2);
        TYPE.put("DANCEFLOOR", 3);
        TYPE.put("EMPTY", 4);
        TYPE.put("DJBOOTH", 5);
        TYPE.put("SEATS", 6);
        TYPE.put("TOILET", 7);
    }
    // static variable that is generated
    // anew for each program start to
    // select between option 1 and 2
    // ----------------------------------
    private static final int CHOSEN_BEHAVIOUR;
    // save the number of moves,
    // drinks, interrogations and dancemoves
    // ----------------------------------
    private int run = 0;
    private int drinks = 0;
    private int suspicious = 0;
    private int danced = 0;
    // Choose randomly between two options
    // how the avatar should behave.
    // ----------------------------------
    static {
        Random random = new Random();
        int randomOption = random.nextInt(2);
        if (randomOption == 0) {
            CHOSEN_BEHAVIOUR = 0;
        } else {
            CHOSEN_BEHAVIOUR = 1;
        }
    }
<<<<<<< Updated upstream
    // My avatar is the next to move.
=======

    // Main method, which includes the
    // sequence of my avatars behavior.
    // -> return Direction
>>>>>>> Stashed changes
    // ----------------------------------
    @Override
    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {
        int[] left = checkSpace(spacesInRange, 1); // check cell to the left
        int[] above = checkSpace(spacesInRange, 3); // check cell above
        int[] bottom = checkSpace(spacesInRange, 4); // check cell down
        int[] right = checkSpace(spacesInRange, 6); // check cell to the right
        // merge the arrays
        int[][] directions = { left, above, bottom, right };
<<<<<<< Updated upstream
        // take cell as minimum
=======
        // save the visited spaces
        visitedSpaces(directions);
        // choose option
>>>>>>> Stashed changes
        int minValue = left[0];
        Direction direction;
        if (CHOSEN_BEHAVIOUR == 0) {
            direction = imitatePolice(directions, minValue, 2);
        } else {
            direction = drinkingAndDancing(directions, minValue);
        }
        return direction;
    }

    // Check the spaces arround the avatar.
    // -> return int[] array
    // ----------------------------------
    // spaceNumber:
    // 0 -- 3 -- 5
    // 1 -- A -- 6
    // 2 -- 4 -- 7
    private int[] checkSpace(ArrayList<SpaceInfo> spacesInRange, int spaceNumber) {
        SpaceType type; // store the type of space
        String typeAsString; // hold the type of space as a string
        int coordX; // store x coordinate
        int coordY; // store y coordinate
        int[] obstacle = new int[3]; // integer array to store obstacle data
        if (spacesInRange.size() >= 8) {
            SpaceInfo space = spacesInRange.get(spaceNumber); // get the space information
            Coordinate coord = space.getRelativeToAvatarCoordinate(); // get the coordinates relative to the avatar
            coordX = coord.getX(); // get X
            coordY = coord.getY(); // get Y
            type = space.getType(); // get type
            typeAsString = type.name(); // convert to string
            // determine the obstacle based on the type of space and its coordinates
            obstacle = determinateObstacle(typeAsString, coordX, coordY);
            // return the determined obstacle array
            return obstacle;
        } else {
            return obstacle;
        }
    }

    // Determinate wich obstacles are
    // arround the avatar.
    // -> return int[] array
    // ----------------------------------
    // return array[0] = 0 --> BAR
    // return array[0] = 1 --> DANCEFLOOR
    // return array[0] = 2 --> EMPTY
    // return array[0] = 3 --> DJBOOTH
    // return array[0] = 4 --> SEATS
    // return array[0] = 5 --> AVATAR
    // return array[0] = 6 --> TOILET
    // return array[0] = 7 --> OBSTACLE
    // return array[0] = 8 --> Wall
    private int[] determinateObstacle(String typeAsString, int coordX, int coordY) {
        int[] array = new int[3];
        array[1] = coordX;
        array[2] = coordY;
<<<<<<< Updated upstream
        // check the obstacles around the avatar and rank them 
        array[0] = TYPE.getOrDefault(typeAsString, 7);
        // if the avatar is out of bounds -> give bad ranking
        if (isOutOfBounds(coordX, coordY)) {
            array[0] = 8;
        }
        return array;
    }
    // check if the avatar is out of bounds
    // ----------------------------------
    private boolean isOutOfBounds(int x, int y) {
        return x <= MIN_BORDER_X || x >= MAX_BORDER_X || y <= MIN_BORDER_Y || y >= MAX_BORDER_Y;
    }
    // Make a desicion based on the obstacles
    // ----------------------------------
    private Direction makeDesicion(int[][] directions, int minValue) {
        int minIndex = 0;
        List<Integer> minIndices = new ArrayList<>();
        minIndices.add(0); // start with the first element
        // run through all arrays and find the smallest number in cell [0]
        for (int i = 1; i < directions.length; i++) {
            if (directions[i][0] < minValue) {
                minValue = directions[i][0];
                minIndex = i;
                minIndices.clear(); // clear the list as we found a new minimum
                minIndices.add(i); // add the index of the new minimum
            } else if (directions[i][0] == minValue) {
                minIndices.add(i); // add index if it matches the current minimum
            }
        }
        // check if there are multiple arrays with the same minimum value
        if (minIndices.size() > 1) {
            System.out.println("Multiple directions have the same minimum value: " + minValue);
            for (int index : minIndices) {
                System.out.println("Direction index: " + index);
            }
            // IMPORTANT: randomly select one of the indices with the minimum value
            Random random = new Random();
            minIndex = minIndices.get(random.nextInt(minIndices.size()));
        }
        // print target
        String[] arrayNames = { "left", "above", "bottom", "right" };
        System.out.println("in the direction: " + arrayNames[minIndex]
                + " has the best ranking: " + minValue);
        // choose the target
        if (minValue == 0) {
            return Direction.STAY;
=======
        // check the obstacles around the avatar and rank them
        array[0] = TYPE.getOrDefault(typeAsString, 8);
        return array;
    }

    // Option 1. Search for the BAR and
    // stay there for a moment and get
    // a drink and the go dancing.
    // -> return Direction
    // ----------------------------------
    private Direction drinkingAndDancing(int[][] directions, int minValue) {
        blink(Color.ORANGE, Color.BLACK);
        int minIndex = 0;
        List<Integer> minIndices = new ArrayList<>();
        minIndices.add(0); // start with the first element
        // the avatar got to much drinks, search for the dancefloor
        // TODO
        if ((drinks > 20 && danced < 30) || (drinks > 40 && danced < 60) ) {
            return needToDance(directions, minValue, 3);
>>>>>>> Stashed changes
        } else {
            // run through all arrays and find the smallest number in cell [0]
            for (int i = 1; i < directions.length; i++) {
                if (directions[i][0] < minValue) {
                    minValue = directions[i][0];
                    minIndex = i;
                    minIndices.clear(); // new minimum -> clear the list
                    minIndices.add(i); // add the index of the new minimum
                } else if (directions[i][0] == minValue) {
                    minIndices.add(i); // add index if it matches the current minimum
                }
            }
            // check if there are multiple arrays with the same minimum value
            // IMPORTANT: randomly select one of the indices with the minimum value, so the
            // avatar does not walk in one direction
            if (minIndices.size() > 1) {
                Random random = new Random();
                minIndex = minIndices.get(random.nextInt(minIndices.size()));
            }
            // choose the target
            if (minValue == 1) { // BAR
                // pause at the bar and get a drink
                drinks++;
                System.out.println("Found the bar, get a drink!");
                wait(100);
                return Direction.DOWN;
            } else {
                // Check if the coordinates of the avatar where visited before
                if (isVisited(directions)) {
                    // System.out.println("All directions were visited before, move in a random
                    // direction.");
                    return randomDirection();
                } else {
                    if (minIndex == 0) {
                        return Direction.LEFT;
                    } else if (minIndex == 1) {
                        return Direction.UP;
                    } else if (minIndex == 2) {
                        return Direction.DOWN;
                    } else {
                        return Direction.RIGHT;
                    }
                }
            }
        }
    }

    // Option 2. Imitate a policeofficer
    // and follow suspicious avatars.
    // -> return Direction
    // ----------------------------------
    private Direction imitatePolice(int[][] directions, int minValue, int targetValue) {
        // blink like the police
        blink(Color.BLUE, Color.RED);
        int minIndex = 0;
        List<Integer> foundIndices = new ArrayList<>();
        // run through all arrays and find the number targetValue in cell [0]
        for (int i = 0; i < directions.length; i++) {
            if (directions[i][0] == targetValue) {
                minIndex = i;
                foundIndices.add(i);
                System.out.println("Found a suspicious avatar, follow him.");
                // pause at the avatar and interrogate him
                suspicious++;
                wait(50);
            }
        }
        // no Avatar found -> move random
        if (foundIndices.size() <= 1) {
            // Check if the coordinates of the avatar where also visited before
            if (isVisited(directions)) {
                // System.out.println("All directions were visited before, move in a random
                // direction.");
            }
            return randomDirection();
        }
        // choose direction
        if (minIndex == 0) {
            return Direction.LEFT;
        } else if (minIndex == 1) {
            return Direction.UP;
        } else if (minIndex == 2) {
            return Direction.DOWN;
        } else {
            return Direction.RIGHT;
        }
    }

    // Search for the dancefloor.
    // -> return Direction
    // ----------------------------------
    private Direction needToDance(int[][] directions, int minValue, int targetValue) {
        blink(Color.BLUE, Color.CYAN);
        int minIndex = 0;
        List<Integer> foundIndices = new ArrayList<>();
        // run through all arrays and find the number targetValue in cell [0]
        for (int i = 0; i < directions.length; i++) {
            if (directions[i][0] == targetValue) {
                minIndex = i;
                foundIndices.add(i);
                System.out.println("I need to dance!");
                // pause at the dancefloor
                danced++;
                wait(50);
            }
        }
        // no floor found -> move random
        if (foundIndices.size() <= 1) {
            // Check if the coordinates of the avatar where also visited before
            if (isVisited(directions)) {
                // System.out.println("All directions were visited before, move in a random
                // direction.");
            }
            return randomDirection();
        }
        // choose direction
        if (minIndex == 0) {
            return Direction.LEFT;
        } else if (minIndex == 1) {
            return Direction.UP;
        } else if (minIndex == 2) {
            return Direction.DOWN;
        } else {
            return Direction.RIGHT;
        }
    }

    // Blink, to identify avatar from
    // the others.
    // ----------------------------------
    private void blink(Color one, Color two) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new Runnable() {
            private boolean isBlue = true;

            @Override
            public void run() {
                if (isBlue) {
                    setAvatarColor(one);
                } else {
                    setAvatarColor(two);
                }
                isBlue = !isBlue;
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
    }

    // Save the visited spaces and print
    // the replicated room.
    // ----------------------------------
    private void visitedSpaces(int[][] directions) {
        int myX = directions[0][1] + 1;
        int myY = directions[0][2];
        visitedCells[myY][myX] = 99;
        run++;
        // Update cells based on the passed directions
        for (int i = 0; i < directions.length; i++) {
            int y = directions[i][2];
            int x = directions[i][1];
            int type = directions[i][0];
            seenEnvironment[y][x] = type;
        }
        // output of the saved map as console output and as txt file
        // printForConsole();
        createTxtFile();
    }

    // Output of the saved map as
    // console output. -> not necessary
    // ----------------------------------
    private void printForConsole() {
        // output of the saved map
        System.out.println("+=============================================================================+");
        System.out.println("\t\t\t      Map saved in Ole's head:");
        System.out.println("-------------------------------------------------------------------------------");
        // write how the avatar behaves
        if (CHOSEN_BEHAVIOUR == 0) {
            // police
            System.out.println(" Option 1: Imitate a police officer and interrogate conspicuous avatars.");
            System.out.println(" The avatar has moved " + run + " times and interrogated " + suspicious
                    + " suspects.");
        } else {
            // destination Bar
            System.out.println(" Option 2: Search the Bar and get a drink.");
            System.out
                    .println(" The avatar has moved " + run + " times and drank " + drinks + " drinks.");
        }
        System.out.println("+=============================================================================+");
        for (int i = 0; i < seenEnvironment.length; i++) {
            for (int j = 0; j < seenEnvironment[i].length; j++) {
                // give a symbol as default value if the space is not visited yet
                char cellSymbol = seenEnvironment[i][j] == 0 ? '.' : Character.forDigit(seenEnvironment[i][j], 10);
                System.out.print(cellSymbol + " ");
            }
            System.out.println();
        }
        System.out.println("+=============================================================================+");
    }

    // Create a txt file and save the
    // map here.
    // ----------------------------------
    private void createTxtFile() {
        // Write the visited grid to a text file -> "OlesVisitedSpaces.txt"
        try (FileWriter writer = new FileWriter("OlesVisitedSpaces.txt")) {
            writer.write("+=============================================================================+\n");
            writer.write("\t\t\t\t\t\t\tMap saved in Ole's head:\n");
            writer.write("-------------------------------------------------------------------------------\n");
            // write how the avatar behaves
            if (CHOSEN_BEHAVIOUR == 0) {
                // police
                writer.write(" Option 1: Imitate a police officer and interrogate conspicuous avatars.\n");
                writer.write(" The avatar has moved " + run + " times and interrogated " + suspicious + " suspects.\n");
            } else {
                // destination Bar
                writer.write(" Option 2: Search the Bar and get a drink.\n");
                writer.write(" The avatar has moved " + run + " times and drank " + drinks + " drinks.\n");
            }
            writer.write("+=============================================================================+\n");
            for (int i = 0; i < seenEnvironment.length; i++) {
                for (int j = 0; j < seenEnvironment[i].length; j++) {
                    char cellSymbol;
                    switch (seenEnvironment[i][j]) {
                        // Not Visited yet
                        case 0:
                            cellSymbol = '.';
                            break;
                        // BAR
                        case 1:
                            cellSymbol = 'B';
                            break;
                        // AVATAR
                        case 2:
                            cellSymbol = 'A';
                            break;
                        // DANCEFLOOR
                        case 3:
                            cellSymbol = 'F';
                            break;
                        // EMPTY
                        case 4:
                            cellSymbol = ' ';
                            break;
                        // DJBOOTH
                        case 5:
                            cellSymbol = 'D';
                            break;
                        // SEAT
                        case 6:
                            cellSymbol = 'S';
                            break;
                        // TOILET
                        case 7:
                            cellSymbol = 'T';
                            break;
                        // OBSTACLE
                        case 8:
                            cellSymbol = 'X';
                            break;
                        default:
                            cellSymbol = '?';
                            break;
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
            writer.write("3 -> F:\tDANCEFLOOR\n");
            writer.write("4 ->  :\tEMPTY\n");
            writer.write("5 -> D:\tDJBOOTH\n");
            writer.write("6 -> S:\tSEATS\n");
            writer.write("7 -> T:\tTOILET\n");
            writer.write("8 -> X:\tOBSTACLE\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Check if a space has already
    // been visited.
    // -> return boolean
    // ----------------------------------
    private boolean isVisited(int[][] directions) {
        // Check all four directions
        for (int i = 0; i < directions.length; i++) {
            int y = directions[i][2];
            int x = directions[i][1];
            // If the space is 99, it means it has been visited
            if (visitedCells[y][x] == 99) {
                blink(Color.LIGHT_GRAY, Color.GRAY);
                return true;
            }
        }
        return false;
    }

    // Delay the application.
    // ----------------------------------
    public static void wait(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    // Move Avatar in a random direction.
    // Is required if no other decision
    // can be made.
    // -> return Direction
    // ----------------------------------
    private Direction randomDirection() {
        int directionNumber = (int) (Math.random() * 4);
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
                return Direction.STAY;
        }
    }

<<<<<<< Updated upstream
    // NOT USED
    // --------------------------------------------------------------------------
    // generate a random desicion
    // ==================================
    // private Direction randomDesicion() {
    // int random = (int) (Math.random() * 4);
    // switch (random) {
    // case 0:
    // System.out.println("(ID: " + getAvatarID() + ") Ole -> left");
    // return Direction.LEFT;
    // case 1:
    // System.out.println("(ID: " + getAvatarID() + ") Ole -> right");
    // return Direction.RIGHT;
    // case 2:
    // System.out.println("(ID: " + getAvatarID() + ") Ole -> up");
    // return Direction.UP;
    // case 3:
    // System.out.println("(ID: " + getAvatarID() + ") Ole -> down");
    // return Direction.DOWN;
    // default:
    // System.out.println("(ID: " + getAvatarID() + ") Ole -> stay");
    // return Direction.STAY;
    // }
    // }

    // // check wich coordinates are mine
    // // ==================================
    // private int[] determineMyCoordinates(ArrayList<SpaceInfo> spacesInRange) {
    // int[] knownX = new int[8];
    // int[] knownY = new int[8];
    // int[] myCoordinates = new int[2];
    // int i = 0;
    // for (SpaceInfo space : spacesInRange) {
    // Coordinate relativeCoord = space.getRelativeToAvatarCoordinate();
    // knownX[i] = relativeCoord.getX();
    // knownY[i] = relativeCoord.getY();
    // ++i;
    // }
    // int avatarX = knownX[1] + 1;
    // int avatarY = knownY[1];
    // myCoordinates[0] = avatarX;
    // myCoordinates[1] = avatarY;
    // return myCoordinates;
    // }

    /**
     * Gets the perception range of the avatar.
     *
     * @return the perception range of the avatar
     */
=======
    // Gets the perception range of the
    // avatar.
    // -> return int
    // ----------------------------------
>>>>>>> Stashed changes
    @Override
    public int getPerceptionRange() {
        return super.getPerceptionRange(); // Assuming SuperAvatar has a method to get the perception range
    }

    // Sets the perception range of the
    // avatar.
    // ----------------------------------
    @Override
    public void setPerceptionRange(int perceptionRange) {
        super.setPerceptionRange(perceptionRange); // Set the perception range via the superclass method
    }
}
