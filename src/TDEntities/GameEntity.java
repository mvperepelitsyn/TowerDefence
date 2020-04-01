package TDEntities;

abstract class GameEntity {
	private int			healthPoints;
	private int			dmgPoints;
	private int			speed;
	private int			xCoord;
	private int			yCoord;
	private int 		rangeOfAttack;
	static int			id = 0;


	public int getRangeOfAttack() {
		return rangeOfAttack;
	}

	public void setRangeOfAttack(int rangeOfAttack) {
		this.rangeOfAttack = rangeOfAttack;
	}

	public int getHealthPoints() {
		return healthPoints;
	}

	public void setHealthPoints(int healthPoints) {
		this.healthPoints = healthPoints;
	}

	public int getDmgPoints() {
		return dmgPoints;
	}

	public void setDmgPoints(int dmgPoints) {
		this.dmgPoints = dmgPoints;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getxCoord() {
		return xCoord;
	}

	public void setxCoord(int xCoord) {
		this.xCoord = xCoord;
	}

	public int getyCoord() {
		return yCoord;
	}

	public void setyCoord(int yCoord) {
		this.yCoord = yCoord;
	}

	abstract public int distance(GameEntity entity);

}
