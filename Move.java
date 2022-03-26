import java.util.*;
import java.lang.*;

public class Move {
	private int id;
	private String moveType;
	private String name;
	private ElementType elementType;
	private int accuracy;
	private int priority;
	private int ammunition;
	private String target;
	private int basePower;
	private String statusCondition;
	private int healthPointEffect;
	private int attackEffect;
	private int defenseEffect;
	private int spAttackEffect;
	private int spDefenseEffect;
	private int speedEffect;

	/* Constructor: Move (Normal Move & Special Move) */
	public Move(int id, String moveType, String name, ElementType elementType, int accuracy, int priority, int ammunition, String target, int basePower) {
		this.id = id;
		this.moveType = moveType;
		this.name = name;
		this.elementType = elementType;
		this.accuracy = accuracy;
		this.priority = priority;
		this.ammunition = ammunition;
		this.target = target;
		this.basePower = basePower;
	}

	/* Constructor: Move (Status Move) */
	public Move(int id, String moveType, String name, ElementType elementType, int accuracy, int priority, int ammunition, String target, String statusCondition, int healthPointEffect, int attackEffect, int defenseEffect, int spAttackEffect, int spDefenseEffect, int speedEffect) {
		this.id = id;
		this.moveType = moveType;
		this.name = name;
		this.elementType = elementType;
		this.accuracy = accuracy;
		this.priority = priority;
		this.ammunition = ammunition;
		this.target = target;
		this.basePower = 0;
		this.statusCondition = statusCondition;
		this.healthPointEffect = healthPointEffect;
		this.attackEffect = attackEffect;
		this.defenseEffect = defenseEffect;
		this.spAttackEffect = spAttackEffect;
		this.spDefenseEffect = spDefenseEffect;
		this.speedEffect = speedEffect;
	}

	/* Getter: id */
	public int getId() {
		return this.id;
	}

	/* Getter: moveType */
	public String getMoveType() {
		return this.moveType;
	}

	/* Getter: name */
	public String getName() {
		return this.name;
	}

	/* Getter: elementType */
	public ElementType getElementType() {
		return this.elementType;
	}

	/* Getter: accuracy */
	public int getAccuracy() {
		return this.accuracy;
	}

	/* Getter: priority */
	public int getPriority() {
		return this.priority;
	}

	/* Getter: ammunition */
	public int getAmmunition() {
		return this.ammunition;
	}

	/* Getter: target */
	public String getTarget() {
		return this.target;
	}

	/* Getter: basePower */
	public int getBasePower() {
		return this.basePower;
	}

	/* Getter: statusCondition */
	public String getStatusCondition() {
		return this.statusCondition;
	}

	/* Getter: healthPointEffect */
	public int getHealthPointEffect() {
		return this.healthPointEffect;
	}

	/* Getter: attackEffect */
	public int getAttackEffect() {
		return this.attackEffect;
	}

	/* Getter: defenseEffect */
	public int getDefenseEffect() {
		return this.defenseEffect;
	}

	/* Getter: spAttackEffect */
	public int getSpAttackEffect() {
		return this.spAttackEffect;
	}

	/* Getter: spDefenseEffect */
	public int getSpDefenseEffect() {
		return this.spDefenseEffect;
	}

	/* Getter: speedEffect */
	public int getSpeedEffect() {
		return this.speedEffect;
	}

	/* Method: getFactor(), get factor from effect value */
	public double getFactor(int effect) {
		if (effect == -4) {
			return 2/6;
		} else if (effect == -3) {
			return 2/5;
		} else if (effect == -2) {
			return 2/4;
		} else if (effect == -1) {
			return 2/3;
		} else if (effect == 0) {
			return (double) 1;
		} else if (effect == 1) {
			return 3/2;
		} else if (effect == 2) {
			return (double) 2;
		} else if (effect == 3) {
			return 5/2;
		} else if (effect == 4) {
			return (double) 3;
		}
		return 1;
	}

	/* Method: calculateDamage(), calculates the damage. */
	public double calculateDamage(double sourceAttack, double targetDefense, double effectivity, double burnedEff) {
		double random = (new Random().nextInt((int)1.15) + 1);
		return Math.floor((this.basePower * (sourceAttack/targetDefense) + 2) * random * effectivity * burnedEff);
	}

	/* Method: execute(), executes the move. */
	public void execute(Monster source, Monster target, List<String[]> elementTypePool) {
		double damage = 0;
		double burnedEff = 1;
		double effectivity = 0;
		if (target.getBaseStats().getBurnTime() > 0) {
			burnedEff = 0.5;
		}
		if (this.target.equals("OWN")) {
			target = source;
		}
		for (ElementType esource : source.getElementTypes()) {
			if (esource == this.elementType) {
				for (ElementType etarget : target.getElementTypes()) {
					for (String[] arr : elementTypePool) {
						if (esource == ElementType.valueOf(arr[0]) && etarget == ElementType.valueOf(arr[1])) {
							if (effectivity < Double.valueOf(arr[2])) {
								effectivity = Double.valueOf(arr[2]);
								System.out.print(arr[0]+" >>> "+arr[1]+" ");
								System.out.println("(Effectivity: "+String.valueOf(effectivity)+")");
							}
						}
					}
				}
			}
		}
		if (moveType.equals("SPECIAL")) {
			damage = calculateDamage(source.getBaseStats().getSpAttack(), target.getBaseStats().getSpDefense(), effectivity, burnedEff);
		} else {
			damage = calculateDamage(source.getBaseStats().getAttack(), target.getBaseStats().getDefense(), effectivity, burnedEff);
		}
		if (moveType.equals("STATUS")){
			damage = 0;
			target.getBaseStats().setHealthPoint(Math.floor(target.getBaseStats().getHealthPoint() + (getHealthPointEffect()*target.getBaseStats().getMaxHealthPoint()/100)));
			target.getBaseStats().setAttack(target.getBaseStats().getAttack() * getFactor(getAttackEffect()));
			target.getBaseStats().setDefense(target.getBaseStats().getDefense() * getFactor(getDefenseEffect()));
			target.getBaseStats().setSpAttack(target.getBaseStats().getSpAttack() * getFactor(getSpAttackEffect()));
			target.getBaseStats().setSpDefense(target.getBaseStats().getSpDefense() * getFactor(getSpDefenseEffect()));
			target.getBaseStats().setSpeed(target.getBaseStats().getSpeed() * getFactor(getSpeedEffect()));
			if (statusCondition.equals("BURN")) {
				target.getBaseStats().setBurnTime(3);
			} else if (statusCondition.equals("POISON")) {
				target.getBaseStats().setSleepTime(3);
			} else if (statusCondition.equals("SLEEP")) {
				target.getBaseStats().setSleepTime(3);
			} else if (statusCondition.equals("PARALYZE")) {
				target.getBaseStats().setParalyzeTime(3);
				if (Controller.getSleepPossibility() == 1) {
					target.getBaseStats().setSleepTime(target.getBaseStats().getSleepTime() + 1);
				}
			}
		} 
		target.getBaseStats().setHealthPoint((target.getBaseStats().getHealthPoint() - damage));
		System.out.println(source.getName()+" used "+this.name+" [Damage: "+String.valueOf(damage)+"]\n");
	}
}
