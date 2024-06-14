package domain.values;

import java.util.ArrayList;
import java.util.List;

import domain.Project;
import domain.Sprite;
import domain.models.interfaces.Valuable;
import ui.components.SpritePanel;
import ui.renderers.LiteralRenderer.LiteralRenderable;
import ui.renderers.SimpleBlockRenderer.SimpleRenderable;

public interface IVariable<T> extends SimpleRenderable<T>, Valuable<T> {
	
	@Override public default Valuable<?> getVariableAt(int i) { return null; }
	@Override public default Valuable<?>[] getAllVariables() { return new Valuable[0]; }
	@Override public default boolean isAplicable(Valuable<?> v) { return false; }
	@Override public default LiteralRenderable<?> removeVariable(Valuable<?> v) { return null; }
	@Override public default void removeVariableAt(int i) {}
	@Override public default void replaceVariable(Valuable<?> old, Valuable<?> newValue) {}
	@Override public default void setVariableAt(int i, Valuable<?> v) {}
	
	@Override
	public default BlockCategory getCategory() {
		return switch(value()) {
			case Number n -> BlockCategory.NUMBER_VARIABLE;
			case Boolean b -> BlockCategory.BOOLEAN_VARIABLE;
			default -> BlockCategory.STRING_VARIABLE;
		};
	}
	
	public static IVariable<?> getGlobalVariable(String name) {
		return getVariable(null, name);
	}
	
	public static IVariable<?> getVariable(Sprite s, String name) {
		return Project.getActiveProject().getVariable(s, name);
	}
	
	public static List<IVariable<?>> getVisibleVariables() {
		List<IVariable<?>> output = new ArrayList<>(Project.getActiveProject().getVariablesOf(null).values());
		output.addAll(Project.getActiveProject().getVariablesOf(SpritePanel.getSprite()).values());
		return output;
	}
	
	public boolean isNative();
	
	public boolean isGlobal();
	
	public String getName();
	
	public void setName(String s);
	
	public Sprite getSprite();
	
	@Override
	default String getTitle() {
		return getName();
	}

}
