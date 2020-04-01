package TDEntities;

public class Enemy extends GameEntity {
	private int			healthPoints;
	private int			dmgPoints;
	private int			speed;
	private int			xCoord;
	private int			yCoord;
	private int 		rangeOfAttack;
	final static int	id = 2;
	public final char 	graphic = 'E';

	public Enemy(int healthPoints, int dmgPoints, int speed, int xCoord, int yCoord, int rangeOfAttack) {
		this.healthPoints = healthPoints;
		this.dmgPoints = dmgPoints;
		this.speed = speed;
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		this.rangeOfAttack = rangeOfAttack;
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
}