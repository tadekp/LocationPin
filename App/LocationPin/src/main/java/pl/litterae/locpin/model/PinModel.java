package pl.litterae.locpin.model;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashSet;
import java.util.Set;

import pl.litterae.locpin.common.Cleanupable;

public final class PinModel implements Cleanupable {
	private static final PinModel instance = new PinModel();
	private final Set<Runnable> changeNotifiers = new HashSet<Runnable>();

	//mutable members
	private LatLng location;
	private String text;
	private Bitmap bitmap;

	private PinModel() {
	}

	@Override
	public void cleanup() {
		location = null;
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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		notifyChange();
	}

	public LatLng getLocation() {
		return location;
	}

	public void setLocation(LatLng location) {
		this.location = location;
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
}
