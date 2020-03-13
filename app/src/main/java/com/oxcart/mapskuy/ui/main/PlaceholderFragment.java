package com.oxcart.mapskuy.ui.main;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.oxcart.mapskuy.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

//import androidx.appcompat.widget.SearchView;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment implements OnMapReadyCallback {
    String url = "https://api.myjson.com/bins/zfvui";
    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private GoogleMap mMap;
    private FragmentActivity myContext;
    private SearchView searchView;
    private View mapView;
    private Double lat;
    private Double longs;
    private String city_name;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static PlaceholderFragment newInstance() {
        PlaceholderFragment fragment = new PlaceholderFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        if (getArguments().getInt(ARG_SECTION_NUMBER) == 1){
            View rootView = inflater.inflate(R.layout.activity_maps, container, false);
            SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            searchView = rootView.findViewById(R.id.location);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    String location = searchView.getQuery().toString();
                    List<Address> addressList = null;
                    if (location != null || !location.equals("")){
                        Geocoder geocoder = new Geocoder(getActivity());
                        try {
                            addressList = geocoder.getFromLocationName(location,1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Address address = addressList.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                    }
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
            return rootView;
        }
        else if(getArguments().getInt(ARG_SECTION_NUMBER) == 2){
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
        else {
            View rootView = inflater.inflate(R.layout.activity_maps, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            return rootView;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray kota = response.getJSONArray("kota");
                    for (int i = 0; i < kota.length() ; i++) {
                        JSONObject jsonObject = kota.getJSONObject(i);
                        lat = jsonObject.getDouble("lat");
                        longs = jsonObject.getDouble("long");
                        city_name = jsonObject.getString("nama_kota");

                        mMap.addMarker(new MarkerOptions().position(new LatLng(lat,longs)).title(city_name).snippet("Detailed Information Here")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, longs),9.0f));
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonObjectRequest);


        // Add a marker in Sydney and move the camera
//        LatLng lamonganIdn = new LatLng(-7.112340, 112.417419);
//        LatLng blitarIdn = new LatLng(-8.101460, 112.167679);
//        LatLng malangIdn = new LatLng(-7.966620, 112.632629);
//        mMap.addMarker(new MarkerOptions().position(malangIdn).title("Marker in Malang"));
//        mMap.addMarker(new MarkerOptions().position(blitarIdn).title("Marker in Blitar"));
//        mMap.addMarker(new MarkerOptions().position(lamonganIdn).title("Marker in Lamongan"));

//        mMap.moveCamera(CameraUpdateFactory.newLatLng(lamonganIdn));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(blitarIdn));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(malangIdn));

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    public void onAttach(Context context) {
        myContext = (FragmentActivity) context;
        super.onAttach(context);
    }
}