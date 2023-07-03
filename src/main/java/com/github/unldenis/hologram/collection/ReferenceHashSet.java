package com.github.unldenis.hologram.collection;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ReferenceHashSet<E> implements Set<E> {

  private final Set<E> entries;

  public ReferenceHashSet() {
    entries = new HashSet<>();
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
}
