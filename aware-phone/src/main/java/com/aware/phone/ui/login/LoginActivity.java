package com.aware.phone.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aware.phone.R;
import com.aware.phone.Aware_Client;
import com.aware.phone.ui.About;
import com.aware.phone.ui.login.LoginViewModel;
import com.aware.phone.ui.login.LoginViewModelFactory;


import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button RegButton = findViewById(R.id.register);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        final Button LogButton = findViewById(R.id.login);
        SharedPreferences sp=getSharedPreferences("Login",Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();


        if(sp.getString("isLogin","").equals("1")){

            Log.i("11111111111111","TTTTTTTTTTTTT");
            Intent i =new Intent(LoginActivity.this,Aware_Client.class);
            startActivity(i);
        }else{
            Log.i("11111111111111","FFFFFFFFF");
        }



        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                RegButton.setEnabled(loginFormState.isDataValid());
                LogButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }

                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);

                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        RegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);

                Context appContext = getApplicationContext();
                RequestQueue requestQueue = Volley.newRequestQueue(appContext);
                JSONObject jsonBody = new JSONObject();
                String URL_reg="http://192.168.3.11:5000/auth/register";
                try {
                    jsonBody.put("Title", "User's Email");
                    jsonBody.put("email",usernameEditText.getText().toString() );
                    Log.i("VOLLEY", usernameEditText.getText().toString());

                    jsonBody.put("Title", "User's password");
                    jsonBody.put("password", passwordEditText.getText().toString() );
                    Log.i("VOLLEY",  passwordEditText.getText().toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                send_data(jsonBody, requestQueue, URL_reg,loadingProgressBar,usernameEditText.getText().toString(), passwordEditText.getText().toString(),loginViewModel);

                try {
                    loginViewModel.Register(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString(),loadingProgressBar);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        LogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);

                Context appContext = getApplicationContext();
                RequestQueue requestQueue = Volley.newRequestQueue(appContext);
                JSONObject jsonBody = new JSONObject();
                String URL_log="http://192.168.3.11:5000/auth/login";
                ///onesignalemailhash
                try {
                    jsonBody.put("Title", "User's Email");
                    jsonBody.put("email",usernameEditText.getText().toString() );
                    Log.i("VOLLEY", usernameEditText.getText().toString());

                    jsonBody.put("Title", "User's password");
                    jsonBody.put("password", passwordEditText.getText().toString() );
                    Log.i("VOLLEY",  passwordEditText.getText().toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                send_data(jsonBody,requestQueue,URL_log,loadingProgressBar,usernameEditText.getText().toString(), passwordEditText.getText().toString(),loginViewModel);




            }
        });


    }

    public void send_data(JSONObject jsonBody, RequestQueue requestQueue, String URL, final ProgressBar loadingProgressBar, final String user_name, final String password, LoginViewModel L) {

        final String requestBody = jsonBody.toString();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.i("VOLLEY", response);
                // Log.i("VOLLEY", String.valueOf(response.charAt(1)));
                if(response.regionMatches(2,"authentication_token",0,20)){
                    Log.i("TOKEN","Token is here!!!!");
                    String token="";
                    String temp="";
                    int number=0;
                    for (int i=0;i<response.length();i++){
                        if(i>24){
                            if(response.charAt(i)=='\"'){
                                break;
                            }
                            token+=response.charAt(i);
                        }
                    }
                    Log.i("TOKEN",token);
                    SharedPreferences sp=getSharedPreferences("Login",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("token",token);
                    editor.putString("user_id",user_name);
                    editor.putString("isLogin","1");
                    editor.apply();

                }else{
                    Log.i("TOKEN","Token not found!!!!");
                }

                Login_Jump(user_name,password,loginViewModel);
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse.statusCode == 409) {
                    showRegFailed("This account has been registered before!",loadingProgressBar);
                }

                if (error.networkResponse.statusCode == 404) {
                    showRegFailed("This account does not exist, please register it berfore login!",loadingProgressBar);


                }
                if (error.networkResponse.statusCode == 400) {
                    showRegFailed("Wrong password!",loadingProgressBar);
                }
                Log.e("VOLLEY", error.toString());

            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };


        requestQueue.add(stringRequest);



    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome);
        // TODO : initiate successful logged in experience
        Intent i =new Intent(LoginActivity.this, Aware_Client.class);
        startActivity(i);
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void Login_Jump(String user_name, String password,LoginViewModel L){

//        SharedPreferences sp = getSharedPreferences("Token",0);
//        String token=sp.getString("token","");
//        String user_id=sp.getString("user_id","");
//        Log.i("Local Token",token);
//        Log.i("user_id",user_id);
        L.login(user_name,password);
    }
    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
    private void showRegFailed(String errorString, ProgressBar loadingProgressBar) {
        Toast.makeText(getApplicationContext(), errorString,Toast.LENGTH_SHORT).show();
        loadingProgressBar.setVisibility(View.GONE);
    }
}