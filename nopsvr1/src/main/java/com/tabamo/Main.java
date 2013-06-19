package com.tabamo;

public class Main {
	private WebServer server;

	public static void main(String[] anArgs) throws Exception {
		new Main().start();
	}

	public Main() {
		this.server = new WebServer(8000);
	}

	public void start() throws Exception {
		this.server.start();
		this.server.join();
	}
}