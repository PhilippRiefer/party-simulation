package PersonalAvatars;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.lang.Math;

import Environment.*;
import AvatarInterface.*;
import java.awt.Color;

public class TimAvatar extends SuperAvatar {

    private final Map<String, Double> characterTraits;
    HashMap<String, Double> needs = new HashMap<>();
    Random random = new Random();
    SpaceType currentSpaceType = SpaceType.EMPTY;
    Coordinate currentCoordinate = new Coordinate(0, 0); // Initialisiere currentCoordinate
    SpaceType[][] knownSpace = new SpaceType[40][20];

    ArrayList<Coordinate> Dancefloor = new ArrayList<>();
    ArrayList<Coordinate> Toilet = new ArrayList<>();
    ArrayList<Coordinate> Bar = new ArrayList<>();
    ArrayList<Coordinate> Seats = new ArrayList<>();
    ArrayList<Coordinate> DJBooth = new ArrayList<>();

    Coordinate Steps = new Coordinate(0, 0);

    public TimAvatar(int id, int perceptionRange, Color color) {
        super(id, perceptionRange, color);

        HashMap<String, Double> tempCharacterTraits = new HashMap<>();

        tempCharacterTraits.put("active", 60.0);
        tempCharacterTraits.put("social", 60.0);
        tempCharacterTraits.put("good drinker", 80.0);
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
        updateNeedsOverTime();
        applyRandomEvents();
        updateNeedsAfterAction(currentSpaceType);
    }


    public void calculateCurrentCoordinate(ArrayList<SpaceInfo> spacesInRange) {
     currentCoordinate.setX(spacesInRange.get(4).getRelativeToAvatarCoordinate().getX());
     currentCoordinate.setY(spacesInRange.get(4).getRelativeToAvatarCoordinate().getY() - 1);

    }

    private void updateNeedsOverTime() {
        double thirstChange = 0.5;
        double hungerChange = 0.5;
        double bladderChange = 0.5;
        double physicalEnergyChange = -0.5;
        double funChange = -0.5;
        double socialEnergyChange = -0.5;

        thirstChange *= 1.0 - (characterTraits.get("good drinker") / 100.0);
        hungerChange *= 1.0 - (characterTraits.get("good eater") / 100.0);
        bladderChange *= 1.0 - (characterTraits.get("strong bladder") / 100.0);
        physicalEnergyChange *= 1.0 + (characterTraits.get("active") / 100.0);
        socialEnergyChange *= 1.0 + (characterTraits.get("social") / 100.0);

        needs.put("thirst", adjustNeed(needs.get("thirst"), thirstChange));
        needs.put("hunger", adjustNeed(needs.get("hunger"), hungerChange));
        needs.put("bladder", adjustNeed(needs.get("bladder"), bladderChange));
        needs.put("physical energy", adjustNeed(needs.get("physical energy"), physicalEnergyChange));
        needs.put("fun", adjustNeed(needs.get("fun"), funChange));
        needs.put("social energy", adjustNeed(needs.get("social energy"), socialEnergyChange));

        System.out.println("Thirst " + needs.get("thirst"));
        System.out.println("hunger " + needs.get("hunger"));
        System.out.println("bladder " + needs.get("bladder"));
        System.out.println("physical energy " + needs.get("physical energy"));
        System.out.println("fun " + needs.get("fun"));
        System.out.println("social energy " + needs.get("social energy"));
    }

    private void applyRandomEvents() {
        if (random.nextDouble() < 0.1) {
            int randomThirstIncrease = random.nextInt(10);
            needs.put("thirst", increaseNeed(needs.get("thirst"), randomThirstIncrease));
            System.out.println("Zufälliges Ereignis: Durst erhöht um " + randomThirstIncrease);
        }
    }

    private double adjustNeed(double currentValue, double change) {
        double newValue =  currentValue + change;
        return Math.max(0.0, Math.min(newValue, 100.0));
    }

    private double increaseNeed(double currentValue, double amount) {
        return Math.min(currentValue + amount, 100.0);
    }

    private double decreaseNeed(double currentValue, double amount) {
        return Math.max(currentValue - amount, 0.0);
    }

    public void drink() {
        needs.put("thirst", decreaseNeed(needs.get("thirst"), 30));
        needs.put("bladder", increaseNeed(needs.get("bladder"), 10));
        System.out.println("Der Avatar hat getrunken. Durst gesenkt, Blase erhöht.");
    }

