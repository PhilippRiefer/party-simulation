
/********************************************
 * Author: Soodeh, Ole, Paola
 * Version: v.2
 * Date:   20240412
 * ------------------------------------------
 * Description: This class is considered the 
 * main of the "Environment-Team".
 ********************************************/

public class Environment {
    //private Room room = new Room();
    private CustomPanel panel = new CustomPanel();
    private AvatarsLocations avatarsLocations = new AvatarsLocations();


    public void startEnvironment() {
        panel.createFrame();  // create the frame for our environment
    }

    public SpaceInfo getAdjacentToAvatar(int avatarId){
        // TODO objective of the sprint
        throw new UnsupportedOperationException("Unimplemented method 'getAdjacentToAvatar in class Environment'");

    }  
}
