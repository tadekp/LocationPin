package pl.litterae.locpin.controller;

import android.content.Context;
import android.os.Handler;

import pl.litterae.locpin.R;
import pl.litterae.locpin.common.Cleanupable;
import pl.litterae.locpin.common.Command;
import pl.litterae.locpin.common.ResultNotifier;
import pl.litterae.locpin.model.PinModel;
import pl.litterae.locpin.view.popup.InfoPopup;

public final class MenuController implements Cleanupable {
	private static final MenuController instance = new MenuController();
	//mutable
	private Context currentContext;
	private String appTitle;

	private MenuController() {
	}

	@Override
	public void cleanup() {
		CommandFactory.getInstance().cleanup();
	}

	public void clearAllData() {
		cleanup();
		PinModel.getInstance().cleanup();
	}

	public static MenuController getInstance() {
		return instance;
	}

	public Context getCurrentContext() {
		return currentContext;
	}

	public void setCurrentContext(Context currentContext) {
		this.currentContext = currentContext;
		appTitle = currentContext.getString(R.string.app_name);
	}

	public boolean performActionFor(int actionID) {
		final Command command = CommandFactory.getInstance().produceCommandForMenu(actionID);
		if (command != null) {
			return performActionForCommand(command);
		} else {
			return false;
		}
	}

	public String getAppTitle() {
		return appTitle;
	}

	private boolean performActionForCommand(final Command command) {
		command.executeWith(new ResultNotifier() {
			@Override
			public void completedWithSuccess(boolean success, final Object result) {
				if (success) {
					command.consumeResult(result);
					final Command nextCommand = CommandFactory.getInstance().produceCommandForType(command.getNextCommandType(), result);
					if (nextCommand != null) {
						new Handler().post(new Runnable() {
							@Override
							public void run() {
								performActionForCommand(nextCommand);
							}
						});
					}
				} else {
					InfoPopup.show(command.getErrorMessageWith(result), currentContext);
				}
			}
		});
		return true;
	}
}
