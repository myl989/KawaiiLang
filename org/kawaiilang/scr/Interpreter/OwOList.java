package org.kawaiilang;
import java.util.ArrayList;
import java.util.List;

public class OwOList<E> implements Cloneable {
  
  private ArrayList<E> list = new ArrayList<>();
  private Class<?> type = null;

  public OwOList() {
	
  }
  
  public OwOList(Class<?> type) {
	this.type = type;
  }

  public OwOList(List<E> list) {
    this.list.addAll(list);
  }
  
  public OwOList(List<E> list, Class<?> type) {
	this(type);
    this.list.addAll(list);
  }

  public void set(ArrayList<E> list) {
    this.list = list;
  }

  public E get(int index) {
    return list.get(index);
  }
  
  //NOTE: add() adds the items without modifying the original list, which is invoked by `append UwU UwU` in the KawaiiLang CoreAPI.
  //append() adds the items and modifies the original list, used in list creation.

  public OwOList<E> add(List<E> list) {
	OwOList<E> tmp = this.clone();
    tmp.list.addAll(list);
    return tmp;
  }

  public OwOList<E> add(E item) {
	OwOList<E> tmp = this.clone();
    tmp.list.add(item);
    return tmp;
  }

  public OwOList<E> addObj(Object item) {
	OwOList<E> tmp = this.clone();
	try {
	  tmp.list.add((E) item);
	} catch (ClassCastException ce) {
	  throw new java.security.InvalidParameterException("Item must be <? extends E>");
	}
	return tmp;
  }
  
  public OwOList<E> append(List<E> list) {
	list.addAll(list);
	return this;
  }
  
  public OwOList<E> append(E item) {
	list.add(item);
	return this;
  }
  
  public OwOList<E> appendObj(Object item) {
	try {
	  list.add((E) item);
	} catch (ClassCastException ce) {
	  throw new java.security.InvalidParameterException("Item must be <? extends E>");
	}
	return this;
  }

  public void set(int index, E item) {
    list.set(index, item);
  }

  public int length() {
    return list.size();
  }
  
  public boolean isEmpty() {
	return list.isEmpty();
  }

  public E pop(int index) {
    return list.remove(index);
  }

  public OwOList<E> slice(int begain, int end) {
    return new OwOList<>(list.subList(begain, end));
  }

  public ArrayList<E> asArrayList() {
    return list;
  }

  public OwOList<E> clone() {
	return new OwOList<E>((ArrayList<E>) list.clone());
  }
  
  public String toString() {
    return list.toString();
  }

  public int hashCode() {
    return 797 + (list.hashCode() >>> 2);
  }
  
  public Class<?> getType() {
	return type;
  }

  public boolean equals(Object o) {
    if (!(o instanceof OwOList)) {
      return false;
    }
    OwOList t = (OwOList) o;
    return (list.equals(t.list));
  }

}