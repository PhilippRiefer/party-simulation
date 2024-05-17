package PersonalAvatars;

import java.util.ArrayList;
import Environment.*;
import AvatarInterface.*;

public class TimAvatar extends SuperAvatar { // implements AvatarInterface

	public TimAvatar(int id, int perceptionRange) {
		super(id, perceptionRange);
	}

	@Override
	public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {
		// TODO you have to check if the place you want to move to actually allows it

		int max = 3;
		int min = 0;
		int directionNumber = (int) (Math.random() * ((max - min) + 1) + min);

		switch (directionNumber) {
			case 0:
				return Direction.LEFT;
			case 1:
				return Direction.RIGHT;
			case 2:
				return Direction.UP;
			case 3:
				return Direction.DOWN;

			default:
				return Direction.STAY;
		}

	}

}

/*
 * EMPTY,
 * OBSTACLE,
 * AVATAR,
 * DANCEFLOOR,
 * DJBOOTH,
 * TOILET,
 * BAR,
 * SEATS
 */