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
    private final TETile[][] tiles;
    public MapGenerator(TETile[][] tiles, long seed) {
        this.width = tiles.length;
        this.height = tiles[0].length;
        this.tiles = tiles;
        this.random = new Random(seed);
        this.root = new RectNode(null, null, null, new Rect(0, 0, this.width, this.height));
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

    //set initial value
    public void initial() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                tiles[i][j] = Tileset.NOTHING;
            }
        }
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
            for (int i = node.entry.x; i < node.entry.x + node.entry.width; i++) {
                for (int j = node.entry.y; j < node.entry.y + node.entry.height; j++) {
                    if  (j == node.entry.y || j == node.entry.y + node.entry.height - 1) {
                        tiles[i][j] = Tileset.WALL;
                    } else if (i == node.entry.x || i == node.entry.x + node.entry.width - 1) {
                        tiles[i][j] = Tileset.WALL;
                    } else {
                        tiles[i][j] = Tileset.FLOOR;
                    }
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
                tiles[x][i] = Tileset.FLOOR;
            }
        } else if (getCenterY(node.left.entry) == getCenterY(node.right.entry)) {
            int start = Math.min(getCenterX(node.left.entry), getCenterX(node.right.entry));
            int end = Math.max(getCenterX(node.left.entry), getCenterX(node.right.entry));
            int y = getCenterY(node.left.entry);
            for (int i = start; i < end; i++) {
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
}
