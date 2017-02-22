package map.exaple.com.googlemapsample;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import map.exaple.com.googlemapsample.geocoding.GeoCodingActivity;
import map.exaple.com.googlemapsample.geocoding.GeoCodingUtil;
import map.exaple.com.googlemapsample.rout.RoutActivity;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback ,GoogleMap.InfoWindowAdapter,GoogleMap.OnInfoWindowClickListener ,PlaceSelectionListener{

    private static final String TAG = "MapActivity";
    private static final int REQUEST_SELECT_PLACE = 1002;
    private GoogleMap mMap;
    private LatLng noida=new LatLng(28.5355,77.3910);
    private Marker mMarker;
    private CheckBox mCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCheckBox=(CheckBox)findViewById(R.id.checkBox);
        mCheckBox.setOnCheckedChangeListener(getCheckChangeListener());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(this);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            // Method #3
            try {
                Intent intent = new PlaceAutocomplete.IntentBuilder
                        (PlaceAutocomplete.MODE_FULLSCREEN)
                        //.setBoundsBias(BOUNDS_MOUNTAIN_VIEW)
                        .build(MapActivity.this);
                startActivityForResult(intent, REQUEST_SELECT_PLACE);
            } catch (GooglePlayServicesRepairableException |
                    GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_PLACE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                this.onPlaceSelected(place);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                this.onError(status);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this,"Please provide permission for Location",Toast.LENGTH_SHORT).show();
        }
        else {
            mMap.setMyLocationEnabled(true);

        }
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //mMap.setInfoWindowAdapter(this);
        //mMap.setOnInfoWindowClickListener(this);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(noida, 13));

//        mMap.addMarker(new MarkerOptions()
//                .title("Noida")
//                .snippet("It is part of National Capital Region of India")
//                .position(noida));

        mMarker =mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.img))
                .anchor(0.5f, 0.5f) // Anchors the mMarker on the bottom left
                .position(noida));


        //load traffic status on map


        //Map click listener

        mMap.setOnMapClickListener(getMapClickListener());


    }

    private GoogleMap.OnMapClickListener getMapClickListener() {
        return new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.addMarker(new MarkerOptions()
                        .title("Added on Click")
                        .snippet("Address: "+ GeoCodingUtil.getAddress(MapActivity.this,latLng))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.img))
                        .anchor(0.5f, 0.5f) // Anchors the mMarker on the bottom left
                        .position(latLng));

            }
        };
    }


    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
       return getInfoWindowView(marker);
    }

    private View getInfoWindowView(Marker marker){
        final View view =getLayoutInflater().inflate(R.layout.info_window_layout,null);
        final TextView title= (TextView) view.findViewById(R.id.title_text);
        TextView subtitle=(TextView) view.findViewById(R.id.subTitle_text);
        TextView desc=(TextView) view.findViewById(R.id.description_text);
        title.setText("Noida");
        subtitle.setText("Lat Long of this place is : "+noida.latitude+", "+noida.longitude);
        desc.setText("Noida, short for the New Okhla Industrial Development Authority, is a systematically planned Indian city under the management of the New Okhla Industrial Development Authority. It is part of National Capital Region of India");

       return view;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(getBaseContext(),
                "Info Window clicked@" + marker.getId(),
                Toast.LENGTH_SHORT).show();

    }

    private CompoundButton.OnCheckedChangeListener getCheckChangeListener() {
        return  new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mMap != null) {
                    mMap.setTrafficEnabled(isChecked);
                }

            }
        };
    }


    @Override
    public void onPlaceSelected(Place place) {
        if (mMarker !=null){

            mMarker.setPosition(place.getLatLng());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mMarker.getPosition(), 13));

        }
        Toast.makeText(this, "Selected Place: " + place.getName(),Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onError(Status status) {
        Toast.makeText(this, "Error Code: "+status.getStatusMessage() ,Toast.LENGTH_SHORT).show();

    }

    public void goToRoutActivity(View view) {
        startActivity(new Intent(this,RoutActivity.class));
    }

    public void goToGeoCoding(View view) {
        startActivity(new Intent(this, GeoCodingActivity.class));
    }
}
