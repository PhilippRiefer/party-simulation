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

    private int[][] seenEnvironment = new int[20][40]; //keep track of environment seen by avatar
    private int[][] visitedCells = new int[20][40]; //keep track the visited cells by avatar
    private static final Map<String, Integer> TYPE = new HashMap<>();
    private int stayCounter; //controls the duration of staying in one place
    private int followCounter; //to control how long to follow other avatars
    private Coordinate targetAvatar;
    private Random random = new Random();
    private Deque<Coordinate> stack = new ArrayDeque<>();
    private Set<Coordinate> visitedSet = new HashSet<>();

    //track avatars activities
    private int drink = 0;
    private int dance = 0;
    private int peeOrDrug = 0;
    private int dj = 0;
    private int rest = 0;
    private int talk = 0;
    
    //init a TYPE map to mapenvironment object types
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
        this.followCounter = 0;
        this.stack.push(new Coordinate(0, 0)); // Start at the initial position
    }
    /**
     * yourTurn:
     * The main decision-making method called every turn. It decides whether to stay, 
     * follow another avatar, or move based on the environment and internal counters.
     */
    @Override
    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {
        Direction direction;
        if (stayCounter > 0) {
            stayCounter--;
            if (stayCounter == 0) {
                System.out.println("Stay duration over. Resuming movement.");
                direction = nextExplorationDirection(spacesInRange);
            } else {
                System.out.println("Staying in place for " + stayCounter + " turns.");
                direction = Direction.STAY;
            }
        } else if (followCounter > 0 && targetAvatar != null) {
            followCounter--;
            direction = getDirectionFromCoordinate(targetAvatar);
            if (direction == Direction.STAY) {
                targetAvatar = null;
                direction = nextExplorationDirection(spacesInRange);
            }
        } else {
            direction = findBestDirection(spacesInRange);
            if (direction == Direction.STAY) {
                direction = nextExplorationDirection(spacesInRange);
            }
        }

        // Update visited spaces and environment
        int[] directions = checkSpace(spacesInRange, 0); // You may need to loop over spacesInRange or find the current space
        visitedSpaces(new int[][]{directions});

        return direction;
    }
    /**
     * Determines the best direction to move based on the perceived spaces.
     */
    private Direction findBestDirection(ArrayList<SpaceInfo> spacesInRange) {
        for (SpaceInfo space : spacesInRange) {
            if (space.getType() == SpaceType.AVATAR) {
                targetAvatar = space.getRelativeToAvatarCoordinate();
                followCounter = 10; // Follow for ... turns
                talk++;
                return getDirectionFromCoordinate(targetAvatar);
            }
        }

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

    private Direction nextExplorationDirection(ArrayList<SpaceInfo> spacesInRange) {
        while (!stack.isEmpty()) {
            Coordinate current = stack.peek();
            if (visitedSet.contains(current)) {
                stack.pop();
                continue;
            }
            visitedSet.add(current);

            for (Direction direction : Direction.values()) {
                Coordinate next = getNextCoordinate(current, direction);
                if (isValidCoordinate(next) && !visitedSet.contains(next)) {
                    stack.push(next);
                    return direction;
                }
            }
        }
        return randomDirection();
    }

    private Coordinate getNextCoordinate(Coordinate current, Direction direction) {
        return switch (direction) {
            case LEFT -> new Coordinate(current.getX() - 1, current.getY());
            case RIGHT -> new Coordinate(current.getX() + 1, current.getY());
            case UP -> new Coordinate(current.getX(), current.getY() - 1);
            case DOWN -> new Coordinate(current.getX(), current.getY() + 1);
            default -> current;
        };
    }

    private boolean isValidCoordinate(Coordinate coord) {
        return coord.getX() >= 0 && coord.getX() < 40 && coord.getY() >= 0 && coord.getY() < 20;
    }
    /**
     * Calculates a heuristic cost for prioritizing movement towards different types of spaces.
     * @param space
     * @return
     */
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
    //Returns a color based on the type of space
    private Color getColorForType(SpaceType type) {
        return switch (type) {
            case DJBOOTH, TOILET, SEATS, AVATAR -> new Color(0, 120, 60);
            case DANCEFLOOR -> new Color(0, 120, 60);
            case BAR -> new Color(100, 20, 60);
            default -> new Color(0, 120, 60);
        };
    }
    //Determines how long to stay at a specific type of space.
    private int getStayDuration(SpaceType type) {
        return switch (type) {
            case AVATAR -> {
                yield 3;
            }
            case DANCEFLOOR -> {
                dance++;
                yield 5;
            }
            case DJBOOTH -> {
                dj++;
                yield 2;
            }
            case TOILET -> {
                peeOrDrug++;
                yield 2;
            }
            case BAR -> {
                drink++;
                yield 5;
            }
            case SEATS -> {
                rest++;
                yield 5;
            }
            default -> 0;
        };
    }
    //Translates a coordinate into a movement direction
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
    //Updates the seenEnvironment based on the perceived spaces.
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
    //Increments the count of visits to cells and triggers createTxtFile after a certain number of moves
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
            writer.write("\t\t\t\t\t\t\tMap of Nightclub exploration by SudehAvatar\n");
            writer.write("-------------------------------------------------------------------------------\n");
            writer.write(" SudehAvatar was wandering in the nightclub for " + wander + " turns.\n");
            
            writer.write(" @ BAR                       " + drink + " turns.\n");
            writer.write(" @ DANCEFLOOR                " + dance + " turns.\n");
            writer.write(" @ DJBOOTH                   " + dj + " turns.\n");
            writer.write(" @ TOILET                    " + peeOrDrug + " turns.\n");
            writer.write(" @ SEATS                     " + rest + " turns.\n");
            writer.write(" followed another avatar for " + followCounter + " turns. In total followed "+talk+" Avatars\n");
            writer.write("+=============================================================================+\n");
            for (int i = 0; i < seenEnvironment.length; i++) {
                for (int j = 0; j < seenEnvironment[i].length; j++) {
                    char cellSymbol;
                    switch (seenEnvironment[i][j]) {
                        case 0 -> cellSymbol = '.';
                        case 1 -> cellSymbol = 'B';
                        case 2 -> cellSymbol = 'A';
                        case 3 -> cellSymbol = 'D';
                        case 4 -> cellSymbol = ' ';
                        case 5 -> cellSymbol = 'J';
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
            writer.write("3 -> D:\tDANCEFLOOR\n");
            writer.write("4 ->  :\tEMPTY\n");
            writer.write("5 -> J:\tDJBOOTH\n");
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
