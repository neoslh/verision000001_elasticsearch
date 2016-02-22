package com.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.models.Doc;
import com.project.repository.*;
@Service("DocServices")
public class DocServicesIml implements DocServices {

	private static Doc D;
	
	@Autowired
	private DocRepository docRepository;

	

	static {
		D = new Doc();
		D.setId("444");
		D.setTitle("titre 2");
		D.setContent("content 2");
	}

	@Override
	public Doc getdoc() {
		return D;
	}

	@Override
	public void addDoc(Doc d) {
		
		docRepository.save(d);
	}

	@Override
	public Iterable<Doc> getdocs() {
		return docRepository.findAll();
	}

}
