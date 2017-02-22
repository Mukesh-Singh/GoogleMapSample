package map.exaple.com.googlemapsample.geocoding;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import map.exaple.com.googlemapsample.R;

public class GeoCodingActivity extends AppCompatActivity {
    private EditText mAddress,mLatLng;
    private TextView mResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_coding);
        mAddress=(EditText)findViewById(R.id.address_edit_text);
        mLatLng=(EditText)findViewById(R.id.lat_long_edit_text);
        mResult=(TextView)findViewById(R.id.resultText);
    }

    public void getAddress(View view) {
        if (!mLatLng.getText().toString().isEmpty() && mLatLng.getText().toString().contains(",")){
            String[] latlng=mLatLng.getText().toString().split(",");
            List<String> address= GeoCodingUtil.getAddress(this,new LatLng(Double.parseDouble(latlng[0]),Double.parseDouble(latlng[1])));

            String allAddress="";
            for (String add :address) {
                allAddress=allAddress+"\n"+add;

            }
            if (!allAddress.isEmpty()) {
                mResult.setText(allAddress);
            }
            else {
                mResult.setText("Address Not Found");
            }

        }

    }

    public void getLatLong(View view) {
        if (!mAddress.getText().toString().isEmpty()){
            LatLng latLng=GeoCodingUtil.getLocationFromAddress(this,mAddress.getText().toString());
            if (latLng!=null) {
                String temp = "Latitude: " + latLng.latitude + "\nLongitude : " + latLng.longitude;
                mResult.setText(temp);
            }
            else {
                mResult.setText("LatLng not found");
            }
        }
    }
}
