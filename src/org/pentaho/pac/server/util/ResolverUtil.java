/*
 *  Copyright 2005-2006 Tim Fennell
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
 * 
 * This code copied from the stripes project:
 * http://sourceforge.net/projects/stripes/
 * From: net.sourceforge.stripes.util.ResolverUtil.java
 * Revision:  533
*/

package org.pentaho.pac.server.util;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.pac.server.common.AppConfigProperties;

public class ResolverUtil<T> {

  private static final Log log = LogFactory.getLog(AppConfigProperties.class);

  public static interface Test {

    boolean matches(Class<?> type);
  }

  public static class IsA implements Test {

    private Class<?> parent;

    public IsA(Class<?> parentType) {
      super();
      this.parent = parentType;
    }

    public boolean matches(Class<?> type) {
      return type != null && parent.isAssignableFrom(type);
    }

    @Override()
    public String toString() {
      return "is assignable to " + parent.getSimpleName(); //$NON-NLS-1$
    }
  }

  public static class NameEndsWith implements Test {

    private String suffix;

    public NameEndsWith(String suffix) {
      super();
      this.suffix = suffix;
    }

    public boolean matches(Class<?> type) {
      return type != null && type.getName().endsWith(suffix);
    }

    @Override()
    public String toString() {
      return "ends with the suffix " + suffix; //$NON-NLS-1$
    }
  }

  public static class AnnotatedWith implements Test {

    private Class<? extends Annotation> annotation;

    public AnnotatedWith(Class<? extends Annotation> annotation) {
      super();
      this.annotation = annotation;
    }

    public boolean matches(Class<?> type) {
      return type != null && type.isAnnotationPresent(annotation);
    }

    @Override()
    public String toString() {
      return "annotated with @" + annotation.getSimpleName(); //$NON-NLS-1$
    }
  }

  private Set<Class<? extends T>> matches = new HashSet<Class<? extends T>>();

  private ClassLoader classloader;

  public int size() {
    return matches.size();
  }

  public Set<Class<? extends T>> getClasses() {
    return matches;
  }

  public ClassLoader getClassLoader() {
    return classloader == null ? Thread.currentThread()
        .getContextClassLoader() : classloader;
  }

  public void setClassLoader(ClassLoader classloader) {
    this.classloader = classloader;
  }

  public void findImplementations(Class<?> parent, String... packageNames) {
    if (packageNames == null)
      return;
    Test test = new IsA(parent);
    for (String pkg : packageNames) {
      findInPackage(pkg, test);
    }
  }

  public void findSuffix(String suffix, String... packageNames) {
    if (packageNames == null)
      return;
    Test test = new NameEndsWith(suffix);
    for (String pkg : packageNames) {
      findInPackage(pkg, test);
    }
  }

  public void findAnnotated(Class<? extends Annotation> annotation,
      String... packageNames) {
    if (packageNames == null)
      return;
    Test test = new AnnotatedWith(annotation);
    for (String pkg : packageNames) {
      findInPackage(pkg, test);
    }
  }

  public void find(Test[] test, String... packageNames) {
    if (packageNames == null)
      return;
    for (String pkg : packageNames) {
      findInPackage(pkg, test);
    }
  }

  public void find(Test test, String... packageNames) {
    find(new Test[] { test }, packageNames);
  }

  public void findInPackage(String packageName, Test... tests) {
    packageName = packageName.replace('.', '/');
    ClassLoader loader = getClassLoader();
    Enumeration<URL> urls;
    try {
      urls = loader.getResources(packageName);
    } catch (IOException ioe) {
      log.debug("Could not read package: " + packageName, ioe); //$NON-NLS-1$
      return;
    }
    while (urls.hasMoreElements()) {
      try {
        URL eurl = urls.nextElement();
        String urlPath = eurl.toURI().toString();

        if (urlPath.indexOf('!') > 0) {
          urlPath = urlPath.substring(0, urlPath.indexOf('!'));
          if (urlPath.startsWith("jar:")) //$NON-NLS-1$
            urlPath = urlPath.substring(4);
          eurl = new URL(urlPath);
        }
        log.info("Scanning for classes in [" + urlPath //$NON-NLS-1$
            + "] matching criteria: " + tests); //$NON-NLS-1$

        // is it a file?
        File file = new File(URLDecoder.decode(eurl.getFile(), "UTF-8")); //$NON-NLS-1$
        // File file = new File(eurl.getFile());
        if (file.exists() && file.isDirectory()) {
          loadImplementationsInDirectory(packageName, file, tests);
        } else {
          loadImplementationsInJar(packageName, eurl, tests);
        }
      } catch (IOException e) {
        log.debug("could not read entries", e); //$NON-NLS-1$
      } catch (URISyntaxException se) {
        log.debug("could not read entries", se); //$NON-NLS-1$
      }
    }
  }

  public void loadImplementationsInDirectory(String parent, File location,
      Test... tests) {
    File[] files = location.listFiles();
    StringBuilder builder = null;
    for (File file : files) {
      builder = new StringBuilder(100);
      builder.append(parent).append("/").append(file.getName()); //$NON-NLS-1$
      String packageOrClass = (parent == null ? file.getName() : builder
          .toString());
      if (file.isDirectory()) {
        loadImplementationsInDirectory(packageOrClass, file, tests);
      } else if (file.getName().endsWith(".class")) { //$NON-NLS-1$
        addIfMatching(packageOrClass, tests);
      }
    }
  }

  public void loadImplementationsInJar(String parent, URL jarfile,
      Test... tests) {
    try {
      JarEntry entry;
      JarInputStream jarStream = new JarInputStream(jarfile.openStream());
      while ((entry = jarStream.getNextJarEntry()) != null) {
        String name = entry.getName();
        if (!entry.isDirectory() && name.startsWith(parent)
            && name.endsWith(".class")) { //$NON-NLS-1$
          addIfMatching(name, tests);
        }
      }
    } catch (IOException ioe) {
      log.debug("Could not search jar file \\\'" + jarfile //$NON-NLS-1$
          + "\\\' for classes matching criteria: " + tests //$NON-NLS-1$
          + " due to an IOException", ioe); //$NON-NLS-1$
    }
  }

  @SuppressWarnings("unchecked")
  protected void addIfMatching(String fqn, Test... tests) {
    try {
      String externalName = fqn.substring(0, fqn.indexOf('.')).replace(
          '/', '.');
      ClassLoader loader = getClassLoader();

      Class<?> type = loader.loadClass(externalName);

      for (Test test : tests) {
        if (log.isDebugEnabled()) {
          log.debug("Checking to see if class " + externalName //$NON-NLS-1$
              + " matches criteria [" + test + "]"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        if (test.matches(type)) {
          matches.add((Class<T>) type);
        }
      }
    } catch (Throwable t) {
      log.debug("Could not examine class \\\'" + fqn + "\\\' due to a " //$NON-NLS-1$ //$NON-NLS-2$
          + t.getClass().getName() + " with message: " //$NON-NLS-1$
          + t.getMessage());
    }
  }
}
