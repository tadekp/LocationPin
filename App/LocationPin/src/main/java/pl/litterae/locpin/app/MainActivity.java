package pl.litterae.locpin.app;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import pl.litterae.locpin.R;
import pl.litterae.locpin.controller.MenuController;
import pl.litterae.locpin.model.PinModel;

public final class MainActivity extends Activity {
	static ImageView testImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment())
					.commit();
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		if (MenuController.getInstance().performActionFor(item.getItemId())) {
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	private final Runnable pinChangeNotifier = new Runnable() {
		@Override
		public void run() {
			if (testImageView != null) {
				Bitmap bitmap = PinModel.getInstance().getBitmap();
				if (bitmap != null) {
					testImageView.setImageBitmap(bitmap);
				}
			}
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		PinModel.getInstance().registerNotifier(pinChangeNotifier);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MenuController.getInstance().cleanup();
		PinModel.getInstance().unregisterNotifier(pinChangeNotifier);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		MenuController.getInstance().clearAllData();
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
		                         Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container, false);
			testImageView = (ImageView) rootView.findViewById(R.id.main_test_image);
			return rootView;
		}
	}

}
