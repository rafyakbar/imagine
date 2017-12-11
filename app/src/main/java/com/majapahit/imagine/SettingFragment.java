package com.majapahit.imagine;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private static final int PICK_IMAGE_REQUEST = 234;

    DataHelper dataHelper;
    SQLiteDatabase db;
    private Uri filePath;

    private View view;
    private ImageView imageView;
    private EditText name, email, location, about, oldPassword, newPassword, confirmPassword;
    private Button saveGeneral, savePassword, favorite, logout;

    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
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

        view = inflater.inflate(R.layout.fragment_setting, container, false);

        dataHelper = new DataHelper(getContext());
        db = dataHelper.getReadableDatabase();

        imageView = view.findViewById(R.id.settingfragment_imageview);
        name = view.findViewById(R.id.settingfragment_name);
        email = view.findViewById(R.id.settingfragment_email);
        location = view.findViewById(R.id.settingfragment_location);
        about = view.findViewById(R.id.settingfragment_about);
        oldPassword = view.findViewById(R.id.settingfragment_oldpassword);
        newPassword = view.findViewById(R.id.settingfragment_newpassword);
        confirmPassword = view.findViewById(R.id.settingfragment_confirmpassword);
        saveGeneral = view.findViewById(R.id.settingfragment_savegeneral_button);
        savePassword = view.findViewById(R.id.settingfragment_savepassword_button);
        favorite = view.findViewById(R.id.settingfragment_favorit_button);
        logout = view.findViewById(R.id.settingfragment_logout_button);

        setData();
        setImageView();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        saveGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.length() == 0 || email.length() == 0 || location.length() == 0 || about.length() == 0) {
                    Toast.makeText(getContext(), "Fill all fields!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!email.getText().toString().contains("@")){
                    Toast.makeText(getContext(), "Invalid email!", Toast.LENGTH_SHORT).show();
                    return;
                }
                save(true);
            }
        });
        savePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String np = newPassword.getText().toString();
                String cp = confirmPassword.getText().toString();
                String op = oldPassword.getText().toString();
                if (np.length() == 0 || cp.length() == 0 || op.length() == 0) {
                    Toast.makeText(getContext(), "Fill all fields!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (np.equals(cp)) {
                    save(false);
                } else {
                    Toast.makeText(getContext(), "Invalid password confirmation!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingModel.update(db, "id", "-");
                SettingModel.update(db, "name", "-");
                SettingModel.update(db, "email", "-");
                SettingModel.update(db, "location", "-");
                SettingModel.update(db, "about", "-");
                SettingModel.update(db, "dir", "-");
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }

    private void setData() {
        HashMap<String, String> hashMap = SettingModel.getUserData(db);
        name.setText(hashMap.get("name"));
        email.setText(hashMap.get("email"));
        location.setText(hashMap.get("location"));
        about.setText(hashMap.get("about"));
    }

    private void save(final boolean general) {
        ArrayList<String> params = new ArrayList<>();
        params.add(SettingModel.getUserData(db).get("id"));
        if (general) {
            params.add(name.getText().toString().trim());
            params.add(email.getText().toString().trim());
            params.add(location.getText().toString().trim());
            params.add(about.getText().toString().trim());
            params.add(SettingModel.getUserData(db).get("dir").replace("/", "="));
        } else {
            params.add(oldPassword.getText().toString());
            params.add(newPassword.getText().toString());
        }

        RequestQueue queue = Volley.newRequestQueue(getContext());
        final String[] result = new String[1];
        result[0] = "";
        String url = (general) ? Server.UPDATEGENERAL_URL : Server.UPDATEPASSWORD_URL;
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

        // Request a string response from the provided URL.
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
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        progressDialog.setMessage("Updating data...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (result[0].equals("wps")) {
                    Toast.makeText(getContext(), "Wrong old password!", Toast.LENGTH_SHORT).show();
                } else if (result[0].equals("failed")) {
                    Toast.makeText(getContext(), "Failed to save!", Toast.LENGTH_SHORT).show();
                } else {
                    if (general) {
                        SettingModel.update(db, "name", name.getText().toString().trim());
                        SettingModel.update(db, "email", email.getText().toString().trim());
                        SettingModel.update(db, "location", location.getText().toString().trim());
                        SettingModel.update(db, "about", about.getText().toString().trim());
                    }
                    Toast.makeText(getContext(), "Update success!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
                uploadFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void uploadFile() {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.show();

            final String file = "images/user/" + SettingModel.getUserData(db).get("id") + "_" + Utility.getTimestamp() + ".jpg";
            StorageReference riversRef = FirebaseStorage.getInstance().getReference().child(file);
            riversRef.putFile(filePath)

                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            FirebaseStorage.getInstance().getReference().child(SettingModel.getUserData(db).get("dir")).delete();
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Picture has been uploaded!", Toast.LENGTH_LONG).show();
                            SettingModel.update(db, "dir", file);
                            save(true);
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
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            Toast.makeText(getContext(), "Choose your picture first!", Toast.LENGTH_SHORT);
        }
    }

    private void setImageView() {
        if (SettingModel.check(db, "dir")) {
            Glide.with(getContext())
                    .using(new FirebaseImageLoader())
                    .load(FirebaseStorage.getInstance().getReference().child(SettingModel.getUserData(db).get("dir")))
                    .into(imageView);
        } else {
            try {
                imageView.setImageBitmap(BitmapFactory.decodeStream(getActivity().getAssets().open("user.png")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
