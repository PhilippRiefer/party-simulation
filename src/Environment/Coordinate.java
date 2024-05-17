package Environment;

import java.util.Objects;

/********************************************
 * Author: Soodeh,...
 * Version: v.1
 * Date:   20240418
 * ------------------------------------------
 * Description:  
 * This class can represent the coordinates of each grid point.
 ********************************************/

/**
 * Represents a coordinate in a two-dimensional space.
 */
public class Coordinate {
    private int x;
    private int y;

    /**
     * Constructs a Coordinate object with the specified x and y values.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x-coordinate of this Coordinate.
     *
     * @return the x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y-coordinate of this Coordinate.
     *
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Compares this coordinate with the specified object for equality.
     * The result is {@code true} if and only if the argument is not {@code null}
     * and is a
     * {@code Coordinate} object that has the same x and y values as this object.
     *
     * @param obj the object to compare this {@code Coordinate} against
     * @return {@code true} if the given object represents a {@code Coordinate}
     *         equivalent to this coordinate, {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Coordinate that = (Coordinate) obj;
        return x == that.x && y == that.y;
    }

    /**
     * Returns a hash code for this coordinate.
     * The hash code value of this object is computed using the x and y coordinates,
     * ensuring consistency with {@link #equals(Object)} such that equal coordinates
     * always produce the same hash code value.
     *
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }
}
