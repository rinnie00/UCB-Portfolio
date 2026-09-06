package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapHandler {
    private long mapSeed;
    private int WIDTH;
    private int HEIGHT;
    private int[] characterPosition;
    private TETile[][] map;
    private List<Character> upward = new ArrayList<>(Arrays.asList('w', 'W'));

    private List<Character> downward = new ArrayList<>(Arrays.asList('s', 'S'));

    private List<Character> left = new ArrayList<>(Arrays.asList('a', 'A'));

    private List<Character> right = new ArrayList<>(Arrays.asList('d', 'D'));

    //make map with random map seed
    public MapHandler(int width, int height, long mapSeed) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.mapSeed = mapSeed;
        map = new TETile[WIDTH][HEIGHT];
        MapGenerator mg = new MapGenerator(map, this.mapSeed);
        mg.bsp();
        mg.drawRoom();
        mg.drawHallway();
        characterPosition = mg.getRandomRoomCenter();
        map[characterPosition[0]][characterPosition[1]] = Tileset.AVATAR;
    }

    //make map with save file
    public MapHandler(int width, int height, int[] initialPosition, long mapSeed) {
        if (initialPosition.length != 2) {
            return;
        }
        this.WIDTH = width;
        this.HEIGHT = height;
        this.mapSeed = mapSeed;
        map = new TETile[WIDTH][HEIGHT];
        MapGenerator mg = new MapGenerator(map, this.mapSeed);
        mg.bsp();
        mg.drawRoom();
        mg.drawHallway();
        characterPosition = initialPosition;
        map[characterPosition[0]][characterPosition[1]] = Tileset.AVATAR;
    }

    public TETile[][] getMap() {
        return map;
    }

    public int[] getCharacterPosition() { return characterPosition; }
    public long getMapSeed() {return mapSeed;}

    //move character
    private TETile[][] move(int[] position) {
        if (position.length != 2) {
            return map;
        }
        map[characterPosition[0]][characterPosition[1]] = Tileset.FLOOR;
        map[position[0]][position[1]] = Tileset.AVATAR;
        characterPosition = position;
        return map;
    }

    //check if the position is in bound
    private boolean checkPositionInBound(int[] position) {
        if (position[0] < 0 || position[0] >= WIDTH || position[1] < 0 || position[1] >= HEIGHT) {
            return false;
        }
        return true;
    }
    //check if the position is a floor
    private boolean isFloor(int[] position) {
        return map[position[0]][position[1]] == Tileset.FLOOR;
    }
    public TETile[][] moveCharacter(char direction) {
        int[] position = new int[2];
        if (upward.contains(direction)) {
            position[0] = characterPosition[0];
            position[1] = characterPosition[1] + 1;
        } else if (downward.contains(direction)) {
            position[0] = characterPosition[0];
            position[1] = characterPosition[1] - 1;
        } else if (left.contains(direction)) {
            position[0] = characterPosition[0] - 1;
            position[1] = characterPosition[1];
        } else if (right.contains(direction)) {
            position[0] = characterPosition[0] + 1;
            position[1] = characterPosition[1];
        }
        if (!checkPositionInBound(position) || !isFloor(position)) {
            return map;
        }
        return move(position);
    }
}
