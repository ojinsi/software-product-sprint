// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  private List<String> randomFacts;

  @Override
  public void init() {
    randomFacts = new ArrayList<>();
    randomFacts.add("I love photography, but haven't been able to do much in college");
    randomFacts.add("I'm a huge Roger Federer fan and cried when he lost at the Wimbledon 2019 Finals");
    randomFacts.add("I have traveled to over 35 countries"); 
    randomFacts.add("I used to be heavily involved in politics and met state politicans to lobby for bills"); 
    randomFacts.add("My favorite band is the Beatles");
  }
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String json = convertToJsonUsingGson(randomFacts);
    response.setContentType("text/html;");
    response.getWriter().println(json);
  }

  private String convertToJsonUsingGson(List<String> randomFacts) {
    Gson gson = new Gson();
    String json = gson.toJson(randomFacts);
    return json;
  }
}
