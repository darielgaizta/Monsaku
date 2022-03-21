public interface StatusCondition {
	public void burn();
	public void poison();
	public void sleep();
	public void paralyze();
	public boolean isConditionActive();
	public void setConditionActive();
	public boolean isStunned();
	public void setStunned();
}