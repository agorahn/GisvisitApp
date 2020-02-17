package hn.gisvisit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RegistroActivity extends AppCompatActivity {

    EditText edtNombre,edtEmail,edtPhone,edtPassword,edtPassword2;
    Button btnRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        edtNombre=(EditText)findViewById(R.id.edtNombre);

        edtNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                edtNombre.setBackgroundTintList( ColorStateList.valueOf( Color.parseColor("#1142F1") ) );

            }
            @Override
            public void afterTextChanged(Editable s) {
                /*
                if(s != null && s.length() > 0 && s.charAt(s.length() - 1) == ' '){
                    //dp something
                }

                 */
            }


        });

        edtEmail=(EditText)findViewById(R.id.edtEmail);

        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                edtEmail.setBackgroundTintList( ColorStateList.valueOf( Color.parseColor("#1142F1") ) );

            }
            @Override
            public void afterTextChanged(Editable s) {
                /*
                if(s != null && s.length() > 0 && s.charAt(s.length() - 1) == ' '){
                    //dp something
                }

                 */
            }


        });

        edtPhone=(EditText)findViewById(R.id.edtPhone);

        edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                edtPhone.setBackgroundTintList( ColorStateList.valueOf( Color.parseColor("#1142F1") ) );

            }
            @Override
            public void afterTextChanged(Editable s) {
                /*
                if(s != null && s.length() > 0 && s.charAt(s.length() - 1) == ' '){
                    //dp something
                }

                 */
            }


        });

        edtPassword=(EditText)findViewById(R.id.edtPassword);

        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                edtPassword.setBackgroundTintList( ColorStateList.valueOf( Color.parseColor("#1142F1") ) );

            }
            @Override
            public void afterTextChanged(Editable s) {
                /*
                if(s != null && s.length() > 0 && s.charAt(s.length() - 1) == ' '){
                    //dp something
                }

                 */
            }


        });

        edtPassword2=(EditText)findViewById(R.id.edtPassword2);

        edtPassword2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                edtPassword2.setBackgroundTintList( ColorStateList.valueOf( Color.parseColor("#1142F1") ) );

            }
            @Override
            public void afterTextChanged(Editable s) {
                /*
                if(s != null && s.length() > 0 && s.charAt(s.length() - 1) == ' '){
                    //dp something
                }

                 */
            }


        });

        btnRegistro=(Button)findViewById(R.id.btnRegistre);


        btnRegistro.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {



                if(validate_data())
                {
                    if(isValidEmail(edtEmail.getText().toString())) {



                        if (valid_samePassword()) {


                            //Log.e("APP","PASO ENTRO A REGISTRAR");
                            registrar(edtNombre.getText().toString(),edtEmail.getText().toString(),edtPhone.getText().toString(),edtPassword.getText().toString(),edtPassword2.getText().toString());



                        }
                    }
                    else
                    {
                        edtEmail.setText("");
                        edtEmail.setHint("Invalid Email");

                        edtEmail.setBackgroundTintList( ColorStateList.valueOf( Color.RED ) );
                    }
                }

            }

        });

    }

    public boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public boolean valid_samePassword()
    {
        if(edtPassword.getText().toString().equals(edtPassword2.getText().toString()))
        {
            return true;
        }
        else
        {

            edtPassword.setText("");
            edtPassword2.setText("");
            edtPassword.setHint("Password do not match");
            edtPassword2.setHint("Password do not match");
            edtPassword.setBackgroundTintList( ColorStateList.valueOf( Color.RED ) );
            edtPassword2.setBackgroundTintList( ColorStateList.valueOf( Color.RED ) );
            return false;
        }
    }

    public boolean validate_data()
    {
        if (edtNombre.length() <= 0) {
            edtNombre.setBackgroundTintList( ColorStateList.valueOf( Color.RED ) );
            return false;
        }
        if (edtEmail.length() <= 0) {
            edtEmail.setBackgroundTintList( ColorStateList.valueOf( Color.RED ) );
            return false;
        }
        if (edtPhone.length() <= 0) {
            edtPhone.setBackgroundTintList( ColorStateList.valueOf( Color.RED ) );
            return false;
        }
        if (edtPassword.length() <= 0) {
            edtPassword.setBackgroundTintList( ColorStateList.valueOf( Color.RED ) );
            return false;
        }
        if (edtPassword2.length() <= 0) {
            edtPassword2.setBackgroundTintList( ColorStateList.valueOf( Color.RED ) );
            return false;
        }

        return true;
    }

    public void registrar(String nombre,String email,String phone,String password,String password2)
    {

        String url="http://gisvisit.com/api/registro.php?nombre="+nombre+"&email="+email+"&phone="+phone+"&password="+password+"&password2="+password2+"";

        Log.e("APP",url);

        /*
        ProgressDialog progressDialog_list;
        progressDialog_list = new ProgressDialog(c.getContext());
        progressDialog_list.setMessage("Loading...");
        progressDialog_list.show();

         */

        StringRequest jsonArrayRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray array=jsonObject.getJSONArray("userData");

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject ob=array.getJSONObject(i);

                        String estado=ob.getString("estado");

                        if(estado.equals("0"))
                        {

                            MessageDialog exampleDialog = new MessageDialog();
                            exampleDialog.mensage="There is already an account with this email!!";
                            exampleDialog.show(getSupportFragmentManager(), "Information");
                        }
                        else
                        {
                            MessageDialog exampleDialog = new MessageDialog();
                            exampleDialog.mensage="Account has been created, enter your email to validate";
                            exampleDialog.show(getSupportFragmentManager(), "Valid Email");
                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("ERROR",e.toString());

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

       // progressDialog_list.dismiss();

    }


}
