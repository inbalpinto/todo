package il.ac.huji.todolist;

import java.util.Date;

public class Item {


	public Item(String name, Date dueDate) {
		this.name = name;
		this.dueDate = dueDate;
	}

	public String name;
	public Date dueDate;

}

