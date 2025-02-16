@REM play-java-hello-world-tutorial launcher script
@REM
@REM Environment:
@REM JAVA_HOME - location of a JDK home dir (optional if java on path)
@REM CFG_OPTS  - JVM options (optional)
@REM Configuration:
@REM PLAY_JAVA_HELLO_WORLD_TUTORIAL_config.txt found in the PLAY_JAVA_HELLO_WORLD_TUTORIAL_HOME.
@setlocal enabledelayedexpansion
@setlocal enableextensions

@echo off


if "%PLAY_JAVA_HELLO_WORLD_TUTORIAL_HOME%"=="" (
  set "APP_HOME=%~dp0\\.."

  rem Also set the old env name for backwards compatibility
  set "PLAY_JAVA_HELLO_WORLD_TUTORIAL_HOME=%~dp0\\.."
) else (
  set "APP_HOME=%PLAY_JAVA_HELLO_WORLD_TUTORIAL_HOME%"
)

set "APP_LIB_DIR=%APP_HOME%\lib\"

rem Detect if we were double clicked, although theoretically A user could
rem manually run cmd /c
for %%x in (!cmdcmdline!) do if %%~x==/c set DOUBLECLICKED=1

rem FIRST we load the config file of extra options.
set "CFG_FILE=%APP_HOME%\PLAY_JAVA_HELLO_WORLD_TUTORIAL_config.txt"
set CFG_OPTS=
call :parse_config "%CFG_FILE%" CFG_OPTS

rem We use the value of the JAVA_OPTS environment variable if defined, rather than the config.
set _JAVA_OPTS=%JAVA_OPTS%
if "!_JAVA_OPTS!"=="" set _JAVA_OPTS=!CFG_OPTS!

rem We keep in _JAVA_PARAMS all -J-prefixed and -D-prefixed arguments
rem "-J" is stripped, "-D" is left as is, and everything is appended to JAVA_OPTS
set _JAVA_PARAMS=
set _APP_ARGS=

