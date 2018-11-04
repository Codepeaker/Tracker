package com.codepeaker.hora.activities;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.codepeaker.hora.R;
import com.codepeaker.hora.databinding.ActivityParentBinding;
import com.codepeaker.hora.databinding.ContentMainBinding;
import com.codepeaker.hora.model.ChildModel;
import com.codepeaker.hora.model.ParentModel;
import com.codepeaker.hora.utils.AppUtils;
import com.codepeaker.hora.viewmodel.ParentViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class ParentActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ParentModel parentModel;
    ActivityParentBinding binding;
    ContentMainBinding contentMainBinding;
    public static final String TAG = ParentActivity.class.getSimpleName();
    private ParentViewModel parentViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_parent);
        setSupportActionBar(binding.toolbar);
        Bundle bundle = getIntent().getExtras();
        if (bundle.get("parent") != null) {
            parentModel = (ParentModel) bundle.get("parent");
        }

        contentMainBinding = binding.contentMainId;

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        parentViewModel = ViewModelProviders.of(this).get(ParentViewModel.class);


        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = AppUtils.getInstance().getDialog(ParentActivity.this
                        , R.layout.add_child_layout);
                final TextView textView = dialog.findViewById(R.id.child_username_tv);
                dialog.findViewById(R.id.add_child_bt).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String childUsername = textView.getText().toString();
                        if (childUsername.isEmpty()) {
                            Toast.makeText(ParentActivity.this
                                    , "Please enter Child Username", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        dialog.dismiss();
                        parentViewModel.addChildForParent(parentModel, childUsername);
                    }
                });
                dialog.show();
            }
        });

    }

    private void observe() {
        parentViewModel.getChildLiveData().observe(this, new Observer<List<ChildModel>>() {
            @Override
            public void onChanged(@Nullable List<ChildModel> childModels) {
                if (childModels != null && childModels.size() != 0) {
                    mMap.clear();
                    ArrayList<LatLng> latLngs = new ArrayList<>();
                    for (ChildModel childModel : childModels) {
                        String location = childModel.getLocation();
                        String lastUpdated = childModel.getLastUpdated();
                        String[] latLongString = location.split(",");
                        LatLng latLng = new LatLng(Double.parseDouble(latLongString[0])
                                , Double.parseDouble(latLongString[1]));
                        latLngs.add(latLng);
                        //add on Maps
                        addMarker(latLng, lastUpdated);
                    }
                    AppUtils.getInstance().setCameraPosition(mMap, latLngs);
                } else {
                    Toast.makeText(ParentActivity.this, "No Child Found, Please add one"
                            , Toast.LENGTH_LONG).show();
                }
                AppUtils.getInstance().hidepDialog();
            }
        });
    }


    private void addMarker(LatLng latLng, String lastUpdated) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(lastUpdated);
        markerOptions.snippet("Last Updated");
        if (mMap != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
            mMap.addMarker(markerOptions);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng india = new LatLng(22, 77);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(india, 9));

        //Load Children
        AppUtils.getInstance().showpDialog(this);
        parentViewModel.loadChildrenLocation(parentModel.getEmail());
        observe();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            mMap.clear();
            AppUtils.getInstance().showpDialog(this);
            parentViewModel.loadChildrenLocation(parentModel.getEmail());
        }
        return true;
    }
}
