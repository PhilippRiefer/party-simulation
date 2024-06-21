package Environment;

import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;

public class Minimap {
	public Minimap() {
		HashMap<Coordinate, SpaceType> minimap = new HashMap<>();
		minimap.put(new Coordinate(0,0), SpaceType.SELF);

		JFrame frame = new JFrame("Minimap");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 400); // Initial size, can be adjusted
		frame.setVisible(true);
		System.out.println("Minimap initialized");

		// Create a menu bar with a reset button
		JMenuBar menuBar = new JMenuBar();
		JButton resetMinimapButton = new JButton("Reset Minimap");
		menuBar.add(resetMinimapButton);
		frame.setJMenuBar(menuBar);

		resetMinimapButton.addActionListener(e -> {
			clearMinimap(minimap);
		});
	}

	public void updateTiles() {
		System.out.println("Minimap updated");
	}

	public void clearMinimap(HashMap<Coordinate, SpaceType> minimap) {
		minimap.clear();
		minimap.put(new Coordinate(0,0), SpaceType.SELF);
		updateTiles();
		System.out.println("Minimap cleared");
	}
}
