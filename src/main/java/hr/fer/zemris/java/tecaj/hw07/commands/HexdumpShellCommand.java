package hr.fer.zemris.java.tecaj.hw07.commands;

import hr.fer.zemris.java.tecaj.hw07.environment.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Class represents command that produces hex-output to the output from given file.
 * @author Vili
 *
 */
public class HexdumpShellCommand implements ShellCommand {
	/**
	 * Hex digits.
	 */
	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
	
	/**
	 * command name
	 */
	private String name;
	
	/**
	 * Description
	 */
	private List<String> description;

	/**
	 * Creates new {@link HexdumpShellCommand}
	 */
	public HexdumpShellCommand() {
		
		name = "hexdump";
		description = new ArrayList<String>();
		description.add("Command expects a single argument: file name, and produces hex-output to the output.");
		description.add("Example of usage: \'hexdump \"C:\\test.txt\"\' ");
	}
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		
		try {
			
			arguments = CommandsUtility.giveMeOneArgument(arguments, CommandType.HEXDUMP);
			
		} catch(IllegalArgumentException e) {
			env.errWriteLn(e.getMessage());
			return ShellStatus.CONTINUE;
		}
		if(arguments == null) {
			env.errWriteLn("Hexdump command expects one argument!");
			return ShellStatus.CONTINUE;
		}
		
		Path pathToSource = CommandsUtility.parsePath(env, arguments);
		if(pathToSource == null ) {
			env.errWriteLn("Source must be existing file!");
			return ShellStatus.CONTINUE;
		}
		if(Files.isDirectory(pathToSource)) {
			env.errWriteLn("Source must be a file!");
			return ShellStatus.CONTINUE;
		}
		if(!Files.isReadable(pathToSource)) {
			env.errWriteLn("Source file is not readable!");
			return ShellStatus.CONTINUE;
		}
		
		try (BufferedReader br = new BufferedReader( 
					new InputStreamReader( 
							new BufferedInputStream( 
									new FileInputStream(
											pathToSource.toString())),"UTF-8"))) {
			
			char[] buffer = new char[16];
			int r = 0;
			int i = 0;
			while(true) {
				
				try {
					r = br.read(buffer);
				} catch (IOException e) {
					env.errWriteLn("There was an error in reading the file!");
				}
				if(r < 1) {
					break;
				}
				env.write(String.format("%08d", i)+": ");
				
				byte[] bytes = new byte[16];
				int j = 0;
				for(char c : buffer) {
					bytes[j++] = (byte) c; 
				}
				String hexes = bytetohex(bytes);
				
				int position  = 1;
				int positionOfHex = 0;
				for(char c : buffer) {
					env.write(String.valueOf(hexes.charAt(positionOfHex++)));
					env.write(String.valueOf(hexes.charAt(positionOfHex++)));
					if(position == 8) {
						env.write("|");
					}
					else if(position == 16) {
						env.write(" | ");
					}
					else {
						env.write(" ");
					}
					position++;
				}
				
				for(char c : buffer) {
					if(c <= 32 || c >= 127) {
						env.write(".");
					}
					else {
						env.write(String.valueOf(c));
					}
				}
				
				env.writeln("");
				i += 10;
			}
			
		} catch (IOException e) {
			env.errWriteLn("There was an error in reading source file!");
			return ShellStatus.CONTINUE;
		}
		
		
		return ShellStatus.CONTINUE;
	}
	
	public static String bytetohex(byte[] bytes) {
		
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	    	
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}	

	@Override
	public String getCommandName() {
		return name;
	}

	@Override
	public List<String> getCommandDescription() {
		return description;
	}

}