    public void eat() {
        needs.put("hunger", decreaseNeed(needs.get("hunger"), 30));
        needs.put("physical energy", increaseNeed(needs.get("physical energy"), 10));
        System.out.println("Der Avatar hat gegessen. Hunger gesenkt, physische Energie erhöht.");
    }

    public void dance() {
        needs.put("fun", increaseNeed(needs.get("fun"), 30));
        needs.put("physical energy", decreaseNeed(needs.get("physical energy"), 10));
        needs.put("social energy", increaseNeed(needs.get("social energy"), 10));
        System.out.println("Der Avatar hat getanzt. Spaß und soziale Energie erhöht, physische Energie gesenkt.");
    }

    public void socialize() {
        needs.put("social energy", increaseNeed(needs.get("social energy"), 30));
        needs.put("fun", increaseNeed(needs.get("fun"), 20));
        System.out.println("Der Avatar hatte soziale Interaktion. Soziale Energie und Spaß erhöht.");
    }

    public void playMusic() {
        needs.put("physical energy", decreaseNeed(needs.get("physical energy"), 30));
        needs.put("fun", increaseNeed(needs.get("fun"), 20));
        System.out.println("Der Avatar hat Musik aufgelegt. Physische Energie gesenkt, Spaß erhöht.");
    }

    public void pee() {
        needs.put("bladder", decreaseNeed(needs.get("bladder"), 30));
        System.out.println("Der Avatar war auf Toilette.");
    }

    public void relax() {
        needs.put("physical energy", increaseNeed(needs.get("physical energy"), 30));
        needs.put("social energy", increaseNeed(needs.get("social energy"), 30));
        System.out.println("Der Avatar hat sich entspannt.");
    }

    private void updateNeedsAfterAction(SpaceType currentSpaceType) {
        switch(currentSpaceType) {
            case AVATAR:
                socialize();
                break;
            case DANCEFLOOR:
                dance();
                break;
            case DJBOOTH:
                playMusic();
                break;
            case TOILET:
                pee();
                break;
            case BAR:
                drink();
                eat();
                break;
            case SEATS:
                relax();
                break;
            default:
                break;
        }
    }

    public Direction doAction(ArrayList<SpaceInfo> spacesInRange) {
        extendKnownSpace(spacesInRange);

        if(Steps.getX() == 0 && Steps.getY() == 0) {
            Steps = pathFinding(makeSpaceTypeDecision());
        }

        return coordianteSteps(Steps, spacesInRange);
    }


