package PersonalAvatars;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.lang.Math;

import Environment.*;
import AvatarInterface.*;


public class TimAvatar extends SuperAvatar { // implements AvatarInterface

	public TimAvatar(int id, int perceptionRange, Color color) {
        super(id, perceptionRange, color); // leverage the super class to handle ID and perceptionRange
    }

	//NICHT FERTIG
	public void playMusic() {
		needs.put("physical energy", decreaseNeed(needs.get("social energy"), 30));
		needs.put("fun", increaseNeed(needs.get("fun"), 20));
	 	System.out.println("Der Avatar Musik aufgelegt. Physische Energie gesenkt, Spaß erhöht");
 }

 	//NICHT FERTIG
	 public void pee() {
		needs.put("bladder", increaseNeed(needs.get("social energy"), 30));
 		System.out.println("Der Avatar war auf Toilette.");
}

 	//NICHT FERTIG
 	public void relax() {
		needs.put("physical energy", increaseNeed(needs.get("social energy"), 30));
		needs.put("social energy", increaseNeed(needs.get("social energy"), 30));
 		System.out.println("Der Avatar hat sich entspannt.");
}

 
private void updateNeedsAfterAction(String currentSpaceType){
	
	switch(currentSpaceType){

		case "AVATAR":
			socialize();
		break;

		case "DANCEFLOOR":
			dance();
		break;

		case "DJBOOTH":
			playMusic();
		break;

		case "TOILET":
			pee();
		break;

		case "BAR":
			drink();
			eat();
		break;

		case "SEATS":
			relax();
		break;

		default:
		break;

	}
}




Coordinate Steps = new Coordinate(1, 1);

public Direction doAction(ArrayList<SpaceInfo> spacesInRange) {

	extendKnownSpace(spacesInRange);

	if(Steps.getX() == 0 && Steps.getY() == 0){
		Steps = pathFinding(makeSpaceTypeDecision());
	}

	return coordianteSteps(Steps);
}


ArrayList<Coordinate> Dancefloor = new ArrayList<Coordinate>();
ArrayList<Coordinate> Toilet = new ArrayList<Coordinate>();
ArrayList<Coordinate> Bar = new ArrayList<Coordinate>();
ArrayList<Coordinate> Seats = new ArrayList<Coordinate>();
ArrayList<Coordinate> DJBooth = new ArrayList<Coordinate>();



	private void extendKnownSpace(ArrayList<SpaceInfo> spacesInRange){
		SpaceType type;
		Coordinate space = new Coordinate(0, 0);
	
		for(int i = 0; i < spacesInRange.size(); i++){
		
			space.setX(spacesInRange.get(i).getRelativeToAvatarCoordinate().getX());
			space.setY(spacesInRange.get(i).getRelativeToAvatarCoordinate().getY());
			type = spacesInRange.get(i).getType();
	
			knownSpace[space.getX()][space.getY()] = type;

			switch(type){
				case DANCEFLOOR:
				if(!Dancefloor.contains(space)){
					Dancefloor.add(space);
				}
				break;

				case DJBOOTH:
				if(!DJBooth.contains(space)){
					DJBooth.add(space);
				}
				break;

				case TOILET:
				if(!Toilet.contains(space)){
					Toilet.add(space);
				}
				break;

				case BAR:
				if(!Bar.contains(space)){
					Bar.add(space);
				}
				break;

				case SEATS:
				if(!Seats.contains(space)){
					Seats.add(space);
				}
				break;

				default:
				break;

			}

		}
	}

	private SpaceType makeSpaceTypeDecision(){
		SpaceType decision = SpaceType.BAR;
		return decision;
	}


	
	private Coordinate pathFinding(SpaceType spacetype){
		Coordinate destination = new Coordinate(0, 0);
		Coordinate Steps = new Coordinate(0, 0);
		
		
		switch(spacetype){
			case DANCEFLOOR:
			destination = Dancefloor.get(1);
			break;

			case DJBOOTH:
			destination = DJBooth.get(1);
			break;

			case TOILET:
			destination = Toilet.get(1);
			break;

			case BAR:
			destination = Bar.get(1);
			break;

			case SEATS:
			destination = Seats.get(1);
			break;

			default:
			break;
		}
		
		Steps.setX(destination.getX() - currentCoordinate.getX());
		Steps.setY(destination.getY() - currentCoordinate.getX());
		return Steps;
	}

	private Direction coordianteSteps(Coordinate Steps){
		
				// Postiver X Wert nach Rechts
				// Positver Y Wert nach Unten
		if(Steps.getY() > 0){
			Steps.setY(Steps.getY() - 1);
			return Direction.DOWN;
		}
		else if (Steps.getY() < 0) {
			Steps.setY(Steps.getY() + 1);
			return Direction.UP;
		}
		else if (Steps.getX() < 0) {
			Steps.setX(Steps.getY() + 1);
			return Direction.LEFT;
		}
		else if (Steps.getX() > 0) {
			Steps.setX(Steps.getY() - 1);
			return Direction.RIGHT;
		}
		else{
			return Direction.STAY;
		}
	
	}


	private Direction randomDirection() {
        int directionNumber = (int) (Math.random() * 4);

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
		updateNeeds();
        return randomDirection();
    }
	

  

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
