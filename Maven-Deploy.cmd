::#!
::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: {{{1 :::::::^::::
:: Copyright © 2013 Martin Krischik
::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
:: $Author$
:: $Revision$
:: $Date$
:: $Id$
:: $HeadURL$
::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: }}}1 :::::::::::
@ECHO OFF

SETLOCAL
    SET PATH=%PATH%;C:\opt\Git\bin
    SET PATH=%PATH%;C:\opt\Scala\2.10.0\bin
    SET Scala_Library="${WORK}/Repositories/Local/net/sourceforge/uiq3/Calculator-Script/${CALCULATOR_VERSION}/Calculator-Script-${CALCULATOR_VERSION}.jar"

    CALL scala -classpath %Scala_Library% -save %~f0 %*
    POPD
ENDLOCAL

GOTO :eof
::!#

import scala.sys.process._
import net.sourceforge.uiq3.Maven.mvn
import net.sourceforge.uiq3.Shell.Err_Exit_Call

val Maven_Deploy = System.getenv ("MAVEN_DEPLOY")
val Project_Name = System.getenv ("PROJECT_NAME")
val Maven_Name	 = Project_Name +" Maven Repository"

Err_Exit_Call (mvn ::: "--activate-profiles" :: "release" :: "install"		:: Nil)
Err_Exit_Call (mvn ::: "--activate-profiles" :: "release" :: "javadoc:javadoc"	:: Nil)
Err_Exit_Call (mvn ::: "--activate-profiles" :: "release" :: "source:jar"	:: Nil)
Err_Exit_Call (mvn :::
    "--define" :: "repo.id="   + Project_Name ::
    "--define" :: "repo.name=" + Maven_Name   ::
    "--define" :: "repo.url="  + Maven_Deploy ::
    "deploy"   :: Nil )

// vim: set wrap tabstop=8 shiftwidth=4 softtabstop=4 noexpandtab :
// vim: set textwidth=0 filetype=scala foldmethod=marker nospell :
