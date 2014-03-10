package net.wespot.pim.view;

/**
 * ****************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * <p/>
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Contributors: Angel Suarez
 * ****************************************************************************
 */

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.*;
import android.widget.*;
import net.wespot.pim.R;
import net.wespot.pim.utils.LocationUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import org.celstec.arlearn.delegators.INQ;
import org.celstec.arlearn2.android.delegators.ARL;
import org.celstec.arlearn2.client.InquiryClient;
import org.celstec.dao.gen.InquiryLocalObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * A dummy fragment representing a section of the app, but that simply displays dummy text.
 */
public class InqWonderMomentFragment extends Fragment implements LocationListener, GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener{

    public static final String INQUIRY_ID = "object";

    private static final String TAG = "InqWonderMomentFragment";
    private static final int REQUEST_CODE = 1234;
    private static final int RESULT_OK = -1;

    private InquiryLocalObject new_inquiry;

    private EditText wm_title;
    private EditText wm_content;

    private EditText wm_date;
    private EditText wm_time;
    private ImageView wm_clear;
    public EditText wm_location;
    // A request to connect to Location Services
    private LocationRequest mLocationRequest;

    // Stores the current instantiation of the location client in this object
    private LocationClient mLocationClient;
    // Note if updates have been turned on. Starts out as "false"; is set to "true" in the
    // method handleRequestSuccess of LocationUpdateReceiver.
    boolean mUpdatesRequested = false;
    public ProgressBar wm_progress_bar;



    private class CreateInquiryObject {
        public InquiryLocalObject inquiry;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_wondermoment, container, false);

        wm_title = (EditText) rootView.findViewById(R.id.wonder_moment_title);
        wm_content = (EditText) rootView.findViewById(R.id.wonder_moment_content);

        wm_date = (EditText) rootView.findViewById(R.id.wonder_moment_date);
        wm_time = (EditText) rootView.findViewById(R.id.wonder_moment_time);
        wm_location = (EditText) rootView.findViewById(R.id.wonder_moment_location);
        wm_clear = (ImageView) rootView.findViewById(R.id.wonder_moment_clear);
        wm_progress_bar = (ProgressBar) rootView.findViewById(R.id.wonder_moment_progress_location);

        setDataTime();

        wm_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                wm_content.setText("");

                Toast.makeText(getActivity(), "New inquiry initialized", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH.toString());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "weSPOT voice recognition...");
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        // Disable button if no recognition service is present
        PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0) {
            Toast.makeText(getActivity(), "Recognizer Not Found", 1000).show();
        }

        setManagerLocation();

        // Create inquiry
        new_inquiry = new InquiryLocalObject();


        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save_inquiry:
                // TODO put here the method to create inquiry

                new_inquiry.setDescription(wm_content.getText().toString());
                new_inquiry.setTitle(wm_title.getText().toString());


                if (!new_inquiry.getTitle().equals("")) {
                    if (!new_inquiry.getDescription().equals("")) {
                        if (INQ.isOnline()) {
                            CreateInquiryObject createInquiryObject = new CreateInquiryObject();
                            createInquiryObject.inquiry = new_inquiry;
                            Toast.makeText(getActivity(), "Synchronizing & saving inquiry...", 1000).show();

                            ARL.eventBus.register(this);
                            // To invoke onEventBackgroundThread this line is needed
                            ARL.eventBus.post(createInquiryObject);

                        } else {
                            Toast.makeText(getActivity(), "Enable internet connection", 1000).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Provide a proper description for the inquiry", 1000).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Provide a proper title for the inquiry", 1000).show();
                }


                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onEventBackgroundThread(CreateInquiryObject inquiryObject){
        InquiryClient.getInquiryClient().createInquiry(inquiryObject.inquiry, INQ.accounts.getLoggedInAccount());
        INQ.inquiry.syncInquiries();
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        getActivity().finish();
    }

    public void onDestroy(){
        super.onDestroy();
        ARL.eventBus.unregister(this);
    }

    private void setManagerLocation() {
        wm_location.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        Log.e(TAG, "Click to start to find location");
                        startFindingLocation();
                        break;
                }
                return false;
            }
        });

        // Retriving location

        // Create a new global location parameters object
        mLocationRequest = LocationRequest.create();

        /*
         * Set the update interval
         */
        mLocationRequest.setInterval(LocationUtils.UPDATE_INTERVAL_IN_MILLISECONDS);

        // Use high accuracy
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Set the interval ceiling to one minute
        mLocationRequest.setFastestInterval(LocationUtils.FAST_INTERVAL_CEILING_IN_MILLISECONDS);

        // Note that location updates are off until the user turns them on
        mUpdatesRequested = false;

        mLocationClient = new LocationClient(getActivity(), this, this);

        Log.e(TAG, "Start to find location");
    }

    private void setDataTime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM d, yyyy");

        String test = sdf.format(cal.getTime());
        wm_date.setText(test);

        sdf = new SimpleDateFormat("HH:mm");
        test = sdf.format(cal.getTime());
        wm_time.setText(test);

        wm_date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        Log.e(TAG, "toco");
                        DialogFragment dateFrag = new DatePickerFragment();
