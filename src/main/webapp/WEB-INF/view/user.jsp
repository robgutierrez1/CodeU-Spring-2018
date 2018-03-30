<%--
  Copyright 2017 Google Inc.
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
     http://www.apache.org/licenses/LICENSE-2.0
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
--%>
<%@ page import="java.util.List" %>
<%@ page import="codeu.model.data.User" %>
<%@ page import="codeu.model.store.basic.UserStore" %>
<%
User user = (User) request.getAttribute("user");
%>

<!DOCTYPE html>
<html>
<head>
  <title><%= user.getName() %></title>
  <link rel="stylesheet" href="/css/main.css" type="text/css">
  <style>
    #about {
      background-color: white;
      height: 150px;
    }
  </style>

</head>
<body>
  <%@ include file="/navbar.html" %>
      
  <div id="container">
    <div
      style="width:75%; margin-left:auto; margin-right:auto; margin-top: 50px;">

      <h1><%= user.getName() %></h1>
      <h2>This is my profile :) </h2>

      <p>Something about me</p>
      <div id="about">
          <img src="http://jconlineimagem.ne10.uol.com.br/imagem/noticia/2017/08/25/normal/5189d69f07b630e3b36dc60922eeddb4.jpg">
      </div>
    </div>
  </div>
  

</body>
</html>
