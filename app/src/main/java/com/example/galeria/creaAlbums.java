package com.example.galeria;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class creaAlbums extends AppCompatActivity {
    Button insertarAlbum;
    EditText nombreAlbum;
    ListView listaAlbums;

    //Conectores de bases de datos
    AdminSQL admin;
    SQLiteDatabase BD_connect;
    ContentValues escritor;
    //Variables
    static String DB_NAME = "/storage/emulated/0/coleccion.sqlite";
    Cursor lector;

    //adaptadores
    ArrayAdapter<String> adapterList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crea_albums);

        insertarAlbum=findViewById(R.id.creaAlbum);
        nombreAlbum=findViewById(R.id.nombraAlb);
        listaAlbums = findViewById(R.id.listaAlbum);

        adapterList = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1);
        listaAlbums.setAdapter(adapterList);
        consulta();
        insertarAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapterList.clear();//limpiamos para que no se repitan los valores :D
                admin = new AdminSQL(getApplicationContext(),DB_NAME,null,1);
                BD_connect = admin.getWritableDatabase();
                escritor = new ContentValues();

                if (BD_connect!=null){
                    escritor.put("nombre_alb",nombreAlbum.getText().toString());
                    BD_connect.insert("album",null,escritor);
                    consulta();
                    BD_connect.close();
                    Toast.makeText(getApplicationContext(),"Album Guardado",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    protected void consulta(){
        admin=new AdminSQL(getApplicationContext(),DB_NAME,null,1);
        BD_connect = admin.getReadableDatabase();
        lector = BD_connect.rawQuery("select * from album",null);
        while (lector.moveToNext()){
            adapterList.add(lector.getString(0));
        }
        adapterList.notifyDataSetChanged();
        nombreAlbum.setText("");
    }
}