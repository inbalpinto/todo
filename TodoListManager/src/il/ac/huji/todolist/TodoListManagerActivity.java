package il.ac.huji.todolist;

import java.util.Date;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.content.Intent;
import android.database.Cursor;
import android.view.ContextMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;



public class TodoListManagerActivity extends Activity {

	private static final int ADD_ACTIVITY = 2;
	private static final int PHONE_NUMBER_INDEX = 5;
	private SimpleCursorAdapter adapter;
	private ListView listItems;

	private TodoDAL dlHelper;
	private Cursor cursor;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_manager);

		listItems = (ListView)findViewById(R.id.lstTodoItems);

		registerForContextMenu(listItems);

		dlHelper = new TodoDAL(this);    

		cursor = dlHelper.getDB().query("todo", new String[] { "_id", "title", "due" }, null, null, null, null, null);
		String[] from = { "title", "due" };
		int[] to = { R.id.txtTodoTitle, R.id.txtTodoDueDate };
		adapter = new ItemDisplayCursorAdapter(this, R.layout.row, cursor, from, to);
		listItems.setAdapter(adapter);
	}


	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		getMenuInflater().inflate(R.menu.todo_list_manager_contextmenu, menu);

		AdapterContextMenuInfo info = (AdapterContextMenuInfo)menuInfo;

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
		Cursor c = (Cursor) adapter.getItem(selectedItemIndex);

		if (c != null && c.getString(1) != null) {
			String itemName = c.getString(1);
			Date dueDate = null;
			Long dueLong = c.getLong(2);
			if (dueLong >= 0) {
				dueDate = new Date(dueLong);
			}
			Item itemToDelete = new Item(itemName, dueDate);

			switch (item.getItemId()){
			case R.id.menuItemDelete:
				dlHelper.delete(itemToDelete);
				cursor.requery();
				break;

			case R.id.menuItemCall:
				String callItem = itemName.substring(PHONE_NUMBER_INDEX);
				Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + callItem));
				startActivity(dial);

				break;
			}
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

			Item itemToAdd = new Item(itemName, dueDate);

			dlHelper.insert(itemToAdd);
			cursor.requery();
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
