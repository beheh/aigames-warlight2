package de.beheh.warlight2.mock;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 *
 * @author Benedict Etzel
 */
public class MockServer implements Runnable {
	
	private InputStream inputStream;

	public InputStream getInputStream() {
		return inputStream;
	}

	private OutputStream outputStream;

	public OutputStream getOutputStream() {
		return outputStream;
	}

	@Override
	public void run() {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
	}

}
