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

package gr.aueb.mipmapgui.controller.file;

import gr.aueb.context.ApplicationContextProvider;
import gr.aueb.users.MipMapUser;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;

public class ActionInitialize {
    private JSONObject JSONObject = new JSONObject();
    private JdbcTemplate jdbcTemplate = (JdbcTemplate) ApplicationContextProvider.getApplicationContext().getBean("jdbcTemplate");
    public ActionInitialize() {}
    
    public void performAction(String user) {
        initializeUserInDB(user);
        getSavedUserFiles(user);
        getSavedSchemata();
        getGlobalFiles();
        getPublicFiles(user);
        getUserList(user);
        getPendingRequests(user);
    }
    
    private void initializeUserInDB (String user) {
        Integer id = jdbcTemplate.queryForObject("select count (*) from mipmapuser where username = '" + user + "'", Integer.class);
        if ( id == 0 ) { 
            Integer max = jdbcTemplate.queryForObject("select max(id) from mipmapuser", Integer.class);
            if (max==null)
                max = 0;
            jdbcTemplate.update("INSERT INTO mipmapuser (id, username, role , score, mappings_accepted, mappings_total) "
                    + "values (" + (++max) + ", '" + user + "','user',0.0, 0, 0) ");
//                            + "on conflict (username) "
//                            + "do nothing");
        }
    }
    
    private void getPendingRequests(String user){
        JSONArray requests = new JSONArray(); 
        jdbcTemplate.query(
            "SELECT m2.id, m2.username, m2.score "
            + "FROM mipmapuser m1, mipmapuser m2, user_user u "
            + "WHERE m1.username = ? "
            + "AND m1.id = u.\"userA\" "
            + "AND u.\"userB\" = m2.id "
            + "AND u.status = 0 "
            + "ORDER BY m2.score DESC, m2.username ASC", new Object[] { user },
            (rs, rowNum) -> new MipMapUser(rs.getInt("id"), rs.getString("username"), rs.getDouble("score"))
            ).stream().forEach((mipMapUser) -> 
            {         
                JSONObject requestsObject = new JSONObject();
                requestsObject.put("userId", mipMapUser.getId());
                requestsObject.put("userName", mipMapUser.getUsername());
                requests.add(requestsObject);
            });
        JSONObject.put("pendingRequests", requests);
    }
    
    private void getUserList(String user){
        JSONArray usersArr = new JSONArray(); 
        ArrayList<String> alreadyConnectedUsers = new ArrayList<>();
        jdbcTemplate.query(
            "SELECT m2.id, m2.username, m2.score "
            + "FROM mipmapuser m1, mipmapuser m2, user_user u "
            + "WHERE m1.username = ? "
            + "AND m1.id = u.\"userB\" "
            + "AND u.\"userA\" = m2.id "
            + "ORDER BY m2.score DESC, m2.username ASC", new Object[] { user },
            (rs, rowNum) -> new MipMapUser(rs.getInt("id"), rs.getString("username"), rs.getDouble("score"))
            ).stream().forEach((mipMapUser) -> 
            {         
                alreadyConnectedUsers.add(mipMapUser.getUsername());
            });
            alreadyConnectedUsers.add(user);
            jdbcTemplate.query(
                    "SELECT id, username from mipmapuser;",
                    (rs, rowNum) -> new MipMapUser(rs.getInt("id"), rs.getString("username"))
                ).stream().forEach((mipMapUser1) -> 
                    {                      
                        if (!alreadyConnectedUsers.contains(mipMapUser1.getUsername())){
                            JSONObject userObject = new JSONObject();
                            userObject.put("userId", mipMapUser1.getId());
                            userObject.put("userName", mipMapUser1.getUsername());
                            usersArr.add(userObject);
                        }
                    });
        
        JSONObject.put("userList", usersArr);
    }
    
    
    private void getSavedUserFiles(String user) {                
        ActionCreateUserDirectory actionCreateUserDirectory = new ActionCreateUserDirectory();
        actionCreateUserDirectory.performAction(user);
        ActionCleanDirectory actionCleanDirectory = new ActionCleanDirectory();
        actionCleanDirectory.performAction(user);

        File tasksDir = new File(MipmapDirectories.getUserPrivatePath(user));
        JSONArray taskFileArr = listMappingTaskFiles(tasksDir);                     
        JSONObject.put("savedTasks", taskFileArr);
    }
    
    private void getPublicFiles(String user) { 
        JSONArray taskFileArr = new JSONArray();
        String publicPath = MipmapDirectories.getUserPublicPath(user);
        if (Files.exists(Paths.get(publicPath))) {
            File tasksDir = new File(publicPath);
            taskFileArr = listMappingTaskFiles(tasksDir);  
        }                
        JSONObject.put("publicTasks", taskFileArr);
    }
    
    private void getGlobalFiles() { 
        JSONArray taskFileArr = new JSONArray();
        String globalPath = MipmapDirectories.getGlobalPath();
        if (Files.exists(Paths.get(globalPath))) {
            File tasksDir = new File(globalPath);
            taskFileArr = listMappingTaskFiles(tasksDir);  
        }                
        JSONObject.put("globalTasks", taskFileArr);
    }
    
    private JSONArray listMappingTaskFiles(File tasksDir) {
        JSONArray taskFileArr = new JSONArray();
        String[] taskFiles = tasksDir.list(DirectoryFileFilter.INSTANCE);
        for (String file : taskFiles) {
            if (!file.equalsIgnoreCase("temp")){
                taskFileArr.add(file);
            }
        }         
        return taskFileArr;
    }
    
    private void getSavedSchemata(){
        JSONArray schemaFileArr = new JSONArray();
        String savedSchemataPath = MipmapDirectories.getSavedSchemataPath();
        if (Files.exists(Paths.get(savedSchemataPath))) {
            File schemaDir = new File(savedSchemataPath);
            String[] schemaFiles = schemaDir.list();
            for (String file : schemaFiles) {
                file = file.substring(0, file.lastIndexOf('.'));
                schemaFileArr.add(file);
            }
        }
        JSONObject.put("savedSchemata", schemaFileArr);
    }
    
    public JSONObject getJSONObject(){
        return this.JSONObject;
    }
    
}
