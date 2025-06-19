package domain.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Pair<T> implements Cloneable, List<T> {
	
	private T left;
	private T right;
	
	public Pair(T left, T right) {
		super();
		this.left = left;
		this.right = right;
	}
	
	public Pair(Pair<T> other) {
		this.left = other.left;
		this.right = other.right;
	}
	
	public Pair() {
		left = null;
		right = null;
	}

	public T getLeft() {
		return left;
	}

	public void setLeft(T left) {
		this.left = left;
	}

	public T getRight() {
		return right;
	}

	public void setRight(T right) {
		this.right = right;
	}
	
	public Object[] toArray() {
		return new Object[] {left, right};
	}
	
	public Pair<T> clone() {
		return new Pair<>(this);
	}

	public class PairIterator implements Iterator<T> {

		private int pos = 0;
		@Override
		public boolean hasNext() {
			return pos < 2;
		}

		@Override
		public T next() {
			return switch (pos++) {
				case 0 -> left;
				case 1 -> right;
				default -> null;
			};
		}
		
	}
	@Override
	public Iterator<T> iterator() {
		return new PairIterator();
	}
	
	public int indexOf(Object elem) {
		if(elem.equals(left)) return 0;
		else if (elem.equals(right)) return 1;
		else return -1;
	}
	
	public boolean setAt(int index, T newValue) {
		switch (index) {
			case 0:
				left = newValue;
				return true;
			case 1:
				right = newValue;
				return true;
			default:
				return false;
		}
	}

	@Override
	public int size() {
		return 2;
	}

	@Override
	public boolean isEmpty() {
		return left == null && right == null;
	}

	@Override
	public boolean contains(Object o) {
		return indexOf(o) == -1;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <Y> Y[] toArray(Y[] a) {
		a[0] = (Y) left;
		a[1] = (Y) right;
		return a;
	}

	@Override
	public boolean add(T e) {
		return false;
	}

	@Override
	public boolean remove(Object o) {
		return setAt(indexOf(o), null);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for(Object o : c) {
			if(indexOf(o) == -1)
				return false;
		}
		return true;
	}

	@Override public boolean addAll(Collection<? extends T> c) {return false;}
	@Override public boolean addAll(int index, Collection<? extends T> c) {return false;}

	@Override 
	public boolean removeAll(Collection<?> c) {
		for(Object o : c) {
			remove(indexOf(o));
		}
		return true;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		left = null;
		right = null;
		
	}

	@Override
	public T get(int index) {
		return switch (index) {
			case 0 -> left;
			case 1 -> right;
			default -> null;
		};
	}

	@Override
	public T set(int index, T element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void add(int index, T element) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public T remove(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int lastIndexOf(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ListIterator<T> listIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
