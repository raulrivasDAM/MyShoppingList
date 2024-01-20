package es.riberadeltajo.tarea_4_raul_rivas_2.ui.add;

import static android.provider.MediaStore.ACTION_IMAGE_CAPTURE;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.riberadeltajo.tarea_4_raul_rivas_2.ListaProductos;
import es.riberadeltajo.tarea_4_raul_rivas_2.MainActivity;
import es.riberadeltajo.tarea_4_raul_rivas_2.Producto;
import es.riberadeltajo.tarea_4_raul_rivas_2.R;
import es.riberadeltajo.tarea_4_raul_rivas_2.databinding.FragmentAddProductBinding;
import es.riberadeltajo.tarea_4_raul_rivas_2.databinding.FragmentGalleryBinding;
import es.riberadeltajo.tarea_4_raul_rivas_2.ui.gallery.GalleryViewModel;

public class AddProductFragment extends Fragment {

    private AddProductViewModel mViewModel;
    private final int FOTO=1;
    Bitmap bmpFoto;
    boolean hayFoto;
    private final ArrayList<Producto> prod = new ArrayList<>();

    private FragmentAddProductBinding binding;
    public static AddProductFragment newInstance() {
        return new AddProductFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        AddProductViewModel addViewModel =
                new ViewModelProvider(this).get(AddProductViewModel.class);

        binding = FragmentAddProductBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        binding.btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!binding.edtPrecio.getText().toString().equals("") && !binding.edtNombreNuevoProd.getText().toString().equals("") && !binding.edtDescripcion.getText().toString().equals("")){

                    Drawable drawable = getResources().getDrawable(R.drawable.producto);
                    Bitmap bitmap = drawableToBitmap(drawable);
                    Double precio = Double.valueOf(binding.edtPrecio.getText().toString());
                    Producto p = new Producto(bitmap,binding.edtNombreNuevoProd.getText().toString(),
                            precio,binding.edtDescripcion.getText().toString());


                    if(hayFoto){
                        p.setImg(bmpFoto);

                    }
                    String consulta = "insert into TablaProductos (foto,nombreProducto,descripcion,precio) values (?,'" +
                            binding.edtNombreNuevoProd.getText().toString() +"',"+
                            precio+",'"+binding.edtDescripcion.getText().toString()+"');";
                    MainActivity.sqLiteDatabase.execSQL(consulta, new Object[]{bitmap});
                    Toast.makeText(getContext(),"Producto agregado",Toast.LENGTH_SHORT).show();
                    ListaProductos.listaProductos.add(p);
                    ListaProductos.adaptador.notifyDataSetChanged();
                }else{
                    Toast.makeText(getContext(),"Debes de completar todos los campos",Toast.LENGTH_SHORT).show();
                }

            }
        });

        binding.imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ACTION_IMAGE_CAPTURE);
                startActivityForResult(i,FOTO);
            }
        });
        return root;
    }
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==FOTO){
            bmpFoto=(Bitmap)data.getExtras().get("data");
//          portada.setImageBitmap(bmpFoto);
            binding.imgCarro.setImageBitmap(bmpFoto);
            hayFoto=true;
        }
    }
    public Bitmap getFoto(){

        return null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AddProductViewModel.class);
        // TODO: Use the ViewModel
    }

}