package com.example.galeria;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class mainMenu extends AppCompatActivity {
    Button lanzaAlbum,launchImg,launchVisor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        lanzaAlbum = findViewById(R.id.creaAlbum);
        launchImg = findViewById(R.id.nombraImg);
        launchVisor = findViewById(R.id.verImg);

        lanzaAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Lanzar a el menu de crear un album
                Intent creaAlbum = new Intent(getApplicationContext(),creaAlbums.class);
                startActivity(creaAlbum);
            }
        });
        launchImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent selimg = new Intent(getApplicationContext(),addImage.class);
                startActivity(selimg);
            }
        });
        launchVisor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //lanzar el visor de imagenes
                Intent verimg = new Intent(getApplicationContext(),visorImg.class);
                startActivity(verimg);
            }
        });

    }
}