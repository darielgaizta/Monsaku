import java.util.*;

public class DefaultMove extends Move {
	
	/* Constructor: DefaultMove */
	public DefaultMove() {
		super(0, "NORMAL", "Basic Attack", null, 100, 0, 100000, "ENEMY", 50);
	}

	@Override
	public void execute(Monster source, Monster target, List<String[]> elementTypePool) {
		double burnedEff = 1;
		if (target.getBaseStats().getBurnTime() > 0) {
			burnedEff = 0.5;
		}
		double damage = calculateDamage(source.getBaseStats().getAttack(), target.getBaseStats().getDefense(), 1, burnedEff);
		source.getBaseStats().setHealthPoint(source.getBaseStats().getHealthPoint() - (source.getBaseStats().getMaxHealthPoint()/4));
		target.getBaseStats().setHealthPoint((target.getBaseStats().getHealthPoint() - damage));
		System.out.println(source.getName()+" used "+this.getName()+" [Damage: "+String.valueOf(damage)+"]\n");
	}
}