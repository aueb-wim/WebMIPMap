package gr.aueb.users;

public class MipMapUser {
    private int id;
    private String username;
    private String role;
    private double score;
    private int mappingsAccepted;
    private int mappingsTotal;
    
    public MipMapUser(int id, String username){
        this.id = id;
        this.username = username;
        this.score = 0.0;
    }
    
    public MipMapUser(int id, String username, double score, int mappingsAccepted, int mappingsTotal){
        this.id = id;
        this.username = username;
        this.role = role;
        this.score = score;
        this.mappingsAccepted = mappingsAccepted;
        this.mappingsTotal = mappingsTotal;
    }
    
    public MipMapUser(int id, String username, double score){
        this.id = id;
        this.username = username;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getMappingsAccepted() {
        return mappingsAccepted;
    }

    public void setMappingsAccepted(int mappingsAccepted) {
        this.mappingsAccepted = mappingsAccepted;
    }

    public int getMappingsTotal() {
        return mappingsTotal;
    }

    public void setMappingsTotal(int mappingsTotal) {
        this.mappingsTotal = mappingsTotal;
    }
    
    
}
