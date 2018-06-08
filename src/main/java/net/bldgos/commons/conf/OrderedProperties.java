package net.bldgos.commons.conf;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class OrderedProperties extends Properties implements Map<Object,Object> {

	private static final long serialVersionUID = -7441479288403015243L;

	private transient LinkedHashMap<String,String> entries=new LinkedHashMap<>();

	public OrderedProperties() {
		this(null);
	}
	public OrderedProperties(Properties defaults) {
		super(defaults);
	}

	/* override Map<Object,Object> methods */
	@Override
	public int size() {
		return entries.size();
	}
	@Override
	public boolean isEmpty() {
		return entries.isEmpty();
	}
	@Override
	public boolean containsKey(Object key) {
		return entries.containsKey(key);
	}
	@Override
	public boolean containsValue(Object value) {
		return entries.containsValue(value);
	}
	@Override
	public Object get(Object key) {
		return entries.get(key);
	}
	@Override
	public Object put(Object key, Object value) {
		return entries.put((String)key, (String)value);
	}
	@Override
	public Object remove(Object key) {
		return entries.remove(key);
	}
	@Override
	public void putAll(Map<? extends Object, ? extends Object> m) {
		for (Map.Entry<? extends Object, ? extends Object> e : m.entrySet())
			entries.put((String)e.getKey(), (String)e.getValue());
	}
	@Override
	public void clear() {
		entries.clear();
	}
	@Override
	public Set<Object> keySet() {
		@SuppressWarnings("unchecked")
		Set<Object> c=(Set<Object>)(Set<?>)entries.keySet();
		return c;
	}
	@Override
	public Collection<Object> values() {
		@SuppressWarnings("unchecked")
		Collection<Object> c=(Collection<Object>)(Collection<?>)entries.values();
		return c;
	}
	@SuppressWarnings("unchecked")
	@Override
	public Set<Map.Entry<Object, Object>> entrySet() {
		Set<Map.Entry<String, String>> s=entries.entrySet();
		Set<Map.Entry<Object, Object>> s2=new LinkedHashSet<>(s.size());
		for(Map.Entry<String, String> e:s) {
			Map.Entry<Object, Object> e2=(Map.Entry<Object, Object>)(Map.Entry<?, ?>)e;
			s2.add(e2);
		}
		return s2;
	}

	/* override Dictionary<Object,Object> methods */
	@Override
	public synchronized Enumeration<Object> keys() {
		return new Enumerator<>(KEYS);
	}
	@Override
	public synchronized Enumeration<Object> elements() {
		return new Enumerator<>(VALUES);
	}
	private static final int KEYS = 0;
	private static final int VALUES = 1;
	private static final int ENTRIES = 2;
	private class Enumerator<T> implements Enumeration<T> {
		private int type;
		private Iterator<T> it;

		@SuppressWarnings("unchecked")
		public Enumerator(int type) {
			this.type = type;
			switch(type) {
			case KEYS:
				this.it = (Iterator<T>)entries.keySet().iterator();
				break;
			case VALUES:
				this.it = (Iterator<T>)entries.values().iterator();
				break;
			case ENTRIES:
				this.it = (Iterator<T>)entries.entrySet().iterator();
				break;
			}
		}

		@Override
		public boolean hasMoreElements() {
			return it.hasNext();
		}

		@Override
		public T nextElement() {
			switch(type) {
			case KEYS:
				return it.next();
			case VALUES:
				return it.next();
			case ENTRIES:
				return it.next();
			}
			return null;
		}
	}

	/* override Properties methods */
	public static void superEnumerate(Properties obj,Hashtable<String, Object> h) {
		try {
			Method method = Properties.class.getDeclaredMethod("enumerate",Hashtable.class);
			method.setAccessible(true);
			method.invoke(obj,h);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	private synchronized void enumerate(LinkedHashMap<String, String> h) {
		if (defaults != null) {
			if(defaults.getClass().equals(this.getClass())) {
				((OrderedProperties)defaults).enumerate(h);
			}else {
				Hashtable<String, Object> superH=new Hashtable<>();
				superEnumerate(defaults,superH);
				for(Enumeration<String> e=superH.keys();e.hasMoreElements();) {
					String key=e.nextElement();
					h.put(key, (String)superH.get(key));
				}
			}
		}
		h.putAll(entries);
	}
	@Override
	public void list(PrintStream out) {
		out.println("-- listing properties --");
		LinkedHashMap<String, String> h = new LinkedHashMap<>();
		enumerate(h);
		for (Map.Entry<String,String> e: h.entrySet()) {
			String key = e.getKey();
			String val = e.getValue();
			if (val.length() > 40) {
				val = val.substring(0, 37) + "...";
			}
			out.println(key + "=" + val);
		}
	}
	@Override
	public void list(PrintWriter out) {
		out.println("-- listing properties --");
		LinkedHashMap<String, String> h = new LinkedHashMap<>();
		enumerate(h);
		for (Map.Entry<String,String> e: h.entrySet()) {
			String key = e.getKey();
			String val = e.getValue();
			if (val.length() > 40) {
				val = val.substring(0, 37) + "...";
			}
			out.println(key + "=" + val);
		}
	}

	/* override Object methods */
	@Override
	public synchronized int hashCode() {
		return entries.hashCode();
	}
	@Override
	public synchronized boolean equals(Object o) {
		if(o instanceof OrderedProperties) {
			OrderedProperties that=(OrderedProperties)o;
			return this.entries.equals(that.entries);
		}
		return false;
	}
	@SuppressWarnings("unchecked")
	@Override
	public synchronized Object clone() {
		OrderedProperties that=new OrderedProperties();
		that.entries=(LinkedHashMap<String, String>)entries.clone();
		return that;
	}
}
