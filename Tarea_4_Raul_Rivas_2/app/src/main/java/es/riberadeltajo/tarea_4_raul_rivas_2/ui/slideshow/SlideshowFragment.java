package es.riberadeltajo.tarea_4_raul_rivas_2.ui.slideshow;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import es.riberadeltajo.tarea_4_raul_rivas_2.ListaContactos;
import es.riberadeltajo.tarea_4_raul_rivas_2.ListaProductos;
import es.riberadeltajo.tarea_4_raul_rivas_2.MainActivity;
import es.riberadeltajo.tarea_4_raul_rivas_2.Producto;
import es.riberadeltajo.tarea_4_raul_rivas_2.databinding.FragmentSlideshowBinding;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Bundle datos = getArguments();
        if(datos!=null){
            int listaId = datos.getInt("listaId", -1);
            Cursor c = MainActivity.sqLiteDatabase.rawQuery(
                    "select idLista, cantidad from TablaProductosListas where idLista = ?",
                    new String[]{String.valueOf(listaId)});
            while (c.moveToNext()) {
                int idProducto = c.getInt(0);
                ListaProductos.listaProductos.clear();

                Cursor productos = MainActivity.sqLiteDatabase.rawQuery(
                        "SELECT * FROM TablaProductos WHERE idProducto IN (SELECT idProducto FROM TablaProductosListas WHERE idLista = ?)",
                        new String[]{String.valueOf(listaId)});


                while (productos.moveToNext()) {

                    byte[] imagenBytes = productos.getBlob(1);
                    Bitmap imagen = BitmapFactory.decodeByteArray(imagenBytes, 0, imagenBytes.length);
                    Double cantidad = c.getDouble(1);

                    //Producto p = new Producto();
                    ListaProductos.listaProductos.add(new Producto(
                            imagen,
                            productos.getString(2),
                            productos.getDouble(4),
                            productos.getString(3),
                            cantidad));
                }
                productos.close();

            }
        }

        Log.d("iei","Numero de contactos que hay: "+ ListaContactos.listaContactos.size());
        ListaContactos.listaContactos.clear();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}