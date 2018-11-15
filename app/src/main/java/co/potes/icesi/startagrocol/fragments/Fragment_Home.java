package co.potes.icesi.startagrocol.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import co.potes.icesi.startagrocol.R;
import co.potes.icesi.startagrocol.model.AdaptadorListaHome;
import co.potes.icesi.startagrocol.model.Proyecto;

public class Fragment_Home extends Fragment {

    private ImageButton btnBusqueda;
    private EditText txtBusqueda;
    private ListView lista;
    private AdaptadorListaHome adaptadorListas;
    private ArrayList<Proyecto> proyectos;
    private FirebaseDatabase db;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_home, container, false);

        String applicationID = "301664";

        btnBusqueda = v.findViewById(R.id.btnBusqueda);
        txtBusqueda = v.findViewById(R.id.txtBusqueda);

        adaptadorListas = new AdaptadorListaHome(this);
        lista = v.findViewById(R.id.lista);
        lista.setAdapter(adaptadorListas);
        proyectos = new ArrayList<Proyecto>();

        db = FirebaseDatabase.getInstance();

        DatabaseReference reference = db.getReference().child("Proyectos");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    String titulo =(String) postSnapshot.child("Titulo").getValue();
                    String url = (String) postSnapshot.child("url").getValue();
                    String descripcion = (String) postSnapshot.child("Descripcion").getValue();


                    Proyecto proyecto = new Proyecto();

                    proyecto.setDescripcion(descripcion);
                    proyecto.setTitulo(titulo);
                    proyecto.setUrl(url);


                    adaptadorListas.agregarLista(proyecto);





                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnBusqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                adaptadorListas.limpiar();

                Toast.makeText(getContext(), "click", Toast.LENGTH_SHORT).show();
            }
        });



        return v;
    }

}
