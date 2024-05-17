package AvatarInterface;

import Environment.*;
import java.util.ArrayList;

/**
 * The AvatarInterface interface represents an avatar in a simulation.
 * It provides methods to control the avatar's actions and retrieve information about its surroundings.
 */
public interface AvatarInterface {
  
  /**
   * Determines the direction in which the avatar should move next.
   * 
   * @param spacesInRange an ArrayList of SpaceInfo objects representing the spaces within the avatar's range of perception
   * @return the Direction in which the avatar should move
   */
  abstract Direction yourTurn(ArrayList<SpaceInfo> spacesInRange);
  
  /**
   * Retrieves the perception range of the avatar.
   * 
   * @return the perception range of the avatar
   */
  abstract int getPerceptionRange();
  
  /**
   * Sets the perception range of the avatar.
   * 
   * @param perceptionRange the new perception range of the avatar
   */
  abstract void setPerceptionRange(int perceptionRange);
  
}
