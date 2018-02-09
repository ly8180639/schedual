package org.schedual.server.cache;

public class RedisException extends Exception{
	/**
	 * <p>Description: </p>
	 * <p>Author:liuyang</p>
	 * @Fields serialVersionUID 
	 */
	private static final long serialVersionUID = 1L;

	public RedisException() {
		super();
	}
	
	public RedisException(String message) {
		super(message);
	}
	
	public RedisException(Throwable cause) {
		super(cause);
	}

	public RedisException(String message, Throwable cause) {
		super(message, cause);
	}
}
