package domain.blocks.event;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class EventThread extends Thread {
	private static Queue<Thread> activeThreads = new LinkedBlockingQueue<>();
	
	public static void killThreads() {
		while(activeThreads.size() > 0)
			activeThreads.poll().interrupt();
	}
	public EventThread(Runnable r) {
		super(r);
		activeThreads.add(this);
	}
	
	private boolean isInterrupted = false;
	@Override
	public void interrupt() {
		super.interrupt();
		isInterrupted = true;
	}
	
	@Override
	public boolean isInterrupted() {
		return isInterrupted;
	}

}
