package com.anfereba.Fondosdepantalla.CategoriasAdmin.SeriesA;

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

public class SeriesA extends AppCompatActivity {

    RecyclerView recyclerViewSerie;
    FirebaseDatabase mFirebaseDataBase;
    DatabaseReference mRef;

    FirebaseRecyclerAdapter<Serie, ViewHolderSerie> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Serie> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Series");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerViewSerie = findViewById(R.id.reciclerViewSerie);
        recyclerViewSerie.setHasFixedSize(true);

        mFirebaseDataBase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDataBase.getReference("Series");

        ListarImagenesSerie();

    }

    private void ListarImagenesSerie() {
        options = new FirebaseRecyclerOptions.Builder<Serie>().setQuery(mRef,Serie.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Serie, ViewHolderSerie>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderSerie viewHolderSerie, int i, @NonNull Serie serie) {
                viewHolderSerie.SeteoSeries(
                        getApplicationContext(),
                        serie.getNombre(),
                        serie.getVistas(),
                        serie.getImagen()
                );
            }

            @NonNull
            @Override
            public ViewHolderSerie onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                //Inflar el Item
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_serie,parent, false);
                ViewHolderSerie viewHolderSerie = new ViewHolderSerie(itemView);

                viewHolderSerie.setOnClickListener(new ViewHolderSerie.ClickListener() {
                    @Override
                    public void onItemClick(View view, int posicion) {
                        Toast.makeText(SeriesA.this, "ITEM CLICK", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        Toast.makeText(SeriesA.this, "LONG CLICK", Toast.LENGTH_SHORT).show();

                    }
                });

                return viewHolderSerie;
            }
        };
        recyclerViewSerie.setLayoutManager(new GridLayoutManager(SeriesA.this,2));
        firebaseRecyclerAdapter.startListening();
        recyclerViewSerie.setAdapter(firebaseRecyclerAdapter);
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
                startActivity(new Intent(SeriesA.this,AgregarSerie.class));
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