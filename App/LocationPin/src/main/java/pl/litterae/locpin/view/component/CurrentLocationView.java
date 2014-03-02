package pl.litterae.locpin.view.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.GridLayout;
import android.widget.TextView;

import pl.litterae.locpin.R;
import pl.litterae.locpin.model.CurrentLocationInfo;

public final class CurrentLocationView extends GridLayout {
	private static final String GPS_COORDINATE_FORMAT = "%1$,.8f";
	private static final String KM_FORMAT = "%1$,.3f";

	private TextView latitudeView;
	private TextView longitudeView;
	private TextView distanceView;
	private CurrentLocationInfo info;

	public CurrentLocationView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CurrentLocationView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CurrentLocationView(Context context) {
		super(context);
	}

	public void setInfo(CurrentLocationInfo info) {
		this.info = info;
		if (info != null) {
			if (latitudeView != null) {
				latitudeView.setText(String.format(GPS_COORDINATE_FORMAT, info.getLocation().getLatitude()));
			}
			if (longitudeView != null) {
				longitudeView.setText(String.format(GPS_COORDINATE_FORMAT, info.getLocation().getLongitude()));
			}
			if (distanceView != null) {
				distanceView.setText(String.format(KM_FORMAT, info.getDistance() / 1000));
			}
		}
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		LayoutInflater li = LayoutInflater.from(getContext());
		li.inflate(R.layout.component_current_location, this);
		latitudeView = (TextView) findViewById(R.id.curloc_latitude_value);
		longitudeView = (TextView) findViewById(R.id.curloc_longitude_value);
		distanceView = (TextView) findViewById(R.id.curloc_distance_value);
		setInfo(info);
	}
}
