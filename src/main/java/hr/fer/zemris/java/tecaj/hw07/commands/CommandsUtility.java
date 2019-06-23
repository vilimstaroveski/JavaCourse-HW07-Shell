package hr.fer.zemris.java.tecaj.hw07.commands;

import hr.fer.zemris.java.tecaj.hw07.environment.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.MyShell;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidParameterException;

/**
 * Utility class for {@link MyShell}.
 * @author Vilim Staroveški
 *
 */
public class CommandsUtility {
	
	private static enum ArgumentsType {
		STRING, PATH, BOTH ;
	}

	public static Path parsePath(Environment env, String arguments) {
		
		arguments = arguments.replace('\"', ' ').trim();
		Path path = null;
		
		try {
			
			path = Paths.get(arguments);
		
		}catch(InvalidParameterException e) {
			env.errWriteLn("Given path does not exists!"+arguments);
			return null;
		}
		
		if(!Files.exists(path)) {
			env.errWriteLn("Given path does not exists!"+arguments);
			return null;
		}
		
		return path;
		
	}

	public static String giveMeOneArgument(String arguments, CommandType type) {
		int numberOfWords = 0;
		switch(type) {
		
		case HELP: 			numberOfWords = numberOfWords(arguments, ArgumentsType.STRING);
							break;
		case TREE:			numberOfWords = numberOfWords(arguments, ArgumentsType.PATH);
							break;
		case LS:			numberOfWords = numberOfWords(arguments, ArgumentsType.PATH);
							break;
		case MKDIR:			numberOfWords = numberOfWords(arguments, ArgumentsType.PATH);
							break;
		case HEXDUMP:		numberOfWords = numberOfWords(arguments, ArgumentsType.PATH);
							break;
		default:			break;
		}
		
		if(numberOfWords == 1) {
			return arguments.trim();
		}
		else if(numberOfWords > 1){
			throw new IllegalArgumentException("Expected 1 argument but recived "+numberOfWords);
		}
		else {
			return null;
		}
		
	}

	private static int numberOfWords(String arguments, ArgumentsType typeOfArguments) {

		if(arguments == null) {
			return 0;
		}
		if(arguments.isEmpty()) {
			return 0;
		}
		switch(typeOfArguments) {
		
		case STRING:		return arguments.split(" ").length;
		case PATH:			return arguments.split("\"").length-1;//because split counts non-character before first "
		case BOTH:			return arguments.split("\"").length-1;//because split counts non-character before first "			
		}
		return 0;
	}

	public static String giveMeTwoArgument(String arguments, CommandType type) {
		int numberOfWords = 0;
		
		switch(type) {
		
		case SYMBOL: 		numberOfWords = numberOfWords(arguments, ArgumentsType.STRING);
							break;
		case CAT:			numberOfWords = numberOfWords(arguments, ArgumentsType.BOTH);
							break;
		case COPY:			numberOfWords = numberOfWords(arguments, ArgumentsType.PATH);
							numberOfWords--;//because of the space between two paths
							break;
		
		default:			break;
		}
		if(numberOfWords == 2) {
			
			if(type == CommandType.CAT) {
				int positionOfSplitter = arguments.lastIndexOf(' ');
				return new StringBuilder(arguments).insert(positionOfSplitter, " &").toString();
			}
			return arguments.trim();
		}
		else if(numberOfWords == 1){
			
			if(type == CommandType.COPY) {
				throw new IllegalArgumentException("Expected 1 or 2 arguments but recived "+numberOfWords);
			}
			
			return arguments.trim();
		}
		else {
			throw new IllegalArgumentException("Expected 1 or 2 arguments but recived "+numberOfWords);
		}
	}

	public static Path parseNullablePath(Environment env, String arguments) {
		
		arguments = arguments.replace('\"', ' ').trim();
		Path path = null;
		
		try {
			
			path = Paths.get(arguments);
		
		}catch(InvalidParameterException e) {
			env.errWriteLn("Given path does not exists!"+arguments);
			return null;
		}
		
		return path;
	}
		
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
