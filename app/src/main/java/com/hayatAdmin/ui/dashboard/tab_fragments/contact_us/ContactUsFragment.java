package com.hayatAdmin.ui.dashboard.tab_fragments.contact_us;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hayatAdmin.BaseFragment;
import com.grocery.R;
import com.hayatAdmin.model.company_contact.CompanyContactRequest;
import com.hayatAdmin.model.company_contact.CompanyContactResponse;
import com.hayatAdmin.rest_client.BCRequests;
import com.hayatAdmin.utils.Const;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUsFragment extends BaseFragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {

    @BindView(R.id.contactUsLayout)
    LinearLayout contactUsLayout;

    @BindView(R.id.textViewCall)
    TextView textViewCall;

    @BindView(R.id.mapExpandCollapseButton)
    Button mapExpandCollapseButton;

    @BindView(R.id.map_relative_layout)
    MapWrapperLayout mapWrapperLayout;

    @BindView(R.id.progress_bar)
    FrameLayout progressBar;

    OnContactUsListener mCallback;
    SharedPreferences pref;
    MapView mMapView;
    private GoogleMap googleMap;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    private ViewGroup infoWindow;
    private TextView infoTitle, infoPhoneNumber, infoAddress;
    private Button infoButton1;
    private OnInfoWindowElemTouchListener infoButtonListener;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    String searchText = "";
    boolean isMapReady = false;
    private boolean isExpanded = false;
    String latitude;
    String longitude;
    String userId;
    String page;
    String perPage;
    String companyName, contactNumber, email, businessType, area, city, state, country, postalCode, companyDetails;
    Uri companyImageUrl;
    CompanyContactResponse companyContactResponse;

    public ContactUsFragment() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnContactUsListener) context;
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected View createView(final LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact_us, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
        mMapView.getMapAsync(mMap -> {
            googleMap = mMap;
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            buildGoogleApiClient();
            googleMap.setMyLocationEnabled(true);

            try {
                mapWrapperLayout.init(googleMap, getPixelsFromDp(getActivity(), 40 + 20));
                this.infoWindow = (ViewGroup) getLayoutInflater().inflate(R.layout.custom_marker_infowindow, null);
                this.infoTitle = (TextView) infoWindow.findViewById(R.id.nameTxt);
                this.infoPhoneNumber = (TextView) infoWindow.findViewById(R.id.mobileTxt);
                this.infoAddress = (TextView) infoWindow.findViewById(R.id.addressTxt);
                this.infoButton1 = (Button) infoWindow.findViewById(R.id.btnOne);
                this.infoButtonListener = new OnInfoWindowElemTouchListener(infoButton1, getResources().getDrawable(R.drawable.background_corner_radius_pink), getResources().getDrawable(R.drawable.background_corner_radius_pink)) {
                    @Override
                    protected void onClickConfirmed(View v, Marker marker) {
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://maps.google.com/maps?saddr=" + mLastLocation.getLatitude() + "," + mLastLocation.getLongitude()+ "&daddr=" + latitude + "," + longitude));
                        startActivity(intent);
                    }
                };
                this.infoButton1.setOnTouchListener(infoButtonListener);

                googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker marker) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {
                        String[] separated = marker.getTitle().split(":");
                        URL url = null;
//                        try {
//                            url = new URL(separated[2]);
//                            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                            clientPicImageView.setImageBitmap(bmp);
//                        } catch (Exception exception) {
//                            Log.e("Error ==> ", "" + exception);
//                        }
                        infoTitle.setText(separated[0]);
                        infoPhoneNumber.setText(separated[1]);
                        infoAddress.setText(marker.getSnippet());
                        infoButtonListener.setMarker(marker);
                        mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
                        return infoWindow;
                    }
                });
            } catch (Exception exception) {
                Log.e("Error ==> ", "" + exception);
            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            getCompanyContact();
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            getCompanyContact();
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    @OnClick(R.id.textViewCall)
    public void onCallButtonAction() {
        try {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "You need to allow call permission to make call from this app", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:+91 9585909514"));
            startActivity(intent);
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    @OnClick(R.id.textViewEmail)
    public void onEmailButtonAction() {
        try {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("abubasith86@gmail.com"));
            startActivity(Intent.createChooser(emailIntent, "Send feedback"));
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    @OnClick(R.id.mapExpandCollapseButton)
    public void onMapExpandCollapseButtonAction() {
        try {
            if (isExpanded) {
                isExpanded = false;
                contactUsLayout.setVisibility(View.VISIBLE);
                mapExpandCollapseButton.setBackground(getActivity().getDrawable(R.drawable.icon_arrow_up));
            } else {
                isExpanded = true;
                contactUsLayout.setVisibility(View.GONE);
                mapExpandCollapseButton.setBackground(getActivity().getDrawable(R.drawable.icon_arrow_down));
            }
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mLastLocation = location;
            googleMap.clear();
            latitude = "10.886209962715947";
            longitude = "79.6798827600631";
            LatLng storeLatLang = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(storeLatLang);
            markerOptions.title("Hayat store" + ":"
                    + "+91 9585909514" + ":"
                    + "");
            markerOptions.snippet("No. 1/107 Dulpakkir Nagar, Enangudi, Nagapattinam, India - 609701");
            markerOptions.icon(BitmapFromVector(getActivity(), R.drawable.icon_shopping));
//                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            googleMap.addMarker(markerOptions);
            //move map camera
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(9));
            googleMap.moveCamera(CameraUpdateFactory.zoomTo(10));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(storeLatLang));

            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses = null;
            String currentAddress = "Current Address";
            try {
                addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                Address address = addresses.get(0);
                ArrayList<String> addressFragments = new ArrayList<>();
                for (int i = 0; i <= address.getMaxAddressLineIndex() && i < 2; i++) {
                    addressFragments.add(address.getAddressLine(i));
                }
                currentAddress = TextUtils.join(Objects.requireNonNull(System.getProperty("line.separator")), addressFragments);
                if (addresses.size() > 0) {
                    Const.currentCity = addresses.get(0).getLocality();
                    Const.currentState = addresses.get(0).getAdminArea();
                    Const.currentCountry = addresses.get(0).getCountryName();
                    Const.selectedCity = Const.currentCountry + "," + Const.currentState + "," + Const.currentCity;

                }
            } catch (Exception exception) {
                Log.e("Error ==> ", "" + exception);
            }

            //stop location updates
            if (mGoogleApiClient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, locationListener);
            }
        }
    };

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, locationListener);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        isMapReady = true;
    }

    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void getCompanyContact() {
        try {
            userId = "1";
            page = "1";
            perPage = "10";
            CompanyContactRequest companyContactRequest = new CompanyContactRequest();
            companyContactRequest.setUserId(userId);
            companyContactRequest.setPage(page);
            companyContactRequest.setPerPage(perPage);
            Call<CompanyContactResponse> companyContactResponseCall = BCRequests.getInstance().getBCRestService().companyContact(companyContactRequest);
            companyContactResponseCall.enqueue(new Callback<CompanyContactResponse>() {
                @Override
                public void onResponse(Call<CompanyContactResponse> call, Response<CompanyContactResponse> response) {
                    try {
                        if (response.isSuccessful()) {
                            companyContactResponse = response.body();
                            if (response != null && companyContactResponse.getClients() != null) {
                                String status = companyContactResponse.getStatus();
                                String message = companyContactResponse.getMessage();
                                if (status.equals("success")) {
                                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                    companyName = companyContactResponse.getClients().get(0).getCompanyName();
                                    contactNumber = companyContactResponse.getClients().get(0).getContactNumber();
                                    email = companyContactResponse.getClients().get(0).getEmail();
                                    businessType = companyContactResponse.getClients().get(0).getBusinessType();
                                    area = companyContactResponse.getClients().get(0).getArea();
                                    city = companyContactResponse.getClients().get(0).getCity();
                                    state = companyContactResponse.getClients().get(0).getState();
                                    country = companyContactResponse.getClients().get(0).getCountry();
                                    postalCode = companyContactResponse.getClients().get(0).getPostalCode();
                                    latitude = companyContactResponse.getClients().get(0).getLatitude();
                                    longitude = companyContactResponse.getClients().get(0).getLongitude();
                                    companyImageUrl = (Uri) companyContactResponse.getClients().get(0).getCompanyImageUrl();
                                    companyDetails = companyContactResponse.getClients().get(0).getCompanyDetails();
                                } else {
                                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    } catch (Exception exception) {
                        Log.e("Error ==> ", "" + exception);
                    }
                }

                @Override
                public void onFailure(Call<CompanyContactResponse> call, Throwable t) {

                }
            });

        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }

    }

    public void showProgress() {
        try {
            progressBar.setVisibility(View.VISIBLE);
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    public void hideProgress() {
        try {
            progressBar.setVisibility(View.GONE);
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    public interface OnContactUsListener {
        void onShowDetailsView(String id);
    }
}
