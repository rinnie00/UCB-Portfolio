package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.*;

public class MapGenerator {
    /**
     * RectNode class make tree structure and contain information of the split rectangle.
     */
    private class RectNode {
        private RectNode left, right, parent;
        private Rect entry;
        public RectNode(RectNode left, RectNode right, RectNode parent, Rect rect) {
            this.left = left;
            this.right = right;
            this.parent = parent;
            this.entry = rect;
        }

        //only for test
        public void bfsPrint() {
            BFSIterator iterator = new BFSIterator(this);
            while (!iterator.hasNext()) {
                System.out.println(iterator.next().entry);
            }
        }

        private class BFSIterator implements Iterator<RectNode> {
            private Queue<RectNode> queue;
            public BFSIterator(RectNode root) {
                queue = new LinkedList<>();
                queue.add(root);
            }
            @Override
            public boolean hasNext() {
                return queue.isEmpty();
            }

            @Override
            public RectNode next() {
                RectNode result = queue.remove();
                if (result.left != null) {
                    queue.add(result.left);
                }
                if (result.right != null) {
                    queue.add(result.right);
                }
                return result;
            }
        }
    }

    /**
     * Rect class contain the information of the split rectangle.
     */
    private class Rect {
        int x, y;
        int width, height;
        public Rect(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ", " + width + ", " + height + ")";
        }
    }

