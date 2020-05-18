/*
 * Copyright (c) 1997, 2007, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package io.github.andyalvarezdev.primitive.collections.abstracts;

import io.github.andyalvarezdev.primitive.collections.IntCollection;
import io.github.andyalvarezdev.primitive.iterators.IntIterator;

import java.util.Arrays;

/**
 * <p>
 * This class provides a skeletal implementation of the Collection
 * interface, to minimize the effort required to implement this interface.
 * </p>
 * <p>
 * To implement an unmodifiable collection, the programmer needs only to
 * extend this class and provide implementations for the iterator and
 * size methods.  (The iterator returned by the iterator
 * method must implement hasNext and next.)
 * </p>
 * <p>
 * To implement a modifiable collection, the programmer must additionally
 * override this class's add method (which otherwise throws an
 * UnsupportedOperationException), and the iterator returned by the
 * iterator method must additionally implement its remove
 * method.
 * </p>
 * <p>
 * The programmer should generally provide a void (no argument) and
 * Collection constructor, as per the recommendation in the
 * Collection interface specification.
 * </p>
 * <p>
 * The documentation for each non-abstract method in this class describes its
 * implementation in detail.  Each of these methods may be overridden if
 * the collection being implemented admits a more efficient implementation.
 * </p>
 * This class is a member of the
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a>.
 *
 * @see IntCollection
 */
public abstract class AbstractIntCollection implements IntCollection
{
	/**
	 * Sole constructor.  (For invocation by subclass constructors, typically
	 * implicit.)
	 */
	protected AbstractIntCollection() {
	}

	// Query Operations

	/**
	 * {@inheritDoc}
	 * <p>This implementation returns size() == 0.
	 */
	public boolean isEmpty()
	{
		return size() == 0;
	}