set "APP_CLASSPATH=%APP_LIB_DIR%\..\conf\;%APP_LIB_DIR%\com.example.play-java-hello-world-tutorial-1.0-SNAPSHOT-sans-externalized.jar;%APP_LIB_DIR%\org.scala-lang.scala-library-2.13.1.jar;%APP_LIB_DIR%\com.typesafe.play.play-enhancer-1.2.2.jar;%APP_LIB_DIR%\com.typesafe.play.twirl-api_2.13-1.5.0.jar;%APP_LIB_DIR%\com.typesafe.play.play-ebean_2.13-5.0.2.jar;%APP_LIB_DIR%\com.typesafe.play.play-server_2.13-2.8.7.jar;%APP_LIB_DIR%\com.typesafe.play.play-logback_2.13-2.8.7.jar;%APP_LIB_DIR%\com.typesafe.play.play-akka-http-server_2.13-2.8.7.jar;%APP_LIB_DIR%\com.typesafe.play.play-java-forms_2.13-2.8.7.jar;%APP_LIB_DIR%\com.typesafe.play.filters-helpers_2.13-2.8.7.jar;%APP_LIB_DIR%\com.typesafe.play.play-guice_2.13-2.8.7.jar;%APP_LIB_DIR%\com.typesafe.play.play-jdbc-evolutions_2.13-2.8.7.jar;%APP_LIB_DIR%\com.typesafe.play.play-jdbc_2.13-2.8.7.jar;%APP_LIB_DIR%\com.h2database.h2-1.4.200.jar;%APP_LIB_DIR%\org.javassist.javassist-3.21.0-GA.jar;%APP_LIB_DIR%\org.scala-lang.modules.scala-xml_2.13-1.2.0.jar;%APP_LIB_DIR%\io.ebean.ebean-11.33.2.jar;%APP_LIB_DIR%\io.ebean.ebean-agent-11.27.1.jar;%APP_LIB_DIR%\com.typesafe.play.play-java-jdbc_2.13-2.7.3.jar;%APP_LIB_DIR%\org.reflections.reflections-0.9.11.jar;%APP_LIB_DIR%\com.typesafe.play.play_2.13-2.8.7.jar;%APP_LIB_DIR%\ch.qos.logback.logback-classic-1.2.3.jar;%APP_LIB_DIR%\com.typesafe.play.play-streams_2.13-2.8.7.jar;%APP_LIB_DIR%\com.typesafe.akka.akka-http-core_2.13-10.1.13.jar;%APP_LIB_DIR%\com.typesafe.play.play-java_2.13-2.8.7.jar;%APP_LIB_DIR%\org.scala-lang.modules.scala-java8-compat_2.13-0.9.1.jar;%APP_LIB_DIR%\net.jodah.typetools-0.5.0.jar;%APP_LIB_DIR%\org.hibernate.validator.hibernate-validator-6.1.5.Final.jar;%APP_LIB_DIR%\org.springframework.spring-context-5.2.9.RELEASE.jar;%APP_LIB_DIR%\org.springframework.spring-core-5.2.9.RELEASE.jar;%APP_LIB_DIR%\org.springframework.spring-beans-5.2.9.RELEASE.jar;%APP_LIB_DIR%\com.google.inject.guice-4.2.3.jar;%APP_LIB_DIR%\com.google.inject.extensions.guice-assistedinject-4.2.3.jar;%APP_LIB_DIR%\com.typesafe.play.play-jdbc-api_2.13-2.8.7.jar;%APP_LIB_DIR%\com.zaxxer.HikariCP-3.4.5.jar;%APP_LIB_DIR%\com.googlecode.usc.jdbcdslog-1.0.6.2.jar;%APP_LIB_DIR%\tyrex.tyrex-1.0.1.jar;%APP_LIB_DIR%\org.yaml.snakeyaml-1.21.jar;%APP_LIB_DIR%\io.ebean.persistence-api-2.2.1.jar;%APP_LIB_DIR%\io.ebean.ebean-annotation-4.5.jar;%APP_LIB_DIR%\io.ebean.ebean-datasource-4.3.2.jar;%APP_LIB_DIR%\org.avaje.avaje-classpath-scanner-3.1.1.jar;%APP_LIB_DIR%\io.ebean.ebean-migration-11.13.1.jar;%APP_LIB_DIR%\org.antlr.antlr4-runtime-4.7.2.jar;%APP_LIB_DIR%\com.fasterxml.jackson.core.jackson-core-2.10.5.jar;%APP_LIB_DIR%\com.google.guava.guava-28.2-jre.jar;%APP_LIB_DIR%\com.typesafe.play.build-link-2.8.7.jar;%APP_LIB_DIR%\org.slf4j.slf4j-api-1.7.30.jar;%APP_LIB_DIR%\org.slf4j.jul-to-slf4j-1.7.30.jar;%APP_LIB_DIR%\org.slf4j.jcl-over-slf4j-1.7.30.jar;%APP_LIB_DIR%\com.typesafe.akka.akka-actor_2.13-2.6.10.jar;%APP_LIB_DIR%\com.typesafe.akka.akka-actor-typed_2.13-2.6.10.jar;%APP_LIB_DIR%\com.typesafe.akka.akka-slf4j_2.13-2.6.10.jar;%APP_LIB_DIR%\com.typesafe.akka.akka-serialization-jackson_2.13-2.6.10.jar;%APP_LIB_DIR%\com.fasterxml.jackson.core.jackson-annotations-2.10.5.jar;%APP_LIB_DIR%\com.fasterxml.jackson.datatype.jackson-datatype-jdk8-2.10.5.jar;%APP_LIB_DIR%\com.fasterxml.jackson.datatype.jackson-datatype-jsr310-2.10.5.jar;%APP_LIB_DIR%\com.fasterxml.jackson.core.jackson-databind-2.10.5.1.jar;%APP_LIB_DIR%\com.typesafe.play.play-json_2.13-2.8.1.jar;%APP_LIB_DIR%\io.jsonwebtoken.jjwt-0.9.1.jar;%APP_LIB_DIR%\jakarta.xml.bind.jakarta.xml.bind-api-2.3.3.jar;%APP_LIB_DIR%\jakarta.transaction.jakarta.transaction-api-1.3.3.jar;%APP_LIB_DIR%\javax.inject.javax.inject-1.jar;%APP_LIB_DIR%\com.typesafe.ssl-config-core_2.13-0.4.2.jar;%APP_LIB_DIR%\org.scala-lang.modules.scala-parser-combinators_2.13-1.1.2.jar;%APP_LIB_DIR%\ch.qos.logback.logback-core-1.2.3.jar;%APP_LIB_DIR%\org.reactivestreams.reactive-streams-1.0.3.jar;%APP_LIB_DIR%\com.typesafe.akka.akka-stream_2.13-2.6.10.jar;%APP_LIB_DIR%\com.typesafe.akka.akka-parsing_2.13-10.1.13.jar;%APP_LIB_DIR%\jakarta.validation.jakarta.validation-api-2.0.2.jar;%APP_LIB_DIR%\org.jboss.logging.jboss-logging-3.3.2.Final.jar;%APP_LIB_DIR%\com.fasterxml.classmate-1.3.4.jar;%APP_LIB_DIR%\aopalliance.aopalliance-1.0.jar;%APP_LIB_DIR%\io.ebean.ebean-datasource-api-4.3.jar;%APP_LIB_DIR%\org.avaje.avaje-classpath-scanner-api-2.2.jar;%APP_LIB_DIR%\com.google.guava.failureaccess-1.0.1.jar;%APP_LIB_DIR%\com.google.guava.listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar;%APP_LIB_DIR%\com.google.code.findbugs.jsr305-3.0.2.jar;%APP_LIB_DIR%\org.checkerframework.checker-qual-2.10.0.jar;%APP_LIB_DIR%\com.google.errorprone.error_prone_annotations-2.3.4.jar;%APP_LIB_DIR%\com.google.j2objc.j2objc-annotations-1.3.jar;%APP_LIB_DIR%\com.typesafe.play.play-exceptions-2.8.7.jar;%APP_LIB_DIR%\com.typesafe.config-1.4.0.jar;%APP_LIB_DIR%\com.fasterxml.jackson.module.jackson-module-scala_2.13-2.10.5.jar;%APP_LIB_DIR%\com.fasterxml.jackson.module.jackson-module-parameter-names-2.10.5.jar;%APP_LIB_DIR%\com.fasterxml.jackson.dataformat.jackson-dataformat-cbor-2.10.5.jar;%APP_LIB_DIR%\org.lz4.lz4-java-1.7.1.jar;%APP_LIB_DIR%\com.typesafe.play.play-functional_2.13-2.8.1.jar;%APP_LIB_DIR%\org.scala-lang.scala-reflect-2.13.1.jar;%APP_LIB_DIR%\joda-time.joda-time-2.10.5.jar;%APP_LIB_DIR%\jakarta.activation.jakarta.activation-api-1.2.2.jar;%APP_LIB_DIR%\com.typesafe.akka.akka-protobuf-v3_2.13-2.6.10.jar;%APP_LIB_DIR%\com.fasterxml.jackson.module.jackson-module-paranamer-2.10.5.jar;%APP_LIB_DIR%\com.thoughtworks.paranamer.paranamer-2.8.jar;%APP_LIB_DIR%\com.example.play-java-hello-world-tutorial-1.0-SNAPSHOT-assets.jar"
set "APP_MAIN_CLASS=play.core.server.ProdServerStart"
set "SCRIPT_CONF_FILE=%APP_HOME%\conf\application.ini"

