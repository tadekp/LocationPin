package pl.litterae.locpin.common;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class KeyValues implements Cleanupable, Cloneable {
	public static final KeyValues EMPTY = new KeyValues();

	public static interface Job {
		void performWith(String key);
	}

	public static interface Operator {
		void operateWith(KeyValues data);
	}

	private final Map<String, Object> map = new HashMap<String, Object>();

	@Override
	protected Object clone() throws CloneNotSupportedException {
		KeyValues copy = new KeyValues();
		for (String key : map.keySet()) {
			copy.map.put(key, map.get(key));
		}
		return copy;
	}

	public void performJob(Job job) {
		if (job != null) {
			Set<String> keys = map.keySet();
			for (String key : keys) {
				job.performWith(key);
			}
		}
	}

	public KeyValues duplicate() {
		KeyValues copy = null;
		try {
			copy = (KeyValues) clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		} finally {
			return copy;
		}
	}

	public KeyValues merge(final KeyValues other, String... filterKeys) {
		if (other != null) {
//			for (String key : other.map.keySet()) {
//				map.put(key, other.getValueForKey(key));
//			}
			Filter filter = new Filter(filterKeys);
			copy(other, this, filter);
//			other.performJob(new Job() {
//				@Override
//				public void performWith(String key) {
//					map.put(key, other.getValueForKey(key));
//				}
//			});
			filter.cleanup();
		}
		return this;
	}

	private static void copy(final KeyValues src, final KeyValues dest, final Filter filter) {
		src.performJob(new Job() {
			@Override
			public void performWith(String key) {
				if (filter.isFiltered(key)) {
					dest.map.put(key, src.getValueForKey(key));
				}
			}
		});
	}

	public boolean containsKey(String key) {
		return map.containsKey(key);
	}

	public Object remove(String key) {
		return map.remove(key);
	}

	public Object getValueForKey(String key) {
		return map.get(key);
	}

	public Long getLongForKey(String key) {
		Object value = getValueForKey(key);
		assert value instanceof Long;
		return (Long) value;
	}

	public Integer getIntForKey(String key) {
		Object value = getValueForKey(key);
		assert value instanceof Integer;
		return (Integer) value;
	}

	public Double getDoubleForKey(String key) {
		Object value = getValueForKey(key);
		assert value instanceof Double;
		return (Double) value;
	}

	public String getStringForKey(String key) {
		Object value = getValueForKey(key);
		assert value instanceof String;
		return (String) value;
	}

	public Boolean getBooleanForKey(String key) {
		Object value = getValueForKey(key);
		assert value instanceof Boolean;
		return (Boolean) value;
	}

	public KeyValues set(String key, Object value) {
		if (key != null && !key.isEmpty() && value != null) {
			map.put(key, value);
		}
		return this;        //to enable multi-dot notation
	}

	@Override
	public void cleanup() {
		map.clear();
	}

	@Override
	public String toString() {
		return "KeyValues{" + "map=" + map + '}';
	}

	public static final class Filter implements Cleanupable {
		private final Set<String> keys;

		public Filter(String... filterKeys) {
			if (filterKeys != null && filterKeys.length > 0) {
				keys = new HashSet<String>();
				for (int index = 0; index < filterKeys.length; index++) {
					keys.add(filterKeys[index]);
				}
			} else {
				keys = null;
			}
		}

		public boolean isFiltered(String key) {
			return keys != null ? keys.contains(key) : true;
		}

		@Override
		public void cleanup() {
			if (keys != null) {
				keys.clear();
			}
		}
	}
}
