package com.anfereba.Fondosdepantalla.CategoriasAdmin.VideoJuegosA;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.anfereba.Fondosdepantalla.CategoriasAdmin.PeliculasA.AgregarPelicula;
import com.anfereba.Fondosdepantalla.CategoriasAdmin.PeliculasA.PeliculasA;
import com.anfereba.Fondosdepantalla.CategoriasAdmin.SeriesA.AgregarSerie;
import com.anfereba.Fondosdepantalla.CategoriasAdmin.SeriesA.SeriesA;
import com.anfereba.Fondosdepantalla.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

public class VideoJuegosA extends AppCompatActivity {

    RecyclerView recyclerViewVideoJuegos;
    FirebaseDatabase mFirebaseDataBase;
    DatabaseReference mRef;

    FirebaseRecyclerAdapter<VideoJuego, ViewHolderVideojuegos> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<VideoJuego> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_juegos);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Videojuegos");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerViewVideoJuegos = findViewById(R.id.reciclerViewVideojuego);
        recyclerViewVideoJuegos.setHasFixedSize(true);

        mFirebaseDataBase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDataBase.getReference("VIDEOJUEGOS");

        ListarImagenesVideoJuego();

    }

    private void ListarImagenesVideoJuego() {

        options = new FirebaseRecyclerOptions.Builder<VideoJuego>().setQuery(mRef,VideoJuego.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<VideoJuego, ViewHolderVideojuegos>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderVideojuegos viewHolderVideojuegos, int i, @NonNull VideoJuego videojuego) {
                viewHolderVideojuegos.SeteoVideoJuegos(
                        getApplicationContext(),
                        videojuego.getNombre(),
                        videojuego.getVistas(),
                        videojuego.getImagen()
                );
            }

            @NonNull
            @Override
            public ViewHolderVideojuegos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                //Inflar el Item
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_videojuegos,parent, false);
                ViewHolderVideojuegos viewHolderVideojuegos = new ViewHolderVideojuegos(itemView);

                viewHolderVideojuegos.setOnClickListener(new ViewHolderVideojuegos.ClickListener() {
                    @Override
                    public void onItemClick(View view, int posicion) {
                        Toast.makeText(VideoJuegosA.this, "ITEM CLICK", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        //Toast.makeText(PeliculasA.this, "LONG CLICK", Toast.LENGTH_SHORT).show();

                        final String Nombre = getItem(position).getNombre();
                        final String Imagen = getItem(position).getImagen();

                        int Vista = getItem(position).getVistas();
                        String VistaString = String.valueOf(Vista);


                        AlertDialog.Builder builder = new AlertDialog.Builder(VideoJuegosA.this);
                        String[] opciones = {"Actualizar","Eliminar"};
                        builder.setItems(opciones, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0){
                                    //Toast.makeText(VideoJuegosA.this, "Actualizar", Toast.LENGTH_SHORT).show();

                                }
                                if (i == 1){
                                    EliminarDatos(Nombre,Imagen);
                                }

                            }
                        });

                        builder.create().show();

                    }
                });

                return viewHolderVideojuegos;
            }
        };
        recyclerViewVideoJuegos.setLayoutManager(new GridLayoutManager(VideoJuegosA.this,2));
        firebaseRecyclerAdapter.startListening();
        recyclerViewVideoJuegos.setAdapter(firebaseRecyclerAdapter);

    }

    private void EliminarDatos(final String NombreActual, final String ImagenActual){
        AlertDialog.Builder builder = new AlertDialog.Builder(VideoJuegosA.this);
        builder.setTitle("Eliminar");
        builder.setMessage("¿Desea eliminar imagen?");
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Eliminar Imagen de la BD//
                Query query = mRef.orderByChild("nombre").equalTo(NombreActual);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()){
                            ds.getRef().removeValue();
                        }
                        Toast.makeText(VideoJuegosA.this, "La imagen ha sido eliminada",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(VideoJuegosA.this, error.getMessage(),
                                Toast.LENGTH_SHORT).show();

                    }
                });

                StorageReference ImageSeleccionada = getInstance().getReferenceFromUrl(ImagenActual);
                ImageSeleccionada.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(VideoJuegosA.this, "Eliminado",
                                Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(VideoJuegosA.this, e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }

                });
            };
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(VideoJuegosA.this, "Cancelado por Administrador",
                        Toast.LENGTH_SHORT).show();
            }
        });

        builder.create().show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseRecyclerAdapter!=null){
            firebaseRecyclerAdapter.startListening();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_agregar,menu);
        menuInflater.inflate(R.menu.menu_vista,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.Agregar:
                startActivity(new Intent(VideoJuegosA.this, AgregarVideojuegos.class));
                finish();
                break;
            case R.id.Vista:
                Toast.makeText(this, "Listar Imagenes", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}