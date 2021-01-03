public interface IMonitor {
	public boolean goodStateReached();
	public void update(String sender, String receiver, String messageType, String[] parameters);
	public boolean requirementSatisfied();
	public void errorDetected(String sender, String receiver, String messageType, String[] parameters);
}

