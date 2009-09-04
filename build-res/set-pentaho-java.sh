#!/bin/sh
# -----------------------------------------------------------------------------
# Find a suitable Java
# -----------------------------------------------------------------------------

setPentahoJava() {
  if [ -n "$PENTAHO_JAVA_HOME" ]; then
    echo "DEBUG: Using PENTAHO_JAVA_HOME"
    _PENTAHO_JAVA_HOME="$PENTAHO_JAVA_HOME"
    _PENTAHO_JAVA="$_PENTAHO_JAVA_HOME"/bin/java
  elif [ -n "$JAVA_HOME" ]; then
    echo "DEBUG: Using JAVA_HOME"
    _PENTAHO_JAVA_HOME="$JAVA_HOME"
    _PENTAHO_JAVA="$_PENTAHO_JAVA_HOME"/bin/java
  elif [ -n "$JRE_HOME" ]; then
    echo "DEBUG: Using JRE_HOME"
    _PENTAHO_JAVA_HOME="$JRE_HOME"
    _PENTAHO_JAVA="$_PENTAHO_JAVA_HOME"/bin/java
  elif [ -n "$1" ] && [ -x "$1"/bin/java ]; then
    echo "DEBUG: Using value ($1) from calling script"
    _PENTAHO_JAVA_HOME="$1"
    _PENTAHO_JAVA="$_PENTAHO_JAVA_HOME"/bin/java
  else
    echo "WARNING: Using java from path"
    _PENTAHO_JAVA_HOME=
    _PENTAHO_JAVA="java"
  fi
  echo "DEBUG: _PENTAHO_JAVA_HOME=$_PENTAHO_JAVA_HOME"
  echo "DEBUG: _PENTAHO_JAVA=$_PENTAHO_JAVA"
}