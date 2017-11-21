package com.example.ploderup.familymap;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.net.HttpURLConnection;

public class LoginFragment extends Fragment {
// MEMBERS
    private final String TAG = "LoginFragment";
    private UserInfo user_info;
    private EditText mHostField;
    private EditText mPortField;
    private EditText mUsernameField;
    private EditText mPasswordField;
    private EditText mFNameField;
    private EditText mLNameField;
    private EditText mEmailField;
    private RadioGroup mGenderButton;
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

        // Initialize class for user input
        user_info = new UserInfo();

        // Wire-up all widgets
        mHostField = v.findViewById(R.id.host_edit_text);
        mHostField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // This space is intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Save new info to UserInfo class
                user_info.setServerHost(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // This space is intentionally left blank
            }
        });
        mPortField = v.findViewById(R.id.port_edit_text);
        mPortField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // This space is intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Save new info to UserInfo class
                user_info.setServerPort(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // This space is intentionally left blank
            }
        });
        mUsernameField = v.findViewById(R.id.username_edit_text);
        mUsernameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // This space is intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Save new info to UserInfo class
                user_info.setUsername(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // This space is intentionally left blank
            }
        });
        mPasswordField = v.findViewById(R.id.password_edit_text);
        mPasswordField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // This space is intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Save new info to UserInfo class
                user_info.setPassword(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // This space is intentionally left blank
            }
        });
        mFNameField = v.findViewById(R.id.fname_edit_text);
        mFNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // This space is intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Save new info to UserInfo class
                user_info.setFirstName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // This space is intentionally left blank
            }
        });
        mLNameField = v.findViewById(R.id.lname_edit_text);
        mLNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // This space is intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Save new info to UserInfo class
                user_info.setLast_name(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // This space is intentionally left blank
            }
        });
        mEmailField = v.findViewById(R.id.email_edit_text);
        mEmailField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // This space is intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Save new info to UserInfo class
                user_info.setEmail(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // This space is intentionally left blank
            }
        });
        mGenderButton = v.findViewById(R.id.gender_radio_group);
        mGenderButton.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.male_radio_button) {
                    user_info.setGender("m");
                } else if (i == R.id.female_radio_button) {
                    user_info.setGender("f");
                }
            }
        });
        mRegisterButton = v.findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user_info.isServerInfoValid()) {
                    if(user_info.isRegisterInfoValid()) {
                        Toast.makeText(getActivity(), R.string.register_successful_toast,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), R.string.invalid_register_toast,
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.invalid_server_toast, Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
        mLoginButton = v.findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(user_info.isServerInfoValid()) {
                if(user_info.isLoginInfoValid()) {
                    Toast.makeText(getActivity(), R.string.login_successful_toast,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), R.string.invalid_login_toast, Toast.LENGTH_SHORT)
                            .show();
                }
            } else {
                Toast.makeText(getActivity(), R.string.invalid_server_toast, Toast.LENGTH_SHORT)
                        .show();
            }
            }
        });

        return v;
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

// INNER-CLASSES
    private class UserInfo {
    // MEMBERS
        private String mServerHost;
        String getServerHost() {
            return mServerHost;
        }
        void setServerHost(String ServerHost) {
            this.mServerHost = ServerHost;
        }

        private String mServerPort;
        String getServerPort() {
            return mServerPort;
        }
        void setServerPort(String ServerPort) {
            this.mServerPort = ServerPort;
        }

        private String mUsername;
        String getUsername() {
            return mUsername;
        }
        void setUsername(String Username) {
            this.mUsername = Username;
        }

        private String mPassword;
        String getPassword() {
            return mPassword;
        }
        void setPassword(String Password) {
            this.mPassword = Password;
        }

        private String mFirstName;
        String getFirstName() {
            return mFirstName;
        }
        void setFirstName(String FirstName) {
            this.mFirstName = FirstName;
        }

        private String mLastName;
        String getLast_name() {
            return mLastName;
        }
        void setLast_name(String LastName) {
            this.mLastName = LastName;
        }

        private String mEmail;
        String getEmail() {
            return mEmail;
        }
        void setEmail(String Email) {
            this.mEmail = Email;
        }

        private String mGender;
        String getGender() {
            return mGender;
        }
        void setGender(String Gender) {
            this.mGender = Gender;
        }


    // METHODS
        public UserInfo() {}

        public boolean isServerInfoValid() {
            final int MAX_PORT_NUM = 65535;
            final int MIN_PORT_NUM = 1;

            if(mServerHost == null || mServerPort == null) return false;
            if(mServerHost.equals("") || mServerPort.equals("")) return false;

            // Check host name
            if(!mServerHost.equals("localhost")) {
                // For the sake of this lab, if the name isn't "localhost", it should match the
                // following regular expression:
                if(!mServerHost.matches("[0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+")) return false;
            }

            // Check port number
            try {
                int i = Integer.parseInt(mServerPort);
                if (i > MAX_PORT_NUM || i < MIN_PORT_NUM) return false;
            } catch(NumberFormatException e) {
                return false;
            }

            // All tests passed
            return true;
        }

        public boolean isLoginInfoValid() {
            if(mUsername == null || mPassword == null) return false;
            if(mUsername.equals("") || mPassword.equals("")) return false;

            // The need for other tests might arise later

            // All tests passed
            return true;
        }

        public boolean isRegisterInfoValid() {
            if(mUsername == null || mPassword == null || mFirstName == null || mLastName == null ||
                    mEmail == null || mGender == null) return false;
            if(mUsername.equals("") || mPassword.equals("") || mFirstName.equals("") ||
                    mLastName.equals("") || mEmail.equals("") || mGender.equals("")) return false;

            // Check email
            if(!mEmail.matches("[A-Za-z0-9]+@[A-Za-z0-9]+\\.[A-Za-z0-9]+")) return false;

            // Check gender
            if(mGender != "m" && mGender != "f") return false;

            // All tests passed
            return true;
        }
    }
}
