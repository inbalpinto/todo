package il.ac.huji.todolist;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class ItemDisplayAdapter extends ArrayAdapter<Item> {

	public ItemDisplayAdapter(TodoListManagerActivity activity, List<Item> items) {
		super(activity, android.R.layout.simple_list_item_1, items);
	}

	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Item item = getItem(position);
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.row, null);
		
		TextView itemName = (TextView)view.findViewById(R.id.txtTodoTitle);
		TextView dueDate = (TextView)view.findViewById(R.id.txtTodoDueDate);
		
		if (itemName != null) {
			itemName.setText(item.name);	
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		if (item.dueDate != null) {
			dueDate.setText(sdf.format(item.dueDate));
			
			if (item.dueDate.before(new Date())) {
				dueDate.setTextColor(Color.RED);
				itemName.setTextColor(Color.RED);
			}
		}
		else {
			dueDate.setText("No due date");
		}
		
		return view;
	}
	
}

