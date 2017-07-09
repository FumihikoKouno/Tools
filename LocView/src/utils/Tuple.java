package utils;

import java.util.ArrayList;
import java.util.List;

public class Tuple {
	private List<Object> tuple;

	public Tuple(Object... args) {
		tuple = new ArrayList<Object>();
		for (Object obj : args) {
			tuple.add(obj);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T get(int index) {
		return (T) (this.tuple.get(index));
	}

	public int size() {
		return this.tuple.size();
	}

	public String toString() {
		String str = "(";
		for (int i = 0; i < this.size(); i++) {
			str = str + this.get(i).toString();
			if (i < this.size() - 1) {
				str = str + ", ";
			}
		}
		str = str + ")";
		return str;
	}
}
