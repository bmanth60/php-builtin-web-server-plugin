package org.jenkinsci.plugins.phpwebserver.beans;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Map;

import org.jenkinsci.plugins.phpwebserver.config.PhpWebserverInstallation;

/**
 * Run/stop a PHP built-in web server.
 *
 * @author Fengtan https://github.com/fengtan/
 *
 */
public class PhpWebserver {

	private Process process;

	/**
	 * Start web server.
	 */
	public PhpWebserver(int port, String host, File root, Boolean importEnvironment) throws IOException {
		if (!portAvailable(port)) {
			throw new IllegalStateException("Port "+port+" is already used");
		}
		String php = PhpWebserverInstallation.getDefaultInstallation().getPhpExe();
		ProcessBuilder pb = new ProcessBuilder(php, "--server", host+":"+port, "--docroot", root.getAbsolutePath());
        if(importEnvironment){
            Map<String, String> systemEnv = System.getenv();
            Map<String, String> phpEnv = pb.environment();
            phpEnv.putAll(systemEnv);
        }
		process = pb.start();
	}

	/**
	 * Stop web server.
	 */
	public void stop() {
		if (process != null) {
			process.destroy();
		}
	}

	/**
	 * Return false if 'port' is already used, true otherwise.
	 */
	private static boolean portAvailable(int port) {
	    ServerSocket socket = null;
	    try {
	        socket = new ServerSocket(port);
	        socket.setReuseAddress(true);
	        return true;
	    } catch (IOException e) {
	    	// Do not handle exception.
	    } finally {
	        if (socket != null) {
	            try {
	                socket.close();
	            } catch (IOException e) {
	            	// Do not handle exception.
	            }
	        }
	    }
	    return false;
	}

}
