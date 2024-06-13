// package PersonalAvatars;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import Environment.*;
import SimulationControl.SimulationControl;
import AvatarInterface.*;

public class NasserAvatar extends SuperAvatar {
    private int plan1 = 0;
    private int count = 0;
    private int count1 = 0;
    private int count2 = 0;
    private int count3 = 0;
    private int count4 = 0;
    private int count5 = 0;
    private int count7 = 3;
    int secondmiddleY;
    private int excludedNumber = 6;
    private String prio = null;
    private SpaceInfo left;
    private SpaceInfo leftTop;
    private SpaceInfo leftBottom;
    private SpaceInfo right;
    private SpaceInfo rightTop;
    private SpaceInfo rightBottom;
    private SpaceInfo top;
    private SpaceInfo bottom;
    private int middleX;
    private int middleY;
    private ArrayList<SpaceInfo> allSpaceInfos;
    Coordinate closestCoordinate = new Coordinate(0, 0);
    private int minX = Integer.MAX_VALUE;
    private int maxX = Integer.MIN_VALUE;
    private int minY = Integer.MAX_VALUE;
    private int maxY = Integer.MIN_VALUE;

    public NasserAvatar(int id, int perceptionRange, Color color) {
        super(id, perceptionRange, Color.CYAN); // Leverage the super class to handle ID and perceptionRange
        this.allSpaceInfos = new ArrayList<>();
    }

    // Zufallszahlen bei dem eine bestimmte Zahl nicht ausgelassen wird
    // (excludedNumber)
    public int getRandomNumberExcluding(Random random, int min, int max, int excludedNumber) {
        int randomValue;
        do {
            randomValue = random.nextInt(max - min + 1) + min;
        } while (randomValue == excludedNumber);
        return randomValue;
    }

    // Grenzen des Dancefloors finden
    public void calculateDanceFloorBoundaries() {
        for (SpaceInfo info : allSpaceInfos) {
            if ("DANCEFLOOR".equals(String.valueOf(info.getType()))) {
                int x = info.getRelativeToAvatarCoordinate().getX();
                int y = info.getRelativeToAvatarCoordinate().getY();
                if (x < minX) {
                    minX = x;
                }

                if (x > maxX) {
                    maxX = x;
                }
                if (y < minY) {
                    minY = y;
                }
                if (y > maxY) {
                    maxY = y;
                }
            }
        }
        // Ausgabe der berechneten Grenzen für Debugging-Zwecke
        // System.out.println("DanceFloor Boundaries: minX=" + minX + ", maxX=" + maxX +
        // ", minY=" + minY + ", maxY=" + maxY);
    }

