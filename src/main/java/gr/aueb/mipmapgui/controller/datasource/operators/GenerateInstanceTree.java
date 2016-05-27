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
import it.unibas.spicy.model.datasource.operators.INodeVisitor;
import it.unibas.spicy.model.mapping.IDataSourceProxy;
////import it.unibas.spicygui.vista.treepm.TreeNodeAdapter;
////import it.unibas.spicygui.vista.treepm.TreeRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
////import org.apache.commons.logging.Log;
////import org.apache.commons.logging.LogFactory;

public class GenerateInstanceTree {

  /*  public JTree buildInstanceTree(INode instanceRoot, IDataSourceProxy dataSource) {
        InstanceTreeVisitor visitor = new InstanceTreeVisitor(dataSource);
        instanceRoot.accept(visitor);
        JTree instanceTree = new JTree(visitor.getResult());
        ////TreeRenderer rendererTree = new TreeRenderer();
        ////instanceTree.setCellRenderer(rendererTree);
        return instanceTree;
    }*/
    
    //giannisk
    public String buildInstanceTreeString(INode instanceRoot, IDataSourceProxy dataSource) {
        InstanceTreeVisitor visitor = new InstanceTreeVisitor(dataSource);
        visitor.visitNode2(instanceRoot);
        String instanceTreeString = visitor.getResult2();
        return instanceTreeString;
    }
}

class InstanceTreeVisitor implements INodeVisitor {

    ////private Log logger = LogFactory.getLog(InstanceTreeVisitor.class);
    private DefaultMutableTreeNode treeRoot;
    private DefaultMutableTreeNode currentTreeNode;
    private IDataSourceProxy dataSource;
    private String treeString;

    public InstanceTreeVisitor(IDataSourceProxy dataSource) {
        this.dataSource = dataSource;
    }
    
    @Override
    public void visitSetNode(SetNode node) {
        visitNode2(node);
    }

    @Override
    public void visitTupleNode(TupleNode node) {
        visitNode2(node);
    }

    @Override
    public void visitSequenceNode(SequenceNode node) {
        visitNode2(node);
    }

    @Override
    public void visitUnionNode(UnionNode node) {
        visitNode2(node);
    }

    private void visitNode(INode node) {
        //web - check before change
        //DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(new TreeNodeAdapter(node, dataSource.getType()));
        
        DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(node);
        ////if (logger.isDebugEnabled()) logger.debug("Creato nuovo nodo: " + treeNode);
        if (treeRoot == null) {
            treeRoot = treeNode;
        } else {
            currentTreeNode.add(treeNode);
        }
        currentTreeNode = treeNode;
        for (INode child : node.getChildren()) {
            child.accept(this);
            currentTreeNode = treeNode;
        }
    }
    
    //giannisk
    void visitNode2(INode node) {
        if(node.isRoot()){
            this.treeString="{ \"id\" : \"node"+node.getValue()+"\", \"parent\" : \"#\" , \"text\" : \""+node.getLabel()+"\" }";
        } else {
            if (node instanceof AttributeNode){
                this.treeString=this.treeString+",{ \"id\" : \"node"+node.getValue()+"\", \"parent\" : \"node"+node.getFather().getValue()+"\" , \"text\" : \""+node.getLabel()+" ("+node.getChild(0).getValue()+")\" }";
            }
            else{
                this.treeString=this.treeString+",{ \"id\" : \"node"+node.getValue()+"\", \"parent\" : \"node"+node.getFather().getValue()+"\" , \"text\" : \""+node.getLabel()+"\" }";
            }
        }

        if (node instanceof SetNode || node instanceof TupleNode){
            for (INode child : node.getChildren()) {
                visitNode2(child);
            }
        }
    }    

    @Override
    public void visitAttributeNode(AttributeNode node) {
        //web - check before change
        //DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(new TreeNodeAdapter(node, dataSource.getType()));
        DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(node);
        currentTreeNode.add(treeNode);
        //return;
    }

    @Override
    public void visitMetadataNode(MetadataNode node) {
        visitAttributeNode(node);
    }

    @Override
    public void visitLeafNode(LeafNode node) {
        //return;
    }

    @Override
    public MutableTreeNode getResult() {
        return this.treeRoot;
    }
    
    //giannisk
    public String getResult2() {
        return this.treeString;
    }
}
