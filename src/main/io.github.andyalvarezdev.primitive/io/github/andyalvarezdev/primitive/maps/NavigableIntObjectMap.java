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
package io.github.andyalvarezdev.primitive.maps;

import io.github.andyalvarezdev.primitive.pair.IntObjectPair;
import io.github.andyalvarezdev.primitive.sets.NavigableIntSet;

/**
 * <p>
 * A {@link SortedIntObjectMap} extended with navigation methods returning the
 * closest matches for given search targets. Methods
 * {@code lowerEntry}, {@code floorEntry}, {@code ceilingEntry},
 * and {@code higherEntry} return {@code Map.Entry} objects
 * associated with keys respectively less than, less than or equal,
 * greater than or equal, and greater than a given key, returning
 * {@code null} if there is no such key.  Similarly, methods
 * {@code lowerKey}, {@code floorKey}, {@code ceilingKey}, and
 * {@code higherKey} return only the associated keys. All of these
 * methods are designed for locating, not traversing entries.
 * </p>
 * <p>A {@code NavigableMap} may be accessed and traversed in either
 * ascending or descending key order.  The {@code descendingMap}
 * method returns a view of the map with the senses of all relational
 * and directional methods inverted. The performance of ascending
 * operations and views is likely to be faster than that of descending
 * ones.  Methods {@code subMap}, {@code headMap},
 * and {@code tailMap} differ from the like-named {@code
 * SortedMap} methods in accepting additional arguments describing
 * whether lower and upper bounds are inclusive versus exclusive.
 * Submaps of any {@code NavigableMap} must implement the {@code
 * NavigableMap} interface.
 * </p>
 * <p>This interface additionally defines methods {@code firstEntry},
 * {@code pollFirstEntry}, {@code lastEntry}, and
 * {@code pollLastEntry} that return and/or remove the least and
 * greatest mappings, if any exist, else returning {@code null}.
 * </p>
 * <p>Implementations of entry-returning methods are expected to
 * return {@code Map.Entry} pairs representing snapshots of mappings
 * at the time they were produced, and thus generally do <em>not</em>
 * support the optional {@code Entry.setValue} method. Note however
 * that it is possible to change mappings in the associated map using
 * method {@code put}.
 * </p>
 * <p>Methods
 * {@link #subMap(int, int) subMap(K, K)},
 * {@link #headMap(int) headMap(K)}, and
 * {@link #tailMap(int) tailMap(K)}
 * are specified to return {@code SortedMap} to allow existing
 * implementations of {@code SortedMap} to be compatibly retrofitted to
 * implement {@code NavigableMap}, but extensions and implementations
 * of this interface are encouraged to override these methods to return
 * {@code NavigableMap}.  Similarly,
 * {@link #keySet()} can be overriden to return {@code NavigableSet}.
 * </p>
 * <p>This interface is a member of the
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a>.
 *
 * @param <V> the type of mapped values
 */
public interface NavigableIntObjectMap<V> extends SortedIntObjectMap<V>
{
	/**
	 * Returns a key-value mapping associated with the greatest key
	 * strictly less than the given key, or {@code null} if there is
	 * no such key.
	 *
	 * @param key the key
	 * @return an entry with the greatest key less than {@code key},
	 *         or {@code null} if there is no such key
	 * @throws ClassCastException   if the specified key cannot be compared
	 *                              with the keys currently in the map
	 * @throws NullPointerException if the specified key is null
	 *                              and this map does not permit null keys
	 */
	IntObjectPair<V> lowerEntry(int key);

	/**
	 * Returns the greatest key strictly less than the given key, or
	 * {@code null} if there is no such key.
	 *
	 * @param key the key
	 * @return the greatest key less than {@code key},
	 *         or {@code null} if there is no such key
	 * @throws ClassCastException   if the specified key cannot be compared
	 *                              with the keys currently in the map
	 * @throws NullPointerException if the specified key is null
	 *                              and this map does not permit null keys
	 */
	int lowerKey(int key);

