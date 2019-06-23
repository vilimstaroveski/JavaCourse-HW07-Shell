package hr.fer.zemris.java.tecaj.hw07.commands;

import hr.fer.zemris.java.tecaj.hw07.environment.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Command that Command takes a single argument: directory name, and creates the appropriate directory structure.
 * @author Vilim Staroveški
 *
 */
public class MkdirShellCommand implements ShellCommand {
	
	/**
	 * Command name
	 */
	private String name;
	
	/**
	 * Description.
	 */
	private List<String> description;
	
	/**
	 * Creates new {@link MkdirShellCommand}
	 */
	public MkdirShellCommand() {
		
		name = "mkdir";
		description = new ArrayList<String>();
		description.add("Command takes a single argument: directory name, and creates the appropriate directory"+
						"structure.");
		description.add("Example of usage: \'ls \"C:\\test\\copy\\new folder\"\'");
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {

		try {
			
			arguments = CommandsUtility.giveMeOneArgument(arguments, CommandType.MKDIR);
			
		} catch(IllegalArgumentException e) {
			env.errWriteLn(e.getMessage());
			return ShellStatus.CONTINUE;
		}
		if(arguments == null) {
			env.errWriteLn("Mkdir command expects one argument!");
			return ShellStatus.CONTINUE;
		}
		if(arguments.contains(".")) {
			env.errWriteLn("Mkdir command expects a path that resolves into directory!");
			return ShellStatus.CONTINUE;
		}
		
		Path pathToDir = CommandsUtility.parseNullablePath(env, arguments);
		if(pathToDir == null ) {
			return ShellStatus.CONTINUE;
		}
		
		try {
			Files.createDirectories(pathToDir);
		} catch (IOException e) {
			env.errWriteLn("There was an IOException in creating directory structure!");
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
