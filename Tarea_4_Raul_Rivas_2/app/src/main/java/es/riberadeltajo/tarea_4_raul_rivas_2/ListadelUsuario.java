package es.riberadeltajo.tarea_4_raul_rivas_2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import es.riberadeltajo.tarea_4_raul_rivas_2.ListaProductos;
import es.riberadeltajo.tarea_4_raul_rivas_2.MainActivity;
import es.riberadeltajo.tarea_4_raul_rivas_2.Producto;
import es.riberadeltajo.tarea_4_raul_rivas_2.R;
import es.riberadeltajo.tarea_4_raul_rivas_2.databinding.ActivityListadelUsuarioBinding;

public class ListadelUsuario extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityListadelUsuarioBinding binding;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityListadelUsuarioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        String nombreLista = getIntent().getStringExtra("nombreLista");
        binding.edtNombreLista.setText(nombreLista.toString());
        int listaId = getIntent().getIntExtra("listaId",-100);
        if(listaId ==-100){
            Toast.makeText(this, "No funcionó", Toast.LENGTH_SHORT).show();
            //Cierra la actividad  si no esta bien el Id que le llega del MyHuevaListaRv
            finish();
            return;
        }

        if(ListaProductos.listaProductos!=null){
            ListaProductos.listaProductos.clear();
        }
        //misProductos = new ArrayList<>();
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

                ListaProductos.listaProductos.add(new Producto(
                        imagen,
                        productos.getString(2),
                        productos.getDouble(4),
                        productos.getString(3),
                        cantidad));
            }
            productos.close();

        }

            if(ListaProductos.adaptador!=null && ListaProductos.listaProductos!=null){
                ListaProductos.adaptador.notifyDataSetChanged();
            }




//        NavController navController = Navigation.findNavController(this, R.id.fragmentContainerView3);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nuevoNombreLista = binding.edtNombreLista.getText().toString();
                for (int i = 0; i < ListaProductos.listaProductos.size(); i++) {
                    Producto p = ListaProductos.listaProductos.get(i);
                    Double cantidad = p.getCantidad();
                    if(!nuevoNombreLista.equals(getIntent().getStringExtra("nombreLista"))){
                        ContentValues valores = new ContentValues();
                        valores.put("nombreLista",nuevoNombreLista);
                        valores.put("cantidad",cantidad);
                        String clausula = "idLista=?";
                        String[] argumentos = {String.valueOf(listaId)};
                        int respuesta= MainActivity.sqLiteDatabase.update("TablaListasProductos",valores,clausula,argumentos);

                        if(respuesta!=0){
                            Toast.makeText(getApplicationContext(),"Lista actualizada",Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }
        });
            binding.btnVolver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(i);
                }
            });

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.fragmentContainerView4);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}