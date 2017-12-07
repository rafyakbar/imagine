package com.majapahit.imagine;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FavoriteFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FavoriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteFragment extends Fragment {
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

    private LinearLayout leftLayout;
    private LinearLayout rightLayout;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoriteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoriteFragment newInstance(String param1, String param2) {
        FavoriteFragment fragment = new FavoriteFragment();
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
        view = inflater.inflate(R.layout.fragment_favorite, container, false);
        leftLayout = view.findViewById(R.id.layout_left_favorite);
        rightLayout = view.findViewById(R.id.layout_right_favorite);

        fakerCardView(20);

        return view;
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
