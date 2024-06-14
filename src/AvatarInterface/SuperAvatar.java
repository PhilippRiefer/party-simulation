package AvatarInterface;

import java.awt.Color;

/**
 * Abstract base class for all avatar types in the simulation.
 */
public abstract class SuperAvatar implements AvatarInterface {

		private final int avatarID;		// identification of Avatar
		private boolean couldMove;		// Flag to check if the last move was successful
		private int perceptionRange;	// perception range of Avatar
		protected Color color;			// color of Avatar

		/**
		 * Constructs a SuperAvatar with a unique ID and perception range.
		 * @param id The unique identifier for the avatar.
		 * @param perceptionRange The range within which the avatar can perceive other objects.
		 * @param color The color of the avatar.
		 */
		public SuperAvatar(int id, int perceptionRange, Color color) {
			this.avatarID = id;
			this.perceptionRange = perceptionRange;
			this.color = color;
		}
		
		/**
		 * Returns the avatar's unique ID.
		 * @return The avatar's ID.
		 */
		public int getAvatarID() {
			return avatarID;
		}

		 /**
		 * Returns whether the avatar could move during the last turn.
		 * @return true if the avatar moved last turn, false otherwise.
		 */
		public boolean getCouldMove() {
			return couldMove;
		}

		/**
		 * Returns the avatar's perception range.
		 * @return The range within which the avatar can perceive other objects.
		 */
		public int getPerceptionRange() {
			return perceptionRange;
		}

		/**
		 * Sets the avatar's perception range.
		 * @param perceptionRange The new perception range to set.
		 */
		public void setPerceptionRange(int perceptionRange) {
			this.perceptionRange = perceptionRange;
		}
		
		/**
		 * Sets the avatar's movement status.
		 * @param couldMove The movement status to set.
		 */
		public void setHasMoved(boolean couldMove) {
			this.couldMove = couldMove;
		}

		/**
		 * Sets the avatar's color.
		 * @param color The color to set.
		 */
		public void setAvatarColor(Color color) {
			this.color = color;
		}

		/**
		 * Returns the avatar's color.
		 * @return The avatar's color.
		 */
		public Color getAvatarColor() {
			return color;
		}
}