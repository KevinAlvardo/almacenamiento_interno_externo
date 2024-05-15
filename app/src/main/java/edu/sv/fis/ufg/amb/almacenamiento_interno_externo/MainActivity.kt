package edu.sv.fis.ufg.amb.almacenamiento_interno_externo

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import android.Manifest
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    val WRITE_EXTERNAL_STORAGE_REQUEST_CODE =101
    lateinit var texto: EditText
    lateinit var boton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        escrituraArchivosAlmacenamienyoInterno(this,"archivo_almacenamiento_interno.txt", " ESTE ES UN CONTENIDO EN EL ALMACENAMIENYO INTERNO ")
        escrituraArchivosAlmacenamienyoInterno(this,"archivo_almacenamiento_externo.txt", " ESTE ES UN CONTENIDO EN EL ALMACENAMIENYO EXTERNO ")


        boton=findViewById<Button>(R.id.btn_guardar)
        texto=findViewById<EditText>(R.id.txt_data)

        boton.setOnClickListener{
            escrituraArchivosAlmacenamienyoInterno(this,"archivo_con_valor_campo_texte.txt",texto.text.toString())
        }
    }


    fun escrituraArchivosAlmacenamienyoInterno(context: Context,fileName: String,content: String){

        val filepath=context.filesDir.absolutePath+"/$fileName"
        val file = File(filepath)

        try {
            file.writeText(content)
            Log.v("ESCRITURA EN ALMACENAMIENTO INTERNO", "RUTA: '${filepath}'");
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }    }

    fun escrituraArchivosAlmacenamienyoExterno(context:Context, fileName:String,content:String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val filePath =
                context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)!!.absolutePath + "/$fileName"
            val file = File(filePath)

            try {
                file.writeText(content)
                Log.v("ESCRITURA EN ARCHIVO", "RUTA: '${filePath}'");
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            val filepath = context.getExternalFilesDir(null)!!.absolutePath + "/$fileName"
            val file = File(filepath)

            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {

                try {
                    file.writeText(content)
                    Log.v("ESCRITURA EN ARCHIVO", "RUTA: '${filepath}'");
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }else {
                val WRITE_EXTERNAL_STORAGE_REQUEST_CODE= 101
                ActivityCompat.requestPermissions(
                    (context as Activity),
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    WRITE_EXTERNAL_STORAGE_REQUEST_CODE)
            }
        }
    }