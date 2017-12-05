package com.example.ploderup.UserInterface;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.ploderup.Communication.LoginTask;
import com.example.ploderup.Communication.RegisterTask;
import com.example.ploderup.Communication.ServerProxy;
import com.example.ploderup.Model.Settings;
import com.example.ploderup.Model.UserInfo;

public class LoginFragment extends Fragment {
// MEMBERS
    private final String TAG = "LoginFragment";
    private UserInfo mUserInfo;

// METHODS
    /**
     * Required empty public constructor.
     */
    public LoginFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserInfo = UserInfo.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EditText mHostField;
        EditText mPortField;
        EditText mUsernameField;
        EditText mPasswordField;
        EditText mFNameField;
        EditText mLNameField;
        EditText mEmailField;
        RadioGroup mGenderButton;
        Button mRegisterButton;
        Button mLoginButton;

        // Inflate the fragment's layout
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        // Retrieve class for user input
        mUserInfo = UserInfo.getInstance();

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
                mUserInfo.setServerHost(charSequence.toString());
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
                mUserInfo.setServerPort(charSequence.toString());
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
                mUserInfo.setUsername(charSequence.toString());
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
                mUserInfo.setPassword(charSequence.toString());
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
                mUserInfo.setFirstName(charSequence.toString());
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
                mUserInfo.setLast_name(charSequence.toString());
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
                mUserInfo.setEmail(charSequence.toString());
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
                    mUserInfo.setGender("m");
                } else if (i == R.id.female_radio_button) {
                    mUserInfo.setGender("f");
                }
            }
        });
        mRegisterButton = v.findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUserInfo.isServerInfoValid()) {
                    if(mUserInfo.isRegisterInfoValid()) {
                        // Attempt to register user
                        new RegisterTask((AppCompatActivity) getActivity()).execute();

                    } else {
                        Toast.makeText(getActivity(), R.string.invalid_register_toast,
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.invalid_server_toast,
                            Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
        mLoginButton = v.findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUserInfo.isServerInfoValid()) {
                    if(mUserInfo.isLoginInfoValid()) {
                        // Attempt to log the user in
                        new LoginTask((AppCompatActivity) getActivity()).execute();

                    } else {
                        Toast.makeText(getActivity(), R.string.invalid_login_toast,
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.invalid_server_toast,
                            Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        // Don't automatically open the keyboard
        getActivity()
                .getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        return v;
    }
}
