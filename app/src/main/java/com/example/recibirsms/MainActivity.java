package com.example.recibirsms;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static MainActivity instance;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    public static MainActivity getInstance(){

        return instance;

    }

    // método para actualizar el EditText

    public void actualizarEditText(final String texto){

        MainActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                EditText textV1 = (EditText) findViewById(R.id.editText);
                textV1.setText(texto);
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;


        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if(result){
                    // El permiso ha sido concedido
                    // Llamamos al método que utiliza dichos permisos

                    Log.d("Events","Estamos en onActivityResult true");
                }

                else{

                    // Explicar al usuario que la acción a realizar no está disponible
                    // porque requiere de un permiso que ha denegado. Hay que respetar la
                    // decisión del usuario, así que no le spamees hasta que lo acepte.

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                    alertDialogBuilder.setTitle("Permiso denegado correctamente...");
                    alertDialogBuilder.setMessage("Sin este permiso no se podrá mandar el SMS");
                    alertDialogBuilder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        // dialog -> dialogo actual
                        // which -> botón pulsado: DialogInterface.BUTTON_POSITIVE
                        public void onClick(DialogInterface dialog, int which) {

                            Toast.makeText(MainActivity.this,"Descarga cancelada...",Toast.LENGTH_SHORT).show();

                        }
                    });

                    alertDialogBuilder.create().show();
                    Log.d("Events","Estamos en onActivityResult false");

                }
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
            requestPermissionLauncher.launch(Manifest.permission.RECEIVE_SMS);
        }
    }


}