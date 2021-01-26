@REM play-silhouette-seed launcher script
@REM
@REM Environment:
@REM JAVA_HOME - location of a JDK home dir (optional if java on path)
@REM CFG_OPTS  - JVM options (optional)
@REM Configuration:
@REM PLAY_SILHOUETTE_SEED_config.txt found in the PLAY_SILHOUETTE_SEED_HOME.
@setlocal enabledelayedexpansion
@setlocal enableextensions

@echo off


if "%PLAY_SILHOUETTE_SEED_HOME%"=="" (
  set "APP_HOME=%~dp0\\.."

  rem Also set the old env name for backwards compatibility
  set "PLAY_SILHOUETTE_SEED_HOME=%~dp0\\.."
) else (
  set "APP_HOME=%PLAY_SILHOUETTE_SEED_HOME%"
)

set "APP_LIB_DIR=%APP_HOME%\lib\"

rem Detect if we were double clicked, although theoretically A user could
rem manually run cmd /c
for %%x in (!cmdcmdline!) do if %%~x==/c set DOUBLECLICKED=1

rem FIRST we load the config file of extra options.
set "CFG_FILE=%APP_HOME%\PLAY_SILHOUETTE_SEED_config.txt"
set CFG_OPTS=
call :parse_config "%CFG_FILE%" CFG_OPTS

rem We use the value of the JAVA_OPTS environment variable if defined, rather than the config.
set _JAVA_OPTS=%JAVA_OPTS%
if "!_JAVA_OPTS!"=="" set _JAVA_OPTS=!CFG_OPTS!

rem We keep in _JAVA_PARAMS all -J-prefixed and -D-prefixed arguments
rem "-J" is stripped, "-D" is left as is, and everything is appended to JAVA_OPTS
set _JAVA_PARAMS=
set _APP_ARGS=

