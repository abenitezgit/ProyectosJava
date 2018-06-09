/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.model;

import java.util.ArrayList;
import java.util.List;

public class Mov {
    String mOVID;
    String mOVDESC;
    int mOVENABLE;
    String cLIID;
    String cLIDESC;
    String sSERVERID;
    String sHOSTNAME;
    String sDOMAIN;
    String sIP;
    String sSERVERDESC;
    String dSERVERID;
    String dHOSTNAME;
    String dDOMAIN;
    String dIP;
    String dSERVERDESC;
    String sDBID;
    String sDBNAME;
    String sDBPORT;
    String sDBTYPE;
    String sDBINSTANCE;
    String dDBID;
    String dDBNAME;
    String dDBPORT;
    String dDBTYPE;
    String dDBINSTANCE;
    String sLOGINID;
    String sLOGINNAME;
    String sLOGINPASS;
    String dLOGINID;
    String dLOGINNAME;
    String dLOGINPASS;
    String sOWNERID;
    String sOWNERNAME;
    String sOWNERPASS;
    String dOWNERID;
    String dOWNERNAME;
    String dOWNERPASS;
    String sTBNAME;
    String dTBNAME;
    int sFIELDUPDATEACTIVE;
    String sFIELDUPDATENAME;
    String sFIELDVALUETYPE;
    String sFIELDVALUEREAD;
    String sFIELDVALUEUPDATE;
    int wHEREACTIVE;
    String wHEREBODY;
    int aPPENDABLE;
    int cREATEDEST;
    int mAXROWS;
    int mAXROWSRANGE;
    int mAXROWSERROR;
    int mAXPCTERROR;
    int rOLLBACKERROR;
    int fECEXTACTIVE;
    int fECEXTEPOCH;
    String fECEXTFIELD;
    String fECEXTINI;
    String fECEXTFIN;
    int fECEXTINIIN;
    int fECEXTFININ;
    String sFIELDUPDATEKEY;
    
    List<MovMatch> lstMovMatch = new ArrayList<>();

    //Getter and Setter
    //

	public String getmOVID() {
		return mOVID;
	}

	public void setmOVID(String mOVID) {
		this.mOVID = mOVID;
	}

	public String getmOVDESC() {
		return mOVDESC;
	}

	public void setmOVDESC(String mOVDESC) {
		this.mOVDESC = mOVDESC;
	}

	public int getmOVENABLE() {
		return mOVENABLE;
	}

	public void setmOVENABLE(int mOVENABLE) {
		this.mOVENABLE = mOVENABLE;
	}

	public String getcLIID() {
		return cLIID;
	}

	public void setcLIID(String cLIID) {
		this.cLIID = cLIID;
	}

	public String getcLIDESC() {
		return cLIDESC;
	}

	public void setcLIDESC(String cLIDESC) {
		this.cLIDESC = cLIDESC;
	}

	public String getsSERVERID() {
		return sSERVERID;
	}

	public void setsSERVERID(String sSERVERID) {
		this.sSERVERID = sSERVERID;
	}

	public String getsHOSTNAME() {
		return sHOSTNAME;
	}

	public void setsHOSTNAME(String sHOSTNAME) {
		this.sHOSTNAME = sHOSTNAME;
	}

	public String getsDOMAIN() {
		return sDOMAIN;
	}

	public void setsDOMAIN(String sDOMAIN) {
		this.sDOMAIN = sDOMAIN;
	}

	public String getsIP() {
		return sIP;
	}

	public void setsIP(String sIP) {
		this.sIP = sIP;
	}

	public String getsSERVERDESC() {
		return sSERVERDESC;
	}

	public void setsSERVERDESC(String sSERVERDESC) {
		this.sSERVERDESC = sSERVERDESC;
	}

	public String getdSERVERID() {
		return dSERVERID;
	}

	public void setdSERVERID(String dSERVERID) {
		this.dSERVERID = dSERVERID;
	}

	public String getdHOSTNAME() {
		return dHOSTNAME;
	}

	public void setdHOSTNAME(String dHOSTNAME) {
		this.dHOSTNAME = dHOSTNAME;
	}

	public String getdDOMAIN() {
		return dDOMAIN;
	}

	public void setdDOMAIN(String dDOMAIN) {
		this.dDOMAIN = dDOMAIN;
	}

	public String getdIP() {
		return dIP;
	}

	public void setdIP(String dIP) {
		this.dIP = dIP;
	}

	public String getdSERVERDESC() {
		return dSERVERDESC;
	}

	public void setdSERVERDESC(String dSERVERDESC) {
		this.dSERVERDESC = dSERVERDESC;
	}

	public String getsDBID() {
		return sDBID;
	}

	public void setsDBID(String sDBID) {
		this.sDBID = sDBID;
	}

