package com.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.rutinas.Rutinas;

public class ConvertToMP3 {
	Rutinas mylib = new Rutinas();
	private final String className = "ConvertToMP3";
	
	private final String UTILEXEC = "ffmpeg";
	private String workFolder;
	private String fileSource;
	private String fileOutput;
	private List<String> commands = new ArrayList<>();
	private StringBuilder sbInput;
	private StringBuilder sbError;

	public ConvertToMP3() {
		
	}

	public ConvertToMP3(String workFolder, String fileSource, String fileOutput) {
		this.workFolder = workFolder;
		this.fileSource = fileSource;
		this.fileOutput = fileOutput;
	}
	
	//Getter and Setter
	public String getWorkFolder() {
		return workFolder;
	}

	public void setWorkFolder(String workFolder) {
		this.workFolder = workFolder;
	}

	public String getFileSource() {
		return fileSource;
	}

	public void setFileSource(String fileSource) {
		this.fileSource = fileSource;
	}

	public String getFileOutput() {
		return fileOutput;
	}

	public void setFileOutput(String fileOutput) {
		this.fileOutput = fileOutput;
	}

	//Private Methods
	private void validaParams() throws Exception {
		try {
			File f;
			/**
			 * Valida Existencia del Folder
			 */
			f = new File(workFolder);
			if (!f.exists()) {
				throw new Exception("Directorio de trabajo ("+workFolder+") no se encuentra");
			} 
			/**
			 * Valida Existencia del Audio Origen
			 */
			f = new File(workFolder+"/"+fileSource);
			if (!f.exists()) {
				throw new Exception("Audio Origen ("+fileSource+") no se encuentra");
			}

			/**
			 * Valida Existencia del conversor util
			 */
			f = new File(workFolder+"/"+UTILEXEC);
			if (!f.exists()) {
				throw new Exception("Utilitario: "+UTILEXEC+" no se encuentra");
			}
			
			//Genera los parametros de conversion
			commands.add("./"+UTILEXEC);
			commands.add("-loglevel");
			commands.add("fatal");
			commands.add("-y");
			commands.add("-i");
			commands.add(fileSource);
			commands.add("-ac");
			commands.add("1");
			commands.add("-ar");
			commands.add("22050");
			commands.add(fileOutput);
			
		} catch (Exception e) {
			throw new Exception("Error valiadParams: "+e.getMessage());
		}
	}

	//Public Methods
	public StringBuilder getStdInput() throws Exception {
		return sbInput; 
	}
	
	public StringBuilder getStdError() throws Exception {
		return sbError;	
	}
	
	public void convert() throws Exception {
		try {
			validaParams();
			
			ProcessBuilder pb = new ProcessBuilder(commands);
	
		    //Map<String, String> env = pb.environment();
		    // If you want clean environment, call env.clear() first
		    // env.clear()
		    //env.put("VAR1", "myValue");
		    //env.remove("OTHERVAR");
		    //env.put("VAR2", env.get("VAR1") + "suffix");
			
			mylib.console(className, "Creando ProcessBuilder");
		    File workingFolder = new File(workFolder);
		    pb.directory(workingFolder);
	
		    mylib.console(className,"Instanciando Process...");
		    Process proc = pb.start();
		    int exitVal = proc.waitFor();
		    
		    mylib.console("exitVal: "+exitVal);
	
		    BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		    
			StringBuilder sb1 = new StringBuilder();
		    String s1 = null;
		    while ((s1 = stdInput.readLine()) != null)
		    {
		        sb1.append(s1+"\n");
		    }
		    sbInput = sb1;
	
		    BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
		    
			StringBuilder sb2 = new StringBuilder();
		    String s2 = null;
		    while ((s2 = stdError.readLine()) != null)
		    {
		        sb2.append(s2+"\n");
		    }
		    sbError = sb2;
	
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error ConvertToMP3: "+e.getMessage());
		}
	}

}
