package TDGame;

import TDEntities.Enemy;
import TDEntities.Tower;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class GameCycle {

	public static void gameCycle() {
		Scanner scan = new Scanner(System.in);
		short[][] map = getMap(scan);
		ArrayList<Tower> towers = new ArrayList<Tower>();
		ArrayList<Enemy> enemies = new ArrayList<Enemy>();
		while (true) {
			action(scan, towers, map);

		}


	}

	static short[][] getMap(Scanner scan) {
		int n = 0, m = 0;
		String[] tmp = new String[2];
		System.out.println("Please input n and m. The size of the map. The numbers have to be integers.");
		while (true) {
			try {
				tmp[0] = scan.next();
				tmp[1] = scan.next();
				n = Integer.parseInt(tmp[0]);
				m = Integer.parseInt(tmp[1]);
				//TODO: We need to check the numbers anyway, even if didn't get the exception.
				if (n <= 0 || m <= 0) {
					System.out.println("One of the numbers is zero or less than zero. Make it greater than zero.");
					continue;
				} else if ((long)n * m > 2147483647) {
					System.out.println("The numbers are too big for a map. Make them smaller.");
					continue;
				}
				break;
			} catch (InputMismatchException | NumberFormatException | OutOfMemoryError e) {
				System.out.println("Error. Wrong format of input data. Try again.");
			}
		}
		return new short[n][m];
	}

	static void action(Scanner scan, ArrayList<Tower> lstTowers, short[][] map) {
		System.out.println("Please input the action, that you want to perform. Type \"usage\" " +
				"if you don't know the actions.");
		String strAction;
		loop : while (true) {
			strAction = scan.next();
			if (strAction != null) {
				switch (strAction) {
					case ("usage") :
						System.out.printf("Actions:\nadd - to add new Tower\n" +
								"delete - to delete an old Tower\n" +
								"skip - to skip the turn\n" +
								"stop - to stop the game\n");
						break;
					case ("stop") :
						System.exit(0);
						break;
					case ("add") :
						//new method to add a Tower
						break;
					case ("delete") :
						//new method to delete a Tower
						break;
					case ("skip") :
						break loop;
					default:
						System.out.println("Unknown action. Try again.");
				}
			}
		}
	}

}
