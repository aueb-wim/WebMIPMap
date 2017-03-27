package gr.aueb.users;

import gr.aueb.context.ApplicationContextProvider;
import gr.aueb.mipmapgui.controller.file.MipmapDirectories;
import java.io.File;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;


public class ActionGetUsers {
     
    private JSONObject JSONObject = new JSONObject();
    private JdbcTemplate jdbcTemplate = (JdbcTemplate) ApplicationContextProvider.getApplicationContext().getBean("jdbcTemplate");
    
    public ActionGetUsers() {}
    
    public void performAction(String user) {
        JSONArray usersArr = new JSONArray();
        jdbcTemplate.query(
            "SELECT m2.id, m2.username, m2.score "
            + "FROM mipmapuser m1, mipmapuser m2, user_user u "
            + "WHERE m1.username = ? "
            + "AND m1.id = u.\"userB\" "
            + "AND u.\"userA\" = m2.id "
            + "AND u.status = 1"
            + "ORDER BY m2.score DESC, m2.username ASC", new Object[] { user },
            (rs, rowNum) -> new MipMapUser(rs.getInt("id"), rs.getString("username"), rs.getDouble("score"))
        ).stream().forEach((mipMapUser) -> 
            {                
                JSONObject userObject = new JSONObject();
                userObject.put("userId", mipMapUser.getId());
                userObject.put("userName", mipMapUser.getUsername());
                //keep two decimals
                userObject.put("userScore", String.format("%.2f", mipMapUser.getScore() ));  
                userObject.put("publicTasks", getPublicFiles(mipMapUser.getUsername()));  
                usersArr.add(userObject);
            });
        JSONObject.put("trustUsers", usersArr);
    }
    
    private JSONArray getPublicFiles(String user) {        
        File tasksDir = new File(MipmapDirectories.getUserPublicPath(user));
        JSONArray taskFileArr = new JSONArray();
        if (tasksDir.exists()) {
            taskFileArr = listMappingTaskFiles(tasksDir);                  
        }
        return taskFileArr;
    }
    
    private JSONArray listMappingTaskFiles(File tasksDir) {
        JSONArray taskFileArr = new JSONArray();
        String[] taskFiles = tasksDir.list(DirectoryFileFilter.INSTANCE);
        for (String file : taskFiles) {
            if (!file.equalsIgnoreCase("temp")){
                JSONObject taskObject = new JSONObject();
                taskObject.put("taskName", file);
                taskFileArr.add(taskObject);
            }
        }         
        return taskFileArr;
    }
    
    public JSONObject getJSONObject(){
        return this.JSONObject;
    }
}
