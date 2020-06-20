package com.example.retailsupporter.Activity.FragmentPages;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.HashMap;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.retailsupporter.R;

import java.util.ArrayList;

import static com.example.retailsupporter.Activity.LoginActivity.db;
import static com.example.retailsupporter.Activity.LoginActivity.mAuth;
import static com.example.retailsupporter.Constant.USER_MANAGER;
import static com.example.retailsupporter.Constant.USER_POSITION;
import static com.example.retailsupporter.Constant.USER_STAFF;
import static com.example.retailsupporter.Controller.Authentication.registerUser;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterUserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegisterUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterUserFragment extends Fragment {
    EditText email, password, passwordConfirm;
    Button registerBtn;
    Spinner registerSpinner;
    ArrayList<String> userAuth;
    String getEmail;
    String getPassword;
    String getPasswordConfirm;
    String getUserAuth;
    TextView regResult;
    AlertDialog warning;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public RegisterUserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterUserFragment newInstance(String param1, String param2) {
        RegisterUserFragment fragment = new RegisterUserFragment();
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
        Log.e("Fragment onCreate", "RU");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        email = view.findViewById(R.id.registerEmail);
        password = view.findViewById(R.id.registerPassword);
        passwordConfirm = view.findViewById(R.id.registerPasswordConfirm);
        registerBtn = view.findViewById(R.id.registerButton);
        registerSpinner = view.findViewById(R.id.registerSpinner);
        regResult = view.findViewById(R.id.regResult);

        userAuth = new ArrayList<>();
        userAuth.add("Please Select Here");
        userAuth.add(USER_MANAGER);
        userAuth.add(USER_STAFF);

        ArrayAdapter userAuthAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, userAuth) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextSize(16);
                ((TextView) v).setTextColor(
                        getResources().getColorStateList(R.color.white)
                );
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                v.setBackgroundColor(Color.GRAY);
                ((TextView) v).setTextColor(
                        getResources().getColorStateList(R.color.white)
                );
                ((TextView) v).setGravity(Gravity.CENTER);

                return v;
            }
        };
        userAuthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        registerSpinner.setAdapter(userAuthAdapter);
        registerSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("ItemSelected-userAuth", registerSpinner.getSelectedItem().toString());
                getUserAuth = registerSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e("Nothing-userAuth", registerSpinner.getSelectedItem().toString());
            }
        });


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getEmail = email.getText().toString();

                if (!isValidEmail(getEmail)) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle("User email");
                    dialog.setMessage("Please enter a user email with correct format");
                    dialog.show();
                    email.setText("");
                    email.requestFocus();
                }
                //check the password length is over 6 to match Google request
                else if (password.getText().length() < 6) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle("User Password");
                    dialog.setMessage("Please enter a user password with at least 6 character.");
                    dialog.show();
                } else if (passwordConfirm.getText().length() < 6) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle("User Password Confirm");
                    dialog.setMessage("Please enter the same user password with at least 6 character.");
                    dialog.show();
                } else if (!password.getText().toString().equals(passwordConfirm.getText().toString())) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle("Different User Password");
                    dialog.setMessage("Please check the password, You must enter the same password.");
                    dialog.show();
                } else if (getUserAuth.equals("Please Select Here")) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle("User Position");
                    dialog.setMessage("Please select user position");
                    dialog.show();
                } else {
                    getPassword = password.getText().toString();
                    getPasswordConfirm = passwordConfirm.getText().toString();
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put(USER_POSITION, getUserAuth);
                    registerUser(db, getContext(), mAuth, getEmail, getPassword, hashMap,warning);
                    regResult.setText("Succeeded register a new user!");
                }
            }
        });


    }

    /**
     * check the email format is valid for Google
     *
     * @param email
     * @return
     */
    public static boolean isValidEmail(String email) {
        boolean result = false;
        String emailPattern = "^\\w{1,63}@[a-zA-Z0-9]{2,63}\\.[a-zA-Z]{2,63}(\\.[a-zA-Z]{2,63})?$";
        if (email.matches(emailPattern)) {
            result = true;
            return result;
        }
        return result;
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
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
