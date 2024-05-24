package Environment;

import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Environment {
    private Room model;
    private SimulationGUI view;

    public Environment() {
        this.model = new Room(20, 20);
        System.out.println("Room constructed");
        this.view = new SimulationGUI();
        System.out.println("View constructed");
<<<<<<< Updated upstream
=======
        
        // Place some Objects on the grid
        // getContentToTheGrid();

    }

    // SpaceType space = new Spacetype(SpaceType.BAR, coordinate);

    public void getContentToTheGrid(ArrayList<SpaceInfo> spacesInRange){
        // BAR
        // SpaceType type;
        // SpaceInfo space = spacesInRange.get(1);
        // type = space.setType();
        view.paintObject(2, 0, Color.BLACK);
        view.paintObject(2, 1, Color.BLACK);
        view.paintObject(2, 2, Color.BLACK);
        view.paintObject(2, 3, Color.BLACK);
        view.paintObject(2, 4, Color.BLACK);
        view.paintObject(2, 5, Color.BLACK);
        view.paintObject(1, 5, Color.BLACK);
        view.paintObject(0, 5, Color.BLACK);
    }
>>>>>>> Stashed changes

        this.view.addSlideListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = view.getValue();
                switch (value) {
                    case 1: // Zoom in
                        System.out.println("Size changed to small");
                        view.repaint(value);
                        model.updateRoom(view.getNumCols(), view.getNumRows());
                        System.out.println(String.valueOf(view.getNumCols()));
                        break;
                    case 2: // original
                        System.out.println("Size changed to medium");
                        view.repaint(value);
                        break;
                    case 3: // Zoom out
                        System.out.println("Size changed to large");
                        view.repaint(value);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void placeAvatar(int avatarId) {
        model.findPlaceForAvatar(avatarId);

        // TODO actually implement code below: make room.findPlaceForAvatar()
        // return coordinate so view can represent the avatar in the grid
        // Coordinate avatarCoordinate = room.findPlaceForAvatar(avatarId);
        // view.paintAvatar(avatarCoordinate);
    }

    public ArrayList<SpaceInfo> getAdjacentToAvatar(int avatarId) {
        return model.getAdjacentToAvatar(avatarId);
    }

}