	/**
	 * {@inheritDoc}
	 * <p>This implementation iterates over the elements in the collection,
	 * checking each element in turn for equality with the specified element.
	 *
	 * @throws ClassCastException   {@inheritDoc}
	 * @throws NullPointerException {@inheritDoc}
	 */
	public boolean contains(int o) {
		IntIterator e = iterator();
		while(e.hasNext()) {
			if(o == e.nextInt()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 * <p>This implementation returns an array containing all the elements
	 * returned by this collection's iterator, in the same order, stored in
	 * consecutive elements of the array, starting with index {@code 0}.
	 * The length of the returned array is equal to the number of elements
	 * returned by the iterator, even if the size of this collection changes
	 * during iteration, as might happen if the collection permits
	 * concurrent modification during iteration.  The {@code size} method is
	 * called only as an optimization hint; the correct result is returned
	 * even if the iterator returns a different number of elements.
	 * </p>
	 * <p>This method is equivalent to:
	 * </p>
	 * <pre> {@code
	 * List<E> list = new ArrayList<E>(size());
	 * for (E e : this)
	 *     list.add(e);
	 * return list.toArray();
	 * }</pre>
	 */
	public int[] toArray()
	{
		// Estimate size of array; be prepared to see more or fewer elements
		int[] r = new int[size()];
		IntIterator it = iterator();
		for(int i = 0; i < r.length; i++) {
			if(!it.hasNext()) {	// fewer elements than expected
				return Arrays.copyOf(r, i);
			}
			r[i] = it.nextInt();
		}
		return it.hasNext() ? finishToArray(r, it) : r;
	}

	/**
	 * {@inheritDoc}
	 * <p>This implementation returns an array containing all the elements
	 * returned by this collection's iterator in the same order, stored in
	 * consecutive elements of the array, starting with index {@code 0}.
	 * If the number of elements returned by the iterator is too large to
	 * fit into the specified array, then the elements are returned in a
	 * newly allocated array with length equal to the number of elements
	 * returned by the iterator, even if the size of this collection
	 * changes during iteration, as might happen if the collection permits
	 * concurrent modification during iteration.  The {@code size} method is
	 * called only as an optimization hint; the correct result is returned
	 * even if the iterator returns a different number of elements.
	 * </p>
	 * <p>This method is equivalent to:
	 * </p>
	 * <pre> {@code
	 * List<E> list = new ArrayList<E>(size());
	 * for (E e : this)
	 *     list.add(e);
	 * return list.toArray(a);
	 * }</pre>
	 *
	 * @throws ArrayStoreException  {@inheritDoc}
	 * @throws NullPointerException {@inheritDoc}
	 */
	public int[] toArray(int[] a) {
		// Estimate size of array; be prepared to see more or fewer elements
		int size = size();
		int[] r = a.length >= size ? a : new int[size];
		IntIterator it = iterator();

		for(int i = 0; i < r.length; i++) {
			if(!it.hasNext()) { // fewer elements than expected
				if(a != r) {
					return Arrays.copyOf(r, i);
				}
				r[i] = 0; // null-terminate
				return r;
			}
			r[i] = it.nextInt();
		}
		return it.hasNext() ? finishToArray(r, it) : r;
	}

	/**
	 * Reallocates the array being used within toArray when the iterator
	 * returned more elements than expected, and finishes filling it from
	 * the iterator.
	 *
	 * @param r  the array, replete with previously stored elements
	 * @param it the in-progress iterator over this collection
	 * @return array containing the elements in the given array, plus any
	 *         further elements returned by the iterator, trimmed to size
	 */
	private static int[] finishToArray(int[] r, IntIterator it) {
		int i = r.length;
		while(it.hasNext()) {
			int cap = r.length;
			if(i == cap) {
				int newCap = increaseCapacity(cap);
				r = Arrays.copyOf(r, newCap);
			}
			r[i++] = it.nextInt();
		}
		// trim if overallocated
		return (i == r.length) ? r : Arrays.copyOf(r, i);
	}

	private static int increaseCapacity(int cap) {
		int newCap = ((cap / 2) + 1) * 3;
		if(newCap <= cap) {
			// integer overflow
			if(cap == Integer.MAX_VALUE) {
				throw new OutOfMemoryError("Required array size too large");
			}
			newCap = Integer.MAX_VALUE;
		}
		return newCap;
	}

	// Modification Operations

	/**
	 * {@inheritDoc}
	 * <p>This implementation always throws an
	 * UnsupportedOperationException.
	 *
	 * @throws UnsupportedOperationException {@inheritDoc}
	 * @throws ClassCastException			{@inheritDoc}
	 * @throws NullPointerException		  {@inheritDoc}
	 * @throws IllegalArgumentException	  {@inheritDoc}
	 * @throws IllegalStateException		 {@inheritDoc}
	 */
	public boolean add(int e) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * <p>This implementation iterates over the collection looking for the
	 * specified element.  If it finds the element, it removes the element
	 * from the collection using the iterator's remove method.
	 * </p>
	 * <p>Note that this implementation throws an
	 * UnsupportedOperationException if the iterator returned by this
	 * collection's iterator method does not implement the remove
	 * method and this collection contains the specified object.
	 *
	 * @throws UnsupportedOperationException {@inheritDoc}
	 * @throws ClassCastException			{@inheritDoc}
	 * @throws NullPointerException		  {@inheritDoc}
	 */
	public boolean remove(int o) {
		IntIterator e = iterator();
		while(e.hasNext()) {
			if(o == e.nextInt()) {
				e.remove();
				return true;
			}
		}
		return false;
	}


	// Bulk Operations

	/**
	 * {@inheritDoc}
	 * <p>This implementation iterates over the specified collection,
	 * checking each element returned by the iterator in turn to see
	 * if it's contained in this collection.  If all elements are so
	 * contained true is returned, otherwise false.
	 *
	 * @throws ClassCastException   {@inheritDoc}
	 * @throws NullPointerException {@inheritDoc}
	 * @see #contains(int)
	 */
	public boolean containsAll(IntCollection c) {
		IntIterator e = c.iterator();
		while(e.hasNext()) {
			if(!contains(e.nextInt())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 * <p>This implementation iterates over the specified collection, and adds
	 * each object returned by the iterator to this collection, in turn.
	 * </p>
	 * <p>Note that this implementation will throw an
	 * UnsupportedOperationException unless add is
	 * overridden (assuming the specified collection is non-empty).
	 *
	 * @throws UnsupportedOperationException {@inheritDoc}
	 * @throws ClassCastException			{@inheritDoc}
	 * @throws NullPointerException		  {@inheritDoc}
	 * @throws IllegalArgumentException	  {@inheritDoc}
	 * @throws IllegalStateException		 {@inheritDoc}
	 * @see #add(int)
	 */
	public boolean addAll(IntCollection c) {
		boolean modified = false;
		IntIterator e = c.iterator();
		while(e.hasNext()) {
			modified |= add(e.nextInt());
		}
		return modified;
	}

	/**
	 * {@inheritDoc}
	 * <p>This implementation iterates over the specified collection, and adds
	 * each object returned by the iterator to this collection, in turn.
	 * </p>
	 * <p>Note that this implementation will throw an
	 * UnsupportedOperationException unless add is
	 * overridden (assuming the specified collection is non-empty).
	 *
	 * @throws UnsupportedOperationException {@inheritDoc}
	 * @throws ClassCastException			{@inheritDoc}
	 * @throws NullPointerException		  {@inheritDoc}
	 * @throws IllegalArgumentException	  {@inheritDoc}
	 * @throws IllegalStateException		 {@inheritDoc}
	 * @see #add(int)
	 */
	public boolean addAll(int[] items) {
		if(items.length <= 0) {
			return false;
		}
		for (int item : items) {
			add(item);
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 * <p>This implementation iterates over this collection, checking each
	 * element returned by the iterator in turn to see if it's contained
	 * in the specified collection.  If it's so contained, it's removed from
	 * this collection with the iterator's remove method.
	 * </p>
	 * <p>Note that this implementation will throw an
	 * UnsupportedOperationException if the iterator returned by the
	 * iterator method does not implement the remove method
	 * and this collection contains one or more elements in common with the
	 * specified collection.
	 *
	 * @throws UnsupportedOperationException {@inheritDoc}
	 * @throws ClassCastException			{@inheritDoc}
	 * @throws NullPointerException		  {@inheritDoc}
	 * @see #remove(int)
	 * @see #contains(int)
	 */
	public boolean removeAll(IntCollection c) {
		boolean modified = false;
		IntIterator e = iterator();
		while(e.hasNext()) {
			if(c.contains(e.nextInt())) {
				e.remove();
				modified = true;
			}
		}
		return modified;
	}

	/**
	 * {@inheritDoc}
	 * <p>This implementation iterates over this collection, checking each
	 * element returned by the iterator in turn to see if it's contained
	 * in the specified collection.  If it's not so contained, it's removed
	 * from this collection with the iterator's remove method.
	 * </p>
	 * <p>Note that this implementation will throw an
	 * UnsupportedOperationException if the iterator returned by the
	 * iterator method does not implement the remove method
	 * and this collection contains one or more elements not present in the
	 * specified collection.
	 *
	 * @throws UnsupportedOperationException {@inheritDoc}
	 * @throws ClassCastException			{@inheritDoc}
	 * @throws NullPointerException		  {@inheritDoc}
	 * @see #remove(int)
	 * @see #contains(int)
	 */
	public boolean retainAll(IntCollection c) {
		boolean modified = false;
		IntIterator e = iterator();
		while(e.hasNext()) {
			if(!c.contains(e.nextInt())) {
				e.remove();
				modified = true;
			}
		}
		return modified;
	}

	/**
	 * {@inheritDoc}
	 * <p>This implementation iterates over this collection, removing each
	 * element using the Iterator.remove operation.  Most
	 * implementations will probably choose to override this method for
	 * efficiency.
	 * </p>
	 * <p>Note that this implementation will throw an
	 * UnsupportedOperationException if the iterator returned by this
	 * collection's iterator method does not implement the
	 * remove method and this collection is non-empty.
	 *
	 * @throws UnsupportedOperationException {@inheritDoc}
	 */
	public void clear() {
		IntIterator e = iterator();
		while(e.hasNext()) {
			e.nextInt();
			e.remove();
		}
	}

	//  String conversion

	/**
	 * Returns a string representation of this collection.  The string
	 * representation consists of a list of the collection's elements in the
	 * order they are returned by its iterator, enclosed in square brackets
	 * ("[]").  Adjacent elements are separated by the characters
	 * ", " (comma and space).  Elements are converted to strings as
	 * by {@link String#valueOf(Object)}.
	 *
	 * @return a string representation of this collection
	 */
	public String toString() {
		IntIterator i = iterator();
		if(!i.hasNext()) {
			return "[]";
		}

		StringBuilder sb = new StringBuilder();
		sb.append('[');
		for(; ;) {
			int e = i.nextInt();
			sb.append(e);
			if(!i.hasNext()) {
				return sb.append(']').toString();
			}
			sb.append(", ");
		}
	}
}
