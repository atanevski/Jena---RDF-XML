package jena.examples.rdf;
/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

/**
 * Tutorial navigating a model
 */
public class TestJena extends Object {

    public static void main(String args[]) {
        Scanner scaner = new Scanner(System.in);
        String inputFileName = "./Resources/hifm-dataset.ttl";
        Model model = ModelFactory.createDefaultModel();
        InputStream in = FileManager.get().open(inputFileName);
        model.read(new InputStreamReader(in), null, "TTL");

        String HIFMprefix = "http://purl.org/net/hifm/ontology#";
        String DrugBankPrefix = "http://wifo5-04.informatik.uni-mannheim.de/drugbank/resource/drugbank/";

        Property rdfs_label = model.createProperty(RDFS.label.toString());
        Property hifm_ont_id = model.createProperty(HIFMprefix + "id");
        Property hifm_ont_manufacturer = model.createProperty(HIFMprefix + "manufacturer");
        Property hifm_ont_pack = model.createProperty(HIFMprefix + "packaging");
        Property hifm_ont_PriceWOvat = model.createProperty(HIFMprefix + "refPriceNoVAT");
        Property hifm_ont_dosage = model.createProperty(HIFMprefix + "dosageForm");
        Property hifm_ont_strength = model.createProperty(HIFMprefix + "strength");
        Property hifm_ont_similarTo = model.createProperty(HIFMprefix + "similarTo"); //????
        Property drugbank_brandName = model.createProperty(DrugBankPrefix + "brandName");
        Property drugbank_genericName = model.createProperty(DrugBankPrefix + "genericName");
        Property drugbank_atcCode = model.createProperty(DrugBankPrefix + "atcCode");
        Resource drugbank_drugs = model.createResource(HIFMprefix + "Drug");

        ResIterator res1 = model.listResourcesWithProperty(RDF.type, drugbank_drugs);

        String Manu = "";
        String totest = "Atorvastatin";
        System.out.println("ID\t\tLabel\t\t\tPrice\t\tManufacturer");

        //while (res1.hasNext()) {
        Resource r = res1.next();
        String Label = r.getRequiredProperty(rdfs_label).getString();

        if (totest.equals(Label)) {
            String id = r.getRequiredProperty(hifm_ont_id).getString();
            String Price = r.getRequiredProperty(hifm_ont_PriceWOvat).getString();
            try {
                Manu = r.getRequiredProperty(hifm_ont_manufacturer).getString();
            } catch (Exception e) {
                Manu = "undefined";
            }

            System.out.println(String.format("%s\t\t%s\t\t\t\t\t\t%s\t\t%s\t", id, Label, Price, Manu));
            StmtIterator aa = r.listProperties(hifm_ont_similarTo);

            ArrayList<String> allSimilarDrugsToThisOne = new ArrayList<>();
            System.out.println("SIMILAR");
            while (aa.hasNext()) {
                Statement b = aa.nextStatement();
                String similarDrug = b.getProperty(hifm_ont_similarTo).getResource().toString();
                findEm(similarDrug, model, Label);

            }
        }

        //}
    }

    private static void findEm(String similarDrug, Model model, String labelOriginal) {
        String HIFMprefix = "http://purl.org/net/hifm/ontology#";
        String DrugBankPrefix = "http://wifo5-04.informatik.uni-mannheim.de/drugbank/resource/drugbank/";
        String HIfMdataPrefix = "http://purl.org/net/hifm/data#";
        Property rdfs_label = model.createProperty(RDFS.label.toString());
        Property hifm_ont_id = model.createProperty(HIFMprefix + "id");
        Property hifm_ont_manufacturer = model.createProperty(HIFMprefix + "manufacturer");
        Property hifm_ont_pack = model.createProperty(HIFMprefix + "packaging");
        Property hifm_ont_PriceWOvat = model.createProperty(HIFMprefix + "refPriceNoVAT");
        Property hifm_ont_dosage = model.createProperty(HIFMprefix + "dosageForm");
        Property hifm_ont_strength = model.createProperty(HIFMprefix + "strength");
        Property hifm_ont_similarTo = model.createProperty(HIFMprefix + "similarTo"); //????
        Property drugbank_brandName = model.createProperty(DrugBankPrefix + "brandName");
        Property drugbank_genericName = model.createProperty(DrugBankPrefix + "genericName");
        Property drugbank_atcCode = model.createProperty(DrugBankPrefix + "atcCode");
        Resource drugbank_drugs = model.createResource(HIFMprefix + "Drug");
        ResIterator res1 = model.listResourcesWithProperty(RDF.type, drugbank_drugs);

        String[] split = similarDrug.split("#");
        String Manu = "";
        while (res1.hasNext()) {

            Resource r = res1.next();

            String Label = r.getRequiredProperty(rdfs_label).getString();
            String id = r.getRequiredProperty(hifm_ont_id).getString();
            String Price = r.getRequiredProperty(hifm_ont_PriceWOvat).getString();
            try {
                Manu = r.getRequiredProperty(hifm_ont_manufacturer).getString();
            } catch (Exception e) {
                Manu = "undefined";
            }

            if (split[1].equals(id) && !(labelOriginal.equals(Label))) {
                System.out.println(String.format("%s\t\t%s\t\t\t\t\t\t%s\t\t%s\t", id, Label, Price, Manu));
            }

        }

    }

}
