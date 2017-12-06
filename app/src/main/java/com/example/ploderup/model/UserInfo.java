package com.example.ploderup.model;

/**
 * Created by ploderup on 12/5/17.
 */

public class UserInfo {
// SINGLETON STUFF
    private static UserInfo mUserInfo;
    private UserInfo() {}
    public static UserInfo getInstance() {
        if (mUserInfo == null) mUserInfo = new UserInfo();
        return mUserInfo;
    }

// MEMBERS
    private String mServerHost;
    public String getServerHost() {
        return mServerHost;
    }
    public void setServerHost(String ServerHost) {
        this.mServerHost = ServerHost;
    }

    private String mServerPort;
    public String getServerPort() {
        return mServerPort;
    }
    public void setServerPort(String ServerPort) {
        this.mServerPort = ServerPort;
    }

    private String mUsername;
    public String getUsername() {
        return mUsername;
    }
    public void setUsername(String Username) {
        this.mUsername = Username;
    }

    private String mPassword;
    public String getPassword() {
        return mPassword;
    }
    public void setPassword(String Password) {
        this.mPassword = Password;
    }

    private String mFirstName;
    public String getFirstName() {
        return mFirstName;
    }
    public void setFirstName(String FirstName) {
        this.mFirstName = FirstName;
    }

    private String mLastName;
    public String getLast_name() {
        return mLastName;
    }
    public void setLast_name(String LastName) {
        this.mLastName = LastName;
    }

    private String mEmail;
    public String getEmail() {
        return mEmail;
    }
    public void setEmail(String Email) {
        this.mEmail = Email;
    }

    private String mGender;
    public String getGender() {
        return mGender;
    }
    public void setGender(String Gender) {
        this.mGender = Gender;
    }


    // METHODS
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
        if(!mGender.equals("m") && !mGender.equals("f")) return false;

        // All tests passed
        return true;
    }
}