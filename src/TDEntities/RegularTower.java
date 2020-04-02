package TDEntities;

public class RegularTower extends Tower {

	private int					healthPoints = 1;
	private final int			dmgPoints = 25;
	private static final int	id = 1;
	public static final char	graphic = 't';

	public static int getId() {
		return id;
	}

	public RegularTower(int xCoord, int yCoord) {
		super(xCoord, yCoord);
	}
}
