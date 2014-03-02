package pl.litterae.locpin.app;

import android.app.Activity;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import pl.litterae.locpin.R;
import pl.litterae.locpin.controller.CurrentLocationController;
import pl.litterae.locpin.controller.MenuController;
import pl.litterae.locpin.controller.UseCase;
import pl.litterae.locpin.model.CurrentLocationInfo;
import pl.litterae.locpin.model.PinModel;
import pl.litterae.locpin.util.Internet;
import pl.litterae.locpin.view.component.CurrentLocationView;

public final class MainActivity extends Activity implements LocationListener {
	private GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Internet.Manager.getInstance().initialize(this);
		MenuController.getInstance().setCurrentContext(this);
		CurrentLocationController.getInstance().initWith(this);

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
			@Override
			public View getInfoWindow(Marker marker) {
				return null;
			}

			@Override
			public View getInfoContents(Marker marker) {
				View view = getLayoutInflater().inflate(R.layout.bubble, null);
				PinModel pinModel = PinModel.getInstance();
				String text;
				Bitmap bitmap;
				if (marker.getTitle().equalsIgnoreCase(UseCase.START_WITH_SERVER.getLabel())) {
					text = pinModel.getText();
					bitmap = pinModel.getBitmap();
				} else {
					text = marker.getTitle();
					bitmap = null;
				}

				TextView textView = (TextView) view.findViewById(R.id.bubble_text);
				textView.setText(text);

				ImageView imageView = (ImageView) view.findViewById(R.id.bubble_image);
				CurrentLocationView currentLocationView = (CurrentLocationView) view.findViewById(R.id.bubble_info);

				if (bitmap != null) {
					imageView.setImageBitmap(bitmap);
					currentLocationView.setVisibility(View.GONE);
				} else {
					imageView.setVisibility(View.GONE);
					Location location = CurrentLocationController.getInstance().readLocation();
					Location pinLocation = PinModel.getInstance().getLocation();
					currentLocationView.setInfo(new CurrentLocationInfo(location, pinLocation));
				}

				return view;
			}
		});

		LatLng iAmHere = CurrentLocationController.getInstance().readPosition();
		adjustToPosition(iAmHere, false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (MenuController.getInstance().performActionFor(item.getItemId())) {
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	private final Runnable pinChangeNotifier = new Runnable() {
		@Override
		public void run() {
			PinModel pinModel = PinModel.getInstance();
			if (pinModel.isComplete()) {
				LatLng position = pinModel.getPosition();
				String text = pinModel.getText();
				map.addMarker(new MarkerOptions().position(position).title(UseCase.START_WITH_SERVER.getLabel()).snippet(text));
				moveTo(position);
			}
		}
	};

	private final CurrentLocationController.Notifier currentLocationNotifier = new CurrentLocationController.Notifier() {
		@Override
		public void obtainedLocation(Location location) {
			adjustToPosition(new LatLng(location.getLatitude(), location.getLongitude()), true);
		}
	};

	private void adjustToPosition(LatLng position, boolean withMarker) {
		if (position != null) {
			if (withMarker) {
				map.addMarker(new MarkerOptions().position(position).title(UseCase.LOCATE_AND_MEASURE.getLabel()));
			}
			moveTo(position);
		}
	}

	private void moveTo(LatLng position) {
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
		map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
	}

	@Override
	protected void onResume() {
		super.onResume();
		PinModel.getInstance().registerNotifier(pinChangeNotifier);
		CurrentLocationController.getInstance().registerNotifier(currentLocationNotifier);
		CurrentLocationController.getInstance().resume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		MenuController.getInstance().cleanup();
		CurrentLocationController.getInstance().unregisterNotifier(currentLocationNotifier);
		CurrentLocationController.getInstance().pause();
		PinModel.getInstance().unregisterNotifier(pinChangeNotifier);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		MenuController.getInstance().clearAllData();
		CurrentLocationController.getInstance().cleanup();
	}

	//LocationListener implementation

	@Override
	public void onLocationChanged(Location location) {
		//TODO: change in case of more functionality...
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
