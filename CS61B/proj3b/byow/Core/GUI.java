package byow.Core;

import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;

public class GUI {
    private int width, height;
    public static int BLOCK = 16;
    private Font fontBig = new Font("Monaco", Font.BOLD, 60);
    private Font fontMid = new Font("Monaco", Font.PLAIN, 30);
    public GUI(int width, int height) {
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * this.BLOCK, this.height * this.BLOCK);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
    }

    public void mainMenu() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(fontBig);
        StdDraw.text(width / 2, height / 4 * 3, "CS61B: THE GAME");
        StdDraw.setFont(fontMid);
        StdDraw.text(width / 2, height / 2 + 2, "New Game (N)");
        StdDraw.text(width / 2, height / 2, "Load Game (L)");
        StdDraw.text(width / 2, height / 2 - 2, "Quit (Q)");
        StdDraw.show();
    }

    public void setMapSeed(String mapSeed) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(fontBig);
        StdDraw.text(width / 2, height / 4 * 3, "Enter the map Seed");
        StdDraw.setFont(fontMid);
        String text = "seed: " + mapSeed;
        StdDraw.text(width / 2, height / 2, text);
    }
}
