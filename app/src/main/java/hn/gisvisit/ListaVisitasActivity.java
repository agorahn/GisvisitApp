package hn.gisvisit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.List;

public class ListaVisitasActivity extends AppCompatActivity {



    private ListView vList;

    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    static List<Visita> visitList;
    static VisitAdapter adapter;
    DatabaseHelper db= new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_visitas);

        vList = findViewById(R.id.listEvent);

        //clienteList = new ArrayList<>();
        visitList = db.getAllVisitas();
        adapter = new VisitAdapter(getApplicationContext(),visitList);

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(vList.getContext(), linearLayoutManager.getOrientation());

        vList.setAdapter(adapter);

        vList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                /*
                Cliente selectedItem = (Cliente) parent.getItemAtPosition(position);

                codigo_vehiculo=selectedItem.getId();

                Toast.makeText(view.getContext(), "CLIENTE: "+codigo_vehiculo, Toast.LENGTH_SHORT).show();


                Bundle parmetros = new Bundle();
                parmetros.putString("id", codigo_vehiculo+"");

                Intent i = new Intent(view.getContext(), ViewContactsActivity.class);
                i.putExtras(parmetros);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(i);

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

    }


}
