package TDEntities;

public class RegularTower extends Tower {

	public static final int	cost = 2;
	private static final char	graphic = 't';

	public RegularTower(int xCoord, int yCoord) {
		super(xCoord, yCoord);
		dmgPoints = 25;
		healthPoints = 100;
		rangeOfAttack = 3;
	}

	@Override
	public char getGraphic() {
		return graphic;
	}
}
