package com.oxcart.mapskuy.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.oxcart.mapskuy.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment implements OnMapReadyCallback {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private GoogleMap mMap;
    private FragmentActivity myContext;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
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

    public static PlaceholderFragment newInstance() {
        PlaceholderFragment fragment = new PlaceholderFragment();
        return fragment;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (getArguments().getInt(ARG_SECTION_NUMBER) == 1){
            View rootView = inflater.inflate(R.layout.activity_maps, container, false);
//            FragmentManager fragmentManager = myContext.getSupportFragmentManager();

            SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            return rootView;
        }
        else if(getArguments().getInt(ARG_SECTION_NUMBER) == 2){
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
        else {
            View rootView = inflater.inflate(R.layout.activity_maps, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng malangIdn = new LatLng(-7.966620, 112.632629);
        LatLng blitarIdn = new LatLng(-8.101460, 112.167679);
        LatLng lamonganIdn = new LatLng(-7.112340, 112.417419);
        mMap.addMarker(new MarkerOptions().position(malangIdn).title("Marker in Malang"));
        mMap.addMarker(new MarkerOptions().position(blitarIdn).title("Marker in Blitar"));
        mMap.addMarker(new MarkerOptions().position(lamonganIdn).title("Marker in Lamongan"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(malangIdn));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(blitarIdn));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(lamonganIdn));
    }

    @Override
    public void onAttach(Context context) {
        myContext = (FragmentActivity) context;
        super.onAttach(context);
    }
}