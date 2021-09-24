package com.anfereba.Fondosdepantalla.CategoriasAdmin.MusicaA;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.anfereba.Fondosdepantalla.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MusicaA extends AppCompatActivity {

    RecyclerView recyclerViewMusica;
    FirebaseDatabase mFirebaseDataBase;
    DatabaseReference mRef;

    FirebaseRecyclerAdapter<Musica, ViewHolderMusica> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Musica> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musica);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Musica");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerViewMusica = findViewById(R.id.reciclerViewMusica);
        recyclerViewMusica.setHasFixedSize(true);

        mFirebaseDataBase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDataBase.getReference("MUSICA");

        ListarImagenesMusica();
    }

    private void ListarImagenesMusica() {

        options = new FirebaseRecyclerOptions.Builder<Musica>().setQuery(mRef,Musica.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Musica, ViewHolderMusica>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderMusica viewHolderMusica, int i, @NonNull Musica musica) {
                viewHolderMusica.SeteoMusica(
                        getApplicationContext(),
                        musica.getNombre(),
                        musica.getVistas(),
                        musica.getImagen()
                );
            }

            @NonNull
            @Override
            public ViewHolderMusica onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                //Inflar el Item
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_musica,parent, false);
                ViewHolderMusica viewHolderMusica = new ViewHolderMusica(itemView);

                viewHolderMusica.setOnClickListener(new ViewHolderMusica.ClickListener() {
                    @Override
                    public void onItemClick(View view, int posicion) {
                        Toast.makeText(MusicaA.this, "ITEM CLICK", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        Toast.makeText(MusicaA.this, "LONG CLICK", Toast.LENGTH_SHORT).show();

                    }
                });

                return viewHolderMusica;
            }
        };
        recyclerViewMusica.setLayoutManager(new GridLayoutManager(MusicaA.this,2));
        firebaseRecyclerAdapter.startListening();
        recyclerViewMusica.setAdapter(firebaseRecyclerAdapter);

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
                //Toast.makeText(this, "Agregar Imagen", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MusicaA.this,AgregarMusica.class));
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