package hn.gisvisit;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

public class VisitAdapter extends BaseAdapter implements Filterable
{
    Context context;

    List<Visita> TempSubjectList;


    // private List<ItemsModel> itemsModelsl;
    private List<Visita> itemsModelListFiltered;
    DatabaseHelper db;

    public VisitAdapter(Context context,List<Visita> listValue)
    {
        this.context = context;
        this.db= new DatabaseHelper(context);
        this.TempSubjectList = listValue;
        this.itemsModelListFiltered = listValue;
    }

    @Override
    public int getCount()
    {
        return this.TempSubjectList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return this.TempSubjectList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {

        ViewItemVisit viewItem = null;

        if(convertView == null)
        {
            viewItem = new ViewItemVisit();

            LayoutInflater layoutInfiater = (LayoutInflater)this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInfiater.inflate(R.layout.list_view_visit, null);

            viewItem.Icon = (ImageView)convertView.findViewById(R.id.image_icon);
            viewItem.NameTextView = (TextView)convertView.findViewById(R.id.main_nombre);
            viewItem.DescripcionTextView = (TextView)convertView.findViewById(R.id.main_descripcion);
            viewItem.Info = (ImageView)convertView.findViewById(R.id.main_info);
            viewItem.Delete = (ImageView)convertView.findViewById(R.id.delete);


            convertView.setTag(viewItem);
        }
        else
        {
            viewItem = (ViewItemVisit) convertView.getTag();
        }

        Cliente tmp=new Cliente();
        tmp=db.getCliente_codigo(TempSubjectList.get(position).getCodigo_c_cliente());

        viewItem.NameTextView.setText(tmp.getNombre());
        viewItem.DescripcionTextView.setText(TempSubjectList.get(position).getRazon_visita());

        if(TempSubjectList.get(position).getEstado_sincronizar().equals("1"))
        {
            Bitmap batmapBitmap = BitmapFactory.decodeResource(convertView.getResources(), R.drawable.visit2);
            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(convertView.getResources(), batmapBitmap);
            viewItem.Icon.setImageDrawable(circularBitmapDrawable);
        }

        viewItem.Info.setVisibility(View.GONE);

        viewItem.Info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //context.startActivity(new Intent(context, AddContactsActivity.class));
               Toast.makeText(context, "INFO:"+TempSubjectList.get(position).getId(), Toast.LENGTH_SHORT).show();

                /*
                Bundle parmetros = new Bundle();
                parmetros.putString("id", TempSubjectList.get(position).getCodigo());

                Intent i = new Intent(v.getContext(), ViewContactsActivity.class);
                i.putExtras(parmetros);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);

                 */




            }
        });


        viewItem.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //context.startActivity(new Intent(context, AddContactsActivity.class));

                //Toast.makeText(context, "BORRAR VISITA ", Toast.LENGTH_SHORT).show();

                new AlertDialog.Builder(view.getRootView().getContext())
                        .setTitle("Advertencia")
                        .setMessage("Do you want to delete?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //set what would happen when positive button is clicked
                                    //finish();
                                    db.deleteVisita(TempSubjectList.get(position).getId()+"");

                                    TempSubjectList.remove(position);
                                    notifyDataSetChanged();

                                    //notifyDataSetChanged();

                                }
                         })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                            }
                        })
                        .show();



                /*
                Intent callIntent =new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:9961907453"));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(callIntent);

                 */

            }
        });


        return convertView;
    }


    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults filterResults = new FilterResults();
                if(constraint == null || constraint.length() == 0){
                    filterResults.count = itemsModelListFiltered.size();
                    filterResults.values = itemsModelListFiltered;

                }else{
                    List<Visita> resultsModel = new ArrayList<>();
                    String searchStr = constraint.toString().toLowerCase();


                    for(Visita itemsModel:itemsModelListFiltered){
                        Cliente tmp=new Cliente();
                        tmp=db.getCliente_codigo(itemsModel.getCodigo_c_cliente());
                        //tmp.getNombre().toLowerCase().contains(searchStr) || itemsModel.getRazon_visita().toLowerCase().contains(searchStr)
                        if(tmp.getNombre().toLowerCase().contains(searchStr) || itemsModel.getRazon_visita().toLowerCase().contains(searchStr)){
                            resultsModel.add(itemsModel);

                        }
                        filterResults.count = resultsModel.size();
                        filterResults.values = resultsModel;
                    }


                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                TempSubjectList = (List<Visita>) results.values;
                notifyDataSetChanged();

            }
        };
        return filter;
    }
}

class ViewItemVisit
{
    ImageView Icon;
    TextView NameTextView;
    TextView DescripcionTextView;
    ImageView Info;
    ImageView Delete;
}