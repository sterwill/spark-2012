package org.tailfeather;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;

public class CookieHelper {

	public static Map<String, Cookie> map(Cookie[] cookies) {
		Map<String, Cookie> map = new HashMap<String, Cookie>();
		if (cookies != null) {
			for (Cookie c : cookies) {
				map.put(c.getName(), c);
			}
		}
		return map;
	}
}
