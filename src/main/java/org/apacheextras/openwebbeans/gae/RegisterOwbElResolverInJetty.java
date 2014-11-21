/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apacheextras.openwebbeans.gae;

import javax.el.ELResolver;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.jsp.JspApplicationContext;
import javax.servlet.jsp.JspFactory;

import org.apache.webbeans.config.WebBeansContext;
import org.apache.webbeans.spi.adaptor.ELAdaptor;

/**
 * Workaround for a _very_ nasty bug in Jetty which is used in GAE...
 * @author <a href="mailto:struberg@yahoo.de">Mark Struberg</a>
 */
public class RegisterOwbElResolverInJetty implements ServletRequestListener
{
    private static volatile ELResolver elResolver = null;
    @Override
    public void requestDestroyed(ServletRequestEvent sre)
    {
        // nothing to do
    }

    @Override
    public void requestInitialized(ServletRequestEvent sre)
    {
        if (elResolver == null)
        {
            synchronized (RegisterOwbElResolverInJetty.class)
            {
                if (elResolver == null)
                {
                    ServletContext context = sre.getServletContext();
                    JspApplicationContext jspContext = b.getJspApplicationContext(context);

                    ELAdaptor elAdaptor = WebBeansContext.getInstance().getService(ELAdaptor.class);

                    elResolver = elAdaptor.getOwbELResolver();
                    jspContext.addELResolver(elResolver);
                }
            }
        }
    }

}
