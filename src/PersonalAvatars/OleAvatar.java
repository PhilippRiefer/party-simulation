package PersonalAvatars;

/********************************************
 * Author:  Ole
 * Version: 0.7
 * Date:    20240511
 * ------------------------------------------
 * Description: personal avatar of Ole 
 ********************************************/

import java.awt.Color;
import java.io.FileWriter;
import java.io.IOException;

import Environment.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import AvatarInterface.SuperAvatar;

import java.util.HashMap;
import java.util.Map;

// import AvatarInterface.SuperAvatar;

public class OleAvatar extends SuperAvatar {
    public OleAvatar(int id, int perceptionRange, Color color) {
        super(id, perceptionRange, color);
    }

    // arrays to store visited spaces
    // ----------------------------------
    private int[][] seenEnvironment = new int[20][40];
    private int[][] visitedCells = new int[20][40];
    // HashMap for all spacetypes
    // ----------------------------------
    private static final Map<String, Integer> TYPE = new HashMap<>();
    // spacetypes with each ranking
    // ----------------------------------
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

    // Main method, which includes the
    // sequence of my avatars behavior.
    // -> return Direction
    // ----------------------------------
    @Override
    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {
        int[] left = checkSpace(spacesInRange, 1); // check cell to the left
        int[] above = checkSpace(spacesInRange, 3); // check cell above
        int[] bottom = checkSpace(spacesInRange, 4); // check cell down
        int[] right = checkSpace(spacesInRange, 6); // check cell to the right
        // merge the arrays
        int[][] directions = { left, above, bottom, right };
        // save the visited spaces
        visitedSpaces(directions);
        // choose option
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
        int[] array = new int[3]; // integer array to store object data
        if (spacesInRange.size() >= 8) {
            SpaceInfo space = spacesInRange.get(spaceNumber); // get the space information
            Coordinate coord = space.getRelativeToAvatarCoordinate(); // get the coordinates relative to the avatar
            type = space.getType(); // get type
            typeAsString = type.name(); // convert to string
            // determine the obstacle based on the type of space and its coordinates
            array = determinateObstacle(typeAsString, coord.getX(), coord.getY());
            // return the determined obstacle array
            return array;
        } else {
            return array;
        }
    }

    // Determinate wich obstacles are
    // arround the avatar.
    // -> return int[] array
    // ----------------------------------
    private int[] determinateObstacle(String typeAsString, int coordX, int coordY) {
        int[] array = new int[3];
        array[1] = coordX;
        array[2] = coordY;
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
                // System.out.println("Found the bar, get a drink!");
                // wait(100);
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
                // System.out.println("Found a suspicious avatar, follow him.");
                // pause at the avatar and interrogate him
                suspicious++;
                wait(10);
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
                // System.out.println("I need to dance!");
                // pause at the dancefloor
                danced++;
                // wait(50);
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
         //createTxtFile();
    }

    // Output of the saved map as
    // console output. -> not necessary
    // ----------------------------------
    private void printForConsole() {
        // output of the saved map
        //System.out.println("+=============================================================================+");
        //System.out.println("\t\t\t      Map saved in Ole's head:");
        //System.out.println("-------------------------------------------------------------------------------");
        // write how the avatar behaves
        if (CHOSEN_BEHAVIOUR == 0) {
            // police
            //System.out.println(" Option 1: Imitate a police officer and interrogate conspicuous avatars.");
            //System.out.println(" The avatar has moved " + run + " times and interrogated " + suspicious
            //        + " suspects.");
        } else {
            // destination Bar
            //System.out.println(" Option 2: Search the Bar and get a drink.");
            //System.out
            //        .println(" The avatar has moved " + run + " times and drank " + drinks + " drinks.");
        }
        //System.out.println("+=============================================================================+");
        for (int i = 0; i < seenEnvironment.length; i++) {
            for (int j = 0; j < seenEnvironment[i].length; j++) {
                // give a symbol as default value if the space is not visited yet
                char cellSymbol = seenEnvironment[i][j] == 0 ? '.' : Character.forDigit(seenEnvironment[i][j], 10);
                //System.out.print(cellSymbol + " ");
            }
            //System.out.println();
        }
        //System.out.println("+=============================================================================+");
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

    // Gets the perception range of the
    // avatar.
    // -> return int
    // ----------------------------------
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
