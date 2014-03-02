package pl.litterae.locpin.controller;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashSet;
import java.util.Set;

import pl.litterae.locpin.common.Cleanupable;

public final class CurrentLocationController implements Cleanupable, LocationListener {
	public static interface Notifier {
		void obtainedLocation(Location location);
	}

	private static final CurrentLocationController instance = new CurrentLocationController();

	private final Set<Notifier> notifiers = new HashSet<Notifier>();
	//mutable
	private LocationManager manager;
	private String provider;

	private CurrentLocationController() {
	}

	public static CurrentLocationController getInstance() {
		return instance;
	}

	@Override
	public void cleanup() {
		manager = null;
		provider = null;
		notifiers.clear();
	}

	public void registerNotifier(Notifier notifier) {
		notifiers.add(notifier);
	}

	public void unregisterNotifier(Notifier notifier) {
		notifiers.add(notifier);
	}

	public void broadcastObtainedLocation(final Location location) {
		Handler handler = new Handler();
		for (final Notifier notifier : notifiers) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					notifier.obtainedLocation(location);
				}
			});
		}
	}

	public void initWith(Context context) {
		manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		provider = manager.getBestProvider(criteria, false);
	}

	public void resume() {
		manager.requestLocationUpdates(provider, 400, 1, this);
	}

	public void pause() {
		manager.removeUpdates(this);
	}

	public Location readLocation() {
		return manager.getLastKnownLocation(provider);
	}

	public LatLng readPosition() {
		Location location = readLocation();

		if (location != null) {
			//System.out.println("Provider " + locationProvider + " has been selected.");
			onLocationChanged(location);
			return new LatLng(location.getLatitude(), location.getLongitude());
		} else {
			Log.i("Location Pin", "Location non available");
			return null;
		}
	}

	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}
}
