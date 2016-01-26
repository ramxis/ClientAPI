
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

import kave.EmbeddedServer;

public class run_platform {
	public static void main(String[] args) throws Exception {
		//new EmbeddedServer().start();
		//new EmbeddedServer().join();
		EmbeddedServer server = new EmbeddedServer();
		server.start();
		server.join();
	}
}