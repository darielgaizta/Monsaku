class StatusMove extends Move {
	int buffHp;
	int buffAttack;
	int buffDefense;
	int buffSpecialAttack;
	int buffSpecialDefense;
	int buffSpeed;

	// constructor
	public StatusMove(String name, ElementType elementType, String moveType,
		int accuracy, int priority, int basePower, int ammunition) {
		super(name, elementType, moveType, accuracy, priority, basePower, ammunition);
		System.out.println("Status Move created.");
	}

	@Override
	public void execute(Monster target) {
		int rint = (int) Math.floor(Math.random() * 5) + 1;
		if (rint == 1) {
			target.getStats().burn();
		} else if (rint == 2) {
			target.getStats().poison();
		} else if (rint == 3) {
			target.getStats().sleep();
		} else if (rint == 4) {
			target.getStats().paralyze();
		} else if (rint == 5) {
			target.setStatsByFactors(buffHp, buffAttack, buffDefense,
			buffSpecialAttack, buffSpecialDefense, buffSpeed);
		}
	}
}