// package Environment;

// import java.awt.*;
// import java.util.ArrayList;
// import java.util.List;
// import javax.swing.*;

// /**
//  * The Minimap class represents a visual minimap of the environment.
//  * It updates dynamically based on the avatar's perception.
//  */
// public class Minimap {
//     private JFrame frame;
//     private MinimapPanel panel;
//     private List<Tile> tiles;

//     /**
//      * Constructs a Minimap with the initial perception range.
//      *
//      * @param initialPerceptionRange The initial perception range of the avatar.
//      */
//     public Minimap(int initialPerceptionRange) {
//         this.tiles = new ArrayList<>();
//         // Start with the avatar's initial position
//         this.tiles.add(new Tile(0, 0, Color.BLACK));
//         this.frame = new JFrame("Minimap");
//         this.panel = new MinimapPanel();
//         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         frame.add(panel);
//         frame.setSize(400, 400); // Initial size, can be adjusted
//         frame.setVisible(true);
//         System.out.println("Minimap initialized with perception range: " + initialPerceptionRange);
//     }

//     /**
//      * Updates the tiles displayed on the minimap.
//      *
//      * @param newTiles The new list of tiles to display.
//      */
//     public void updateTiles(List<Tile> newTiles) {
//         this.tiles = newTiles;
//         panel.repaint();
//         System.out.println("Minimap updated with new tiles: " + tiles.size());
//     }

//     /**
//      * The MinimapPanel class is a custom JPanel used for drawing the tiles on the minimap.
//      */
//     class MinimapPanel extends JPanel {
//         @Override
//         protected void paintComponent(Graphics g) {
//             super.paintComponent(g);
//             drawTiles(g);
//         }

//         /**
//          * Draws the tiles on the minimap.
//          *
//          * @param g The Graphics object used for drawing.
//          */
//         private void drawTiles(Graphics g) {
//             if (tiles.isEmpty()) {
//                 return;
//             }

//             // Find the min and max coordinates to determine the grid size
//             int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
//             int minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;

//             for (Tile tile : tiles) {
//                 if (tile.x < minX) minX = tile.x;
//                 if (tile.x > maxX) maxX = tile.x;
//                 if (tile.y < minY) minY = tile.y;
//                 if (tile.y > maxY) maxY = tile.y;
//             }

//             int numTilesX = maxX - minX + 1;
//             int numTilesY = maxY - minY + 1;

//             int tileWidth = getWidth() / numTilesX;
//             int tileHeight = getHeight() / numTilesY;

//             for (Tile tile : tiles) {
//                 int x = (tile.x - minX) * tileWidth;
//                 int y = (tile.y - minY) * tileHeight;
//                 g.setColor(tile.color);
//                 g.fillRect(x, y, tileWidth, tileHeight);
//             }
//             System.out.println("MinimapPanel painted " + tiles.size() + " tiles.");
//         }
//     }
// }
