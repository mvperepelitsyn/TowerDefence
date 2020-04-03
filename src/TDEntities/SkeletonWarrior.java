package TDEntities;

public class SkeletonWarrior extends Enemy {

	private static final char 	graphic = 'S';

	public SkeletonWarrior(int healthPoints, int dmgPoints, int speed, int xCoord, int yCoord, int rangeOfAttack) {
		super(healthPoints, dmgPoints, speed, xCoord, yCoord, rangeOfAttack);
	}

	public char getGraphic() {
		return graphic;
	}
}
