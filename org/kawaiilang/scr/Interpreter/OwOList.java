package org.kawaiilang;
import java.util.ArrayList;
import java.util.List;

class OwOList {

  private ArrayList<Object> list = new ArrayList<>();

  public OwOList() {

  }

  public OwOList(List<Object> list) {
    this.list.addAll(list);
  }

  public void set(ArrayList<Object> list) {
    this.list = list;
  }

  public Object get(int index) {
    return list.get(index);
  }

  public void append(List<Object> list) {
    this.list.addAll(list);
  }

  public void append(Object item) {
    list.add(item);
  }

  public void set(int index, Object item) {
    list.set(index, item);
  }

  public int length() {
    return list.size();
  }

  public Object pop(int index) {
    return list.remove(index);
  }

  public OwOList slice(int begain, int end) {
    return new OwOList(list.subList(begain, end));
  }

  public ArrayList<Object> asArrayList() {
    return list;
  }

  public String toString() {
    return list.toString();
  }

  public int hashCode() {
    return 797 + 2 * list.hashCode();
  }

  public boolean equals(Object o) {
    if (!(o instanceof OwOList)) {
      return false;
    }
    OwOList t = (OwOList) o;
    return (list.equals(t.list));
  }

}