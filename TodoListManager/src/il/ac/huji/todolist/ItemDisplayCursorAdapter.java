package il.ac.huji.todolist;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ItemDisplayCursorAdapter extends SimpleCursorAdapter {

	public ItemDisplayCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
		super(context, layout, c, from, to);
	}

	private View fixView(Cursor cursor, View view) {
		String title = cursor.getString(1);
		
		int index = cursor.getColumnIndexOrThrow("due");
		Long dueLong = null;
		if (!cursor.isNull(index)) {
			dueLong = cursor.getLong(index);
		}
		      
		Date dueDate = null;
	
		if (dueLong != null) {
			dueDate = new Date(dueLong);
		}

		TextView titleView = (TextView)view.findViewById(R.id.txtTodoTitle);
		TextView dueView = (TextView)view.findViewById(R.id.txtTodoDueDate);

		if (title != null) {
			titleView.setText(title);	
		}

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		if (dueDate != null) {
			dueView.setText(sdf.format(dueDate));

			if (dueDate.before(new Date())) {
				dueView.setTextColor(Color.RED);
				titleView.setTextColor(Color.RED);
			}
			else {
				dueView.setTextColor(Color.BLACK);
				titleView.setTextColor(Color.BLACK);
			}
		}
		else {
			dueView.setText("No due date");
			dueView.setTextColor(Color.BLACK);
			titleView.setTextColor(Color.BLACK);
		}

		return view;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		Cursor c = getCursor();

		final LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.row, null);

		return fixView(c,v);
	}

	@Override
	public void bindView(View v, Context context, Cursor c) {
		fixView(c,v);
	}
}







