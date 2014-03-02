package pl.litterae.locpin.controller;

import android.location.Location;

import pl.litterae.locpin.R;
import pl.litterae.locpin.common.Cleanupable;
import pl.litterae.locpin.common.Command;
import pl.litterae.locpin.model.ServerUrl;
import pl.litterae.locpin.model.StartInfo;

public final class CommandFactory implements Cleanupable {
	private static final CommandFactory instance = new CommandFactory();
	//mutable
	private Command currentCommand;

	private CommandFactory() {
	}

	public static CommandFactory getInstance() {
		return instance;
	}

	@Override
	public void cleanup() {
		if (currentCommand != null) {
			currentCommand.cleanup();
			currentCommand = null;
		}
	}

	Command produceCommandForMenu(int actionID) {
		cleanup();
		switch (actionID) {
			case R.id.action_start:
				currentCommand = produceCommandForType(Command.Type.START, ServerUrl.START.getUrl());
				break;
			case R.id.action_locate_me:
				currentCommand = produceCommandForType(Command.Type.LOCATE_ME, null);
				break;
		}
		return currentCommand;
	}

	Command produceCommandForType(Command.Type commandType, Object data) {
		cleanup();
		if (commandType != null) {
			switch (commandType) {
				case START:
					currentCommand = new StartCommand((String) data);
					break;
				case ACCESS_IMAGE:
					if (data instanceof StartInfo) {
						StartInfo startInfo = (StartInfo) data;
						currentCommand = new AccessImageCommand(startInfo.getImageUrl());
					}
					break;
				case LOCATE_ME:
					currentCommand = new LocateMeCommand();
					break;
				case SHOW_DISTANCE:
					currentCommand = new ShowDistanceCommand((Location) data);
					break;
			}
		}
		return currentCommand;
	}

}
