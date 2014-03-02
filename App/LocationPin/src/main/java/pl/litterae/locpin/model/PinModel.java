package pl.litterae.locpin.model;

import android.graphics.Bitmap;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import pl.litterae.locpin.common.Cleanupable;

public final class PinModel implements Cleanupable {
	private static final PinModel instance = new PinModel();
	private final Set<Runnable> changeNotifiers = new HashSet<Runnable>();

	//mutable members
	private LatLng position;
	private String text;
	private Bitmap bitmap;

	private PinModel() {
	}

	@Override
	public void cleanup() {
		position = null;
		text = null;
		if (bitmap != null) {
			bitmap.recycle();
			bitmap = null;
		}
		changeNotifiers.clear();
	}

	public void registerNotifier(Runnable notifier) {
		changeNotifiers.add(notifier);
	}

	public void unregisterNotifier(Runnable notifier) {
		changeNotifiers.remove(notifier);
	}

	public static PinModel getInstance() {
		return instance;
	}

	public boolean isComplete() {
		return text != null && position != null && bitmap != null;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		notifyChange();
	}

	public LatLng getPosition() {
		return position;
	}

	public void setPosition(LatLng position) {
		this.position = position;
		notifyChange();
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
		notifyChange();
	}

	private void notifyChange() {
		for (Runnable notifier : changeNotifiers) {
			notifier.run();
		}
	}

	public Location getLocation() {
		if (isComplete()) {
			Location location = new Location(text);
			location.setLatitude(position.latitude);
			location.setLongitude(position.longitude);
			location.setTime(new Date().getTime());
			return location;
		}
		return null;
	}
}