	/**
	 * Returns a key-value mapping associated with the greatest key
	 * less than or equal to the given key, or {@code null} if there
	 * is no such key.
	 *
	 * @param key the key
	 * @return an entry with the greatest key less than or equal to
	 *         {@code key}, or {@code null} if there is no such key
	 * @throws ClassCastException   if the specified key cannot be compared
	 *                              with the keys currently in the map
	 * @throws NullPointerException if the specified key is null
	 *                              and this map does not permit null keys
	 */
	IntObjectPair<V> floorEntry(int key);

	/**
	 * Returns the greatest key less than or equal to the given key,
	 * or {@code null} if there is no such key.
	 *
	 * @param key the key
	 * @return the greatest key less than or equal to {@code key},
	 *         or {@code null} if there is no such key
	 * @throws ClassCastException   if the specified key cannot be compared
	 *                              with the keys currently in the map
	 * @throws NullPointerException if the specified key is null
	 *                              and this map does not permit null keys
	 */
	int floorKey(int key);

	/**
	 * Returns a key-value mapping associated with the least key
	 * greater than or equal to the given key, or {@code null} if
	 * there is no such key.
	 *
	 * @param key the key
	 * @return an entry with the least key greater than or equal to
	 *         {@code key}, or {@code null} if there is no such key
	 * @throws ClassCastException   if the specified key cannot be compared
	 *                              with the keys currently in the map
	 * @throws NullPointerException if the specified key is null
	 *                              and this map does not permit null keys
	 */
	IntObjectPair<V> ceilingEntry(int key);

	/**
	 * Returns the least key greater than or equal to the given key,
	 * or {@code null} if there is no such key.
	 *
	 * @param key the key
	 * @return the least key greater than or equal to {@code key},
	 *         or {@code null} if there is no such key
	 * @throws ClassCastException   if the specified key cannot be compared
	 *                              with the keys currently in the map
	 * @throws NullPointerException if the specified key is null
	 *                              and this map does not permit null keys
	 */
	int ceilingKey(int key);

	/**
	 * Returns a key-value mapping associated with the least key
	 * strictly greater than the given key, or {@code null} if there
	 * is no such key.
	 *
	 * @param key the key
	 * @return an entry with the least key greater than {@code key},
	 *         or {@code null} if there is no such key
	 * @throws ClassCastException   if the specified key cannot be compared
	 *                              with the keys currently in the map
	 * @throws NullPointerException if the specified key is null
	 *                              and this map does not permit null keys
	 */
	IntObjectPair<V> higherEntry(int key);

	/**
	 * Returns the least key strictly greater than the given key, or
	 * {@code null} if there is no such key.
	 *
	 * @param key the key
	 * @return the least key greater than {@code key},
	 *         or {@code null} if there is no such key
	 * @throws ClassCastException   if the specified key cannot be compared
	 *                              with the keys currently in the map
	 * @throws NullPointerException if the specified key is null
	 *                              and this map does not permit null keys
	 */
	int higherKey(int key);

	/**
	 * Returns a key-value mapping associated with the least
	 * key in this map, or {@code null} if the map is empty.
	 *
	 * @return an entry with the least key,
	 *         or {@code null} if this map is empty
	 */
	IntObjectPair<V> firstEntry();

	/**
	 * Returns a key-value mapping associated with the greatest
	 * key in this map, or {@code null} if the map is empty.
	 *
	 * @return an entry with the greatest key,
	 *         or {@code null} if this map is empty
	 */
	IntObjectPair<V> lastEntry();

	/**
	 * Removes and returns a key-value mapping associated with
	 * the least key in this map, or {@code null} if the map is empty.
	 *
	 * @return the removed first entry of this map,
	 *         or {@code null} if this map is empty
	 */
	IntObjectPair<V> pollFirstEntry();

	/**
	 * Removes and returns a key-value mapping associated with
	 * the greatest key in this map, or {@code null} if the map is empty.
	 *
	 * @return the removed last entry of this map,
	 *         or {@code null} if this map is empty
	 */
	IntObjectPair<V> pollLastEntry();

