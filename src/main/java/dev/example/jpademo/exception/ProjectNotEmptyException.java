package dev.example.jpademo.exception;

public class ProjectNotEmptyException extends PersistentExceprion {

	private static final long serialVersionUID = 1L;

	final private long id;

	public ProjectNotEmptyException(long id, String message) {
		super(message);
		this.id = id;
	}

	public long getId() {
		return id;
	}
	
}