set "APP_CLASSPATH=%APP_LIB_DIR%\..\conf\;%APP_LIB_DIR%\play-silhouette-seed.play-silhouette-seed-6.0.0-sans-externalized.jar;%APP_LIB_DIR%\Slay.jar;%APP_LIB_DIR%\org.scala-lang.scala-library-2.13.1.jar;%APP_LIB_DIR%\com.typesafe.play.twirl-api_2.13-1.5.0.jar;%APP_LIB_DIR%\com.typesafe.play.play-server_2.13-2.8.0.jar;%APP_LIB_DIR%\com.typesafe.play.play-logback_2.13-2.8.0.jar;%APP_LIB_DIR%\com.typesafe.play.play-akka-http-server_2.13-2.8.0.jar;%APP_LIB_DIR%\com.typesafe.play.filters-helpers_2.13-2.8.0.jar;%APP_LIB_DIR%\com.mohiva.play-silhouette_2.13-6.1.1.jar;%APP_LIB_DIR%\com.mohiva.play-silhouette-password-bcrypt_2.13-6.1.1.jar;%APP_LIB_DIR%\com.mohiva.play-silhouette-persistence_2.13-6.1.1.jar;%APP_LIB_DIR%\com.mohiva.play-silhouette-crypto-jca_2.13-6.1.1.jar;%APP_LIB_DIR%\com.mohiva.play-silhouette-totp_2.13-6.1.1.jar;%APP_LIB_DIR%\org.webjars.webjars-play_2.13-2.8.0.jar;%APP_LIB_DIR%\org.webjars.bootstrap-4.4.1.jar;%APP_LIB_DIR%\org.webjars.jquery-3.2.1.jar;%APP_LIB_DIR%\net.codingwell.scala-guice_2.13-4.2.6.jar;%APP_LIB_DIR%\com.iheart.ficus_2.13-1.4.7.jar;%APP_LIB_DIR%\com.typesafe.play.play-mailer_2.13-7.0.1.jar;%APP_LIB_DIR%\com.typesafe.play.play-mailer-guice_2.13-7.0.1.jar;%APP_LIB_DIR%\com.enragedginger.akka-quartz-scheduler_2.13-1.8.3-akka-2.6.x.jar;%APP_LIB_DIR%\com.adrianhurt.play-bootstrap_2.13-1.5.1-P27-B4.jar;%APP_LIB_DIR%\com.typesafe.play.play-ehcache_2.13-2.8.0.jar;%APP_LIB_DIR%\com.typesafe.play.play-guice_2.13-2.8.0.jar;%APP_LIB_DIR%\org.scala-lang.modules.scala-xml_2.13-1.2.0.jar;%APP_LIB_DIR%\com.typesafe.play.play_2.13-2.8.0.jar;%APP_LIB_DIR%\ch.qos.logback.logback-classic-1.2.3.jar;%APP_LIB_DIR%\com.typesafe.play.play-streams_2.13-2.8.0.jar;%APP_LIB_DIR%\com.typesafe.akka.akka-http-core_2.13-10.1.11.jar;%APP_LIB_DIR%\com.typesafe.play.play-cache_2.13-2.8.0.jar;%APP_LIB_DIR%\com.typesafe.play.play-ws_2.13-2.7.3.jar;%APP_LIB_DIR%\com.typesafe.play.play-openid_2.13-2.7.3.jar;%APP_LIB_DIR%\com.typesafe.play.play-json-joda_2.13-2.7.4.jar;%APP_LIB_DIR%\com.atlassian.jwt.jwt-core-2.0.5.jar;%APP_LIB_DIR%\com.atlassian.jwt.jwt-api-2.0.5.jar;%APP_LIB_DIR%\org.apache.commons.commons-lang3-3.9.jar;%APP_LIB_DIR%\de.svenkubiak.jBCrypt-0.4.1.jar;%APP_LIB_DIR%\com.warrenstrange.googleauth-1.2.0.jar;%APP_LIB_DIR%\org.webjars.requirejs-2.3.6.jar;%APP_LIB_DIR%\org.webjars.webjars-locator-0.38.jar;%APP_LIB_DIR%\org.webjars.popper.js-1.14.3.jar;%APP_LIB_DIR%\com.google.inject.guice-4.2.2.jar;%APP_LIB_DIR%\com.google.guava.guava-28.1-jre.jar;%APP_LIB_DIR%\org.scala-lang.scala-reflect-2.13.1.jar;%APP_LIB_DIR%\com.google.code.findbugs.jsr305-3.0.2.jar;%APP_LIB_DIR%\com.typesafe.config-1.4.0.jar;%APP_LIB_DIR%\javax.inject.javax.inject-1.jar;%APP_LIB_DIR%\org.slf4j.slf4j-api-1.7.29.jar;%APP_LIB_DIR%\org.apache.commons.commons-email-1.5.jar;%APP_LIB_DIR%\org.quartz-scheduler.quartz-2.3.2.jar;%APP_LIB_DIR%\com.adrianhurt.play-bootstrap-core_2.13-1.5.1-P27.jar;%APP_LIB_DIR%\net.sf.ehcache.ehcache-2.10.6.jar;%APP_LIB_DIR%\org.ehcache.jcache-1.0.1.jar;%APP_LIB_DIR%\javax.cache.cache-api-1.1.1.jar;%APP_LIB_DIR%\com.google.inject.extensions.guice-assistedinject-4.2.2.jar;%APP_LIB_DIR%\com.typesafe.play.build-link-2.8.0.jar;%APP_LIB_DIR%\org.slf4j.jul-to-slf4j-1.7.29.jar;%APP_LIB_DIR%\org.slf4j.jcl-over-slf4j-1.7.29.jar;%APP_LIB_DIR%\com.typesafe.akka.akka-actor_2.13-2.6.1.jar;%APP_LIB_DIR%\com.typesafe.akka.akka-actor-typed_2.13-2.6.1.jar;%APP_LIB_DIR%\com.typesafe.akka.akka-slf4j_2.13-2.6.1.jar;%APP_LIB_DIR%\com.typesafe.akka.akka-serialization-jackson_2.13-2.6.1.jar;%APP_LIB_DIR%\com.fasterxml.jackson.core.jackson-core-2.10.1.jar;%APP_LIB_DIR%\com.fasterxml.jackson.core.jackson-annotations-2.10.1.jar;%APP_LIB_DIR%\com.fasterxml.jackson.datatype.jackson-datatype-jdk8-2.10.1.jar;%APP_LIB_DIR%\com.fasterxml.jackson.datatype.jackson-datatype-jsr310-2.10.1.jar;%APP_LIB_DIR%\com.fasterxml.jackson.core.jackson-databind-2.10.1.jar;%APP_LIB_DIR%\com.typesafe.play.play-json_2.13-2.8.1.jar;%APP_LIB_DIR%\io.jsonwebtoken.jjwt-0.9.1.jar;%APP_LIB_DIR%\jakarta.xml.bind.jakarta.xml.bind-api-2.3.2.jar;%APP_LIB_DIR%\jakarta.transaction.jakarta.transaction-api-1.3.3.jar;%APP_LIB_DIR%\org.scala-lang.modules.scala-java8-compat_2.13-0.9.0.jar;%APP_LIB_DIR%\com.typesafe.ssl-config-core_2.13-0.4.1.jar;%APP_LIB_DIR%\org.scala-lang.modules.scala-parser-combinators_2.13-1.1.2.jar;%APP_LIB_DIR%\ch.qos.logback.logback-core-1.2.3.jar;%APP_LIB_DIR%\org.reactivestreams.reactive-streams-1.0.3.jar;%APP_LIB_DIR%\com.typesafe.akka.akka-stream_2.13-2.6.1.jar;%APP_LIB_DIR%\com.typesafe.akka.akka-parsing_2.13-10.1.11.jar;%APP_LIB_DIR%\com.typesafe.play.play-ws-standalone_2.13-2.0.6.jar;%APP_LIB_DIR%\com.typesafe.play.play-ws-standalone-xml_2.13-2.0.6.jar;%APP_LIB_DIR%\com.typesafe.play.play-ws-standalone-json_2.13-2.0.6.jar;%APP_LIB_DIR%\com.typesafe.play.play-ahc-ws_2.13-2.7.3.jar;%APP_LIB_DIR%\joda-time.joda-time-2.10.5.jar;%APP_LIB_DIR%\com.nimbusds.nimbus-jose-jwt-4.41.1.jar;%APP_LIB_DIR%\commons-codec.commons-codec-1.11.jar;%APP_LIB_DIR%\org.bouncycastle.bcprov-jdk15on-1.60.jar;%APP_LIB_DIR%\org.bouncycastle.bcpkix-jdk15on-1.60.jar;%APP_LIB_DIR%\org.apache.httpcomponents.httpclient-4.5.9.jar;%APP_LIB_DIR%\org.webjars.webjars-locator-core-0.43.jar;%APP_LIB_DIR%\aopalliance.aopalliance-1.0.jar;%APP_LIB_DIR%\com.google.guava.failureaccess-1.0.1.jar;%APP_LIB_DIR%\com.google.guava.listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar;%APP_LIB_DIR%\org.checkerframework.checker-qual-2.8.1.jar;%APP_LIB_DIR%\com.google.errorprone.error_prone_annotations-2.3.2.jar;%APP_LIB_DIR%\com.google.j2objc.j2objc-annotations-1.3.jar;%APP_LIB_DIR%\org.codehaus.mojo.animal-sniffer-annotations-1.18.jar;%APP_LIB_DIR%\com.sun.mail.javax.mail-1.5.6.jar;%APP_LIB_DIR%\com.mchange.c3p0-0.9.5.4.jar;%APP_LIB_DIR%\com.mchange.mchange-commons-java-0.2.15.jar;%APP_LIB_DIR%\com.typesafe.play.play-exceptions-2.8.0.jar;%APP_LIB_DIR%\com.fasterxml.jackson.module.jackson-module-scala_2.13-2.10.1.jar;%APP_LIB_DIR%\com.fasterxml.jackson.module.jackson-module-parameter-names-2.10.1.jar;%APP_LIB_DIR%\com.fasterxml.jackson.dataformat.jackson-dataformat-cbor-2.10.1.jar;%APP_LIB_DIR%\com.typesafe.play.play-functional_2.13-2.8.1.jar;%APP_LIB_DIR%\jakarta.activation.jakarta.activation-api-1.2.1.jar;%APP_LIB_DIR%\com.typesafe.akka.akka-protobuf-v3_2.13-2.6.1.jar;%APP_LIB_DIR%\com.typesafe.play.play-ahc-ws-standalone_2.13-2.0.6.jar;%APP_LIB_DIR%\com.typesafe.play.shaded-asynchttpclient-2.0.6.jar;%APP_LIB_DIR%\com.typesafe.play.shaded-oauth-2.0.6.jar;%APP_LIB_DIR%\com.github.stephenc.jcip.jcip-annotations-1.0-1.jar;%APP_LIB_DIR%\net.minidev.json-smart-2.3.jar;%APP_LIB_DIR%\org.apache.httpcomponents.httpcore-4.4.11.jar;%APP_LIB_DIR%\commons-logging.commons-logging-1.2.jar;%APP_LIB_DIR%\io.github.classgraph.classgraph-4.8.53.jar;%APP_LIB_DIR%\javax.activation.activation-1.1.jar;%APP_LIB_DIR%\com.fasterxml.jackson.module.jackson-module-paranamer-2.10.1.jar;%APP_LIB_DIR%\com.google.protobuf.protobuf-java-3.10.0.jar;%APP_LIB_DIR%\com.typesafe.play.cachecontrol_2.13-1.1.7.jar;%APP_LIB_DIR%\net.minidev.accessors-smart-1.2.jar;%APP_LIB_DIR%\com.thoughtworks.paranamer.paranamer-2.8.jar;%APP_LIB_DIR%\org.joda.joda-convert-1.9.2.jar;%APP_LIB_DIR%\org.ow2.asm.asm-5.0.4.jar;%APP_LIB_DIR%\play-silhouette-seed.play-silhouette-seed-6.0.0-assets.jar"
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
  echo and download a valid Java JDK and install before running play-silhouette-seed.
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
"%_JAVACMD%" !_JAVA_OPTS! !PLAY_SILHOUETTE_SEED_OPTS! -cp "%APP_CLASSPATH%" %MAIN_CLASS% !_APP_ARGS!

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
