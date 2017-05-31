/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.users.recommendation.mappingmodel;

import it.unibas.spicy.model.correspondence.GetIdFromDb;
import gr.aueb.mipmapgui.Costanti;
import java.util.Objects;
import org.apache.commons.lang3.math.NumberUtils;

/**
 *
 * @author ioannisxar
 */
public class Correspondence {
    
    private String source, target, transformation, type, sequence, offset, owner;
    private GetIdFromDb dbConfig;
    private double score;    

    public Correspondence(String target){
        this.target = target;
        type = Costanti.EMPTY_CORRESPONDENCE;
        transformation = "No transformation";
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
    
    public void addScore(double score){
        this.score = score;
    }
    
    public double getScore(){
        return score;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
    
    @Override
    public boolean equals(Object o){
        if(o instanceof Correspondence){
            if(type.equals(((Correspondence) o).getType())){
                switch (type) {
                    case Costanti.SIMPLE_CORRESPONDENCE:
                    case Costanti.EMPTY_CORRESPONDENCE:
                    case Costanti.CONSTANT_DATE:
                    case Costanti.CONSTANT_DATETIME:
                    case Costanti.CONSTANT_DB_SEQUENCE:
                        return true;
                    case Costanti.CONSTANT_STRING:
                    case Costanti.CONSTANT_NUMBER:
                        return source.equals(((Correspondence) o).getSource());
                    case Costanti.FUNCTION:
                        return transformation.equals(((Correspondence) o).getTransformation());
                    case Costanti.CONSTANT_SEQUENCE:
                        return offset.equals(((Correspondence) o).getOffset());
                    default:
                        return false;
                }
            } else {
                return false;
            }
        }else{
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.source);
        hash = 97 * hash + Objects.hashCode(this.target);
        hash = 97 * hash + Objects.hashCode(this.transformation);
        hash = 97 * hash + Objects.hashCode(this.type);
        hash = 97 * hash + Objects.hashCode(this.sequence);
        hash = 97 * hash + Objects.hashCode(this.offset);
        hash = 97 * hash + Objects.hashCode(this.dbConfig);
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.score) ^ (Double.doubleToLongBits(this.score) >>> 32));
        return hash;
    }
    
}
