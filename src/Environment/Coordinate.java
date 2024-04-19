/********************************************
 * Author: Soodeh,...
 * Version: v.1
 * Date:   20240418
 * ------------------------------------------
 * Description:  
 * This class can represent the coordinates of each grid point.
 ********************************************/

 // change

public class Coordinate {
    private int x;
    private int y;

    // Constructor
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Getter methods
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // Setter methods (if needed)
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
