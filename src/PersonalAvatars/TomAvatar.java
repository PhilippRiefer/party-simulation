package PersonalAvatars;
import Environment.*;

import java.util.ArrayList;

import javax.swing.DropMode;

import AvatarInterface.*;


public class TomAvatar extends SuperAvatar {


    public TomAvatar(int id) {
        super(id);
    }

    @Override
    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {

   
        for(SpaceInfo infos : spacesInRange){
            if(infos.getType() == SpaceType.AVATAR){
                Coordinate coordinateOfAvatar = infos.getRelativeToAvatarCoordinate();
                if(coordinateOfAvatar.getY() < 0)
                    return Direction.UP;
                if(coordinateOfAvatar.getY() > 0)
                    return Direction.DOWN;
                if(coordinateOfAvatar.getX() < 0)
                    return Direction.LEFT;
                if(coordinateOfAvatar.getX() > 0)
                    return Direction.RIGHT;
            }
            return Direction.STAY;
        }
        return Direction.STAY;
    }
}
