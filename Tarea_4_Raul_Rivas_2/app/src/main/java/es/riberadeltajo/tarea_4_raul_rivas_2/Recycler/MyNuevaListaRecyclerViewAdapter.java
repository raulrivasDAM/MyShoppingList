package es.riberadeltajo.tarea_4_raul_rivas_2.Recycler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import es.riberadeltajo.tarea_4_raul_rivas_2.ListadelUsuario;
import es.riberadeltajo.tarea_4_raul_rivas_2.ProductosListas;
import es.riberadeltajo.tarea_4_raul_rivas_2.R;
import es.riberadeltajo.tarea_4_raul_rivas_2.databinding.FragmentNuevalistaBinding;
import es.riberadeltajo.tarea_4_raul_rivas_2.ui.gallery.GalleryFragment;
import es.riberadeltajo.tarea_4_raul_rivas_2.ui.gallery.GalleryViewModel;
import es.riberadeltajo.tarea_4_raul_rivas_2.ui.slideshow.SlideshowFragment;


public class MyNuevaListaRecyclerViewAdapter extends RecyclerView.Adapter<MyNuevaListaRecyclerViewAdapter.ViewHolder> {

    private final List<ProductosListas> mValues;

    private Context contexto;

    private static GalleryFragment gallery;

    public MyNuevaListaRecyclerViewAdapter(List<ProductosListas> items, Context context) {

        mValues = items;
        contexto = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentNuevalistaBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mItem=mValues.get(position);
        holder.id.setText(mValues.get(position).id+"");
        holder.nombreLista.setText(mValues.get(position).nombreLista);

        holder.btnVerLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int listaId= mValues.get(holder.getAbsoluteAdapterPosition()).id;
                String nombre = mValues.get(holder.getAbsoluteAdapterPosition()).nombreLista;
                Intent i = new Intent(contexto, ListadelUsuario.class);
                i.putExtra("listaId",listaId);
                i.putExtra("nombreLista",nombre);
                contexto.startActivity(i);
            }
        });

        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int listaId = mValues.get(holder.getAbsoluteAdapterPosition()).id;
                String nombre = mValues.get(holder.getAbsoluteAdapterPosition()).nombreLista;


                SlideshowFragment slideshowFragment = new SlideshowFragment();

                Bundle bundle = new Bundle();
                bundle.putInt("listaId", listaId);
                bundle.putString("nombreLista", nombre);


                slideshowFragment.setArguments(bundle);


                FragmentManager fragmentManager = ((AppCompatActivity)contexto).getSupportFragmentManager();


                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView2, slideshowFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView nombreLista;
        public final Button btnVerLista;
        public final Button btnShare;


        public final TextView id;
        public ProductosListas mItem;

        public ViewHolder(FragmentNuevalistaBinding binding) {
            super(binding.getRoot());
            nombreLista = binding.txtNombreLista;
            btnVerLista = binding.btnVerLista;
            btnShare = binding.btnShare;
            id = binding.txtId;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + nombreLista.getText() + "'";
        }
    }
}