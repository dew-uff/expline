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

package br.ufrj.cos.expline.derivation;

import java.io.PrintWriter;
import java.util.Iterator;

import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.rdf.model.impl.StatementImpl;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasoner;
import com.hp.hpl.jena.reasoner.rulesys.Rule;
import com.hp.hpl.jena.util.PrintUtil;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * Some code samples from the user manual.
 */
public class JenaExamples {

  
    
    /** illustrate generic rules and derivation tracing */
    public void test3() {
        // Test data
        String egNS = PrintUtil.egNS;   // Namespace for examples
        Model rawData = ModelFactory.createDefaultModel();
        Property p = rawData.createProperty(egNS, "p");
        Resource A = rawData.createResource(egNS + "A");
        Resource B = rawData.createResource(egNS + "B");
        Resource C = rawData.createResource(egNS + "C");
        Resource D = rawData.createResource(egNS + "D");
        A.addProperty(p, B);
        B.addProperty(p, C);
        C.addProperty(p, D);
        
        // Rule example
        String rules = "[rule1: (?a eg:p ?b) (?b eg:p ?c) -> (?a eg:p ?c)]";
        Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
        reasoner.setDerivationLogging(true);
        InfModel inf = ModelFactory.createInfModel(reasoner, rawData);
        
        
        //Perguntar se uma afirmação é verdade
        System.out.println("Perguntar se uma afirmação é verdade");
        System.out.println(inf.contains(new StatementImpl(A, p, D)));
        
        //Listar os objetos que atendem uma determinada afirmação
        System.out.println("Listar os objetos que atendem uma determinada afirmação");
        for (NodeIterator i = inf.listObjectsOfProperty(A, p); i.hasNext(); ) {
        	Resource temp = i.next().asResource();
        	System.out.println(temp);
        }
        
        //Listar a "arvore de derivação" para uma determinada afirmação
        System.out.println("Listar a arvore de derivação para uma determinada afirmação");
        PrintWriter out = new PrintWriter(System.out);
        for (StmtIterator i = inf.listStatements(A, p, D); i.hasNext(); ) {
            Statement s = i.nextStatement(); 
            System.out.println("Statement is " + s);
            for (Iterator<com.hp.hpl.jena.reasoner.Derivation> id = inf.getDerivation(s); id.hasNext(); ) {
            	com.hp.hpl.jena.reasoner.Derivation deriv = id.next();
                deriv.printTrace(out, true);
            }
        }
        out.flush();
        
        
    	// Query for all things related to "a" by "p"
        System.out.println("Consulta");
        StmtIterator i = inf.listStatements(A, p, (RDFNode)null);
        while (i.hasNext()) {
        	System.out.println(" - " + PrintUtil.print(i.nextStatement()));
        }
        
    }
    
    
    /** illustrate generic rules and derivation tracing */
    public void test4() {
        // Test data
        String egNS = PrintUtil.egNS;   // Namespace for examples
        Model rawData = ModelFactory.createDefaultModel();
        Property p = rawData.createProperty(egNS, "p");
        
        Property has = rawData.createProperty(egNS, "has");
        Property selects = rawData.createProperty(egNS, "selects");
        Property contains = rawData.createProperty(egNS, "contains");
        Property implicates = rawData.createProperty(egNS, "implicates");
        Property conflicts = rawData.createProperty(egNS, "conflicts");
        
        Resource mandatory = rawData.createResource(egNS + "mand");
        Resource variationPoint = rawData.createResource(egNS + "vp");
        Resource opVariationPoint = rawData.createResource(egNS + "opvp");
        Resource optional = rawData.createResource(egNS + "op");
        Resource var = rawData.createResource(egNS + "var");
        
        
        
        Resource expLine = rawData.createResource(egNS + "ExpLine");
        Resource absWf = rawData.createResource(egNS + "AbstWf");
        
        Resource A = rawData.createResource(egNS + "A");
        Resource B = rawData.createResource(egNS + "B");
        Resource B1 = rawData.createResource(egNS + "B1");
        Resource B2 = rawData.createResource(egNS + "B2");
        Resource C = rawData.createResource(egNS + "C");
        Resource D = rawData.createResource(egNS + "D");
        Resource D1 = rawData.createResource(egNS + "D1");
        Resource D2 = rawData.createResource(egNS + "D2");
        
        
        
        A.addProperty(RDF.type, mandatory);
        B.addProperty(RDF.type, variationPoint);
        B1.addProperty(RDF.type, var);
        B2.addProperty(RDF.type, var);
        C.addProperty(RDF.type, optional); 
        D.addProperty(RDF.type, variationPoint);
        D1.addProperty(RDF.type, var);
        D2.addProperty(RDF.type, var);
        
        B.addProperty(contains, B1);
        B.addProperty(contains, B2);
        D.addProperty(contains, D1);
        D.addProperty(contains, D2);
        
        
        absWf.addProperty(selects, A);
        absWf.addProperty(selects, B1);
        absWf.addProperty(selects, C);
        //absWf.addProperty(selects, D2);
        
        // Rule example
        String rules = "[rule1: (eg:AbstWf eg:selects ?a) (?a rdf:type eg:mand) -> (eg:AbstWf eg:has ?a) ]"+
        			   "[rule2: (eg:AbstWf eg:selects ?a) (?a rdf:type eg:op) -> (eg:AbstWf eg:has ?a) ]"+
        			   "[rule3: (eg:AbstWf eg:selects ?a) (?a rdf:type eg:var) (?b rdf:type eg:vp) (?b eg:contains ?a) -> (eg:AbstWf eg:has ?a) ]"+
        			   "[rule4: (eg:AbstWf eg:isProductOf eg:ExpLine) -> (eg:AbstWf eg:has ?a) ]";
//        			   "[rule4: (eg:AbstWf eg:selects eg:B1) -> (eg:AbstWf eg:implicates eg:D1) ]"+
//        			   "[rule5: (eg:AbstWf eg:selects eg:B2) -> (eg:AbstWf eg:conflicts eg:D2) ]"+
//        			   "[rule6: (eg:AbstWf eg:selects eg:B2) -> (eg:AbstWf eg:conflicts eg:D2) ]";
        
        
        Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
        reasoner.setDerivationLogging(true);
        InfModel inf = ModelFactory.createInfModel(reasoner, rawData);
        
        
        //Perguntar se uma afirmação é verdade
//        System.out.println("Perguntar se uma afirmação é verdade");
//        System.out.println(inf.contains(new StatementImpl(A, p, D)));
        
        
    	// Query for all things related to "a" by "p"
        System.out.println("Consulta");
        StmtIterator i = inf.listStatements(absWf, has, (RDFNode)null);
        while (i.hasNext()) {
        	System.out.println(" - " + PrintUtil.print(i.nextStatement()));
        }
        
        
    }
    
    
    public static void main(String[] args) {
        try {
//            new ManualExample().test1();
//            new ManualExample().test2("file:testing/reasoners/rdfs/dttest2.nt");
//            new ManualExample().test2("file:testing/reasoners/rdfs/dttest3.nt");
              new JenaExamples().test4();
//            new ManualExample().test4();
        	
        } catch (Exception e) {
            System.out.println("Problem: " + e);
            e.printStackTrace();
        }
    }
}
