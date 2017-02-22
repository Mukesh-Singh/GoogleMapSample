package map.exaple.com.googlemapsample.rout;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by mukesh on 16/2/17.
 */

public class Util {
    /**
     *
     * @param origin
     * @param dest
     * @return url i.e. https://maps.googleapis.com/maps/api/directions/json?origin=28.5355,77.3910&destination=28.6289143,77.2065107&sensor=false
     */
    public static String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service

        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
    }
}