rem Bundled JRE has priority over standard environment variables
if defined BUNDLED_JVM (
  set "_JAVACMD=%BUNDLED_JVM%\bin\java.exe"
) else (
  if "%JAVACMD%" neq "" (
    set "_JAVACMD=%JAVACMD%"
  ) else (
    if "%JAVA_HOME%" neq "" (
      if exist "%JAVA_HOME%\bin\java.exe" set "_JAVACMD=%JAVA_HOME%\bin\java.exe"
    )
  )
)

if "%_JAVACMD%"=="" set _JAVACMD=java

rem Detect if this java is ok to use.
for /F %%j in ('"%_JAVACMD%" -version  2^>^&1') do (
  if %%~j==java set JAVAINSTALLED=1
  if %%~j==openjdk set JAVAINSTALLED=1
)

rem BAT has no logical or, so we do it OLD SCHOOL! Oppan Redmond Style
set JAVAOK=true
if not defined JAVAINSTALLED set JAVAOK=false

if "%JAVAOK%"=="false" (
  echo.
  echo A Java JDK is not installed or can't be found.
  if not "%JAVA_HOME%"=="" (
    echo JAVA_HOME = "%JAVA_HOME%"
  )
  echo.
  echo Please go to
  echo   http://www.oracle.com/technetwork/java/javase/downloads/index.html
  echo and download a valid Java JDK and install before running play-java-hello-world-tutorial.
  echo.
  echo If you think this message is in error, please check
  echo your environment variables to see if "java.exe" and "javac.exe" are
  echo available via JAVA_HOME or PATH.
  echo.
  if defined DOUBLECLICKED pause
  exit /B 1
)

