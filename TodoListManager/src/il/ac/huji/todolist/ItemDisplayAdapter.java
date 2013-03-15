package il.ac.huji.todolist;

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
		
		TextView row = (TextView)view.findViewById(R.id.itemName);
		
		if ((position % 2) == 1) {
			row.setText(item.name);
			row.setTextColor(Color.BLUE);
		} else {
			row.setText(item.name);
			row.setTextColor(Color.RED);
		}
		return view;
	}
}

