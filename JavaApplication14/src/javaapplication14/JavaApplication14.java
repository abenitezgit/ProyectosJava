/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication14;

/**
 *
 * @author andresbenitez
 */
public class JavaApplication14 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    
    
    ServiceModel srv = new ServiceModel();
    IServiceModel ad = srv.getBasicHttpBindingIServiceModel();
    
    nodeResponse node = new nodeResponse();
    
    //node.errCode = ad.getTreeInfo("21").errCode;
    }
    
    static class nodeResponse {
        private String errCode;
        private String errDesc;
        private String authenticated;
        private String treeID;
        private String startingNodeID;

        public String getErrCode() {
            return errCode;
        }

        public void setErrCode(String errCode) {
            this.errCode = errCode;
        }

        public String getErrDesc() {
            return errDesc;
        }

        public void setErrDesc(String errDesc) {
            this.errDesc = errDesc;
        }

        public String getAuthenticated() {
            return authenticated;
        }

        public void setAuthenticated(String authenticated) {
            this.authenticated = authenticated;
        }

        public String getTreeID() {
            return treeID;
        }

        public void setTreeID(String treeID) {
            this.treeID = treeID;
        }

        public String getStartingNodeID() {
            return startingNodeID;
        }

        public void setStartingNodeID(String startingNodeID) {
            this.startingNodeID = startingNodeID;
        }

        
        
    }
}
