package com.lic.myearth;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginPage extends AppCompatActivity {

    EditText edt_username,edt_password;
    CheckBox chk_show_hide;

    boolean double_tap = false;

    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);



        edt_username=findViewById(R.id.edt_username);
        edt_password=findViewById(R.id.edt_password);

        chk_show_hide=findViewById(R.id.chk_Show_hide);


        findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });



        chk_show_hide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    edt_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    edt_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });






    }

    @Override
    public void onBackPressed() {
        if (double_tap)
        {
            super.onBackPressed();
        }
        else {
            Toast.makeText(LoginPage.this,"Press Again to Exit",Toast.LENGTH_SHORT).show();
            double_tap = true;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    double_tap = false;
                }
            },2000);
        }

    }


    private void userLogin() {
        //first getting the values
        final String username = edt_username.getText().toString();
        final String password = edt_password.getText().toString();

        //validating inputs
        if (TextUtils.isEmpty(username)) {
            edt_username.setError("Please enter your username");
            edt_username.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            edt_password.setError("Please enter your password");
            edt_password.requestFocus();
            return;
        }


        //if everything is fine

        class UserLogin extends AsyncTask<Void, Void, String> {

            ProgressBar progressBar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar = (ProgressBar) findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressBar.setVisibility(View.GONE);


                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        JSONObject userJson = obj.getJSONObject("user");

                        //creating a new user object
                        User user = new User(
                                userJson.getInt("uid"),
                                userJson.getString("user_id"),
                                userJson.getString("image_path"),
                                userJson.getString("username"),
                                userJson.getString("email"),

                                userJson.getString("gender"),
                                userJson.getString("designation")

                        );

                        //storing the user in shared preferences
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                        //starting the profile activity


                        if (userJson.getString("designation").contentEquals("Admin")) {

                            // Create your intent.



                            startActivity(new Intent(getApplicationContext(), AdminDashboard.class));
                            finish();
                        }
                        else if (userJson.getString("designation").contentEquals("Manager")){
                            startActivity(new Intent(getApplicationContext(), HomeAdmin.class));
                            finish();

                        }

                        else if (userJson.getString("designation").contentEquals("Supervisor")){
                            startActivity(new Intent(getApplicationContext(), HomeAdmin.class));
                            finish();

                        }

                        else if (userJson.getString("designation").contentEquals("Director")){
                            startActivity(new Intent(getApplicationContext(), AdminDashboard.class));
                            finish();

                        }

                        else if (userJson.getString("designation").contentEquals("Driver")){
                            startActivity(new Intent(getApplicationContext(), HomeAdmin.class));
                            finish();

                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("user_id", username);
                params.put("password", password);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_LOGIN, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }
}