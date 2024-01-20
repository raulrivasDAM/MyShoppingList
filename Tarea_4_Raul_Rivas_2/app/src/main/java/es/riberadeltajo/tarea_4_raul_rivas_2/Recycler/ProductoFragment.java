package es.riberadeltajo.tarea_4_raul_rivas_2.Recycler;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.riberadeltajo.tarea_4_raul_rivas_2.ListaProductos;
import es.riberadeltajo.tarea_4_raul_rivas_2.R;

public class ProductoFragment extends Fragment {

    // TODO: Customize parameters
    private int mColumnCount = 1;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ProductoFragment newInstance(int columnCount) {
        ProductoFragment fragment = new ProductoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    public ProductoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ProductoFragment", "Fragmento creado");
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_producto_list, container, false);


        RecyclerView recyclerView = view.findViewById(R.id.rvNuevasListas);  // Reemplaza "R.id.recyclerView" con el ID real de tu RecyclerView
        Context context = view.getContext();

        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            ListaProductos.adaptador = new MyProductoRecyclerViewAdapter(ListaProductos.listaProductos);
            recyclerView.setAdapter(ListaProductos.adaptador);

        }

        return view;
    }
}