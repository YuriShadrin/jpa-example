package dev.example.jpademo.exception;

public class ItemNotFoundException extends PersistentExceprion {

	private static final long serialVersionUID = 1L;

	final private long id;
	final private Item item;

	public ItemNotFoundException(long id, Item item) {
		super(String.format("Item not found: id=%d, type: %s", id, item));
		this.id = id;
		this.item = item;
	}

	public long getId() {
		return id;
	}

	public Item getItem() {
		return item;
	}
}
