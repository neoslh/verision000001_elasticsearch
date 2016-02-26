package com.project.comfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.elasticsearch.client.TransportClientFactoryBean;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.apache.commons.lang.StringUtils.*;
import static org.elasticsearch.common.settings.ImmutableSettings.*;

import org.elasticsearch.common.settings.Settings;

@Configuration
@EnableWebMvc
@EnableElasticsearchRepositories(basePackages = "com.project.repository")
@ComponentScan(basePackages = "com.project")
@PropertySource(value = "classpath:elasticsearch.properties")
public class Config {

	private static final Logger logger = LoggerFactory
			.getLogger(TransportClientFactoryBean.class);
	private String clusterNodes = "127.0.0.1:9300";
	private String clusterName = "elasticsearch";
	private Boolean clientTransportSniff = true;
	private Boolean clientIgnoreClusterName = Boolean.FALSE;
	private String clientPingTimeout = "5s";
	private String clientNodesSamplerInterval = "5s";
	private TransportClient client;
	static final String COLON = ":";
	static final String COMMA = ",";

	protected void buildClient() throws Exception {
		client = new TransportClient(settings());
		Assert.hasText(clusterNodes,
				"[Assertion failed] clusterNodes settings missing.");
		for (String clusterNode : split(clusterNodes, COMMA)) {
			String hostName = substringBefore(clusterNode, COLON);
			String port = substringAfter(clusterNode, COLON);
			Assert.hasText(hostName,
					"[Assertion failed] missing host name in 'clusterNodes'");
			Assert.hasText(port,
					"[Assertion failed] missing port in 'clusterNodes'");
			logger.info("adding transport node : " + clusterNode);
			client.addTransportAddress(new InetSocketTransportAddress(hostName,
					Integer.valueOf(port)));
		}
		client.connectedNodes();
	}

	private Settings settings() {
		return settingsBuilder()
				.put("cluster.name", clusterName)
				.put("client.transport.sniff", clientTransportSniff)
				.put("client.transport.ignore_cluster_name",
						clientIgnoreClusterName)
				.put("client.transport.ping_timeout", clientPingTimeout)
				.put("client.transport.nodes_sampler_interval",
						clientNodesSamplerInterval).build();
	}

	@Bean
	public ElasticsearchOperations elasticsearchTemplate() {
		try {
			buildClient();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ElasticsearchTemplate(client);
	}

	@Bean
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(5 * 1024 * 1024);
		return multipartResolver;
	}

}

/*
  D={ id:"8888888888888888888888888888", title:"tttt", content:"ooooo", }
 $.ajax({ url: "http://localhost:8080/project/add", type: 'post', dataType:
  'json', data:JSON.stringify(D), contentType:
  "application/json; charset=utf-8", success: function (data) {
 console.log(data); } });
 */
