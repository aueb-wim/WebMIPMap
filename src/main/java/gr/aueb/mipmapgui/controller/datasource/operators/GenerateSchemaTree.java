/*
    Copyright (C) 2007-2011  Database Group - Universita' della Basilicata
    Giansalvatore Mecca - giansalvatore.mecca@unibas.it
    Salvatore Raunich - salrau@gmail.com
    Marcello Buoncristiano - marcello.buoncristiano@yahoo.it

    This file is part of ++Spicy - a Schema Mapping and Data Exchange Tool
    
    ++Spicy is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    ++Spicy is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with ++Spicy.  If not, see <http://www.gnu.org/licenses/>.
 */
 
package gr.aueb.mipmapgui.controller.datasource.operators;

import it.unibas.spicy.model.datasource.INode;
import it.unibas.spicy.model.datasource.nodes.AttributeNode;
import it.unibas.spicy.model.datasource.nodes.LeafNode;
import it.unibas.spicy.model.datasource.nodes.MetadataNode;
import it.unibas.spicy.model.datasource.nodes.SequenceNode;
import it.unibas.spicy.model.datasource.nodes.SetNode;
import it.unibas.spicy.model.datasource.nodes.TupleNode;
import it.unibas.spicy.model.datasource.nodes.UnionNode;
import it.unibas.spicy.model.datasource.operators.CheckJoinsAndKeys;
import it.unibas.spicy.model.datasource.operators.INodeVisitor;
import it.unibas.spicy.model.mapping.IDataSourceProxy;
import java.util.Random;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class GenerateSchemaTree {

    public JSONObject buildSchemaTreeDataObject(IDataSourceProxy dataSource) {
        SchemaTreeVisitor visitor = new SchemaTreeVisitor(dataSource);  
        visitor.visitNode(dataSource.getIntermediateSchema());
        JSONObject JSONTreeData = visitor.getJSONTreeData();
        return JSONTreeData;
    }
}

class SchemaTreeVisitor {

    private IDataSourceProxy dataSource;
    private DefaultMutableTreeNode treeRoot;
    private DefaultMutableTreeNode currentTreeNode;
    private CheckJoinsAndKeys keyChecker = new CheckJoinsAndKeys();
    private JSONObject JSONdata = new JSONObject();
    private JSONArray JSONarr = new JSONArray();
    private String type;
    private Random randomGenerator = new Random();
    private int counter = randomGenerator.nextInt(100000000);

    public SchemaTreeVisitor(IDataSourceProxy dataSource) {
        this.dataSource = dataSource;
        this.type = dataSource.getType();
    }
    
    void visitNode(INode node) {
        node.setValue(counter++);
        JSONObject nodeJSON = new JSONObject();        
        nodeJSON.put("id","sch_node" + node.getValue());        
        if(node.isRoot()){            
            nodeJSON.put("parent","#");
            nodeJSON.put("text",node.getLabel());            
            if(this.type.equalsIgnoreCase("xml")){                               
                nodeJSON.put("icon","xml_db.png");                
            }
            else{
                nodeJSON.put("icon","root_db.png");                
            }        
        } 
        else {            
            nodeJSON.put("parent","sch_node"+node.getFather().getValue());            
            if (node instanceof AttributeNode){
                nodeJSON.put("text", node.getLabel() + " (" + node.getChild(0).getLabel() + ")");
                if (keyChecker.checkIfIsKey(node, dataSource.getDataSource())){
                   nodeJSON.put("icon","key.png"); 
                }
                else{
                    nodeJSON.put("icon","attribute.png"); 
                }
            }            
            else if(node instanceof SetNode){                
                nodeJSON.put("text",node.getLabel());                
                if(this.type.equalsIgnoreCase("relational")){                       
                    nodeJSON.put("icon","relational_set.png");                    
                }
                else if(this.type.equalsIgnoreCase("xml")){
                    nodeJSON.put("icon","xml_set.png");                    
                }
                else if(this.type.equalsIgnoreCase("sql")){
                    nodeJSON.put("icon","sql_set.png");                    
                }
                else{ //csv
                    nodeJSON.put("icon","csv_set.gif");                    
                }
            }            
            else{ //tuple                
                nodeJSON.put("text",node.getLabel());                
                if(this.type.equalsIgnoreCase("relational")){             
                    nodeJSON.put("icon","relational_tuple.png");                    
                }
                else if(this.type.equalsIgnoreCase("xml")){
                    nodeJSON.put("icon","xml_tuple.png");                    
                }
                else if(this.type.equalsIgnoreCase("sql")){
                    nodeJSON.put("icon","sql_tuple.png");                    
                }
                else{ //csv
                    nodeJSON.put("icon","csv_tuple.png");                    
                }
            }
        }
        JSONarr.add(nodeJSON);
        
        if (node instanceof SetNode || node instanceof TupleNode){
            for (INode child : node.getChildren()) {
                visitNode(child);
            }
        }
        /*NOTE*/
        //on the desktop application only instance nodes have values
        //as a result, the engine will recognize it as "schema" node and not "instance"
        //but we need the value as an identifier to build the tree and then make the connections for TGD View
        //otherwise, we can set it to null here:
        //node.setValue(null);
    }

    public MutableTreeNode getResult() {
        return this.treeRoot;
    }
    
    public JSONObject getJSONTreeData() {
        this.JSONdata.put("data", this.JSONarr);
        return this.JSONdata;
    }
}

