package hr.fer.zemris.java.tecaj.hw07.commands;

import hr.fer.zemris.java.tecaj.hw07.environment.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.nio.charset.Charset;

/**
 * Class that prints file to output in given charset.
 * @author Vilim Staroveški
 *
 */
public class CatShellCommand implements ShellCommand {

	/**
	 * Name of command
	 */
	private String name;
	
	/**
	 * Description
	 */
	private List<String> description;
	
	/**
	 * Creates new {@link CatShellCommand}
	 */
	public CatShellCommand() {

		name = "cat";
		description = new ArrayList<String>();
		description.add("Command that prints text file to output. ");
		description.add("It takes one mandatory argument(path to the text file) and one"
				+ " optional(charset which we want to apply).");
		description.add("Example of usage: \'cat \"C:\\test.txt\"  UTF-8\'");
	}
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		
		try {
			
			arguments = CommandsUtility.giveMeTwoArgument(arguments, CommandType.CAT);
			
		} catch(IllegalArgumentException e) {
			env.errWriteLn(e.getMessage());
			return ShellStatus.CONTINUE;
		}
		
		String[] args = arguments.split(" & ");
		Path pathToFile = CommandsUtility.parsePath(env, args[0]);
		if(pathToFile == null ) {
			return ShellStatus.CONTINUE;
		}
		
		if(Files.isDirectory(pathToFile)) {
			env.errWriteLn("Given path is not a file!");
			return ShellStatus.CONTINUE;
		}
		
		Charset c = null;
		
		if(args.length == 2) {
			try {
				
				c = Charset.forName(args[1]);
				
			} catch(IllegalArgumentException e) {
				env.errWriteLn("Unknown charset: "+e.getMessage());
				return ShellStatus.CONTINUE;
			}
		}
		else {
			c = Charset.defaultCharset();
		}
		
		try(BufferedReader br = new BufferedReader( new InputStreamReader( new BufferedInputStream( new FileInputStream(pathToFile.toString())),c))) {
			
			while(true) {
				String line = br.readLine();
				if(line == null) {
					break;
				}
				env.writeln(line);
			}
		} catch (UnsupportedEncodingException e) {
			env.errWriteLn("Unsuported encoding!"+ e.getMessage());
			return ShellStatus.CONTINUE;
		} catch (FileNotFoundException e) {
			env.errWriteLn("File not find: "+e.getMessage());
			return ShellStatus.CONTINUE;
		} catch (IOException e) {
			env.errWriteLn("There was an IOException!");
			return ShellStatus.CONTINUE;
		}
		return ShellStatus.CONTINUE;
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
