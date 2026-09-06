package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.algs4.StdDraw;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class Engine {
    TERenderer ter = new TERenderer();
    GUI gui;
    /* Feel free to change the width and height. */
    public static final int WIDTH = 60;
    public static final int HEIGHT = 60;
    public static final int DIGIT = 10;
    private static File savefile = new File("./saveFile.txt");

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public Engine() {
        gui = new GUI(WIDTH, HEIGHT);
    }
    public void interactWithKeyboard() {
        char select = mainMenu();
        MapHandler mh;
        if (select == 'n' || select == 'N') {
            long seed = getMapSeed();
            mh = new MapHandler(WIDTH, HEIGHT, seed);
        } else if (select == 'l' || select == 'L') {
            mh = loadSaveFile();
            ter.initialize(WIDTH, HEIGHT);
            ter.renderFrame(mh.getMap());
        } else {
            System.exit(0);
            return;
        }

        //play
        ArrayList<Character> whiteList = new ArrayList<>(Arrays.asList('w', 'W', 'a', 'A', 's', 'S', 'd', 'D'));
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char input = StdDraw.nextKeyTyped();
                if (input == ':') {
                    while (true) {
                        if (StdDraw.hasNextKeyTyped()) {
                            char special = StdDraw.nextKeyTyped();
                            if (special == 'q' || special == 'Q') {
                                setSaveFile(mh);
                                System.exit(0);
                                return;
                            } else {
                                break;
                            }
                        }
                    }
                }
                if (whiteList.contains(input)) {
                    mh.moveCharacter(input);
                    ter.renderFrame(mh.getMap());
                }
            }
        }
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, running both of these:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        char[] inputArr = input.toCharArray();
        ArrayList<Character> inputList = new ArrayList<>();
        for (char in: inputArr) {
            inputList.add(in);
        }
        Character first = inputList.remove(0);
        MapHandler mh;
        if (first.equals('n') || first.equals('N')) {
            String seedString = "";
            Character curr = inputList.remove(0);
            while (!curr.equals('s') && !curr.equals('S')) {
                seedString += curr;
                curr = inputList.remove(0);
            }

            long seed = Long.parseLong(seedString);
            mh = new MapHandler(WIDTH, HEIGHT, seed);
        } else if (first.equals('l') || first.equals('L')) {
            //loading
            mh = loadSaveFile();
        } else {
            return new TETile[WIDTH][HEIGHT];
        }
        boolean flag = false;
        for (Character direction: inputList) {
            if (flag && (direction.equals('q') || direction.equals('Q'))) {
                setSaveFile(mh);
                break;
            }
            if (direction.equals(':')) {
                flag = true;
                continue;
            }
            mh.moveCharacter(direction);
        }
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(mh.getMap());
        return mh.getMap();

    }
    //this method returns valid char
    public char mainMenu() {
        ArrayList<Character> whiteList = new ArrayList<>(Arrays.asList('n', 'N', 'l', 'L', 'q', 'Q'));
        gui.mainMenu();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char input = StdDraw.nextKeyTyped();
                if (whiteList.contains(input)) {
                    return input;
                }
            }
        }
    }

    public long getMapSeed() {
        List<Character> list = Arrays.asList('1', '2', '3', '4', '5', '6', '7', '8', '9', '0');
        ArrayList<Character> whiteList = new ArrayList<>(list);
        ArrayList<Character> endChar = new ArrayList<>(Arrays.asList('s', 'S'));
        gui.setMapSeed("");
        int digit = 0;
        String mapSeed = "";
        while (digit <= DIGIT) {
            if (StdDraw.hasNextKeyTyped()) {
                Character input = StdDraw.nextKeyTyped();
                if (endChar.contains(input) && mapSeed.length() > 0) {
                    break;
                }
                if (whiteList.contains(input)) {
                    mapSeed += input;
                    digit++;
                    gui.setMapSeed(mapSeed);
                }
            }
        }
        return Long.parseLong(mapSeed);
    }

    public void setSaveFile(MapHandler mapHandler) {
        try {
            if (!savefile.exists()) {
                savefile.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(savefile);
            PrintWriter writer = new PrintWriter(fileWriter);

            int[] position = mapHandler.getCharacterPosition();

            writer.println(mapHandler.getMapSeed());
            writer.println(position[0]);
            writer.println(position[1]);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MapHandler loadSaveFile() {
        try {
            if (!savefile.exists()) {
                return null;
            }

            Scanner scanner = new Scanner(savefile);

            long seed = Long.parseLong(scanner.next());
            int[] position = new int[2];
            position[0] = Integer.parseInt(scanner.next());
            position[1] = Integer.parseInt(scanner.next());
            return new MapHandler(WIDTH, HEIGHT, position, seed);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
