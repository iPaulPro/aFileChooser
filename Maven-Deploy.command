#!/opt/local/bin/zsh
########################################################### {{{1 ###########
#   Copyright © 2005 … 2013  Martin Krischik
#
#   This program is free software; you can redistribute it and/or
#   modify it under the terms of the GNU General Public License
#   as published by the Free Software Foundation; either version 2
#   of the License, or (at your option) any later version.
#
#   This program is distributed in the hope that it will be useful,
#   but WITHOUT ANY WARRANTY; without even the implied warranty of
#   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#   GNU General Public License for more details.
#
#   You should have received a copy of the GNU General Public License
#   along with this program; if not, write to the Free Software
#   Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
############################################################################
#  $Author: krischik $
#  $Revision: 4694 $
#  $Date: 2012-03-29 20:47:27 +0200 (Do, 29. Mär 2012) $
#  $Id: Start-Vim.command 4694 2012-03-29 18:47:27Z krischik $
#  $HeadURL: https://uiq3.svn.sourceforge.net/svnroot/uiq3/trunk/Java/Utilities/Start-Vim.command $
########################################################### }}}1 ###########

local Download_Server="krischik,uiq3@shell.sourceforge.net"
local Scala_Library="${WORK}/Repositories/Local/net/sourceforge/uiq3/Calculator-Script/6.2.0/Calculator-Script-6.2.0.jar"

ssh-add					\
    /Users/martin/.ssh/id_rsa		\
    /Users/martin/.ssh/id_dsa		\
    /Users/martin/.ssh/Martin_Krischik_SF

scala -classpath ${Scala_Library} -save Maven-Deploy.cmd

pushd "/Work/HomePage/uiq3/htdocs"
    rsync						\
	--archive					\
	--delete					\
	--verbose					\
	--keep-dirlinks					\
	"Repository"					\
	"krischik,uiq3@web.sourceforge.net:htdocs"
popd
# vim: set wrap tabstop=8 shiftwidth=4 softtabstop=4 noexpandtab :
# vim: set textwidth=0 filetype=zsh foldmethod=marker nospell :
