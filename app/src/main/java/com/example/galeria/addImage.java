package com.example.galeria;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class addImage extends AppCompatActivity {
    private static final int REQUEST_SELECT_PHOTO = 1;
    ImageView mainImg;
    Button guardarImagen;
    Spinner listaAlbumes;
    EditText nombreImagen;

    //SQL Elements
    AdminSQL admin;
    SQLiteDatabase BD_con;
    ContentValues datos;
    static String DB_NAME = "/storage/emulated/0/coleccion.sqlite";
    Cursor lector;

    //Spinner elements to adapt data
    ArrayAdapter<String> adaptador;

    //Variables de operacion
    String albumSl;
    String rutaImg;
    Bitmap mapaBits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);

        nombreImagen = findViewById(R.id.nombreImagen);
        mainImg = findViewById(R.id.imagenPrincipal);
        mainImg.setImageResource(R.drawable.engrane);
        guardarImagen = findViewById(R.id.guardarImg);
        listaAlbumes = findViewById(R.id.listaAlbums);

        adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item);
        listaAlbumes.setAdapter(adaptador);
        consultarAlbums();

        listaAlbumes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //variable para guardar el titulo del album.
                albumSl=adaptador.getItem(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mainImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallExplorer();
            }
        });
        guardarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertarDatos(nombreImagen.getText().toString(),albumSl,rutaImg);
                Toast.makeText(getApplicationContext(),"Imagen Guardada",Toast.LENGTH_SHORT).show();
                
                nombreImagen.setText("");
                mainImg.setImageResource(R.drawable.engrane);
            }
        });

    }
    int contador=0;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_SELECT_PHOTO:
                if( resultCode != 0 ) {
                    Cursor c = managedQuery(data.getData(),null,null,null,null);
                    if( c.moveToFirst() ) {
                        int i = 0;
                        //CajaTexto.setText(String.valueOf(contador));
                        mapaBits =null;
                        //Ciclo while, diversidad de dispositivos
                        while (mapaBits==null){
                            rutaImg = c.getString(i);
                            mapaBits = redimensionarImagen(rutaImg);
                            mainImg.setImageBitmap(mapaBits);
                            i++;
                        }
                        ////////////////////////////////////////////
                        //contador+=1;
                    }
                }
            default: break;
        }
    }
    public Bitmap redimensionarImagen(String absolutePath){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(absolutePath, options);
        int imageWidth = options.outWidth;
        int imageHeight = options.outHeight;
        int ratio;
        options.inJustDecodeBounds = false;
        if (imageWidth>imageHeight) {
            ratio = imageWidth/250;
        }
        else {
            ratio = imageHeight/250;
        }
        options.inSampleSize = ratio;
        return BitmapFactory.decodeFile(absolutePath, options);
    }
    private void CallExplorer(){
        android.content.Intent ii = new Intent(android.content.Intent.ACTION_PICK);
        ii.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(ii,REQUEST_SELECT_PHOTO);
    }
    private void consultarAlbums(){
        admin = new AdminSQL(getApplicationContext(),DB_NAME,null,1);
        BD_con = admin.getReadableDatabase();

        lector= BD_con.rawQuery("SELECT * FROM album",null);
        while (lector.moveToNext()){
            adaptador.add(lector.getString(0));
        }
        adaptador.notifyDataSetChanged();
    }

    private void insertarDatos(String nom,String album,String ruta){
        admin= new AdminSQL(this,DB_NAME,null,1);
        BD_con = admin.getWritableDatabase();
        datos = new ContentValues();
        datos.put("nombreImg",nom);
        datos.put("album",album);
        datos.put("ruta",ruta);
        BD_con.insert("imagen",null,datos);
    }
}