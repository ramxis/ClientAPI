
//
//  ========================================================================
//  Copyright (c) 1995-2014 Mort Bay Consulting Pty. Ltd.
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
//
// adapted version... (Sebastian Proksch, APLv2)
// see: http://archive.eclipse.org/jetty/8.0.0.M1/xref/org/eclipse/jetty/embedded/LikeJettyXml.html

import java.lang.management.ManagementFactory;

import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;

import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ScheduledExecutorScheduler;
import org.eclipse.jetty.webapp.WebAppContext;

import com.google.inject.servlet.GuiceFilter;


public class run_platform {
	

	public static void main(String[] args) throws Exception {
		QueuedThreadPool threadPool = new QueuedThreadPool();
		threadPool.setMaxThreads(500);

		// Server
		Server server = new Server(threadPool);

		// Scheduler
		server.addBean(new ScheduledExecutorScheduler());

		// HTTP Configuration
		HttpConfiguration http_config = createHttpConfiguration();
		// httpConfig.addCustomizer(new ForwardedRequestCustomizer());

		// Handler Structure
		HandlerCollection handlers = createHandlers();
		server.setHandler(handlers);

		// Extra options
		server.setDumpAfterStart(false);
		server.setDumpBeforeStop(false);
		server.setStopAtShutdown(true);

		// === jetty-jmx.xml ===
		MBeanContainer mbContainer = new MBeanContainer(ManagementFactory.getPlatformMBeanServer());
		server.addBean(mbContainer);

		// === jetty-http.xml ===
		ServerConnector http = new ServerConnector(server, new HttpConnectionFactory(http_config));
		http.setPort(8080);
		http.setIdleTimeout(30000);
		server.addConnector(http);

		
		server = new Server(8080);

        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setContextPath("/");
        webAppContext.setResourceBase("src/main/webapp");
        webAppContext.setParentLoaderPriority(true);
        webAppContext.addEventListener(new GuiceConfig());
        webAppContext.addFilter(GuiceFilter.class, "/*", null);

        server.setHandler(webAppContext);
        server.start();
        server.join();
	}

	

	private static HandlerCollection createHandlers() {
		HandlerCollection handlers = new HandlerCollection();
		ContextHandlerCollection contexts = new ContextHandlerCollection();
		handlers.setHandlers(new Handler[] { contexts, new DefaultHandler() });
		return handlers;
	}

	private static HttpConfiguration createHttpConfiguration() {
		HttpConfiguration http_config = new HttpConfiguration();
		http_config.setSecureScheme("https");
		http_config.setSecurePort(8443);
		http_config.setOutputBufferSize(32768);
		http_config.setRequestHeaderSize(8192);
		http_config.setResponseHeaderSize(8192);
		http_config.setSendServerVersion(true);
		http_config.setSendDateHeader(false);
		return http_config;
	}
}