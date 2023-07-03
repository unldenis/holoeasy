package com.github.unldenis.hologram.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ReferenceArrayList<E> implements List<E> {

  private final List<E> entries;

  public ReferenceArrayList() {
    entries = new ArrayList<>();
  }

  @Override
  public int size() {
    return entries.size();
  }

  @Override
  public boolean isEmpty() {
    return entries.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    for(E e: entries) {
      if(e == o) {
        return true;
      }
    }
    return false;
  }

  @Override
  public Iterator<E> iterator() {
    return entries.iterator();
  }

  @Override
  public Object[] toArray() {
    return entries.toArray();
  }

  @Override
  public <T> T[] toArray(T[] a) {
    return entries.toArray(a);
  }

  @Override
  public boolean add(E e) {
    return entries.add(e);
  }

  @Override
  public boolean remove(Object o) {
    Iterator<E> iterator = entries.iterator();
    while (iterator.hasNext()) {
      E next = iterator.next();
      if(next == o) {
        iterator.remove();
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    throw new IllegalStateException("Not implemented.");
  }

  @Override
  public boolean addAll(Collection<? extends E> c) {
    return entries.addAll(c);
  }

  @Override
  public boolean addAll(int index, Collection<? extends E> c) {
    return entries.addAll(index, c);
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    throw new IllegalStateException("Not implemented.");
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    throw new IllegalStateException("Not implemented.");
  }

  @Override
  public void clear() {
    entries.clear();
  }

  @Override
  public E get(int index) {
    return entries.get(index);
  }

  @Override
  public E set(int index, E element) {
    return entries.set(index, element);
  }

  @Override
  public void add(int index, E element) {
    entries.add(index, element);
  }

  @Override
  public E remove(int index) {
    return entries.remove(index);
  }

  @Override
  public int indexOf(Object o) {
    Iterator<E> it = entries.iterator();
    for(int i = 0; it.hasNext(); i++) {
      E next = it.next();
      if(next == o) {
        return i;
      }
    }
    return -1;
  }

  @Override
  public int lastIndexOf(Object o) {
    throw new IllegalStateException("Not implemented.");
  }

  @Override
  public ListIterator<E> listIterator() {
    return entries.listIterator();
  }

  @Override
  public ListIterator<E> listIterator(int index) {
    return entries.listIterator();
  }

  @Override
  public List<E> subList(int fromIndex, int toIndex) {
    return entries.subList(fromIndex, toIndex);
  }

}
