package battleship;

import java.util.Scanner;

public class Field {
    private final char[][] field;
    private final char[][] fogOfWarField;
    protected final char[][] namedField;
    private final int row, column;

    public Field(int row, int column) {
        this.row = row;
        this.column = column;
        field = new char[row][column];
        fogOfWarField = new char[row][column];
        namedField = new char[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                field[i][j] = '~';
                fogOfWarField[i][j] = '~';
            }
        }
    }

    private void printColumns() {
        System.out.print("  ");
        for (int i = 0; i < column; i++) {
            System.out.print(i + 1 + " ");
        }
        System.out.println();
    }

    private void printRow(char row) {
        System.out.print(row + " ");
//        for (int i = 0; i < this.row; i++) System.out.print(field[row][i] + " ");
    }

    public void printField(boolean fogOfWar) {
        printColumns();

        char currentRow = 'A';
        for (int i = 0; i < row; i++) {
            printRow(currentRow); // print current row letter
            for (int j = 0; j < column; j++) {
                if (fogOfWar) {
                    System.out.print(fogOfWarField[i][j] + " ");
                } else {
                    System.out.print(field[i][j] + " ");
                }
            }
            System.out.println();
            currentRow++;
        }
        System.out.println();
    }


    public void markCoords(String start, String end, Ship ship) {
        int[][] coords = translateCoords(start, end);

        if (coords[0][0] == coords[1][0]) {
            int direction = coords[1][1] - coords[0][1];
            if (direction > 0) putRight(coords, ship);
            else putLeft(coords, ship);
        } else if (coords[0][1] == coords[1][1]) {
            int direction = coords[1][0] - coords[0][0];
            if (direction > 0) putDown(coords, ship);
            else putUp(coords, ship);

        }
    }

    private void putRight(int[][] coords, Ship ship) {
        int idx = 0;
        for (int i = coords[0][1]; i <= coords[1][1]; i++) {
            field[coords[0][0]][i] = 'O';
            namedField[coords[0][0]][i] = ship.getLetter();
            ship.setCoords(idx, new int[]{coords[0][0], i});
            idx++;
        }
    }

    private void putLeft(int[][] coords, Ship ship) {
        int idx = 0;
        for (int i = coords[0][1]; i >= coords[1][1]; i--) {
            field[coords[0][0]][i] = 'O';
            namedField[coords[0][0]][i] = ship.getLetter();
            ship.setCoords(idx, new int[]{coords[0][0], i});
            idx++;
        }

    }

    private void putDown(int[][] coords, Ship ship) {
        int idx = 0;
        for (int i = coords[0][0]; i <= coords[1][0]; i++) {
            field[i][coords[0][1]] = 'O';
            namedField[i][coords[0][1]] = ship.getLetter();
            ship.setCoords(idx, new int[]{i, coords[0][1]});
            idx++;
        }

    }

    private void putUp(int[][] coords, Ship ship) {
        int idx = 0;
        for (int i = coords[0][0]; i >= coords[1][0]; i--) {
            field[i][coords[0][1]] = 'O';
            namedField[i][coords[0][1]] = ship.getLetter();
            ship.setCoords(idx, new int[]{i, coords[0][1]});
            idx++;
        }

    }

    private int[][] translateCoords(String start, String end) {
        int[] startCoords = {translateChars(start.charAt(0)), new Scanner(start).useDelimiter("[^0-9]+").nextInt() - 1};
        int[] endCoords = {translateChars(end.charAt(0)), new Scanner(end).useDelimiter("[^0-9]+").nextInt() - 1};

        return new int[][]{startCoords, endCoords};

    }

    private int[] translateCoord(String coord) {
        return new int[]{translateChars(coord.charAt(0)), new Scanner(coord).useDelimiter("[^0-9]+").nextInt() - 1};
    }

    public boolean isValidCoords(String start, String end, Ship ship) {

        int[][] coords = translateCoords(start, end);

        // CHECK IF OUT OF BOUNDS
        if (isOutOfBounds(coords)) {
            System.out.printf("Coordinates out of bounds for the %s, please try again!%n", ship.getName());
            return false;
        }

        // CHECK IF COORDS ARE VALID
        if (Math.abs(coords[1][1] - coords[0][1]) != 0 && Math.abs(coords[1][0] - coords[0][0]) != 0) {
            System.out.println("Error! Wrong ship location! Try again:");
            return false;
        }
        // CHECK IF SHIP IS CLOSE TO OTHER SHIPS
        if (isCloseToOtherShips(coords)) {
            System.out.println("Error! You placed it too close to another one. Try again:");
            return false; // Coords not valid because ship is close to other ships
        }

        // CHECK IF VALID ROWS OR COLUMNS
        if (Math.abs(coords[1][1] - coords[0][1]) == ship.getCells() - 1 || Math.abs(coords[1][0] - coords[0][0]) == ship.getCells() - 1) {
            return true;
        }
        System.out.printf("Error! Wrong length of the %s! Try again:%n", ship.getName());
        return false;
    }

    private int translateChars(char c) {
        return c - 'A';
    }

    private boolean isCloseToOtherShips(int[][] coords) {
        for (int[] coord : coords) {
            for (int i = coord[0] - 1; i <= coord[0] + 1; i++) {
                for (int j = coord[1] - 1; j <= coord[1] + 1; j++) {
                    if (i >= 0 && i < field.length && j >= 0 && j < field.length) {
                        if (field[i][j] == 'O') {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean isOutOfBounds(int[][] coords) {
        for (int[] coord : coords) {
            for (int i : coord) {
                if (i > field.length) return true;
            }
        }
        return false;
    }

    public boolean shoot(String coords) { // true = hit, false = miss

        int[] coord = translateCoord(coords);
        if (coord[0] >= field.length || coord[1] >= field.length) {
            System.out.println("Error! You entered the wrong coordinates! Try again:");
            return false;
        } else {
            if (field[coord[0]][coord[1]] == 'O' || field[coord[0]][coord[1]] == 'X') {

                field[coord[0]][coord[1]] = 'X';
                fogOfWarField[coord[0]][coord[1]] = 'X';

            } else {
                field[coord[0]][coord[1]] = 'M';
                fogOfWarField[coord[0]][coord[1]] = 'M';

                return false;
            }
        }
        return true;
    }

    public Ship[] makeShips() {
        return new Ship[]{new Ship("Aircraft Carrier", 5), new Ship("Battleship", 4),
                new Ship("Submarine", 3), new Ship("Cruiser", 3),
                new Ship("Destroyer", 2)};
    }

    public int countDestroyedShips(Ship[] ships) {
        int destroyedShips = 0;

        for (Ship ship : ships) {

            int destroyedCells = 0;
            int[][] coords = ship.getCoords();

            for (int[] coord : coords) {
                if (field[coord[0]][coord[1]] == 'X') {
                    destroyedCells++;
                }
            }
            if (destroyedCells == ship.getCells()) destroyedShips++;
        }
        return destroyedShips;
    }
}