/*
 * Copyright 2002-2006 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.xebia.springframework.jmx;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.servlet.ServletContext;

import org.springframework.core.style.ToStringCreator;
import org.springframework.jmx.export.naming.ObjectNamingStrategy;
import org.springframework.web.context.ServletContextAware;

/**
 * {@link ObjectNamingStrategy} wrapper to append "<code>application=${servletcontextName},bean=${beanKey}</code>"
 * to the canonical name generated by the underlying <code>objectNamingStrategy</code>.
 * 
 * @see ServletContext#getServletContextName()
 * @author <a href="mailto:cyrille.leclerc@pobox.com">Cyrille Le Clerc</a>
 */
public class ServletContextAwareObjectNamingStrategy implements ObjectNamingStrategy, ServletContextAware {

    protected ObjectNamingStrategy objectNamingStrategy;

    protected String servletContextName;

    /**
     * <p>
     * Compose <code>ObjectName</code> with underlying <code>objectNamingStrategy</code>'s
     * result and the following parameters: "<code>application=${servletcontextName},bean=${beanKey}</code>".
     * </p>
     */
    public ObjectName getObjectName(Object managedBean, String beanKey) throws MalformedObjectNameException {
        ObjectName objectName = this.objectNamingStrategy.getObjectName(managedBean, beanKey);
        String canonicalName = objectName.getCanonicalName();
        canonicalName += ",application=" + ObjectName.quote(this.servletContextName) + ",bean=" + ObjectName.quote(beanKey);
        ObjectName result = ObjectName.getInstance(canonicalName);
        return result;
    }

    public void setObjectNamingStrategy(ObjectNamingStrategy objectNamingStrategy) {
        this.objectNamingStrategy = objectNamingStrategy;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContextName = servletContext.getServletContextName();
    }

    @Override
    public String toString() {
        return new ToStringCreator(this).append("servletContextName", this.servletContextName).append("objectNamingStrategy",
                this.objectNamingStrategy).toString();
    }
}