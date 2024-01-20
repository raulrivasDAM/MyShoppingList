package es.riberadeltajo.tarea_4_raul_rivas_2.Recycler;

import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import es.riberadeltajo.tarea_4_raul_rivas_2.Producto;
import es.riberadeltajo.tarea_4_raul_rivas_2.ProductosListas;
import es.riberadeltajo.tarea_4_raul_rivas_2.databinding.FragmentProductoBinding;

import java.util.List;

public class MyProductoRecyclerViewAdapter extends RecyclerView.Adapter<MyProductoRecyclerViewAdapter.ViewHolder> {

    private final List<Producto> mValues;

    public MyProductoRecyclerViewAdapter(List<Producto> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    return new ViewHolder(FragmentProductoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

            holder.mItem = mValues.get(position);
            holder.img.setImageBitmap(mValues.get(position).img);
            holder.nombreProducto.setText(mValues.get(position).nombreProducto);
            holder.precio.setText(mValues.get(position).precio + " €");
            holder.descripcion.setText(mValues.get(position).descripcion);
            holder.cantidad.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    int cantidad = 0;

                    try {

                        cantidad = Integer.parseInt(s.toString());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

                    int posi = holder.getAbsoluteAdapterPosition();
                    mValues.get(posi).setCantidad(cantidad);
                    ProductosListas.miListaProductos.add(mValues.get( holder.getAbsoluteAdapterPosition()));

                }
            });




        //ProductosListas.miListaProductos.add(new Producto());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView img;
        public final TextView nombreProducto;
        public final TextView precio;
        public final TextView descripcion;
        public final EditText cantidad;
        public Producto mItem;

    public ViewHolder(FragmentProductoBinding binding) {
      super(binding.getRoot());
      img = binding.img;
      nombreProducto = binding.nombreProducto;
      precio= binding.precio;
      descripcion = binding.descripcion;
      cantidad = binding.cantidad;
    }

        @Override
        public String toString() {
            return super.toString() + " '" + nombreProducto.getText() + "'";
        }
    }
}