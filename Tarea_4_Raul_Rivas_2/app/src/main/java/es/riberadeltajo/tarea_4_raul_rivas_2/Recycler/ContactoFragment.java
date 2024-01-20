package es.riberadeltajo.tarea_4_raul_rivas_2.Recycler;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.riberadeltajo.tarea_4_raul_rivas_2.Contacto;
import es.riberadeltajo.tarea_4_raul_rivas_2.ListaContactos;
import es.riberadeltajo.tarea_4_raul_rivas_2.R;


public class ContactoFragment extends Fragment {

    private final int PERMISO_CONTACTOS = 2;
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;


    public ContactoFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ContactoFragment newInstance(int columnCount) {
        ContactoFragment fragment = new ContactoFragment();
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
        View view = inflater.inflate(R.layout.fragment_contacto_list, container, false);

       
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            if (ContextCompat.checkSelfPermission(getContext(), "android.permission.READ_CONTACTS") !=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{"android.permission.READ_CONTACTS"}, PERMISO_CONTACTOS);
            } else {

                cargarContactos();
            }


            // Inicializar el adaptador y configurar el RecyclerView
            ListaContactos.adaptadorContactos = new MyContactoRecyclerViewAdapter(ListaContactos.listaContactos, context);
            recyclerView.setAdapter(ListaContactos.adaptadorContactos);
        }

        return view;
    }


    private void cargarContactos() {
        ListaContactos.listaContactos.clear();

        Cursor cursor = getContext().getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.getCount() > 0) {
            int idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

            while (cursor.moveToNext()) {
                if (idIndex >= 0 && nameIndex >= 0) {
                    String contactId = cursor.getString(idIndex);
                    String contactName = cursor.getString(nameIndex);

                    String phoneNumber = obtenerNumeroTelefono(contactId);

                    Contacto nuevoContacto = new Contacto();
                    nuevoContacto.setId(Integer.parseInt(contactId));
                    nuevoContacto.setNombre(contactName);
                    nuevoContacto.setTlf(phoneNumber);

                    ListaContactos.listaContactos.add(nuevoContacto);
                }
            }

            cursor.close();
        }
    }

    private String obtenerNumeroTelefono(String contactId) {
        Cursor phoneCursor = getContext().getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                new String[]{contactId},
                null
        );

        String phoneNumber = null;

        if (phoneCursor != null && phoneCursor.moveToFirst()) {
            int numberIndex = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

            if (numberIndex >= 0) {
                phoneNumber = phoneCursor.getString(numberIndex);
            }

            phoneCursor.close();
        }

        return phoneNumber;
    }

}
