/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.users.recommendation.mappingmodel;

import it.unibas.spicy.model.correspondence.GetIdFromDb;
import gr.aueb.mipmapgui.Costanti;
import org.apache.commons.lang3.math.NumberUtils;

/**
 *
 * @author ioannisxar
 */
public class Correspondence {
    
    private String source, target, transformation, type, sequence, offset;
    private GetIdFromDb dbConfig;
    
    public Correspondence(){
        type = Costanti.EMPTY_CORRESPONDENCE;
    }
    
    public Correspondence(String source, String target, String transformation){
        dbConfig = null;
        sequence = null;
        offset = null;
        this.source = source;
        this.target = target;
        this.transformation = transformation;
        if(!source.equals(transformation)){
            type = Costanti.FUNCTION;
        } else {
            if(source.equals("date()")){
                type = Costanti.CONSTANT_DATE;
            } else if(source.equals("datetime()")){
                type = Costanti.CONSTANT_DATETIME;
            } else if(source.startsWith("\"") && source.endsWith("\"")){
                type = Costanti.CONSTANT_STRING;
            } else if(NumberUtils.isNumber(source)){
                type = Costanti.CONSTANT_NUMBER;
            } else {
                type = Costanti.SIMPLE_CORRESPONDENCE;
            }
        }
    }
   
    public Correspondence(String source, String target, String transformation, String sequence, String offset){
        dbConfig = null;
        this.source = source;
        this.target = target;
        this.transformation = transformation;
        this.sequence = sequence;
        this.offset = offset;
        this.type = Costanti.CONSTANT_SEQUENCE;
    }
    
    public Correspondence(String source, String target, String transformation, String sequence, GetIdFromDb dbConfig){
        dbConfig = null;
        this.source = source;
        this.target = target;
        this.transformation = transformation;
        this.sequence = sequence;
        this.dbConfig = dbConfig;
        this.type = Costanti.CONSTANT_DB_SEQUENCE;
    }
    
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTransformation() {
        return transformation;
    }

    public void setTransformation(String transformation) {
        this.transformation = transformation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public GetIdFromDb getDbConfig() {
        return dbConfig;
    }

    public void setDbConfig(GetIdFromDb dbConfig) {
        this.dbConfig = dbConfig;
    }
    
}
