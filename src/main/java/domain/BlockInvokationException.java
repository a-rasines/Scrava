package domain;

public class BlockInvokationException extends RuntimeException{
	private static final long serialVersionUID = 2771864721475560405L;

	public BlockInvokationException() {
		super();
	}

	public BlockInvokationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public BlockInvokationException(String message, Throwable cause) {
		super(message, cause);
	}

	public BlockInvokationException(String message) {
		super(message);
	}

	public BlockInvokationException(Throwable cause) {
		super(cause);
	}
	
	

}
