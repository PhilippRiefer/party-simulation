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
    SpaceType nextSpaceType = SpaceType.EMPTY;
    Coordinate currentCoordinate = new Coordinate(0, 0); // Initialisiere currentCoordinate
    Coordinate lastCoordinate = new Coordinate(0, 0); // Initialisiere currentCoordinate
    SpaceType[][] knownSpace = new SpaceType[40][20];
    int obstacleCounter = 0;

    ArrayList<Coordinate> Dancefloor = new ArrayList<>();
    ArrayList<Coordinate> Toilet = new ArrayList<>();
    ArrayList<Coordinate> Bar = new ArrayList<>();
    ArrayList<Coordinate> Seats = new ArrayList<>();
    ArrayList<Coordinate> DJBooth = new ArrayList<>();
    ArrayList<SpaceInfo> oldSpaceInRange = new ArrayList<>();

    Coordinate Steps = new Coordinate(0, 0);

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
        updateNeedsOverTime();
        applyRandomEvents();
        updateNeedsAfterAction(currentSpaceType);
    }


    Direction lastMove = null;
    public void calculateCurrentCoordinate(ArrayList<SpaceInfo> spacesInRange) {

     currentCoordinate.setX(spacesInRange.get(4).getRelativeToAvatarCoordinate().getX());
     currentCoordinate.setY(spacesInRange.get(4).getRelativeToAvatarCoordinate().getY() - 1);
        if(lastMove != null){
            switch (lastMove) {
            case RIGHT:
                currentSpaceType = oldSpaceInRange.get(6).getType(); 

                 break;
            case LEFT:
                currentSpaceType = oldSpaceInRange.get(1).getType();

            break;
            case DOWN:
                currentSpaceType = oldSpaceInRange.get(4).getType(); 

                break;
            case UP:
                currentSpaceType = oldSpaceInRange.get(3).getType();
            break;
            default:
                break;
            }
            oldSpaceInRange.clear();
            oldSpaceInRange.addAll(spacesInRange);
    
        }
        else{
            currentSpaceType = SpaceType.EMPTY;
            oldSpaceInRange.clear();
            oldSpaceInRange.addAll(spacesInRange);
        }
    }

    private void updateNeedsOverTime() {
        double thirstChange = 0.1;
        double hungerChange = 0.1;
        double bladderChange = 0.1;
        double physicalEnergyChange = -0.1;
        double funChange = -0.1;
        double socialEnergyChange = -0.1;

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

       // System.out.println("Thirst " + needs.get("thirst"));
       // System.out.println("hunger " + needs.get("hunger"));
      //  System.out.println("bladder " + needs.get("bladder"));
      //  System.out.println("physical energy " + needs.get("physical energy"));
      //  System.out.println("fun " + needs.get("fun"));
       // System.out.println("social energy " + needs.get("social energy"));
    }

    private void applyRandomEvents() {
        if (random.nextDouble() < 0.1) {
            int randomThirstIncrease = random.nextInt(80);
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
        needs.put("thirst", decreaseNeed(needs.get("thirst"), 3));
        needs.put("bladder", increaseNeed(needs.get("bladder"), 1));
        System.out.println("Der Avatar hat getrunken. Durst gesenkt, Blase erhöht.");
    }

    public void eat() {
        needs.put("hunger", decreaseNeed(needs.get("hunger"), 3));
        needs.put("physical energy", increaseNeed(needs.get("physical energy"), 1));
        System.out.println("Der Avatar hat gegessen. Hunger gesenkt, physische Energie erhöht.");
    }

    public void dance() {
        needs.put("fun", increaseNeed(needs.get("fun"), 3));
        needs.put("physical energy", decreaseNeed(needs.get("physical energy"), 1));
        needs.put("social energy", increaseNeed(needs.get("social energy"), 1));
        System.out.println("Der Avatar hat getanzt. Spaß und soziale Energie erhöht, physische Energie gesenkt.");
    }

    public void socialize() {
        needs.put("social energy", increaseNeed(needs.get("social energy"), 3));
        needs.put("fun", increaseNeed(needs.get("fun"), 2));
        System.out.println("Der Avatar hatte soziale Interaktion. Soziale Energie und Spaß erhöht.");
    }

    public void playMusic() {
        needs.put("physical energy", decreaseNeed(needs.get("physical energy"), 1));
        needs.put("fun", increaseNeed(needs.get("fun"), 2));
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

/**     
/////////////DoAction/////////////////
 */

    SpaceType destinationSpaceType;
    public Direction doAction(ArrayList<SpaceInfo> spacesInRange) {
        extendKnownSpace(spacesInRange);
        System.out.println("currentSpaceType:" + currentSpaceType);
        System.out.println("nextSpaceType:" + nextSpaceType);
        System.out.println("destination:" + destinationSpaceType);
        System.out.println("Steps x:" + Steps.getX() + " Steps y:" + Steps.getY());
        System.out.println("currentNeed:" + mostNeed);
       



        if(Steps.getX() == 0 && Steps.getY() == 0) {
            destinationSpaceType = makeSpaceTypeDecision();
            Steps = pathFinding(destinationSpaceType);
        }

        return coordianteSteps(Steps, spacesInRange);
    }
/**     
/////////////extendKnownSpace/////////////////
*/

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



/**     
/////////////makeSpaceTypeDecision/////////////////
- getMostUrgentNeed(needs)
*/

    String mostNeed;
    private SpaceType makeSpaceTypeDecision() {
        mostNeed = getMostUrgentNeed(needs);

        switch (mostNeed) {
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

/**     
//////////////getMostUrgentNeed////////////////
- calculatePriority
*/

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

/**     
//////////////calculatePriority////////////////
*/
    public static double calculatePriority(String need, double value) {
        switch (need) {
            case "thirst":
            case "hunger":
            case "bladder":
                
                return value;
            case "physical energy":
            case "fun":
            case "social energy":
                
                return 100 - value;
            default:
                throw new IllegalArgumentException("Unbekanntes Bedürfnis: " + need);
        }
    }
    

/**     
//////////////pathFinding////////////////
- randomIndex
- randomCoordinate
*/


    private Coordinate pathFinding(SpaceType spacetype) {
     Coordinate destination = null;
      if(spacetype != SpaceType.EMPTY && spacetype == currentSpaceType){
                return currentCoordinate;
      }
      else{

            switch (spacetype) {
             case DANCEFLOOR:
                destination = Dancefloor.isEmpty() ? null : Dancefloor.get(randomIndex(Dancefloor.size()));
                break;
             case DJBOOTH:
                destination = DJBooth.isEmpty() ? null : DJBooth.get(randomIndex(DJBooth.size()));
                break;
             case TOILET:
                destination = Toilet.isEmpty() ? null : Toilet.get(randomIndex(Toilet.size()));
                break;
             case BAR:
                destination = Bar.isEmpty() ? null : Bar.get(randomIndex(Bar.size()));
                break;
             case SEATS:
                destination = Seats.isEmpty() ? null : Seats.get(randomIndex(Seats.size()));
                break;
             default:
                break;
            }
        }

      
      
        if (destination == null) {
            destinationSpaceType = SpaceType.EMPTY;
          return new Coordinate(randomCoordinate().getX() - currentCoordinate.getX(),
                                randomCoordinate().getY() - currentCoordinate.getY());
        }
      
        // Calculate steps only if a destination is found
        return new Coordinate(
            (destination.getX() - currentCoordinate.getX()),
            destination.getY() - currentCoordinate.getY()
        );
    }


/**     
//////////////randomIndex////////////////
*/

    private int randomIndex(int index){
        random = new Random();

        return random.nextInt(index);
    }

/**     
//////////////randomCoordinate////////////////
*/

    private Coordinate randomCoordinate() {
        int randomX = 0;
        int randomY = 0;
      
          randomX = (int) (Math.random() * 40);
          randomY = (int) (Math.random() * 20);

          if(randomX == 0)
          {
            randomX++;
          }

          if(randomY == 0)
          {
            randomY++;
          }
        
        return new Coordinate(randomX, randomY);
      }



/**     
//////////////coordianteSteps////////////////
- calculateCurrentSpaceType
*/

    private Direction coordianteSteps(Coordinate Steps, ArrayList<SpaceInfo> spacesInRange) {
        Coordinate nextCoordinate = new Coordinate(0, 0);

        if (Steps.getY() > 0){

            nextCoordinate.setY(currentCoordinate.getY() + 1);
            nextCoordinate.setX(currentCoordinate.getX());

            if(itsFree(nextCoordinate, spacesInRange)){
                Steps.setY(Steps.getY() - 1);
                lastMove = Direction.DOWN;
                return Direction.DOWN;
            }
            else{
                lastMove = Direction.STAY;
                return Direction.STAY;
            }

            
        }
            
        else if (Steps.getY() < 0) {
        
            nextCoordinate.setY(currentCoordinate.getY() - 1);
            nextCoordinate.setX(currentCoordinate.getX());

            if(itsFree(nextCoordinate, spacesInRange)){
                Steps.setY(Steps.getY() + 1);
                lastMove = Direction.UP;
                return Direction.UP;
            }
            else{
                lastMove = Direction.STAY;
                return Direction.STAY;
            }
        }
          
        else if (Steps.getX() < 0) {
            

            nextCoordinate.setX(currentCoordinate.getX() - 1);
            nextCoordinate.setY(currentCoordinate.getY());

            if(itsFree(nextCoordinate, spacesInRange)){
                Steps.setX(Steps.getX() + 1);
                lastMove = Direction.LEFT;
                return Direction.LEFT;
            }
            else{
                lastMove = Direction.STAY;
                return Direction.STAY;
            }
        }
            
        else if (Steps.getX() > 0) {
            

            nextCoordinate.setX(currentCoordinate.getX() + 1);
            nextCoordinate.setY(currentCoordinate.getY());

            if(itsFree(nextCoordinate, spacesInRange)){
                Steps.setX(Steps.getX() - 1);
                lastMove = Direction.RIGHT;
                return Direction.RIGHT;
            }
            else{
                lastMove = Direction.STAY;
                return Direction.STAY;
            }   
        }
        else{
            lastMove = Direction.STAY;
            return Direction.STAY;
        }      
    
      }
      int wait = 0;
    private Boolean itsFree(Coordinate nextCoordinate, ArrayList<SpaceInfo> spacesInRange){
    
        for(SpaceInfo spaceInfo : spacesInRange){
            if(spaceInfo.getRelativeToAvatarCoordinate().getX() == nextCoordinate.getX() && spaceInfo.getRelativeToAvatarCoordinate().getY() == nextCoordinate.getY()){
                if(spaceInfo.getType() == SpaceType.OBSTACLE){
                    Steps.setX(0);
                    Steps.setY(0);
                    return false;
                }
                else if(wait >= 3){
                    Steps.setX(0);
                    Steps.setY(0);
                    wait = 0;
                    return false;
                }
                else if(spaceInfo.getType() == SpaceType.AVATAR){
                    wait++;
                    return false;
                }
                else{
                    return true; 
                }
            }
        }
        return false;
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
