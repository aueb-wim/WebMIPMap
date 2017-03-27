package gr.aueb.mipmapgui.controller.file;

import gr.aueb.mipmapgui.Costanti;

public class MipmapDirectories {
    
    public static String getSavedSchemataPath(){
        return Costanti.SERVER_MAIN_FOLDER + Costanti.SERVER_SCHEMATA_FOLDER;
    }
    
    public static String getGlobalPath(){
        return Costanti.SERVER_MAIN_FOLDER + Costanti.SERVER_GLOBAL_TASKS_PATH;
    }
    
    public static String getUserPrivatePath(String user){
        return Costanti.SERVER_MAIN_FOLDER + Costanti.SERVER_FILES_FOLDER + user + "/"+ Costanti.SERVER_PRIVATE_USER_FOLDER;
    }
    
    public static String getUserPublicPath(String user){
        return Costanti.SERVER_MAIN_FOLDER + Costanti.SERVER_FILES_FOLDER + user + "/"+ Costanti.SERVER_PUBLIC_USER_FOLDER;
    }
    
    public static String getUserTempPath(String user){
        return Costanti.SERVER_MAIN_FOLDER + Costanti.SERVER_FILES_FOLDER + user + "/" + Costanti.SERVER_TEMP_FOLDER + Costanti.SERVER_TEMP_TASKS_FOLDER;
    }
}
