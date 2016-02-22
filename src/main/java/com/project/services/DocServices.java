package com.project.services;

import com.project.models.Doc;

public interface DocServices {
	Doc getdoc();
	public Iterable<Doc> getdocs();
	void addDoc(Doc d);
}
