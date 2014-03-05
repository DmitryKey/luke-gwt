package org.apache.luke.utils;

import java.util.Set;

/**
 * Util package for displaying data structure contents
 * @author dmitrykan
 *
 */
public class DataStructureDisplayer<T> {
	
	public void displaySet(Set<T> _set) {
		if (_set == null)
			return;
		System.out.println("set:" + _set.toString() + " of size:" + _set.size());
		for (T elem: _set) {
			System.out.println(elem.toString());
		}
	}

}
