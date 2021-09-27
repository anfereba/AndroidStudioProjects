package com.anfereba.Fondosdepantalla.FragmentosAdministrador;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anfereba.Fondosdepantalla.MainActivityAdministrador;
import com.anfereba.Fondosdepantalla.R;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import static com.google.firebase.storage.FirebaseStorage.getInstance;

import java.util.HashMap;
import java.util.Objects;

public class PerfilAdmin extends Fragment {

    //Firebase

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference BASE_DE_DATOS_ADMINISTRADORES;

    StorageReference storageReference;
    String RutaDeAlmacenamiento = "Fotos_Perfil_Administradores/*";

    //Solicitudes
    private static final int CODIGO_DE_SOLICITUD_DE_CAMARA = 100;
    private static final int CODIGO_DE_CAMARA_DE_SELECCION_DE_IMAGENES = 200;

    //Solicitudes de galeria
    private static final int CODIGO_DE_SOLICITUD_DE_ALMACENAMIENTO = 300;
    private static final int CODIGO_DE_GALERIA_DE_SELECCION_DE_IMAGEN = 400;

    //Permisos a solicitar
    private String[] permisos_de_la_camara;
    private String[] permisos_de_almacenamiento;

    private Uri imagen_uri;
    private String imagen_perfil;
    private ProgressDialog progressDialog;

    //Vistas

