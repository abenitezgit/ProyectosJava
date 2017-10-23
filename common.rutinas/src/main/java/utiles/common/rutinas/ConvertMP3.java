package utiles.common.rutinas;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ConvertMP3 {
	String UTILEXEC = "ffmpeg";
	String folder;
	String source;
	String output;
	List<String> commands = new ArrayList<>();
	StringBuilder sbInput;
	StringBuilder sbError;

	public void setConfig(String folder, String source, String output) {
		this.folder = folder;
		this.source = source;
		this.output = output;
	}
	
	public boolean isValidaParams() {
		try {
			boolean isVal = true;
			
			/**
			 * Valida Existencia del Folder
			 */
			try {
				File f = new File(folder);
				if (!f.exists()) {
					isVal = false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				isVal = false;
			}
			
			/**
			 * Valida Existencia del Audio a convertir
			 */
			try {
				File f = new File(folder+"/"+source);
				if (!f.exists()) {
					isVal = false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				isVal = false;
			}

			/**
			 * Valida Existencia del conversor util
			 */
			try {
				File f = new File(folder+"/"+UTILEXEC);
				if (!f.exists()) {
					isVal = false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				isVal = false;
			}
			
			if (isVal) {
				commands.add("./"+UTILEXEC);
				commands.add("-loglevel");
				commands.add("fatal");
				commands.add("-y");
				commands.add("-i");
				commands.add(source);
				commands.add("-ac");
				commands.add("1");
				commands.add("-ar");
				commands.add("22050");
				commands.add(output);
			}
			
			return isVal;
		} catch (Exception e) {
			return false;
		}
	}

	public StringBuilder getStdInput() throws Exception {
		return sbInput; 
	}
	
	public StringBuilder getStdError() throws Exception {
		return sbError;	
	}
	
	public int convert() throws Exception {
		try {
			
			ProcessBuilder pb = new ProcessBuilder(commands);
	
		    //Map<String, String> env = pb.environment();
		    // If you want clean environment, call env.clear() first
		    // env.clear()
		    //env.put("VAR1", "myValue");
		    //env.remove("OTHERVAR");
		    //env.put("VAR2", env.get("VAR1") + "suffix");
			
			System.out.println("Creando ProcessBuilder");
		    File workingFolder = new File(folder);
		    pb.directory(workingFolder);
	
		    System.out.println("Instanciando Process...");
		    Process proc = pb.start();
		    int exitVal = proc.waitFor();
		    
		    System.out.println("exitVal: "+exitVal);
	
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
	
			return proc.exitValue();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error convert:"+e.getMessage() );
			return 99;
		}
	}

}