	public String getsDBNAME() {
		return sDBNAME;
	}

	public void setsDBNAME(String sDBNAME) {
		this.sDBNAME = sDBNAME;
	}

	public String getsDBPORT() {
		return sDBPORT;
	}

	public void setsDBPORT(String sDBPORT) {
		this.sDBPORT = sDBPORT;
	}

	public String getsDBTYPE() {
		return sDBTYPE;
	}

	public void setsDBTYPE(String sDBTYPE) {
		this.sDBTYPE = sDBTYPE;
	}

	public String getsDBINSTANCE() {
		return sDBINSTANCE;
	}

	public void setsDBINSTANCE(String sDBINSTANCE) {
		this.sDBINSTANCE = sDBINSTANCE;
	}

	public String getdDBID() {
		return dDBID;
	}

	public void setdDBID(String dDBID) {
		this.dDBID = dDBID;
	}

	public String getdDBNAME() {
		return dDBNAME;
	}

	public void setdDBNAME(String dDBNAME) {
		this.dDBNAME = dDBNAME;
	}

	public String getdDBPORT() {
		return dDBPORT;
	}

	public void setdDBPORT(String dDBPORT) {
		this.dDBPORT = dDBPORT;
	}

	public String getdDBTYPE() {
		return dDBTYPE;
	}

	public void setdDBTYPE(String dDBTYPE) {
		this.dDBTYPE = dDBTYPE;
	}

	public String getdDBINSTANCE() {
		return dDBINSTANCE;
	}

	public void setdDBINSTANCE(String dDBINSTANCE) {
		this.dDBINSTANCE = dDBINSTANCE;
	}

	public String getsLOGINID() {
		return sLOGINID;
	}

	public void setsLOGINID(String sLOGINID) {
		this.sLOGINID = sLOGINID;
	}

	public String getsLOGINNAME() {
		return sLOGINNAME;
	}

	public void setsLOGINNAME(String sLOGINNAME) {
		this.sLOGINNAME = sLOGINNAME;
	}

	public String getsLOGINPASS() {
		return sLOGINPASS;
	}

	public void setsLOGINPASS(String sLOGINPASS) {
		this.sLOGINPASS = sLOGINPASS;
	}

	public String getdLOGINID() {
		return dLOGINID;
	}

	public void setdLOGINID(String dLOGINID) {
		this.dLOGINID = dLOGINID;
	}

	public String getdLOGINNAME() {
		return dLOGINNAME;
	}

	public void setdLOGINNAME(String dLOGINNAME) {
		this.dLOGINNAME = dLOGINNAME;
	}

	public String getdLOGINPASS() {
		return dLOGINPASS;
	}

	public void setdLOGINPASS(String dLOGINPASS) {
		this.dLOGINPASS = dLOGINPASS;
	}

	public String getsOWNERID() {
		return sOWNERID;
	}

	public void setsOWNERID(String sOWNERID) {
		this.sOWNERID = sOWNERID;
	}

	public String getsOWNERNAME() {
		return sOWNERNAME;
	}

	public void setsOWNERNAME(String sOWNERNAME) {
		this.sOWNERNAME = sOWNERNAME;
	}

	public String getsOWNERPASS() {
		return sOWNERPASS;
	}

	public void setsOWNERPASS(String sOWNERPASS) {
		this.sOWNERPASS = sOWNERPASS;
	}

	public String getdOWNERID() {
		return dOWNERID;
	}

	public void setdOWNERID(String dOWNERID) {
		this.dOWNERID = dOWNERID;
	}

	public String getdOWNERNAME() {
		return dOWNERNAME;
	}

	public void setdOWNERNAME(String dOWNERNAME) {
		this.dOWNERNAME = dOWNERNAME;
	}

	public String getdOWNERPASS() {
		return dOWNERPASS;
	}

	public void setdOWNERPASS(String dOWNERPASS) {
		this.dOWNERPASS = dOWNERPASS;
	}

	public String getsTBNAME() {
		return sTBNAME;
	}

	public void setsTBNAME(String sTBNAME) {
		this.sTBNAME = sTBNAME;
	}

	public String getdTBNAME() {
		return dTBNAME;
	}

	public void setdTBNAME(String dTBNAME) {
		this.dTBNAME = dTBNAME;
	}

	public int getsFIELDUPDATEACTIVE() {
		return sFIELDUPDATEACTIVE;
	}

	public void setsFIELDUPDATEACTIVE(int sFIELDUPDATEACTIVE) {
		this.sFIELDUPDATEACTIVE = sFIELDUPDATEACTIVE;
	}

	public String getsFIELDUPDATENAME() {
		return sFIELDUPDATENAME;
	}

