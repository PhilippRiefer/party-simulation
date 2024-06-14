package PersonalAvatars;

/********************************************
 * Author:  Ole
 * Version: 1.0
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
import java.util.HashMap;
import java.util.Map;

import AvatarInterface.SuperAvatar;

public class OleAvatar extends SuperAvatar {
    public OleAvatar(int id, int perceptionRange, Color color) {
        super(id, perceptionRange, color);
    }

    // arrays to store visited spaces
    // Important: NOT THE SAME
    // ----------------------------------
    private int[][] seenEnvironment = new int[20][40]; // every cell around the avatar he has ever seen
    private int[][] visitedCells = new int[20][40]; // only the cells he has actually visited
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
    // save the number of moves,
    // drinks, interrogations and dancemoves
    // ----------------------------------
    private int run = 0;
    private int drinks = 0;
    private int drinksCounted = 0;
    private int suspicious = 0;
    private int danced = 0;

    // Not really needed
    private String desicion;

    // static variable to check if a cell
    // visited before
    // ----------------------------------
    private static int VISITED = 99;

    // static variable that is generated
    // anew for each program start to
    // select between behaviour 1 and 2
    // ----------------------------------
    private static final int CHOSEN_BEHAVIOUR;
    // Choose randomly between two options
    // how the avatar should behave.
    // ----------------------------------
    static {
        // generate a number between 0 and 1
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
        Direction direction;
        // check all cells around the avatar
        int[] left = checkSpace(spacesInRange, 1); // check cell to the left
        int[] above = checkSpace(spacesInRange, 3); // check cell above
        int[] bottom = checkSpace(spacesInRange, 4); // check cell down
        int[] right = checkSpace(spacesInRange, 6); // check cell to the right
        // merge the arrays into one
        int[][] directions = { left, above, bottom, right };
        // save the visited spaces
        visitedSpaces(directions);
        int minValue = left[0]; // set left as cell with the smallest value
        // choose behaviour
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
        int[] spaceInfoAsInteger = new int[3]; // array to store the relevant information as an integer
        SpaceInfo space = spacesInRange.get(spaceNumber); // get the space information
        Coordinate coord = space.getRelativeToAvatarCoordinate(); // get the coordinates relative to the avatar
        type = space.getType(); // get type
        typeAsString = type.name(); // convert to type to string
        // determine the obstacle based on the type and its coordinates
        spaceInfoAsInteger = determinateObstacle(typeAsString, coord.getX(), coord.getY());
        return spaceInfoAsInteger; // return the determined obstacle array
    }

    // Determinate wich obstacles are
    // arround the avatar.
    // -> return int[] array
    // ----------------------------------
    // spaceInfoAsInteger:
    // [0] = Type
    // [1] = X-Coordinate
    // [2] = Y-Coordinate
    private int[] determinateObstacle(String typeAsString, int coordX, int coordY) {
        int[] array = new int[3]; // array to store the relevant information as an integer
        array[0] = TYPE.getOrDefault(typeAsString, 8); // check the types around the avatar
        array[1] = coordX; // X-Coordinate
        array[2] = coordY; // Y-Coordinate
        return array;
    }

    // Option 1. Search for the BAR and
    // stay there for a moment and get
    // a drink and the go dancing.
    // -> return Direction
    // ----------------------------------
    private Direction drinkingAndDancing(int[][] directions, int minValue) {
        blink(Color.ORANGE, Color.BLACK); // let the avatar blink
        int minIndex = 0; // variable to identify which direction contains the targetValue
        // create list for the number of values found
        List<Integer> minIndices = new ArrayList<>();
        minIndices.add(0); // start with the first element
        // the avatar got to much drinks, search for the dancefloor
        if ((drinks > 20 && danced < 30) || (drinks > 40 && danced < 60)) {
            return needToDance(directions, minValue, 3);
        } else {
            // run through all arrays and find the smallest number in cell [0]
            for (int i = 1; i < directions.length; i++) {
                // check whether the cell has a smaller value than on the left
                if (directions[i][0] < minValue) {
                    minValue = directions[i][0]; // save it
                    minIndex = i; // save the direction
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
                drinksCounted++;
                wait(10);
                return Direction.DOWN;
            } else {
                // Check if the coordinates of the avatar where also visited before
                // and and visualize this with a color
                if (isVisited(directions)) {
                    return randomDirection();
                } else {
                    // choose direction based on the index
                    return chooseDirBasedOnIndex(minIndex);
                }
            }
        }
    }

    // Option 2. Imitate a policeofficer
    // and follow suspicious avatars.
    // -> return Direction
    // ----------------------------------
    private Direction imitatePolice(int[][] directions, int minValue, int targetValue) {
        blink(Color.BLUE, Color.RED); // blink like the police
        int minIndex = 0; // variable to identify which direction contains the targetValue
        // create list for the number of values found
        List<Integer> foundIndices = new ArrayList<>();
        List<Integer> minIndices = new ArrayList<>();
        // run through all arrays and find the targetValue in cell [0]
        for (int i = 0; i < directions.length; i++) {
            if (directions[i][0] == targetValue) {
                minIndex = i; // save direction
                foundIndices.add(i); // save direction
                // pause at the avatar and interrogate him
                suspicious++;
                wait(10);
            }
        }
        // no Avatar found -> search for the best ranking
        if (foundIndices.size() <= 1) {
            // run through all arrays and find the smallest number in cell [0]
            for (int i = 1; i < directions.length; i++) {
                // check whether the cell has a smaller value than on the left
                if (directions[i][0] < minValue) {
                    minValue = directions[i][0]; // save it
                    minIndex = i; // save the direction
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
            // stay away from the bar
            if (minValue == 1) { // BAR
                return Direction.DOWN;
            } else {
                // Check if the coordinates of the avatar where also visited before
                // and and visualize this with a color
                if (isVisited(directions)) {
                    return randomDirection();
                }
            }
        }
        return chooseDirBasedOnIndex(minIndex);
    }

    // Search for the dancefloor.
    // -> return Direction
    // ----------------------------------
    private Direction needToDance(int[][] directions, int minValue, int targetValue) {
        int minIndex = 0; // variable to identify which direction contains the targetValue
        // reset values if values are exceeded
        if (drinks > 40 && danced < 60) {
            drinks = 0;
            danced = 0;
        }
        // create list for the number of values found
        List<Integer> foundIndices = new ArrayList<>();
        // run through all arrays and find the targetValue in cell [0]
        for (int i = 0; i < directions.length; i++) {
            if (directions[i][0] == targetValue) {
                minIndex = i; // save direction
                foundIndices.add(i); // save direction
                danced++; // increase variable
            }
        }
        // no floor found -> move random
        if (foundIndices.size() <= 1) {
            // Check if the coordinates of the avatar where also visited before
            // and and visualize this with a color
            if (isVisited(directions)) {
            }
            return randomDirection();
        }
        // choose direction based on the index
        return chooseDirBasedOnIndex(minIndex);
    }

    // Choose the direction based on the
    // index.
    // -> return Direction
    // ----------------------------------
    private Direction chooseDirBasedOnIndex(int minIndex) {
        switch (minIndex) {
            case 0:
                desicion = "LEFT";
                return Direction.LEFT;
            case 1:
                desicion = "UP";
                return Direction.UP;
            case 2:
                desicion = "DOWN";
                return Direction.DOWN;
            case 3:
                desicion = "RIGHT";
                return Direction.RIGHT;
            default:
                desicion = "RIGHT";
                return Direction.RIGHT;
        }
    }

    // Blink, to identify avatar from
    // the others.
    // ----------------------------------
    private void blink(Color one, Color two) {
        // create a scheduled executor service with a single thread
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        // task to run at a fixed rate
        executorService.scheduleAtFixedRate(new Runnable() {
            private boolean isBlue = true; // track the current color state

            @Override
            public void run() {
                // if the current state is blue, set the avatar color to the first color
                if (isBlue) {
                    setAvatarColor(one);
                }
                // set the avatar color to the second color
                else {
                    setAvatarColor(two);
                }
                isBlue = !isBlue; // toggle the color state
            }
        }, 0, 100, TimeUnit.MILLISECONDS); // initial delay of 0 milliseconds, then repeat every 100 milliseconds
    }

    // Save the visited spaces and print
    // the replicated room.
    // ----------------------------------
    private void visitedSpaces(int[][] directions) {
        int myX = directions[0][1] + 1; // calculate my x coordinate (leftx = myx + 1)
        int myY = directions[0][2]; // lefty = myy
        visitedCells[myY][myX] = VISITED; // give the cell a unique value to identify it later
        run++;
        // Update cells based on the passed directions
        for (int i = 0; i < directions.length; i++) {
            int y = directions[i][2];
            int x = directions[i][1];
            int type = directions[i][0];
            seenEnvironment[y][x] = type; // save type as integer value in corresponding cell
        }
        // output of the saved map as txt file
        createTxtFile();
    }

    // Create a txt file and save the
    // map here.
    // ----------------------------------
    private void createTxtFile() {
        // Write the visited grid to a text file -> "OlesVisitedSpaces.txt"
        try (FileWriter writer = new FileWriter("OlesVisitedSpaces.txt")) {
            // writer.write(" The avatar moves " + desicion + "\n");
            writer.write("+=============================================================================+\n");
            writer.write("\t\t\t\t\t\t\tMap saved in Ole's head:\n");
            writer.write("-------------------------------------------------------------------------------\n");
            // show which characteristic the avatar has
            if (CHOSEN_BEHAVIOUR == 0) {
                // police
                writer.write(" Option 1: Imitate a police officer and interrogate conspicuous avatars.\n");
                writer.write(" The avatar has moved " + run + " times and interrogated " + suspicious + " suspects.\n");
            } else {
                // destination Bar
                writer.write(" Option 2: Search the Bar and get a drink.\n");
                writer.write(" The avatar has moved " + run + " times and drank " + drinksCounted + " drinks.\n");
            }
            writer.write("+=============================================================================+\n");
            // run through the array with the seen environment and replace the integer
            // values with more understandable char values
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
            // also create a legend to explain the char values
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
            // If cell is 99, it means it has been visited
            if (visitedCells[y][x] == VISITED) {
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
        // generate number between 0 and 3
        int directionNumber = (int) (Math.random() * 4);
        // check the random number and return a direction based on the number
        switch (directionNumber) {
            case 0:
                desicion = "LEFT";
                return Direction.LEFT;
            case 1:
                desicion = "RIGHT";
                return Direction.RIGHT;
            case 2:
                desicion = "UP";
                return Direction.UP;
            case 3:
                desicion = "DOWN";
                return Direction.DOWN;
            default:
                desicion = "STAY";
                return Direction.STAY;
        }
    }

    // Gets the perception range of the
    // avatar.
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
