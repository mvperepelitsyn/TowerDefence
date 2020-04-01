package TDEntities;

public class Enemy extends GameEntity {
	private int			healthPoints = 100;
	private int			dmgPoints = 1;
	private int			speed = 1;
	private int			xCoord;
	private int			yCoord;
	private int 		rangeOfAttack = 1;
	static int			id = 2;

	public Enemy(int xCoord, int yCoord) {
		this.xCoord = xCoord;
		this.yCoord = yCoord;
	}



	@Override
	public int getRangeOfAttack() {
		return rangeOfAttack;
	}

	@Override
	public int getHealthPoints() {
		return healthPoints;
	}

	@Override
	public void setHealthPoints(int healthPoints) {
		this.healthPoints = healthPoints;
	}

	@Override
	public int getDmgPoints() {
		return dmgPoints;
	}

	@Override
	public int getSpeed() {
		return speed;
	}

	@Override
	public int getxCoord() {
		return xCoord;
	}

	@Override
	public void setxCoord(int xCoord) {
		this.xCoord = xCoord;
	}

	@Override
	public int getyCoord() {
		return yCoord;
	}

	@Override
	public void setyCoord(int yCoord) {
		this.yCoord = yCoord;
	}

	@Override
	public int distance(GameEntity tower) {
		if (getyCoord() == tower.getyCoord()) {
			return Math.abs(getxCoord() - tower.getxCoord());

		} else {
			return -1;
		}
	}
}