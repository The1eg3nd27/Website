package com.spectre.payload.tools;

public class PlayerInfoDTO {
    private String handle;
    private String sid;
    private String organization;
    private String rank;
    private String location;
    private String language;
    private String joinDate;
    private String memberCount;

    public PlayerInfoDTO() {}

    public PlayerInfoDTO(String handle, String sid, String organization, String rank,
                         String location, String language, String joinDate, String memberCount) {
        this.handle = handle;
        this.sid = sid;
        this.organization = organization;
        this.rank = rank;
        this.location = location;
        this.language = language;
        this.joinDate = joinDate;
        this.memberCount = memberCount;
    }


    public String getHandle() {
        return handle;
    }
    
    public void setHandle(String handle) {
        this.handle = handle;
    }
    
    public String getSid() {
        return sid;
    }
    
    public void setSid(String sid) {
        this.sid = sid;
    }
    
    public String getOrganization() {
        return organization;
    }
    
    public void setOrganization(String organization) {
        this.organization = organization;
    }
    
    public String getRank() {
        return rank;
    }
    
    public void setRank(String rank) {
        this.rank = rank;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getLanguage() {
        return language;
    }
    
    public void setLanguage(String language) {
        this.language = language;
    }
    
    public String getJoinDate() {
        return joinDate;
    }
    
    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }
    
    public String getMemberCount() {
        return memberCount;
    }
    
    public void setMemberCount(String memberCount) {
        this.memberCount = memberCount;
    }
    
}
