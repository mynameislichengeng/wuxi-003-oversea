package com.wizarpos.atool.store;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.wizarpos.atool.log.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class SqliteHelper extends SQLiteOpenHelper {
	
	protected static final Logger logger = Logger.getLogger();
	//目前保持生产和测试环境的版本号一致
//	protected static String dbname = "cashier.db";     //生产 金融云 
//	protected static int cashierVersion = 7;
	protected static String dbname = "cashier2.db";    //测试
	protected static int cashierVersion = 30; //收款是30,不能比30低
//	protected static String dbname = "train.db";       //内测
//	protected static int cashierVersion = 30;
//	protected static String defaultDatabaseName = dbname;
	protected static String defaultDatabaseName = SDBHelper.DB_DIR + File.separator + dbname;
	protected static int databaseVersion = cashierVersion;
	
	protected Map<String, TableMeta> tableMap = new HashMap<String, TableMeta>();
	protected TableGenerator generator = null;
	protected SQLiteDatabase db = null;
	
	public SqliteHelper(Context context) {
		super(context, defaultDatabaseName, null, databaseVersion);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		this.db = db;
		List<TableMeta> list = generator.getTableMetaList();
		for (TableMeta tm : list) {
			db.execSQL(tm.toSQLString());
		}
	}
	
	public void initParameter(SQLiteDatabase db) {
		this.db = db;
		List<TableMeta> list = generator.getTableMetaList();
		for (TableMeta tm : list) {
			tableMap.put(tm.getTableName(), tm);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + "cash_pay_repair");
		db.execSQL("DROP TABLE IF EXISTS " + "merchant_card_def");
		db.execSQL("DROP TABLE IF EXISTS " + "tb_config");
		db.execSQL("DROP TABLE IF EXISTS " + "ticket_card_def");
		onCreate(db);
	}
	
	public long insert(String table, ContentValues values) {
		return db.insert(table, null, values);
	}
	
	public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
		return db.update(table, values, whereClause, whereArgs);
	}
	
	public void insertOrUpdate(String table, ContentValues values, String whereClause, String[] whereArgs) {
		List<Map<String, Object>> r = query(table, whereClause, whereArgs);
		if (r == null || r.size() < 1) {
			insert(table, values);
		} else if (r.size() > 1) {
			return;
		} else {
			update(table, values, whereClause, whereArgs);
		}
	}
	
	public long insertOrUpdate(String table, ContentValues values) {
		TableMeta tableMeta = tableMap.get(table);
		String primaryKeyName = tableMeta.getPrimaryKeyName();
		String whereClause = primaryKeyName + " = ?";
		String[] whereArgs = {values.getAsString(primaryKeyName)};
		
		List<Map<String, Object>> r = query(table, whereClause, whereArgs);
		if (r == null || r.size() < 1) {
			return insert(table, values);
		} else if (r.size() > 1) {
			return 0;
		} else {
			return update(table, values, whereClause, whereArgs);
		}
	}
	
	public int delete(String table, String whereClause, String[] whereArgs) {
		return db.delete(table, whereClause, whereArgs);
	}
	
	public int delete(String table) {
		return db.delete(table, null, null);
	}
	
	public List<Map<String, Object>> query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
		Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
		return convert(table, cursor);
	}
	
	public List<Map<String, Object>> query(String table, String[] columns, String selection, String[] selectionArgs) {
		Cursor cursor = db.query(table, columns, selection, selectionArgs, null, null, null);
		return convert(table, cursor);
	}
	
	public List<Map<String, Object>> query(String table, String selection, String[] selectionArgs) {
		TableMeta tableMeta = tableMap.get(table);
		List<String> fieldNameList = tableMeta.getFieldNameList();
		Cursor cursor = db.query(table, fieldNameList.toArray(new String[fieldNameList.size()]), selection, selectionArgs, null, null, null);
		return convert(table, cursor);
	}
	
	public Map<String, Object> query(String table, String cname, String cvalue) {
		List<Map<String, Object>> result = query(table, cname + " = ?", new String[]{cvalue});
		if (result != null && result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
	
	public List<Map<String, Object>> query(String table) {
		Cursor cursor = db.query(table, null, null, null, null, null, null);
		return convert(table, cursor);
	}
	
	protected List<Map<String, Object>> convert(String table, Cursor cursor) {
		TableMeta tableMeta = tableMap.get(table);
		List<String> fieldNameList = tableMeta.getFieldNameList();
		
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		Map<String, Object> columnData = null;
		
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			columnData = new HashMap<String, Object>();
			for (int i = 0; i < fieldNameList.size(); i++) {
				int type = cursor.getType(i);
				
				if (type == Cursor.FIELD_TYPE_NULL) {
					columnData.put(fieldNameList.get(i), cursor.getString(i));
				} else if (type == Cursor.FIELD_TYPE_INTEGER) {
					columnData.put(fieldNameList.get(i), cursor.getLong(i));
				} else if (type == Cursor.FIELD_TYPE_FLOAT) {
					columnData.put(fieldNameList.get(i), cursor.getDouble(i));
				} else if (type == Cursor.FIELD_TYPE_STRING) {
					columnData.put(fieldNameList.get(i), cursor.getString(i));
				} else if (type == Cursor.FIELD_TYPE_BLOB) {
					columnData.put(fieldNameList.get(i), cursor.getBlob(i));
				}
			}
			result.add(columnData);
			cursor.moveToNext();
		}
		return result;
	}
	
	public void close() {
		if (db != null) {
			db.close();
		}
	}
	
}
