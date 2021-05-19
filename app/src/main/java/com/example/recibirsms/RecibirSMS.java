package com.example.recibirsms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecibirSMS extends BroadcastReceiver {

    public static String TELEFONO_EMISOR = "5556";

    // tenemos dos cosas:
    // 1-> contexto en el que el receptor se está ejecutando
    // 2-> intent con los datos
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle extras = intent.getExtras();

        SmsMessage[] msgs;

        String emisor;
        String mensaje = "";
        String regexp = "(\\d{10})$"; // número de 10 dígitos y al final
        Pattern patron = Pattern.compile(regexp);
        String codigo = "";

        String format = extras.getString("format");

        boolean isVersionM = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);

        if(extras != null){

            Object[] pdus = (Object[]) extras.get("pdus");
            msgs = new SmsMessage[pdus.length];

            for(int i=0; i<msgs.length;i++){

                if(isVersionM){

                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);

                }
                else{
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);

                }

                // Comprobamos si el mensaje proviene del telefono esperado
                // En el caso de que provenga, tendremos que utilizar lo
                // visto en AAD

                emisor = msgs[i].getOriginatingAddress();

                Toast.makeText(context, emisor, Toast.LENGTH_LONG).show();
                TELEFONO_EMISOR = emisor;

                if(emisor.equals(TELEFONO_EMISOR)){

                    mensaje = msgs[i].getMessageBody();
                    Matcher matcher = patron.matcher(mensaje);

                    if(matcher.find()){
                        codigo = matcher.group(1);
                        MainActivity mainActivity = MainActivity.getInstance();
                        mainActivity.actualizarEditText(codigo);
                    }

                }

            }
        }

    }
}
