package com.project.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.project.models.Doc;



public interface DocRepository extends ElasticsearchRepository<Doc,String> {

	public Doc findById(String id);

	public List<Doc> findByTitle(String title);
}

