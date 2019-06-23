package hr.fer.zemris.java.tecaj.hw07.environment;

import java.io.IOException;

import hr.fer.zemris.java.tecaj.hw07.commands.ShellCommand;

/**
 * Interface represents an working envrionment for shell.
 * @author Vili
 *
 */
public interface Environment {

	/**
	 * Reads input from user.
	 * @return user input.
	 * @throws IOException if there is an IO error occured
	 */
	String readLine() throws IOException;
	/**
	 * Writes to the output
	 * @param text text to write
	 * @throws IOException if there is an IO error occured.
	 */
	void write(String text) throws IOException;
	/**
	 * Writes line to output
	 * @param text text to write
	 * @throws IOException if there is an IO error occured.
	 */
	void writeln(String text) throws IOException;
	/**
	 * Returns Iterable collection of commands.
	 * @return Iterable collection of commands.
	 */
	Iterable<ShellCommand> commands();
	/**
	 * Returns multiline symbol.
	 * @return multiline symbol.
	 */
	Character getMultilineSymbol();
	/**
	 * Changes symbol for multiline.
	 * @param symbol symbol multilne
	 */
	void setMultilineSymbol(Character symbol);
	/**
	 * Returns prompt symbol.
	 * @return prompt symbol.
	 */
	Character getPromptSymbol();
	/**
	 * Changes symbol for prompt
	 * @param symbol symbol prompt
	 */
	void setPromptSymbol(Character symbol);
	/**
	 * Returns morelines symbol.
	 * @return morelines symbol.
	 */
	Character getMorelinesSymbol();
	/**
	 * Changes symbol for morelines
	 * @param symbol symbol morelines
	 */
	void setMorelinesSymbol(Character symbol);
	/**
	 * Writes to err output
	 * @param messege text to write.
	 */
	void errWriteLn(String messege);
}
