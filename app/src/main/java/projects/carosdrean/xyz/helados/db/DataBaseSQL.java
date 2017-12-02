package projects.carosdrean.xyz.helados.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import projects.carosdrean.xyz.helados.entidad.Identificador;

/**
 * Created by josue on 25/11/2017.
 */

public class DataBaseSQL extends SQLiteOpenHelper {

    private Context context;
    private String TABLE_NAME = "IDS";

    public DataBaseSQL(Context context) {
        super(context, "Identificador", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryCreteTable = "CREATE TABLE " + "IDS" + " ("
                + "id"  + " TEXT PRIMARY KEY, "
                + "ids" + " TEXT"
                + ")";

        db.execSQL(queryCreteTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXIST " + TABLE_NAME);
        onCreate(db);
    }

    public Identificador obtenerRegistro(Identificador identificador){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor registros = db.rawQuery(query, null);
        while (registros.moveToNext()){
            identificador.setId(registros.getString(0));
            identificador.setIdentificador(registros.getString(1));
        }
        db.close();
        return identificador;
    }

    public void ingresarRegistro(ContentValues contentValues){
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, contentValues);
        db.close();
    }
}
