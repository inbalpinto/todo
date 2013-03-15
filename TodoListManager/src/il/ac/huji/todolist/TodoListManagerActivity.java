package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class TodoListManagerActivity extends Activity {

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

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.todo_list_manager, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuItemAdd:
			EditText newTodoItem = (EditText) findViewById(R.id.edtNewItem);
			if ((newTodoItem != null) && (newTodoItem.length() != 0)) {
				adapter.add(new Item(newTodoItem.getText().toString()));
				newTodoItem.setText("");
			}
			break;
		case R.id.menuItemDelete:
			Item itemToDelete = (Item) listItems.getSelectedItem();
			if (itemToDelete != null)
			{
				adapter.remove(itemToDelete);
			}
			break;
		}
		return true;
	}

}
