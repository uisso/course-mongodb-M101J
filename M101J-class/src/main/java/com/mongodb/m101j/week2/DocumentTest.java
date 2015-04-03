package com.mongodb.m101j.week2;

import java.util.Arrays;
import java.util.Date;

import org.bson.Document;

import com.mongodb.m101j.util.Helpers;

/**
 * 
 * TODO Add element description, main objective and relations
 *
 * @author wilson
 *
 */
public class DocumentTest {

    public static void main(String[] args) {

        Document document = new Document()//
                .append("userName", "jyemin")//
                .append("birthDate", new Date(234832423))//
                .append("programmer", true)//
                .append("age", 8)//
                .append("languages", Arrays.asList("Java", "C++"))//
                .append("address", new Document("street", "20 Main")//
                        .append("town", "Westfield")//
                .append("zip", "56789"));

        Helpers.printJson(document);
    }
}
