package hr.fer.zemris.java.tecaj.hw07.shell;

import hr.fer.zemris.java.tecaj.hw07.commands.CatShellCommand;
import hr.fer.zemris.java.tecaj.hw07.commands.CharsetsShellCommand;
import hr.fer.zemris.java.tecaj.hw07.commands.CopyShellCommand;
import hr.fer.zemris.java.tecaj.hw07.commands.ExitShellCommand;
import hr.fer.zemris.java.tecaj.hw07.commands.HelpShellCommand;
import hr.fer.zemris.java.tecaj.hw07.commands.HexdumpShellCommand;
import hr.fer.zemris.java.tecaj.hw07.commands.LsShellCommand;
import hr.fer.zemris.java.tecaj.hw07.commands.MkdirShellCommand;
import hr.fer.zemris.java.tecaj.hw07.commands.ShellCommand;
import hr.fer.zemris.java.tecaj.hw07.commands.SymbolShellCommand;
import hr.fer.zemris.java.tecaj.hw07.commands.TreeShellCommand;
import hr.fer.zemris.java.tecaj.hw07.environment.Environment;
import hr.fer.zemris.java.tecaj.hw07.environment.MyEnvironment;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Program is a command line program, which takes users inputs and make 
 * them into executable commands.
 * User is allowed to give input into multiple lines. Example:
 * 
 * >copy \
 * | "C:\test.txt" \
 * | "C:\new folder"
 * 
 * in which '>' is a default prompt symbol, '\' is a default morelines symbol
 * and '|' is a default multiline symbol.
 * Supported commands are:
 * exit = {@link ExitShellCommand}, 
 * ls = {@link LsShellCommand},
 * help = {@link HelpShellCommand},
 * symbol = {@link SymbolShellCommand},
 * cat = {@link CatShellCommand},
 * charsets = {@link CharsetsShellCommand},
 * tree = {@link TreeShellCommand},
 * copy = {@link CopyShellCommand},
 * mkdir = {@link MkdirShellCommand},
 * hexdump = {@link HexdumpShellCommand}.
 * 
 * See links for usage informations.
 * 
 * @author Vilim Staroveški
 *
 */
public class MyShell {

	/**
	 * Mapping of supported commands.
	 */
	private static Map<String, ShellCommand> commands;	
	
	/**
	 * Static initialisation of supported commands. 
	 */
	static {
		commands = new HashMap<String, ShellCommand>();
		commands.put("exit", new ExitShellCommand());
		commands.put("ls", new LsShellCommand());
		commands.put("help", new HelpShellCommand());
		commands.put("symbol", new SymbolShellCommand());
		commands.put("cat", new CatShellCommand());
		commands.put("charsets", new CharsetsShellCommand());
		commands.put("tree", new TreeShellCommand());
		commands.put("copy", new CopyShellCommand());
		commands.put("mkdir", new MkdirShellCommand());
		commands.put("hexdump", new HexdumpShellCommand());
	}
	
	/**
	 * Method called at the start of program. 
	 * @param args - not neccessary, so ignorable.
	 */
	public static void main(String[] args) {

		System.out.println("Welcome to MyShell v 1.0!");
		Environment environment = new MyEnvironment();
		ShellStatus status = null;
		String usersInput = null;
		String commandName = null;
		String arguments = null;
		do {

			try {
				usersInput = environment.readLine();
			} catch (IOException e) {
				environment.errWriteLn("There was an error in reading the users input!");
			}

			if(usersInput.contains(" ")) {
				commandName = usersInput.substring(0, usersInput.indexOf(" "));
				arguments = usersInput.substring(usersInput.indexOf(" ")+1);
			}
			else {
				commandName = usersInput;
				arguments = null;
			}
			
			ShellCommand command = commands.get(commandName);
			if(command  == null) {
				environment.errWriteLn("Unknown command \""+commandName+"\"");
				status = ShellStatus.CONTINUE;
			}
			else {
				status = command.executeCommand(environment, arguments);
			}
			
		} while(status != ShellStatus.TERMINATE);
		
		
	}

}
