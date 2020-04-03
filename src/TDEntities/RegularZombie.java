package TDEntities;

public class RegularZombie extends Enemy {

	private static final char 	graphic = 'z';

	public RegularZombie(int healthPoints, int dmgPoints, int speed, int xCoord, int yCoord, int rangeOfAttack) {
		super(healthPoints, dmgPoints, speed, xCoord, yCoord, rangeOfAttack);
	}

	public char getGraphic() {
		return graphic;
	}
}
