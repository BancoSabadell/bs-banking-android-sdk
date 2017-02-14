/*
 * Copyright 2016 Victor Albertos
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

package com.sabadell.bs_banking.internal.net;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Prettify server error message.
 */
public final class ErrorAdapter {
  private final Gson gson;

  public ErrorAdapter() {
    this.gson = new Gson();
  }

  public String adapt(String json) {
    try {
      return gson
          .fromJson(json, ResponseError.class)
          .message;
    } catch (JsonSyntaxException e) {
      return json;
    }
  }

  private static class ResponseError {
    private final String message;

    public ResponseError(String message) {
      this.message = message;
    }
  }
}
