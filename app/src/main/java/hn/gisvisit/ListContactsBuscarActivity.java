package hn.gisvisit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ListContactsBuscarActivity extends AppCompatActivity {


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
        setContentView(R.layout.activity_lista_contacto_buscar);

        Toast.makeText(getApplicationContext(), "CLIENTE: "+db.getClienteCount(), Toast.LENGTH_SHORT).show();


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

                AddVisitActivity.tmp = db.getCliente(codigo_vehiculo+"");

               AddVisitActivity.estado_busqueda=true;
               finish();

            }
        });


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