    public void changeToRelativeToCoordinate(ArrayList<SpaceInfo> spacesInRange) {

        // Iteriert durch jede SpaceInfo in der Liste spacesInRange
        for (SpaceInfo candidate : spacesInRange) {
            boolean isUnique = true;
            // Bekommt die Koordinaten des aktuellen SpaceInfo'-Objekts
            Coordinate candidateCoordinate = candidate.getRelativeToAvatarCoordinate();

            // Iteriert durch die bereits in allSpaceInfos gespeicherten SpaceInfo-Objekte
            // um gleiche Objekte zu finden
            for (SpaceInfo existing : allSpaceInfos) {
                // Bekommt die Koordinaten des aktuellen SpaceInfo'-Objekts in allSpaceInfos
                Coordinate existingCoordinate = existing.getRelativeToAvatarCoordinate();
                // Prüft ob die X- und Y-Koordinaten des Elements mit einem schon existierenden
                // Objekt übereinstimmen
                if (candidateCoordinate.getX() == existingCoordinate.getX() &&
                        candidateCoordinate.getY() == existingCoordinate.getY()) {
                    // Setzt isUnique auf false, wenn ein Duplikat gefunden wird
                    isUnique = false;
                    // Verlässt die innere Schleife da bereits ein Duplikat gefunden worden ist
                    break;
                }
            }

            // Fügt das Objekt zu allSpaceInfos hinzu wenn kein Duplikat gefunden worden ist
            if (isUnique) {
                allSpaceInfos.add(candidate);
            }
        }

        // Sortiert die Liste allSpaceInfos aufsteigend nach den X-Koordinaten der
        // SpaceInfo-Objekte
        // Beginnt mit 0,0 endet mit 39,19
        Collections.sort(allSpaceInfos, new Comparator<SpaceInfo>() {
            @Override
            public int compare(SpaceInfo o1, SpaceInfo o2) {
                // Vergleicht die X-Koordinaten von zwei SpaceInfo-Objekten für die Sortierung
                // mithilfe der compare Funktion
                return Integer.compare(o1.getRelativeToAvatarCoordinate().getX(),
                        o2.getRelativeToAvatarCoordinate().getX());
            }
        });

        // wenn plan1 = 1 ist dann heißt ist es das die Karte komplett rekonstruiert
        // worden ist
        // sorgt dafür das kein weiteres Element mehr in der Liste gespeichert werden
        // (unnötige Speicherung)
        if (plan1 == 1) {
            if (count4 == 0) {
                Random random = new Random();
                // Generiert eine Zufallszahl um eine Entscheidung für den nächsten Schritt zu
                // erhalten
                int randomValue = getRandomNumberExcluding(random, 0, 5, excludedNumber);
                // Damit ein Aktionsfeld nicht zweimal hintereinander gewählt wird
                excludedNumber = randomValue;
                //System.out.println("wert:" + randomValue);

                switch (randomValue) {
                    case 0:
                        // Setze eine zufällige Koordinate
                        closestCoordinate = new Coordinate(random.nextInt(38 - 1 + 1) + 1,
                                random.nextInt(18 - 1 + 1) + 1);
                       // System.out.println("X= " + closestCoordinate.getX() + " Y = " + closestCoordinate.getY());
                        count4 = 3;
                        break;
                    case 1:
                        prio = "SEATS";
                        count4++;
                        break;
                    case 2:
                        prio = "BAR";
                        count4++;
                        break;
                    case 3:
                        prio = "DJBOOTH";
                        count4++;
                        break;
                    case 4:
                        prio = "DANCEFLOOR";
                        count4++;
                        break;
                    case 5:
                        prio = "TOILET";
                        count4++;
                        break;
                    default:
                        prio = "EMPTY";
                }
            }

            // Berechnung des kürzesten Weges eines Feldes zu der aktuellen Position
            // Count4 wird verwendet damit die Berechnung nicht immer neustarten nach jedem
            // Schritt
            if (count4 == 1) {
                // Initialisiert die Distanz mit dem höchsten Wert damit der Vergleich starten
                // kann
                int minDistance = Integer.MAX_VALUE;

                // Durchläuft die Liste um den kürzesten Weg zu finden zu dem gewählten
                // Aktionsfeld prio
                for (SpaceInfo spaceInfo : allSpaceInfos) {
                    // prio = gewähltes Aktionsfeld
                    if (prio.equals(String.valueOf(spaceInfo.getType()))) {
                        Coordinate coordinate = spaceInfo.getRelativeToAvatarCoordinate();
                        int distance = Math.abs(coordinate.getX() - middleX) + Math.abs(coordinate.getY() - middleY);
                        if (distance < minDistance) {
                            minDistance = distance;
                            closestCoordinate = coordinate;
                        }
                    }
                }

                // Ausgabe der Koordinaten des nächstgelegenen Punktes zur Bar
                /*if (closestCoordinate != null) {
                    System.out.println("Nächste Koordinaten zur " + prio + " : x = " + closestCoordinate.getX()
                            + ", y = " + closestCoordinate.getY());
                } else {
                    System.out.println("Keine Bar gefunden.");
                }*/
                count4++;
            }
        }

        // Bekomme damit die ganzen Koordinaten aus allen Richtungen und kann damit
        // arbeiten
        leftTop = spacesInRange.get(0);
        Coordinate leftTopCoordinate = leftTop.getRelativeToAvatarCoordinate();
        // System.out.println("x-Koordinate: " + leftTopCoordinate.getX() + "
        // y-Koordinate: " + leftTopCoordinate.getY()+" Spacetyp: " +
        // leftTop.getType());

        // System.out.println();

        left = spacesInRange.get(1);
        Coordinate leftCoordinate = left.getRelativeToAvatarCoordinate();
        // System.out.println("x-Koordinate: " + leftCoordinate.getX() + " y-Koordinate:
        // " + leftCoordinate.getY()+" Spacetyp: " + left.getType());

        // System.out.println();

        leftBottom = spacesInRange.get(2);
        Coordinate leftBottomCoordinate = leftBottom.getRelativeToAvatarCoordinate();
        // System.out.println("x-Koordinate: " + leftBottomCoordinate.getX() + "
        // y-Koordinate: " + leftBottomCoordinate.getY()+" Spacetyp: " +
        // leftBottom.getType());

        // System.out.println();

        top = spacesInRange.get(3);
        Coordinate topCoordinate = top.getRelativeToAvatarCoordinate();
        // System.out.println("x-Koordinate: " + topCoordinate.getX() + " y-Koordinate:"
        // + topCoordinate.getY()+" Spacetyp: " + top.getType());

        // System.out.println();

        bottom = spacesInRange.get(4);
        Coordinate bottomCoordinate = bottom.getRelativeToAvatarCoordinate();
        // System.out.println("x-Koordinate: " + bottomCoordinate.getX() + "
        // y-Koordinate: " + bottomCoordinate.getY()+" Spacetyp: " + bottom.getType());

        // System.out.println();

        rightTop = spacesInRange.get(5);
        Coordinate rightTopCoordinate = rightTop.getRelativeToAvatarCoordinate();
        // System.out.println("x-Koordinate: " + rightTopCoordinate.getX() + "
        // y-Koordinate: " + rightTopCoordinate.getY()+" Spacetyp: " +
        // rightTop.getType());

        // System.out.println();

        right = spacesInRange.get(6);
        Coordinate rightCoordinate = right.getRelativeToAvatarCoordinate();
        // System.out.println("x-Koordinate: " + rightCoordinate.getX() + "
        // y-Koordinate: " + rightCoordinate.getY()+" Spacetyp: " + right.getType());

        // System.out.println();

        rightBottom = spacesInRange.get(7);
        Coordinate rightBottomCoordinate = rightBottom.getRelativeToAvatarCoordinate();
        // System.out.println("x-Koordinate: " + rightBottomCoordinate.getX() + "
        // y-Koordinate: " + rightBottomCoordinate.getY()+" Spacetyp: " +
        // rightBottom.getType());

        // Koordinaten wo der Avatar sich genau befindet
        middleX = bottomCoordinate.getX();
        middleY = bottomCoordinate.getY() - 1;
    }