rem if configuration files exist, prepend their contents to the script arguments so it can be processed by this runner
call :parse_config "%SCRIPT_CONF_FILE%" SCRIPT_CONF_ARGS

call :process_args %SCRIPT_CONF_ARGS% %%*

set _JAVA_OPTS=!_JAVA_OPTS! !_JAVA_PARAMS!

if defined CUSTOM_MAIN_CLASS (
    set MAIN_CLASS=!CUSTOM_MAIN_CLASS!
) else (
    set MAIN_CLASS=!APP_MAIN_CLASS!
)

rem Call the application and pass all arguments unchanged.
"%_JAVACMD%" !_JAVA_OPTS! !PLAY_JAVA_HELLO_WORLD_TUTORIAL_OPTS! -cp "%APP_CLASSPATH%" %MAIN_CLASS% !_APP_ARGS!

@endlocal

exit /B %ERRORLEVEL%


rem Loads a configuration file full of default command line options for this script.
rem First argument is the path to the config file.
rem Second argument is the name of the environment variable to write to.
:parse_config
  set _PARSE_FILE=%~1
  set _PARSE_OUT=
  if exist "%_PARSE_FILE%" (
    FOR /F "tokens=* eol=# usebackq delims=" %%i IN ("%_PARSE_FILE%") DO (
      set _PARSE_OUT=!_PARSE_OUT! %%i
    )
  )
  set %2=!_PARSE_OUT!
exit /B 0


:add_java
  set _JAVA_PARAMS=!_JAVA_PARAMS! %*
exit /B 0


:add_app
  set _APP_ARGS=!_APP_ARGS! %*
exit /B 0


rem Processes incoming arguments and places them in appropriate global variables
:process_args
  :param_loop
  call set _PARAM1=%%1
  set "_TEST_PARAM=%~1"

  if ["!_PARAM1!"]==[""] goto param_afterloop


  rem ignore arguments that do not start with '-'
  if "%_TEST_PARAM:~0,1%"=="-" goto param_java_check
  set _APP_ARGS=!_APP_ARGS! !_PARAM1!
  shift
  goto param_loop

  :param_java_check
  if "!_TEST_PARAM:~0,2!"=="-J" (
    rem strip -J prefix
    set _JAVA_PARAMS=!_JAVA_PARAMS! !_TEST_PARAM:~2!
    shift
    goto param_loop
  )

  if "!_TEST_PARAM:~0,2!"=="-D" (
    rem test if this was double-quoted property "-Dprop=42"
    for /F "delims== tokens=1,*" %%G in ("!_TEST_PARAM!") DO (
      if not ["%%H"] == [""] (
        set _JAVA_PARAMS=!_JAVA_PARAMS! !_PARAM1!
      ) else if [%2] neq [] (
        rem it was a normal property: -Dprop=42 or -Drop="42"
        call set _PARAM1=%%1=%%2
        set _JAVA_PARAMS=!_JAVA_PARAMS! !_PARAM1!
        shift
      )
    )
  ) else (
    if "!_TEST_PARAM!"=="-main" (
      call set CUSTOM_MAIN_CLASS=%%2
      shift
    ) else (
      set _APP_ARGS=!_APP_ARGS! !_PARAM1!
    )
  )
  shift
  goto param_loop
  :param_afterloop

exit /B 0
