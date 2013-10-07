package samplepkg;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class BuilderCheck {
	public static void main(String[] args) {
		Item orderedItem = null;
		orderedItem = (Pizza) new Item.Builder().setFieldValue("extraToppings", true).build(Pizza.class.getName());
		System.out.println(orderedItem.toString());
		orderedItem = (Burger) new Item.Builder().setFieldValue("doubleCheeze", true).build(Burger.class.getName());
		System.out.println(orderedItem.toString());
	}
}

/**
 * This class will remain immutable, we can have getters for the fields, but they are not added at this point.
 */

class Pizza extends Item {
	private int itemnumber;
	private boolean extraToppings = false;
	peivate boolean extraCheeze = false;

	private Pizza() {

	}

	@Override
	void setItemNumber(int itemnumber) {
		this.itemNumber = itemnumber;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Pizza!!!!!!!!!!!!!!!!!!!! extraToppings = "+ extraToppings;
	}
}

/**
 * This class will remain immutable, we can have getters for the fields, but they are not added at this point.
 */
class Burger extends Item {
	private int itemnumber;
	private boolean doubleCheeze = false;

	private Burger() {

	}

	@Override
	void setItemNumber(int itemnumber) {
		this.itemNumber = itemnumber;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Burger!!!!!!!!!!!!!!!!!!!! doubleCheeze = " + doubleCheeze;
	}
}

/**
 * Any class that is subclass of Item will create an instance of it using the Builder class of Item. 
 * As in Builder pattern, user can set the properties before building the object.
 * As in Factory pattern, creation of class will be decided on runtime.
 * Any class that will extend Item is expected to make their constructor private to take the advantage of Builder class.
 * */
abstract class Item {
	int itemNumber;

	abstract void setItemNumber(int itemnumber);

	static class Builder {
		HashMap<String, Object> fieldValue = new HashMap<String, Object>();

		public Builder setFieldValue(String fieldName, Object fieldValue) {
			this.fieldValue.put(fieldName, fieldValue);
			return this;
		}

		public Object build(String className) {
			Class<?> classClass = null;
			Object classObject = null;
			try {
				classClass = Class.forName(className);
				Constructor<?> classConstructor = classClass.getDeclaredConstructor();
				classConstructor.setAccessible(true);
				classObject = classConstructor.newInstance((Object[]) null);
			} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
			
			if (classObject != null && classClass!=null) {
				if(fieldValue!=null){
					Set<String> keys = fieldValue.keySet();
					Iterator<String> i = keys.iterator();
					while(i.hasNext()){
						String fieldName = i.next();
						try {
							Field field = classClass.getDeclaredField(fieldName);
							field.setAccessible(true);
							Object value = fieldValue.get(fieldName);
							field.set(classObject, value);
						} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
							e.printStackTrace();
						}
					}
					i = null;
					fieldValue.clear();
					fieldValue = null;
				}
			}
			return classObject;
		}
	}
}
