package io.github.andyalvarezdev.primitive.maps.impl;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import io.github.andyalvarezdev.primitive.collections.IntCollection;
import io.github.andyalvarezdev.primitive.maps.abstracts.AbstractIntObjectMap;
import io.github.andyalvarezdev.primitive.pair.IntObjectPair;
import io.github.andyalvarezdev.primitive.pair.impl.ImmutableIntObjectPairImpl;
import io.github.andyalvarezdev.primitive.Comparators;
import io.github.andyalvarezdev.primitive.comparators.IntComparator;
import io.github.andyalvarezdev.primitive.iterators.IntIterator;
import io.github.andyalvarezdev.primitive.maps.IntObjectMap;
import io.github.andyalvarezdev.primitive.maps.NavigableIntObjectMap;
import io.github.andyalvarezdev.primitive.maps.SortedIntObjectMap;
import io.github.andyalvarezdev.primitive.sets.IntSet;
import io.github.andyalvarezdev.primitive.sets.NavigableIntSet;
import io.github.andyalvarezdev.primitive.sets.SortedIntSet;
import io.github.andyalvarezdev.primitive.sets.abstracts.AbstractIntSet;
import io.github.andyalvarezdev.primitive.sets.impl.TreeIntSet;

/**
 * <p>
 * A Red-Black tree based {@link NavigableIntObjectMap} implementation.
 * The map is sorted according to the {@linkplain Comparable natural
 * ordering} of its keys, or by a {@link IntComparator} provided at map
 * creation time, depending on which constructor is used.
 * </p>
 * <p>This implementation provides guaranteed log(n) time cost for the
 * containsKey, get, put and remove
 * operations.  Algorithms are adaptations of those in Cormen, Leiserson, and
 * Rivest's <I>Introduction to Algorithms</I>.
 * </p>
 * <p>Note that the ordering maintained by a sorted map (whether or not an
 * explicit comparator is provided) must be <i>consistent with equals</i> if
 * this sorted map is to correctly implement the Map interface.  (See
 * Comparable or Comparator for a precise definition of
 * <i>consistent with equals</i>.)  This is so because the Map
 * interface is defined in terms of the equals operation, but a map performs
 * all key comparisons using its compareTo (or compare)
 * method, so two keys that are deemed equal by this method are, from the
 * standpoint of the sorted map, equal.  The behavior of a sorted map
 * <i>is</i> well-defined even if its ordering is inconsistent with equals; it
 * just fails to obey the general contract of the Map interface.
 * </p>
 * <p><strong>Note that this implementation is not synchronized.</strong>
 * If multiple threads access a map concurrently, and at least one of the
 * threads modifies the map structurally, it <i>must</i> be synchronized
 * externally.  (A structural modification is any operation that adds or
 * deletes one or more mappings; merely changing the value associated
 * with an existing key is not a structural modification.)  This is
 * typically accomplished by synchronizing on some object that naturally
 * encapsulates the map.
 * If no such object exists, the map should be "wrapped" using the
 * {@link Collections#synchronizedSortedMap Collections.synchronizedSortedMap}
 * method.  This is best done at creation time, to prevent accidental
 * unsynchronized access to the map:</p>
 * <pre>
 *   SortedMap m = Collections.synchronizedSortedMap(new TreeIntObjectMap(...));</pre>
 *
 * <p>The iterators returned by the iterator method of the collections
 * returned by all of this class's "collection view methods" are
 * <i>fail-fast</i>: if the map is structurally modified at any time after the
 * iterator is created, in any way except through the iterator's own
 * remove method, the iterator will throw a {@link
 * ConcurrentModificationException}.  Thus, in the face of concurrent
 * modification, the iterator fails quickly and cleanly, rather than risking
 * arbitrary, non-deterministic behavior at an undetermined time in the future.
 * </p>
 * <p>Note that the fail-fast behavior of an iterator cannot be guaranteed
 * as it is, generally speaking, impossible to make any hard guarantees in the
 * presence of unsynchronized concurrent modification.  Fail-fast iterators
 * throw ConcurrentModificationException on a best-effort basis.
 * Therefore, it would be wrong to write a program that depended on this
 * exception for its correctness:   <i>the fail-fast behavior of iterators
 * should be used only to detect bugs.</i>
 * </p>
 * <p>All Map.Entry pairs returned by methods in this class
 * and its views represent snapshots of mappings at the time they were
 * produced. They do <em>not</em> support the Entry.setValue
 * method. (Note however that it is possible to change mappings in the
 * associated map using put.)
 * </p>
 * <p>This class is a member of the
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a>.
 *
 * @param <V> the type of mapped values
 * @author Josh Bloch and Doug Lea
 * @version 1.73, 05/10/06
 * @see IntObjectMap
 * @see HashIntObjectMap
 * @see Comparable
 * @see IntComparator
 * @see IntCollection
 * @since 1.0.0
 */
public class TreeIntObjectMap<V> extends AbstractIntObjectMap<V> implements NavigableIntObjectMap<V>, Cloneable, java.io.Serializable
{
	/**
	 * The comparator used to maintain order in this tree map, or
	 * null if it uses the natural ordering of its keys.
	 *
	 * @serial
	 */
	private final IntComparator comparator;

	private transient Entry<V> root = null;

	/**
	 * The number of entries in the tree
	 */
	private transient int size = 0;

	/**
	 * The number of structural modifications to the tree.
	 */
	private transient int modCount = 0;

	/**
	 * Constructs a new, empty tree map, using the natural ordering of its
	 * keys.  All keys inserted into the map must implement the {@link
	 * Comparable} interface.  Furthermore, all such keys must be
	 * <i>mutually comparable</i>: k1.compareTo(k2) must not throw
	 * a ClassCastException for any keys k1 and
	 * k2 in the map.  If the user attempts to put a key into the
	 * map that violates this constraint (for example, the user attempts to
	 * put a string key into a map whose keys are integers), the
	 * put(Object key, Object value) call will throw a
	 * ClassCastException.
	 */
	public TreeIntObjectMap()
	{
		comparator = null;
	}

	/**
	 * Constructs a new, empty tree map, ordered according to the given
	 * comparator.  All keys inserted into the map must be <i>mutually
	 * comparable</i> by the given comparator: comparator.compare(k1,
	 * k2) must not throw a ClassCastException for any keys
	 * k1 and k2 in the map.  If the user attempts to put
	 * a key into the map that violates this constraint, the put(Object
	 * key, Object value) call will throw a
	 * ClassCastException.
	 *
	 * @param comparator the comparator that will be used to order this map.
	 *                   If null, the {@linkplain Comparable natural
	 *                   ordering} of the keys will be used.
	 */
	public TreeIntObjectMap(IntComparator comparator)
	{
		this.comparator = comparator;
	}

	/**
	 * Constructs a new tree map containing the same mappings as the given
	 * map, ordered according to the <i>natural ordering</i> of its keys.
	 * All keys inserted into the new map must implement the {@link
	 * Comparable} interface.  Furthermore, all such keys must be
	 * <i>mutually comparable</i>: k1.compareTo(k2) must not throw
	 * a ClassCastException for any keys k1 and
	 * k2 in the map.  This method runs in n*log(n) time.
	 *
	 * @param m the map whose mappings are to be placed in this map
	 * @throws ClassCastException   if the keys in m are not {@link Comparable},
	 *                              or are not mutually comparable
	 * @throws NullPointerException if the specified map is null
	 */
	public TreeIntObjectMap(IntObjectMap<? extends V> m)
	{
		comparator = null;
		putAll(m);
	}

	/**
	 * Constructs a new tree map containing the same mappings and
	 * using the same ordering as the specified sorted map.  This
	 * method runs in linear time.
	 *
	 * @param m the sorted map whose mappings are to be placed in this map,
	 *          and whose comparator is to be used to sort this map
	 * @throws NullPointerException if the specified map is null
	 */
	public TreeIntObjectMap(SortedIntObjectMap<? extends V> m)
	{
		comparator = m.comparator();
		try
		{
			buildFromSorted(m.size(), m.entrySet().iterator(), null, null);
		}
		catch(java.io.IOException cannotHappen)
		{
		}
		catch(ClassNotFoundException cannotHappen)
		{
		}
	}


	// Query Operations

	/**
	 * Returns the number of key-value mappings in this map.
	 *
	 * @return the number of key-value mappings in this map
	 */
	public int size()
	{
		return size;
	}

	/**
	 * Returns true if this map contains a mapping for the specified
	 * key.
	 *
	 * @param key key whose presence in this map is to be tested
	 * @return true if this map contains a mapping for the
	 *         specified key
	 * @throws ClassCastException   if the specified key cannot be compared
	 *                              with the keys currently in the map
	 * @throws NullPointerException if the specified key is null
	 *                              and this map uses natural ordering, or its comparator
	 *                              does not permit null keys
	 */
	public boolean containsKey(int key)
	{
		return getEntry(key) != null;
	}

