package TDEntities;

public class Tower extends GameEntity {
	protected int				healthPoints = 1;
	protected int				dmgPoints = 50;
	protected final int			speed = 0;
	private int					xCoord;
	private int					yCoord;
	protected int 				rangeOfAttack = 5;
	public static final int 	id = 0;
	public static final char 	graphic = 'T';

	//other variables are basic
	public Tower(int xCoord, int yCoord) {
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
}
