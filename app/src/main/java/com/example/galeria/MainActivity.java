package com.example.galeria;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //elementos visuales
    EditText cajaNombre,cajaPass;
    Button entrar;
    TextView registrarUsser;

    SharedPreferences crearArchivo;
    Editor escritorPref;
    //Variables estaticas guardare claves e identificadores
    static String idArchivo = "registro";//ID de coleccion
    static String nombreUs = "nombreUsu";
    static String passUs = "passUsu";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //entrada de botones
        registrarUsser = findViewById(R.id.register);
        entrar = findViewById(R.id.login);
        //entradas de texto
        cajaNombre=findViewById(R.id.nameBox);
        cajaPass=findViewById(R.id.passBox);

        crearArchivo = getSharedPreferences(idArchivo,MODE_PRIVATE);//id de coleccion y modo de acceso
        escritorPref = crearArchivo.edit();

        registrarUsser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                escritorPref.putString(nombreUs,cajaNombre.getText().toString());
                escritorPref.putString(passUs,cajaPass.getText().toString());
                escritorPref.commit();
                Toast.makeText(getApplicationContext(),"Datos registrados",Toast.LENGTH_SHORT).show();
            }
        });
        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compare(crearArchivo.getString(nombreUs,"NO VALUE"),crearArchivo.getString(passUs,"NO VALUE"));
            }
        });
    }
    protected void compare(String us,String ps){
        //Se almacenan los valores de compareTo valores enteros
        Integer var1 = us.compareTo(cajaNombre.getText().toString());
        Integer var2 = ps.compareTo(cajaPass.getText().toString());

        try {
            if (var1==0 && var2 ==0){
                Toast.makeText(getApplicationContext(),"ACCESO CONCEDIDO",Toast.LENGTH_SHORT).show();
                Intent menu = new Intent(getApplicationContext(),mainMenu.class);
                startActivity(menu);
            }
            else
                Toast.makeText(getApplicationContext(),"ACCESO DENEGADO",Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"NO FUNCIONO",Toast.LENGTH_SHORT).show();
        }
    }
}