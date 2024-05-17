package PersonalAvatars;

/********************************************
 * Author: Ole
 * Version: v.1
 * Date:   20240511
 * ------------------------------------------
 * Description: personal avatar of Ole 
 ********************************************/

import Environment.*;

import java.awt.Color;
import java.util.ArrayList;
import AvatarInterface.SuperAvatar;

public class OleAvatar extends SuperAvatar {

    // Coordinates
	Coordinate Up  = new Coordinate(0,-1);
	Coordinate Right  = new Coordinate(1, 0);
	Coordinate Down  = new Coordinate(0,1);
	Coordinate Left  = new Coordinate(-1,0);

    // Object with personal avatar id and
    // perception range of the avatar
    // ------------------------------------
    public OleAvatar(int id, int perceptionRange, Color color) {
        super(id, perceptionRange, color); // leverage the super class to handle ID and perceptionRange
    }

    @Override
    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {

        // for (int i=0;i<4;i++){
        //     if(spacesInRange.get(i).getRelativeToAvatarCoordinate().equals(Up)){
        //         SpaceType upTyp = spacesInRange.get(i).getType();
        //         Coordinate upCoord = spacesInRange.get(i).getRelativeToAvatarCoordinate();
        //     }
        //     else if(spacesInRange.get(i).getRelativeToAvatarCoordinate().equals(Rigth)){
        //         SpaceType rightTyp = spacesInRange.get(i).getType();
        //         Coordinate rightCoord = spacesInRange.get(i).getRelativeToAvatarCoordinate();
        //     }
        //     else if(spacesInRange.get(i).getRelativeToAvatarCoordinate().equals(Down)){
        //         SpaceType downTyp = spacesInRange.get(i).getType();
        //         Coordinate downCoord = spacesInRange.get(i).getRelativeToAvatarCoordinate();
        //     }
        //     else if(spacesInRange.get(i).getRelativeToAvatarCoordinate().equals(Left)){
        //         SpaceType leftTyp = spacesInRange.get(i).getType();
        //         Coordinate leftCoord = spacesInRange.get(i).getRelativeToAvatarCoordinate();
        //     }
        // } 

        return randomDirection();
    }

    // check the space in the avatars range and make a ranking
    // 1: BAR, 2: EMPTY, 3: DJBOOTH, 4: SEATS,
    // 5: DANCEFLOOR, 6: TOILET, 7: AVATAR, 8: OBSTACLE
    // ------------------------------------
    @SuppressWarnings("unlikely-arg-type")
    private int checkSpace(ArrayList<SpaceInfo> spacesInRange) {
        if (spacesInRange.equals("EMPTY")) {
            System.out.println("Desired place is empty. I would like to move there.");
            return 2;
        } else if (spacesInRange.equals("OBSTACLE")) {
            System.out.println("There is an obstacle in the targeted area. I cannot move there.");
            return 8;
        } else if (spacesInRange.equals("AVATAR")) {
            System.out.println("There is an other avatar in the targeted area. I cannot move there.");
            return 7;
        } else if (spacesInRange.equals("DANCEFLOOR")) {
            System.out.println("It looks as if there is a dance floor there. Get out of here quickly.");
            return 5;
        } else if (spacesInRange.equals("DJBOOTH")) {
            System.out.println("The music gets louder. There must be a DJ here.");
            return 3;
        } else if (spacesInRange.equals("TOILET")) {
            System.out.println("No, I don't have to yet.");
            return 6;
        } else if (spacesInRange.equals("BAR")) {
            System.out.println("Yes, I like it here.");
            return 1;
        } else if (spacesInRange.equals("SEATS")) {
            System.out.println("My legs are tired, i have to sit.");
            return 4;
        } else{
            return 9;
        }
    }

    // move in a random direction
    // ------------------------------------
    private Direction randomDirection() {
        int random = (int) (Math.random() * 4);
        switch (random) {
            case 0:
            System.out.println("(ID: "  + getAvatarID() + ") Ole -> left");
            return Direction.LEFT;
        case 1:
            System.out.println("(ID: "  + getAvatarID() + ") Ole -> right");
            return Direction.RIGHT;
        case 2:
            System.out.println("(ID: "  + getAvatarID() + ") Ole -> up");
            return Direction.UP;
        case 3:
            System.out.println("(ID: "  + getAvatarID() + ") Ole -> down");
            return Direction.DOWN;
        default:
            System.out.println("(ID: "  + getAvatarID() + ") Ole -> stay");
            return Direction.STAY;
        }
    }
}