package com.example.android.bluetoothlegatt;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GraphFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GraphFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GraphFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private LineGraphSeries<DataPoint> mSeries;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private final static String TAG = GraphFragment.class.getSimpleName();

//    public class customComparator extends Comparator {
//        public boolean compare(Object object1, Object object2) {
//            return ((Measurement)object1).timestamp.compareTo(((Measurement)object2).timestamp)>0;
//        }
//    }

    public void setGraphData(Measurement[] msr) {

        //double[] m = new double[msr.length];
//        Date[] dates = (Date[])data.get("timestamps");

        if (msr.length == 0)
            return;

        Arrays.sort(msr);

        double min = msr[0].rr_value;
        double max = min;

        DataPoint[] dp = new DataPoint[msr.length];
        for (int i = 0; i < msr.length; i++) {

            if (msr[i].rr_value < min)
                min = msr[i].rr_value;

            if (msr[i].rr_value > max)
                max = msr[i].rr_value;

            dp[i] = new DataPoint(msr[i].timestamp, msr[i].rr_value);
            Log.d(TAG, String.format("rr_value: %d", msr[i].rr_value));
            System.out.println("Timestamp: " +msr[i].timestamp.getTime());
        }

//        Calendar calendar = Calendar.getInstance();
//        Date d1 = calendar.getTime();
//        calendar.add(Calendar.SECOND, 1);
//        Date d2 = calendar.getTime();
//        calendar.add(Calendar.SECOND, 1);
//        Date d3 = calendar.getTime();
//
//        DataPoint[] ff = new DataPoint[] {
//                new DataPoint(d1, msr[1].rr_value),
//                new DataPoint(d2, msr[2].rr_value),
//                new DataPoint(d3, msr[3].rr_value)
//        };
//
//        mSeries.resetData(ff);


        //Log.d(TAG, String.format("Min value: %f", min));
        //Log.d(TAG, String.format("Max value: %f", max));

        View rootView = getView();
        GraphView graph = (GraphView) rootView.findViewById(R.id.graph);

//        graph.getViewport().setMinX(d1.getTime());
//        graph.getViewport().setMaxX(d3.getTime());
//        graph.getViewport().setXAxisBoundsManual(true);

        //graph.getViewport().setYAxisBoundsManual(true);


        if (min == max) {
            min -= 2;
            max += 2;
        }
        //graph.getViewport().setMinY(min);
        //graph.getViewport().setMaxY(max);

        // set date label formatter
        //graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        //graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

// set manual x bounds to have nice steps
//        graph.getViewport().setMinX(msr[0].timestamp.getTime());
//        graph.getViewport().setMaxX(msr[msr.length - 1].timestamp.getTime());
//        graph.getViewport().setXAxisBoundsManual(true);

        System.out.println("New data set!");
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GraphFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GraphFragment newInstance(String param1, String param2) {
        GraphFragment fragment = new GraphFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public GraphFragment() {
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

        View rootView = inflater.inflate(R.layout.fragment_graph, container, false);
        GraphView graph = (GraphView) rootView.findViewById(R.id.graph);
        /**mSeries = new LineGraphSeries<DataPoint>(new DataPoint[] {
        });
        graph.addSeries(mSeries);*/

        final java.text.DateFormat dateTimeFormatter = DateFormat.getTimeFormat(getActivity());


        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // show normal x values
                    return dateTimeFormatter.format(new Date((long) value*1000));
                } else {
                    // show currency for y values
                    return super.formatLabel(value, isValueX);
                }
            }
        });

        Calendar calendar = Calendar.getInstance();
        Date d1 = calendar.getTime();
        calendar.add(Calendar.SECOND, 1);
        Date d2 = calendar.getTime();
        calendar.add(Calendar.SECOND, 1);
        Date d3 = calendar.getTime();


        mSeries = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(d1, 1),
                new DataPoint(d2, 5),
                new DataPoint(d3, 3)
        });
        graph.addSeries(mSeries);

// set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

// set manual x bounds to have nice steps
        graph.getViewport().setMinX(d1.getTime());
        graph.getViewport().setMaxX(d3.getTime());
        graph.getViewport().setXAxisBoundsManual(true);

// you can directly pass Date objects to DataPoint-Constructor
// this will convert the Date to double via Date#getTime()
        // Inflate the layout for this fragment
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        ((GraphActivity) activity).onSectionAttached(
//                getArguments().getInt(MainActivity.ARG_SECTION_NUMBER));
    }

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
