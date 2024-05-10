package AvatarInterface;

import java.util.ArrayList;
import Environment.*;

public class TimAvatar extends SuperAvatar { // implements AvatarInterface
	public TimAvatar(int id) {
		super(id);
	}

	@Override
	public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {
		int max = 3;
		int min = 0;
		int directionNumber = (int) (Math.random() * ((max - min) + 1) + min);

		switch (directionNumber) {
			case 0: return Direction.LEFT;
			case 1: return Direction.RIGHT;
			case 2: return Direction.UP;
			case 3: return Direction.DOWN;

			default: return Direction.STAY;
		}	
	}

	
}