	public void setsFIELDUPDATENAME(String sFIELDUPDATENAME) {
		this.sFIELDUPDATENAME = sFIELDUPDATENAME;
	}

	public String getsFIELDVALUETYPE() {
		return sFIELDVALUETYPE;
	}

	public void setsFIELDVALUETYPE(String sFIELDVALUETYPE) {
		this.sFIELDVALUETYPE = sFIELDVALUETYPE;
	}

	public String getsFIELDVALUEREAD() {
		return sFIELDVALUEREAD;
	}

	public void setsFIELDVALUEREAD(String sFIELDVALUEREAD) {
		this.sFIELDVALUEREAD = sFIELDVALUEREAD;
	}

	public String getsFIELDVALUEUPDATE() {
		return sFIELDVALUEUPDATE;
	}

	public void setsFIELDVALUEUPDATE(String sFIELDVALUEUPDATE) {
		this.sFIELDVALUEUPDATE = sFIELDVALUEUPDATE;
	}

	public int getwHEREACTIVE() {
		return wHEREACTIVE;
	}

	public void setwHEREACTIVE(int wHEREACTIVE) {
		this.wHEREACTIVE = wHEREACTIVE;
	}

	public String getwHEREBODY() {
		return wHEREBODY;
	}

	public void setwHEREBODY(String wHEREBODY) {
		this.wHEREBODY = wHEREBODY;
	}

	public int getaPPENDABLE() {
		return aPPENDABLE;
	}

	public void setaPPENDABLE(int aPPENDABLE) {
		this.aPPENDABLE = aPPENDABLE;
	}

	public int getcREATEDEST() {
		return cREATEDEST;
	}

	public void setcREATEDEST(int cREATEDEST) {
		this.cREATEDEST = cREATEDEST;
	}

	public int getmAXROWS() {
		return mAXROWS;
	}

	public void setmAXROWS(int mAXROWS) {
		this.mAXROWS = mAXROWS;
	}

	public int getmAXROWSRANGE() {
		return mAXROWSRANGE;
	}

	public void setmAXROWSRANGE(int mAXROWSRANGE) {
		this.mAXROWSRANGE = mAXROWSRANGE;
	}

	public int getmAXROWSERROR() {
		return mAXROWSERROR;
	}

	public void setmAXROWSERROR(int mAXROWSERROR) {
		this.mAXROWSERROR = mAXROWSERROR;
	}

	public int getmAXPCTERROR() {
		return mAXPCTERROR;
	}

	public void setmAXPCTERROR(int mAXPCTERROR) {
		this.mAXPCTERROR = mAXPCTERROR;
	}

	public int getrOLLBACKERROR() {
		return rOLLBACKERROR;
	}

	public void setrOLLBACKERROR(int rOLLBACKERROR) {
		this.rOLLBACKERROR = rOLLBACKERROR;
	}

	public int getfECEXTACTIVE() {
		return fECEXTACTIVE;
	}

	public void setfECEXTACTIVE(int fECEXTACTIVE) {
		this.fECEXTACTIVE = fECEXTACTIVE;
	}

	public int getfECEXTEPOCH() {
		return fECEXTEPOCH;
	}

	public void setfECEXTEPOCH(int fECEXTEPOCH) {
		this.fECEXTEPOCH = fECEXTEPOCH;
	}

	public String getfECEXTFIELD() {
		return fECEXTFIELD;
	}

	public void setfECEXTFIELD(String fECEXTFIELD) {
		this.fECEXTFIELD = fECEXTFIELD;
	}

	public String getfECEXTINI() {
		return fECEXTINI;
	}

	public void setfECEXTINI(String fECEXTINI) {
		this.fECEXTINI = fECEXTINI;
	}

	public String getfECEXTFIN() {
		return fECEXTFIN;
	}

	public void setfECEXTFIN(String fECEXTFIN) {
		this.fECEXTFIN = fECEXTFIN;
	}

	public int getfECEXTINIIN() {
		return fECEXTINIIN;
	}

	public void setfECEXTINIIN(int fECEXTINIIN) {
		this.fECEXTINIIN = fECEXTINIIN;
	}

	public int getfECEXTFININ() {
		return fECEXTFININ;
	}

	public void setfECEXTFININ(int fECEXTFININ) {
		this.fECEXTFININ = fECEXTFININ;
	}

	public String getsFIELDUPDATEKEY() {
		return sFIELDUPDATEKEY;
	}

	public void setsFIELDUPDATEKEY(String sFIELDUPDATEKEY) {
		this.sFIELDUPDATEKEY = sFIELDUPDATEKEY;
	}

	public List<MovMatch> getLstMovMatch() {
		return lstMovMatch;
	}

	public void setLstMovMatch(List<MovMatch> lstMovMatch) {
		this.lstMovMatch = lstMovMatch;
	}
    
}
