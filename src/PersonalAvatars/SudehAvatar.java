package PersonalAvatars;
/*
 * Author: Soodeh
 * Date: 20240516
 * Version: 0.1
 * 
 * Description: personal avatar class which extends SuperAvatar...
 */

import java.util.ArrayList;

import AvatarInterface.SuperAvatar;
import Environment.Direction;
import Environment.SpaceInfo;

public class SudehAvatar extends SuperAvatar {

    public SudehAvatar(int id, int perceptionRange) {
        super(id, perceptionRange);
        
    }

    @Override
    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {
        // TODO implement an algorithem to move around your avatar:
        //A possible strategy:
        //a loop that iterates over available dorections at enum DIRECTION (UP,RIGHT,DOWN,LEFT,STAY),
        //it checks for the free cells
        //and then choose a random free direction 
        //move there and its location get updated
        //and return the selected direction?

        //change this to an appropriate return
        return Direction.LEFT;
        
    }

}
