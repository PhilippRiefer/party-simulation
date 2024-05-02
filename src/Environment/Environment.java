import java.util.ArrayList;
import java.util.HashMap;

public class Environment {
    private Room room;
    private SimulationGUI view;

    public Environment(){
        // this.room = new Room();
        this.room = new Room(20,20);
        System.out.println("Room constructed");
        this.view = new SimulationGUI();
        System.out.println("View constructed");
    }

    public void placeAvatar(int avatarId) {
        room.findPlaceForAvatar(avatarId);

        // TODO actually implement code below: make room.findPlaceForAvatar() 
        // return coordinate so view can represent the avatar in the grid
        // Coordinate avatarCoordinate = room.findPlaceForAvatar(avatarId);
        // view.paintAvatar(avatarCoordinate);
    }

    public ArrayList<SpaceInfo> getAdjacentToAvatar(int avatarId){
        return room.getAdjacentToAvatar(avatarId);
    }

    
    
}
