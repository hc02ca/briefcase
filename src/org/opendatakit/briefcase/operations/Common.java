/*
 * Copyright (C) 2018 Nafundi
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.opendatakit.briefcase.operations;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.opendatakit.briefcase.model.BriefcasePreferences;
import org.opendatakit.briefcase.reused.UncheckedFiles;
import org.opendatakit.briefcase.reused.http.RequestBuilder;
import org.opendatakit.common.cli.Param;

public class Common {
  public static final Param<String> DEPRECATED_AGGREGATE_SERVER = Param.arg("url", "aggregate_url", "Aggregate server URL");
  public static final Param<URL> SERVER_URL = Param.arg("U", "odk_url", "ODK Server URL", RequestBuilder::url);
  public static final Param<Integer> MAX_HTTP_CONNECTIONS = Param.arg("mhc", "max_http_connections", "Maximum simultaneous HTTP connections (defaults to 8)", Integer::parseInt);
  static final Param<Path> STORAGE_DIR = Param.arg("sd", "storage_directory", "Briefcase storage directory", Common::absolutePath);
  static final Param<String> FORM_ID = Param.arg("id", "form_id", "Form ID");
  static final Param<Integer> PROJECT_ID = Param.arg("pid", "project_id", "ODK Project ID number", Integer::parseInt);
  static final Param<String> CREDENTIALS_USERNAME = Param.arg("u", "odk_username", "ODK Username");
  static final Param<String> CREDENTIALS_EMAIL = Param.arg("E", "odk_email", "ODK Email");
  static final Param<String> CREDENTIALS_PASSWORD = Param.arg("p", "odk_password", "ODK Password");

  static Path getOrCreateBriefcaseDir(Path storageDir) {
    Path briefcaseDir = BriefcasePreferences.buildBriefcaseDir(storageDir);
    if (!Files.exists(briefcaseDir)) {
      System.err.println("The directory " + briefcaseDir.toString() + " doesn't exist. Creating it");
      UncheckedFiles.createBriefcaseDir(briefcaseDir);
    }
    return briefcaseDir;
  }

  /**
   * Returns an absolute path to the given string path, using the user.dir property
   * to resolve input relative paths if necessary
   */
  static Path absolutePath(String path) {
    Path storageDir = Paths.get(path);
    return storageDir.isAbsolute()
        ? storageDir
        : Paths.get(System.getProperty("user.dir")).resolve(storageDir);
  }
}