    @Override
    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {
        changeToRelativeToCoordinate(spacesInRange);
        calculateDanceFloorBoundaries();
        return avatarDirection();
    }

    public Direction avatarDirection() {

        Random random = new Random();
        int nextmove;

        int currentPhase = 0; // Richtung ist geradeaus
        // wenn plan1 = 0 ist dann findet erstmal die Rekonstruktion der Karte statt
        if (plan1 == 0) {
            // Avatar geht zuerst ganz nach oben und dann ganz nach links
            if ("OBSTACLE".equals(String.valueOf(top.getType())) && count == 0) {
                currentPhase = 3;
                if ("OBSTACLE".equals(String.valueOf(left.getType()))) {
                    count++;
                }
            }

            // Geht nach rechts bis es gegen die Wand kommt
            if (/* "OBSTACLE".equals(String.valueOf(left.getType()))|| */count == 1) {
                currentPhase = 1;

                if ("OBSTACLE".equals(String.valueOf(right.getType()))) {
                    count = 2;
                    count1 = 0;
                }
            }

            // Geht nach links bis es gegen die Wand kommt
            if (/* "OBSTACLE".equals(String.valueOf(right.getType()))|| */count == 3) {
                currentPhase = 3;

                if ("OBSTACLE".equals(String.valueOf(left.getType()))) {
                    count = 2;
                    count2 = 0;
                }
            }

            // Wenn Avatar am Ende der Rekonstruktion angekommen ist
            if ("OBSTACLE".equals(String.valueOf(right.getType()))
                    && "OBSTACLE".equals(String.valueOf(rightBottom.getType()))
                    && "OBSTACLE".equals(String.valueOf(bottom.getType()))/* middleX == 38 && middleY == 18 */) {
                if (count3 <= 1) {
                    count3++;
                    return Direction.STAY;
                } else {
                    plan1++;
                    // Letzte Durchführung
                }
            }

            // Geht drei Schritte nach unten
            if (count == 2) {
                /*
                 * if (middleX == 1 && middleY == 17) {// Auf den Koordinaten geht er dann nur
                 * zwei Schritte nach unten
                 * count1++;
                 * count2++;
                 * }
                 */
                currentPhase = 2;
                count1++;
                count2++;
                if (count1 == 3) {
                    count = 3;
                } else if (count2 == 3) {
                    count = 1;
                }
            }
        }

        // Priorität startet, da Rekonstruktion abgeschlossen ist mit plan1=0
        else if (plan1 == 1) {
            // SimulationControl.wait(20); // Geschwindigkeit des Avatars wird langsamer
            // gemacht
            // Gehe in Richtung der nächsten Koordinaten zu prio
            if (middleX < closestCoordinate.getX()) {

                if (("AVATAR".equals(String.valueOf(right.getType())))) {
                    nextmove = random.nextInt(2);
                    count7 = nextmove;
                    switch (nextmove) {
                        case 0:
                            return Direction.UP;
                        case 1:
                            return Direction.DOWN;

                    }
                }
                return Direction.RIGHT;
            } else if (middleX > closestCoordinate.getX()) {
                if (("AVATAR".equals(String.valueOf(left.getType())))) {
                    nextmove = random.nextInt(2);
                    count7 = nextmove;
                    switch (nextmove) {
                        case 0:
                            return Direction.UP;
                        case 1:
                            return Direction.DOWN;

                    }
                }
                return Direction.LEFT;
            } else if (middleY < closestCoordinate.getY()) {
                if (("AVATAR".equals(String.valueOf(bottom.getType())))) {
                    nextmove = random.nextInt(2);
                    count7 = nextmove;
                    switch (nextmove) {
                        case 0:
                            return Direction.RIGHT;
                        case 1:
                            return Direction.LEFT;

                    }
                }

                return Direction.DOWN;
            } else if (middleY > closestCoordinate.getY()) {
                if (("AVATAR".equals(String.valueOf(top.getType())))) {
                    nextmove = random.nextInt(2);
                    count7 = nextmove;
                    switch (nextmove) {
                        case 0:
                            return Direction.RIGHT;
                        case 1:
                            return Direction.LEFT;

                    }
                }
                return Direction.UP;
            } else {
                // Bleibe stehen, wenn die Koordinaten erreicht sind
                if ("BAR".equals(prio)) {
                    // SimulationControl.wait(20);
                } else if ("SEATS".equals(prio)) {
                    // SimulationControl.wait(20);
                } else if ("TOILETS".equals(prio)) {
                    // SimulationControl.wait(20);
                } else if ("DJBOOTH".equals(prio)) {
                    // SimulationControl.wait(20);
                } else if ("DANCEFLOOR".equals(prio)) {
                    // Gehe zur Mitte der Tanzfläche
                    closestCoordinate = new Coordinate(17, 8);
                    plan1 = 2; // wechsel von plan1=1 zu =2 um zur Mitte zu gehen
                }
                count4 = 0;
                return Direction.STAY;
            }
        }

        // Geht zur Mitte der Tanzfläche
        if (plan1 == 2) {
            // SimulationControl.wait(500);
            if (middleX < ((maxX + minX) / 2)) {
                if (("AVATAR".equals(String.valueOf(right.getType())))) {
                    nextmove = random.nextInt(2);
                    count7 = nextmove;
                    switch (nextmove) {
                        case 0:
                            return Direction.UP;
                        case 1:
                            return Direction.DOWN;

                    }
                }
                return Direction.RIGHT;
            } else if (middleX > ((maxX + minX) / 2)) {
                if (("AVATAR".equals(String.valueOf(left.getType())))) {
                    nextmove = random.nextInt(2);
                    count7 = nextmove;
                    switch (nextmove) {
                        case 0:
                            return Direction.UP;
                        case 1:
                            return Direction.DOWN;

                    }
                }
                return Direction.LEFT;
            } else if (middleY < ((maxY + minY) / 2)) {
                if (("AVATAR".equals(String.valueOf(bottom.getType())))) {
                    nextmove = random.nextInt(2);
                    count7 = nextmove;
                    switch (nextmove) {
                        case 0:
                            return Direction.RIGHT;
                        case 1:
                            return Direction.LEFT;

                    }
                }
                return Direction.DOWN;
            } else if (middleY > ((maxY + minY) / 2)) {
                if (("AVATAR".equals(String.valueOf(top.getType())))) {
                    nextmove = random.nextInt(2);
                    count7 = nextmove;
                    switch (nextmove) {
                        case 0:
                            return Direction.RIGHT;
                        case 1:
                            return Direction.LEFT;

                    }
                }
                return Direction.UP;
            } else {
                // Sobald der Avatar die Mitte der Tanzfläche erreicht hat setzt plan1 zurück
                // um wieder zur Priorität zurückzukehren
                plan1 = 3;
                return Direction.STAY;
            }
        }

        // Hier tanzt der Avatar jetzt verrückt und zufällig und bleibt aber dabei in
        // dem Dancefloor
        if (plan1 == 3) {
            // Random random = new Random();
            // SimulationControl.wait(50);
            int direction = random.nextInt(4);
            count5++;
            if (count5 == 60) { // 60 Tanzschritte führt
                plan1 = 1; // Setzt plan1 zurück um zur Priorität zurückzukehren
                count5 = 0;
            }
            // If Bedingung sorgen dafür, dass Avatar in dem Dancefloor bleibt
            switch (direction) {
                case 0:
                    if (middleY == minY/*
                                        * ((!"DANCEFLOOR".equals(String.valueOf(top.getType()))))&&((!"DANCEFLOOR".
                                        * equals(String.valueOf(leftTop.getType()))))
                                        * &&((!"DANCEFLOOR".equals(String.valueOf(rightTop.getType()))))
                                        */) {
                        if (("AVATAR".equals(String.valueOf(bottom.getType())))) {
                            nextmove = random.nextInt(2);
                            count7 = nextmove;
                            switch (nextmove) {
                                case 0:
                                    return Direction.RIGHT;
                                case 1:
                                    return Direction.LEFT;

                            }
                        }
                        return Direction.DOWN;
                    } else {
                        if (("AVATAR".equals(String.valueOf(top.getType())))) {
                            nextmove = random.nextInt(2);
                            count7 = nextmove;
                            switch (nextmove) {
                                case 0:
                                    return Direction.RIGHT;
                                case 1:
                                    return Direction.LEFT;

                            }
                        }
                        return Direction.UP;
                    }
                case 1:
                    if (middleX == maxX) {
                        if (("AVATAR".equals(String.valueOf(left.getType())))) {
                            nextmove = random.nextInt(2);
                            count7 = nextmove;
                            switch (nextmove) {
                                case 0:
                                    return Direction.UP;
                                case 1:
                                    return Direction.DOWN;

                            }
                        }
                        return Direction.LEFT;
                    }
                    if (("AVATAR".equals(String.valueOf(right.getType())))) {
                        nextmove = random.nextInt(2);
                        count7 = nextmove;
                        switch (nextmove) {
                            case 0:
                                return Direction.UP;
                            case 1:
                                return Direction.DOWN;

                        }
                    }
                    return Direction.RIGHT;
                case 2:
                    if (middleY == maxY) {
                        if (("AVATAR".equals(String.valueOf(top.getType())))) {
                            nextmove = random.nextInt(2);
                            count7 = nextmove;
                            switch (nextmove) {
                                case 0:
                                    return Direction.RIGHT;
                                case 1:
                                    return Direction.LEFT;

                            }
                        }
                        return Direction.UP;
                    }
                    if (("AVATAR".equals(String.valueOf(bottom.getType())))) {
                        nextmove = random.nextInt(2);
                        count7 = nextmove;
                        switch (nextmove) {
                            case 0:
                                return Direction.RIGHT;
                            case 1:
                                return Direction.LEFT;

                        }
                    }
                    return Direction.DOWN;
                case 3:
                    if (middleX == minX) {
                        if (("AVATAR".equals(String.valueOf(right.getType())))) {
                            nextmove = random.nextInt(2);
                            count7 = nextmove;
                            switch (nextmove) {
                                case 0:
                                    return Direction.UP;
                                case 1:
                                    return Direction.DOWN;

                            }
                        }
                        return Direction.RIGHT;
                    }
                    if (("AVATAR".equals(String.valueOf(left.getType())))) {
                        nextmove = random.nextInt(2);
                        count7 = nextmove;
                        switch (nextmove) {
                            case 0:
                                return Direction.UP;
                            case 1:
                                return Direction.DOWN;

                        }
                    }
                    return Direction.LEFT;
            }
        }

        Direction direction = Direction.STAY; // Standardmäßig keine Bewegung

        switch (currentPhase) { // Wechselt die Richtung
            case 0:
                if (("AVATAR".equals(String.valueOf(top.getType())))) {
                    nextmove = random.nextInt(2);
                    count7 = nextmove;
                    switch (nextmove) {
                        case 0:
                            return Direction.RIGHT;
                        case 1:
                            return Direction.LEFT;

                    }
                }
                direction = Direction.UP;
                break;
            case 1:
                if (("AVATAR".equals(String.valueOf(right.getType())))) {
                    nextmove = random.nextInt(2);
                    count7 = nextmove;
                    switch (nextmove) {
                        case 0:
                            return Direction.UP;
                        case 1:
                            return Direction.DOWN;

                    }
                }
                direction = Direction.RIGHT;
                break;
            case 2:
                if (("AVATAR".equals(String.valueOf(bottom.getType())))) {
                    nextmove = random.nextInt(2);
                    count7 = nextmove;
                    switch (nextmove) {
                        case 0:
                            return Direction.RIGHT;
                        case 1:
                            return Direction.LEFT;

                    }
                }
                direction = Direction.DOWN;
                break;
            case 3:
                if (("AVATAR".equals(String.valueOf(left.getType())))) {
                    nextmove = random.nextInt(2);
                    count7 = nextmove;
                    switch (nextmove) {
                        case 0:
                            return Direction.UP;
                        case 1:
                            return Direction.DOWN;

                    }
                }
                direction = Direction.LEFT;
                break;
            default:
                return Direction.STAY;
        }
        return direction;
    }

//     @Override
//     public int getPerceptionRange() {
//         return super.getPerceptionRange(); // Assuming SuperAvatar has a method to get the perception range
//     }

    @Override
    public void setPerceptionRange(int perceptionRange) {
        super.setPerceptionRange(perceptionRange); // Set the perception range via the superclass method
    }
}