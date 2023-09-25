package battleship;
import java.util.Scanner;

public class Main {
    static String[] players = {"Player 1", "Player 2"};

    static Field[] field = {new Field(10, 10), new Field(10, 10)};

    static int i = 0; // CURRENT PLAYER (0 = p1, 1 = p2)
    public static final Scanner sc = new Scanner(System.in);
    static Ship[][] ships = new Ship[2][4];

    public static void main(String[] args) {

        int count = 0;
        while (count < players.length) {
            System.out.println(i);

            System.out.printf("%s, place your ships on the game field%n%n", players[i]);
            ships[i] = field[i].makeShips();
            field[i].printField(false);

            for (Ship ship : ships[i]) {

                System.out.printf("Enter the coordinates of the %s (%d cells):%n", ship.getName(), ship.getCells());

                String start = sc.next();
                String end = sc.next();
                while (!field[i].isValidCoords(start, end, ship)) {
                    start = sc.next();
                    end = sc.next();
                }
                field[i].markCoords(start, end, ship);
                System.out.println();
                field[i].printField(false);
            }

            promptEnterKey();
            count++;

        }

        int[] destroyedShips = {0, 0};
        boolean[] destroyedThisTurn = {false, false};

        while (true) {

            printFields(1 - i, i);
            System.out.printf("%s, it's your turn%n", players[i]);
            String coordsShoot = sc.next();

            if (field[1 - i].shoot(coordsShoot)) { // shot hit
                field[1 - i].printField(true);

                int currentlyDestroyedShips = field[1 - i].countDestroyedShips(ships[1 - i]);

                destroyedThisTurn[i] = currentlyDestroyedShips > destroyedShips[i];

                if (destroyedThisTurn[i]) {
                    destroyedShips[i]++;
                    System.out.println("You sank a ship! Specify a new target!");
                } else {
                    System.out.println("You hit a ship!");
                }

            } else { // Shot missed

                System.out.println("You missed!");
//                field[1-i].printField(true);
            }
            // IF ALL SHIPS ARE DESTROYED, STOP EXECUTING!
            if (destroyedShips[i] == ships[1 - i].length) {
                System.out.println("ALL SHIPS DESTROYED!");
                break;
            }

        promptEnterKey();


        }
        System.out.println("You sank the last ship. You won. Congratulations!");
    }

    public static void switchPlayer() {
        if (i == 0) i = 1;
        else i = 0;
    }

    // i = 0 -> 0, 1
    // i = 1 -> 1, 0
    public static void printFields(int p1, int p2) {
        field[p1].printField(true);
        System.out.println("---------------------");
        field[p2].printField(false);

    }
    public static void promptEnterKey(){
        System.out.println("Press Enter and pass the move to another player");
        String pressedKey = sc.nextLine();

        sc.nextLine();
        if (pressedKey.equals("")) switchPlayer();
    }
}
