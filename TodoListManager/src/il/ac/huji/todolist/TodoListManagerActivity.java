package il.ac.huji.todolist;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.ContextMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class TodoListManagerActivity extends Activity {

	private static final int ADD_ACTIVITY = 2;
	private static final int PHONE_NUMBER_INDEX = 5;
	private ArrayAdapter<Item> adapter;
	private ListView listItems;

	////////////////
	private SQLiteDatabase db;
	private Cursor cursor;
	//////////////////////

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_manager);

		List<Item> items = new ArrayList<Item>();
		listItems = (ListView)findViewById(R.id.lstTodoItems);

		adapter = new ItemDisplayAdapter(this, items);
	//	listItems.setAdapter(adapter); //TODO check this

		registerForContextMenu(listItems);

		///////////////////////////////////////////////////////       
		TodoDBHelper helper = new TodoDBHelper(this);
		db = helper.getWritableDatabase();

	
//		ContentValues todoItem = new ContentValues();
//
//		todoItem.put("_id", 2525);
//		todoItem.put("title", "todo Title");
//		todoItem.put("due", 555111222);
//		db.insert("todo", null, todoItem);
//
//		// check -1 TODO 
//		String namePrefix = "Aa";
//		db.delete("todo_db", "name like '?%'", new String[] { namePrefix });
//		//check -1 TODO
//
//		Cursor cursorDemo = db.query("todo_db", new String[] {"title", "due"}, "name like '?%'", new String[] { namePrefix }, null, null, "title desc");
//		if (cursorDemo.moveToFirst()) { // if the query has lines
//			do {
//				String title = cursorDemo.getString(1); //...and so on
//
//			} while (cursorDemo.moveToNext());
//		}
//

//		 cursor = db.query("todo", new String[] {"_id", "title", "due"}, null, null, null, null, null);
//		if (cursor.moveToFirst()) {
//			do {
//				String itemTitle = cursor.getString(1);
//				long itemDate = cursor.getLong(2);
//				Date dueDate = new Date(itemDate);
//				System.out.println("title = " + itemTitle + ", when = " + dueDate.toGMTString());
//			} while (cursor.moveToNext());
//		}
		
		cursor = db.query("todo", new String[] { "_id", "title", "due" }, null, null, null, null, null);
		String[] from = { "title", "due" };
		int[] to = { R.id.txtTodoTitle, R.id.txtTodoDueDate };
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.row, cursor, from, to);
		listItems.setAdapter(adapter);
		
		
		//////////////////////////////////////////////////////////

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		getMenuInflater().inflate(R.menu.todo_list_manager_contextmenu, menu);

		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
		String title = (String) ((TextView) info.targetView.findViewById(R.id.txtTodoTitle)).getText();	 
		menu.setHeaderTitle(title);

		if (title.startsWith("Call ")) {
			MenuItem callItem = menu.findItem(R.id.menuItemCall);
			callItem.setTitle(title);
		}
		else {
			menu.removeItem(R.id.menuItemCall);
		}

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		int selectedItemIndex = info.position;

		switch (item.getItemId()){
		case R.id.menuItemDelete:
			adapter.remove(adapter.getItem(selectedItemIndex));
			break;

		case R.id.menuItemCall:
			String callItem = adapter.getItem(selectedItemIndex).name.substring(PHONE_NUMBER_INDEX);
			Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + callItem));
			startActivity(dial);

			break;
		}


		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.todo_list_manager, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ADD_ACTIVITY && resultCode == RESULT_OK) {
			Date dueDate = (Date) data.getSerializableExtra("dueDate");
			String itemName = data.getStringExtra("title");
			adapter.add(new Item(itemName, dueDate));

			if (itemName != null) {

				/////////////////////////////// ADD TO DB/////////////////////////////////////

				ContentValues values = new ContentValues();
				values.put("title", itemName);
				values.put("due", dueDate.getTime());
				long success = db.insert("todo", null, values);
				//TODO check for -1
				//if success == -1 do...
			
				cursor.requery();

				//ParseObject parseObject = new ParseObject("Activity");
				//parseObject.put("activity", (String)spnActivity.getSelectedItem());
				//parseObject.put("when", new Date().getTime());
				//parseObject.saveInBackground();

				/////////////////////////////////////////////////////////////////////////////    			

			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuItemAdd:
			Intent intent = new Intent(this, AddNewTodoItemActivity.class);
			startActivityForResult(intent, ADD_ACTIVITY);
			break;
		}
		return true;
	}




}

