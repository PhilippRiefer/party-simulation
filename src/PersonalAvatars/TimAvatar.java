package PersonalAvatars;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import AvatarInterface.*;
import Environment.*;

public class TimAvatar extends SuperAvatar {

    private final Map<String, Double> characterTraits;
    private HashMap<String, Double> needs = new HashMap<>();
    private SpaceType currentSpaceType = SpaceType.EMPTY;
    private Coordinate currentCoordinate = new Coordinate(0, 0);
    private Direction lastDirection;

    private TimAvatarMove mover = new TimAvatarMove();
    private TimAvatarUpdateNeeds updater = new TimAvatarUpdateNeeds();
    private TimAvatarCurrentSpace orientation = new TimAvatarCurrentSpace();

    public TimAvatar(int id, int perceptionRange, Color color) {
        super(id, perceptionRange, Color.ORANGE);

        HashMap<String, Double> tempCharacterTraits = new HashMap<>();

        tempCharacterTraits.put("active", 60.0);
        tempCharacterTraits.put("social", 60.0);
        tempCharacterTraits.put("good drinker", 40.0);
        tempCharacterTraits.put("good eater", 40.0);
        tempCharacterTraits.put("strong bladder", 20.0);

        characterTraits = Collections.unmodifiableMap(tempCharacterTraits);

        needs.put("thirst", 0.0);
        needs.put("hunger", 0.0);
        needs.put("bladder", 0.0);
        needs.put("physical energy", 100.0);
        needs.put("fun", 50.0);
        needs.put("social energy", 100.0);
    }

    public void updateNeeds() {
        updater.updateNeedsOverTime(needs, characterTraits);
        updater.applyRandomEvents(needs);
        updater.updateNeedsAfterAction(currentSpaceType, needs);
    }

    Coordinate steps = new Coordinate(0,0);
    private Direction doAction(ArrayList<SpaceInfo> spacesInRange) {
        if(steps.getX() == 0 && steps.getY() == 0) {
            SpaceType destinationSpaceType = updater.makeSpaceTypeDecision(needs);
            steps = mover.pathFinding(destinationSpaceType, currentCoordinate);
        }
        lastDirection = mover.coordinateSteps(steps, spacesInRange);
        return lastDirection;
    }


    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {
        currentCoordinate = orientation.calculateCurrentCoordinate(spacesInRange);
        currentSpaceType = orientation.calculateCurrentSpaceType(spacesInRange, spacesInRange, null, currentSpaceType);
        updateNeeds();
        return doAction(spacesInRange);
    }

    @Override
    public int getPerceptionRange() {
        return super.getPerceptionRange();
    }

    @Override
    public void setPerceptionRange(int perceptionRange) {
        super.setPerceptionRange(perceptionRange);
    }
}
