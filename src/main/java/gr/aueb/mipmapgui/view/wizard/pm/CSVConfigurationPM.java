/* Copyright 2015-2016 by the Athens University of Economics and Business (AUEB).
   This file is part of WebMIPMap.
   WebMIPMap is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    WebMIPMap is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    You should have received a copy of the GNU General Public License
    along with WebMIPMap.  If not, see <http://www.gnu.org/licenses/>.
 */

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
        