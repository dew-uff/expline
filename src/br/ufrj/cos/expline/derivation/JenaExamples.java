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
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.rdf.model.impl.StatementImpl;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasoner;
import com.hp.hpl.jena.reasoner.rulesys.Rule;
import com.hp.hpl.jena.util.PrintUtil;

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
    }
    
    
    public static void main(String[] args) {
        try {
//            new ManualExample().test1();
//            new ManualExample().test2("file:testing/reasoners/rdfs/dttest2.nt");
//            new ManualExample().test2("file:testing/reasoners/rdfs/dttest3.nt");
              new JenaExamples().test3();
//            new ManualExample().test4();
        	
        } catch (Exception e) {
            System.out.println("Problem: " + e);
            e.printStackTrace();
        }
    }
}
