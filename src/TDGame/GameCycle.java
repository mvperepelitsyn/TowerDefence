package TDGame;

import TDEntities.Enemy;
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
		LinkedHashMap<Long, Tower> towers = new LinkedHashMap<Long, Tower>();
		LinkedHashMap<Long, Enemy> enemies = new LinkedHashMap<Long, Enemy>();
		while (true) {
			action(scan, towers, enemies, map);
			Update.updateMap(towers, enemies, map);
			addEnemy(towers, enemies, map);
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
			System.out.println("");
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

	static void action(Scanner scan, HashMap<Long, Tower> lstTowers,
					   HashMap<Long, Enemy> lstEnemies, char[][] map) {
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
						addNewTower(scan, lstTowers, lstEnemies, map);
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

	static void addNewTower(Scanner scan, HashMap<Long, Tower> lstTowers,
							HashMap<Long, Enemy> lstEnemies, char[][] map) {
		String[] tmp = new String[2];
		int x = 0, y = 0, n = map.length, m = map[0].length;
		System.out.println("Please input 0<=x<" + m + " and 0<=y<" + n +
				". The coordinates of tower. The numbers have to be integers.");
		while (true) {
			try {
				tmp[0] = scan.next();
				tmp[1] = scan.next();
				x = Integer.parseInt(tmp[0]);
				y = Integer.parseInt(tmp[1]);
				if (x < 0 || y < 0 || y >= n || x >= m) {
					System.out.println("The coordinates are out of reach. Try again:");
					continue;
				}
				else {
					long a = coordHash(x,y);
					if (!lstTowers.containsKey(a) || !lstEnemies.containsKey(a)) {
						lstTowers.put(a, new Tower(x,y));
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

	static void deleteTower(Scanner scan, HashMap<Long, Tower> towers, char[][] map) {
		int x = 0, y = 0, n = map.length, m = map[0].length;;
		String[] tmp = new String[2];
		if (!towers.isEmpty()) {
			System.out.println("Please input 0<=x<" + m + " and 0<=y<" + n +
					". The coordinates of tower. The numbers have to be integers.");
			while (true) {
				try {
					tmp[0] = scan.next();
					tmp[1] = scan.next();
					x = Integer.parseInt(tmp[0]);
					y = Integer.parseInt(tmp[1]);
					if (x >= n || y >= m || x < 0 || y < 0) {
						System.out.println("The coordinates are out of reach. Try again:");
						continue;
					} else {
						long a = coordHash(x, y);
						if (towers.containsKey(a)) {
							towers.remove(a);
							map[x][y] = '*';
						} else {
							System.out.println("No such tower!");
							continue;
						}
					}
					break;
				} catch (InputMismatchException | NumberFormatException | OutOfMemoryError e) {
					System.out.println("Error. Wrong format of input data. Try again:");
				}
			}
		}
		else {
			System.out.println("There are no towers!");
		}
	}

	static long coordHash(int x, int y) {
		return x | ((long)y << 32);
	}


	static void addEnemy(HashMap<Long, Tower> towers, HashMap<Long, Enemy> enemies, char[][] map) {
		int rows = map[0].length, count = rows / 5, rndRow;
		long a;
		Random rnd = new Random();
		for (int i = 0; i < count; i++) {
			rndRow = rnd.nextInt(rows);
			a = coordHash(map.length - 1, rndRow);
			if (!(towers.containsKey(a) || enemies.containsKey(a))) {
				enemies.put(a, new Enemy(rnd.nextInt(100) + 1, rnd.nextInt(100) + 1,
						rnd.nextInt((map[0].length - 1) / 3) + 1, rndRow, map[0].length - 1,
						rnd.nextInt((map[0].length - 1) / 3) + 1));
				map[rndRow][map[0].length - 1] = Enemy.graphic;
			}
		}
	}
}