package TDGame;

import TDEntities.*;

import java.util.*;

public class GameCycle {

	static int iterForEnemies;
	static int towerPoints;
	static int numberOfTowersSimultaniously;
	static int lives = 3;

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
		while (iterForEnemies + enemies.size() > 0 && lives > 0) {
			System.out.println("Lives left: " + lives);
			System.out.println("Gold for tower: "+ towerPoints);
			System.out.println("Towers can be added on field: " + (numberOfTowersSimultaniously - towers.size()));
			System.out.println("Enemy left: " + ((iterForEnemies + enemies.size() > 0) ?
					(iterForEnemies + enemies.size()) : "No enemies left"));
			//player input
			action(scan, towers, enemies, map);
			//towers and enemy fight
			doTurn(towers,enemies,map);
			//generate new enemy
			addEnemy(towers, enemies, map);
			//show map after turn
			showMap(map);
			//show lists of the tower and enemy which placed on the map
			showLists(towers, enemies);
			System.out.println();
			towerPoints++;
		}
		if (lives > 0) {
			System.out.println("Congratulations! You won!");
		} else {
			System.out.println("You lost! Better luck next time!");
		}
	}

	private static void showLists(HashMap<Long, Tower> towers, HashMap<Long, Enemy> enemies) {
		System.out.println("Tower list: ");
		if (towers.size() == 0) {
			System.out.println("No towers");
		}
		for (Map.Entry<Long, Tower> entry : towers.entrySet()) {
			Tower tmp = entry.getValue();
			System.out.println("x: " + tmp.getxCoord() + " y: " + tmp.getyCoord() + " h: " +
					tmp.getHealthPoints() + " d: " + tmp.getDmgPoints() + " s: " + tmp.getSpeed() +
					" r: " + tmp.getRangeOfAttack() + " type: " + tmp.getGraphic());
		}
		System.out.println("Enemy list: ");
		if (enemies.size() == 0) {
			System.out.println("No enemies");
		}
		for (Map.Entry<Long, Enemy> entry : enemies.entrySet()) {
			Enemy tmp = entry.getValue();
			System.out.println("x: " + tmp.getxCoord() + " y: " + tmp.getyCoord() + " h: " +
					tmp.getHealthPoints() + " d: " + tmp.getDmgPoints() + " s: " + tmp.getSpeed() +
					" r: " + tmp.getRangeOfAttack() + " type: " + tmp.getGraphic());
		}
	}

	private static void doTurn(HashMap<Long, Tower> towers,
							   HashMap<Long, Enemy> enemies, char[][] map) {

		HashMap<Long, Enemy> new_enemies = new LinkedHashMap<>();

		/* turn */
		Iterator<Map.Entry<Long,Enemy>> it_e = enemies.entrySet().iterator();
		// iteration on each enemy
		enemy: while (it_e.hasNext()) {
			//get enemy
			Map.Entry<Long,Enemy> pair_enemy = it_e.next();
			Enemy tmp_enemy = pair_enemy.getValue();

			//iteration on each tower
			Iterator<Map.Entry<Long,Tower>> it_t = towers.entrySet().iterator();
			//coords of the enemy and speed
			int x_en = tmp_enemy.getxCoord();
			int y_en = tmp_enemy.getyCoord();
			int speed_enemy = tmp_enemy.getSpeed();

			//each enemy attack all towers that he can
			boolean enemy_dead = false;
			//iteration through each tower
			while (it_t.hasNext()) {
				//get tower
				Map.Entry<Long, Tower> pair_tower = it_t.next();
				Tower tmp_tower = pair_tower.getValue();
				//coords of the tower (it is necessary for deleting)
				int x_t = tmp_tower.getxCoord();
				int y_t = tmp_tower.getyCoord();

				//tower atack if enemy is close. get atack ranges enemy and tower
				int distance = tmp_tower.distance(tmp_enemy);
				int range_tower = tmp_tower.getRangeOfAttack();
				int range_enemy = tmp_enemy.getRangeOfAttack();

				//if distance == -1 => enemy and tower not on one line
				if (distance != -1) {
					int tower_health = tmp_tower.getHealthPoints();
					int enemy_health = tmp_enemy.getHealthPoints();

					//tower attacks
					if (distance <= range_tower) {
						enemy_health -= tmp_tower.getDmgPoints();
						tmp_enemy.setHealthPoints(enemy_health);
						enemy_dead = (enemy_health <= 0);
					}
					//enemy attacks
					if (distance <= range_enemy) {
						tower_health -= tmp_enemy.getDmgPoints();
						tmp_tower.setHealthPoints(tower_health);

						//tower killed
						if (tower_health <= 0) {
							//System.out.println("tower destroy");
							it_t.remove();
							map[y_t][x_t] = '*';
						}
					}
				}
			}

			//enemy killed by one of the tower
			if (enemy_dead) {
				//iterForEnemies--;
				it_e.remove();
				map[y_en][x_en] = '*';
				continue;
			}
			//we reach this point if enemy is alive
			//turn the enemy to the next position
			int distance = map[0].length + 1; //more than can be
			//we haven't towers or enemies on position x < 0
			int low = Math.max(x_en - speed_enemy, 0);

			//check if the enemy can move with it speed (no barriers)
			for (int i = x_en - 1; i >= low; i--) {
				if (map[y_en][i] == 'T' || map[y_en][i] == 'E' || map[y_en][i] == 't' || map[y_en][i] == 'z'
						|| map[y_en][i] == 'Z' || map[y_en][i] == 's' || map[y_en][i] == 'S') {
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
				//if x > 0 than we don't lose. lose otherwise (because we can't create new tower)
				if (x_en > 0) {
					//map[y_en][x_en] = 'E';
					map[y_en][x_en] = tmp_enemy.getGraphic();
					new_enemies.put(coordHash(x_en, y_en), tmp_enemy);
				} else {
					lives--;
					System.out.println("You've lost one life. You were attacked by enemy on y = " + y_en + ". The enemy has been annihilated.");
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
		String		tmpStr;
		String[]	tmp;
		boolean		flag = true;
		System.out.println("Please input n and m. The size of the map. The numbers have to be integers.");
		while (flag) {
			try {
				tmpStr = scan.nextLine().trim();
				tmp = tmpStr.split(" ");
				n = Integer.parseInt(tmp[0]);
				m = Integer.parseInt(tmp[1]);
				iterForEnemies = ((n * m) / 2 > 0) ? (n * m) / 2 : 3;
				//towerPoints = (iterForEnemies / 2 > 0) ? iterForEnemies / 2 : 2;
				towerPoints = (int) (Math.sqrt(iterForEnemies) * 2.5);
				//numberOfTowersSimultaniously = (n / 1.5 > 0) ? (int)(n / 1.5) : 1;
				numberOfTowersSimultaniously = (int) (n * 1.2);

				if (tmp.length > 2) {
					System.out.println("Wrong format of input data. Try again.");
				} else if (n <= 0 || m <= 1) {
					System.out.println("N is less than 1 or M less than 2. Make it greater.");
				} else if ((long)n * m > 2147483647) {
					System.out.println("The numbers are too big for a map (n * m > 2147483647). Make them smaller.");
				} else {
					flag = false;
				}
			} catch (InputMismatchException | NumberFormatException | OutOfMemoryError | ArrayIndexOutOfBoundsException e) {
				System.out.println("Error. Wrong format of input data. Try again.");
			}
		}
		return new char[n][m];
	}

	static void action(Scanner scan, HashMap<Long, Tower> lstTowers,
					   HashMap<Long, Enemy> lstEnemies, char[][] map) {
		System.out.println("Please input the action, that you want to perform. Type \"usage\" " +
				"if you don't know the actions.");
		String	strAction;
		boolean	flagAction = true;
		while (flagAction) {
			strAction = scan.nextLine().trim();
			switch (strAction) {
				case ("usage") :
					System.out.printf("Actions:\nadd - to add new Tower\n" +
							"delete - to delete an old Tower\n" +
							"skip - to skip the turn\n" +
							"stop - to stop the game\n");
					break;
				case ("stop") :
					System.out.println("You've stopped the game. See you later!");
					System.exit(0);
				case ("add") :
					if (towerPoints > 0 && lstTowers.size() <= numberOfTowersSimultaniously) {
						if (!addNewTower(scan, lstTowers, lstEnemies, map)) {
							System.out.println("Please input the action, that you want to perform. Type \"usage\" " +
									"if you don't know the actions.");
							break;
						}
					} else {
						System.out.println("Oops! You can't add more Towers.\nThere are too many Towers on the map or you're run out of Towers.");
					}
					flagAction = false;
					break;
				case ("delete") :
					if (!deleteTower(scan, lstTowers, map)) {
						System.out.println("Please input the action, that you want to perform. Type \"usage\" " +
								"if you don't know the actions.");
						break;
					}
					flagAction  = false;
					break;
				case ("skip") :
					flagAction = false;
					break;
				default:
					System.out.println("Unknown action. Try again.");
			}
		}
	}

	static boolean addNewTower(Scanner scan, HashMap<Long, Tower> lstTowers,
							HashMap<Long, Enemy> lstEnemies, char[][] map) {
		String		tmpStr;
		String[]	tmp;
		int x, y, n = map.length, m = map[0].length;
		System.out.println("Please input the type of Tower \"strong\" or \"regular\" and the coordinates 0<=x<" + (m-1) + " and 0<=y<" + n +
				". The numbers have to be integers.\nIf you changed your mind and don't want to add a Tower or you don't have enough points then input \"again\".");
		System.out.println("Strong Tower characteristics: cost = 6, health = 150, damage = 70, attack range = 5");
		System.out.println("Regular Tower characteristics: cost = 3, health = 100, damage = 25, attack range = 3");
		while (true) {
			try {
				tmpStr = scan.nextLine().trim();
				tmp = tmpStr.split(" ");
				if (tmp[0].equals("again")) {
					if (tmp.length == 1) {
						return false;
					} else {
						System.out.println("Wrong type of input data. Try again.");
						continue;
					}
				} else if (tmp.length > 3) {
					System.out.println("Wrong type of input data. Try again.");
					continue;
				}
				x = Integer.parseInt(tmp[1]);
				y = Integer.parseInt(tmp[2]);
				if (!(tmp[0].equals("regular") || tmp[0].equals("strong"))) {
					System.out.println("Wrong type of Tower. Try again:");
					continue;
				}
				if (x < 0 || y < 0 || y >= n || x >= m - 1) {
					System.out.println("The coordinates are out of reach. Try again:");
					continue;
				}
				else {
					long a = coordHash(x,y);
					if (!lstTowers.containsKey(a) && !lstEnemies.containsKey(a)) {
						if (tmp[0].equals("regular") && towerPoints >= RegularTower.cost) {
							RegularTower reg = new RegularTower(x,y);
							lstTowers.put(a, reg);
							map[y][x] = reg.getGraphic();
							towerPoints -= RegularTower.cost;
						} else if (tmp[0].equals("strong") && towerPoints >= StrongTower.cost){
							StrongTower strong = new StrongTower(x,y);
							lstTowers.put(a, strong);
							map[y][x] = strong.getGraphic();
							towerPoints -= StrongTower.cost;
						}
						else {
							System.out.println("You don't have enough points! Change the type of the Tower or type" +
									"\"again\" to get back to different actions.");
							continue;
						}
					}
					else {
						System.out.println("Tower already exists or there is the enemy!");
						continue;
					}
				}
				break;
			} catch (InputMismatchException | NumberFormatException | OutOfMemoryError | ArrayIndexOutOfBoundsException e) {
				System.out.println("Error. Wrong format of input data. Try again.");
			}
		}
		return true;
	}

	static boolean deleteTower(Scanner scan, HashMap<Long, Tower> towers, char[][] map) {
		int x = 0, y = 0, n = map.length, m = map[0].length;;
		String[]	tmp;
		String		tmpStr;
		if (!towers.isEmpty()) {
			System.out.println("Please input 0<=x<" + (m-1) + " and 0<=y<" + n +
					". The coordinates of tower. The numbers have to be integers.\n" +
					"If you changed your mind and don't want to delete a Tower anymore then input \"again\".");
			while (true) {
				try {
					tmpStr = scan.nextLine().trim();
					tmp = tmpStr.split(" ");
					if (tmp[0].equals("again")) {
						if (tmp.length == 1) {
							return false;
						} else {
							System.out.println("Wrong format of input data. Try again.");
							continue;
						}
					} else if (tmp.length > 2) {
						System.out.println("Wrong format of input data. Try again.");
						continue;
					}
					x = Integer.parseInt(tmp[0]);
					y = Integer.parseInt(tmp[1]);
					if (x >= m - 1  || y >= n || x < 0 || y < 0) {
						System.out.println("The coordinates are out of reach. Try again:");
						continue;
					} else {
						long a = coordHash(x, y);
						if (towers.containsKey(a)) {
							if (towers.get(a) instanceof StrongTower) {
								towerPoints+=StrongTower.cost/2;
							}
							else if (towers.get(a) instanceof RegularTower) {
								towerPoints+=RegularTower.cost/2;
							}
							towers.remove(a);
							map[y][x] = '*';
						} else {
							System.out.println("No such tower!");
							continue;
						}
					}
					break;
				} catch (InputMismatchException | NumberFormatException | OutOfMemoryError | ArrayIndexOutOfBoundsException e) {
					System.out.println("Error. Wrong format of input data. Try again:");
				}
			}
		}
		else {
			System.out.println("There are no towers!");
		}
		return true;
	}

	static long coordHash(int x, int y) {
		return x | ((long)y << 32);
	}

	static void addEnemy(HashMap<Long, Tower> towers, HashMap<Long, Enemy> enemies, char[][] map) {
		int n = map.length, m = map[0].length;
		int rndRow, rndEnemy, count = (n / 3 > 0) ? n / 3 : 1;
		long a;
		Enemy tmp;
		Random rnd = new Random();
		for (int i = 0; i < count; i++) {
			rndRow = rnd.nextInt(n);
			a = coordHash(m - 1, rndRow);
			int b = ((m- 1) / 3) + 1;
			if ((!(towers.containsKey(a) || enemies.containsKey(a))) && iterForEnemies > 0) {
				rndEnemy = rnd.nextInt(4) + 1;
				switch (rndEnemy) {
					case (1) :
						tmp = new RegularZombie(rnd.nextInt(75) + 1, rnd.nextInt(75) + 1,
								rnd.nextInt(m / 6 + 1) + 1, m - 1, rndRow, 1);
						break;
					case (2) :
						tmp = new BigZombie(rnd.nextInt(50) + 100, rnd.nextInt(25) + 25,
								1, m - 1, rndRow, 1);
						break;
					case (3) :
						tmp = new SkeletonArcher(rnd.nextInt(25) + 25, rnd.nextInt(25) + 75,
								rnd.nextInt(m / 6 + 1) + 1, m - 1, rndRow,
								rnd.nextInt(2) + 3);
						break;
					case (4) :
						tmp = new SkeletonWarrior(rnd.nextInt(100) + 1, rnd.nextInt(25) + 75,
								rnd.nextInt(m / 6 + 1) + 1, m - 1, rndRow,
								rnd.nextInt(1) + 1);
						break;
					default:
						tmp = new Enemy(rnd.nextInt(100) + 1, rnd.nextInt(100) + 1,
								rnd.nextInt(m / 6 + 1) + 1, m - 1, rndRow,
								rnd.nextInt(m / 6+ 1) + 1);
				}
				enemies.put(a, tmp);
				map[rndRow][m - 1] = tmp.getGraphic();
				iterForEnemies--;
			}
		}
	}
}