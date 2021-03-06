package com.example.dbmsproject3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class MainActivity extends AppCompatActivity {

    private   Retrofit retrofit;
    private   RetrofitInterface retrofitInterface;
    private String BaseURLLocal = "http://10.0.2.2:3000";
    private   String BaseURL="https://mvt-placement-assistance.herokuapp.com/";
    private Boolean isLogedin = false;


public void signout(View view)
{final TextView user = findViewById(R.id.user);
    final Button button = findViewById(R.id.signout);
    final Button button1 = findViewById(R.id.adminLogin);
    final Button button2 = findViewById(R.id.collegeLogin);


    SharedPreferences sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString("Activeuser","");
    editor.putString("Token","");
    editor.putString("token","");


    button.setVisibility(View.INVISIBLE);
    user.setVisibility(View.INVISIBLE);
    editor.commit();

    isLogedin=false;
    Toast.makeText(this,"Signed out successfully", Toast.LENGTH_SHORT).show();


}

    public void sendAdminLogin()
    {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String currentUser = sharedPreferences.getString("Activeuser","");
        if(currentUser.equals("Admin")&&isLogedin)
        {
            Intent intent = new Intent(getApplicationContext(),AdminActivity.class);
            startActivity(intent);
        }
        else {

            View view = getLayoutInflater().inflate(R.layout.admin_login_layout, null);
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setView(view);
            final AlertDialog alert = alertDialog.create();
            alert.show();

            Button login = view.findViewById(R.id.login);
            final EditText username = view.findViewById(R.id.adminUsername);
            final EditText password = view.findViewById(R.id.passwordEdit);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
            alert.dismiss();



                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("username", username.getText().toString());
                    map.put("password", password.getText().toString());
                   // map.put("auth","admin");

                    Call<RecieveInfo> call = retrofitInterface.requestAdminLogin(map);
                    Log.i("DEBUG","2222");
                    call.enqueue(new Callback<RecieveInfo>() {
                        @Override
                        public void onResponse(Call<RecieveInfo> call, Response<RecieveInfo> response) {
                            Log.i("CODE",Integer.toString(response.code()));
                            if (response.code() == 200) {


                               Toast.makeText(MainActivity.this, "Admin Login success", Toast.LENGTH_SHORT).show();
                                SharedPreferences sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext());;
                                final SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("Activeuser", "Admin");
                                final TextView user = findViewById(R.id.user);
                                user.setText("Signed in as: Admin");
                                user.setVisibility(View.VISIBLE);


                                isLogedin = true;
                                final Button button = findViewById(R.id.signout);
                                button.setVisibility(View.VISIBLE);
                                RecieveInfo recieveInfo = response.body();
                                editor.putString("Token", recieveInfo.gettoken());
                                editor.commit();

                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                       SharedPreferences.Editor editor =sharedPreferences.edit();
                                        editor.putString("Activeuser", "");
                                        editor.putString("Token", "");
                                        editor.commit();
                                        button.setVisibility(View.INVISIBLE);
                                        user.setVisibility(View.INVISIBLE);
                                    }
                                });

                                Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                                startActivity(intent);


                            } else
                                Toast.makeText(MainActivity.this, "Incorrect credentials", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<RecieveInfo> call, Throwable t) {
                            Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView user2 = findViewById(R.id.user);
        user2.setVisibility(View.INVISIBLE);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String currentUser = sharedPreferences.getString("Activeuser","");
        final TextView user = findViewById(R.id.user);
        final Button button = findViewById(R.id.signout);
        final Button button1 = findViewById(R.id.adminLogin);
        final Button button2 = findViewById(R.id.collegeLogin);
        final Button button3 = findViewById(R.id.publicView);
     
        if(!currentUser.equals(""))
        {
            isLogedin=true;

            user.setVisibility(View.VISIBLE);
            user.setText("Loged in as: "+currentUser);

            button.setVisibility(View.VISIBLE);




        }

        retrofit = new Retrofit.Builder()
                .baseUrl(BaseURL)
                .client(getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);


        Button adminLogin = findViewById(R.id.adminLogin);
        adminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendAdminLogin();
            }
        });



        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCollegeLogin();
            }
        });


        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewButton();
            }
        });




    }



    public void sendCollegeLogin(){
        SharedPreferences sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext());;

        String activeuser = sharedPreferences.getString("Activeuser","");

        if(!(activeuser.equals("")||activeuser.equals("Admin")))
        {
            Intent intent = new Intent(getApplicationContext(),CollegeActivity.class);
            startActivity(intent);
        }
        else {
            View view = this.getLayoutInflater().inflate(R.layout.college_login_layout, null);
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(view);
            final AlertDialog alert = builder.create();
            alert.show();
            final EditText username = view.findViewById(R.id.cetCode);
            final EditText password = view.findViewById(R.id.passwordEdit);
            final Button submit = view.findViewById(R.id.login);

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alert.dismiss();
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    ;

                    String token = sharedPreferences.getString("token", "");
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("username", username.getText().toString());
                    map.put("password", password.getText().toString());
                    map.put("token", token);
                    map.put("auth", "college");
                    Call<RecieveInfo> call = retrofitInterface.requestCollegeLogin(map);
                    call.enqueue(new Callback<RecieveInfo>() {
                        @Override
                        public void onResponse(Call<RecieveInfo> call, Response<RecieveInfo> response) {
                            if (response.code() == 200) {

                                   final Button button = findViewById(R.id.collegeLogin);
                                   // button.setText("Continue as "+username.getText().toString());
                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                ;
                                final SharedPreferences.Editor editor = sharedPreferences.edit();
                                RecieveInfo recieveInfo = response.body();
                                editor.putString("Token", recieveInfo.gettoken());
                                editor.putString("Activeuser", username.getText().toString());
                                editor.commit();
                                final TextView user = findViewById(R.id.user);
                             //   user.setText("Signed in as: " + username.getText().toString());
                                user.setVisibility(View.VISIBLE);
                                isLogedin = true;
                                final Button button6 = findViewById(R.id.signout);
                                button6.setVisibility(View.VISIBLE);

                                button6.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                        SharedPreferences.Editor editor =sharedPreferences.edit();
                                        editor.putString("Activeuser", "");
                                        editor.putString("token", "");
                                        editor.commit();

                                        button6.setVisibility(View.INVISIBLE);
                                        user.setVisibility(View.INVISIBLE);
                                    }
                                });


                                Toast.makeText(MainActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), CollegeActivity.class);
                                startActivity(intent);

                            } else if (response.code() == 400)
                                Toast.makeText(MainActivity.this, "Login Unsuccessfull", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<RecieveInfo> call, Throwable t) {
                            Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });

        }

    }


    public void ViewButton(){

    View view = this.getLayoutInflater().inflate(R.layout.view_layout,null);

    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setView(view);
    AlertDialog alert = builder.create();
    alert.show();



    view.findViewById(R.id.viewColleges).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(),ViewActivity.class);
            intent.putExtra("option",0);
            startActivity(intent);

        }
    });
        view.findViewById(R.id.viewQuestion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ViewActivity.class);
                intent.putExtra("option",1);
                startActivity(intent);

            }
        });




    }


    public static OkHttpClient getUnsafeOkHttpClient() {

        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                @Override
                public void checkClientTrusted(
                        X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(
                        X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            } };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts,
                    new SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext
                    .getSocketFactory();

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient = okHttpClient.newBuilder()
                    .sslSocketFactory(sslSocketFactory)
                    .hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER).build();

            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}