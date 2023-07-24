package dev.example.jpademo.exception;

public class PersistentExceprion extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PersistentExceprion(String message) {
		super(message);
	}

	public PersistentExceprion(String message, Throwable cause) {
		super(message, cause);
	}
}
