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
		//we use linked to get correct iteration through enemies and towers
		HashMap<Long, Tower> towers = new LinkedHashMap<Long, Tower>();
		HashMap<Long, Enemy> enemies = new LinkedHashMap<Long, Enemy>();
		while (true) {
			action(scan, towers, enemies, map);

			for (Map.Entry<Long, Tower> entry : towers.entrySet()) {
				Tower tmp = entry.getValue();
				System.out.println("x: " + tmp.getxCoord() + " y: " + tmp.getyCoord() + " h: " +
						tmp.getHealthPoints() + " d: " + tmp.getDmgPoints() + " s: " + tmp.getSpeed() +
						" r: " + tmp.getRangeOfAttack());
			}

			showMap(map);
			System.out.println();
			doTurn(towers,enemies,map);
			addEnemy(towers, enemies, map);

			for (Map.Entry<Long, Enemy> entry : enemies.entrySet()) {
				Enemy tmp = entry.getValue();
				System.out.println("x: " + tmp.getxCoord() + " y: " + tmp.getyCoord() + " h: " +
						tmp.getHealthPoints() + " d: " + tmp.getDmgPoints() + " s: " + tmp.getSpeed() +
						" r: " + tmp.getRangeOfAttack());
			}

			showMap(map);
			System.out.println();
		}
	}

	private static void doTurn(HashMap<Long, Tower> towers,
							   HashMap<Long, Enemy> enemies, char[][] map) {

		HashMap<Long, Enemy> new_enemies = new LinkedHashMap<>();

		/* turn */
		Iterator it_e = enemies.entrySet().iterator();
		// iteration on each enemy
		enemy: while (it_e.hasNext()) {
			//get enemy
			Map.Entry pair_enemy = (Map.Entry)it_e.next();
			Enemy tmp_enemy = (Enemy) pair_enemy.getValue();

			//iteration on each tower
			Iterator it_t = towers.entrySet().iterator();
			//coords of the enemy and speed
			int x_en = tmp_enemy.getxCoord();
			int y_en = tmp_enemy.getyCoord();
			int speed_enemy = tmp_enemy.getSpeed();

			//iteration through each tower
			while (it_t.hasNext()) {
				//get tower
				Map.Entry pair_tower = (Map.Entry)it_t.next();
				Tower tmp_tower = (Tower) pair_tower.getValue();
				//coords of the tower (it is necessary for deleting)
				int x_t = tmp_tower.getxCoord();
				int y_t = tmp_tower.getyCoord();

				//tower atack if enemy is close. get atack ranges enemy and tower
				int distance = tmp_tower.distance(tmp_enemy);
				int range_tower = tmp_tower.getRangeOfAttack();
				int range_enemy = tmp_enemy.getRangeOfAttack();

				//if distance == -1 => enemy and tower not on one line
				if (distance != - 1) {
					//tower attack
					if (distance <= range_tower) {
						tmp_enemy.setHealthPoints(tmp_enemy.getHealthPoints() - tmp_tower.getDmgPoints());
					}
					//enemy attack
					if (distance <= range_enemy) {
						tmp_tower.setHealthPoints(tmp_tower.getHealthPoints() - tmp_enemy.getDmgPoints());
					}
					//enemy killed
					if (tmp_enemy.getHealthPoints() <= 0) {
						it_e.remove();
						map[y_en][x_en] = '*';
						continue enemy;
					}
					//tower killed
					if (tmp_tower.getHealthPoints() <= 0) {
						it_t.remove();
						map[y_t][x_t] = '*';
					}
				}
			}
			//we reach this point if enemy is alive
			//turn the enemy to the next position
			int distance = map[0].length + 1; //more than can be
			//we haven't towers or enemies on position x < 0
			int low = Math.max(x_en - speed_enemy, 0);

			//check if the enemy can move with it speed (no barriers)
			for (int i = x_en - 1; i >= low; i--) {
				if (map[y_en][i] == 'T' || map[y_en][i] == 'E') {
					distance = x_en - i;
					break;
				}
			}

			//write new coordinate
			//if enemy has barrier it moves close to it
			if (speed_enemy < distance) {
				map[y_en][x_en] = '*';
				x_en = x_en - speed_enemy;
				tmp_enemy.setxCoord(x_en);
			}
			else if (distance > 1) {
				map[y_en][x_en] = '*';
				x_en = x_en - distance + 1;
				tmp_enemy.setxCoord(x_en);
			}

			//we can't change a key, so we write it to a new collection
			//if distance == 1 so we have a barrier and don't move
			if (distance > 1) {
				it_e.remove();
				new_enemies.put(coordHash(x_en, y_en), tmp_enemy);

				//if x > 0 than we don't lose. lose otherwise (because we can't create new tower)
				if (x_en > 0) {
					map[y_en][x_en] = 'E';
				} else {
					System.out.println("You were killed by enemy on y = " + y_en);
					System.exit(0);
				}
			}
		}
		//rewrite our hashmap. now it has new enemies position
		enemies.putAll(new_enemies);
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
					if (!lstTowers.containsKey(a) && !lstEnemies.containsKey(a)) {
						lstTowers.put(a, new Tower(x,y));
						map[y][x] = Tower.graphic;
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
							map[y][x] = '*';
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
		int rows = map.length, count = rows / 5, rndRow;
		long a;
		Random rnd = new Random();
		for (int i = 0; i < count; i++) {
			rndRow = rnd.nextInt(rows);
			a = coordHash(map[0].length - 1,rndRow);
			if (!(towers.containsKey(a) || enemies.containsKey(a))) {
				enemies.put(a, new Enemy(rnd.nextInt(100) + 1, rnd.nextInt(100) + 1,
						rnd.nextInt((map[0].length - 1) / 3) + 1, map[0].length - 1, rndRow,
						rnd.nextInt((map[0].length - 1) / 3) + 1));
				map[rndRow][map[0].length - 1] = Enemy.graphic;
			}
		}
	}
}