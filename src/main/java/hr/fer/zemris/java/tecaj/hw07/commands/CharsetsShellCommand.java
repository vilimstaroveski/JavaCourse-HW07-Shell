package hr.fer.zemris.java.tecaj.hw07.commands;

import hr.fer.zemris.java.tecaj.hw07.environment.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class that represents command that prints all available charsets to the output
 * @author Vilim Staroveški
 *
 */
public class CharsetsShellCommand implements ShellCommand {

	/**
	 * Command name
	 */
	private String name;
	
	/**
	 * Description
	 */
	private List<String> description;
	
	/**
	 * Creates new {@link CharsetsShellCommand}
	 */
	public CharsetsShellCommand() {
		
		name = "charsets";
		description = new ArrayList<String>();
		description.add("Command prints all available charsets to the output");
		description.add("It takes no arguments and lists names of supported charsets in "
				+ "each in new line.");
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		
		if(arguments != null) {
			
			env.errWriteLn("Charsets command takes no arguments!");
			return ShellStatus.CONTINUE;
		}
		
		Map<String, Charset> charsets = Charset.availableCharsets();
		
		try {
			
			env.writeln("Available charsets are:");
			Set<String> keySet = charsets.keySet();
			for(String key : keySet) {
				
				env.writeln(charsets.get(key).name());
			}
			
		} catch (IOException e) {
			System.err.println("There was an IOException in writing to output!");
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
