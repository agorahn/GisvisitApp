package hn.gisvisit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListContactsActivity extends AppCompatActivity {

    FloatingActionButton btnAddContact;

    private ListView cList;

    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    static List<Cliente> clienteList;
    static ClienteAdapter adapter;
    public static int codigo_vehiculo=18;
    DatabaseHelper db= new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_contacts);

        Toast.makeText(getApplicationContext(), "CLIENTE: "+db.getClienteCount(), Toast.LENGTH_SHORT).show();

        btnAddContact=(FloatingActionButton)findViewById(R.id.btnAddContact);

        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), AddContactsActivity.class));

            }
        });

        //working on list de contactos

        cList = findViewById(R.id.list_contactos);

        //clienteList = new ArrayList<>();
        clienteList = db.getAllClientes();
        adapter = new ClienteAdapter(getApplicationContext(),clienteList);

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(cList.getContext(), linearLayoutManager.getOrientation());

        cList.setAdapter(adapter);

        cList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                Cliente selectedItem = (Cliente) parent.getItemAtPosition(position);

                codigo_vehiculo=selectedItem.getId();

                Toast.makeText(view.getContext(), "CLIENTE: "+codigo_vehiculo, Toast.LENGTH_SHORT).show();


                Bundle parmetros = new Bundle();
                parmetros.putString("id", codigo_vehiculo+"");

                Intent i = new Intent(view.getContext(), ViewContactsActivity.class);
                i.putExtras(parmetros);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(i);

                /*
                final ProgressDialog progressDialog_list; progressDialog_list = new ProgressDialog(view.getContext());
                progressDialog_list.setMessage("Loading...");
                progressDialog_list.show();
                progressDialog_list.dismiss();
                 */


            }
        });

       // getDataContactos();



        //working on list de contactos

        //working on filter

        SearchView searchView = (SearchView)findViewById(R.id.search);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                Log.e("Main"," data search"+newText);

                adapter.getFilter().filter(newText);


                return true;
            }
        });



        //working on filter

    }

    private void getDataContactos() {
      //  final ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
      //  progressDialog.setMessage("Loading...");
      //  progressDialog.show();

                try {

                    clienteList=db.getAllClientes();

                } catch (Exception e) {

                    //progressDialog.dismiss();
                }

        adapter.notifyDataSetChanged();

       // progressDialog.dismiss();
    }







}
