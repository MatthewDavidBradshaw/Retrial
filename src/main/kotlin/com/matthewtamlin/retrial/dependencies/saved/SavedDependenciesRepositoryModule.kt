/*
 * Copyright 2018 Matthew Tamlin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.matthewtamlin.retrial.dependencies.saved

import dagger.Module
import dagger.Provides
import java.io.File
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
class SavedDependenciesRepositoryModule {
  @Provides
  @Singleton
  fun provideSavedDependenciesRepository(
      @DependencyDatabaseFile file: File,
      savedDependencySerialiser: SavedDependencySerialiser
  ): SavedDependenciesRepository {

    return FileBasedSavedDependenciesRepository(file, savedDependencySerialiser)
  }
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class DependencyDatabaseFile