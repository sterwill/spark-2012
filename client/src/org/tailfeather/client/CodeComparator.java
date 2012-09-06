package org.tailfeather.client;

import java.util.Comparator;

import org.tailfeather.entity.Code;

public class CodeComparator implements Comparator<Code> {
	@Override
	public int compare(Code o1, Code o2) {
		if (o1 == o2) {
			return 0;
		}
		return o1.getTime().compareTo(o2.getTime());
	}
}
