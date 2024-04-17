package debug;

import java.io.PrintStream;

import ui.components.BlockPanel;

public class DebugOut extends PrintStream{

	public static void setup() {
		System.setOut(new DebugOut());
	}
	
	private DebugOut() {
		super(System.out);
	}
	
	private String prefix(StackTraceElement caller) {
		return "["+caller.getClassName().replaceAll(".*\\.", "")+"] " + caller.getMethodName() + " (" + caller.getFileName() + ":" + caller.getLineNumber() + ") ";
	}
	
	@Override
	public void println(boolean x) {
		StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
		if(BlockPanel.DEBUG_MUTED_FUNCTIONS.contains(caller.getMethodName())) return;
		super.println(prefix(caller) + x);
	}
	
	@Override
	public void println(String x) {
		if(!x.startsWith("[")) {
			StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
			if(BlockPanel.DEBUG_MUTED_FUNCTIONS.contains(caller.getMethodName())) return;
			super.println(prefix(caller) + x);
		} else
			super.println(x);
	}
	@Override
	public void println(char x) {
		StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
		if(BlockPanel.DEBUG_MUTED_FUNCTIONS.contains(caller.getMethodName())) return;
		super.println(prefix(caller) + x);
	}
	
	@Override
	public void println(char[] x) {
		StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
		if(BlockPanel.DEBUG_MUTED_FUNCTIONS.contains(caller.getMethodName())) return;
		super.println(prefix(caller) + String.valueOf(x));
	}
	@Override
	public void println(double x) {
		StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
		if(BlockPanel.DEBUG_MUTED_FUNCTIONS.contains(caller.getMethodName())) return;
		super.println(prefix(caller) + x);
	}
	@Override
	public void println(float x) {
		StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
		if(BlockPanel.DEBUG_MUTED_FUNCTIONS.contains(caller.getMethodName())) return;
		super.println(prefix(caller) + x);
	}
	@Override
	public void println(int x) {
		StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
		if(BlockPanel.DEBUG_MUTED_FUNCTIONS.contains(caller.getMethodName())) return;
		super.println(prefix(caller) + x);
	}
	@Override
	public void println(long x) {
		StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
		if(BlockPanel.DEBUG_MUTED_FUNCTIONS.contains(caller.getMethodName())) return;
		super.println(prefix(caller) + x);
	}
	@Override
	public void println(Object x) {
		StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
		if(BlockPanel.DEBUG_MUTED_FUNCTIONS.contains(caller.getMethodName())) return;
		super.println(prefix(caller) + x);
	}
	
	public void printStackTrace() {
		StackTraceElement[] caller = Thread.currentThread().getStackTrace();
		String out = "";
		for(StackTraceElement element : caller) {
			out += prefix(element) + "\n";
		}
		super.println(out);
		
	}
	
	public static void main(String[] args) {
		setup();
		System.out.println("a");
	}
	
}
