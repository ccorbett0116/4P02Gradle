package org.example;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.ResponsePath;
import com.graphhopper.config.CHProfile;
import com.graphhopper.config.Profile;
import com.graphhopper.util.PointList;

public class RoutingExample {
    public static void main(String[] args) {
        // Initialize GraphHopper
        GraphHopper hopper = new GraphHopper();
        hopper.setOSMFile("ontario-latest.osm.pbf"); // You'll need to download this file
        hopper.setGraphHopperLocation("graph-cache");

        // Set up the routing profile
        hopper.setProfiles(new Profile("car").setVehicle("car").setWeighting("fastest"));
        hopper.getCHPreparationHandler().setCHProfiles(new CHProfile("car"));

        // Load the graph
        hopper.importOrLoad();

        // Toronto coordinates
        double fromLat = 43.651070;
        double fromLon = -79.347015;

        // Ottawa coordinates
        double toLat = 45.421532;
        double toLon = -75.697189;

        // Create a routing request
        GHRequest request = new GHRequest(fromLat, fromLon, toLat, toLon)
                .setProfile("car")
                .setLocale("en");

        // Calculate the route
        GHResponse response = hopper.route(request);

        // Check for errors
        if (response.hasErrors()) {
            System.out.println("Error: " + response.getErrors());
            return;
        }

        // Get the best path
        ResponsePath path = response.getBest();

        // Print route details
        System.out.println("Distance: " + path.getDistance() / 1000 + " km");
        System.out.println("Time: " + path.getTime() / 60000 + " minutes");

        // Print the points along the route
        PointList points = path.getPoints();
        System.out.println("\nRoute points:");
        for (int i = 0; i < points.size(); i++) {
            System.out.printf("Point %d: %.6f, %.6f%n",
                    i, points.getLat(i), points.getLon(i));
        }

        // Clean up
        hopper.close();
    }
}