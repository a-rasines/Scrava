package domain.models.types;

import domain.Sprite;

public abstract class EventBlock extends CapsuleBlock{
	private static final long serialVersionUID = 7925114653357578338L;

	@Override
	public EventBlock create(Sprite s) {
		EventBlock ev = newInstance(s);
		s.registerEvent(ev);
		return ev;
	}
	
	public abstract EventBlock newInstance(Sprite s);
	
	@Override
	public boolean attachable() {
		return false;
	}
	
	@Override
	public String getHead() {
		return null;
	}

}
