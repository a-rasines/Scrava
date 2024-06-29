package clickable;

import java.util.Collection;
import java.util.List;

import domain.models.interfaces.Clickable;
import domain.models.interfaces.InvocableBlock;
import domain.models.interfaces.Valuable;
import domain.models.interfaces.VariableHolder;
import ui.components.BlockPanel;
import ui.renderers.CapsuleRenderer;
import ui.renderers.IRenderer.DragableRenderer;
import ui.renderers.InvocableBlockRenderer;

public class CapsuleBlockClickable extends InvocableClickable {

	private static final long serialVersionUID = -2966714178244994567L;
	
	public CapsuleBlockClickable(DragableRenderer renderer, BlockClickable parent) {
		super(renderer, parent);
	}
	
	@Override
	public CapsuleRenderer getRenderer() {
		return (CapsuleRenderer) super.getRenderer();
	}
	
	public boolean removeChild(int bundle, BlockClickable child) {
		System.out.println("removing " + child + " from " + bundle);
		if(child.getBlock() instanceof Valuable)
			super.removeChild(child);
		else {
			System.out.println("not valuable");
			if(getRenderer().remove(bundle, child.getRenderer())) {
				System.out.println("inside");				
				child.setParent(null);
				if(child.getBlock() instanceof Valuable vBlock) {
					VariableHolder vh = (VariableHolder)getBlock();
					vh.removeVariable(vBlock);
				}
				System.out.println("removed child " + child + " newSize:" + getRenderer().sizeOf(bundle));
				InvocableClickable bc = ((InvocableClickable) child).next();
				BlockPanel.INSTANCE.addBlock(child.getRenderer());
				
				
				while(bc != null) {
					if(bc.getBlock() instanceof Valuable vBlock) {
						VariableHolder vh = (VariableHolder)getBlock();
						vh.removeVariable(vBlock);
					}
					bc.setParent(null);
					System.out.println("removing child " + bc + " res:" + getRenderer().remove(bundle, bc.getRenderer()) + " newSize:" + getRenderer().sizeOf(bundle));
					BlockPanel.INSTANCE.addBlock(bc.getRenderer());
					bc = bc.next();
				}
				return true;
			}
			return false;
		}
		return true;
	}
	
	@Override
	public void removeChild(BlockClickable child) {
		if(child.getBlock() instanceof InvocableBlock) {
			System.out.println("removing " + child.toString());
			for(int i = 0; i < getRenderer().bundleCount(); i++)
				if(removeChild(i, child))
					return;
		}
		System.out.println("No bundle");
		super.removeChild(child);
	}
	
	private transient boolean inside = false;
	@Override
	public void onHover(int x, int y, BlockClickable clicked) {
		System.out.println("▓▓▓▓▓ HOVER " + this + "▓▓▓▓▓ hovered: " + hovered);
		if(hovered == clicked)
			hovered = null;
		super.append = !(between(x, 0, getRenderer().getWidth()) && between(y, 0, getRenderer().getHeight()));
		int index = getBlockBundleIndex(clicked);
		inside = index < 0 && !append;
		System.out.println("outside_bundle=" + inside + " append=" + append);
		if(inside) {
			System.out.println("arm hovered");
			hovered = null;
			super.onHover(x, y, clicked);
			return;
		}
		if(index < 0) {
			System.out.println("no bundle");
			return;
		}
		Rect r = getRenderer().getBlockBundlesSize().get(index);
		System.out.println("Bundle size: " + r);
		if(r!=null) {
			if(hovered != null) {
				Rect r1 = hovered.getPosition();
				if(x > r1.x && y > r1.y && x < r1.x + r1.w && y < r1.y + r1.h + InvocableBlockRenderer.CONNECTOR.getHeight()) {
					System.out.println("px:" + x + " py:" + y + " hx:" + hovered.getPosition().x + " hy:" + hovered.getPosition().y + " rx:" + r.x + " ry:" + r.y);
					hovered.onHover(x - hovered.getPosition().x, y - hovered.getPosition().y, clicked);
					return;
				} else {
					hovered.onHoverEnd(false, clicked);
					System.out.println("hover end " + hovered);
					hovered = null;
				}
			}
			for(InvocableBlock ib : getRenderer().getBlocksOf(index)) {
				InvocableClickable ic = (InvocableClickable) ib.getRenderer().getClickable();
				Rect r1 = ic.getPosition();
				System.out.println(ib.toString().replaceAll(".*\\.", "") + " " + r1);
				r1.y = r1.y + InvocableBlockRenderer.CONNECTOR.getHeight();
				r1.h = r1.h - InvocableBlockRenderer.CONNECTOR.getHeight();
				if(x > r1.x && y > r1.y && x < r1.x + r1.w && y < r1.y + r1.h) {
					System.out.println("hovered " + ic.getBlock().toString().replaceAll(".*\\.", ""));
					hovered = ic;
					break;
				}
			}
			if(hovered != null)
				hovered.onHover(x - hovered.getPosition().x, y - hovered.getPosition().y, clicked);
		}
		
	}
	
