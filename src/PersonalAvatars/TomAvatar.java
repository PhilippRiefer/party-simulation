package PersonalAvatars;

import Environment.*;
import java.util.ArrayList;
import AvatarInterface.*;
import java.awt.Color;

public class TomAvatar extends SuperAvatar {

    private static final int mapSizeX = 200;
    private static final int mapSizeY = 200;
    private SpaceType[][] mentalMap;
    private Direction direction;
    private int xDirection;
    private int yDirection;

    public TomAvatar(int id, int perceptionRange, Color color) {
        super(id, perceptionRange, color);
        this.mentalMap = new SpaceType[mapSizeX][mapSizeY];
        this.direction = Direction.STAY;
        this.xDirection = 0;
        this.yDirection = 0;
    }

    @Override
    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {

        for (SpaceInfo infos : spacesInRange) {

            Coordinate coordinateOfAvatar = infos.getRelativeToAvatarCoordinate();

            // saving field infos and get Direction of the next empfty field
            if (coordinateOfAvatar.getY() < 0) {
                mentalMap[100][100 - yDirection] = infos.getType();

                if(infos.getType() == SpaceType.EMPTY)
                    direction = Direction.UP;
            }

            if (coordinateOfAvatar.getY() > 0) {
                mentalMap[100][100 + yDirection] = infos.getType();

                if(infos.getType() == SpaceType.EMPTY)
                    direction = Direction.DOWN;
            }

            if (coordinateOfAvatar.getX() < 0) {
                mentalMap[100 - xDirection][100] = infos.getType();

                if(infos.getType() == SpaceType.EMPTY)
                    direction = Direction.LEFT;
            }

            if (coordinateOfAvatar.getX() > 0) {
                mentalMap[100 + xDirection][100] = infos.getType();

                if(infos.getType() == SpaceType.EMPTY)
                    direction = Direction.UP;
            }

            return direction;

        }

        return Direction.STAY;
    }
}