//                            dateFrag.show(getSupportFragmentManager(), "timePicker");
                        dateFrag.show(getChildFragmentManager(), "timePicker");
                        break;
                }
                return false;
            }
        });

        wm_time.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        Log.e(TAG, "toco");
                        DialogFragment timeFrag = new TimePickerFragment();
                        timeFrag.show(getChildFragmentManager(), "timePicker");
//                            timeFrag.show(getSupportFragmentManager(), "timePicker");
                        break;
                }
                return false;
            }
        });
    }

    public void startFindingLocation() {

        // In Gingerbread and later, use Geocoder.isPresent() to see if a geocoder is available.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && !Geocoder.isPresent()) {
            // No geocoder is present. Issue an error message
            Toast.makeText(getActivity(), R.string.wonder_moment_no_geocoder_available, Toast.LENGTH_LONG).show();
            return;
        }

        if (servicesConnected()) {

            // Get the current location
            Location currentLocation = mLocationClient.getLastLocation();

            // Turn the indefinite activity indicator on
            wm_progress_bar.setVisibility(View.VISIBLE);

            // Start the background task
            (new GetAddressTask(getActivity())).execute(currentLocation);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_inquiry, menu);

        menu.setGroupVisible(R.id.actions_general, false);
        menu.setGroupVisible(R.id.actions_wonder_moment, true);
        super.onCreateOptionsMenu(menu, inflater);
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            Log.e(TAG, matches.toString());
//                 resultList.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, matches));
            wm_content.setText((wm_content.getText().equals(null)? matches.get(0) : wm_content.getText()+" "+matches.get(0)));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onLocationChanged(Location location) {
        // Report to the UI that the location was updated
//            mConnectionStatus.setText(R.string.location_updated);

        // In the UI, set the latitude and longitude to the value received
//            wm_location.setText(LocationUtils.getLatLng(getActivity(), location));
    }

    @Override
    public void onStart() {
        super.onStart();
            /*
            * Connect the client. Don't re-start any requests here;
            * instead, wait for onResume()
            */
        mLocationClient.connect();

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onConnected(Bundle bundle) {
        if (mUpdatesRequested) {
            startPeriodicUpdates();
        }
    }

    private void startPeriodicUpdates() {
        mLocationClient.requestLocationUpdates(mLocationRequest, (com.google.android.gms.location.LocationListener) getActivity());
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private boolean servicesConnected() {

        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());

        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d(LocationUtils.APPTAG, getString(R.string.wonder_moment_play_services_available));

            // Continue
            return true;
            // Google Play services was not available for some reason
        } else {
            // Display an error dialog
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(), 0);
//                if (dialog != null) {
//                    ErrorDialogFragment errorFragment = new ErrorDialogFragment();
//                    errorFragment.setDialog(dialog);
//                    errorFragment.show(getSupportFragmentManager(), LocationUtils.APPTAG);
//                }
            return false;
        }
    }

    public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        final Calendar c =  Calendar.getInstance();
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            String test = sdf.format(c.getTime());

            c.set(0,0,0,hourOfDay,minute);

            wm_time.setText(sdf.format(c.getTime()));
        }

    }
    public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        final Calendar c = Calendar.getInstance();
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }
        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM d, yyyy");
            String test = sdf.format(c.getTime());

            c.set(year,month,day);

            wm_date.setText(sdf.format(c.getTime()));
        }

    }
    protected class GetAddressTask extends AsyncTask<Location, Void, String> {


        // Store the context passed to the AsyncTask when the system instantiates it.
        Context localContext;

        // Constructor called by the system to instantiate the task
        public GetAddressTask(Context context) {

            // Required by the semantics of AsyncTask
            super();

            // Set a Context for the background task
            localContext = context;
        }

        /**
         * Get a geocoding service instance, pass latitude and longitude to it, format the returned
         * address, and return the address to the UI thread.
         */
        @Override
        protected String doInBackground(Location... params) {
            /*
             * Get a new geocoding service instance, set for localized addresses. This example uses
             * android.location.Geocoder, but other geocoders that conform to address standards
             * can also be used.
             */
            Geocoder geocoder = new Geocoder(localContext, Locale.getDefault());

            // Get the current location from the input parameter list
            Location location = params[0];

            // Create a list to contain the result address
            List <Address> addresses = null;

            // Try to get an address for the current location. Catch IO or network problems.
            try {

                /*
                 * Call the synchronous getFromLocation() method with the latitude and
                 * longitude of the current location. Return at most 1 address.
                 */
                addresses = geocoder.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1
                );

                // Catch network or other I/O problems.
            } catch (IOException exception1) {

                // Log an error and return an error message
                Log.e(LocationUtils.APPTAG, String.valueOf(R.string.wonder_moment_IO_Exception_getFromLocation));

                // print the stack trace
                exception1.printStackTrace();

                // Return an error message
                return String.valueOf((R.string.wonder_moment_IO_Exception_getFromLocation));

                // Catch incorrect latitude or longitude values
            } catch (IllegalArgumentException exception2) {

                // Construct a message containing the invalid arguments
                String errorString = localContext.getString(
                        R.string.wonder_moment_illegal_argument_exception,
                        location.getLatitude(),
                        location.getLongitude()
                );
                // Log the error and print the stack trace
                Log.e(LocationUtils.APPTAG, errorString);
                exception2.printStackTrace();

                //
                return errorString;
            }
            // If the reverse geocode returned an address
            if (addresses != null && addresses.size() > 0) {

                // Get the first address
                Address address = addresses.get(0);

                // Format the first line of address
                String addressText = localContext.getString(R.string.wonder_moment_address_output_string,

                        // If there's a street address, add it
                        address.getMaxAddressLineIndex() > 0 ?
                                address.getAddressLine(0) : "",

                        // Locality is usually a city
                        address.getLocality(),

                        // The country of the address
                        address.getCountryName()
                );

                // Return the text
                return addressText;

                // If there aren't any addresses, post a message
            } else {
                return localContext.getString(R.string.wonder_moment_no_address_found);
            }
        }

        /**
         * A method that's called once doInBackground() completes. Set the text of the
         * UI element that displays the address. This method runs on the UI thread.
         */
        @Override
        protected void onPostExecute(String address) {

            // Turn off the progress bar
            wm_progress_bar.setVisibility(View.GONE);

            // Set the address in the UI
            wm_location.setText(address);
        }

    }
}


