package PersonalAvatars;

import java.awt.Color;
import java.util.ArrayList;
import Environment.*;
import AvatarInterface.*;

public class NasserAvatar extends SuperAvatar {
    private int moveCounter = 0; 
    private int stepsPerDirection = 3;
    

    public NasserAvatar(int id, int perceptionRange, Color color) {
        super(id, perceptionRange, color); // Leverage the super class to handle ID and perceptionRange
    }


    public void changeToRelativeToCoordinate(ArrayList<SpaceInfo>spacesInRange){
        SpaceInfo leftTop = spacesInRange.get(0);
        Coordinate leftTopCoordinate = leftTop.getRelativeToAvatarCoordinate();
        System.out.println("x-Koordinate: " + leftTopCoordinate.getX() + " y-Koordinate: " + leftTopCoordinate.getY()+" Spacetyp: " + leftTop.getType());

        System.out.println();

        SpaceInfo left = spacesInRange.get(1);
        Coordinate leftCoordinate = left.getRelativeToAvatarCoordinate();
        System.out.println("x-Koordinate: " + leftCoordinate.getX() + " y-Koordinate: " + leftCoordinate.getY()+" Spacetyp: " + left.getType());

        System.out.println();

        SpaceInfo leftBottom = spacesInRange.get(2);
        Coordinate leftBottomCoordinate = leftBottom.getRelativeToAvatarCoordinate();
        System.out.println("x-Koordinate: " + leftBottomCoordinate.getX() + " y-Koordinate: " + leftBottomCoordinate.getY()+" Spacetyp: " + leftBottom.getType());

        
        System.out.println();

        SpaceInfo top = spacesInRange.get(3);
        Coordinate topCoordinate = top.getRelativeToAvatarCoordinate();
        System.out.println("x-Koordinate: " + topCoordinate.getX() + " y-Koordinate: " + topCoordinate.getY()+" Spacetyp: " + top.getType());

        System.out.println();

        SpaceInfo bottom = spacesInRange.get(4);
        Coordinate bottomCoordinate = bottom.getRelativeToAvatarCoordinate();
        System.out.println("x-Koordinate: " + bottomCoordinate.getX() + " y-Koordinate: " + bottomCoordinate.getY()+" Spacetyp: " + bottom.getType());

        System.out.println();

        SpaceInfo rightTop = spacesInRange.get(5);
        Coordinate rightTopCoordinate = rightTop.getRelativeToAvatarCoordinate();
        System.out.println("x-Koordinate: " + rightTopCoordinate.getX() + " y-Koordinate: " + rightTopCoordinate.getY()+" Spacetyp: " + rightTop.getType());

        
        System.out.println();

        SpaceInfo right = spacesInRange.get(6);
        Coordinate rightCoordinate = right.getRelativeToAvatarCoordinate();
        System.out.println("x-Koordinate: " + rightCoordinate.getX() + " y-Koordinate: " + rightCoordinate.getY()+" Spacetyp: " + right.getType());

        System.out.println();

        SpaceInfo rightBottom = spacesInRange.get(7);
        Coordinate rightBottomCoordinate = rightBottom.getRelativeToAvatarCoordinate();
        System.out.println("x-Koordinate: " + rightBottomCoordinate.getX() + " y-Koordinate: " + rightBottomCoordinate.getY()+" Spacetyp: " + rightBottom.getType());
    }

    @Override
    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {
        changeToRelativeToCoordinate(spacesInRange);
        //Coordinate coordinateOfAvatar = infos.getRelativeToAvatarCoordinate();
        // Kreisförmige Bewegung in einem größeren Kreis
        return circularDirection();
    }

    private Direction circularDirection() {

        int currentPhase = moveCounter / stepsPerDirection; // Bestimmt die aktuelle Phase basierend auf der Anzahl der Schritte

        Direction direction = Direction.STAY; // Standardmäßig keine Bewegung

        switch (currentPhase % 4) { // Wechselt die Richtung 
            case 0:
                direction = Direction.UP;
                break;
            case 1:
                direction = Direction.RIGHT;
                break;
            case 2:
                direction = Direction.DOWN;
                break;
            case 3:
                direction = Direction.LEFT;
                break;
            default: return Direction.STAY;
        }
        moveCounter++; // Erhöht den Zähler nach jedem Zug
        return direction;
    }

    @Override
    public int getPerceptionRange() {
        return super.getPerceptionRange(); // Assuming SuperAvatar has a method to get the perception range
    }

    @Override
    public void setPerceptionRange(int perceptionRange) {
        super.setPerceptionRange(perceptionRange); // Set the perception range via the superclass method
    }
}

