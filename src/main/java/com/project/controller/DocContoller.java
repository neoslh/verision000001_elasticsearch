package com.project.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.project.models.Doc;
import com.project.services.DocServices;


@RestController
public class DocContoller {

	@Autowired
	DocServices docservice;
	
	@Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
 
	@RequestMapping(value = "/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Iterable<Doc>> getUser() {
		
		Iterable<Doc> D = docservice.getdocs();
		 
		if (D == null) {
			System.out.println("Error");
			return new ResponseEntity<Iterable<Doc>>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Iterable<Doc>>(D, HttpStatus.OK);
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<Void> addDocument(@RequestBody Doc d,UriComponentsBuilder ucBuilder) {
		d.setData(new HashMap());
		d.getData().put("titre", "toto");
		d.getData().put("auteur", "tata");
		elasticsearchTemplate.deleteIndex(Doc.class);
		elasticsearchTemplate.createIndex(Doc.class);
		elasticsearchTemplate.putMapping(Doc.class);
		System.out.println(d.getContent());
		docservice.addDoc(d);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/add").buildAndExpand().toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public ResponseEntity<String> Upload(@RequestParam("file") MultipartFile multipartFile,@RequestParam("id") String id) 
	{
		String path="D:/";
		String fileName = multipartFile.getOriginalFilename();
        if (!"".equalsIgnoreCase(fileName)) {
            try {
				multipartFile.transferTo(new File(path + fileName));
			} catch (IllegalStateException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				  return new ResponseEntity<>(id,HttpStatus.BAD_REQUEST);
			}
        }
	  
        return new ResponseEntity<String>(id,HttpStatus.OK);
	} 

}
