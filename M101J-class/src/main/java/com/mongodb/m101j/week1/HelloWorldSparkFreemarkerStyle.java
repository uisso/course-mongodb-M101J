package com.mongodb.m101j.week1;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 
 * TODO Add element description, main objective and relations
 *
 * @author wilson
 *
 */
public class HelloWorldSparkFreemarkerStyle {

	public static void main(String[] args) {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_22);
        configuration.setClassForTemplateLoading(HelloWorldSparkFreemarkerStyle.class, "/");

		Spark.get("/", new Route() {
            public Object handle(Request request, Response response) throws Exception {
                StringWriter writer = new StringWriter();
                try {
                    Template helloTemplate = configuration.getTemplate("hello.ftl");
                    Map<String, Object> helloMap = new HashMap<String, Object>();
                    helloMap.put("name", "Freemarker");
                    helloTemplate.process(helloMap, writer);
                    System.out.println(writer);

                } catch (Exception e) {
                    Spark.halt(500);
                    e.printStackTrace();
                }
                return writer;
			}
		});
	}

}