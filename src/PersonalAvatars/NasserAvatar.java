package PersonalAvatars;

import java.awt.Color;
import java.util.ArrayList;
import Environment.*;
import AvatarInterface.*;

public class NasserAvatar extends SuperAvatar {
    private int count = 0;
    private int count1 =0;
    private int count2 = 0;
    private SpaceInfo left;
    private SpaceInfo right;
    private SpaceInfo top;
    private int middleX;
    private int middleY;
    private ArrayList<SpaceInfo> allSpaceInfos;


    public NasserAvatar(int id, int perceptionRange, Color color) {
        super(id, perceptionRange, color); // Leverage the super class to handle ID and perceptionRange
        this.allSpaceInfos = new ArrayList<>();
    }


    public void changeToRelativeToCoordinate(ArrayList<SpaceInfo>spacesInRange){

        for(int i = 0; i<spacesInRange.size();i++){
            SpaceInfo spaceinfo = spacesInRange.get(i);
            allSpaceInfos.add(spaceinfo);
        }
                                          
                     
        SpaceInfo leftTop = spacesInRange.get(0);
        Coordinate leftTopCoordinate = leftTop.getRelativeToAvatarCoordinate();
       // System.out.println("x-Koordinate: " + leftTopCoordinate.getX() + " y-Koordinate: " + leftTopCoordinate.getY()+" Spacetyp: " + leftTop.getType());

        //System.out.println();

        left = spacesInRange.get(1);
        Coordinate leftCoordinate = left.getRelativeToAvatarCoordinate();
        //System.out.println("x-Koordinate: " + leftCoordinate.getX() + " y-Koordinate: " + leftCoordinate.getY()+" Spacetyp: " + left.getType());

        //System.out.println();

        SpaceInfo leftBottom = spacesInRange.get(2);
        Coordinate leftBottomCoordinate = leftBottom.getRelativeToAvatarCoordinate();
        //System.out.println("x-Koordinate: " + leftBottomCoordinate.getX() + " y-Koordinate: " + leftBottomCoordinate.getY()+" Spacetyp: " + leftBottom.getType());

        
        //System.out.println();

        top = spacesInRange.get(3);
        Coordinate topCoordinate = top.getRelativeToAvatarCoordinate();
        //System.out.println("x-Koordinate: " + topCoordinate.getX() + " y-Koordinate: " + topCoordinate.getY()+" Spacetyp: " + top.getType());

        //System.out.println();

        SpaceInfo bottom = spacesInRange.get(4);
        Coordinate bottomCoordinate = bottom.getRelativeToAvatarCoordinate();
        //System.out.println("x-Koordinate: " + bottomCoordinate.getX() + " y-Koordinate: " + bottomCoordinate.getY()+" Spacetyp: " + bottom.getType());

       // System.out.println();

        SpaceInfo rightTop = spacesInRange.get(5);
        Coordinate rightTopCoordinate = rightTop.getRelativeToAvatarCoordinate();
        //System.out.println("x-Koordinate: " + rightTopCoordinate.getX() + " y-Koordinate: " + rightTopCoordinate.getY()+" Spacetyp: " + rightTop.getType());

        
        //System.out.println();

        right = spacesInRange.get(6);
        Coordinate rightCoordinate = right.getRelativeToAvatarCoordinate();
        //System.out.println("x-Koordinate: " + rightCoordinate.getX() + " y-Koordinate: " + rightCoordinate.getY()+" Spacetyp: " + right.getType());

        //System.out.println();

        SpaceInfo rightBottom = spacesInRange.get(7);
        Coordinate rightBottomCoordinate = rightBottom.getRelativeToAvatarCoordinate();
        //System.out.println("x-Koordinate: " + rightBottomCoordinate.getX() + " y-Koordinate: " + rightBottomCoordinate.getY()+" Spacetyp: " + rightBottom.getType());

        middleX = bottomCoordinate.getX();
        middleY = bottomCoordinate.getY()-1;
    }

    @Override
    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {
        changeToRelativeToCoordinate(spacesInRange);

        //Coordinate coordinateOfAvatar = infos.getRelativeToAvatarCoordinate();
        // Kreisförmige Bewegung in einem größeren Kreis
        return circularDirection();
    }

    private Direction circularDirection() {
    
        int currentPhase = 0; // Bestimmt die aktuelle Phase basierend auf der Anzahl der Schritte

        if("OBSTACLE".equals(String.valueOf(top.getType()))&&count == 0){
            currentPhase = 3;
            if("OBSTACLE".equals(String.valueOf(left.getType()))){
                count++;
            }
        }

        if("OBSTACLE".equals(String.valueOf(left.getType()))||count == 1){
            currentPhase = 1;

            if("OBSTACLE".equals(String.valueOf(right.getType()))){
                count = 2;
                count1 =0;
            }
        }

       if("OBSTACLE".equals(String.valueOf(right.getType()))||count == 3){
            currentPhase = 3;

            if("OBSTACLE".equals(String.valueOf(left.getType()))){
                count = 2;    
                count2 = 0;
            }
        }

        if(middleX == 1 && middleY == 18){
            return Direction.STAY;
        }

        if(count == 2){
            currentPhase = 2;
            count1++;
            count2++;
            if(count1 == 2){
            count = 3; 
            } else if(count2 ==2){
                count = 1;
            }
        }

 

        Direction direction = Direction.STAY; // Standardmäßig keine Bewegung

        switch (currentPhase) { // Wechselt die Richtung 
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
       // moveCounter++; // Erhöht den Zähler nach jedem Zug
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