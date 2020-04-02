package TDGame;

import TDEntities.Enemy;
import TDEntities.Tower;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Update {

	static void towersShooting(HashMap<Long, Tower> towers, HashMap<Long, Enemy> enemies,
							   char[][] map) {
		int leftZone = 0, rightZone = 0, max = 0;
		long a;
		Enemy tmpEnemy;
		Tower tmpTower;
		for (Map.Entry<Long, Tower> tower : towers.entrySet()) {
			tmpTower = tower.getValue();
			leftZone = (tmpTower.getyCoord() - tmpTower.getRangeOfAttack() >= 0) ?
					tmpTower.getRangeOfAttack() : tmpTower.getyCoord();
			rightZone = (tmpTower.getyCoord() + tmpTower.getRangeOfAttack() >= map[0].length) ?
					map[0].length - 1 - tmpTower.getyCoord() : tmpTower.getRangeOfAttack();
			for (int i = leftZone; i > 0; i--) {
				if (map[tmpTower.getxCoord()][tmpTower.getyCoord() - leftZone] == 'E') {
					a = GameCycle.coordHash(tmpTower.getxCoord(), tmpTower.getyCoord() - leftZone);
					tmpEnemy = enemies.get(a);
					tmpEnemy.setHealthPoints(tmpEnemy.getHealthPoints() - tmpTower.getDmgPoints());
					if (tmpEnemy.getHealthPoints() <= 0) {
						enemies.remove(a);
						map[tmpTower.getxCoord()][tmpTower.getyCoord() - leftZone] = '*';
					}
				}
			}
			for (int i = rightZone; i > 0; i--) {
				if (map[tmpTower.getxCoord()][tmpTower.getyCoord() + rightZone] == 'E') {
					a = GameCycle.coordHash(tmpTower.getxCoord(), tmpTower.getyCoord() + rightZone);
					tmpEnemy = enemies.get(a);
					tmpEnemy.setHealthPoints(tmpEnemy.getHealthPoints() - tmpTower.getDmgPoints());
					if (tmpEnemy.getHealthPoints() <= 0) {
						enemies.remove(a);
						map[tmpTower.getxCoord()][tmpTower.getyCoord() + rightZone] = '*';
					}
				}

			}
		}
	}

	static void enemiesKickAndJump(HashMap<Long, Tower> towers, HashMap<Long, Enemy> enemies,
							char[][] map) {
		int leftZone = 0, lengthOfJump = 0;
		long a;
		Tower tmpTower;
		Enemy tmpEnemy;
		boolean wasAnAttack;
		for (Map.Entry<Long, Enemy> enemyPair : enemies.entrySet()) {
			wasAnAttack = false;
			tmpEnemy = enemyPair.getValue();
			leftZone = (tmpEnemy.getyCoord() - tmpEnemy.getRangeOfAttack() >= 0) ?
					tmpEnemy.getRangeOfAttack() : tmpEnemy.getyCoord();
			for (int i = leftZone; i > 0; i--) {
				if (map[tmpEnemy.getxCoord()][tmpEnemy.getyCoord() - leftZone] == 'T') {
					wasAnAttack = true;
					a = GameCycle.coordHash(tmpEnemy.getxCoord(), tmpEnemy.getyCoord() - leftZone);
					tmpTower = towers.get(a);
					tmpTower.setHealthPoints(tmpTower.getHealthPoints() - tmpEnemy.getDmgPoints());
					if (tmpTower.getHealthPoints() <= 0) {
						towers.remove(a);
						map[tmpTower.getxCoord()][tmpTower.getyCoord() - leftZone] = '*';
					}
				}
			}
			if (!wasAnAttack) {
				for (int i = 1; i < tmpEnemy.getSpeed(); i++) {
					if (map[tmpEnemy.getxCoord()][tmpEnemy.getyCoord() - i] != '*') {
						wasAnAttack = true;
						lengthOfJump = i - 1;
					}
				}
				long a1 = GameCycle.coordHash(tmpEnemy.getxCoord(), tmpEnemy.getyCoord());
				long a2 = GameCycle.coordHash(tmpEnemy.getyCoord(), tmpEnemy.getxCoord());
				long a3 = enemyPair.getKey();
				lengthOfJump = wasAnAttack ? lengthOfJump : tmpEnemy.getSpeed();
				map[tmpEnemy.getxCoord()][tmpEnemy.getyCoord()] = '*';
				map[tmpEnemy.getxCoord()][tmpEnemy.getyCoord() - lengthOfJump] = 'E';
				a = GameCycle.coordHash(tmpEnemy.getxCoord(), tmpEnemy.getyCoord() - lengthOfJump);
				if (a != enemyPair.getKey()) {
					enemies.remove(enemyPair.getKey());
					enemies.put(a, tmpEnemy);
				}
			}

		}

	}

	static public void updateMap(HashMap<Long, Tower> towers, HashMap<Long, Enemy> enemies,
								 char[][] map) {
		towersShooting(towers, enemies, map);
		enemiesKickAndJump(towers, enemies, map);
	}
}
