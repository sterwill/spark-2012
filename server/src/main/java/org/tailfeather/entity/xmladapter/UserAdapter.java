package org.tailfeather.entity.xmladapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tailfeather.dao.UserDao;
import org.tailfeather.entity.User;

@Component
public class UserAdapter extends XmlAdapter<String, User> {

	@Autowired
	private UserDao userDao;

	@Override
	public User unmarshal(String v) throws Exception {
		if (v == null) {
			return null;
		}
		return userDao.findById(v);
	}

	@Override
	public String marshal(User v) throws Exception {
		return v.getId();
	}

}
