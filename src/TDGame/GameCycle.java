package TDGame;

import TDEntities.Tower;

import java.util.ArrayList;
import java.util.Scanner;

public class GameCycle {

	public static void gameCycle() {
		Scanner scan = new Scanner(System.in);
		short[][] map = getMap(scan);


	}

	static short[][] getMap(Scanner scan) {
		int n = 0, m = 0;
		System.out.println("Please input n and m. The size of the map.");
		while (true) {
			try {
				n = scan.nextInt();
				m = scan.nextInt();
				//TODO: We need to check the numbers anyway, even if didn't get the exception.
				break;
			} catch (Exception e) {
				System.out.println("Wrong format of input data. Try again.");
			}
		}
		return new short[n][m];
	}

	static void action(Scanner scan, ArrayList<Tower> lstTowers) {
		System.out.println("Please input the action, that you want to perform. Type \"usage\" " +
				"if you don't know the actions.");
		String strAction;
		while (true) {
			strAction = scan.next();
			if (strAction != null) {
				if (strAction.equals("usage")) {
					System.out.printf("Actions:\nadd - to add new Tower\n" +
							"delete - to delete an old Tower\n" +
							"skip - to skip the turn\n" +
							"stop - to stop the game\n");
				} else if (strAction.equals("stop")) {
					System.exit(0);
				} else if (strAction.equals("add")) {
					//new method to add the Tower
				} else if (strAction.equals("delete")) {
					//new method to delete the Tower
				} else if (strAction.equals("skip")) {
					break ;
				} else {
					System.out.println("Unknown action. Try again.");
				}
			}
		}
	}

}
