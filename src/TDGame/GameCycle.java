package TDGame;

import TDEntities.Enemy;
import TDEntities.RegularTower;
import TDEntities.StrongTower;
import TDEntities.Tower;

import java.util.*;

public class GameCycle {

	static int iterForEnemies;
	static int towerPoints;
	static int numberOfTowersSimultaniously;
	static int lives = 3;

	public static void gameCycle() {
		Scanner scan = new Scanner(System.in);
		GameMap gameMap = new GameMap(getMap(scan));
		while (iterForEnemies + gameMap.getEnemyCount() > 0 && lives > 0) {
			System.out.println("Lives left: " + lives);
			System.out.println("Gold for tower: "+ towerPoints);
			System.out.println("Towers can be added on field: "+ (numberOfTowersSimultaniously - gameMap.getTowersCount()));
			System.out.println("Enemy left: "+ (iterForEnemies + gameMap.getEnemyCount()));
			//player input (add tower, delete tower, stop game, skip turn)
			action(scan, gameMap);
			/* in doTurn we have only lives left (int) */
			lives -= gameMap.doTurn();

			//generate new enemy
			/* with class iter enemy change here if we create an enemy*/
			iterForEnemies = gameMap.addEnemy(iterForEnemies);

			//show map after turn
			gameMap.showMap();
			//show lists of the tower and enemy which placed on the map
			gameMap.showLists();
			System.out.println();
			//economics!!!
			towerPoints++;
		}
		if (lives > 0) {
			System.out.println("Congratulations! You won!");
		} else {
			System.out.println("You lost! Better luck next time!");
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
				towerPoints = (int) (Math.sqrt(iterForEnemies) * 2.5);
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

	static void action(Scanner scan,
					   GameMap gameMap) {
		System.out.println("Please input the action, that you want to perform. Type \"usage\" " +
				"if you don't know the actions.");
		String	strAction;
		boolean	flagAction = true;
		while (flagAction) {
			strAction = scan.nextLine().trim().toLowerCase();
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
					if (towerPoints > 0 && gameMap.getTowersCount() <= numberOfTowersSimultaniously) {
						int newTowerPoints = gameMap.addNewTower(scan, towerPoints);
						if (newTowerPoints == -1) {
							System.out.println("Please input the action, that you want to perform. Type \"usage\" " +
									"if you don't know the actions.");
							break;
						}
						else {
							towerPoints = newTowerPoints;
						}
					} else {
						System.out.println("Oops! You can't add more Towers.\nThere are too many Towers on the map or you're run out of Towers.");
					}
					flagAction = false;
					break;
				case ("delete") :
					int addPoints = gameMap.deleteTower(scan);
					if (addPoints <= 0) {
						System.out.println("Please input the action, that you want to perform. Type \"usage\" " +
								"if you don't know the actions.");
						break;
					}
					else {
						towerPoints+=addPoints;
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
}