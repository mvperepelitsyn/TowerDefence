package TDEntities;

public class BigZombie extends Enemy {

	private static final char 	graphic = 'Z';

	public BigZombie(int healthPoints, int dmgPoints, int speed, int xCoord, int yCoord, int rangeOfAttack) {
		super(healthPoints, dmgPoints, speed, xCoord, yCoord, rangeOfAttack);
	}

	public char getGraphic() {
		return graphic;
	}
}
