package PersonalAvatars;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import Environment.*;
import AvatarInterface.*;

/**
 * This class represents a template avatar that extends the SuperAvatar class and implements the AvatarInterface.
 * It provides a basic implementation of the avatar's behavior and perception range.
 */
public class TimAvatar extends SuperAvatar { // implements AvatarInterface

	/**
     * Constructs a TemplateAvatar object with the specified ID and perception range.
     *
     * @param id              the ID of the avatar
     * @param perceptionRange the perception range of the avatar
     */

	private final Map<String, Integer> CharacterTraits;

    public TimAvatar(int id, int perceptionRange) {
		super(id, perceptionRange);

		HashMap<String, Integer> tempCharacterTraits = new HashMap<>();

		tempCharacterTraits.put("activ", 100);
		tempCharacterTraits.put("social", 60);
		tempCharacterTraits.put("good drinker", 80);
		tempCharacterTraits.put("good eater", 40);
		tempCharacterTraits.put("strong bladder", 20);

		CharacterTraits = Collections.unmodifiableMap(tempCharacterTraits);

		HashMap<String, Integer> needs = new HashMap<>();

		needs.put("thirst", 60);
		needs.put("hunger", 30);
		needs.put("bladder", 20);
		needs.put("physical energy", 20);
		needs.put("fun", 20);
		needs.put("social energy", 20);

	}

	x y type 

	

	private void extendKnownSpace(ArrayList<SpaceInfo> spacesInRange){
		SpaceInfo SpaceInFront = spacesInRange.get(0);
		SpaceInfo SpaceInBack = spacesInRange.get(1);
		SpaceInfo SpaceLeft = spacesInRange.get(2);
		SpaceInfo SpaceRight = spacesInRange.get(3);

		Coordinate FrontCoordinate = SpaceInFront.getRelativeToAvatarCoordinate();
		SpaceType FrontType = SpaceInFront.getType();

		Coordinate BackCoordinate = SpaceInBack.getRelativeToAvatarCoordinate();
		SpaceType BackType = SpaceInBack.getType();

		Coordinate LeftCoordinate = SpaceLeft.getRelativeToAvatarCoordinate();
		SpaceType LeftType = SpaceLeft.getType();

		Coordinate RightCoordinate = SpaceRight.getRelativeToAvatarCoordinate();
		SpaceType RightType = SpaceRight.getType();





	}

	private void calculationNeeds(){
				
	}

	private SpaceType makeSpaceTypeDecision(){
		SpaceType spaceType;

		return spaceType; 
	}

	

	private Coordinate searchInKnownSpace(SpaceType spaceType){
		Coordinate coordinate;

		return coordinate;
	}

	private int pathFinding(Coordinate coordinate){
		int direction = 4;


		return direction;
	}

	private Direction doAction(int direction){
		switch (direction) {
            case 0:
                return Direction.LEFT;
            case 1:
                return Direction.RIGHT;
            case 2:
                return Direction.UP;
            case 3:
                return Direction.DOWN;
            default:
                return Direction.STAY; // Safety net, though unnecessary as directionNumber is bound by 0-3
	}
}


    /**
     * Determines the direction for the avatar's next turn based on the spaces within its perception range.
     * This method can be overridden to implement a more sophisticated strategy.
     *
     * @param spacesInRange the list of spaces within the avatar's perception range
     * @return the direction for the avatar's next turn
     */
    @Override
    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {
		extendKnownSpace(spacesInRange);
		calculationNeeds();
		SpaceType spaceType = makeSpaceTypeDecision();
		Coordinate coordinate = searchInKnownSpace(spaceType);
		int direction = pathFinding(coordinate);
        return doAction(direction);
    }
	


    /**
     * Generates a random direction for the avatar to move.
     *
     * @return a random direction
     */
 
  

    /**
     * Returns the perception range of the avatar.
     *
     * @return the perception range
     */
    @Override
    public int getPerceptionRange() {
        return super.getPerceptionRange(); // Assuming SuperAvatar has a method to get the perception range
    }

    /**
     * Sets the perception range of the avatar.
     *
     * @param perceptionRange the perception range to set
     */
    @Override
    public void setPerceptionRange(int perceptionRange) {
        super.setPerceptionRange(perceptionRange); // Set the perception range via the superclass method
    }


}


/*EMPTY,
OBSTACLE,
AVATAR,
DANCEFLOOR,
DJBOOTH,
TOILET,
BAR,
SEATS*/


