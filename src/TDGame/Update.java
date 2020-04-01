package TDGame;

import TDEntities.Enemy;
import TDEntities.Tower;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Update {

	static void towersShooting(LinkedHashMap<Long, Tower> towers, LinkedHashMap<Long, Enemy> enemies,
							   char[][] map) {
		int leftZone = 0, rightZone = 0, max = 0;
		long a;
		Enemy tmpEnemy;
		Tower tmpTower;
		for (Map.Entry<Long, Tower> tower : towers.entrySet()) {
			tmpTower = tower.getValue();
			leftZone = (tmpTower.getyCoord() - tmpTower.getRangeOfAttack() >= 0) ?
					tmpTower.getRangeOfAttack() : tmpTower.getRangeOfAttack() - tmpTower.getyCoord();
			rightZone = (tmpTower.getyCoord() + tmpTower.getRangeOfAttack() >= map.length) ?
					map.length - 1 - tmpTower.getyCoord() : tmpTower.getRangeOfAttack();
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

	static void enemiesKickAndJump(LinkedHashMap<Long, Tower> towers, LinkedHashMap<Long, Enemy> enemies,
							char[][] map) {
		int leftZone = 0;
		long a;
		Tower tmpTower;
		Enemy tmpEnemy;
		for (Map.Entry<Long, Enemy> enemyPair : enemies.entrySet()) {
			tmpEnemy = enemyPair.getValue();
			leftZone = (tmpEnemy.getyCoord() - tmpEnemy.getRangeOfAttack() >= 0) ?
					tmpEnemy.getRangeOfAttack() : tmpEnemy.getRangeOfAttack() - tmpEnemy.getyCoord();
			for (int i = leftZone; i > 0; i--) {
				//if (map[])
			}

		}

	}

	static public void updateMap(LinkedHashMap<Long, Tower> towers, LinkedHashMap<Long, Enemy> enemies,
								 char[][] map) {
		towersShooting(towers, enemies, map);
		enemiesKickAndJump(towers, enemies, map);
	}
}
