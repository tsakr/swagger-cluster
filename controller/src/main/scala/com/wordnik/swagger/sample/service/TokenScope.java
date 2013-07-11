/**
 *  Copyright 2013 Wordnik, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.wordnik.swagger.sample.service;

public class TokenScope {
  public static final ThreadLocal userThreadLocal = new ThreadLocal();

  public static void setUserId(Long userId) {
    userThreadLocal.set(userId);
  }

  public static Long getUserId() {
    return (Long)userThreadLocal.get();
  }

  public static void unsetUserId() {
    userThreadLocal.remove();
  }
}
