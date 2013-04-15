package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


public class TodoDAL {

	private SQLiteDatabase db;

	public TodoDAL(Context context) { 
		TodoDBHelper helper = new TodoDBHelper(context);
		db = helper.getWritableDatabase();

		Parse.initialize(context, context.getResources().getString(R.string.parseApplication), context.getString(R.string.clientKey)); 
		ParseUser.enableAutomaticUser();

		//db.delete("todo", null, null);
	}
	

	public boolean insert(ITodoItem todoItem) { 
		boolean success = true;
		
		ContentValues values = new ContentValues();
		values.put("title", todoItem.getTitle());

		if (todoItem.getDueDate() != null) {
			values.put("due", todoItem.getDueDate().getTime());
		} 
		else { 	
			values.putNull("due");
		}

		long retVal = db.insert("todo", null, values);
		if (retVal == -1) {
			success = false;
		}

		ParseObject parseObject = new ParseObject("todo");
		parseObject.put("title", todoItem.getTitle());
		if (todoItem.getDueDate() != null) {
			parseObject.put("due", todoItem.getDueDate().getTime());
		} 

		try {
			parseObject.save();
		} catch (ParseException e) {
			e.printStackTrace();
			success = false;
		}

		return success;
	}
	

	public boolean update(ITodoItem todoItem) { 
		boolean success = true;

		ContentValues values = new ContentValues();
		values.put("title", todoItem.getTitle());
		if (todoItem.getDueDate() != null) {
			values.put("due", todoItem.getDueDate().getTime());
		} 
		else { 	
			values.putNull("due");
		}

		long retVal = db.update("todo", values, "title = ?", new String[] { todoItem.getTitle() });
		if (retVal <= 0) {
			success = false;
		}

		ParseQuery query = new ParseQuery("todo");
		query.whereEqualTo("title", todoItem.getTitle());
		try {
			List<ParseObject> objects = query.find();
			for (ParseObject object : objects) {
				
				if (todoItem.getDueDate() != null) {
					object.put("due", todoItem.getDueDate().getTime()); 
				}
				else {
					object.remove("due");
				}
				object.save();
			}
		} catch (ParseException e) {
			e.printStackTrace();
			success = false;
		}

		return success;
	}
	
	
	public boolean delete(ITodoItem todoItem) { 
		boolean success = true;

		long retVal = db.delete("todo", "title = ?", new String[] { todoItem.getTitle() });
		if (retVal <= 0) {
			success = false;
		}

		ParseQuery query = new ParseQuery("todo");
		query.whereEqualTo("title", todoItem.getTitle());
		try {
			List<ParseObject> objects = query.find();
			for (ParseObject object : objects) {
				object.delete();
			}
		} catch (ParseException e) {
			e.printStackTrace();
			success = false;
		}

		return success;
	}
	
	
	public List<ITodoItem> all() { 
//		List<ITodoItem> items = new ArrayList<ITodoItem>();
//
//		Cursor c = db.query("todo", new String[] { "title", "due" }, null, null, null, null, null);
//		while(c.moveToNext()) {
//			String itemName = c.getString(0);
//			Long dueDate = c.getLong(1);
//			Item item = new Item(itemName, new Date(dueDate));
//			items.add(item);
//		}
//		return items;
		
		ParseQuery query = new ParseQuery("todo");
		List<ITodoItem> items = new ArrayList<ITodoItem>();
		
		try {
			List<ParseObject> objects = query.find();
			for (ParseObject object : objects) {
				items.add(new Item(object.getString("title"), new Date(object.getLong("due"))));
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		return items;
	}

	public SQLiteDatabase getDB() {
		return db;
	}

}