package edu.sv.fis.ufg.amb.almacenamiento_interno_externo

// Importaciones necesarias para la funcionalidad de la aplicación
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File

// Declaración de la clase principal de la actividad
class MainActivity : AppCompatActivity() {
    private val WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 101  // Código de solicitud para el permiso de escritura en almacenamiento externo
    private lateinit var texto: EditText  // Declaración del campo de texto
    private lateinit var boton: Button  // Declaración del botón

    // Método onCreate: punto de entrada de la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)  // Establece el layout de la actividad

        // Inicialización de los componentes de la interfaz de usuario
        boton = findViewById(R.id.btn_guardar)
        texto = findViewById(R.id.txt_data)

        // Escribe contenido en un archivo en el almacenamiento interno al iniciar la actividad
        escrituraArchivosAlmacenamientoInterno(
            this,
            "archivo_almacenamiento_interno.txt",
            "ESTE ES UN CONTENIDO EN EL ALMACENAMIENTO INTERNO"
        )

        // Escribe contenido en un archivo en el almacenamiento externo al iniciar la actividad
        escrituraArchivosAlmacenamientoExterno(
            this,
            "archivo_almacenamiento_externo.txt",
            "ESTE ES UN CONTENIDO EN EL ALMACENAMIENTO EXTERNO"
        )

        // Configura el listener del botón para escribir el contenido del campo de texto en un archivo
        boton.setOnClickListener {
            escrituraArchivosAlmacenamientoInterno(
                this,
                "archivo_con_valor_campo_texto.txt",
                texto.text.toString()
            )
        }
    }

    // Función para escribir en un archivo en el almacenamiento interno
    private fun escrituraArchivosAlmacenamientoInterno(context: Context, fileName: String, content: String) {
        val filepath = context.filesDir.absolutePath + "/$fileName"  // Ruta completa del archivo
        val file = File(filepath)

        try {
            file.writeText(content)  // Escribe el contenido en el archivo
            Log.v("ESCRITURA EN ALMACENAMIENTO INTERNO", "RUTA: '$filepath'")  // Log para verificar la ruta del archivo
        } catch (e: Exception) {
            e.printStackTrace()  // Manejo de excepciones
        }
    }

    // Función para escribir en un archivo en el almacenamiento externo
    private fun escrituraArchivosAlmacenamientoExterno(context: Context, fileName: String, content: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Para Android Q y versiones superiores
            val filePath = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)!!.absolutePath + "/$fileName"
            val file = File(filePath)

            try {
                file.writeText(content)  // Escribe el contenido en el archivo
                Log.v("ESCRITURA EN ARCHIVO", "RUTA: '$filePath'")  // Log para verificar la ruta del archivo
            } catch (e: Exception) {
                e.printStackTrace()  // Manejo de excepciones
            }
        } else {
            // Para versiones anteriores a Android Q
            val filepath = context.getExternalFilesDir(null)!!.absolutePath + "/$fileName"
            val file = File(filepath)

            // Verifica si el permiso de escritura en almacenamiento externo está concedido
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                try {
                    file.writeText(content)  // Escribe el contenido en el archivo
                    Log.v("ESCRITURA EN ARCHIVO", "RUTA: '$filepath'")  // Log para verificar la ruta del archivo
                } catch (e: Exception) {
                    e.printStackTrace()  // Manejo de excepciones
                }
            } else {
                // Solicita el permiso de escritura en almacenamiento externo
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    WRITE_EXTERNAL_STORAGE_REQUEST_CODE
                )
            }
        }
    }
}
