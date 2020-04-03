package TDGame;

import TDEntities.*;

import java.util.*;

public class GameMap {
	//we use linked to get correct iteration through enemies and towers
	private HashMap<Long, Tower> towers = new LinkedHashMap<Long, Tower>();
	private HashMap<Long, Enemy> enemies = new LinkedHashMap<Long, Enemy>();
	private char[][] map;
	private int n = 0;
	private int m = 0;

	public GameMap(char[][] map) {
		this.n = map.length;
		this.m = map[0].length;
		//fill map with spaces
		for (int i = 0; i < n; i++) {
			Arrays.fill(map[i],'*');
		}
		this.map = map;
	}

	public int getTowersCount () {
		return towers.size();
	}

	public int getEnemyCount () {
		return enemies.size();
	}

	public void setSymbol(int x, int y, char c) {
		this.map[y][x] = c;
	}

	public void showMap() {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				System.out.print(this.map[i][j]);
			}
			System.out.println("");
		}
	}

	public void showLists() {
		System.out.println("Tower list: ");
		for (Map.Entry<Long, Tower> entry : this.towers.entrySet()) {
			Tower tmp = entry.getValue();
			System.out.println("x: " + tmp.getxCoord() + " y: " + tmp.getyCoord() + " h: " +
					tmp.getHealthPoints() + " d: " + tmp.getDmgPoints() + " s: " + tmp.getSpeed() +
			" r: " + tmp.getRangeOfAttack() + " type: " + tmp.getClass().getSimpleName() + " symbol: " + tmp.getGraphic());
		}
		System.out.println("Enemy list: ");
		for (Map.Entry<Long, Enemy> entry : this.enemies.entrySet()) {
			Enemy tmp = entry.getValue();
			System.out.println("x: " + tmp.getxCoord() + " y: " + tmp.getyCoord() + " h: " +
					tmp.getHealthPoints() + " d: " + tmp.getDmgPoints() + " s: " + tmp.getSpeed() +
					" r: " + tmp.getRangeOfAttack() + " type: " + tmp.getClass().getSimpleName() + " symbol: " + tmp.getGraphic());
		}
	}

	public int doTurn() {

		int lostLive = 0;
		HashMap<Long, Enemy> new_enemies = new LinkedHashMap<>();

		/* turn */
		Iterator<Map.Entry<Long,Enemy>> it_e = enemies.entrySet().iterator();
		// iteration on each enemy
		while (it_e.hasNext()) {
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
							setSymbol(x_t,y_t,'*');
						}
					}
				}
			}

			//enemy killed by one of the tower
			if (enemy_dead) {
				//iterForEnemies--;
				it_e.remove();
				//map[y_en][x_en] = '*';
				setSymbol(x_en,y_en,'*');
				continue;
			}
			//we reach this point if enemy is alive
			//turn the enemy to the next position
			int distance = m + 1; //more than can be
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
				setSymbol(x_en,y_en,'*');
				x_en = x_en - speed_enemy;
				tmp_enemy.setxCoord(x_en);
			}
			else if (distance > 1) {
				setSymbol(x_en,y_en,'*');
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
					//map[y_en][x_en] =tmp_enemy.getGraphic();
					setSymbol(x_en,y_en,tmp_enemy.getGraphic());
					new_enemies.put(coordHash(x_en, y_en), tmp_enemy);
				} else {
					lostLive++;
					System.out.println("You've lost one life. You were attacked by enemy on y = " + y_en + ". The enemy has been annihilated.");
				}
			}
		}
		//rewrite our hashmap. now it has new enemies position
		enemies.putAll(new_enemies);
		return lostLive;
	}

	public int addEnemy(int leftEnemies) {
		int rndRow, rndEnemy, count = (n / 3 > 0) ? n / 3 : 1;
		long a;
		if (leftEnemies > 0) {
			Enemy tmp;
			Random rnd = new Random();
			for (int i = 0; i < count; i++) {
				rndRow = rnd.nextInt(n);
				a = coordHash(m - 1, rndRow);
				int b = ((m - 1) / 3) + 1;
				if ((!(towers.containsKey(a) || enemies.containsKey(a))) && leftEnemies > 0) {
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
					leftEnemies--;
				}
			}
		}
		return leftEnemies;
	}

	public int addNewTower(Scanner scan, int towerPoints) {
		String		tmpStr;
		String[]	tmp;
		int x, y;
		System.out.println("Please input the type of Tower \"strong\" or \"regular\" and the coordinates 0<=x<" + (m-1) + " and 0<=y<" + n +
				". The numbers have to be integers.\nIf you changed your mind and don't want to add a Tower or you don't have enough points then input \"again\".");
		System.out.println("Strong Tower characteristics: cost = 6, health = 150, damage = 70, attack range = 5");
		System.out.println("Regular Tower characteristics: cost = 3, health = 100, damage = 25, attack range = 3");
		while (true) {
			try {
				tmpStr = scan.nextLine().trim().toLowerCase();
				tmp = tmpStr.split(" ");
				if (tmp[0].equals("again")) {
					if (tmp.length == 1) {
						return -1;
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
					if (!towers.containsKey(a) && !towers.containsKey(a)) {
						if (tmp[0].equals("regular") && towerPoints >= RegularTower.cost) {
							RegularTower reg = new RegularTower(x,y);
							towers.put(a, reg);
							map[y][x] = reg.getGraphic();
							towerPoints -= RegularTower.cost;
						} else if (tmp[0].equals("strong") && towerPoints >= StrongTower.cost){
							StrongTower strong = new StrongTower(x,y);
							towers.put(a, strong);
							map[y][x] = strong.getGraphic();
							towerPoints -= StrongTower.cost;
						}
						else {
							System.out.println("You don't have enough points!");
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
		return towerPoints;
	}

	public int deleteTower(Scanner scan) {
		int x,y;
		int pointsAdd = -1;
		String[] tmp;
		String tmpStr;
		if (!towers.isEmpty()) {
			System.out.println("Please input 0<=x<" + (m - 1) + " and 0<=y<" + n +
					". The coordinates of tower. The numbers have to be integers.\n" +
					"If you changed your mind and don't want to delete a Tower anymore then input \"again\".");
			while (true) {
				try {
					tmpStr = scan.nextLine().trim();
					tmp = tmpStr.split(" ");
					if (tmp[0].equals("again")) {
						if (tmp.length == 1) {
							return 0;
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
					if (x >= m - 1 || y >= n || x < 0 || y < 0) {
						System.out.println("The coordinates are out of reach. Try again:");
						continue;
					} else {
						long a = coordHash(x, y);
						if (towers.containsKey(a)) {
							if (towers.get(a) instanceof StrongTower) {
								pointsAdd = StrongTower.cost / 2;
							} else if (towers.get(a) instanceof RegularTower) {
								pointsAdd = RegularTower.cost / 2;
							}
							towers.remove(a);
							//map[y][x] = '*';
							setSymbol(x,y,'*');
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
		} else {
			System.out.println("There are no towers!");
		}
		return pointsAdd;
	}

	private long coordHash(int x, int y) {
		return x | ((long)y << 32);
	}
}
