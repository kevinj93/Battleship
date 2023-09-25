package battleship;

public class Ship {
    private final String name;
    private final int cells;

    private char letter;
    private final int[][] coords;

    // Store ship name, no. of cells and the coordinations on the game field.
    public Ship(String name, int cells) {
        this.name = name;
        this.cells = cells;
        coords = new int[cells][2];

        switch (name) {
            case "Aircraft Carrier":
                letter = 'A';
                break;
            case "Battleship":
                letter = 'B';
                break;
            case "Submarine":
                letter = 'S';
                break;
            case "Cruiser":
                letter = 'C';
                break;
            case "Destroyer":
                letter = 'D';
                break;
        }

    }

    // Store ship coords
    public void setCoords(int i, int[] values) {

        coords[i] = values;
    }

    public String getName() {
        return name;
    }

    public int getCells() {
        return cells;
    }

    public char getLetter() {
        return letter;
    }


    // Get ship coordinates.
    public int[][] getCoords() {
        return coords;
    }
}
