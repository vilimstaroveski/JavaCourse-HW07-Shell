package hr.fer.zemris.java.tecaj.hw07.commands;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw07.environment.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

/**
 * Class represents command that shuts down program.
 * @author Vilim Staroveški
 *
 */
public class ExitShellCommand implements ShellCommand {
	
	/** 
	 * command name
	 */
	private String name;
	
	/**
	 * Description
	 */
	private List<String> description;
	
	/**
	 * Creates new {@link ExitShellCommand}
	 */
	public ExitShellCommand() {
		
		name = "exit";
		description = new ArrayList<String>();
		description.add("Shut down the program.");
		description.add("Takes no arguments.");
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		
		if(arguments != null) {
			
			env.errWriteLn("Exit command takes no arguments!");
			return ShellStatus.CONTINUE;
		}
		
		return ShellStatus.TERMINATE;
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
