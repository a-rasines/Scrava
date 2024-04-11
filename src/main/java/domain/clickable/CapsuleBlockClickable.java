package domain.clickable;

import java.util.Collection;
import java.util.List;

import domain.models.interfaces.Clickable;
import domain.models.interfaces.InvocableBlock;
import domain.models.interfaces.Valuable;
import domain.models.interfaces.VariableHolder;
import ui.BlockPanel;
import ui.renderers.CapsuleRenderer;
import ui.renderers.IRenderer;
import ui.renderers.IRenderer.DragableRenderer;
import ui.renderers.IRenderer.IRenderable;
import ui.renderers.InvocableBlockRenderer;
import ui.renderers.LiteralRenderer;
import ui.renderers.LiteralRenderer.LiteralRenderable;

public class CapsuleBlockClickable extends InvocableClickable {

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
				if(child.getBlock() instanceof Valuable) {
					VariableHolder vh = (VariableHolder)getBlock();
					LiteralRenderable<?> lr = vh.removeVariable((Valuable<?>)child.getBlock());
					LiteralRenderer.of(lr, lr.value(), this);
				}
				System.out.println("removed child " + child + " newSize:" + getRenderer().sizeOf(bundle));
				InvocableClickable bc = ((InvocableClickable) child).next();
				BlockPanel.INSTANCE.addBlock(child.getRenderer());
				
				
				while(bc != null) {
					if(bc.getBlock() instanceof Valuable) {
						VariableHolder vh = (VariableHolder)getBlock();
						LiteralRenderable<?> lr = vh.removeVariable((Valuable<?>)child.getBlock());
						LiteralRenderer.of(lr, lr.value(), this);
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
	
	private boolean inside = false;
	@Override
	public void onHover(int x, int y, BlockClickable clicked) {
		if(hovered == clicked)
			hovered = null;
		super.append = !(between(x, 0, getRenderer().getWidth()) && between(y, 0, getRenderer().getHeight()));
		int index = getBlockBundleIndex(clicked);
		inside = index < 0 && !append;
		if(inside) {
			System.out.println("arm hovered");
			hovered = null;
			super.onHover(x, y, clicked);
		}
		if(index < 0) {
			System.out.println("no bundle");
			return;
		}
		Rect r = getRenderer().getBlockBundlesSize().get(index);
		System.out.println("hovered: " + (hovered==null?"null":hovered.getBlock().toString().replaceAll(".*\\.", "")));
		if(r!=null) {
			if(hovered != null) {
				Rect r1 = hovered.getPosition();
				if(x > r1.x && y > r1.y && x < r1.x + r1.w && y < r1.y + r1.h + InvocableBlockRenderer.CONNECTOR.getHeight()) {
					hovered.onHover(x, y, clicked);
					return;
				} else
					hovered = null;
			}
			if(index < 0) {
				System.out.println("no bundle");
				return;
			}
			for(InvocableBlock ib : getRenderer().getBlocksOf(index)) {
				InvocableClickable ic = (InvocableClickable) IRenderer.getDragableRendererOf((IRenderable) ib).getClickable();
				Rect r1 = ic.getPosition();
				System.out.println(ib.toString().replaceAll(".*\\.", "") + " " + r1);
				r1.y = r1.y + InvocableBlockRenderer.CONNECTOR.getHeight();
				r1.h = r1.h - InvocableBlockRenderer.CONNECTOR.getHeight();
				System.out.println(r1);
				if(x > r1.x && y > r1.y && x < r1.x + r1.w && y < r1.y + r1.h) {
					System.out.println("hovered " + ic.getBlock().toString().replaceAll(".*\\.", ""));
					hovered = ic;
				}
				break;
			}
			if(hovered != null)
				hovered.onHover(x, y, clicked);
		}
		
	}
	
	private int getBlockBundleIndex(BlockClickable clicked) {
		List<Rect> rcts = getRenderer().getBlockBundlesSize();
		for(int j = 0; j < getRenderer().getBlockBundlesSize().size(); j++) {
			Rect pos = rcts.get(j);
			System.out.println("pY:" + pos.y + " rY:" + (clicked.getRenderer().getY() - getRenderer().getY() + 10) + " pH:" + (pos.y + pos.h + InvocableBlockRenderer.CONNECTOR.getHeight()));
			if(pos.y <= (clicked.getRenderer().getY() - getRenderer().getY() + 10) && pos.y + pos.h + InvocableBlockRenderer.CONNECTOR.getHeight() >= (clicked.getRenderer().getY() - getRenderer().getY() + 10)) {
				System.out.println("detected inside bundle " + j);
				return j;
			}
		}
		return -1;
	}
	
	@Override
	public void onHoverEnd(boolean click, BlockClickable clicked) {
		if(click)
			System.out.println("▓▓ hover end " + this.getBlock().toString().replaceAll(".*\\.", "") + " inside=" + inside + " append=" + append);
		if(inside && (append || clicked.getBlock() instanceof Valuable)) {
			System.out.println("Hovered bar");
			super.onHoverEnd(click, clicked);
			return;
		}
		if(!click)return;
		else if(!(clicked instanceof InvocableClickable)) {
			if(hovered != null) {
				System.out.println(hovered.getBlock().toString().replaceAll(".*\\.", ""));
				hovered.onHoverEnd(click, clicked);
			}
		} else {
			System.out.println("block hover end");
			int i = 0;
			int index = getBlockBundleIndex(clicked);
			if(index < 0) {
				System.out.println("no bundle");
				super.onHoverEnd(click, clicked);
				return;
			}
			if(hovered == null) {
				System.out.println("adding to queue");
				InvocableClickable cl = null;
				Rect pos = getRenderer().getBlockBundlesSize().get(index);
				clicked.setPosition(pos.x, pos.y + pos.h);
				while(clicked != null) {
					cl = (InvocableClickable) clicked;
					BlockPanel.INSTANCE.removeBlock(clicked.getRenderer());
					nestClickable(index, i++, clicked);
					clicked = ((InvocableClickable) clicked).next();
				}
				if(getRenderer().sizeOf(index) > i)
					cl.setNext((InvocableClickable) getRenderer().get(index, i).getClickable());
			} else {
				i = getRenderer().indexOf(index, (DragableRenderer) hovered.getRenderer()) + 1;
				System.out.println("Hovered " + hovered.getBlock().toString().replaceAll(".*\\.", "") + " endIndex:" + i);
				InvocableClickable end = getRenderer().sizeOf(index) > i?(InvocableClickable) getRenderer().get(index, i).getClickable():null;
				((InvocableClickable) hovered).setNext((InvocableClickable) clicked);
				while(true) {
					BlockPanel.INSTANCE.removeBlock(clicked.getRenderer());
					Rect pos = getRenderer().getBlockBundlesSize().get(index);
					clicked.getRenderer().moveTo(pos.x, pos.y + pos.h);
					if(end != null)
						nestClickable(index, i++, clicked);
					if(((InvocableClickable) clicked).next() == null) {
						((InvocableClickable) clicked).setNext(end);
						break;
					} else
						clicked = ((InvocableClickable) clicked).next();
				}
			}
			getRenderer().update();
		}
	}
	
	public void nestClickable(int index, BlockClickable child) {
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
			cl.add(IRenderer.getDragableRendererOf(bbr).getClickable());
		return cl;
	}
	@Override
	public String toString() {
		return getBlock().getClass().getSimpleName() + "@" + Integer.toHexString(getBlock().hashCode());
	}
}
