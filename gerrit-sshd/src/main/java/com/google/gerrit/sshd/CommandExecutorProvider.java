// Copyright (C) 2009 The Android Open Source Project
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

package com.google.gerrit.sshd;

import com.google.gerrit.server.CurrentUser;
import com.google.gerrit.server.config.GerritServerConfig;
import com.google.gerrit.server.git.WorkQueue;
import com.google.inject.Inject;
import com.google.inject.Provider;

import org.eclipse.jgit.lib.Config;

import java.util.concurrent.ThreadFactory;

class CommandExecutorProvider implements Provider<WorkQueue.Executor> {

  private final QueueProvider queues;
  private final CurrentUser user;

  @Inject
  CommandExecutorProvider(QueueProvider queues,
      CurrentUser user) {
    this.queues = queues;
    this.user = user;
  }

  @Override
  public WorkQueue.Executor get() {
    WorkQueue.Executor executor;

      executor = queues.getInteractiveQueue();
    
    return executor;
  }
}
