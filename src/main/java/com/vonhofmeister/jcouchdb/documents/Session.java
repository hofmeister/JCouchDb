package com.vonhofmeister.jcouchdb.documents;

/**
 *
 * @author Henrik Hofmeister (@vonhofdk)
 */
public class Session {

    
    private SessionInfo info = new SessionInfo();
    private boolean ok = true;
    private UserContext userCtx = new UserContext();

    public SessionInfo getInfo() {
        return info;
    }

    public void setInfo(SessionInfo info) {
        this.info = info;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public UserContext getUserCtx() {
        return userCtx;
    }

    public void setUserCtx(UserContext userCtx) {
        this.userCtx = userCtx;
    }
    
    
    
    public static class SessionInfo {
        private String authenticated = "default";
        private String authentication_db = "_users";
        private String[] authentication_handlers = {"oauth","cookie","default"};

        public String getAuthenticated() {
            return authenticated;
        }

        public void setAuthenticated(String authenticated) {
            this.authenticated = authenticated;
        }

        public String getAuthentication_db() {
            return authentication_db;
        }

        public void setAuthentication_db(String authentication_db) {
            this.authentication_db = authentication_db;
        }

        public String[] getAuthentication_handlers() {
            return authentication_handlers;
        }

        public void setAuthentication_handlers(String[] authentication_handlers) {
            this.authentication_handlers = authentication_handlers;
        }
        
        
    }

    public static class UserContext {
        private String user;
        private String[] roles = {"_admin"};

        public String[] getRoles() {
            return roles;
        }

        public void setRoles(String[] roles) {
            this.roles = roles;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }
        
    }
    
}
