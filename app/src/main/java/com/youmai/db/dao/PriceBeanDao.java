package com.youmai.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.youmai.db.bean.PriceBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "PRICE_BEAN".
*/
public class PriceBeanDao extends AbstractDao<PriceBean, Long> {

    public static final String TABLENAME = "PRICE_BEAN";

    /**
     * Properties of entity PriceBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property StartHour = new Property(1, byte.class, "startHour", false, "START_HOUR");
        public final static Property StartMin = new Property(2, byte.class, "startMin", false, "START_MIN");
        public final static Property EndHour = new Property(3, byte.class, "endHour", false, "END_HOUR");
        public final static Property EndMin = new Property(4, byte.class, "endMin", false, "END_MIN");
        public final static Property Price = new Property(5, int.class, "price", false, "PRICE");
    }


    public PriceBeanDao(DaoConfig config) {
        super(config);
    }
    
    public PriceBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"PRICE_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"START_HOUR\" INTEGER NOT NULL ," + // 1: startHour
                "\"START_MIN\" INTEGER NOT NULL ," + // 2: startMin
                "\"END_HOUR\" INTEGER NOT NULL ," + // 3: endHour
                "\"END_MIN\" INTEGER NOT NULL ," + // 4: endMin
                "\"PRICE\" INTEGER NOT NULL );"); // 5: price
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"PRICE_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, PriceBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getStartHour());
        stmt.bindLong(3, entity.getStartMin());
        stmt.bindLong(4, entity.getEndHour());
        stmt.bindLong(5, entity.getEndMin());
        stmt.bindLong(6, entity.getPrice());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, PriceBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getStartHour());
        stmt.bindLong(3, entity.getStartMin());
        stmt.bindLong(4, entity.getEndHour());
        stmt.bindLong(5, entity.getEndMin());
        stmt.bindLong(6, entity.getPrice());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public PriceBean readEntity(Cursor cursor, int offset) {
        PriceBean entity = new PriceBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            (byte) cursor.getShort(offset + 1), // startHour
            (byte) cursor.getShort(offset + 2), // startMin
            (byte) cursor.getShort(offset + 3), // endHour
            (byte) cursor.getShort(offset + 4), // endMin
            cursor.getInt(offset + 5) // price
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, PriceBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setStartHour((byte) cursor.getShort(offset + 1));
        entity.setStartMin((byte) cursor.getShort(offset + 2));
        entity.setEndHour((byte) cursor.getShort(offset + 3));
        entity.setEndMin((byte) cursor.getShort(offset + 4));
        entity.setPrice(cursor.getInt(offset + 5));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(PriceBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(PriceBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(PriceBean entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
