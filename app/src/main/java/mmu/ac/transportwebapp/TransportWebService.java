package mmu.ac.transportwebapp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class TransportWebService {

    /**
     * This accepts a lat and lng, creates the necessary URL and passes it to the getStationsFromURL function
     * @param Latitude
     * @param Longitude
     * @return ArrayList<Station>
     */
    public ArrayList<Station> getStationsFromLocation(double Latitude, double Longitude) {
        try {
            // Create the url object with the Url of the webserver and pass the variables with it
            URL url = new URL("http://10.0.2.2:8080/stations?lat=" + Latitude + "&lng=" + Longitude);
            return getStationsFromURL(url);
        } catch (Exception e) {
            return null;
        }
    }

    //This functions accepts a Latitude and Longitude and returns a array of stations that are close by.
    public ArrayList<Station> getStationsFromURL(URL url) {
        ArrayList<Station> Stations = new ArrayList<>();
        try {
            URLConnection connection = url.openConnection();

            //Create a new reader to read the output from the URL we just made
            InputStreamReader ins = new InputStreamReader(connection.getInputStream());
            BufferedReader in = new BufferedReader(ins);

            String responseBody = "";
            String line = "";

            // Store each line of the response into the responseBody ready for parsing
            while((line = in.readLine()) != null) {
                responseBody = responseBody + line;
            }

            // Parse the response text to a JSON array of objects
            JSONArray returned_JSONarray_of_stations = new JSONArray(responseBody);

            /**
             * For each JSONObject in the array:
             * I create a new Station object,
             * Set the stationName, Latitude and Longitude
             * then add the station to the arrayList of Stations ready for the return
             */
            for(int i = 0; i < returned_JSONarray_of_stations.length(); i++) {
                Station newStation = new Station();
                JSONObject stationObject = returned_JSONarray_of_stations.getJSONObject(i);
                System.out.println(stationObject.getString("StationName"));
                newStation.setStationName(stationObject.getString("StationName"));
                newStation.setLatitude(stationObject.getDouble("Latitude"));
                newStation.setLongitude(stationObject.getDouble("Longitude"));
                Stations.add(newStation);
            }

        } catch (Exception e) {

        }
        return Stations;
    }

}
