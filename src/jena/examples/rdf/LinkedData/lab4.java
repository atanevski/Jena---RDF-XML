/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jena.examples.rdf.LinkedData;

import org.apache.jena.rdf.model.*;

import org.apache.jena.util.FileManager;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

/**
 *
 * @author Aleksandar Tanevski
 */
public class lab4 {

    public static final String dboPrefix = "http://dbpedia.org/ontology/";
    public static final String geoPrefix = "http://www.w3.org/2003/01/geo/wgs84_pos#";
    public static final String ns8Prefix = "http://dbpedia.org/ontology/PopulatedPlace/";
    public static final String ns7Prefix = "http://www.w3.org/ns/prov#";
    public static final String dbpPrefix = "http://dbpedia.org/property/";
    public static final String dctPrefix = "http://purl.org/dc/terms/";
    public static final String owlPrefix = "http://www.w3.org/2002/07/owl#";
    public static final String rdfPrefix = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    public static final String georssPrefix = "http://www.georss.org/georss/";
    public static final String rdfsPrefix = "http://www.w3.org/2000/01/rdf-schema#";
    public static final String foaPrefix = "http://xmlns.com/foaf/0.1/";
    public static final String dbDataPrefix = "http://dbpedia.org/data/";

    public static void main(String[] args) {
        Scanner scaner = new Scanner(System.in);
        String city = "http://dbpedia.org/data/Skopje.xml";

        Model modelCity = ModelFactory.createDefaultModel();
        modelCity.read(city, "TTL");
        //model1.write(System.out, "TTL");

        //Print city name 
        System.out.println("City name - " + SomethingSpecial(modelCity, RDFS.label, "en"));

        //Print country name 
        Property aProperty = modelCity.createProperty(dboPrefix + "country");
        NodeIterator aaa = modelCity.listObjectsOfProperty(aProperty);

        Model countryModel = ModelFactory.createDefaultModel();
        String tempString = aaa.next().toString();
        String[] split = tempString.split("/");

        countryModel.read(dbDataPrefix + split[split.length - 1]);

        System.out.println("Country name - " + SomethingSpecial(countryModel, RDFS.label, "en"));

        //model 1 - all people with birthPlace property
        Property birthPlace = modelCity.createProperty(dboPrefix + "birthPlace");
        ResIterator res = modelCity.listResourcesWithProperty(birthPlace);

        Resource r = null;
        while (res.hasNext()) {
            r = res.next();
            String temp = r.toString().toLowerCase();
            if (temp.contains("milcho") && temp.contains("manchevski")) {
                break;
            }
        }

        Resource MilchoManchevski = r;

        //model 2 - Milcho Mancevski
        Model model2 = ModelFactory.createDefaultModel();
        model2.read(MilchoManchevski.toString(), "TTL");

        System.out.println("Name of person - " + SomethingSpecial(model2, RDFS.label, "en"));
        System.out.println("Occupation - " + SpecialLiteral(model2, dbpPrefix + "occupation"));
        System.out.println();

        Property director = model2.createProperty(dboPrefix + "director");
        ResIterator res1 = model2.listResourcesWithProperty(director);

        Resource r1 = null;
        while (res1.hasNext()) {
            r1 = res1.next();
            Model tempModel = ModelFactory.createDefaultModel();
            tempModel.read(r1.toString());

            System.out.println("Movie title - " + SomethingSpecial(tempModel, RDFS.label, "en"));
            System.out.println("--------------------------------------------------------------");
            System.out.println("Abstract - " + SpecialLiteral(tempModel, dboPrefix + "abstract"));
            System.out.println("Starring: ");
            SpecialLiteral1(tempModel, dboPrefix + "starring");
            System.out.println("\n");
        }
    }

    public static String SomethingSpecial(Model m, Property p, String lang) {
        NodeIterator NodeIter = m.listObjectsOfProperty(RDFS.label);
        while (NodeIter.hasNext()) {
            RDFNode temp = NodeIter.next();
            if (lang.equals(temp.asLiteral().getLanguage())) {
                return temp.asLiteral().getValue().toString();

            }
        }
        return null;
    }

    public static String SpecialLiteral(Model m, String property) {
        Property prop = m.createProperty(property);
        NodeIterator NodeIter = m.listObjectsOfProperty(prop);
        return NodeIter.next().asLiteral().getString();
    }

    public static String SpecialLiteral(Model m, Property property) {
        NodeIterator NodeIter = m.listObjectsOfProperty(property);
        return NodeIter.next().asLiteral().getString();
    }

    public static void SpecialLiteral1(Model m, String property) {
        Property prop = m.createProperty(property);
        NodeIterator NodeIter = m.listObjectsOfProperty(prop);
        while (NodeIter.hasNext()) {
            String[] split = NodeIter.next().toString().split("/");
            System.out.println("\t" + split[split.length - 1].replaceAll("_", " "));
        }
    }
}
