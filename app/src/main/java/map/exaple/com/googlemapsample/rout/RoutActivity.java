package map.exaple.com.googlemapsample.rout;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import map.exaple.com.googlemapsample.R;

public class RoutActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng mSource=new LatLng(28.5355,77.3910);
    private LatLng mDestination=new LatLng(28.6289143,77.2065107);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rout);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setTrafficEnabled(true);

        mMap.addMarker(new MarkerOptions()
                .title("Source")
                .position(mSource));

        mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.img))
                .title("Destination")
                .anchor(0.5f, 0.5f) // Anchors the mMarker on the bottom left
                .position(mDestination));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mSource, 10));

        DownloadRoutListAsync downloadRoutListAsync = new DownloadRoutListAsync(getCallBackForRoutDownload());

        // Start downloading json data from Google Directions API
        downloadRoutListAsync.execute(Util.getDirectionsUrl(mSource,mDestination));
    }

    private Callback getCallBackForRoutDownload() {
        return new Callback() {
            @Override
            public void onSuccess(Object object) {
                ParseRoutDataAsync parserTask = new ParseRoutDataAsync(getCallBackRoutListParsed());
                String string= (String) object;
                // Invokes the thread for parsing the JSON data
                parserTask.execute(string);
            }

            @Override
            public void onFailure(String message) {

            }
        };
    }

    private Callback getCallBackRoutListParsed() {
        return new Callback() {
            @Override
            public void onSuccess(Object object) {
                PolylineOptions lineOptions=new PolylineOptions();
                lineOptions.addAll((ArrayList<LatLng>) object);
                lineOptions.width(10);
                lineOptions.color(Color.BLUE);
                mMap.addPolyline(lineOptions);
            }

            @Override
            public void onFailure(String message) {

            }
        };
    }


}