	private int getBlockBundleIndex(BlockClickable clicked) {
		List<Rect> rcts = getRenderer().getBlockBundlesSize();
		for(int j = 0; j < getRenderer().getBlockBundlesSize().size(); j++) {
			Rect pos = rcts.get(j);
			System.out.println("pY:" + pos.y + " rY:" + (clicked.getRenderer().getGlobalY() - getRenderer().getGlobalY() + 10) + " pH:" + (pos.y + pos.h + InvocableBlockRenderer.CONNECTOR.getHeight()));
			if(pos.y <= (clicked.getRenderer().getGlobalY() - getRenderer().getGlobalY() + 10) && pos.y + pos.h + InvocableBlockRenderer.CONNECTOR.getHeight() >= (clicked.getRenderer().getGlobalY() - getRenderer().getGlobalY() + 10)) {
				System.out.println("detected inside bundle " + j);
				return j;
			}
		}
		return -1;
	}
	
	@Override
	public void onHoverEnd(boolean click, BlockClickable clicked) {
		if(click)
			System.out.println("▓▓ hover end " + this.getBlock().toString().replaceAll(".*\\.", "") + " hovered=" + (hovered==null?"null":hovered) + " inside=" + inside + " append=" + append);
		if(inside && (append || clicked.getBlock() instanceof Valuable)) { //Invocable behaviour
			System.out.println("Hovered bar");
			super.onHoverEnd(click, clicked);
			return;
		}
		
		if(!click)return; //No action needed if hover has just changed block
		
		else if(!(clicked instanceof InvocableClickable)) { //If block gonna be added
			if(hovered != null) { //This block isn't the one hovered
				System.out.println("hovered:" + hovered.getBlock().toString().replaceAll(".*\\.", ""));
				hovered.onHoverEnd(click, clicked);
			}
		} else if(hovered instanceof CapsuleBlockClickable cbc && cbc.getBlockBundleIndex(clicked) > -1) {
			hovered.onHoverEnd(click, clicked);
			return;
		} else {
			System.out.println("block hover end");
			int index = getBlockBundleIndex(clicked);
			if(index < 0) {
				System.out.println("no bundle");
				super.onHoverEnd(click, clicked);
				return;
			}
			if(hovered == null) {
				System.out.println("adding to queue");
				InvocableClickable cl = (InvocableClickable) clicked;
				BlockPanel.INSTANCE.removeBlock(clicked.getRenderer());
				nestClickable(index, clicked);
				CapsuleRenderer r = getRenderer();
				int ind = r.indexOf(index, clicked.getRenderer());
				if(ind > 0)
					((InvocableClickable)r.get(index, ind - 1).getClickable()).setNext(cl);
				clicked = ((InvocableClickable) clicked).next();
				while(clicked != null) {
					cl = (InvocableClickable) clicked;
					BlockPanel.INSTANCE.removeBlock(clicked.getRenderer());
					nestClickable(index, clicked);
					clicked = ((InvocableClickable) clicked).next();
					ind++;
				}
				System.out.println("cl: " + cl.getBlock().toString() + " -> " + getRenderer().get(index, ind).getBlock());
				if(r.sizeOf(index) > ind + 1)
					cl.setNext(((InvocableClickable)r.get(index, ind + 1).getClickable()));
			} else {
				int i = getRenderer().indexOf(index, (DragableRenderer) hovered.getRenderer()) + 1;
				System.out.println("Hovered " + hovered.getBlock().toString().replaceAll(".*\\.", "") + " endIndex:" + i);
				InvocableClickable end = getRenderer().sizeOf(index) > i ? (InvocableClickable) getRenderer().get(index, i).getClickable():null;
				((InvocableClickable) hovered).setNext((InvocableClickable) clicked);
				while(true) {
					BlockPanel.INSTANCE.removeBlock(clicked.getRenderer());
					Rect pos = getRenderer().getBlockBundlesSize().get(index);
					clicked.getRenderer().moveTo(pos.x, pos.y + pos.h);
					System.out.println(index);
					if(end != null)
						nestClickable(index, i++, clicked);
					else
						nestClickable(index, -1, clicked);
					if(((InvocableClickable) clicked).next() == null) {
						((InvocableClickable) clicked).setNext(end);
						break;
					} else
						clicked = ((InvocableClickable) clicked).next();
				}
			}
			getRenderer().update();
			System.out.println("Update Chaneeeel"); //@BellosoAsier wanted to add his touch to the debug process
		}
	}
	
	public void nestClickable(int index, BlockClickable child) {
		child.setParent(this);
		if(child.getBlock() instanceof InvocableBlock && !getRenderer().contains(index, child.getRenderer()))
			getRenderer().add(index, -1, child.getRenderer());
	}
	
	public void nestClickable(int bundle, int index, BlockClickable child) {
		System.out.println(toString() + " <- " + child.getBlock().toString().replaceAll(".*\\.", ""));
		getRenderer().add(bundle, index, child.getRenderer());
		update();
	}
	
	@Override
	protected Collection<Clickable> getNestedClickables() {
		Collection<Clickable> cl = super.getNestedClickables();
		for(int i = 0; i < getRenderer().bundleCount(); i++)
		for(InvocableBlock bbr: getRenderer().getBlocksOf(i))
			cl.add(bbr.getRenderer().getClickable());
		return cl;
	}
	@Override
	public String toString() {
		return getBlock().getClass().getSimpleName() + "@" + Integer.toHexString(getBlock().hashCode());
	}
}
