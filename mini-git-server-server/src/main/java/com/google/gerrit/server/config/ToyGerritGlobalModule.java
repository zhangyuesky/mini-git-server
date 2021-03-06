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

package com.google.gerrit.server.config;

import com.google.gerrit.lifecycle.LifecycleListener;
import com.google.gerrit.lifecycle.LifecycleModule;
import com.google.gerrit.server.AnonymousUser;
import com.google.gerrit.server.GerritPersonIdent;
import com.google.gerrit.server.GerritPersonIdentProvider;
import com.google.gerrit.server.cache.CachePool;
import com.google.gerrit.server.git.LocalDiskRepositoryManager;
import com.google.gerrit.server.git.WorkQueue;
import com.google.gerrit.server.util.IdGenerator;
import com.google.inject.Inject;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.eclipse.jgit.lib.Config;
import org.eclipse.jgit.lib.PersonIdent;

import java.util.Properties;


/** Starts global state with standard dependencies. */
public class ToyGerritGlobalModule extends FactoryModule {
  // private final AuthType loginType;

  public static class VelocityLifecycle implements LifecycleListener {
    private final SitePaths site;

    @Inject
    VelocityLifecycle(final SitePaths site) {
      this.site = site;
    }

    @Override
    public void start() {
      String rl = "resource.loader";
      String pkg = "org.apache.velocity.runtime.resource.loader";
      Properties p = new Properties();

      p.setProperty(rl, "file, class");
      p.setProperty("file." + rl + ".class", pkg + ".FileResourceLoader");
      p.setProperty("file." + rl + ".path", site.mail_dir.getAbsolutePath());
      p.setProperty("class." + rl + ".class", pkg + ".ClasspathResourceLoader");
      p.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,
              "org.apache.velocity.runtime.log.SimpleLog4JLogSystem" );
      p.setProperty("runtime.log.logsystem.log4j.category", "velocity");

      try {
        Velocity.init(p);
      } catch(Exception e) {
        throw new RuntimeException(e);
      }
    }

    @Override
    public void stop() {
    }
  }

  @Inject
  ToyGerritGlobalModule(//final AuthConfig authConfig,
						@GerritServerConfig final Config config) {
    //loginType = authConfig.getAuthType();
  }

  @Override
  protected void configure() {
//    switch (loginType) {
//      case HTTP_LDAP:
//      case LDAP:
//      case LDAP_BIND:
//      case CLIENT_SSL_CERT_LDAP:
//        install(new LdapModule());
//        break;
//
//      default:
//        bind(Realm.class).to(DefaultRealm.class);
//        break;
//    }

//    bind(ApprovalTypes.class).toProvider(ApprovalTypesProvider.class).in(
//        SINGLETON);
//    bind(EmailExpander.class).toProvider(EmailExpanderProvider.class).in(
//        SINGLETON);
    bind(AnonymousUser.class);

    bind(PersonIdent.class).annotatedWith(GerritPersonIdent.class).toProvider(
        GerritPersonIdentProvider.class);

    bind(IdGenerator.class);
    bind(CachePool.class);
//    install(ProjectCacheImpl.module());

//
//    bind(GitRepositoryManager.class).to(LocalDiskRepositoryManager.class);
//    bind(WorkQueue.class);
//    bind(ToolsCatalog.class);
//    bind(EventFactory.class);
//    bind(TransferConfig.class);
//
//    bind(ReplicationQueue.class).to(PushReplication.class).in(SINGLETON);
//    factory(PushAllProjectsOp.Factory.class);
//
//    bind(MergeQueue.class).to(ChangeMergeQueue.class).in(SINGLETON);
//    factory(ReloadSubmitQueueOp.Factory.class);
//
//    bind(FromAddressGenerator.class).toProvider(
//        FromAddressGeneratorProvider.class).in(SINGLETON);
//    bind(EmailSender.class).to(SmtpEmailSender.class).in(SINGLETON);
//
//    bind(PatchSetInfoFactory.class);
//    bind(IdentifiedUser.GenericFactory.class).in(SINGLETON);
//    bind(ChangeControl.GenericFactory.class);
//    bind(ProjectControl.GenericFactory.class);
//    factory(FunctionState.Factory.class);
//    factory(ReplicationUser.Factory.class);

    install(new LifecycleModule() {
      @Override
      protected void configure() {
        listener().to(LocalDiskRepositoryManager.Lifecycle.class);
        listener().to(CachePool.Lifecycle.class);
        listener().to(WorkQueue.Lifecycle.class);
        listener().to(VelocityLifecycle.class);
      }
    });
  }
}