    private void extendKnownSpace(ArrayList<SpaceInfo> spacesInRange) {
        for(SpaceInfo spaceInfo : spacesInRange) {
            Coordinate coordinate = new Coordinate(spaceInfo.getRelativeToAvatarCoordinate().getX(), spaceInfo.getRelativeToAvatarCoordinate().getY());
            SpaceType type = spaceInfo.getType();

            knownSpace[coordinate .getX()][coordinate .getY()] = type;

            switch(type) {
                case DANCEFLOOR:
                    if(!Dancefloor.contains(coordinate )) {
                        Dancefloor.add(coordinate );
                    }
                    break;
                case DJBOOTH:
                    if(!DJBooth.contains(coordinate )) {
                        DJBooth.add(coordinate );
                    }
                    break;
                case TOILET:
                    if(!Toilet.contains(coordinate )) {
                        Toilet.add(coordinate );
                    }
                    break;
                case BAR:
                    if(!Bar.contains(coordinate )) {
                        Bar.add(coordinate);
                    }
                    break;
                case SEATS:
                    if(!Seats.contains(coordinate )) {
                        Seats.add(coordinate );
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private SpaceType makeSpaceTypeDecision() {
        String mostUrgentNeed = getMostUrgentNeed(needs);

        switch (mostUrgentNeed) {
                case "thirst":
                return SpaceType.BAR;

                case "hunger":
                return SpaceType.BAR;


                case "bladder":
                return SpaceType.TOILET;

                case "physical energy":
                return SpaceType.SEATS;

                case "fun":
                return SpaceType.DJBOOTH;

                case "social energy":
                return SpaceType.DANCEFLOOR;
        
                default:
                 return SpaceType.EMPTY;
            }
        
    }

    public static String getMostUrgentNeed(Map<String, Double> needs) {
        String mostUrgentNeed = null;
        double highestPriority = Double.MIN_VALUE;

        for (Map.Entry<String, Double> entry : needs.entrySet()) {
            String need = entry.getKey();
            double value = entry.getValue();
            double priority = calculatePriority(need, value);

            if (priority > highestPriority) {
                highestPriority = priority;
                mostUrgentNeed = need;
            }
        }

        return mostUrgentNeed;
    }

    public static double calculatePriority(String need, double value) {
        switch (need) {
            case "thirst":
            case "hunger":
                
                return value;
            case "bladder":
            case "physical energy":
            case "fun":
            case "social energy":
                
                return 100 - value;
            default:
                throw new IllegalArgumentException("Unbekanntes Bedürfnis: " + need);
        }
    }



    private Coordinate pathFinding(SpaceType spacetype) {
        Coordinate destination = null;
      if(spacetype != SpaceType.EMPTY && spacetype == currentSpaceType){
                return new Coordinate(0, 0);
      }
      else{
        switch (spacetype) {
          case DANCEFLOOR:
            destination = Dancefloor.isEmpty() ? null : Dancefloor.get(0);
            break;
          case DJBOOTH:
            destination = DJBooth.isEmpty() ? null : DJBooth.get(0);
            break;
          case TOILET:
            destination = Toilet.isEmpty() ? null : Toilet.get(0);
            break;
          case BAR:
            destination = Bar.isEmpty() ? null : Bar.get(0);
            break;
          case SEATS:
            destination = Seats.isEmpty() ? null : Seats.get(0);
            break;
          default:
            break;
        }
}
      
        if (destination == null) {
          return new Coordinate(randomCoordinate().getX() - currentCoordinate.getX(),
                                randomCoordinate().getY() - currentCoordinate.getY());
        }
      
        // Calculate steps only if a destination is found
        return new Coordinate(
            destination.getX() - currentCoordinate.getX(),
            destination.getY() - currentCoordinate.getY()
        );
      }

      
    private Direction coordianteSteps(Coordinate Steps, ArrayList<SpaceInfo> spacesInRange) {
        Coordinate nextCoordinate = new Coordinate(0, 0);
        if (Steps.getY() > 0) {

            Steps.setY(Steps.getY() - 1);
            nextCoordinate.setY(currentCoordinate.getY() + 1);
            nextCoordinate.setX(currentCoordinate.getX());
            calculateCurrentSpaceType(spacesInRange, nextCoordinate);
            return Direction.DOWN;

        } else if (Steps.getY() < 0) {
            Steps.setY(Steps.getY() + 1);
            nextCoordinate.setY(currentCoordinate.getY() - 1);
            nextCoordinate.setX(currentCoordinate.getX());
            calculateCurrentSpaceType(spacesInRange, nextCoordinate);
            return Direction.UP;

        } else if (Steps.getX() < 0) {
            Steps.setX(Steps.getX() + 1);
            nextCoordinate.setX(currentCoordinate.getX() - 1);
            nextCoordinate.setY(currentCoordinate.getY());
            calculateCurrentSpaceType(spacesInRange, nextCoordinate);
            return Direction.LEFT;

        } else if (Steps.getX() > 0) {
            Steps.setX(Steps.getX() - 1);
            nextCoordinate.setX(currentCoordinate.getX() + 1);
            nextCoordinate.setY(currentCoordinate.getY());
            calculateCurrentSpaceType(spacesInRange, nextCoordinate);
            return Direction.RIGHT;
        } else {
            return Direction.STAY;
        }
    }

    public void calculateCurrentSpaceType(ArrayList<SpaceInfo> spacesInRange, Coordinate nextCoordinate) {
        for (SpaceInfo space : spacesInRange) {
            if (nextCoordinate.equals(space.getRelativeToAvatarCoordinate())) {
                currentSpaceType = space.getType();
                break;
            }
        }

    }
                
       

    private Coordinate randomCoordinate() {
        int randomX = 0;
        int randomY = 0;
      
          randomX = (int) (Math.random() * 40);
          randomY = (int) (Math.random() * 20);
        
        return new Coordinate(randomX, randomY);
      }

    @Override
    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {
        calculateCurrentCoordinate(spacesInRange);
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