	/**
	 * <p>
	 * Returns a reverse order view of the mappings contained in this map.
	 * The descending map is backed by this map, so changes to the map are
	 * reflected in the descending map, and vice-versa.  If either map is
	 * modified while an iteration over a collection view of either map
	 * is in progress (except through the iterator's own {@code remove}
	 * operation), the results of the iteration are undefined.
	 * </p>
	 * <p>The returned map has an ordering equivalent to
	 * {@link java.util.Collections#reverseOrder(java.util.Comparator) Collections.reverseOrder}(comparator()).
	 * The expression {@code m.descendingMap().descendingMap()} returns a
	 * view of {@code m} essentially equivalent to {@code m}.
	 *
	 * @return a reverse order view of this map
	 */
	NavigableIntObjectMap<V> descendingMap();

	/**
	 * Returns a {@link NavigableIntSet} view of the keys contained in this map.
	 * The set's iterator returns the keys in ascending order.
	 * The set is backed by the map, so changes to the map are reflected in
	 * the set, and vice-versa.  If the map is modified while an iteration
	 * over the set is in progress (except through the iterator's own {@code
	 * remove} operation), the results of the iteration are undefined.  The
	 * set supports element removal, which removes the corresponding mapping
	 * from the map, via the {@code Iterator.remove}, {@code Set.remove},
	 * {@code removeAll}, {@code retainAll}, and {@code clear} operations.
	 * It does not support the {@code add} or {@code addAll} operations.
	 *
	 * @return a navigable set view of the keys in this map
	 */
	NavigableIntSet navigableKeySet();

	/**
	 * Returns a reverse order {@link NavigableIntSet} view of the keys contained in this map.
	 * The set's iterator returns the keys in descending order.
	 * The set is backed by the map, so changes to the map are reflected in
	 * the set, and vice-versa.  If the map is modified while an iteration
	 * over the set is in progress (except through the iterator's own {@code
	 * remove} operation), the results of the iteration are undefined.  The
	 * set supports element removal, which removes the corresponding mapping
	 * from the map, via the {@code Iterator.remove}, {@code Set.remove},
	 * {@code removeAll}, {@code retainAll}, and {@code clear} operations.
	 * It does not support the {@code add} or {@code addAll} operations.
	 *
	 * @return a reverse order navigable set view of the keys in this map
	 */
	NavigableIntSet descendingKeySet();

	/**
	 * <p>
	 * Returns a view of the portion of this map whose keys range from
	 * {@code fromKey} to {@code toKey}.  If {@code fromKey} and
	 * {@code toKey} are equal, the returned map is empty unless
	 * {@code fromExclusive} and {@code toExclusive} are both true.  The
	 * returned map is backed by this map, so changes in the returned map are
	 * reflected in this map, and vice-versa.  The returned map supports all
	 * optional map operations that this map supports.
	 * </p>
	 * <p>The returned map will throw an {@code IllegalArgumentException}
	 * on an attempt to insert a key outside of its range, or to construct a
	 * submap either of whose endpoints lie outside its range.
	 *
	 * @param fromKey	   low endpoint of the keys in the returned map
	 * @param fromInclusive {@code true} if the low endpoint
	 *                      is to be included in the returned view
	 * @param toKey		 high endpoint of the keys in the returned map
	 * @param toInclusive   {@code true} if the high endpoint
	 *                      is to be included in the returned view
	 * @return a view of the portion of this map whose keys range from
	 *         {@code fromKey} to {@code toKey}
	 * @throws ClassCastException	   if {@code fromKey} and {@code toKey}
	 *                                  cannot be compared to one another using this map's comparator
	 *                                  (or, if the map has no comparator, using natural ordering).
	 *                                  Implementations may, but are not required to, throw this
	 *                                  exception if {@code fromKey} or {@code toKey}
	 *                                  cannot be compared to keys currently in the map.
	 * @throws NullPointerException	 if {@code fromKey} or {@code toKey}
	 *                                  is null and this map does not permit null keys
	 * @throws IllegalArgumentException if {@code fromKey} is greater than
	 *                                  {@code toKey}; or if this map itself has a restricted
	 *                                  range, and {@code fromKey} or {@code toKey} lies
	 *                                  outside the bounds of the range
	 */
	NavigableIntObjectMap<V> subMap(int fromKey, boolean fromInclusive, int toKey, boolean toInclusive);

