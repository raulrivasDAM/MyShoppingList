package es.riberadeltajo.tarea_4_raul_rivas_2.Recycler;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.riberadeltajo.tarea_4_raul_rivas_2.NuevaListaProductos;
import es.riberadeltajo.tarea_4_raul_rivas_2.R;


public class NuevaListaFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;


    public NuevaListaFragment() {
    }


    @SuppressWarnings("unused")
    public static NuevaListaFragment newInstance(int columnCount) {
        NuevaListaFragment fragment = new NuevaListaFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nuevalista_list, container, false);


        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            RecyclerView rv = view.findViewById(R.id.rvNuevasListas);
            Context contexto = view.getContext();

            if (rv != null) {
                rv.setLayoutManager(new LinearLayoutManager(contexto));
                NuevaListaProductos.adaptadorNuevasListas = new MyNuevaListaRecyclerViewAdapter(NuevaListaProductos.nuevaListaProductos, getContext());
                rv.setAdapter( NuevaListaProductos.adaptadorNuevasListas);

            }


        }
        return view;
    }
}