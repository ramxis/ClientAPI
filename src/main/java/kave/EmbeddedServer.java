/**
 * Copyright 2016 - Muhammad Rameez
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * Contributors:
 *    
 */
package kave;

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

public class EmbeddedServer {

	private Server server;

	public void start() throws Exception, InterruptedException {
		QueuedThreadPool threadPool = new QueuedThreadPool();
		threadPool.setMaxThreads(500);

		server = new Server(threadPool);

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
		String homePath = System.getProperty("user.home");
		server.start();
		//server.join();
		
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

	public void stop() throws Exception {
		server.stop();
	}

	public void join() throws InterruptedException {
		// TODO Auto-generated method stub
		server.join();
		
	}
}