    ImageView FOTOPERFILIMG;
    TextView UIDPERFIL, NOMBRESPERFIL,APELLIDOSPEFIL,CORREOPERFIL,PASSWORDPERFIL,EDADPERFIL;
    Button ACTUALIZARPASS,ACTUALIZARDATOS;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil_admin, container, false);

        FOTOPERFILIMG = view.findViewById(R.id.FOTOPERFILIMG);
        UIDPERFIL = view.findViewById(R.id.UIDPERFIL);
        NOMBRESPERFIL = view.findViewById(R.id.NOMBRESPERFIL);
        APELLIDOSPEFIL = view.findViewById(R.id.APELLIDOSPERFIL);
        CORREOPERFIL = view.findViewById(R.id.CORREOPERFIL);
        PASSWORDPERFIL = view.findViewById(R.id.PASSWORDPERFIL);
        EDADPERFIL = view.findViewById(R.id.EDADPERFIL);
        ACTUALIZARPASS = view.findViewById(R.id.ACTUALIZARPASS);
        ACTUALIZARDATOS = view.findViewById(R.id.ACTUALIZARDATOS);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        storageReference = getInstance().getReference();

        //Inicializar los permisos
        permisos_de_la_camara = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        permisos_de_almacenamiento = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};




        progressDialog = new ProgressDialog(getActivity());




        BASE_DE_DATOS_ADMINISTRADORES = FirebaseDatabase.getInstance().getReference("BASE DE DATOS ADMINISTRADORES");
        BASE_DE_DATOS_ADMINISTRADORES.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    //OBTENER LOS DATOS
                    String uid = ""+snapshot.child("UID").getValue();
                    String nombre = ""+snapshot.child("NOMBRES").getValue();
                    String apellidos = ""+snapshot.child("APELLIDOS").getValue();
                    String correo = ""+snapshot.child("CORREO").getValue();
                    String pass = ""+snapshot.child("PASSWORD").getValue();
                    String edad = ""+snapshot.child("EDAD").getValue();
                    String imagen = ""+snapshot.child("IMAGEN").getValue();

                    UIDPERFIL.setText(uid);
                    NOMBRESPERFIL.setText(nombre);
                    APELLIDOSPEFIL.setText(apellidos);
                    CORREOPERFIL.setText(correo);
                    PASSWORDPERFIL.setText(pass);
                    EDADPERFIL.setText(edad);

                    try{
                        //Si existe la iamgen, cargue el string y luego la imagen
                        Picasso.get().load(imagen).placeholder(R.drawable.perfil).into(FOTOPERFILIMG);
                    }catch ( Exception e){
                        //No existe imagen en la bd del administrador
                        Picasso.get().load(R.drawable.perfil).into(FOTOPERFILIMG);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FOTOPERFILIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CambiarImagenPerfilAdministrador();
            }
        });




        return view;
    }

    private void CambiarImagenPerfilAdministrador() {
        String[] opcion = {"Cambiar foto de perfil"};

        //Crear el alertDialog

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Asignamos titulo
        builder.setTitle("Elegir una opcion");
        builder.setItems(opcion, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0){
                    imagen_perfil = "IMAGEN";
                    ElegirFoto();

                }
            }
        });
        builder.create().show();
    }

    private boolean Comprobar_los_permisos_de_la_camara(){
        boolean resultado_uno = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);
        boolean resultado_dos = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return resultado_dos && resultado_dos;
    }

    private boolean Comprobar_los_permisos_de_almacenamiento(){
        boolean resultado_uno = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);
        return resultado_uno;
    }

    private void Solicitar_los_permisos_de_almacenamiento(){
        requestPermissions(permisos_de_almacenamiento, CODIGO_DE_SOLICITUD_DE_ALMACENAMIENTO);
    }

    //Solicitar permisos de la camara en tiempo de ejecucion //
    private void Solicitar_permisos_de_camara(){
        requestPermissions(permisos_de_la_camara,CODIGO_DE_SOLICITUD_DE_CAMARA);
    }

    //Elegir de donde procede la imagen
    private void ElegirFoto() {
        String[] opciones = {"Camara","Galeria"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Seleccionar imagen de: ");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0){
                    //Vamos a verificar si el permiso ya esta concedido
                    if (!Comprobar_los_permisos_de_la_camara()){
                        Solicitar_permisos_de_camara();

                    }else{
                        Elegir_De_Camara();
                    }
                }else if (i == 1){
                    if (!Comprobar_los_permisos_de_almacenamiento()){
                        Solicitar_los_permisos_de_almacenamiento();
                    }else{
                        Elegir_De_Galeria();
                    }
                }
            }
        });
        builder.create().show();
    }

    private void Elegir_De_Galeria() {
        Intent GaleriaIntent = new Intent(Intent.ACTION_PICK);
        GaleriaIntent.setType("image/*");
        startActivityForResult(GaleriaIntent,CODIGO_DE_GALERIA_DE_SELECCION_DE_IMAGEN);
    }

    private void Elegir_De_Camara() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Foto Temporal");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Descripcion Temporal");
        imagen_uri = Objects.requireNonNull(getActivity()).getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        //Actividad para abrir la camara

        Intent camaraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camaraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imagen_uri);
        startActivityForResult(camaraIntent,CODIGO_DE_CAMARA_DE_SELECCION_DE_IMAGENES);

    }

    private void ActualizarImagenEnBD(Uri uri){
        String Ruta_de_archivo_y_nombre = RutaDeAlmacenamiento + "" + imagen_perfil + "_" + user.getUid();
        StorageReference storageReference2 = storageReference.child(Ruta_de_archivo_y_nombre);
        storageReference2.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        Uri downloadUri = uriTask.getResult();
                        if (uriTask.isSuccessful()){
                            HashMap<String, Object> results = new HashMap<>();
                            results.put(imagen_perfil,downloadUri.toString());
                            BASE_DE_DATOS_ADMINISTRADORES.child(user.getUid()).updateChildren(results)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(getActivity(), "Imagen cambiada con exito", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(getActivity(), MainActivityAdministrador.class));
                                            getActivity().finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            Toast.makeText(getActivity(), "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Este metodo se llamara despues de elegir la imagen de la camara de la galeria
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == CODIGO_DE_CAMARA_DE_SELECCION_DE_IMAGENES){
                ActualizarImagenEnBD(imagen_uri);
                progressDialog.setTitle("Procesando");
                progressDialog.setMessage("La imagen se esta cambiando, espere por favor ...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
            if (requestCode == CODIGO_DE_GALERIA_DE_SELECCION_DE_IMAGEN){
                imagen_uri = data.getData();
                ActualizarImagenEnBD(imagen_uri);
                progressDialog.setTitle("Procesando");
                progressDialog.setMessage("La imagen se esta cambiando, espere por favor ...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //Resultados de los permisos de solicitud
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CODIGO_DE_SOLICITUD_DE_CAMARA:
                if (grantResults.length > 0){
                    boolean Camara_Aceptada = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean EscribirAlmacenamiento_Aceptada = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (Camara_Aceptada && EscribirAlmacenamiento_Aceptada){
                        Elegir_De_Camara();
                    }else{
                        Toast.makeText(getActivity(), "Por favor acepte los permisos de camara y almacenamiento", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case CODIGO_DE_SOLICITUD_DE_ALMACENAMIENTO:
                if (grantResults.length > 0){
                    boolean EscribirAlmacenamiento_Aceptada = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(EscribirAlmacenamiento_Aceptada){
                        Elegir_De_Galeria();
                    }else{
                        Toast.makeText(getActivity(), "Por favor acepte los permisos almacenamiento", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}