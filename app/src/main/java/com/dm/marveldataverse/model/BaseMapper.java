package com.dm.marveldataverse.model;

import android.content.Context;

import com.dm.marveldataverse.core.DBManager;

/**
 * Esta clase representa la parte común de todos los Mappers, es decir, la instancia de la base de
 * datos
 */
public class BaseMapper {

    protected DBManager instance;

    /**
     * El método constructor obtiene la instancia de conexión a la base de datos
     * @param context El contexto de la aplicación
     */
    protected BaseMapper (Context context){
        this.instance = DBManager.getManager(context);
    }

}
