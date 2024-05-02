package debug;

import java.io.PrintStream;
import java.util.List;

public class DebugOut extends PrintStream{
	
	public static final List<String> DEBUG_MUTED_FUNCTIONS = List.of(
			//block interaction
			"addBlock",
			"getBlockBundleIndex",
			"moveTo",
			"removeBlock",
			"removeChild",
			
			//event
			"mouseDragged",
			"mousePressed",
			"onClick",
			"onDrag",
			"onHover",
			//"onHoverEnd",
			
			"paintComponent",
			"patch",
			"renderText"
		);

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
		if(DEBUG_MUTED_FUNCTIONS.contains(caller.getMethodName())) return;
		super.println(prefix(caller) + x);
	}
	
	@Override
	public void println(String x) {
		if(!x.startsWith("[")) {
			StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
			if(DEBUG_MUTED_FUNCTIONS.contains(caller.getMethodName())) return;
			super.println(prefix(caller) + x);
		} else
			super.println(x);
	}
	@Override
	public void println(char x) {
		StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
		if(DEBUG_MUTED_FUNCTIONS.contains(caller.getMethodName())) return;
		super.println(prefix(caller) + x);
	}
	
	@Override
	public void println(char[] x) {
		StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
		if(DEBUG_MUTED_FUNCTIONS.contains(caller.getMethodName())) return;
		super.println(prefix(caller) + String.valueOf(x));
	}
	@Override
	public void println(double x) {
		StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
		if(DEBUG_MUTED_FUNCTIONS.contains(caller.getMethodName())) return;
		super.println(prefix(caller) + x);
	}
	@Override
	public void println(float x) {
		StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
		if(DEBUG_MUTED_FUNCTIONS.contains(caller.getMethodName())) return;
		super.println(prefix(caller) + x);
	}
	@Override
	public void println(int x) {
		StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
		if(DEBUG_MUTED_FUNCTIONS.contains(caller.getMethodName())) return;
		super.println(prefix(caller) + x);
	}
	@Override
	public void println(long x) {
		StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
		if(DEBUG_MUTED_FUNCTIONS.contains(caller.getMethodName())) return;
		super.println(prefix(caller) + x);
	}
	@Override
	public void println(Object x) {
		StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
		if(DEBUG_MUTED_FUNCTIONS.contains(caller.getMethodName())) return;
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
	
}
