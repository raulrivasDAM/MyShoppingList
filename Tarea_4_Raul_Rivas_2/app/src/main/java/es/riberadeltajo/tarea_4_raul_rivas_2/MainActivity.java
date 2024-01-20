package es.riberadeltajo.tarea_4_raul_rivas_2;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.riberadeltajo.tarea_4_raul_rivas_2.Recycler.MyProductoRecyclerViewAdapter;
import es.riberadeltajo.tarea_4_raul_rivas_2.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private ActivityMainBinding binding;
    private Handler manejador; // interactua con la IU

    private Context contexto;
    public static SQLiteDatabase sqLiteDatabase;
    public MyProductoRecyclerViewAdapter adaptadorProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_add)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        //conexionDatos = new ConexionDatos(this);
        adaptadorProductos = new MyProductoRecyclerViewAdapter(ListaProductos.listaProductos);
        sqLiteDatabase = openOrCreateDatabase("ProductosBD",MODE_PRIVATE,null);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS TablaProductos");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS TablaProductosListas");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS TablaListasProductos");


        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS TablaProductos (" +
                "idProducto INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "foto BLOB, " +
                "nombreProducto VARCHAR(255), " +
                "descripcion VARCHAR(255)," +
                "precio REAL)");

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS TablaListasProductos(" +
                "idLista INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombreLista VARCHAR(255), " +
                "fecha DATE  DEFAULT CURRENT_DATE);");

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS TablaProductosListas (" +
                "idProducto INTEGER, " +
                "idLista INTEGER, " +
                "cantidad REAL,"+
                "FOREIGN KEY (idProducto) REFERENCES TablaProductos(idProducto), " +
                "FOREIGN KEY (idLista) REFERENCES TablaListasProductos(idLista),"+
                "UNIQUE(idProducto, idLista))");


        if (isNetworkAvailable()) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            manejador = new Handler(Looper.getMainLooper());
            contexto = this;

            executor.execute(() -> {
                Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM TablaProductos", null);
                if (c.getCount() == 0) {
                    descargarJSON();

                } else {
                    verTabla();
                }
                c.close();
            });
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "Fatality: No hay conexión a Internet",
                    Toast.LENGTH_LONG
            ).show();
        }

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }

    private void descargarJSON() {
        ConnectivityManager conManager = (ConnectivityManager) getSystemService(
                getApplicationContext().CONNECTIVITY_SERVICE
        );
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();

        if (conManager == null || !networkInfo.isConnected()) {
            manejador.post(() -> {
                Toast.makeText(getApplicationContext(), "Fatality", Toast.LENGTH_LONG).show();
            });
            return;
        }

        try {
            URL url = new URL("https://fp.cloud.riberadeltajo.es/listacompra/listaproductos.json");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setReadTimeout(5000);
            connection.setConnectTimeout(3000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);

            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                readJSON(connection.getInputStream());
            }

            connection.getInputStream().close();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readJSON(InputStream is) {
        String json;

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder stringBuilder = new StringBuilder();

            while ((json = reader.readLine()) != null) {
                stringBuilder.append(json).append("\n");
            }

            JSONArray jsonArray = new JSONObject(stringBuilder.toString()).getJSONArray("productos");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject product = jsonArray.getJSONObject(i);

                String name = product.getString("nombre");
                String description = product.getString("descripcion");
                String imageName = product.getString("imagen");
                double price = Double.parseDouble(product.getString("precio"));

                Log.d("iei", "imagen: " + imageName);
                Log.d("ouou","nombre: "+name);
                Log.d(getClass().getSimpleName(),"desc: "+description);
                Log.d("precio","price: "+price);
                getImage(imageName,name,price, description);

                Log.d("llega hasta aqui","price: "+price);
                Log.d("llega hasta aqui despues del load","price: ");
            }

            verTabla();


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    private void getImage(String imageName, String productName, double productPrice, String description) {
        Bitmap bitmap = descargarImagenInternet(imageName);

        insertarEnBaseDeDatos(bitmap, productName, productPrice , description);


    }

    private void insertarEnBaseDeDatos(Bitmap imagen, String productName, double productPrice, String description) {

        if (sqLiteDatabase != null) {
            Log.d("INFO", "Almacenando en la base de datos...");
            // Convertir el bitmap a un array de bytes
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imagen.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();


            sqLiteDatabase.execSQL("INSERT INTO TablaProductos (foto, nombreProducto,descripcion, precio) VALUES (?, ?, ?, ?)",
                    new Object[]{byteArray, productName, description,productPrice });

            Log.d("me mueran","Llega hasta aqui");
            ListaProductos.listaProductos.add(new Producto(imagen, productName, productPrice, description));

            runOnUiThread(() -> {
                if (ListaProductos.adaptador != null) {
                    ListaProductos.adaptador.notifyDataSetChanged();
                }
            });

        }
        Log.d("INFO", "La base de datos no esta vacia");
    }


    private Bitmap descargarImagenInternet(String nombre) {
        // Lógica para descargar una imagen desde Internet
        Bitmap bitmap = null;

        try {
            URL imagenURL = new URL("https://fp.cloud.riberadeltajo.es/listacompra/images/" + nombre);
            HttpURLConnection con = (HttpURLConnection) imagenURL.openConnection();
            con.setReadTimeout(10000);
            con.setConnectTimeout(5000);
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.connect();

            if (con.getResponseCode() == 200) {
                InputStream is = con.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);
            }

            con.getInputStream().close();
        } catch (MalformedURLException e) {
            Log.e("ConexionDatos", "Error de URL de imagen: " + e.getMessage());
        } catch (IOException e) {
            Log.e("ConexionDatos", "Error de descarga de imagen: " + e.getMessage());
        }

        return bitmap;
    }


    private void  verTabla() {
        ListaProductos.listaProductos.clear();
        Cursor c=sqLiteDatabase.rawQuery("SELECT * FROM TablaProductos", null);
        while(c.moveToNext()){
            byte[] imagenBytes = c.getBlob(1);
            Bitmap imagen = BitmapFactory.decodeByteArray(imagenBytes, 0, imagenBytes.length);
            ListaProductos.listaProductos.add(new Producto(imagen, c.getString(2), c.getDouble(4), c.getString(3)));

        }
        runOnUiThread(() -> {
            if (ListaProductos.adaptador != null) {
                ListaProductos.adaptador.notifyDataSetChanged();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()== R.id.action_settings){
            for(Producto pr : ListaProductos.listaProductos){
                System.out.println(pr.nombreProducto);

            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}