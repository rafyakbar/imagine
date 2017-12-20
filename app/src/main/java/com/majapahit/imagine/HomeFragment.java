package com.majapahit.imagine;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.majapahit.imagine.url.Server;
import com.majapahit.imagine.util.DataHelper;
import com.majapahit.imagine.util.SettingModel;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private View view;
    private Random random = new Random();
    DataHelper dataHelper;
    SQLiteDatabase db;

    private LinearLayout leftLayout;
    private LinearLayout rightLayout;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        view = inflater.inflate(R.layout.fragment_home, container, false);
        leftLayout = view.findViewById(R.id.layout_left);
        rightLayout = view.findViewById(R.id.layout_right);

        dataHelper = new DataHelper(getContext());
        db = dataHelper.getReadableDatabase();

        //fakerCardView(20);
        getData();

        return view;
    }

    private void getData(){
        RequestQueue queue = Volley.newRequestQueue(getContext());

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Server.FOLLOWINGPOSTLIST_URL + SettingModel.getUserData(db).get("id"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSE", response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int c = 0; c < jsonArray.length(); c++) {
                                ImageView imageView = new ImageView(getContext());
                                imageView.setVisibility(View.VISIBLE);
                                Glide.with(getContext()).using(new FirebaseImageLoader()).load(FirebaseStorage.getInstance().getReference().child(jsonArray.getJSONObject(c).getString("dir"))).into(imageView);

//                                LinearLayout linearLayout = new LinearLayout(getContext());
//                                linearLayout.setOrientation(LinearLayout.VERTICAL);
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                if (c % 2 == 0){
                                    layoutParams.setMargins(20, 20, 10, 0);
                                    imageView.setLayoutParams(layoutParams);
                                    leftLayout.addView(imageView);
                                }
                                else {
                                    layoutParams.setMargins(10, 20, 20, 0);
                                    imageView.setLayoutParams(layoutParams);
                                    rightLayout.addView(imageView);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("message", "Error: " + error.getMessage());
                Log.d("message", "Failed with error msg:\t" + error.getMessage());
                Log.d("message", "Error StackTrace: \t" + error.getStackTrace());
                try {
                    byte[] htmlBodyBytes = error.networkResponse.data;
                    Log.e("message", new String(htmlBodyBytes), error);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void fakerCardView(int count){
        for (int c = 1; c <= count; c++){
            if (c % 2 == 0){
                leftLayout.addView(createCardView("Ini CardView " + c, c));
            }
            else {
                rightLayout.addView(createCardView("Ini CardView " + c, c));
            }
        }
    }

    private CardView createCardView(String text, int count) {
        LinearLayout.LayoutParams cardlayoutparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, random.nextInt(500) + 500){
        };
        if (count % 2 == 0)
            cardlayoutparams.setMargins(30, 30, 15, 0);
        else
            cardlayoutparams.setMargins(15, 30, 30, 0);

        CardView card = new CardView(getContext());
        card.setLayoutParams(cardlayoutparams);
        card.setRadius(9);
        card.setContentPadding(25, 25, 25, 25);
        card.setCardBackgroundColor(Color.LTGRAY);
        card.setMaxCardElevation(15);
        card.setCardElevation(9);

        TextView tv = new TextView(getContext());
        tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tv.setText(text + count);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        tv.setTextColor(Color.RED);
        tv.setGravity(Gravity.CENTER);

        card.addView(tv);

        return card;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//            Toast.makeText(context, "masuk", Toast.LENGTH_SHORT).show();
        }
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
