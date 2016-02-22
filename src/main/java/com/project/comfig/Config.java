package com.project.comfig;

import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.node.NodeBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@EnableElasticsearchRepositories(basePackages = "com.project.repository")
@ComponentScan(basePackages = "com.project")
public class Config {


	@Bean
	public NodeBuilder nodeBuilder() {
		return new NodeBuilder();
	}

	@Bean
	public ElasticsearchOperations elasticsearchTemplate() {

		ImmutableSettings.Builder elasticsearchSettings = ImmutableSettings
				.settingsBuilder().put("http.enabled", "false")
				.put("path.data", "/elasticsearch_data/");

		

		return new ElasticsearchTemplate(nodeBuilder().local(true)
				.settings(elasticsearchSettings.build()).node().client());

	}

}
