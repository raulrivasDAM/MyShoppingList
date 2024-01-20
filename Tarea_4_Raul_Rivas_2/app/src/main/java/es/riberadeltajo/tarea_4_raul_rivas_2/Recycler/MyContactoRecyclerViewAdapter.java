package es.riberadeltajo.tarea_4_raul_rivas_2.Recycler;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import es.riberadeltajo.tarea_4_raul_rivas_2.Contacto;
import es.riberadeltajo.tarea_4_raul_rivas_2.ListaProductos;
import es.riberadeltajo.tarea_4_raul_rivas_2.Producto;
import es.riberadeltajo.tarea_4_raul_rivas_2.ProductosListas;
import es.riberadeltajo.tarea_4_raul_rivas_2.databinding.FragmentContactoBinding;

import java.util.List;


public class MyContactoRecyclerViewAdapter extends RecyclerView.Adapter<MyContactoRecyclerViewAdapter.ViewHolder> {

    private final List<Contacto> mValues;
    private final Context mContext;

    public MyContactoRecyclerViewAdapter(List<Contacto> items, Context context) {

        mValues = items;
        mContext=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentContactoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.id.setText(mValues.get(position).id+"");
        holder.txtNombreContacto.setText(mValues.get(position).nombre);
        if (!mValues.get(position).tlf.equals("")) {
            holder.telefono.setText(mValues.get(position).tlf);
        } else {

            holder.telefono.setText("");
        }
        holder.btnCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contacto contacto = mValues.get(holder.getAbsoluteAdapterPosition());
                compartirContacto(contacto);
                Toast.makeText(v.getContext(), "iei",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void compartirContacto(Contacto contacto) {

        String phoneNumberWithCountryCode = "+34666778899";

        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("¡Hola! machooote aqui tienes la lista de la compra:\n");
        messageBuilder.append("Nombre: ").append(contacto.getNombre()).append("\n");
        messageBuilder.append("Apellidos: ").append(contacto.getApellidos()).append("\n");
        messageBuilder.append("Tiene Teléfono: ").append(contacto.getTlf()).append("\n");

        if(ListaProductos.listaProductos!=null && !ListaProductos.listaProductos.isEmpty()){
            messageBuilder.append("Lista de la compra:\n");
            for(Producto pr : ListaProductos.listaProductos){
                messageBuilder.append("- ").append(pr.getNombreProducto()).append("\n");
                messageBuilder.append("- ").append(pr.getDescripcion()).append("\n");
                messageBuilder.append("- ").append(pr.getCantidad()).append("\n");
            }
        }
        String message = messageBuilder.toString();
        String url = String.format("https://api.whatsapp.com/send?phone=%s&text=%s", phoneNumberWithCountryCode, message);

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

        mContext.startActivity(intent);
//        if (intent.resolveActivity(mContext.getPackageManager()) != null) {
//            mContext.startActivity(intent);
//        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView id;
        public final TextView txtNombreContacto;
        public final Button btnCompartir;
        public final TextView telefono;


        public ViewHolder(FragmentContactoBinding binding) {
            super(binding.getRoot());
           id = binding.idContacto;
           txtNombreContacto = binding.txtNombreContacto;
           btnCompartir = binding.btnCompartir;
           telefono = binding.txtTelefono;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + txtNombreContacto.getText() + "'";
        }
    }
}