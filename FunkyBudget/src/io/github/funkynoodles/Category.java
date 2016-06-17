package io.github.funkynoodles;

import java.util.ArrayList;

public class Category {
	private String name;
	public ArrayList<Category> subCategories = new ArrayList<Category>();

	public Category(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
