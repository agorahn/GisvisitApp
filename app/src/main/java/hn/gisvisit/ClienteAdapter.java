package hn.gisvisit;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
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

public class ClienteAdapter extends BaseAdapter implements Filterable
{
    Context context;

    List<Cliente> TempSubjectList;

   // private List<ItemsModel> itemsModelsl;
    private List<Cliente> itemsModelListFiltered;

    public ClienteAdapter(Context context,List<Cliente> listValue)
    {
        this.context = context;
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
        ViewItem viewItem = null;

        if(convertView == null)
        {
            viewItem = new ViewItem();

            LayoutInflater layoutInfiater = (LayoutInflater)this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInfiater.inflate(R.layout.list_view_clients, null);

            viewItem.NameTextView = (TextView)convertView.findViewById(R.id.main_nombre);
            viewItem.DescripcionTextView = (TextView)convertView.findViewById(R.id.main_descripcion);
            viewItem.Info = (ImageView)convertView.findViewById(R.id.main_info);
            viewItem.Telefono = (ImageView)convertView.findViewById(R.id.main_call);


            convertView.setTag(viewItem);
        }
        else
        {
            viewItem = (ViewItem) convertView.getTag();
        }


        viewItem.NameTextView.setText(TempSubjectList.get(position).getNombre());
        viewItem.DescripcionTextView.setText(TempSubjectList.get(position).getDireccion());

        viewItem.Info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //context.startActivity(new Intent(context, AddContactsActivity.class));
                //Toast.makeText(context, "INFO:"+TempSubjectList.get(position).getCelular()+" INFO:"+TempSubjectList.get(position).getTelefono(), Toast.LENGTH_SHORT).show();

                Bundle parmetros = new Bundle();
                parmetros.putString("id", TempSubjectList.get(position).getId()+"");

                Intent i = new Intent(v.getContext(), ViewContactsActivity.class);
                i.putExtras(parmetros);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);

            }
        });


        viewItem.Telefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //context.startActivity(new Intent(context, AddContactsActivity.class));

                //Toast.makeText(context, "TELEFONO: ", Toast.LENGTH_SHORT).show();
                String phoneNumber="";
                if(TempSubjectList.get(position).getTelefono().length()>1)
                {
                    phoneNumber=TempSubjectList.get(position).getTelefono();
                }
                else if(TempSubjectList.get(position).getCelular().length()>1)
                {
                    phoneNumber=TempSubjectList.get(position).getCelular();
                }

                Intent callIntent =new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+phoneNumber));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(callIntent);

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
                    List<Cliente> resultsModel = new ArrayList<>();
                    String searchStr = constraint.toString().toLowerCase();

                    for(Cliente itemsModel:itemsModelListFiltered){
                        if(itemsModel.getNombre().toLowerCase().contains(searchStr) || itemsModel.getDireccion().toLowerCase().contains(searchStr) || itemsModel.getCodigo_c_empresa().toLowerCase().contains(searchStr)){
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

                TempSubjectList = (List<Cliente>) results.values;
                notifyDataSetChanged();

            }
        };
        return filter;
    }
}

class ViewItem
{
    TextView NameTextView;
    TextView DescripcionTextView;
    ImageView Info;
    ImageView Telefono;
}
