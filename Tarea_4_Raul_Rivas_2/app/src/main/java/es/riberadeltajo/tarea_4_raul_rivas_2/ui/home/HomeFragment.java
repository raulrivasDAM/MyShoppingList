package es.riberadeltajo.tarea_4_raul_rivas_2.ui.home;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import es.riberadeltajo.tarea_4_raul_rivas_2.ListaProductos;
import es.riberadeltajo.tarea_4_raul_rivas_2.MainActivity;
import es.riberadeltajo.tarea_4_raul_rivas_2.NuevaListaProductos;
import es.riberadeltajo.tarea_4_raul_rivas_2.Producto;
import es.riberadeltajo.tarea_4_raul_rivas_2.ProductosListas;
import es.riberadeltajo.tarea_4_raul_rivas_2.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.btnCrearLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreLista = binding.nombreLista.getText().toString();


                MainActivity.sqLiteDatabase.execSQL("insert into TablaListasProductos (nombreLista) values ('"+nombreLista+"');");
                Toast.makeText(getContext(),"Lista guardada",Toast.LENGTH_SHORT).show();
                Log.d("Mira a ver machote", "Productos seleccionados antes de la consulta a la BD");

                Cursor c =   MainActivity.sqLiteDatabase.rawQuery("select max(idLista) from TablaListasProductos ",null);
                if(c.moveToFirst()){
                    int id = c.getInt(0);

                    NuevaListaProductos.nuevaListaProductos.add(new ProductosListas(id,nombreLista,ProductosListas.miListaProductos));
                    Log.d("Mira a ver machote", "Cantidad de productos seleccionados: " + ProductosListas.miListaProductos.size());

                    for(Producto p: ProductosListas.miListaProductos){
                        Cursor cur = MainActivity.sqLiteDatabase.rawQuery(
                                "SELECT * FROM TablaProductosListas WHERE idProducto = ? AND idLista = ?",
                                new String[]{String.valueOf(ListaProductos.listaProductos.indexOf(p) + 1), String.valueOf(id)});

                        if(cur.getCount()==0){
                            MainActivity.sqLiteDatabase.execSQL("insert into TablaProductosListas (idProducto, idLista, cantidad) values (?,?,?) ",
                                    new Object[]{ListaProductos.listaProductos.indexOf(p)+1,id,p.getCantidad()});
                            Log.d("Mira a ver machote",p.nombreProducto+ ""+ p.descripcion+ ""+ p.cantidad+""+p.img);

                        }else{
                            Log.d("Clave duplicada", "El producto ya existe en la lista");
                        }

                    }
                }
                c.close();
                ProductosListas.miListaProductos.clear();
                if (NuevaListaProductos.adaptadorNuevasListas != null && NuevaListaProductos.nuevaListaProductos != null) {
                    // Realizar la actualización del adaptador
                    NuevaListaProductos.adaptadorNuevasListas.notifyDataSetChanged();

                }
            }

        });





        return root;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}