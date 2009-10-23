package data;

import java.util.*;
import java.util.Map.*;

public class RemoteBook implements Book {

	int socket;
	VirtualBook book;
	
	public RemoteBook(int socket) {
		this(socket, null);
	}
	
	public RemoteBook(int socket, VirtualBook book) {
		this.socket = socket;
		this.book = book;
	}
	
	@Override
	public Iterator<Entry<String, String>> enumerateProperties() {
		if(null != this.book)
			return this.enumerateProperties();
		else 
			return null; // return socket.send("enumerateProperties");
	}

	@Override
	public Iterator<Entry<String, Integer>> enumerateTags() {
		if(null != this.book)
			return this.enumerateTags();
		else 
			return null; // return socket.send("enumerateTagss");
	}

	@Override
	public String getProperty(String name) throws IllegalArgumentException {
		if(null != this.book)
			return this.getProperty(name);
		else 
			return null; // return socket.send("enumerateProperties", name);
	}

	@Override
	public String setProperty(String name, String value) {
		if(null != this.book)
			return this.setProperty(name, value);
		else 
			return null; // return socket.send("setProperty", name, value);
	}

	@Override
	public int tag(String tag) throws IllegalArgumentException {
		if(null != this.book)
			return this.tag(tag);
		else
			return 0; // return socket.send("tag", tag);
	}

	@Override
	public int untag(String tag) throws IllegalArgumentException {
		if(null != this.book)
			return this.untag(tag);
		else 
			return 0; // return socket.send("enumerateProperties");
	}

	@Override
	public int weight(String tag) throws IllegalArgumentException {
		if(null != this.book)
			return this.weight(tag);
		else 
			return 0; // return socket.send("enumerateProperties");
	}

}