	/**
	 * Returns true if this map maps one or more keys to the
	 * specified value.  More formally, returns true if and only if
	 * this map contains at least one mapping to a value v such
	 * that (value==null ? v==null : value.equals(v)).  This
	 * operation will probably require time linear in the map size for
	 * most implementations.
	 *
	 * @param value value whose presence in this map is to be tested
	 * @return true if a mapping to value exists;
	 *         false otherwise
	 * @since 1.0.0
	 */
	public boolean containsValue(Object value)
	{
		for(Entry<V> e = getFirstEntry(); e != null; e = successor(e))
		{
			if(valEquals(value, e.value))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * <p>
	 * Returns the value to which the specified key is mapped,
	 * or {@code null} if this map contains no mapping for the key.
	 * </p>
	 * <p>More formally, if this map contains a mapping from a key
	 * {@code k} to a value {@code v} such that {@code key} compares
	 * equal to {@code k} according to the map's ordering, then this
	 * method returns {@code v}; otherwise it returns {@code null}.
	 * (There can be at most one such mapping.)
	 * </p>
	 * <p>A return value of {@code null} does not <i>necessarily</i>
	 * indicate that the map contains no mapping for the key; it's also
	 * possible that the map explicitly maps the key to {@code null}.
	 * The {@link #containsKey containsKey} operation may be used to
	 * distinguish these two cases.
	 *
	 * @throws ClassCastException   if the specified key cannot be compared
	 *                              with the keys currently in the map
	 * @throws NullPointerException if the specified key is null
	 *                              and this map uses natural ordering, or its comparator
	 *                              does not permit null keys
	 */
	public V get(int key)
	{
		Entry<V> p = getEntry(key);
		return (p == null ? null : p.value);
	}

	public IntComparator comparator()
	{
		return comparator;
	}

	/**
	 * @throws NoSuchElementException {@inheritDoc}
	 */
	public int firstKey()
	{
		return key(getFirstEntry());
	}

	/**
	 * @throws NoSuchElementException {@inheritDoc}
	 */
	public int lastKey()
	{
		return key(getLastEntry());
	}

	/**
	 * Copies all of the mappings from the specified map to this map.
	 * These mappings replace any mappings that this map had for any
	 * of the keys currently in the specified map.
	 *
	 * @param map mappings to be stored in this map
	 * @throws ClassCastException   if the class of a key or value in
	 *                              the specified map prevents it from being stored in this map
	 * @throws NullPointerException if the specified map is null or
	 *                              the specified map contains a null key and this map does not
	 *                              permit null keys
	 */
	public void putAll(IntObjectMap<? extends V> map)
	{
		int mapSize = map.size();
		if(size == 0 && mapSize != 0 && map instanceof SortedIntObjectMap)
		{
			IntComparator c = ((SortedIntObjectMap) map).comparator();
			if(c == comparator || (c != null && c.equals(comparator)))
			{
				++modCount;
				try
				{
					buildFromSorted(mapSize, map.entrySet().iterator(), null, null);
				}
				catch(java.io.IOException cannotHappen)
				{
				}
				catch(ClassNotFoundException cannotHappen)
				{
				}
				return;
			}
		}
		super.putAll(map);
	}

	/**
	 * Returns this map's entry for the given key, or null if the map
	 * does not contain an entry for the key.
	 *
	 * @return this map's entry for the given key, or null if the map
	 *         does not contain an entry for the key
	 * @throws ClassCastException   if the specified key cannot be compared
	 *                              with the keys currently in the map
	 * @throws NullPointerException if the specified key is null
	 *                              and this map uses natural ordering, or its comparator
	 *                              does not permit null keys
	 */
	final Entry<V> getEntry(int key)
	{
		// Offload comparator-based version for sake of performance
		if(comparator != null)
		{
			return getEntryUsingComparator(key);
		}

		Entry<V> p = root;
		while(p != null)
		{
			int cmp = Comparators.DEFAULT_INT_COMPARATOR.compare(key, p.key);
			if(cmp < 0)
			{
				p = p.left;
			}
			else if(cmp > 0)
			{
				p = p.right;
			}
			else
			{
				return p;
			}
		}
		return null;
	}

	/**
	 * Version of getEntry using comparator. Split off from getEntry
	 * for performance. (This is not worth doing for most methods,
	 * that are less dependent on comparator performance, but is
	 * worthwhile here.)
	 */
	final Entry<V> getEntryUsingComparator(int key)
	{
		IntComparator cpr = comparator;
		if(cpr != null)
		{
			Entry<V> p = root;
			while(p != null)
			{
				int cmp = cpr.compare(key, p.key);
				if(cmp < 0)
				{
					p = p.left;
				}
				else if(cmp > 0)
				{
					p = p.right;
				}
				else
				{
					return p;
				}
			}
		}
		return null;
	}

	/**
	 * Gets the entry corresponding to the specified key; if no such entry
	 * exists, returns the entry for the least key greater than the specified
	 * key; if no such entry exists (i.e., the greatest key in the Tree is less
	 * than the specified key), returns null.
	 */
	final Entry<V> getCeilingEntry(int key)
	{
		Entry<V> p = root;
		while(p != null)
		{
			int cmp = compare(key, p.key);
			if(cmp < 0)
			{
				if(p.left != null)
				{
					p = p.left;
				}
				else
				{
					return p;
				}
			}
			else if(cmp > 0)
			{
				if(p.right != null)
				{
					p = p.right;
				}
				else
				{
					Entry<V> parent = p.parent;
					Entry<V> ch = p;
					while(parent != null && ch == parent.right)
					{
						ch = parent;
						parent = parent.parent;
					}
					return parent;
				}
			}
			else
			{
				return p;
			}
		}
		return null;
	}

	/**
	 * Gets the entry corresponding to the specified key; if no such entry
	 * exists, returns the entry for the greatest key less than the specified
	 * key; if no such entry exists, returns null.
	 */
	final Entry<V> getFloorEntry(int key)
	{
		Entry<V> p = root;
		while(p != null)
		{
			int cmp = compare(key, p.key);
			if(cmp > 0)
			{
				if(p.right != null)
				{
					p = p.right;
				}
				else
				{
					return p;
				}
			}
			else if(cmp < 0)
			{
				if(p.left != null)
				{
					p = p.left;
				}
				else
				{
					Entry<V> parent = p.parent;
					Entry<V> ch = p;
					while(parent != null && ch == parent.left)
					{
						ch = parent;
						parent = parent.parent;
					}
					return parent;
				}
			}
			else
			{
				return p;
			}

		}
		return null;
	}

	/**
	 * Gets the entry for the least key greater than the specified
	 * key; if no such entry exists, returns the entry for the least
	 * key greater than the specified key; if no such entry exists
	 * returns null.
	 */
	final Entry<V> getHigherEntry(int key)
	{
		Entry<V> p = root;
		while(p != null)
		{
			int cmp = compare(key, p.key);
			if(cmp < 0)
			{
				if(p.left != null)
				{
					p = p.left;
				}
				else
				{
					return p;
				}
			}
			else
			{
				if(p.right != null)
				{
					p = p.right;
				}
				else
				{
					Entry<V> parent = p.parent;
					Entry<V> ch = p;
					while(parent != null && ch == parent.right)
					{
						ch = parent;
						parent = parent.parent;
					}
					return parent;
				}
			}
		}
		return null;
	}

	/**
	 * Returns the entry for the greatest key less than the specified key; if
	 * no such entry exists (i.e., the least key in the Tree is greater than
	 * the specified key), returns null.
	 */
	final Entry<V> getLowerEntry(int key)
	{
		Entry<V> p = root;
		while(p != null)
		{
			int cmp = compare(key, p.key);
			if(cmp > 0)
			{
				if(p.right != null)
				{
					p = p.right;
				}
				else
				{
					return p;
				}
			}
			else
			{
				if(p.left != null)
				{
					p = p.left;
				}
				else
				{
					Entry<V> parent = p.parent;
					Entry<V> ch = p;
					while(parent != null && ch == parent.left)
					{
						ch = parent;
						parent = parent.parent;
					}
					return parent;
				}
			}
		}
		return null;
	}

	/**
	 * Associates the specified value with the specified key in this map.
	 * If the map previously contained a mapping for the key, the old
	 * value is replaced.
	 *
	 * @param key   key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 * @return the previous value associated with key, or
	 *         null if there was no mapping for key.
	 *         (A null return can also indicate that the map
	 *         previously associated null with key.)
	 * @throws ClassCastException   if the specified key cannot be compared
	 *                              with the keys currently in the map
	 * @throws NullPointerException if the specified key is null
	 *                              and this map uses natural ordering, or its comparator
	 *                              does not permit null keys
	 */
	public V put(int key, V value)
	{
		Entry<V> t = root;
		if(t == null)
		{
			// TBD:
			// 5045147: (coll) Adding null to an empty TreeSet should
			// throw NullPointerException
			//
			// compare(key, key); // type check
			root = new Entry<V>(key, value, null);
			size = 1;
			modCount++;
			return null;
		}
		int cmp;
		Entry<V> parent;
		// split comparator and comparable paths
		IntComparator cpr = comparator;
		if(cpr != null)
		{
			do
			{
				parent = t;
				cmp = cpr.compare(key, t.key);
				if(cmp < 0)
				{
					t = t.left;
				}
				else if(cmp > 0)
				{
					t = t.right;
				}
				else
				{
					return t.setValue(value);
				}
			}
			while(t != null);
		}
		else
		{
			do
			{
				parent = t;
				cmp = Comparators.DEFAULT_INT_COMPARATOR.compare(key, t.key);
				if(cmp < 0)
				{
					t = t.left;
				}
				else if(cmp > 0)
				{
					t = t.right;
				}
				else
				{
					return t.setValue(value);
				}
			}
			while(t != null);
		}
		Entry<V> e = new Entry<V>(key, value, parent);
		if(cmp < 0)
		{
			parent.left = e;
		}
		else
		{
			parent.right = e;
		}
		fixAfterInsertion(e);
		size++;
		modCount++;
		return null;
	}

	/**
	 * Removes the mapping for this key from this TreeIntObjectMap if present.
	 *
	 * @param key key for which mapping should be removed
	 * @return the previous value associated with key, or
	 *         null if there was no mapping for key.
	 *         (A null return can also indicate that the map
	 *         previously associated null with key.)
	 * @throws ClassCastException   if the specified key cannot be compared
	 *                              with the keys currently in the map
	 * @throws NullPointerException if the specified key is null
	 *                              and this map uses natural ordering, or its comparator
	 *                              does not permit null keys
	 */
	public V remove(int key)
	{
		Entry<V> p = getEntry(key);
		if(p == null)
		{
			return null;
		}

		V oldValue = p.value;
		deleteEntry(p);
		return oldValue;
	}

	/**
	 * Removes all of the mappings from this map.
	 * The map will be empty after this call returns.
	 */
	public void clear()
	{
		modCount++;
		size = 0;
		root = null;
	}

	/**
	 * Returns a shallow copy of this TreeIntObjectMap instance. (The keys and
	 * values themselves are not cloned.)
	 *
	 * @return a shallow copy of this map
	 */
	@SuppressWarnings("unchecked")
	public Object clone()
	{
		TreeIntObjectMap<V> clone = null;
		try
		{
			clone = (TreeIntObjectMap<V>) super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			throw new InternalError();
		}

		// Put clone into "virgin" state (except for comparator)
		clone.root = null;
		clone.size = 0;
		clone.modCount = 0;
		clone.entrySet = null;
		clone.navigableKeySet = null;
		clone.descendingMap = null;

		// Initialize clone with our mappings
		try
		{
			clone.buildFromSorted(size, entrySet().iterator(), null, null);
		}
		catch(java.io.IOException cannotHappen)
		{
		}
		catch(ClassNotFoundException cannotHappen)
		{
		}

		return clone;
	}

	// NavigableMap API methods

	/**
	 * @since 1.0.0
	 */
	public IntObjectPair<V> firstEntry()
	{
		return exportEntry(getFirstEntry());
	}

	/**
	 * @since 1.0.0
	 */
	public IntObjectPair<V> lastEntry()
	{
		return exportEntry(getLastEntry());
	}

	/**
	 * @since 1.0.0
	 */
	public IntObjectPair<V> pollFirstEntry()
	{
		Entry<V> p = getFirstEntry();
		IntObjectPair<V> result = exportEntry(p);
		if(p != null)
		{
			deleteEntry(p);
		}
		return result;
	}

	/**
	 * @since 1.0.0
	 */
	public IntObjectPair<V> pollLastEntry()
	{
		Entry<V> p = getLastEntry();
		IntObjectPair<V> result = exportEntry(p);
		if(p != null)
		{
			deleteEntry(p);
		}
		return result;
	}

	/**
	 * @throws ClassCastException   {@inheritDoc}
	 * @throws NullPointerException if the specified key is null
	 *                              and this map uses natural ordering, or its comparator
	 *                              does not permit null keys
	 * @since 1.0.0
	 */
	public IntObjectPair<V> lowerEntry(int key)
	{
		return exportEntry(getLowerEntry(key));
	}

	/**
	 * @throws ClassCastException   {@inheritDoc}
	 * @throws NullPointerException if the specified key is null
	 *                              and this map uses natural ordering, or its comparator
	 *                              does not permit null keys
	 * @since 1.0.0
	 */
	public int lowerKey(int key)
	{
		return keyOrNull(getLowerEntry(key));
	}

	/**
	 * @throws ClassCastException   {@inheritDoc}
	 * @throws NullPointerException if the specified key is null
	 *                              and this map uses natural ordering, or its comparator
	 *                              does not permit null keys
	 * @since 1.0.0
	 */
	public IntObjectPair<V> floorEntry(int key)
	{
		return exportEntry(getFloorEntry(key));
	}

	/**
	 * @throws ClassCastException   {@inheritDoc}
	 * @throws NullPointerException if the specified key is null
	 *                              and this map uses natural ordering, or its comparator
	 *                              does not permit null keys
	 * @since 1.0.0
	 */
	public int floorKey(int key)
	{
		return keyOrNull(getFloorEntry(key));
	}

	/**
	 * @throws ClassCastException   {@inheritDoc}
	 * @throws NullPointerException if the specified key is null
	 *                              and this map uses natural ordering, or its comparator
	 *                              does not permit null keys
	 * @since 1.0.0
	 */
	public IntObjectPair<V> ceilingEntry(int key)
	{
		return exportEntry(getCeilingEntry(key));
	}

	/**
	 * @throws ClassCastException   {@inheritDoc}
	 * @throws NullPointerException if the specified key is null
	 *                              and this map uses natural ordering, or its comparator
	 *                              does not permit null keys
	 * @since 1.0.0
	 */
	public int ceilingKey(int key)
	{
		return keyOrNull(getCeilingEntry(key));
	}

	/**
	 * @throws ClassCastException   {@inheritDoc}
	 * @throws NullPointerException if the specified key is null
	 *                              and this map uses natural ordering, or its comparator
	 *                              does not permit null keys
	 * @since 1.0.0
	 */
	public IntObjectPair<V> higherEntry(int key)
	{
		return exportEntry(getHigherEntry(key));
	}

	/**
	 * @throws ClassCastException   {@inheritDoc}
	 * @throws NullPointerException if the specified key is null
	 *                              and this map uses natural ordering, or its comparator
	 *                              does not permit null keys
	 * @since 1.0.0
	 */
	public int higherKey(int key)
	{
		return keyOrNull(getHigherEntry(key));
	}

	// Views

	/**
	 * Fields initialized to contain an instance of the entry set view
	 * the first time this view is requested.  Views are stateless, so
	 * there's no reason to create more than one.
	 */
	private transient EntrySet entrySet = null;
	private transient KeySet navigableKeySet = null;
	private transient NavigableIntObjectMap<V> descendingMap = null;

	/**
	 * Returns a {@link IntSet} view of the keys contained in this map.
	 * The set's iterator returns the keys in ascending order.
	 * The set is backed by the map, so changes to the map are
	 * reflected in the set, and vice-versa.  If the map is modified
	 * while an iteration over the set is in progress (except through
	 * the iterator's own remove operation), the results of
	 * the iteration are undefined.  The set supports element removal,
	 * which removes the corresponding mapping from the map, via the
	 * Iterator.remove, Set.remove,
	 * removeAll, retainAll, and clear
	 * operations.  It does not support the add or addAll
	 * operations.
	 */
	public IntSet keySet()
	{
		return navigableKeySet();
	}

	/**
	 * @since 1.0.0
	 */
	public NavigableIntSet navigableKeySet()
	{
		KeySet nks = navigableKeySet;
		return (nks != null) ? nks : (navigableKeySet = new KeySet(this));
	}

	/**
	 * @since 1.0.0
	 */
	public NavigableIntSet descendingKeySet()
	{
		return descendingMap().navigableKeySet();
	}

	/**
	 * Returns a {@link Collection} view of the values contained in this map.
	 * The collection's iterator returns the values in ascending order
	 * of the corresponding keys.
	 * The collection is backed by the map, so changes to the map are
	 * reflected in the collection, and vice-versa.  If the map is
	 * modified while an iteration over the collection is in progress
	 * (except through the iterator's own remove operation),
	 * the results of the iteration are undefined.  The collection
	 * supports element removal, which removes the corresponding
	 * mapping from the map, via the Iterator.remove,
	 * Collection.remove, removeAll,
	 * retainAll and clear operations.  It does not
	 * support the add or addAll operations.
	 */
	public Collection<V> values()
	{
		Collection<V> vs = values;
		return (vs != null) ? vs : (values = new Values());
	}

	/**
	 * Returns a {@link Set} view of the mappings contained in this map.
	 * The set's iterator returns the entries in ascending key order.
	 * The set is backed by the map, so changes to the map are
	 * reflected in the set, and vice-versa.  If the map is modified
	 * while an iteration over the set is in progress (except through
	 * the iterator's own remove operation, or through the
	 * setValue operation on a map entry returned by the
	 * iterator) the results of the iteration are undefined.  The set
	 * supports element removal, which removes the corresponding
	 * mapping from the map, via the Iterator.remove,
	 * Set.remove, removeAll, retainAll and
	 * clear operations.  It does not support the
	 * add or addAll operations.
	 */
	public Set<IntObjectPair<V>> entrySet()
	{
		EntrySet es = entrySet;
		return (es != null) ? es : (entrySet = new EntrySet());
	}

	/**
	 * @since 1.0.0
	 */
	public NavigableIntObjectMap<V> descendingMap()
	{
		NavigableIntObjectMap<V> km = descendingMap;
		return (km != null) ? km : (descendingMap = new DescendingSubMap<V>(this, true, 0, true, true, 0, true));
	}

	/**
	 * @throws ClassCastException	   {@inheritDoc}
	 * @throws NullPointerException	 if fromKey or toKey is
	 *                                  null and this map uses natural ordering, or its comparator
	 *                                  does not permit null keys
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @since 1.0.0
	 */
	public NavigableIntObjectMap<V> subMap(int fromKey, boolean fromInclusive, int toKey, boolean toInclusive)
	{
		return new AscendingSubMap<V>(this, false, fromKey, fromInclusive, false, toKey, toInclusive);
	}

	/**
	 * @throws ClassCastException	   {@inheritDoc}
	 * @throws NullPointerException	 if toKey is null
	 *                                  and this map uses natural ordering, or its comparator
	 *                                  does not permit null keys
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @since 1.0.0
	 */
	public NavigableIntObjectMap<V> headMap(int toKey, boolean inclusive)
	{
		return new AscendingSubMap<V>(this, true, 0, true, false, toKey, inclusive);
	}

	/**
	 * @throws ClassCastException	   {@inheritDoc}
	 * @throws NullPointerException	 if fromKey is null
	 *                                  and this map uses natural ordering, or its comparator
	 *                                  does not permit null keys
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @since 1.0.0
	 */
	public NavigableIntObjectMap<V> tailMap(int fromKey, boolean inclusive)
	{
		return new AscendingSubMap<V>(this, false, fromKey, inclusive, true, 0, true);
	}

	/**
	 * @throws ClassCastException	   {@inheritDoc}
	 * @throws NullPointerException	 if fromKey or toKey is
	 *                                  null and this map uses natural ordering, or its comparator
	 *                                  does not permit null keys
	 * @throws IllegalArgumentException {@inheritDoc}
	 */
	public SortedIntObjectMap<V> subMap(int fromKey, int toKey)
	{
		return subMap(fromKey, true, toKey, false);
	}

	/**
	 * @throws ClassCastException	   {@inheritDoc}
	 * @throws NullPointerException	 if toKey is null
	 *                                  and this map uses natural ordering, or its comparator
	 *                                  does not permit null keys
	 * @throws IllegalArgumentException {@inheritDoc}
	 */
	public SortedIntObjectMap<V> headMap(int toKey)
	{
		return headMap(toKey, false);
	}

	/**
	 * @throws ClassCastException	   {@inheritDoc}
	 * @throws NullPointerException	 if fromKey is null
	 *                                  and this map uses natural ordering, or its comparator
	 *                                  does not permit null keys
	 * @throws IllegalArgumentException {@inheritDoc}
	 */
	public SortedIntObjectMap<V> tailMap(int fromKey)
	{
		return tailMap(fromKey, true);
	}

	// View class support

	class Values extends AbstractCollection<V>
	{
		public Iterator<V> iterator()
		{
			return new ValueIterator(getFirstEntry());
		}

		public int size()
		{
			return TreeIntObjectMap.this.size();
		}

		public boolean contains(Object o)
		{
			return TreeIntObjectMap.this.containsValue(o);
		}

		public boolean remove(Object o)
		{
			for(Entry<V> e = getFirstEntry(); e != null; e = successor(e))
			{
				if(valEquals(e.getValue(), o))
				{
					deleteEntry(e);
					return true;
				}
			}
			return false;
		}

		public void clear()
		{
			TreeIntObjectMap.this.clear();
		}
	}

	class EntrySet extends AbstractSet<IntObjectPair<V>>
	{
		public Iterator<IntObjectPair<V>> iterator()
		{
			return new EntryIterator(getFirstEntry());
		}

		public boolean contains(Object o)
		{
			if(!(o instanceof IntObjectPair))
			{
				return false;
			}
			@SuppressWarnings("unchecked")
			IntObjectPair<V> entry = (IntObjectPair<V>) o;
			V value = entry.getValue();
			Entry<V> p = getEntry(entry.getKey());
			return p != null && valEquals(p.getValue(), value);
		}

		public boolean remove(Object o)
		{
			if(!(o instanceof IntObjectPair))
			{
				return false;
			}
			@SuppressWarnings("unchecked")
			IntObjectPair<V> entry = (IntObjectPair<V>) o;
			V value = entry.getValue();
			Entry<V> p = getEntry(entry.getKey());
			if(p != null && valEquals(p.getValue(), value))
			{
				deleteEntry(p);
				return true;
			}
			return false;
		}

		public int size()
		{
			return TreeIntObjectMap.this.size();
		}

		public void clear()
		{
			TreeIntObjectMap.this.clear();
		}
	}

	/*
		 * Unlike Values and EntrySet, the KeySet class is static,
		 * delegating to a NavigableMap to allow use by SubMaps, which
		 * outweighs the ugliness of needing type-tests for the following
		 * Iterator methods that are defined appropriately in main versus
		 * submap classes.
		 */

	IntIterator keyIterator()
	{
		return new KeyIterator(getFirstEntry());
	}

	IntIterator descendingKeyIterator()
	{
		return new DescendingKeyIterator(getLastEntry());
	}

	static final class KeySet extends AbstractIntSet implements NavigableIntSet
	{
		private final NavigableIntObjectMap<?> m;

		KeySet(NavigableIntObjectMap<?> map)
		{
			m = map;
		}

		public IntIterator iterator()
		{
			if(m instanceof TreeIntObjectMap)
			{
				return ((TreeIntObjectMap<?>) m).keyIterator();
			}
			else
			{
				return (((TreeIntObjectMap.NavigableSubMap) m).keyIterator());
			}
		}

		public IntIterator descendingIterator()
		{
			if(m instanceof TreeIntObjectMap)
			{
				return ((TreeIntObjectMap<?>) m).descendingKeyIterator();
			}
			else
			{
				return (((TreeIntObjectMap.NavigableSubMap) m).descendingKeyIterator());
			}
		}

		public int size()
		{
			return m.size();
		}

		public boolean isEmpty()
		{
			return m.isEmpty();
		}

		public boolean contains(int o)
		{
			return m.containsKey(o);
		}

		public void clear()
		{
			m.clear();
		}

		public int lower(int e)
		{
			return m.lowerKey(e);
		}

		public int floor(int e)
		{
			return m.floorKey(e);
		}

		public int ceiling(int e)
		{
			return m.ceilingKey(e);
		}

		public int higher(int e)
		{
			return m.higherKey(e);
		}

		public int first()
		{
			return m.firstKey();
		}

		public int last()
		{
			return m.lastKey();
		}

		public IntComparator comparator()
		{
			return m.comparator();
		}

		public int pollFirst()
		{
			IntObjectPair<?> e = m.pollFirstEntry();
			return e == null ? null : e.getKey();
		}

		public int pollLast()
		{
			IntObjectPair<?> e = m.pollLastEntry();
			return e == null ? null : e.getKey();
		}

		public boolean remove(int o)
		{
			int oldSize = size();
			m.remove(o);
			return size() != oldSize;
		}

		public NavigableIntSet subSet(int fromElement, boolean fromInclusive, int toElement, boolean toInclusive)
		{
			return new TreeIntSet(m.subMap(fromElement, fromInclusive, toElement, toInclusive));
		}

		public NavigableIntSet headSet(int toElement, boolean inclusive)
		{
			return new TreeIntSet(m.headMap(toElement, inclusive));
		}

		public NavigableIntSet tailSet(int fromElement, boolean inclusive)
		{
			return new TreeIntSet(m.tailMap(fromElement, inclusive));
		}

		public SortedIntSet subSet(int fromElement, int toElement)
		{
			return subSet(fromElement, true, toElement, false);
		}

		public SortedIntSet headSet(int toElement)
		{
			return headSet(toElement, false);
		}

		public SortedIntSet tailSet(int fromElement)
		{
			return tailSet(fromElement, true);
		}

		public NavigableIntSet descendingSet()
		{
			return new TreeIntSet(m.descendingMap());
		}
	}

	/**
	 * Base class for TreeIntObjectMap Iterators
	 */
	abstract class PrivateEntryIterator
	{
		Entry<V> next;
		Entry<V> lastReturned;
		int expectedModCount;

		PrivateEntryIterator(Entry<V> first)
		{
			expectedModCount = modCount;
			lastReturned = null;
			next = first;
		}

		public final boolean hasNext()
		{
			return next != null;
		}

		final Entry<V> nextEntry()
		{
			Entry<V> e = next;
			if(e == null)
			{
				throw new NoSuchElementException();
			}
			if(modCount != expectedModCount)
			{
				throw new ConcurrentModificationException();
			}
			next = successor(e);
			lastReturned = e;
			return e;
		}

		final Entry<V> prevEntry()
		{
			Entry<V> e = next;
			if(e == null)
			{
				throw new NoSuchElementException();
			}
			if(modCount != expectedModCount)
			{
				throw new ConcurrentModificationException();
			}
			next = predecessor(e);
			lastReturned = e;
			return e;
		}

		public void remove()
		{
			if(lastReturned == null)
			{
				throw new IllegalStateException();
			}
			if(modCount != expectedModCount)
			{
				throw new ConcurrentModificationException();
			}
			// deleted entries are replaced by their successors
			if(lastReturned.left != null && lastReturned.right != null)
			{
				next = lastReturned;
			}
			deleteEntry(lastReturned);
			expectedModCount = modCount;
			lastReturned = null;
		}
	}

	final class EntryIterator extends PrivateEntryIterator implements Iterator<IntObjectPair<V>>
	{
		EntryIterator(Entry<V> first)
		{
			super(first);
		}

		@Override
		public IntObjectPair<V> next()
		{
			return nextEntry();
		}
	}

	final class ValueIterator extends PrivateEntryIterator implements Iterator<V>
	{
		ValueIterator(Entry<V> first)
		{
			super(first);
		}

		@Override
		public V next()
		{
			return nextEntry().value;
		}
	}

	final class KeyIterator extends PrivateEntryIterator implements IntIterator
	{
		KeyIterator(Entry<V> first)
		{
			super(first);
		}

		public int nextInt()
		{
			return nextEntry().key;
		}
	}

	final class DescendingKeyIterator extends PrivateEntryIterator implements IntIterator
	{
		DescendingKeyIterator(Entry<V> first)
		{
			super(first);
		}

		@Override
		public int nextInt()
		{
			return prevEntry().key;
		}
	}

	// Little utilities

	/**
	 * Compares two keys using the correct comparison method for this TreeIntObjectMap.
	 */
	final int compare(int k1, int k2)
	{
		return comparator == null ? Comparators.DEFAULT_INT_COMPARATOR.compare(k1, k2) : comparator.compare(k1, k2);
	}

	/**
	 * Test two values for equality.  Differs from o1.equals(o2) only in
	 * that it copes with null o1 properly.
	 */
	final static boolean valEquals(Object o1, Object o2)
	{
		return (o1 == null ? o2 == null : o1.equals(o2));
	}

	/**
	 * Return SimpleImmutableEntry for entry, or null if null
	 */
	static <V> IntObjectPair<V> exportEntry(Entry<V> e)
	{
		return e == null ? null : new ImmutableIntObjectPairImpl<V>(e.getKey(), e.getValue());
	}

	/**
	 * Return key for entry, or null if null
	 */
	static <V> int keyOrNull(TreeIntObjectMap.Entry<V> e)
	{
		return e == null ? null : e.key;
	}

	/**
	 * Returns the key corresponding to the specified Entry.
	 *
	 * @throws NoSuchElementException if the Entry is null
	 */
	static int key(Entry<?> e)
	{
		if(e == null)
		{
			throw new NoSuchElementException();
		}
		return e.key;
	}


	// SubMaps

	/**
	 * @serial include
	 */
	static abstract class NavigableSubMap<V> extends AbstractIntObjectMap<V> implements NavigableIntObjectMap<V>, java.io.Serializable
	{
		public static final long serialVersionUID = 5865325056403379349L;

		/**
		 * The backing map.
		 */
		final TreeIntObjectMap<V> m;

		/**
		 * Endpoints are represented as triples (fromStart, lo,
		 * loInclusive) and (toEnd, hi, hiInclusive). If fromStart is
		 * true, then the low (absolute) bound is the start of the
		 * backing map, and the other values are ignored. Otherwise,
		 * if loInclusive is true, lo is the inclusive bound, else lo
		 * is the exclusive bound. Similarly for the upper bound.
		 */
		final int lo, hi;
		final boolean fromStart, toEnd;
		final boolean loInclusive, hiInclusive;

		NavigableSubMap(TreeIntObjectMap<V> m, boolean fromStart, int lo, boolean loInclusive, boolean toEnd, int hi, boolean hiInclusive)
		{
			if(!fromStart && !toEnd)
			{
				if(m.compare(lo, hi) > 0)
				{
					throw new IllegalArgumentException("fromKey > toKey");
				}
			}
			else
			{
				if(!fromStart) // type check
				{
					m.compare(lo, lo);
				}
				if(!toEnd)
				{
					m.compare(hi, hi);
				}
			}

			this.m = m;
			this.fromStart = fromStart;
			this.lo = lo;
			this.loInclusive = loInclusive;
			this.toEnd = toEnd;
			this.hi = hi;
			this.hiInclusive = hiInclusive;
		}

		// internal utilities

		final boolean tooLow(int key)
		{
			if(!fromStart)
			{
				int c = m.compare(key, lo);
				if(c < 0 || (c == 0 && !loInclusive))
				{
					return true;
				}
			}
			return false;
		}

		final boolean tooHigh(int key)
		{
			if(!toEnd)
			{
				int c = m.compare(key, hi);
				if(c > 0 || (c == 0 && !hiInclusive))
				{
					return true;
				}
			}
			return false;
		}

		final boolean inRange(int key)
		{
			return !tooLow(key) && !tooHigh(key);
		}

		final boolean inClosedRange(int key)
		{
			return (fromStart || m.compare(key, lo) >= 0) && (toEnd || m.compare(hi, key) >= 0);
		}

		final boolean inRange(int key, boolean inclusive)
		{
			return inclusive ? inRange(key) : inClosedRange(key);
		}

		/*
				 * Absolute versions of relation operations.
				 * Subclasses map to these using like-named "sub"
				 * versions that invert senses for descending maps
				 */

		final TreeIntObjectMap.Entry<V> absLowest()
		{
			TreeIntObjectMap.Entry<V> e = (fromStart ? m.getFirstEntry() : (loInclusive ? m.getCeilingEntry(lo) : m.getHigherEntry(lo)));
			return (e == null || tooHigh(e.key)) ? null : e;
		}

		final TreeIntObjectMap.Entry<V> absHighest()
		{
			TreeIntObjectMap.Entry<V> e = (toEnd ? m.getLastEntry() : (hiInclusive ? m.getFloorEntry(hi) : m.getLowerEntry(hi)));
			return (e == null || tooLow(e.key)) ? null : e;
		}

		final TreeIntObjectMap.Entry<V> absCeiling(int key)
		{
			if(tooLow(key))
			{
				return absLowest();
			}
			TreeIntObjectMap.Entry<V> e = m.getCeilingEntry(key);
			return (e == null || tooHigh(e.key)) ? null : e;
		}

		final TreeIntObjectMap.Entry<V> absHigher(int key)
		{
			if(tooLow(key))
			{
				return absLowest();
			}
			TreeIntObjectMap.Entry<V> e = m.getHigherEntry(key);
			return (e == null || tooHigh(e.key)) ? null : e;
		}

		final TreeIntObjectMap.Entry<V> absFloor(int key)
		{
			if(tooHigh(key))
			{
				return absHighest();
			}
			TreeIntObjectMap.Entry<V> e = m.getFloorEntry(key);
			return (e == null || tooLow(e.key)) ? null : e;
		}

		final TreeIntObjectMap.Entry<V> absLower(int key)
		{
			if(tooHigh(key))
			{
				return absHighest();
			}
			TreeIntObjectMap.Entry<V> e = m.getLowerEntry(key);
			return (e == null || tooLow(e.key)) ? null : e;
		}

		/**
		 * Returns the absolute high fence for ascending traversal
		 */
		final TreeIntObjectMap.Entry<V> absHighFence()
		{
			return (toEnd ? null : (hiInclusive ? m.getHigherEntry(hi) : m.getCeilingEntry(hi)));
		}

		/**
		 * Return the absolute low fence for descending traversal
		 */
		final TreeIntObjectMap.Entry<V> absLowFence()
		{
			return (fromStart ? null : (loInclusive ? m.getLowerEntry(lo) : m.getFloorEntry(lo)));
		}

		// Abstract methods defined in ascending vs descending classes
		// These relay to the appropriate absolute versions

		abstract TreeIntObjectMap.Entry<V> subLowest();

		abstract TreeIntObjectMap.Entry<V> subHighest();

		abstract TreeIntObjectMap.Entry<V> subCeiling(int key);

		abstract TreeIntObjectMap.Entry<V> subHigher(int key);

		abstract TreeIntObjectMap.Entry<V> subFloor(int key);

		abstract TreeIntObjectMap.Entry<V> subLower(int key);

		/**
		 * Returns ascending iterator from the perspective of this submap
		 */
		abstract IntIterator keyIterator();

		/**
		 * Returns descending iterator from the perspective of this submap
		 */
		abstract IntIterator descendingKeyIterator();

		// public methods

		public boolean isEmpty()
		{
			return (fromStart && toEnd) ? m.isEmpty() : entrySet().isEmpty();
		}

		public int size()
		{
			return (fromStart && toEnd) ? m.size() : entrySet().size();
		}

		public final boolean containsKey(int key)
		{
			return inRange(key) && m.containsKey(key);
		}

		public final V put(int key, V value)
		{
			if(!inRange(key))
			{
				throw new IllegalArgumentException("key out of range");
			}
			return m.put(key, value);
		}

		public final V get(int key)
		{
			return !inRange(key) ? null : m.get(key);
		}

		public final V remove(int key)
		{
			return !inRange(key) ? null : m.remove(key);
		}

		public final IntObjectPair<V> ceilingEntry(int key)
		{
			return exportEntry(subCeiling(key));
		}

		public final int ceilingKey(int key)
		{
			return keyOrNull(subCeiling(key));
		}

		public final IntObjectPair<V> higherEntry(int key)
		{
			return exportEntry(subHigher(key));
		}

		public final int higherKey(int key)
		{
			return keyOrNull(subHigher(key));
		}

		public final IntObjectPair<V> floorEntry(int key)
		{
			return exportEntry(subFloor(key));
		}

		public final int floorKey(int key)
		{
			return keyOrNull(subFloor(key));
		}

		public final IntObjectPair<V> lowerEntry(int key)
		{
			return exportEntry(subLower(key));
		}

		public final int lowerKey(int key)
		{
			return keyOrNull(subLower(key));
		}

		public final int firstKey()
		{
			return key(subLowest());
		}

		public final int lastKey()
		{
			return key(subHighest());
		}

		public final IntObjectPair<V> firstEntry()
		{
			return exportEntry(subLowest());
		}

		public final IntObjectPair<V> lastEntry()
		{
			return exportEntry(subHighest());
		}

		public final IntObjectPair<V> pollFirstEntry()
		{
			TreeIntObjectMap.Entry<V> e = subLowest();
			IntObjectPair<V> result = exportEntry(e);
			if(e != null)
			{
				m.deleteEntry(e);
			}
			return result;
		}

		public final IntObjectPair<V> pollLastEntry()
		{
			TreeIntObjectMap.Entry<V> e = subHighest();
			IntObjectPair<V> result = exportEntry(e);
			if(e != null)
			{
				m.deleteEntry(e);
			}
			return result;
		}

		// Views
		transient NavigableIntObjectMap<V> descendingMapView = null;
		transient EntrySetView entrySetView = null;
		transient KeySet navigableKeySetView = null;

		public final NavigableIntSet navigableKeySet()
		{
			KeySet nksv = navigableKeySetView;
			return (nksv != null) ? nksv : (navigableKeySetView = new TreeIntObjectMap.KeySet(this));
		}

		public final IntSet keySet()
		{
			return navigableKeySet();
		}

		public NavigableIntSet descendingKeySet()
		{
			return descendingMap().navigableKeySet();
		}

		public final SortedIntObjectMap<V> subMap(int fromKey, int toKey)
		{
			return subMap(fromKey, true, toKey, false);
		}

		public final SortedIntObjectMap<V> headMap(int toKey)
		{
			return headMap(toKey, false);
		}

		public final SortedIntObjectMap<V> tailMap(int fromKey)
		{
			return tailMap(fromKey, true);
		}

		// View classes

		abstract class EntrySetView extends AbstractSet<IntObjectPair<V>>
		{
			private transient int size = -1, sizeModCount;

			public int size()
			{
				if(fromStart && toEnd)
				{
					return m.size();
				}
				if(size == -1 || sizeModCount != m.modCount)
				{
					sizeModCount = m.modCount;
					size = 0;
					@SuppressWarnings("rawtypes")
					Iterator i = iterator();
					while(i.hasNext())
					{
						size++;
						i.next();
					}
				}
				return size;
			}

			public boolean isEmpty()
			{
				TreeIntObjectMap.Entry<V> n = absLowest();
				return n == null || tooHigh(n.key);
			}

			@SuppressWarnings("unchecked")
			public boolean contains(Object o)
			{
				if(!(o instanceof IntObjectPair))
				{
					return false;
				}
				IntObjectPair<V> entry = (IntObjectPair<V>) o;
				int key = entry.getKey();
				if(!inRange(key))
				{
					return false;
				}
				TreeIntObjectMap.Entry node = m.getEntry(key);
				return node != null && valEquals(node.getValue(), entry.getValue());
			}

			@SuppressWarnings("unchecked")
			public boolean remove(Object o)
			{
				if(!(o instanceof Map.Entry))
				{
					return false;
				}
				IntObjectPair<V> entry = (IntObjectPair<V>) o;
				int key = entry.getKey();
				if(!inRange(key))
				{
					return false;
				}
				TreeIntObjectMap.Entry<V> node = m.getEntry(key);
				if(node != null && valEquals(node.getValue(), entry.getValue()))
				{
					m.deleteEntry(node);
					return true;
				}
				return false;
			}
		}

		/**
		 * Iterators for SubMaps
		 */
		abstract class SubMapIterator
		{
			TreeIntObjectMap.Entry<V> lastReturned;
			TreeIntObjectMap.Entry<V> next;
			final int fenceKey;
			int expectedModCount;

			SubMapIterator(TreeIntObjectMap.Entry<V> first, TreeIntObjectMap.Entry<V> fence)
			{
				expectedModCount = m.modCount;
				lastReturned = null;
				next = first;
				fenceKey = fence == null ? 0 : fence.key;
			}

			public final boolean hasNext()
			{
				return next != null && next.key != fenceKey;
			}

			final TreeIntObjectMap.Entry<V> nextEntry()
			{
				TreeIntObjectMap.Entry<V> e = next;
				if(e == null || e.key == fenceKey)
				{
					throw new NoSuchElementException();
				}
				if(m.modCount != expectedModCount)
				{
					throw new ConcurrentModificationException();
				}
				next = successor(e);
				lastReturned = e;
				return e;
			}

			final TreeIntObjectMap.Entry<V> prevEntry()
			{
				TreeIntObjectMap.Entry<V> e = next;
				if(e == null || e.key == fenceKey)
				{
					throw new NoSuchElementException();
				}
				if(m.modCount != expectedModCount)
				{
					throw new ConcurrentModificationException();
				}
				next = predecessor(e);
				lastReturned = e;
				return e;
			}

			final void removeAscending()
			{
				if(lastReturned == null)
				{
					throw new IllegalStateException();
				}
				if(m.modCount != expectedModCount)
				{
					throw new ConcurrentModificationException();
				}
				// deleted entries are replaced by their successors
				if(lastReturned.left != null && lastReturned.right != null)
				{
					next = lastReturned;
				}
				m.deleteEntry(lastReturned);
				lastReturned = null;
				expectedModCount = m.modCount;
			}

			final void removeDescending()
			{
				if(lastReturned == null)
				{
					throw new IllegalStateException();
				}
				if(m.modCount != expectedModCount)
				{
					throw new ConcurrentModificationException();
				}
				m.deleteEntry(lastReturned);
				lastReturned = null;
				expectedModCount = m.modCount;
			}

		}

		final class SubMapEntryIterator extends SubMapIterator implements Iterator<IntObjectPair<V>>
		{
			SubMapEntryIterator(TreeIntObjectMap.Entry<V> first, TreeIntObjectMap.Entry<V> fence)
			{
				super(first, fence);
			}

			public IntObjectPair<V> next()
			{
				return nextEntry();
			}

			public void remove()
			{
				removeAscending();
			}
		}

		final class SubMapKeyIterator extends SubMapIterator implements IntIterator
		{
			SubMapKeyIterator(TreeIntObjectMap.Entry<V> first, TreeIntObjectMap.Entry<V> fence)
			{
				super(first, fence);
			}

			public int nextInt()
			{
				return nextEntry().key;
			}

			public void remove()
			{
				removeAscending();
			}
		}

		final class DescendingSubMapEntryIterator extends SubMapIterator implements Iterator<IntObjectPair<V>>
		{
			DescendingSubMapEntryIterator(TreeIntObjectMap.Entry<V> last, TreeIntObjectMap.Entry<V> fence)
			{
				super(last, fence);
			}

			public IntObjectPair<V> next()
			{
				return prevEntry();
			}

			public void remove()
			{
				removeDescending();
			}
		}

		final class DescendingSubMapKeyIterator extends SubMapIterator implements IntIterator
		{
			DescendingSubMapKeyIterator(TreeIntObjectMap.Entry<V> last, TreeIntObjectMap.Entry<V> fence)
			{
				super(last, fence);
			}

			public int nextInt()
			{
				return prevEntry().key;
			}

			public void remove()
			{
				removeDescending();
			}
		}
	}

	/**
	 * @serial include
	 */
	static final class AscendingSubMap<V> extends NavigableSubMap<V>
	{
		private static final long serialVersionUID = 912986545866124060L;

		AscendingSubMap(TreeIntObjectMap<V> m, boolean fromStart, int lo, boolean loInclusive, boolean toEnd, int hi, boolean hiInclusive)
		{
			super(m, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
		}

		public IntComparator comparator()
		{
			return m.comparator();
		}

		public NavigableIntObjectMap<V> subMap(int fromKey, boolean fromInclusive, int toKey, boolean toInclusive)
		{
			if(!inRange(fromKey, fromInclusive))
			{
				throw new IllegalArgumentException("fromKey out of range");
			}
			if(!inRange(toKey, toInclusive))
			{
				throw new IllegalArgumentException("toKey out of range");
			}
			return new AscendingSubMap<V>(m, false, fromKey, fromInclusive, false, toKey, toInclusive);
		}

		public NavigableIntObjectMap<V> headMap(int toKey, boolean inclusive)
		{
			if(!inRange(toKey, inclusive))
			{
				throw new IllegalArgumentException("toKey out of range");
			}
			return new AscendingSubMap<V>(m, fromStart, lo, loInclusive, false, toKey, inclusive);
		}

		public NavigableIntObjectMap<V> tailMap(int fromKey, boolean inclusive)
		{
			if(!inRange(fromKey, inclusive))
			{
				throw new IllegalArgumentException("fromKey out of range");
			}
			return new AscendingSubMap<V>(m, false, fromKey, inclusive, toEnd, hi, hiInclusive);
		}

		public NavigableIntObjectMap<V> descendingMap()
		{
			NavigableIntObjectMap<V> mv = descendingMapView;
			return (mv != null) ? mv : (descendingMapView = new DescendingSubMap<V>(m, fromStart, lo, loInclusive, toEnd, hi, hiInclusive));
		}

		IntIterator keyIterator()
		{
			return new SubMapKeyIterator(absLowest(), absHighFence());
		}

		IntIterator descendingKeyIterator()
		{
			return new DescendingSubMapKeyIterator(absHighest(), absLowFence());
		}

		final class AscendingEntrySetView extends EntrySetView
		{
			public Iterator<IntObjectPair<V>> iterator()
			{
				return new SubMapEntryIterator(absLowest(), absHighFence());
			}
		}

		public Set<IntObjectPair<V>> entrySet()
		{
			EntrySetView es = entrySetView;
			return (es != null) ? es : new AscendingEntrySetView();
		}

		TreeIntObjectMap.Entry<V> subLowest()
		{
			return absLowest();
		}

		TreeIntObjectMap.Entry<V> subHighest()
		{
			return absHighest();
		}

		TreeIntObjectMap.Entry<V> subCeiling(int key)
		{
			return absCeiling(key);
		}

		TreeIntObjectMap.Entry<V> subHigher(int key)
		{
			return absHigher(key);
		}

		TreeIntObjectMap.Entry<V> subFloor(int key)
		{
			return absFloor(key);
		}

		TreeIntObjectMap.Entry<V> subLower(int key)
		{
			return absLower(key);
		}
	}

	/**
	 * @serial include
	 */
	static final class DescendingSubMap<V> extends NavigableSubMap<V>
	{
		private static final long serialVersionUID = 912986545866120460L;

		DescendingSubMap(TreeIntObjectMap<V> m, boolean fromStart, int lo, boolean loInclusive, boolean toEnd, int hi, boolean hiInclusive)
		{
			super(m, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
		}

		private final IntComparator reverseComparator = Comparators.reverseOrder(m.comparator);

		public IntComparator comparator()
		{
			return reverseComparator;
		}

		public NavigableIntObjectMap<V> subMap(int fromKey, boolean fromInclusive, int toKey, boolean toInclusive)
		{
			if(!inRange(fromKey, fromInclusive))
			{
				throw new IllegalArgumentException("fromKey out of range");
			}
			if(!inRange(toKey, toInclusive))
			{
				throw new IllegalArgumentException("toKey out of range");
			}
			return new DescendingSubMap<V>(m, false, toKey, toInclusive, false, fromKey, fromInclusive);
		}

		public NavigableIntObjectMap<V> headMap(int toKey, boolean inclusive)
		{
			if(!inRange(toKey, inclusive))
			{
				throw new IllegalArgumentException("toKey out of range");
			}
			return new DescendingSubMap<V>(m, false, toKey, inclusive, toEnd, hi, hiInclusive);
		}

		public NavigableIntObjectMap<V> tailMap(int fromKey, boolean inclusive)
		{
			if(!inRange(fromKey, inclusive))
			{
				throw new IllegalArgumentException("fromKey out of range");
			}
			return new DescendingSubMap<V>(m, fromStart, lo, loInclusive, false, fromKey, inclusive);
		}

		public NavigableIntObjectMap<V> descendingMap()
		{
			NavigableIntObjectMap<V> mv = descendingMapView;
			return (mv != null) ? mv : (descendingMapView = new AscendingSubMap<V>(m, fromStart, lo, loInclusive, toEnd, hi, hiInclusive));
		}

		IntIterator keyIterator()
		{
			return new DescendingSubMapKeyIterator(absHighest(), absLowFence());
		}

		IntIterator descendingKeyIterator()
		{
			return new SubMapKeyIterator(absLowest(), absHighFence());
		}

		final class DescendingEntrySetView extends EntrySetView
		{
			public Iterator<IntObjectPair<V>> iterator()
			{
				return new DescendingSubMapEntryIterator(absHighest(), absLowFence());
			}
		}

		public Set<IntObjectPair<V>> entrySet()
		{
			EntrySetView es = entrySetView;
			return (es != null) ? es : new DescendingEntrySetView();
		}

		TreeIntObjectMap.Entry<V> subLowest()
		{
			return absHighest();
		}

		TreeIntObjectMap.Entry<V> subHighest()
		{
			return absLowest();
		}

		TreeIntObjectMap.Entry<V> subCeiling(int key)
		{
			return absFloor(key);
		}

		TreeIntObjectMap.Entry<V> subHigher(int key)
		{
			return absLower(key);
		}

		TreeIntObjectMap.Entry<V> subFloor(int key)
		{
			return absCeiling(key);
		}

		TreeIntObjectMap.Entry<V> subLower(int key)
		{
			return absHigher(key);
		}
	}

	/**
	 * This class exists solely for the sake of serialization
	 * compatibility with previous releases of TreeIntObjectMap that did not
	 * support NavigableMap.  It translates an old-version SubMap into
	 * a new-version AscendingSubMap. This class is never otherwise
	 * used.
	 *
	 * @serial include
	 */
	private class SubMap extends AbstractIntObjectMap<V> implements SortedIntObjectMap<V>, java.io.Serializable
	{
		private static final long serialVersionUID = -6520786458950516097L;
		private boolean fromStart = false, toEnd = false;
		private int fromKey, toKey;

		private Object readResolve()
		{
			return new AscendingSubMap<V>(TreeIntObjectMap.this, fromStart, fromKey, true, toEnd, toKey, false);
		}

		public Set<IntObjectPair<V>> entrySet()
		{
			throw new InternalError();
		}

		public int lastKey()
		{
			throw new InternalError();
		}

		public int firstKey()
		{
			throw new InternalError();
		}

		public SortedIntObjectMap<V> subMap(int fromKey, int toKey)
		{
			throw new InternalError();
		}

		public SortedIntObjectMap<V> headMap(int toKey)
		{
			throw new InternalError();
		}

		public SortedIntObjectMap<V> tailMap(int fromKey)
		{
			throw new InternalError();
		}

		public IntComparator comparator()
		{
			throw new InternalError();
		}
	}


	// Red-black mechanics

	private static final boolean RED = false;
	private static final boolean BLACK = true;

	/**
	 * Node in the Tree.  Doubles as a means to pass key-value pairs back to
	 * user (see Map.Entry).
	 */

	static final class Entry<V> implements IntObjectPair<V>
	{
		int key;
		V value;
		Entry<V> left = null;
		Entry<V> right = null;
		Entry<V> parent;
		boolean color = BLACK;

		/**
		 * Make a new cell with given key, value, and parent, and with
		 * null child links, and BLACK color.
		 */
		Entry(int key, V value, Entry<V> parent)
		{
			this.key = key;
			this.value = value;
			this.parent = parent;
		}

		/**
		 * Returns the key.
		 *
		 * @return the key
		 */
		public int getKey()
		{
			return key;
		}

		/**
		 * Returns the value associated with the key.
		 *
		 * @return the value associated with the key
		 */
		public V getValue()
		{
			return value;
		}

		/**
		 * Replaces the value currently associated with the key with the given
		 * value.
		 *
		 * @return the value associated with the key before this method was
		 *         called
		 */
		public V setValue(V value)
		{
			V oldValue = this.value;
			this.value = value;
			return oldValue;
		}

		public boolean equals(Object o)
		{
			if(!(o instanceof IntObjectPair))
			{
				return false;
			}
			IntObjectPair<?> e = (IntObjectPair<?>) o;

			return valEquals(key, e.getKey()) && valEquals(value, e.getValue());
		}

		public int hashCode()
		{
			int valueHash = (value == null ? 0 : value.hashCode());
			return key ^ valueHash;
		}

		public String toString()
		{
			return key + "=" + value;
		}
	}

	/**
	 * Returns the first Entry in the TreeIntObjectMap (according to the TreeIntObjectMap's
	 * key-sort function).  Returns null if the TreeIntObjectMap is empty.
	 */
	final Entry<V> getFirstEntry()
	{
		Entry<V> p = root;
		if(p != null)
		{
			while(p.left != null)
			{
				p = p.left;
			}
		}
		return p;
	}

	/**
	 * Returns the last Entry in the TreeIntObjectMap (according to the TreeIntObjectMap's
	 * key-sort function).  Returns null if the TreeIntObjectMap is empty.
	 */
	final Entry<V> getLastEntry()
	{
		Entry<V> p = root;
		if(p != null)
		{
			while(p.right != null)
			{
				p = p.right;
			}
		}
		return p;
	}

	/**
	 * Returns the successor of the specified Entry, or null if no such.
	 */
	static <V> TreeIntObjectMap.Entry<V> successor(Entry<V> t)
	{
		if(t == null)
		{
			return null;
		}
		else if(t.right != null)
		{
			Entry<V> p = t.right;
			while(p.left != null)
			{
				p = p.left;
			}
			return p;
		}
		else
		{
			Entry<V> p = t.parent;
			Entry<V> ch = t;
			while(p != null && ch == p.right)
			{
				ch = p;
				p = p.parent;
			}
			return p;
		}
	}

	/**
	 * Returns the predecessor of the specified Entry, or null if no such.
	 */
	static <V> Entry<V> predecessor(Entry<V> t)
	{
		if(t == null)
		{
			return null;
		}
		else if(t.left != null)
		{
			Entry<V> p = t.left;
			while(p.right != null)
			{
				p = p.right;
			}
			return p;
		}
		else
		{
			Entry<V> p = t.parent;
			Entry<V> ch = t;
			while(p != null && ch == p.left)
			{
				ch = p;
				p = p.parent;
			}
			return p;
		}
	}

	/**
	 * Balancing operations.
	 * </p>
	 * Implementations of rebalancings during insertion and deletion are
	 * slightly different than the CLR version.  Rather than using dummy
	 * nilnodes, we use a set of accessors that deal properly with null.  They
	 * are used to avoid messiness surrounding nullness checks in the main
	 * algorithms.
	 */

	private static <V> boolean colorOf(Entry<V> p)
	{
		return (p == null ? BLACK : p.color);
	}

	private static <V> Entry<V> parentOf(Entry<V> p)
	{
		return (p == null ? null : p.parent);
	}

	private static <V> void setColor(Entry<V> p, boolean c)
	{
		if(p != null)
		{
			p.color = c;
		}
	}

	private static <V> Entry<V> leftOf(Entry<V> p)
	{
		return (p == null) ? null : p.left;
	}

	private static <V> Entry<V> rightOf(Entry<V> p)
	{
		return (p == null) ? null : p.right;
	}

	/**
	 * From CLR
	 */
	private void rotateLeft(Entry<V> p)
	{
		if(p != null)
		{
			Entry<V> r = p.right;
			p.right = r.left;
			if(r.left != null)
			{
				r.left.parent = p;
			}
			r.parent = p.parent;
			if(p.parent == null)
			{
				root = r;
			}
			else if(p.parent.left == p)
			{
				p.parent.left = r;
			}
			else
			{
				p.parent.right = r;
			}
			r.left = p;
			p.parent = r;
		}
	}

	/**
	 * From CLR
	 */
	private void rotateRight(Entry<V> p)
	{
		if(p != null)
		{
			Entry<V> l = p.left;
			p.left = l.right;
			if(l.right != null)
			{
				l.right.parent = p;
			}
			l.parent = p.parent;
			if(p.parent == null)
			{
				root = l;
			}
			else if(p.parent.right == p)
			{
				p.parent.right = l;
			}
			else
			{
				p.parent.left = l;
			}
			l.right = p;
			p.parent = l;
		}
	}

	/**
	 * From CLR
	 */
	private void fixAfterInsertion(Entry<V> x)
	{
		x.color = RED;

		while(x != null && x != root && x.parent.color == RED)
		{
			if(parentOf(x) == leftOf(parentOf(parentOf(x))))
			{
				Entry<V> y = rightOf(parentOf(parentOf(x)));
				if(colorOf(y) == RED)
				{
					setColor(parentOf(x), BLACK);
					setColor(y, BLACK);
					setColor(parentOf(parentOf(x)), RED);
					x = parentOf(parentOf(x));
				}
				else
				{
					if(x == rightOf(parentOf(x)))
					{
						x = parentOf(x);
						rotateLeft(x);
					}
					setColor(parentOf(x), BLACK);
					setColor(parentOf(parentOf(x)), RED);
					rotateRight(parentOf(parentOf(x)));
				}
			}
			else
			{
				Entry<V> y = leftOf(parentOf(parentOf(x)));
				if(colorOf(y) == RED)
				{
					setColor(parentOf(x), BLACK);
					setColor(y, BLACK);
					setColor(parentOf(parentOf(x)), RED);
					x = parentOf(parentOf(x));
				}
				else
				{
					if(x == leftOf(parentOf(x)))
					{
						x = parentOf(x);
						rotateRight(x);
					}
					setColor(parentOf(x), BLACK);
					setColor(parentOf(parentOf(x)), RED);
					rotateLeft(parentOf(parentOf(x)));
				}
			}
		}
		root.color = BLACK;
	}

	/**
	 * Delete node p, and then rebalance the tree.
	 */
	private void deleteEntry(Entry<V> p)
	{
		modCount++;
		size--;

		// If strictly internal, copy successor's element to p and then make p
		// point to successor.
		if(p.left != null && p.right != null)
		{
			Entry<V> s = successor(p);
			p.key = s.key;
			p.value = s.value;
			p = s;
		} // p has 2 children

		// Start fixup at replacement node, if it exists.
		Entry<V> replacement = (p.left != null ? p.left : p.right);

		if(replacement != null)
		{
			// Link replacement to parent
			replacement.parent = p.parent;
			if(p.parent == null)
			{
				root = replacement;
			}
			else if(p == p.parent.left)
			{
				p.parent.left = replacement;
			}
			else
			{
				p.parent.right = replacement;
			}

			// Null out links so they are OK to use by fixAfterDeletion.
			p.left = p.right = p.parent = null;

			// Fix replacement
			if(p.color == BLACK)
			{
				fixAfterDeletion(replacement);
			}
		}
		else if(p.parent == null)
		{ // return if we are the only node.
			root = null;
		}
		else
		{ //  No children. Use self as phantom replacement and unlink.
			if(p.color == BLACK)
			{
				fixAfterDeletion(p);
			}

			if(p.parent != null)
			{
				if(p == p.parent.left)
				{
					p.parent.left = null;
				}
				else if(p == p.parent.right)
				{
					p.parent.right = null;
				}
				p.parent = null;
			}
		}
	}

	/**
	 * From CLR
	 */
	private void fixAfterDeletion(Entry<V> x)
	{
		while(x != root && colorOf(x) == BLACK)
		{
			if(x == leftOf(parentOf(x)))
			{
				Entry<V> sib = rightOf(parentOf(x));

				if(colorOf(sib) == RED)
				{
					setColor(sib, BLACK);
					setColor(parentOf(x), RED);
					rotateLeft(parentOf(x));
					sib = rightOf(parentOf(x));
				}

				if(colorOf(leftOf(sib)) == BLACK && colorOf(rightOf(sib)) == BLACK)
				{
					setColor(sib, RED);
					x = parentOf(x);
				}
				else
				{
					if(colorOf(rightOf(sib)) == BLACK)
					{
						setColor(leftOf(sib), BLACK);
						setColor(sib, RED);
						rotateRight(sib);
						sib = rightOf(parentOf(x));
					}
					setColor(sib, colorOf(parentOf(x)));
					setColor(parentOf(x), BLACK);
					setColor(rightOf(sib), BLACK);
					rotateLeft(parentOf(x));
					x = root;
				}
			}
			else
			{ // symmetric
				Entry<V> sib = leftOf(parentOf(x));

				if(colorOf(sib) == RED)
				{
					setColor(sib, BLACK);
					setColor(parentOf(x), RED);
					rotateRight(parentOf(x));
					sib = leftOf(parentOf(x));
				}

				if(colorOf(rightOf(sib)) == BLACK && colorOf(leftOf(sib)) == BLACK)
				{
					setColor(sib, RED);
					x = parentOf(x);
				}
				else
				{
					if(colorOf(leftOf(sib)) == BLACK)
					{
						setColor(rightOf(sib), BLACK);
						setColor(sib, RED);
						rotateLeft(sib);
						sib = leftOf(parentOf(x));
					}
					setColor(sib, colorOf(parentOf(x)));
					setColor(parentOf(x), BLACK);
					setColor(leftOf(sib), BLACK);
					rotateRight(parentOf(x));
					x = root;
				}
			}
		}

		setColor(x, BLACK);
	}

	private static final long serialVersionUID = 919286545866124006L;

	/**
	 * Save the state of the TreeIntObjectMap instance to a stream (i.e.,
	 * serialize it).
	 *
	 * @serialData The <i>size</i> of the TreeIntObjectMap (the number of key-value
	 * mappings) is emitted (int), followed by the key (Object)
	 * and value (Object) for each key-value mapping represented
	 * by the TreeIntObjectMap. The key-value mappings are emitted in
	 * key-order (as determined by the TreeIntObjectMap's Comparator,
	 * or by the keys' natural ordering if the TreeIntObjectMap has no
	 * Comparator).
	 *
	 * @param s the stream
	 * @throws java.io.IOException if the stream throws a exception
	 */
	private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException
	{
		// Write out the Comparator and any hidden stuff
		s.defaultWriteObject();

		// Write out size (number of Mappings)
		s.writeInt(size);

		// Write out keys and values (alternating)
		for(Iterator<IntObjectPair<V>> i = entrySet().iterator(); i.hasNext();)
		{
			IntObjectPair<V> e = i.next();
			s.writeInt(e.getKey());
			s.writeObject(e.getValue());
		}
	}

	/**
	 * Reconstitute the TreeIntObjectMap instance from a stream (i.e.,
	 * deserialize it).
	 *
	 * @param s the stream
	 * @throws java.io.IOException if the stream throws a exception
	 * @throws  ClassNotFoundException it the stream represent a unknown class
	 */
	private void readObject(final java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException
	{
		// Read in the Comparator and any hidden stuff
		s.defaultReadObject();

		// Read in size
		int size = s.readInt();

		buildFromSorted(size, null, s, null);
	}

	/**
	 * Intended to be called only from TreeSet.readObject
     *
	 * @param size the size of the set
     * @param s the stream
     * @param defaultVal the default values
     * @throws java.io.IOException if the stream throws an exception
     * @throws ClassNotFoundException if the stream represents an unknown class
	 */
	public void readTreeSet(int size, java.io.ObjectInputStream s, V defaultVal) throws java.io.IOException, ClassNotFoundException
	{
		buildFromSorted(size, null, s, defaultVal);
	}

	/**
	 * Intended to be called only from TreeIntSet.addAll
     *
     * @param set the set to be added
     * @param defaultVal the default value.
	 */
	public void addAllForTreeSet(SortedIntSet set, V defaultVal)
	{
		try
		{
			buildFromSorted(set.size(), set.iterator(), null, defaultVal);
		}
		catch(java.io.IOException cannotHappen)
		{
		}
		catch(ClassNotFoundException cannotHappen)
		{
		}
	}


	/**
	 * Linear time tree building algorithm from sorted data.  Can accept keys
	 * and/or values from iterator or stream. This leads to too many
	 * parameters, but seems better than alternatives.  The four formats
	 * that this method accepts are:
	 * </p>
	 * 1) An iterator of Map.Entries.  (it != null, defaultVal == null).
	 * 2) An iterator of keys.         (it != null, defaultVal != null).
	 * 3) A stream of alternating serialized keys and values.
	 * (it == null, defaultVal == null).
	 * 4) A stream of serialized keys. (it == null, defaultVal != null).
	 * </p>
	 * It is assumed that the comparator of the TreeIntObjectMap is already set prior
	 * to calling this method.
	 *
	 * @param size	   the number of keys (or key-value pairs) to be read from
	 *                   the iterator or stream
	 * @param it		 If non-null, new entries are created from entries
	 *                   or keys read from this iterator.
	 * @param str		If non-null, new entries are created from keys and
	 *                   possibly values read from this stream in serialized form.
	 *                   Exactly one of it and str should be non-null.
	 * @param defaultVal if non-null, this default value is used for
	 *                   each value in the map.  If null, each value is read from
	 *                   iterator or stream, as described above.
	 * @throws java.io.IOException	propagated from stream reads. This cannot
	 *                                occur if str is null.
	 * @throws ClassNotFoundException propagated from readObject.
	 *                                This cannot occur if str is null.
	 */
	private void buildFromSorted(int size, Object it, java.io.ObjectInputStream str, V defaultVal) throws java.io.IOException, ClassNotFoundException
	{
		this.size = size;
		root = buildFromSorted(0, 0, size - 1, computeRedLevel(size), it, str, defaultVal);
	}

	/**
	 * Recursive "helper method" that does the real work of the
	 * previous method.  Identically named parameters have
	 * identical definitions.  Additional parameters are documented below.
	 * It is assumed that the comparator and size fields of the TreeIntObjectMap are
	 * already set prior to calling this method.  (It ignores both fields.)
	 *
	 * @param level	the current level of tree. Initial call should be 0.
	 * @param lo	   the first element index of this subtree. Initial should be 0.
	 * @param hi	   the last element index of this subtree.  Initial should be
	 *                 size-1.
	 * @param redLevel the level at which nodes should be red.
	 *                 Must be equal to computeRedLevel for tree of this size.
	 */
	@SuppressWarnings("unchecked")
	private final Entry<V> buildFromSorted(int level, int lo, int hi, int redLevel, Object it, java.io.ObjectInputStream str, V defaultVal) throws java.io.IOException, ClassNotFoundException
	{
		/*
				 * Strategy: The root is the middlemost element. To get to it, we
				 * have to first recursively construct the entire left subtree,
				 * so as to grab all of its elements. We can then proceed with right
				 * subtree.
				 *
				 * The lo and hi arguments are the minimum and maximum
				 * indices to pull out of the iterator or stream for current subtree.
				 * They are not actually indexed, we just proceed sequentially,
				 * ensuring that items are extracted in corresponding order.
				 */

		if(hi < lo)
		{
			return null;
		}

		int mid = (lo + hi) / 2;

		Entry<V> left = null;
		if(lo < mid)
		{
			left = buildFromSorted(level + 1, lo, mid - 1, redLevel, it, str, defaultVal);
		}

		// extract key and/or value from iterator or stream
		int key;
		V value;
		if(it != null)
		{
			if(defaultVal == null)
			{
				Iterator<IntObjectPair<V>> iterator = (Iterator) it;
				IntObjectPair<V> entry = iterator.next();
				key = entry.getKey();
				value = entry.getValue();
			}
			else
			{
				IntIterator iterator = (IntIterator) it;
				key = iterator.nextInt();
				value = defaultVal;
			}
		}
		else
		{ // use stream
			key = str.readInt();
			value = (defaultVal != null ? defaultVal : (V) str.readObject());
		}

		Entry<V> middle = new Entry<V>(key, value, null);

		// color nodes in non-full bottommost level red
		if(level == redLevel)
		{
			middle.color = RED;
		}

		if(left != null)
		{
			middle.left = left;
			left.parent = middle;
		}

		if(mid < hi)
		{
			Entry<V> right = buildFromSorted(level + 1, mid + 1, hi, redLevel, it, str, defaultVal);
			middle.right = right;
			right.parent = middle;
		}

		return middle;
	}

	/**
	 * Find the level down to which to assign all nodes BLACK.  This is the
	 * last `full' level of the complete binary tree produced by
	 * buildTree. The remaining nodes are colored RED. (This makes a `nice'
	 * set of color assignments wrt future insertions.) This level number is
	 * computed by finding the number of splits needed to reach the zeroeth
	 * node.  (The answer is ~lg(N), but in any case must be computed by same
	 * quick O(lg(N)) loop.)
	 */
	private static int computeRedLevel(int sz)
	{
		int level = 0;
		for(int m = sz - 1; m >= 0; m = m / 2 - 1)
		{
			level++;
		}
		return level;
	}
}