	/**
	 * <p>
	 * Returns a view of the portion of this map whose keys are less than (or
	 * equal to, if {@code inclusive} is true) {@code toKey}.  The returned
	 * map is backed by this map, so changes in the returned map are reflected
	 * in this map, and vice-versa.  The returned map supports all optional
	 * map operations that this map supports.
	 * </p>
	 * <p>The returned map will throw an {@code IllegalArgumentException}
	 * on an attempt to insert a key outside its range.
	 *
	 * @param toKey	 high endpoint of the keys in the returned map
	 * @param inclusive {@code true} if the high endpoint
	 *                  is to be included in the returned view
	 * @return a view of the portion of this map whose keys are less than
	 *         (or equal to, if {@code inclusive} is true) {@code toKey}
	 * @throws ClassCastException	   if {@code toKey} is not compatible
	 *                                  with this map's comparator (or, if the map has no comparator,
	 *                                  if {@code toKey} does not implement {@link Comparable}).
	 *                                  Implementations may, but are not required to, throw this
	 *                                  exception if {@code toKey} cannot be compared to keys
	 *                                  currently in the map.
	 * @throws NullPointerException	 if {@code toKey} is null
	 *                                  and this map does not permit null keys
	 * @throws IllegalArgumentException if this map itself has a
	 *                                  restricted range, and {@code toKey} lies outside the
	 *                                  bounds of the range
	 */
	NavigableIntObjectMap<V> headMap(int toKey, boolean inclusive);

	/**
	 * <p>
	 * Returns a view of the portion of this map whose keys are greater than (or
	 * equal to, if {@code inclusive} is true) {@code fromKey}.  The returned
	 * map is backed by this map, so changes in the returned map are reflected
	 * in this map, and vice-versa.  The returned map supports all optional
	 * map operations that this map supports.
	 * </p>
	 * <p>The returned map will throw an {@code IllegalArgumentException}
	 * on an attempt to insert a key outside its range.
	 *
	 * @param fromKey   low endpoint of the keys in the returned map
	 * @param inclusive {@code true} if the low endpoint
	 *                  is to be included in the returned view
	 * @return a view of the portion of this map whose keys are greater than
	 *         (or equal to, if {@code inclusive} is true) {@code fromKey}
	 * @throws ClassCastException	   if {@code fromKey} is not compatible
	 *                                  with this map's comparator (or, if the map has no comparator,
	 *                                  if {@code fromKey} does not implement {@link Comparable}).
	 *                                  Implementations may, but are not required to, throw this
	 *                                  exception if {@code fromKey} cannot be compared to keys
	 *                                  currently in the map.
	 * @throws NullPointerException	 if {@code fromKey} is null
	 *                                  and this map does not permit null keys
	 * @throws IllegalArgumentException if this map itself has a
	 *                                  restricted range, and {@code fromKey} lies outside the
	 *                                  bounds of the range
	 */
	NavigableIntObjectMap<V> tailMap(int fromKey, boolean inclusive);

	/**
	 * {@inheritDoc}
	 * <p>Equivalent to {@code subMap(fromKey, true, toKey, false)}.
	 *
	 * @throws ClassCastException	   {@inheritDoc}
	 * @throws NullPointerException	 {@inheritDoc}
	 * @throws IllegalArgumentException {@inheritDoc}
	 */
	SortedIntObjectMap<V> subMap(int fromKey, int toKey);

	/**
	 * {@inheritDoc}
	 * <p>Equivalent to {@code headMap(toKey, false)}.
	 *
	 * @throws ClassCastException	   {@inheritDoc}
	 * @throws NullPointerException	 {@inheritDoc}
	 * @throws IllegalArgumentException {@inheritDoc}
	 */
	SortedIntObjectMap<V> headMap(int toKey);

	/**
	 * {@inheritDoc}
	 * <p>Equivalent to {@code tailMap(fromKey, true)}.
	 *
	 * @throws ClassCastException	   {@inheritDoc}
	 * @throws NullPointerException	 {@inheritDoc}
	 * @throws IllegalArgumentException {@inheritDoc}
	 */
	SortedIntObjectMap<V> tailMap(int fromKey);
}
