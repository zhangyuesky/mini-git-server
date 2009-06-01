// Copyright (C) 2008 The Android Open Source Project
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.gerrit.pgm;

import com.google.gerrit.client.rpc.Common;
import com.google.gerrit.git.WorkQueue;
import com.google.gerrit.server.GerritServer;
import com.google.gwtjsonrpc.server.XsrfException;
import com.google.gwtorm.client.OrmException;

/**
 * Creates the Gerrit 2 database schema.
 */
public class CreateSchema {
  public static void main(final String[] argv) throws OrmException,
      XsrfException {
    try {
      mainImpl(argv);
    } finally {
      WorkQueue.terminate();
    }
  }

  private static void mainImpl(final String[] argv) throws OrmException,
      XsrfException {
    GerritServer.getInstance(false);
    Common.getSchemaFactory().open().close();
    System.out.println("Gerrit2 schema initialized");
  }
}
