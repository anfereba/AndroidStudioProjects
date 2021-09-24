package com.anfereba.Fondosdepantalla.CategoriasAdmin.VideoJuegosA;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anfereba.Fondosdepantalla.R;
import com.squareup.picasso.Picasso;

public class ViewHolderVideojuegos extends RecyclerView.ViewHolder{

    View mView;

    private ViewHolderVideojuegos.ClickListener mClickListener;

    public interface ClickListener{
        void onItemClick(View view, int posicion); /*Admin Presiona Normal el item */
        void onItemLongClick(View view, int position); /*Admin Mantiene Presionado el item */
    }

    //Metodo para poder presionar o mantener presionado un item

    public void setOnClickListener(ViewHolderVideojuegos.ClickListener clickListener){
        mClickListener = clickListener;

    }
    public ViewHolderVideojuegos(@NonNull View itemView) {
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

    public void SeteoVideoJuegos(Context context, String nombre, int vista, String imagen) {
        ImageView Imagen_VideoJuegos;
        TextView NombreImagen_VideoJuegos;
        TextView Vista_VideoJuegos;

        //Conexion con el item

        Imagen_VideoJuegos = mView.findViewById(R.id.Imagen_VideoJuegos);
        NombreImagen_VideoJuegos = mView.findViewById(R.id.NombreImagen_VideoJuegos);
        Vista_VideoJuegos = mView.findViewById(R.id.Vista_VideoJuegos);

        NombreImagen_VideoJuegos.setText(nombre);

        //Convertir a string el parametro vista
        String VistaString = String.valueOf(vista);
        Vista_VideoJuegos.setText(VistaString);

        //Controlar posibles errores
        try {
            //Si la imagen fue traida exitosamente
            Picasso.get().load(imagen).into(Imagen_VideoJuegos);

        }catch (Exception e){
            //Si la imagen no fue traida exitosamente
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

        }


    }
}
