import java.util.EventListener;

public interface ChatListener extends EventListener
{
	public void sendEventOccured(SendEvent s);
}
