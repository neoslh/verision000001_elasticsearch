package com.project.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "doc", type = "doc")
public class Doc {
	@Id
	private String id;
	private String title;
	private String content;
	
	
	
	public Doc(){};
	
	public Doc(String _id,String _title,String _content)
	{
		id=_id;
		title=_title;
		content=_content;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


}
