package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText ID, Usuario, AreaUsuario;
    ListView Lista;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ID = findViewById(R.id.txtID);
        Usuario = findViewById(R.id.txtNombreUsuario);
        AreaUsuario = findViewById(R.id.txtAreaUsuario);
        Lista = findViewById(R.id.ListaUsuario);
        CargaUsuarios();
    }

    public void RegistrarUsuario(View view) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "Produccion", null, 1);
        SQLiteDatabase baseDatos = admin.getWritableDatabase();
        String IDUsuario = ID.getText().toString();
        String NombreUsuario = Usuario.getText().toString();
        String Areausuario = AreaUsuario.getText().toString();
        if (!IDUsuario.isEmpty() && !NombreUsuario.isEmpty()
                && !Areausuario.isEmpty()) {
            ContentValues DatosUsuario = new ContentValues();
            DatosUsuario.put("ID_Usuario", IDUsuario);
            DatosUsuario.put("NombreUsuario", NombreUsuario);
            DatosUsuario.put("AreaUsuario", Areausuario);
            baseDatos.insert("Usuarios", null, DatosUsuario);
            baseDatos.close();
            ID.setText("");
            Usuario.setText("");
            AreaUsuario.setText("");
            CargaUsuarios();
            Toast.makeText(this, "Se ha registrado el usuario correctamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No pueden haber campos vacios!", Toast.LENGTH_SHORT).show();
        }
    }

    public void BuscarUsuario(View view) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "Produccion", null, 1);
        SQLiteDatabase baseDatos = admin.getWritableDatabase();
        String IDUsuario = ID.getText().toString();
        if (!IDUsuario.isEmpty()) {
            Cursor fila = baseDatos.rawQuery("Select NombreUsuario, AreaUsuario  from Usuarios where ID_Usuario=" + IDUsuario, null);
            if (fila.moveToFirst()) {
                Usuario.setText(fila.getString(0));
                AreaUsuario.setText(fila.getString(1));
                baseDatos.close();
            } else {
                Toast.makeText(this, "El ID ingresado no existe", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "El campo ID no puede estar vacio", Toast.LENGTH_SHORT).show();
        }
    }

    public void EliminarUsuario(View view) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "Produccion", null, 1);
        SQLiteDatabase baseDatos = admin.getWritableDatabase();
        String IDUsuario = ID.getText().toString();
        if (!IDUsuario.isEmpty()) {
            int Eliminar = baseDatos.delete("Usuarios", "ID_Usuario=" + IDUsuario, null);
            if (Eliminar == 1) {
                Toast.makeText(this, "Se elimino el usuario correctamente", Toast.LENGTH_SHORT).show();
                CargaUsuarios();
            } else {
                Toast.makeText(this, "No se encontro el ID ingresado por el usuario", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "el campo no puede estar vacio", Toast.LENGTH_SHORT).show();
        }
    }

    public void ModificarUsuario(View view) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "Produccion", null, 1);
        SQLiteDatabase baseDatos = admin.getWritableDatabase();
        String IDUsuario = ID.getText().toString();
        String NombreUsuario = Usuario.getText().toString();
        String Areausuario = AreaUsuario.getText().toString();
        if (!IDUsuario.isEmpty() && !NombreUsuario.isEmpty() &&
                !Areausuario.isEmpty()) {
            ContentValues DatosUsuario = new ContentValues();
            DatosUsuario.put("NombreUsuario", NombreUsuario);
            DatosUsuario.put("AreaUsuario", Areausuario);
            int Modificar = baseDatos.update("Usuarios", DatosUsuario,
                    "ID_Usuario=" + IDUsuario, null);
            if (Modificar == 1) {
                baseDatos.close();
                ID.setText("");
                Usuario.setText("");
                AreaUsuario.setText("");
                CargaUsuarios();
                Toast.makeText(this, "Se modifico correctamente la base de datos", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No existe la ID a modificar", Toast.LENGTH_SHORT).show();

            }
        } else {
            Toast.makeText(this, "No pueden haber campos vacios", Toast.LENGTH_LONG).show();
        }
    }

    public void CargaUsuarios() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "Produccion", null, 1);
        SQLiteDatabase baseDatos = admin.getWritableDatabase();
        Cursor fila = baseDatos.rawQuery("Select * from Usuarios", null);
        ArrayList<String> ListaUsuarios = new ArrayList<>();
        if (fila.moveToFirst()) {
            do {
                String idUsuario2 = fila.getString(0);
                String NombreUsuario2 = fila.getString(1);
                String AreaUsuario2 = fila.getString(2);
                String UserInfo = " - ID del Usuario: " + idUsuario2 +
                        " - Nombre del Usuario: " + NombreUsuario2 +
                        " - Area del Usuario: " + AreaUsuario2;
                ListaUsuarios.add(UserInfo);
            } while (fila.moveToNext());
        }
        baseDatos.close();
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ListaUsuarios);
        Lista.setAdapter(adapter);
    }
}