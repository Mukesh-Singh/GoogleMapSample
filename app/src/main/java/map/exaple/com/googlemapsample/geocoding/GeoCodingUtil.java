package map.exaple.com.googlemapsample.geocoding;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by mukesh on 16/2/17.
 */

public class GeoCodingUtil {

    public static List<String> getAddress(Context context,LatLng latLng){
        List<String> addressString=new ArrayList<>();
        Geocoder geocoder= new Geocoder(context, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            for (Address addres: addresses) {
                String address = addres.getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addres.getLocality();
                String state = addres.getAdminArea();
                String country = addres.getCountryName();
                String postalCode = addres.getPostalCode();
                String knownName = addres.getFeatureName(); // Only if available else return NULL
                String completeAddress=address+", "+city+", "+", "+state+", "+country+", "+postalCode+", "+knownName;
                addressString.add(completeAddress);

            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return addressString;
    }



    public static LatLng getLocationFromAddress(Context context,String strAddress){

        Geocoder coder = new Geocoder(context,Locale.getDefault());
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress,5);
            if (address==null) {
                return null;
            }
            Address location=address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng((location.getLatitude() ),
                     (location.getLongitude()));

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return p1;
    }


}
