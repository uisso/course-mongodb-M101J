package com.mongodb.m101j.week1;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 
 * @author wilson
 *
 */
public class HelloWorldFreemarkerStyle {
	
	public static void main(String...args){
		Configuration configuration = new Configuration(Configuration.VERSION_2_3_22);
		configuration.setClassForTemplateLoading(HelloWorldFreemarkerStyle.class, "/");
		
		try {
			Template helloTemplate = configuration.getTemplate("hello.ftl");
			StringWriter writer = new StringWriter();
			Map<String, Object> helloMap = new HashMap<String, Object>();
			helloMap.put("name", "Freemarker");
			helloTemplate.process(helloMap, writer);
			System.out.println(writer);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
