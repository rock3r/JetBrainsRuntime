#
# Copyright (c) 2026, JetBrains s.r.o.. All rights reserved.
# DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
#
# This code is free software; you can redistribute it and/or modify it
# under the terms of the GNU General Public License version 2 only, as
# published by the Free Software Foundation.  Oracle designates this
# particular file as subject to the "Classpath" exception as provided
# by Oracle in the LICENSE file that accompanied this code.
#
# This code is distributed in the hope that it will be useful, but WITHOUT
# ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
# FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
# version 2 for more details (a copy is included in the LICENSE file that
# accompanied this code).
#
# You should have received a copy of the GNU General Public License version
# 2 along with this work; if not, write to the Free Software Foundation,
# Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
#
# Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
# or visit www.oracle.com if you need additional information or have any
# questions.
#

################################################################################
# Setup experimental Skia interop for Java2D/Compose surface sharing
################################################################################
AC_DEFUN_ONCE([LIB_SETUP_SKIA_INTEROP],
[
  AC_ARG_WITH(skia-interop, [AS_HELP_STRING([--with-skia-interop],
    [enable experimental JBR Skia interop using bundled sources or a source path (bundled, no, or path) @<:@no@:>@])])

  SKIA_INTEROP_ENABLED=false
  SKIA_INTEROP_PATH=

  if test "x${with_skia_interop}" != x && test "x${with_skia_interop}" != xno; then
    if test "x$OPENJDK_TARGET_OS" != xmacosx; then
      AC_MSG_ERROR([JBR Skia interop is currently only supported on macOS])
    fi

    SKIA_INTEROP_ENABLED=true
    if test "x${with_skia_interop}" = xyes || test "x${with_skia_interop}" = xbundled; then
      SKIA_INTEROP_PATH=bundled
    else
      SKIA_INTEROP_PATH="${with_skia_interop}"
      AC_MSG_CHECKING([for Skia interop source path ${SKIA_INTEROP_PATH}])
      if test -d "${SKIA_INTEROP_PATH}"; then
        AC_MSG_RESULT([yes])
      else
        AC_MSG_RESULT([no])
        AC_MSG_ERROR([Can't find Skia interop source path '${SKIA_INTEROP_PATH}'])
      fi
    fi
  fi

  AC_SUBST(SKIA_INTEROP_ENABLED)
  AC_SUBST(SKIA_INTEROP_PATH)
])
