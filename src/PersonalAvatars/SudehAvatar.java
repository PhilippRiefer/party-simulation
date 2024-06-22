package PersonalAvatars;

import java.awt.Color;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

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
    private Random random = new Random();

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
        Direction direction;
        if (stayCounter > 0) {
            stayCounter--;
            if (stayCounter == 0) {
                System.out.println("Stay duration over. Resuming movement.");
                direction = randomDirection();
            } else {
                System.out.println("Staying in place for " + stayCounter + " turns.");
                direction = Direction.STAY;
            }
        } else {
            direction = findBestDirection(spacesInRange);
            if (direction == Direction.STAY) {
                direction = randomDirection();
            }
        }

        // Update visited spaces and environment
        int[] directions = checkSpace(spacesInRange, 0); // You may need to loop over spacesInRange or find the current space
        visitedSpaces(new int[][]{directions});

        return direction;
    }

    private Direction findBestDirection(ArrayList<SpaceInfo> spacesInRange) {
        PriorityQueue<SpaceInfo> queue = new PriorityQueue<>(Comparator.comparingInt(this::heuristicCost));
        for (SpaceInfo space : spacesInRange) {
            queue.offer(space);
        }

        while (!queue.isEmpty()) {
            SpaceInfo space = queue.poll();
            SpaceType type = space.getType();
            if (type != SpaceType.OBSTACLE) {
                int stayDuration = getStayDuration(type);
                if (stayDuration > 0) {
                    setAvatarColor(getColorForType(type));
                    stayCounter = stayDuration;
                    return getDirectionFromCoordinate(space.getRelativeToAvatarCoordinate());
                }
            }
        }
        return Direction.STAY;
    }

    private int heuristicCost(SpaceInfo space) {
        SpaceType type = space.getType();
        int baseCost = switch (type) {
            case BAR -> 10;
            case AVATAR -> 20;
            case DANCEFLOOR -> 15;
            case DJBOOTH -> 25;
            case TOILET -> 10;
            case SEATS -> 5;
            default -> 0;
        };
        Coordinate coord = space.getRelativeToAvatarCoordinate();
        int distance = Math.abs(coord.getX()) + Math.abs(coord.getY());
        return baseCost + distance;
    }

    private Color getColorForType(SpaceType type) {
        return switch (type) {
            case BAR, DANCEFLOOR, DJBOOTH, TOILET, SEATS -> new Color(0, 90, 80);
            case AVATAR -> new Color(204, 0, 200);
            default -> new Color(0, 90, 80);
        };
    }

    private int getStayDuration(SpaceType type) {
        return switch (type) {
            case AVATAR -> {
                talk++;
                yield 2;
            }
            case DANCEFLOOR -> {
                dance++;
                yield 10;
            }
            case DJBOOTH -> {
                dj++;
                yield 10;
            }
            case TOILET -> {
                peeOrDrug++;
                yield 10;
            }
            case BAR -> {
                drink++;
                yield 10;
            }
            case SEATS -> {
                rest++;
                yield 10;
            }
            default -> 0;
        };
    }

    private Direction getDirectionFromCoordinate(Coordinate coordinate) {
        int x = coordinate.getX();
        int y = coordinate.getY();

        if (x == 1) {
            return Direction.RIGHT;
        } else if (x == -1) {
            return Direction.LEFT;
        } else if (y == 1) {
            return Direction.DOWN;
        } else if (y == -1) {
            return Direction.UP;
        }

        return randomDirection();
    }

    private Direction randomDirection() {
        int directionNumber = random.nextInt(4);
        return switch (directionNumber) {
            case 0 -> Direction.LEFT;
            case 1 -> Direction.RIGHT;
            case 2 -> Direction.UP;
            case 3 -> Direction.DOWN;
            default -> Direction.STAY;
        };
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

        for (int[] direction : directions) {
            int y = direction[2];
            int x = direction[1];
            int type = direction[0];
            seenEnvironment[y][x] = type;
        }
        createTxtFile();
    }

    private void createTxtFile() {
        try (FileWriter writer = new FileWriter("SudehVisitedSpaces.txt")) {
            writer.write("+=============================================================================+\n");
            writer.write("\t\t\t\t\t\t\tMap of SudehAvatar's Decisions\n");
            writer.write("-------------------------------------------------------------------------------\n");
            writer.write(" SudehAvatar was wandering in the nightclub for " + wander + " minutes.\n");
            writer.write(" then it Decided to: \n\n");
            writer.write(" buy a Drink      " + drink + " times.\n");
            writer.write(" Dance for        " + dance + " minutes.\n");
            writer.write(" be the DJ for    " + dj + " rounds.\n");
            writer.write(" be social for    " + talk + " seconds.\n");
            writer.write(" go to the Toilet " + peeOrDrug + " times, for pee or drug?.\n");
            writer.write(" chill for        " + rest + " minutes.\n");
            writer.write("+=============================================================================+\n");
            for (int i = 0; i < seenEnvironment.length; i++) {
                for (int j = 0; j < seenEnvironment[i].length; j++) {
                    char cellSymbol;
                    switch (seenEnvironment[i][j]) {
                        case 0 -> cellSymbol = '.';
                        case 1 -> cellSymbol = 'B';
                        case 2 -> cellSymbol = 'A';
                        case 3 -> cellSymbol = 'Y';
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

    @Override
    public int getPerceptionRange() {
        return super.getPerceptionRange();
    }

    @Override
    public void setPerceptionRange(int perceptionRange) {
        super.setPerceptionRange(perceptionRange);
    }
}
