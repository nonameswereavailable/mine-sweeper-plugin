package com.royalswans.minesweeper.sweeperfield;

import java.util.ArrayList;
import java.util.Random;

public class Field {
    private final int y;
    private final int width;
    private final int height;
    private final int size;
    private final long mines;
    private final Random rand = new Random();

    public Field(int startX, int startZ, int endX, int endZ, int y) {
        this.y = y;

        this.width = Math.abs(endX - startX);
        this.height = Math.abs(endZ - startZ);
        this.size = width * height;
        this.mines = (int) (size / 6.4);
    }

    public ArrayList<ArrayList<Character>> generateField() {
        ArrayList<ArrayList<Character>> field = new ArrayList<>();

        ArrayList<Character> arr;
        int randNum;

        for (int z = 0; z < height; z++) {
            arr = new ArrayList<>();

            for (int x = 0; x < width; x++) {
                randNum = rand.nextInt(size);

                if (randNum < mines) {
                    arr.add('*');
                }
                else {
                    arr.add(' ');
                }
            }

            field.add(arr);
        }

        int squareStartX;
        int squareStartZ;
        int squareEndX;
        int squareEndZ;
        int mineCount;
        ArrayList<Character> squareArr;


        for (int z = 0; z < height; z++) {
            arr = field.get(z);

            for (int x = 0; x < width; x++) {
                if (arr.get(x) == '*') continue;

                squareStartX = Math.max(x - 1, 0);
                squareStartZ = Math.max(z - 1, 0);
                squareEndX = Math.min(x + 1, width - 1);
                squareEndZ = Math.min(z + 1, height - 1);
                mineCount = 0;

                for (int squareZ = squareStartZ; squareZ <= squareEndZ; squareZ++) {
                    squareArr = field.get(squareZ);
                    for (int squareX = squareStartX; squareX <= squareEndX; squareX++) {
                        if (squareArr.get(squareX) == '*') {
                            mineCount++;
                        }
                    }
                }

                arr.set(x, (char) ('0' + mineCount));
            }

            field.set(z, arr);
        }

        return field;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getFreeSquares(ArrayList<ArrayList<Character>> fieldArr) {
        int count = 0;

        for (ArrayList<Character> arr : fieldArr) {
            for (char c : arr) {
                if (c != '*') {
                    count++;
                }
            }
        }

        return count;
    }
}
