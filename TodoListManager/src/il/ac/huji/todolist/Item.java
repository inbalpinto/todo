package il.ac.huji.todolist;

import java.util.Date;

public class Item implements ITodoItem {


	public Item(String name, Date dueDate) {
		this.name = name;
		this.dueDate = dueDate;
	}

	public String name;
	public Date dueDate;
	
	@Override
	public String getTitle() {
		return name;
	}
	@Override
	public Date getDueDate() {
		return dueDate;
	}

}

