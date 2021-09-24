package com.anfereba.Fondosdepantalla.CategoriasAdmin.PeliculasA;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anfereba.Fondosdepantalla.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import static com.google.firebase.storage.FirebaseStorage.getInstance;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class AgregarPelicula extends AppCompatActivity {

    TextView VistaPeliculas;
    EditText NombrePeliculas;
    ImageView ImagenAgregarPelicula;
    Button PublicarPelicula;

    String RutaDeAlmacenamiento = "Pelicula_Subida/";
    String RutaDeBaseDeDatos = "Peliculas";
    Uri RutaArchivoUri;

    StorageReference mStorageReference;
    DatabaseReference DatabaseReference;

    ProgressDialog progressDialog;

    String rNombre, rImagen, rVista;

    int CODIGO_DE_SOLICITUD_IMAGEN = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_pelicula);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Publicar");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        VistaPeliculas = findViewById(R.id.VistaPeliculas);
        NombrePeliculas = findViewById(R.id.NombrePeliculas);
        ImagenAgregarPelicula = findViewById(R.id.ImagenAgregarPelicula);
        PublicarPelicula = findViewById(R.id.PublicarPelicula);

        mStorageReference = FirebaseStorage.getInstance().getReference();
        DatabaseReference = FirebaseDatabase.getInstance().getReference(RutaDeBaseDeDatos);
        progressDialog = new ProgressDialog(AgregarPelicula.this);

        Bundle intent = getIntent().getExtras();
        if (intent != null){
            //Recuperar los datos de la actividad anterior
            rNombre = intent.getString("NombreEnviado");
            rImagen = intent.getString("ImagenEnviada");
            rVista = intent.getString("VistaEnviada");

            //setear
            NombrePeliculas.setText(rNombre);
            VistaPeliculas.setText(rVista);
            Picasso.get().load(rImagen).into(ImagenAgregarPelicula);

            //Cambiar el nombre actionbar
            actionBar.setTitle("Actualizar");
            String actualizar = "Actualizar";
            //Cambiar el nombre del boton
            PublicarPelicula.setText(actualizar);
        }

        ImagenAgregarPelicula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Seleccionar imagen"),CODIGO_DE_SOLICITUD_IMAGEN);
            }
        });

        PublicarPelicula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PublicarPelicula.getText().equals("Publicar")){
                    SubirImagen();
                }else{
                    EmpezarActualizacion();
                }

            }
        });
    }

    private void EmpezarActualizacion() {
        progressDialog.setTitle("Actualizando");
        progressDialog.setMessage("Espere por favor ...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        EliminarImagenAnterior();
    }

    private void EliminarImagenAnterior() {
        StorageReference Imagen = getInstance().getReferenceFromUrl(rImagen);
        Imagen.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //Si la imagen se elimino
                Toast.makeText(AgregarPelicula.this, "La imagen anterior ha sido eliminada"
                        , Toast.LENGTH_SHORT).show();
                SubirNuevaImagen();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AgregarPelicula.this, "Wey" + e.getMessage()
                        , Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                
            }
        });
    }

    private void SubirNuevaImagen() {
        String nuevaImagen = System.currentTimeMillis()+".png";
        StorageReference mStorageReference2  = mStorageReference.child(RutaDeAlmacenamiento + nuevaImagen);
        Bitmap bitmap = ((BitmapDrawable)ImagenAgregarPelicula.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        byte [] data = byteArrayOutputStream.toByteArray();
        UploadTask uploadTask = mStorageReference2.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AgregarPelicula.this, "Nueva Imagen Cargada", Toast.LENGTH_SHORT).show();
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                Uri downloadUri = uriTask.getResult();
                ActualizarImagenBD(downloadUri.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AgregarPelicula.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void ActualizarImagenBD(String NuevaImagen) {
        final String nombreActualizar = NombrePeliculas.getText().toString();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Peliculas");

        //Consulta

        Query query = databaseReference.orderByChild("nombre").equalTo(rNombre);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Datos a Actualizar
                for (DataSnapshot ds : snapshot.getChildren()){
                    ds.getRef().child("nombre").setValue(nombreActualizar);
                    ds.getRef().child("imagen").setValue(NuevaImagen);
                }
                progressDialog.dismiss();
                Toast.makeText(AgregarPelicula.this, "Actualizado Correctamente", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AgregarPelicula.this, PeliculasA.class));
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SubirImagen() {
        if (RutaArchivoUri!=null){
            progressDialog.setTitle("Espere por favor");
            progressDialog.setMessage("Subiendo Imagen PELICULA ...");
            progressDialog.show();
            progressDialog.setCancelable(false);
            StorageReference mStorageReference2 = mStorageReference.child(
                    RutaDeAlmacenamiento+System.currentTimeMillis()+"."+ObtenerExtensionDelArchivo(RutaArchivoUri));
            //20210204_1234.PNG
            mStorageReference2.putFile(RutaArchivoUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful());
                                Uri downloadURI = uriTask.getResult();
                                String mNombre = NombrePeliculas.getText().toString();
                                String mVista = VistaPeliculas.getText().toString();
                                int VISTA = Integer.parseInt(mVista);

                                Pelicula pelicula = new Pelicula(downloadURI.toString(),mNombre,VISTA);
                                String ID_IMAGEN = DatabaseReference.push().getKey();
                                DatabaseReference.child(ID_IMAGEN).setValue(pelicula);
                                progressDialog.dismiss();
                                Toast.makeText(AgregarPelicula.this, "Subido Exitosamente", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AgregarPelicula.this,PeliculasA.class));
                                finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(AgregarPelicula.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.setTitle("Publicando");
                    progressDialog.setCancelable(false);
                }
            });


        }
        else{
            Toast.makeText(this, "DEBE ASIGNAR UNA IMAGEN", Toast.LENGTH_SHORT).show();
        }
    }

    //Obtenemos la extension

    private String ObtenerExtensionDelArchivo(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    //Comprobar si la imagen seleccionada por el administrador fue correcta
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODIGO_DE_SOLICITUD_IMAGEN
                && resultCode == RESULT_OK
                && data!=null
                && data.getData() != null){

            RutaArchivoUri = data.getData();
            try {
                //Convertimos a bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),RutaArchivoUri);
                //seteamos la imagen
                ImagenAgregarPelicula.setImageBitmap(bitmap);

            }catch (Exception e){
                Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }
    }
}