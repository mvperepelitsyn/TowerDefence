package TDGame;

import TDEntities.Enemy;
import TDEntities.GameEntity;
import TDEntities.Tower;

import java.util.*;

public class GameCycle {

	public static void gameCycle() {
		Scanner scan = new Scanner(System.in);
		char[][] map = getMap(scan);
		//fill map with spaces
		for (int i = 0; i < map.length; i++) {
			Arrays.fill(map[i],'*');
		}
		HashMap<Long, Tower> towers = new HashMap<>();
		HashMap<Long, Enemy> enemies = new HashMap<>();
		while (true) {
			action(scan, towers, enemies, map);
			showMap(map);
		}
	}

	private static void showMap(char[][] map) {
		int n = map.length;
		int m = map[0].length;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				System.out.print(map[i][j]);
			}
			System.out.println();
		}
	}

	static char[][] getMap(Scanner scan) {
		int n, m;
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

	static void action(Scanner scan, HashMap<Long, Tower> lstTowers, HashMap<Long, Enemy> lstEnemies, char[][] map) {
		System.out.println("Please input the action, that you want to perform. Type \"usage\" " +
				"if you don't know the actions.");
		String strAction;
		boolean flag = true;
		while (flag) {
			strAction = scan.next();
			if (strAction != null) {
				switch (strAction) {
					case ("usage") :
						System.out.printf("Actions:\nadd - to add new Tower\n" +
								"delete - to delete an old Tower\n" +
								"skip - to skip the turn\n" +
								"stop - to stop the game\n");
						flag = false;
						break;
					case ("stop") :
						System.exit(0);
						break;
					case ("add") :
						towerAdd(scan, map, lstTowers, lstEnemies);
						flag = false;
						break;
					case ("delete") :
						//new method to delete a Tower
						flag = false;
						break;
					case ("skip"):
						flag = false;
						break;
					default:
						System.out.println("Unknown action. Try again.");
				}
			}
		}
	}

	private static void towerAdd(Scanner scan, char[][] map, HashMap<Long, Tower> lstTowers, HashMap<Long, Enemy> lstEnemies) {
		String[] tmp = new String[2];
		int x, y, n = map.length, m = map[0].length;
		System.out.println("Please input 0<=x<" + m + " and 0<=y<" + n +
				". The coordinates of tower. The numbers have to be integers.");
		while (true) {
			try {
				tmp[0] = scan.next();
				tmp[1] = scan.next();
				x = Integer.parseInt(tmp[0]);
				y = Integer.parseInt(tmp[1]);
				if (x < 0 || y < 0 || y >= n || x > m) {
					System.out.println("Range error");
					continue;
				}
				else {
					long a = coordHash(x,y);
					if (!lstTowers.containsKey(a) || !lstEnemies.containsKey(a)) {
						lstTowers.put(a,new Tower(x,y));
						map[x][y] = Tower.graphic;
					}
					else {
						System.out.println("Tower already exists or there is the enemy!");
						continue;
					}
				}
				break;
			} catch (InputMismatchException | NumberFormatException | OutOfMemoryError e) {
				System.out.println("Error. Wrong format of input data. Try again.");
			}
		}
	}

	static long coordHash(int x, int y) {
		return x | ((long)y << 32);
	}

}
