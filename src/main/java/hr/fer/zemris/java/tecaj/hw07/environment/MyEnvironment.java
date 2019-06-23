package hr.fer.zemris.java.tecaj.hw07.environment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import hr.fer.zemris.java.tecaj.hw07.commands.CatShellCommand;
import hr.fer.zemris.java.tecaj.hw07.commands.CopyShellCommand;
import hr.fer.zemris.java.tecaj.hw07.commands.ExitShellCommand;
import hr.fer.zemris.java.tecaj.hw07.commands.HelpShellCommand;
import hr.fer.zemris.java.tecaj.hw07.commands.HexdumpShellCommand;
import hr.fer.zemris.java.tecaj.hw07.commands.LsShellCommand;
import hr.fer.zemris.java.tecaj.hw07.commands.MkdirShellCommand;
import hr.fer.zemris.java.tecaj.hw07.commands.ShellCommand;
import hr.fer.zemris.java.tecaj.hw07.commands.SymbolShellCommand;
import hr.fer.zemris.java.tecaj.hw07.commands.CharsetsShellCommand;
import hr.fer.zemris.java.tecaj.hw07.commands.TreeShellCommand;

/**
 * Class that implements {@link Environment} interface. It represents an environment
 * for command line program.
 * 
 * @author Vilim Staroveški
 *
 */
public class MyEnvironment implements Environment {

	/**
	 * Symbol that represents new input.
	 */
	private String PROMPT;
	
	/**
	 * Symbol that tell it is going to be a new line in this input.
	 */
	private String MORELINES;

	/**
	 * Symbol that represents new line in the same input.
	 */
	private String MULTILINE;
	
	/**
	 * Mapping of supported commands.
	 */
	private Map<String, ShellCommand> commands;
	
	/**
	 * Default output is to console.
	 */
	private OutputStreamWriter osw = new OutputStreamWriter(System.out);
	
	/**
	 * Default input is from console.
	 */
	private BufferedReader bis = new BufferedReader(new InputStreamReader(System.in));
	
	/**
	 * Creates new {@link Environment} with '>' as default prompt symbol, '\' as default morelines symbol
	 * and '|' as default multiline symbol and creates new {@link Map} with supported commands.
	 */
	public MyEnvironment() {
		
		PROMPT = ">";
		MORELINES = "\\";
		MULTILINE = "|";
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
	
	@Override
	public String readLine() throws IOException {

		write(PROMPT);
		String userInput = this.bis.readLine();
		
		String finalUserLine = "";
		
		finalUserLine += userInput.trim();
		
		while(finalUserLine.endsWith(MORELINES)) {
			
			finalUserLine = finalUserLine.substring(0, finalUserLine.length()-1);
			write(MULTILINE);
			userInput = this.bis.readLine();
			finalUserLine += userInput.trim();
			System.out.println("ispis");
		}
		
		return finalUserLine.trim().replaceAll(" +", " ");
		
	}

	@Override
	public void write(String text) throws IOException {

		this.osw.write(text);
		this.osw.flush();
	}

	@Override
	public void writeln(String text) throws IOException {

		
		this.osw.write(text+"\n");
		this.osw.flush();
	}
	
	@Override
	public void errWriteLn(String messege) {
		
		try {
			
			writeln(messege);
		
		} catch (IOException e) {
			System.err.println("There was an IOException in using output stream!");
		}
		
	}

	@Override
	public Iterable<ShellCommand> commands() {

		return new CommandsIterable();
	}

	@Override
	public Character getMultilineSymbol() {

		return MULTILINE.charAt(0);
	}

	@Override
	public void setMultilineSymbol(Character symbol) {

		MULTILINE = new String(symbol.toString());
	}

	@Override
	public Character getPromptSymbol() {

		return PROMPT.charAt(0);
	}

	@Override
	public void setPromptSymbol(Character symbol) {

		PROMPT = new String(symbol.toString());
	}

	@Override
	public Character getMorelinesSymbol() {

		return MORELINES.charAt(0);
	}

	@Override
	public void setMorelinesSymbol(Character symbol) {

		MORELINES = new String(symbol.toString());
	}

	/**
	 * Class that represents iterable collection of supported commands.
	 * @author Vilim Staroveški
	 *
	 */
	private class CommandsIterable implements Iterable<ShellCommand>{

		@Override
		public Iterator<ShellCommand> iterator() {

			return new CommandsIterator();
		}
	}
	 
	/**
	 * Iterator class used for iteration trough iterable collection of commands.
	 * 
	 * @author Vilim Staroveški
	 *
	 */
	private class CommandsIterator implements Iterator<ShellCommand> {

		/**
		 * Map containing all supported commands.
		 */
		private Map<String, ShellCommand> commandsMap;
		
		/**
		 * List with the names of supported commands.
		 */
		private List<String> keyList;
		
		/**
		 * Number of how many commands are left to iterate.
		 */
		private int howManyLeft;
		
		/**
		 * Creates new iterator for iterable collection of commands.
		 */
		public CommandsIterator() {
			
			this.commandsMap = commands;
			this.keyList = new ArrayList<String>();
			this.keyList.addAll(commandsMap.keySet());
			this.howManyLeft = commands.size();
		}
		
		@Override
		public boolean hasNext() {
			
			return howManyLeft > 0;
		}

		@Override
		public ShellCommand next() {

			return this.commandsMap.get(this.keyList.get(howManyLeft-- -1));
		}
		
	}
}
