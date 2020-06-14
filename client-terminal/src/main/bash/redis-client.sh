#!/usr/bin/bash
BASE_DIR=$(dirname $0)/..
CLASSPATH=$(echo $BASE_DIR/lib/*.jar | tr ' ' ':')
# get java version
JAVA="$JAVA_HOME/bin/java"
JAVA_VERSION=$($JAVA -version 2>&1 | awk -F\" '/java version/{print $2}')
echo "java version:$JAVA_VERSION path:$JAVA"

JAVA_CONFIG=$(mktemp XXXXXXXX)
cat <<EOF | xargs echo >$JAVA_CONFIG
${JAVA_OPTS}
-cp $CLASSPATH
io.github.jrbase.clientterminal.JRTerminal $@
EOF

echo $JAVA_OPTS
echo $CLASSPATH

JAVA_CONFIG=$(cat $JAVA_CONFIG | xargs echo)

echo $JAVA_CONFIG

JAVA="$JAVA_HOME/bin/java"

HOSTNAME=$(hostname)

#nohup $JAVA $JAVA_CONFIG >> server_stdout 2>&1 &
$JAVA $JAVA_CONFIG

