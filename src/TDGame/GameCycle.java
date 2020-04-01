package TDGame;

import TDEntities.Enemy;
import TDEntities.Tower;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class GameCycle {

	public static void gameCycle() {
		Scanner scan = new Scanner(System.in);
		char[][] map = getMap(scan);
		ArrayList<Tower> towers = new ArrayList<Tower>();
		ArrayList<Enemy> enemies = new ArrayList<Enemy>();
		while (true) {
			action(scan, towers, map);

		}


	}

	static char[][] getMap(Scanner scan) {
		int n = 0, m = 0;
		String[] tmp = new String[2];
		System.out.println("Please input n and m. The size of the map. The numbers have to be integers.");
		while (true) {
			try {
				tmp[0] = scan.next();
				tmp[1] = scan.next();
				n = Integer.parseInt(tmp[0]);
				m = Integer.parseInt(tmp[1]);
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
		return new char[n][m];
	}

	static void action(Scanner scan, ArrayList<Tower> lstTowers, char[][] map) {
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
					case ("add") :
						addNewTower(scan, lstTowers, map);
						break loop;
					case ("delete") :
						deleteTower(scan, lstTowers, map);
						break loop;
					case ("skip") :
						break loop;
					default:
						System.out.println("Unknown action. Try again.");
				}
			}
		}
	}

	static void addNewTower(Scanner scan, ArrayList<Tower> towers, char[][] map) {
		int x = 0, y = 0;
		String[] tmp = new String[2];
		System.out.println("Please input the coordinates of new Tower:");
		while (true) {
			try {
				tmp[0] = scan.next();
				tmp[1] = scan.next();
				x = Integer.parseInt(tmp[0]);
				y = Integer.parseInt(tmp[1]);
				if (x >= map.length || y >= map[0].length || x < 0 || y < 0) {
					System.out.println("The coordinates are out of reach. Try again:");
					continue;
				}
				break;
			} catch (InputMismatchException | NumberFormatException | OutOfMemoryError e) {
				System.out.println("Error. Wrong format of input data. Try again:");
			}
		}
		//Here we must check if Tower with such coordinates is already existing
		towers.add(new Tower(x, y));
		map[x][y] = 'T';
	}

	static void deleteTower(Scanner scan, ArrayList<Tower> towers, char[][] map) {
		int x = 0, y = 0;
		String[] tmp = new String[2];
		Tower tmpTower;
		System.out.println("Please input the coordinates of Tower you want to delete:");
		while (true) {
			try {
				tmp[0] = scan.next();
				tmp[1] = scan.next();
				x = Integer.parseInt(tmp[0]);
				y = Integer.parseInt(tmp[1]);
				if (x >= map.length || y >= map[0].length || x < 0 || y < 0) {
					System.out.println("The coordinates are out of reach. Try again:");
					continue;
				}
				tmpTower = new Tower(x, y);
//				if (!towers.contains(tmpTower)) { //we need Artem's function over here. In order to do it
//					System.out.println("There is no Tower with such coordinates. You want to try again?" +
//							" Y or N?");
//					tmp[0] = scan.nextLine();
//					if (tmp[0] != null && tmp[0].equals("Y")) {
//						continue;
//					}
//				}
				break;
			} catch (InputMismatchException | NumberFormatException | OutOfMemoryError e) {
				System.out.println("Error. Wrong format of input data. Try again:");
			}
		}

	}
}
