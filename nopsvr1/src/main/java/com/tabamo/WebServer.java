package com.tabamo;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.RequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

public class WebServer {
	private static final String LOG_PATH = "./var/logs/access/yyyy_mm_dd.request.log";
	private static final String WEB_XML = "META-INF/webapp/WEB-INF/web.xml";
	private static final String CLASS_ONLY_AVAILABLE_IN_IDE = "com.sjl.IDE";
	private static final String PROJECT_RELATIVE_PATH_TO_WEBAPP = "src/main/java/META-INF/webapp";
	private Server server;
	private int port;
	private String bindInterface;

	public WebServer(int aPort) {
		this(aPort, null);
	}

	public WebServer(int aPort, String aBindInterface) {
		this.port = aPort;
		this.bindInterface = aBindInterface;
	}

	public void start() throws Exception {
		this.server = new Server();

		this.server.setThreadPool(createThreadPool());
		this.server.addConnector(createConnector());
		this.server.setHandler(createHandlers());
		this.server.setStopAtShutdown(true);

		this.server.start();
	}

	public void join() throws InterruptedException {
		this.server.join();
	}

	public void stop() throws Exception {
		this.server.stop();
	}

	private ThreadPool createThreadPool() {
		QueuedThreadPool _threadPool = new QueuedThreadPool();
		_threadPool.setMinThreads(10);
		_threadPool.setMaxThreads(100);
		return _threadPool;
	}

	private SelectChannelConnector createConnector() {
		SelectChannelConnector _connector = new SelectChannelConnector();
		_connector.setPort(this.port);
		_connector.setHost(this.bindInterface);
		return _connector;
	}

	private HandlerCollection createHandlers() {
		WebAppContext _ctx = new WebAppContext();
		_ctx.setContextPath("/");

		if (isRunningInShadedJar()) {
			_ctx.setWar(getShadedWarUrl());
		} else {
			_ctx.setWar("src/main/java/META-INF/webapp");
		}

		List _handlers = new ArrayList();

		_handlers.add(_ctx);

		HandlerList _contexts = new HandlerList();
		_contexts.setHandlers((Handler[]) _handlers.toArray(new Handler[0]));

		RequestLogHandler _log = new RequestLogHandler();
		_log.setRequestLog(createRequestLog());

		HandlerCollection _result = new HandlerCollection();
		_result.setHandlers(new Handler[] { _contexts, _log });

		return _result;
	}

	private RequestLog createRequestLog() {
		NCSARequestLog _log = new NCSARequestLog();

		File _logPath = new File("./var/logs/access/yyyy_mm_dd.request.log");
		_logPath.getParentFile().mkdirs();

		_log.setFilename(_logPath.getPath());
		_log.setRetainDays(90);
		_log.setExtended(false);
		_log.setAppend(true);
		_log.setLogTimeZone("GMT");
		_log.setLogLatency(true);
		return _log;
	}

	private boolean isRunningInShadedJar() {
		try {
			Class.forName("com.sjl.IDE");
			return false;
		} catch (ClassNotFoundException anExc) {
		}
		return true;
	}

	private URL getResource(String aResource) {
		return Thread.currentThread().getContextClassLoader()
				.getResource(aResource);
	}

	private String getShadedWarUrl() {
		String _urlStr = getResource("META-INF/webapp/WEB-INF/web.xml")
				.toString();

		return _urlStr.substring(0, _urlStr.length() - 15);
	}

	public static abstract interface WebContext {
		public abstract File getWarPath();

		public abstract String getContextPath();
	}
}