    private final RectNode root;
    private final double MIN = 0.4, MAX = 0.6;
    private final int width, height;
    private final int maxDepth = 4;
    private final Random random;
    private TETile[][] tiles;
    public MapGenerator(TETile[][] tiles, long seed) {
        this.width = tiles.length;
        this.height = tiles[0].length;
        this.tiles = tiles;
        this.random = new Random(seed);
        this.root = new RectNode(null, null, null, new Rect(0, 0, this.width, this.height));
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                tiles[i][j] = Tileset.NOTHING;
            }
        }
    }

    //the result of the bsp algorithm will store private variable: root.
    public void bsp() {
        bsp(root, 0);
    }

    //recursive helper function of bsp
    private void bsp(RectNode node, int n) {
        if (n >= maxDepth) {
            return;
        }
        Rect leftEntry, rightEntry;
        if (node.entry.width >= node.entry.height) {
            int floor = (int) Math.floor(MIN * node.entry.width);
            int ceil = (int) Math.ceil(MAX * node.entry.width);
            int split = RandomUtils.uniform(random, floor, ceil);
            leftEntry = new Rect(node.entry.x, node.entry.y, split, node.entry.height);
            rightEntry = new Rect(node.entry.x + split, node.entry.y, node.entry.width - split, node.entry.height);
        } else {
            int floor = (int) Math.floor(MIN * node.entry.height);
            int ceil = (int) Math.ceil(MAX * node.entry.height);
            int split = RandomUtils.uniform(random, floor, ceil);
            leftEntry = new Rect(node.entry.x, node.entry.y, node.entry.width, split);
            rightEntry = new Rect(node.entry.x, node.entry.y + split, node.entry.width, node.entry.height - split);
        }
        node.left = new RectNode(null, null, node, leftEntry);
        node.right = new RectNode(null, null, node, rightEntry);
        bsp(node.left, n + 1);
        bsp(node.right, n + 1);
    }

    //draw a room using the information contained in the leaves of the bsp tree.
    //rooms must have walls and floors.
    public void drawRoom() {
        drawRoom(root);
    }

    private void drawRoom(RectNode node) {
        if (node.right != null) {
            drawRoom(node.right);
        }
        if (node.left != null) {
            drawRoom(node.left);
        }
        if (node.right == null && node.left == null) { //if node is the leaf ot the tree.
            drawRectangle(tiles, Room(node));
        }
    }

    private Rect Room(RectNode node) {
        int centX = getCenterX(node.entry);
        int centY = getCenterY(node.entry);
        int x = RandomUtils.uniform(random, node.entry.x, centX);
        int y = RandomUtils.uniform(random, node.entry.y, centY);
        int width = RandomUtils.uniform(random, centX - x + 2, centX - x + node.entry.width / 2);
        int height = RandomUtils.uniform(random, centY - y + 2, centY - y + node.entry.height / 2);
        return new Rect(x, y, width, height);
    }

    private void drawRectangle(TETile[][] tiles, Rect rect) {
        for (int i = rect.x; i < rect.x + rect.width; i++) {
            for (int j = rect.y; j < rect.y + rect.height; j++) {
                if (j == rect.y || j == rect.y + rect.height - 1){
                    tiles[i][j] = Tileset.WALL;
                } else if (i == rect.x || i == rect.x + rect.width - 1) {
                    tiles[i][j] = Tileset.WALL;
                } else {
                    tiles[i][j] = Tileset.FLOOR;
                }
            }
        }
    }

    //draw the hallway connecting the rooms.
    public void drawHallway() {
        drawHallway(root);
    }

    private void drawHallway(RectNode node) {
        if (node.left == null || node.right == null) {
            return;
        }
        if (getCenterX(node.left.entry) == getCenterX(node.right.entry)) {
            int start = Math.min(getCenterY(node.left.entry), getCenterY(node.right.entry));
            int end = Math.max(getCenterY(node.left.entry), getCenterY(node.right.entry));
            int x = getCenterX(node.left.entry);
            for (int i = start; i < end; i++) {
                if (tiles[x][i] == Tileset.WALL) {
                    if (tiles[x+1][i] == Tileset.FLOOR && tiles[x-1][i] == Tileset.FLOOR) {
                        tiles[x][i] = Tileset.FLOOR;
                        continue;
                    } else if (tiles[x+1][i] == Tileset.FLOOR) {
                        tiles[x-1][i] = Tileset.WALL;
                    } else if (tiles[x-1][i] == Tileset.FLOOR) {
                        tiles[x+1][i] = Tileset.WALL;
                    }
                }
                if (tiles[x+1][i] == Tileset.NOTHING) {
                    tiles[x+1][i] = Tileset.WALL;
                }
                if (tiles[x-1][i] == Tileset.NOTHING) {
                    tiles[x-1][i] = Tileset.WALL;
                }
                tiles[x][i] = Tileset.FLOOR;
            }
        } else if (getCenterY(node.left.entry) == getCenterY(node.right.entry)) {
            int start = Math.min(getCenterX(node.left.entry), getCenterX(node.right.entry));
            int end = Math.max(getCenterX(node.left.entry), getCenterX(node.right.entry));
            int y = getCenterY(node.left.entry);
            for (int i = start; i < end; i++) {
                if (tiles[i][y] == Tileset.WALL) {
                    if (tiles[i][y+1] == Tileset.FLOOR && tiles[i][y-1] == Tileset.FLOOR) {
                        tiles[i][y] = Tileset.FLOOR;
                        continue;
                    } else if (tiles[i][y+1] == Tileset.FLOOR) {
                        tiles[i][y-1] = Tileset.WALL;
                    } else if (tiles[i][y-1] == Tileset.FLOOR) {
                        tiles[i][y+1] = Tileset.WALL;
                    }
                }
                if (tiles[i][y+1]== Tileset.NOTHING) {
                    tiles[i][y+1] = Tileset.WALL;
                }
                if (tiles[i][y-1] == Tileset.NOTHING) {
                    tiles[i][y-1] = Tileset.WALL;
                }
                tiles[i][y] = Tileset.FLOOR;
            }
        }

        drawHallway(node.left);
        drawHallway(node.right);
    }

    private int getCenterX(Rect rect) {
        return rect.x + rect.width / 2;
    }

    private int getCenterY(Rect rect) {
        return rect.y + rect.height / 2;
    }

    public TETile[][] getMap() {
        return tiles;
    }

    public int[] getRandomRoomCenter() {
        int[] result = new int[2];
        RectNode curr = root;
        while (curr.right != null && curr.left != null) {
            double random = RandomUtils.uniform(this.random, 0.0, 1.0);
            if (random > 0.5) {
                curr = curr.left;
            } else {
                curr = curr.right;
            }
        }
        result[0] = curr.entry.x + curr.entry.width / 2;
        result[1] = curr.entry.y + curr.entry.height / 2;
        return result;
    }
}
