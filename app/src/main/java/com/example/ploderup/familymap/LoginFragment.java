package com.example.ploderup.familymap;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.net.HttpURLConnection;

public class LoginFragment extends Fragment {
// MEMBERS
    private EditText mHostField;
    private EditText mPortField;
    private EditText mUsernameField;
    private EditText mPasswordField;
    private EditText mFNameField;
    private EditText mLNameField;
    private EditText mEmailField;
    // private RadioGroup mGenderSelection;
    private Button mRegisterButton;
    private Button mLoginButton;

// METHODS
    /**
     * Required empty public constructor.
     */
    public LoginFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the fragment's layout
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        // Wire-up all widgets
        mHostField = v.findViewById(R.id.host_edit_text);
        mHostField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // This space is intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // I need a class to store all of this info;
                // class.setHost(s.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // This space is intentionally left blank
            }
        });
        mPortField = v.findViewById(R.id.host_edit_text);
        mPortField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // This space is intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // I need a class to store all of this info;
                // class.setPort(s.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // This space is intentionally left blank
            }
        });
        mUsernameField = v.findViewById(R.id.host_edit_text);
        mUsernameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // This space is intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // I need a class to store all of this info;
                // class.setUsername(s.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // This space is intentionally left blank
            }
        });
        mPasswordField = v.findViewById(R.id.host_edit_text);
        mPasswordField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // This space is intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // I need a class to store all of this info;
                // class.setPassword(s.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // This space is intentionally left blank
            }
        });
        mFNameField = v.findViewById(R.id.host_edit_text);
        mFNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // This space is intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // I need a class to store all of this info;
                // class.setFName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // This space is intentionally left blank
            }
        });
        mLNameField = v.findViewById(R.id.host_edit_text);
        mLNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // This space is intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // I need a class to store all of this info;
                // class.setLName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // This space is intentionally left blank
            }
        });
        mEmailField = v.findViewById(R.id.host_edit_text);
        mEmailField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // This space is intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // I need a class to store all of this info;
                // class.setEmail(s.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // This space is intentionally left blank
            }
        });


        return v;
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
