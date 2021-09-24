package com.anfereba.Fondosdepantalla.CategoriasAdmin.PeliculasA;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anfereba.Fondosdepantalla.R;
import com.squareup.picasso.Picasso;

public class ViewHolderPelicula extends RecyclerView.ViewHolder {

    View mView;

    private ViewHolderPelicula.ClickListener mClickListener;

    public interface ClickListener{
        void onItemClick(View view, int posicion); /*Admin Presiona Normal el item */
        void onItemLongClick(View view, int position); /*Admin Mantiene Presionado el item */
    }

    //Metodo para poder presionar o mantener presionado un item

    public void setOnClickListener(ViewHolderPelicula.ClickListener clickListener){
        mClickListener = clickListener;

    }
    public ViewHolderPelicula(@NonNull View itemView) {
        super(itemView);
        mView = itemView;


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view,getAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemLongClick(view,getAdapterPosition());
                return true;
            }
        });

    }

    public void SeteoPeliculas(Context context, String nombre, int vista, String imagen) {
        ImageView ImagenPelicula;
        TextView NombreImagenPelicula;
        TextView VistaPelicula;

        //Conexion con el item

        ImagenPelicula = mView.findViewById(R.id.ImagenPelicula);
        NombreImagenPelicula = mView.findViewById(R.id.NombreImagenPelicula);
        VistaPelicula = mView.findViewById(R.id.VistaPelicula);

        NombreImagenPelicula.setText(nombre);

        //Convertir a string el parametro vista
        String VistaString = String.valueOf(vista);
        VistaPelicula.setText(VistaString);

        //Controlar posibles errores
        try {
            //Si la imagen fue traida exitosamente
            Picasso.get().load(imagen).into(ImagenPelicula);

        }catch (Exception e){
            //Si la imagen no fue traida exitosamente
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

        }


    }
}
