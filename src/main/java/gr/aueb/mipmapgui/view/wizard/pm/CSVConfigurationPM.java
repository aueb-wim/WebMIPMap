package gr.aueb.mipmapgui.view.wizard.pm;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;

public class CSVConfigurationPM {

    private HashSet <String> schemaPathList = new HashSet();
    private String dbName;
    
    public HashSet<String> getSchemaPathList(){
        return this.schemaPathList;
    }
    
    public void addToSchemaPathList(String filePath){  
        HashSet<String> oldPathList = new HashSet<> (this.schemaPathList);
        this.schemaPathList.add(filePath);   
    }
    
    public void removeFromSchemaPathList(String fileName){   
        HashSet<String> oldPathList = new HashSet<> (this.schemaPathList);
        //find the file path that ends with the specific filename and remove it from the list
        Iterator<String> iterator = this.schemaPathList.iterator();
        while (iterator.hasNext()) {
            String element = iterator.next();
            File userFile = new File(element);
            String listFileName = userFile.getName();
            if(listFileName.equals(fileName)){
                iterator.remove();            
            }
        }
    }

    public String getDBName() {
        return this.dbName;
    }

    public void setDBName(String dbName) {
        String oldDBName = this.dbName;
        this.dbName = dbName;
    }
        
    public boolean checkDatabaseNameField() {
        return (this.getDBName()!= null && !this.getDBName().equals(""));    
    }   
}
        