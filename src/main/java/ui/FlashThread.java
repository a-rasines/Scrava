package ui;

import ui.renderers.LiteralRenderer;

public class FlashThread extends Thread{
	
	public static final FlashThread INSTANCE = new FlashThread();
	
	public FlashThread() {
		super();
		start();
	}
	
	private LiteralRenderer hovered;
	private boolean interupted = false;
	
	public synchronized void setHovered(LiteralRenderer hovered) {
		if(hovered == this.hovered) return;
		if(this.hovered != null) {
			interupted = true;
			this.hovered.opacity = 1;
			//this.hovered.patch(0,0,0,0,null);
			interupted = false;
		}
		this.hovered = hovered;
	}
	private float opacity = 90;
	@Override
	public void run() {
		while(true) {
			if(this.hovered != null && !interupted) {
				if(opacity <= -90) opacity = 90;
				opacity -= 5;
				hovered.opacity = ((Math.abs(opacity)+10)/100);
//				try {
//					if(!interupted) //Threads racing once again
//						hovered.patch(0,0,0,0,null);
//				} catch(NullPointerException e) {}//Threads racing, I ain't no traffic cop
			}
			try { Thread.sleep(16); } catch (InterruptedException e) { e.printStackTrace(); }
		}
			
	}
}
