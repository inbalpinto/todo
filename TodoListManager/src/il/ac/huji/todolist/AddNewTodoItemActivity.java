
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;

public class AddNewTodoItemActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addnewtodoitem_activity);

		findViewById(R.id.btnOK).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				EditText edtNewItem = (EditText) findViewById(R.id.edtNewItem);
				String itemName = edtNewItem.getText().toString();
				if (itemName == null || "".equals(itemName)) {
					setResult(RESULT_CANCELED);
					finish();
				} else {
					DatePicker dp = (DatePicker) findViewById(R.id.datePicker);
					Date dueDate = null;
					if (dp != null)	{
						dueDate = (Date) new Date((dp.getYear() - 1900), dp.getMonth(), dp.getDayOfMonth()); 
					}
					
					Intent resultIntent = new Intent();
					resultIntent.putExtra("dueDate", dueDate);
					resultIntent.putExtra("title", itemName);
					setResult(RESULT_OK, resultIntent);
					finish();
				}
			}
		});
		
		
		findViewById(R.id.btnCancel).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
					setResult(RESULT_CANCELED);
					finish();
				 
			}
		});
	}

}