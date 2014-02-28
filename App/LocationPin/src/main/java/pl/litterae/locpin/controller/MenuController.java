package pl.litterae.locpin.controller;

import android.os.Handler;

import pl.litterae.locpin.common.Cleanupable;
import pl.litterae.locpin.common.Command;
import pl.litterae.locpin.common.ResultNotifier;
import pl.litterae.locpin.model.StartInfo;

public final class MenuController implements Cleanupable {
	private static final MenuController instance = new MenuController();

	private MenuController() {
	}

	@Override
	public void cleanup() {
		CommandFactory.getInstance().cleanup();
	}

	public static MenuController getInstance() {
		return instance;
	}

	public boolean performActionFor(int actionID) {
		final Command command = CommandFactory.getInstance().produceCommandForMenu(actionID);
		if (command != null) {
			command.executeWith(new ResultNotifier() {
				@Override
				public void completedWithSuccess(boolean success, final Object result) {
					if (success) {
						switch (command.getType()) {
							case START:
								new Handler().post(new Runnable() {
									@Override
									public void run() {
										StartInfo startInfo = (StartInfo) result;
									}
								});
								break;
						}
					} else {
						//TODO: toast or popup with error info
					}
				}
			});
			return true;
		} else {
			return false;
		}
	}

}
