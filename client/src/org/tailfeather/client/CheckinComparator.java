package org.tailfeather.client;

import java.util.Comparator;

import org.tailfeather.entity.Checkin;

public class CheckinComparator implements Comparator<Checkin> {
	@Override
	public int compare(Checkin o1, Checkin o2) {
		if (o1 == o2) {
			return 0;
		}
		return o1.getTime().compareTo(o2.getTime());
	}
}
