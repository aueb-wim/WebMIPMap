package gr.aueb.mipmapgui.view.wizard.pm;

public class SQLConfigurationPM {
    private String dbName;
    private String schemaPath;
    
    public String getDBName() {
        return this.dbName;
    }

    public void setDBName(String dbName) {
        this.dbName = dbName;
    }
    
    public String getSchemaPath() {
        return this.schemaPath;
    }

    public void setSchemaPath(String schemaPath) {
        this.schemaPath = schemaPath;
    }       
}
