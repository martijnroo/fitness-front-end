package com.example.android.bluetoothlegatt;

import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecordFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecordFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private boolean recording = false;

    private FloatingActionButton button;
    private Chronometer timer;
    private TextView infoText;
    private final static String TAG = RecordFragment.class.getSimpleName();
    private BluetoothManager btManager;
    private BluetoothAdapter btAdapter;

    private MainActivity mActivity;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecordFragment newInstance(String param1, String param2) {
        RecordFragment fragment = new RecordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public RecordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View recordFragmentView = inflater.inflate(R.layout.fragment_record, container, false);

        button = (FloatingActionButton) recordFragmentView.findViewById(R.id.workout_record_fab);

        timer = (Chronometer) recordFragmentView.findViewById(R.id.workout_timer);
        infoText = (TextView) recordFragmentView.findViewById(R.id.text_intro);

        // When the button is clicked, start recording an exercise
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.v("BUTTON:", "PRESSED THE BUTTON");

                if (recording) {
                    button.setIcon(R.drawable.ic_action_new);

                    timer.stop();
                    // TODO: stop measuring the RR here!

                    long elapsed = SystemClock.elapsedRealtime() - timer.getBase();
                    long endTime = System.currentTimeMillis();
                    long startTime = endTime - elapsed;

                    // Format the strings according to the backend API specification
                    DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                    String formattedStart = formatter.format(new Date(startTime));
                    String formattedEnd = formatter.format(new Date(endTime));

                    // Create an intent, and send the data to ExerciseFormActivity
                    Intent intent = new Intent(getActivity(), ExerciseFormActivity.class);
                    intent.putExtra("timer_start", formattedStart);
                    intent.putExtra("timer_end", formattedEnd);

                    // TODO: Alex, send the list of recordings to the server here
                    // replace this with the recorded measurements
                    // NOTE: the measurement time needs to be saved and sent to the server somehow!

                    /*
                    ArrayList<Integer> rr_list = new ArrayList<Integer>();
                    rr_list.add(0);
                    rr_list.add(1);
                    rr_list.add(2);

                    ArrayList<Measurement> msr = new ArrayList<>(); // a list of measurement objects
                    for(int rr : rr_list) {
                        Measurement mObj = new Measurement();
                        mObj.user_id = 9;
                        mObj.rr_value = rr;
                        //mObj.timestamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
                        msr.add(mObj);
                    }

                    NetworkManager.getInstance().sendMeasurementsData(msr);
                    */

                    startActivity(intent);

                }
                else {
                    button.setIcon(R.drawable.ic_action_stop);

                    timer.setBase(SystemClock.elapsedRealtime());   // reset the time
                    timer.start();

                    infoText.setVisibility(View.GONE);

                    // TODO: Alex, start measuring the RR here!


                }

                recording = !recording;

                //FragmentManager fragmentManager = getFragmentManager();
                //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                //MainActivity.MainFragment mainFragment = new MainActivity.MainFragment();

                //fragmentTransaction.replace(R.id.view_main_top, mainFragment);
                //fragmentTransaction.commit();

            }
        });


        return recordFragmentView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /**
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }**/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
