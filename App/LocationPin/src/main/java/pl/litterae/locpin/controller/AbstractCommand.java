package pl.litterae.locpin.controller;

import pl.litterae.locpin.common.Command;

public abstract class AbstractCommand implements Command {
	private final Type type;

	protected AbstractCommand(Type type) {
		this.type = type;
	}

	@Override
	public void cleanup() {
	}

	@Override
	public final Type getType() {
		return type;
	}
}
