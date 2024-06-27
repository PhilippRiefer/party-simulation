package AvatarInterface;

import Environment.*;
import java.util.*;

/**
 * A* Pathfinding algorithm implementation to find the shortest path between two points in a grid.
 */
public class AStarPathfinding {

    /**
     * Finds the shortest path from the start coordinate to the goal coordinate, avoiding obstacles.
     *
     * @param start The starting coordinate.
     * @param goal The goal coordinate.
     * @param obstacles A set of coordinates representing obstacles.
     * @return A list of coordinates representing the path from start to goal.
     */
    public static List<Coordinate> findPath(Coordinate start, Coordinate goal, Set<Coordinate> obstacles) {
        System.out.println("A* Pathfinding started, from " + start + " to " + goal);

        // Priority queue to store nodes to be evaluated, sorted by f value (g + h).
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(Node::getF));
        // Map to store all nodes.
        Map<Coordinate, Node> allNodes = new HashMap<>();

        Node startNode = new Node(start, null, 0, heuristic(start, goal));
        openSet.add(startNode);
        allNodes.put(start, startNode);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            // If the goal is reached, reconstruct the path.
            if (current.getCoordinate().equals(goal)) {
                System.out.println("A* Pathfinding: Goal reached!");
                return reconstructPath(current);
            }

            // Evaluate neighbors.
            for (Coordinate neighbor : getNeighbors(current.getCoordinate())) {
                if (obstacles.contains(neighbor)) {
                    continue;
                }

                int tentativeG = current.getG() + 1;
                Node neighborNode = allNodes.getOrDefault(neighbor, new Node(neighbor, null, Integer.MAX_VALUE, heuristic(neighbor, goal)));

                // If a better path is found, update the node.
                if (tentativeG < neighborNode.getG()) {
                    neighborNode.setPrevious(current);
                    neighborNode.setG(tentativeG);
                    neighborNode.setF(tentativeG + neighborNode.getH());

                    if (!openSet.contains(neighborNode)) {
                        openSet.add(neighborNode);
                    }
                    allNodes.put(neighbor, neighborNode);
                }
            }
        }

        System.out.println("A* Pathfinding: No path found.");
        return Collections.emptyList(); // No path found
    }

    /**
     * Reconstructs the path from the goal to the start by following the previous nodes.
     *
     * @param current The goal node.
     * @return A list of coordinates representing the path from start to goal.
     */
    private static List<Coordinate> reconstructPath(Node current) {
        List<Coordinate> path = new ArrayList<>();
        while (current != null) {
            path.add(current.getCoordinate());
            current = current.getPrevious();
        }
        Collections.reverse(path);
        System.out.println("A* Pathfinding: Path reconstructed - " + path);
        return path;
    }

    /**
     * Calculates the heuristic value (Manhattan distance) between two coordinates.
     *
     * @param a The first coordinate.
     * @param b The second coordinate.
     * @return The heuristic value.
     */
    private static int heuristic(Coordinate a, Coordinate b) {
        return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
    }

    /**
     * Returns the neighboring coordinates of a given coordinate.
     *
     * @param coordinate The coordinate to find neighbors for.
     * @return A list of neighboring coordinates.
     */
    private static List<Coordinate> getNeighbors(Coordinate coordinate) {
        List<Coordinate> neighbors = new ArrayList<>();
        neighbors.add(new Coordinate(coordinate.getX() - 1, coordinate.getY()));
        neighbors.add(new Coordinate(coordinate.getX() + 1, coordinate.getY()));
        neighbors.add(new Coordinate(coordinate.getX(), coordinate.getY() - 1));
        neighbors.add(new Coordinate(coordinate.getX(), coordinate.getY() + 1));
        return neighbors;
    }
}

/**
 * Node class used in the A* pathfinding algorithm.
 */
class Node {
    private final Coordinate coordinate;
    private Node previous;
    private int g;
    private final int h;
    private int f;

    public Node(Coordinate coordinate, Node previous, int g, int h) {
        this.coordinate = coordinate;
        this.previous = previous;
        this.g = g;
        this.h = h;
        this.f = g + h;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public Node getPrevious() {
        return previous;
    }

    public void setPrevious(Node previous) {
        this.previous = previous;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getH() {
        return h;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    @Override
    public String toString() {
        return "Node{" +
                "coordinate=" + coordinate +
                ", g=" + g +
                ", h=" + h +
                ", f=" + f +
                '}';
    }
}
