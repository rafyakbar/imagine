package com.majapahit.imagine;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.majapahit.imagine.url.Server;
import com.majapahit.imagine.util.DataHelper;
import com.majapahit.imagine.util.SettingModel;
import com.majapahit.imagine.util.Utility;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.BitSet;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IdeaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IdeaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IdeaFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private static final int PICK_IMAGE_REQUEST = 234;

    private View view;
    DataHelper dataHelper;
    SQLiteDatabase db;

    private Button browseButton;
    private Button uploadButton;
    private ImageView imageView;
    private EditText title, tags;

    private Uri filePath;
    private String file;

    public IdeaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IdeaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IdeaFragment newInstance(String param1, String param2) {
        IdeaFragment fragment = new IdeaFragment();
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
        view = inflater.inflate(R.layout.fragment_idea, container, false);

        dataHelper = new DataHelper(getContext());
        db = dataHelper.getReadableDatabase();

        browseButton = view.findViewById(R.id.browsebutton);
        uploadButton = view.findViewById(R.id.uploadbutton);
        imageView = view.findViewById(R.id.imageView);
        title = view.findViewById(R.id.ideafragment_title);
        tags = view.findViewById(R.id.ideafragment_tag);

        browseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadFile() {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            file = "images/post/" + SettingModel.getUserData(db).get("id") + "_" + Utility.getTimestamp() + ".jpg";
            StorageReference riversRef = FirebaseStorage.getInstance().getReference().child(file);
            riversRef.putFile(filePath)

                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            enterToDatabase();
                            Toast.makeText(getContext(), "Your idea has been posted!", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        } else {
            Toast.makeText(getContext(), "Choose your photo!", Toast.LENGTH_SHORT);
        }
    }

    private void enterToDatabase() {
        ArrayList<String> params = new ArrayList<>();
        params.add(SettingModel.getUserData(db).get("id"));
        params.add(file.replace("/","="));
        params.add(title.getText().toString().trim());
        params.add(tags.getText().toString().trim());

        RequestQueue queue = Volley.newRequestQueue(getContext());
        final String[] result = new String[1];
        result[0] = "";
        String url = Server.POSTIDEA_URL;
        final ProgressDialog progressDialog = new ProgressDialog(getActivity().getWindow().getContext());
        for (String v :
                params) {
            v = v.replace(" ", "=+-+=");
            try {
                url += URLEncoder.encode(v, "UTF-8") + "/";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        Log.d("url", url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSE", response);
                        result[0] = response;
                        progressDialog.dismiss();
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
                result[0] = "failed";
                progressDialog.dismiss();
            }
        });
        queue.add(stringRequest);

        progressDialog.setMessage("Updating data...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a picture"), PICK_IMAGE_REQUEST);
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
