package pl.litterae.locpin.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import java.util.HashSet;
import java.util.Set;

public interface Internet {
	static interface Notifier {
		void internetStateChanged(boolean connected);
	}

	static final class Manager {
		private static Manager instance = new Manager();
		private final Set<Notifier> notifiers = new HashSet<Notifier>();
		private boolean connected;

		private Manager() {
		}

		public static Manager getInstance() {
			return instance;
		}

		public void initialize(Context context) {
			connected = detectConnected(context);
		}

		public void registerNotifier(Notifier notifier) {
			notifiers.add(notifier);
		}

		public void unregisterNotifier(Notifier notifier) {
			if (notifiers.contains(notifier)) {
				notifiers.remove(notifier);
			}
		}

		public boolean detectConnected(Context context) {
			ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo[] infos = connectivityManager.getAllNetworkInfo();
			return detectConnected(infos);
		}

		private boolean detectConnected(NetworkInfo[] infos) {
			if (infos != null) {
				for (NetworkInfo info : infos) {
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
			return false;
		}

		public boolean isConnected() {
			return connected;
		}

		public boolean nonTrivialSetConnected(final boolean connected) {
			if (connected != this.connected) {
				this.connected = connected;
				new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... params) {
						for (Notifier notifier : notifiers) {
							notifier.internetStateChanged(connected);
						}
						return null;
					}
				}.execute();
				return true;
			} else {
				return false;
			}
		}
	}
}
