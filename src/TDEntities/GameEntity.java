package TDEntities;

abstract class GameEntity {
	static int id = 0;

	abstract public int getRangeOfAttack();

	abstract public int getHealthPoints();

	abstract public void setHealthPoints(int healthPoints);

	abstract public int getDmgPoints();

	abstract public int getSpeed();

	abstract public int getxCoord();

	abstract public void setxCoord(int xCoord);

	abstract public int getyCoord();

	abstract public void setyCoord(int yCoord);

	abstract public int distance(GameEntity o);

}
