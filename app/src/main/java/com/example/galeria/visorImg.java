package com.example.galeria;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class visorImg extends AppCompatActivity {
    Button verimagen;
    Spinner albumes,nombresImg;
    ImageView imagenNew;

    //Adaptadores
    ArrayAdapter<String> adapterAlbum;
    ArrayAdapter<String> adapterNombre;

    //Sql Tools
    AdminSQL admin;
    SQLiteDatabase BD_con;
    Cursor fila;
    static String DB_NAME = "/storage/emulated/0/coleccion.sqlite";

    //Variables de datos
    String nombreSelect;
    String rutaSelect;
    String albumSelect;
    Bitmap mapaBits=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visor_img);

        imagenNew = findViewById(R.id.imgResult);
        verimagen = findViewById(R.id.verImg);
        albumes = findViewById(R.id.listAlbumes);
        nombresImg = findViewById(R.id.listNames);

        adapterAlbum = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item);
        albumes.setAdapter(adapterAlbum);
        //seteando adaptadores
        adapterNombre = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item);
        nombresImg.setAdapter(adapterNombre);

        consulta();
        albumes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                adapterNombre.clear();
                selectNames(adapterAlbum.getItem(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        nombresImg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                nombreSelect = adapterNombre.getItem(i);
                if(nombreSelect!=null){
                    consultaRuta(nombreSelect);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                imagenNew.setImageResource(R.drawable.engrane);
            }
        });
    }
    protected void selectNames(String album){
        admin = new AdminSQL(getApplicationContext(),DB_NAME,null,1);
        BD_con = admin.getReadableDatabase();
        fila = BD_con.rawQuery("select nombreImg from imagen where album ='"+ album + "'",null);

        while (fila.moveToNext()){
            adapterNombre.add(fila.getString(0));
        }
        admin.close();
        adapterNombre.notifyDataSetChanged();
    }
    protected void consulta(){ //de los albumes
        admin=new AdminSQL(getApplicationContext(),DB_NAME,null,1);
        BD_con = admin.getReadableDatabase();
        fila = BD_con.rawQuery("select * from album",null);
        while (fila.moveToNext()){
            adapterAlbum.add(fila.getString(0));
        }
        admin.close();
        adapterAlbum.notifyDataSetChanged();
    }
    protected  void consultaRuta(String nombrefoto){
        admin = new AdminSQL(getApplicationContext(),DB_NAME,null,1);
        BD_con = admin.getReadableDatabase();

        fila= BD_con.rawQuery("SELECT * FROM imagen WHERE nombreImg = '"+ nombrefoto +"'",null);
        while (fila.moveToNext()){
            rutaSelect = fila.getString(2);
            mapaBits = redimensionarImagen(rutaSelect);
            imagenNew.setImageBitmap(mapaBits);
        }
        admin.close();

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
    /*protected  void consultaMulti(String nombre,String alb){
        admin = new AdminSQL(getApplicationContext(),DB_NAME,null,1);
        BD_con = admin.getReadableDatabase();
        fila = BD_con.rawQuery("SELECT * FROM imagen WHERE nombreImg = '"+ nombre +"'")
    }*/

}