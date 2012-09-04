package org.tailfeather.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.tailfeather.entity.Code;
import org.tailfeather.exceptions.CodeNotFoundException;
import org.tailfeather.repository.CodeRepository;

@Component
public class CodeDao {
	@Autowired
	CodeRepository codeRepository;

	@Transactional
	public Code create(Code newObject) {
		return codeRepository.save(newObject);
	}

	@Transactional
	public void delete(String id) throws CodeNotFoundException {
		try {
			codeRepository.delete(id);
		} catch (EntityNotFoundException e) {
			throw new CodeNotFoundException(id);
		}
	}

	@Transactional
	public List<Code> findAll() {
		return new ArrayList<Code>(codeRepository.findAll());
	}

	@Transactional
	public Code findById(String id) {
		return codeRepository.findOne(id);
	}

	@Transactional
	public Iterable<Code> findByUser(String userId) {
		return codeRepository.findByUser(userId);
	}

	@Transactional
	public Code update(Code code) throws CodeNotFoundException {
		try {
			return codeRepository.save(code);
		} catch (EntityNotFoundException e) {
			throw new CodeNotFoundException(code.getId());
		}
	}
}
