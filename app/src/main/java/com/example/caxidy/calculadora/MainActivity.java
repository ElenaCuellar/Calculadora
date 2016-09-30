package com.example.caxidy.calculadora;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    //Variables que no son para View, sino para el funcionamiento
    protected String operador, op1, op2, memoria;
    protected double operando1, operando2, resultado, resMemoria;
    protected boolean primero, tieneComa;
    protected DecimalFormat formato;
    //Variables para señalar los View
    protected TextView pant, not;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Inicializar variables
        operador=op1=op2="";
        memoria=null;
        resultado=operando1=operando2=resMemoria=0;
        primero=tieneComa=false;
        formato = new DecimalFormat("####.####");
        //Apuntar a elementos View
        pant = (TextView)findViewById(R.id.pantalla);
        not = (TextView)findViewById(R.id.notas);
    }
    //Metodos que se llaman al pulsar los botones:
    public void pulsaNumero(View v){ //Coge el texto del boton. Es decir, selecciona el numero.
        Button num = (Button) findViewById(v.getId());
        String numero = num.getText().toString();
        //Guardar el numero en una variable String
        if(primero)
            op2= agregarCifra(op2,numero);
        else
            op1= agregarCifra(op1,numero);
    }
    public void pulsaOperador(View v){
        try {
            Button bOper = (Button) findViewById(v.getId()); //Selecciona el operador que se ha pulsado...
            operador = bOper.getText().toString(); //y lo almacena en la variable
            operando1 = Double.parseDouble(op1); //Pasa a double el primer operando
            primero = true; //Se ha ocupado el primer operando
        }catch(NumberFormatException e){not.setText(R.string.notif);}
    }
    public void pulsaComa(View v){
        if(primero)
            op2=agregarCifra(op2,".");
        else
            op1= agregarCifra(op1,".");
    }
    public void pulsaC(View v){ //Se devuelven las variables a su estado inicial
        pant.setText("0.0");
        primero=false;
        op1 = ""; //Si lo ponemos dentro de limpiar() no podemos concatenar operaciones.
        limpiar();
    }
    public void pulsaIgual(View v){
        tieneComa=false;
        if(primero) //Por si solo metemos una cifra y pulsamos =, que no intente coger el segundo operando
            operando2=Double.parseDouble(op2); //Pasa a double el segundo operando
        //Opera...
        try{
            switch(operador){
                case "+": resultado=operando1+operando2; break;
                case "-": resultado=operando1-operando2; break;
                case "*": resultado=operando1*operando2; break;
                case "/":
                    if((int)operando1==0 || (int)operando2==0) //Controla la division entre 0
                        resultado=0;
                    else
                        resultado=operando1/operando2;
                    break;
                default:
                    operando1=Double.parseDouble(op1); //Pasa a double el primer operando
                    resultado=operando1;
                    break;
            }
            resMemoria = resultado; //Guardamos el resultado en double por si lo queremos meter en memoria
            if(Double.toString(resultado).contains("."))  //Comprobamos si el resultado tiene coma para controlar el guardado en memoria
                tieneComa=true;
            pant.setText(formato.format(resultado)); //Se le aplica el formato para redondear decimales y pasar la coma al idioma del SO
            primero=false; //Se vacian los operandos, por lo que la primera posicion esta libre
            op1=Double.toString(resultado); //Se pasa el resultado al primer operando para tener la posibilidad de concatenar la operacion.
            //Comprobar si se ha añadido la coma decimal y el 0 (al ser double) a un numero que no tenia decimal y eliminarlo...
            String resDec = op1.substring(op1.indexOf('.')+1,op1.length());
            if(resDec.equals("0"))
                op1=op1.substring(0,op1.indexOf('.'));
            limpiar();
        }catch(NumberFormatException nfe){not.setText(R.string.notif);}
    }
    public void pulsaMemoria(View v){ //Guardar un numero en pantalla en memoria (resultado anterior, operando...)
        String pantalla = pant.getText().toString();
        if(pantalla.equals("")) {
            memoria = "0";
            not.setText(R.string.errorGuardar);
        }
        else {
            memoria = pantalla;
            //Cambiar la coma del resultado por el punto decimal para poder operar
            if(!memoria.contains(".") && tieneComa)
                memoria = Double.toString(resMemoria);
            not.setText(R.string.mensjGuardar);
        }
        resMemoria=0;
    }
    public void pulsaRecuperar(View v){ //Recuperar el numero guardado en memoria para operar con el
        if(memoria!=null){
            if(primero) {
                op2 = memoria;
                pant.setText(op2);
            }
            else {
                op1 = memoria;
                pant.setText(op1);
            }
        }
        else
            not.setText(R.string.errorRecup);
    }
    //Metodos de funcionamiento
    public String agregarCifra(String op,String n){ //Para añadir cifras a un operando
        if(op.contains(".") && n.equals(".")) //Para no poner dos comas o mas
            not.setText(R.string.errorComa);
        else {
            op += n;
            pant.setText(op); //Mostrar numero por pantalla
        }
        return op; //devolvemos el numero resultante a la variable
    }
    public void limpiar(){
        operando1=operando2=0;
        operador=op2="";
        resultado=0;
        not.setText("");
    }
}