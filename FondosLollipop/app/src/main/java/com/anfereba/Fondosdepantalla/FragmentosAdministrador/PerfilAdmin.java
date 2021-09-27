package com.anfereba.Fondosdepantalla.FragmentosAdministrador;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.anfereba.Fondosdepantalla.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class PerfilAdmin extends Fragment {

    //Firebase

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference BASE_DE_DATOS_ADMINISTRADORES;

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




        return view;
    }
}