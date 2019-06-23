package hr.fer.zemris.java.tecaj.hw07.commands;

import hr.fer.zemris.java.tecaj.hw07.environment.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;
import hr.fer.zemris.java.tecaj.hw07.shell.Symbols;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Command takes one or two arguments. User can provide name of symbol 
 * that is used in this shell and the program prints the symbol to output.
 * @author Vilim Staroveški
 *
 */
public class SymbolShellCommand implements ShellCommand {

	/**
	 * Name
	 */
	private String name;
	
	/**
	 * Description
	 */
	private List<String> description;
	
	/**
	 * Creates new {@link SymbolShellCommand}
	 */
	public SymbolShellCommand() {
		name = "symbol";
		description = new ArrayList<String>();
		description.add("Command takes one or two arguments. User can provide name of symbol "
				+ "that is used in this shell and the program prints the symbol to output.");
		description.add("Example of usage \'symbol PROMPT\'");
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {

		try {
			
			arguments = CommandsUtility.giveMeTwoArgument(arguments, CommandType.SYMBOL);
			
		} catch(IllegalArgumentException e) {
			env.errWriteLn(e.getMessage());
			return ShellStatus.CONTINUE;
		}
		
		String[] splitedArgs = arguments.split(" ");
		
		Symbols wantedSymbol = null;
		if(splitedArgs[0].equalsIgnoreCase("PROMPT")) {
			wantedSymbol = Symbols.PROMPT;
		}
		else if(splitedArgs[0].equalsIgnoreCase("MULTILINE")) {
			wantedSymbol = Symbols.MULTILINE;
		}
		else if(splitedArgs[0].equalsIgnoreCase("MORELINES")) {
			wantedSymbol = Symbols.MORELINES;
		}
		else {
			env.errWriteLn("Unknown symbol name: \""+splitedArgs[0]+"\"");
			return ShellStatus.CONTINUE;
		}
		
		if(splitedArgs.length == 2) {
			
			if(splitedArgs[1].length() > 1) {
				env.errWriteLn("New symbol must be a character!");
				return ShellStatus.CONTINUE;
			}
			
			String oldSymbol = getSymbol(env, wantedSymbol);
			setNewSymbol(env, wantedSymbol, splitedArgs[1]);
			
			try {
				env.writeln("Symbol for "+splitedArgs[0]+" changed from \'"+oldSymbol+"\' to \'"+splitedArgs[1]+"\'");
			} catch (IOException e) {
			}
		} else {
		
			try {
				env.writeln("Symbol for "+splitedArgs[0]+" is: \'"+getSymbol(env, wantedSymbol)+"\'");
			} catch (IOException e) {
			}
		}
		
		
		return ShellStatus.CONTINUE;
		
		
	}

	/**
	 * Sets new symbol for given symbol type
	 * @param env enviornment in which we work
	 * @param wantedSymbol wanted symbol type
	 * @param newSymbol wanted symbol
	 */
	private void setNewSymbol(Environment env, Symbols wantedSymbol, String newSymbol) {
		
		switch(wantedSymbol) {
		case MULTILINE: env.setMultilineSymbol(newSymbol.charAt(0));
						break;
		case MORELINES: env.setMorelinesSymbol(newSymbol.charAt(0));
						break;
		case PROMPT: 	env.setPromptSymbol(newSymbol.charAt(0));
						break;
		}
	}

	/**
	 * Gets symbol for given symbol type
	 * @param env enviornment in which we work
	 * @param wantedSymbol wanted symbol type
	 */
	private String getSymbol(Environment env, Symbols wantedSymbol) {
		
		switch(wantedSymbol) {
		case MULTILINE: return env.getMultilineSymbol().toString();
		
		case MORELINES: return env.getMorelinesSymbol().toString();
		
		case PROMPT: return env.getPromptSymbol().toString();
		
		}
		return null;
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
