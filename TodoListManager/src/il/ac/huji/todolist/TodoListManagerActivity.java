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
import android.widget.TextView;
import android.content.Intent;
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


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_manager);

		List<Item> items = new ArrayList<Item>();
		listItems = (ListView)findViewById(R.id.lstTodoItems);

		adapter = new ItemDisplayAdapter(this, items);
		listItems.setAdapter(adapter);
		
        registerForContextMenu(listItems);


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
    		if (itemName != null) {
        		adapter.add(new Item(itemName, dueDate));
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

