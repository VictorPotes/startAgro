package co.potes.icesi.startagrocol.fragments;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import co.potes.icesi.startagrocol.R;
import co.potes.icesi.startagrocol.model.Proyecto;
import co.potes.icesi.startagrocol.util.UtilDomi;

import static android.app.Activity.RESULT_OK;

public class Fragment_Publicar extends Fragment implements View.OnClickListener{


    private FirebaseDatabase db;
    FirebaseStorage storage;


    private static final int REQUEST_GALLERY = 101;
    private EditText et_titulo;
    private EditText et_resumen;
    private EditText et_caracteristicas;
    private Button btnAgregarFotoPrimaria;
    private Button btnGuardar;
    private ImageView imagenPrimaria;

    private String path;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_publicar, container, false);

        db = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        ActivityCompat.requestPermissions(getActivity(), new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        }, 11);


        imagenPrimaria = v.findViewById(R.id.img_fotoP);



        et_titulo = v.findViewById(R.id.et_titulo);

        btnAgregarFotoPrimaria = v.findViewById(R.id.btn_agregarfotoPrim);

        et_resumen= v.findViewById(R.id.et_resumen);

        et_caracteristicas = v.findViewById(R.id.et_caracteristicas);

        btnGuardar = v.findViewById(R.id.btn_guardar);

        DatabaseReference reference = db.getReference().child("Proyectos");









        return v;
    }


    @Override
    public void onClick(View v) {
        if(v.equals(btnGuardar)){
            String titulo = et_titulo.getText().toString();
            String resumen = et_resumen.getText().toString();
            String caracteristicas = et_caracteristicas.getText().toString();


            DatabaseReference reference = db.getReference().child("Proyectos").push();
            String id_proyecto = reference.getKey();

            Proyecto p = new Proyecto();
            p.setId(id_proyecto);
            p.setTitulo(titulo);
            p.setDescripcion(resumen);




            if(path != null){
                try {
                    StorageReference ref = storage.getReference().child("Proyectos").child("Proyecto");
                    FileInputStream file = new FileInputStream(new File(path));
                    //Sube la foto
                    ref.putStream(file);

                    p.setUrl(String.valueOf(ref.getDownloadUrl()));
                    reference.setValue(p);
                }catch (FileNotFoundException ex){

                }
            }

        }else if (v.equals(btnAgregarFotoPrimaria)){
            Intent i = new Intent();
            i.setType("image/*");
            //i.setType("video/*");
            //i.setType("*/*");
            i.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(i, REQUEST_GALLERY);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_GALLERY && resultCode == RESULT_OK){

            path = UtilDomi.getPath(getContext(), data.getData());
            Bitmap m = BitmapFactory.decodeFile(path);
            imagenPrimaria.setImageBitmap(m);
        }
    }
}
