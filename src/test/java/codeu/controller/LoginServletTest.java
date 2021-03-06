// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package codeu.controller;

import codeu.model.data.User;
import codeu.model.store.basic.UserStore;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mindrot.jbcrypt.BCrypt;

public class LoginServletTest {

  private LoginServlet loginServlet;
  private HttpServletRequest mockRequest;
  private HttpServletResponse mockResponse;
  private RequestDispatcher mockRequestDispatcher;

  @Before
  public void setup() {
    loginServlet = new LoginServlet();
    mockRequest = Mockito.mock(HttpServletRequest.class);
    mockResponse = Mockito.mock(HttpServletResponse.class);
    mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
    Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/login.jsp"))
        .thenReturn(mockRequestDispatcher);
  }

  @Test
  public void testDoGet() throws IOException, ServletException {
    loginServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }
  
  @Test
  public void testDoPost_InvalidUsername() throws IOException, ServletException {
    Mockito.when(mockRequest.getParameter("username")).thenReturn("test username");

    UserStore mockUserStore = Mockito.mock(UserStore.class);
    loginServlet.setUserStore(mockUserStore);
    Mockito.when(mockUserStore.isUserRegistered("test username")).thenReturn(false);

    loginServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockRequest)
        .setAttribute("error", "That username was not found.");
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }


  @Test
  public void testDoPost_InvalidPassword() throws IOException, ServletException {
    Mockito.when(mockRequest.getParameter("username")).thenReturn("test username");
    Mockito.when(mockRequest.getParameter(BCrypt.hashpw("password", BCrypt.gensalt()))).thenReturn(BCrypt.hashpw("test password", BCrypt.gensalt()));

    UserStore mockUserStore = Mockito.mock(UserStore.class);
    loginServlet.setUserStore(mockUserStore);
    Mockito.when(mockUserStore.isUserRegistered("test username")).thenReturn(true);
    
    User mockUser = Mockito.mock(User.class);
    Mockito.when(mockUserStore.getUser("test username")).thenReturn(mockUser);
    Mockito.when(mockUser.getPassword()).thenReturn(BCrypt.hashpw("invalid password", BCrypt.gensalt()));

    loginServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockRequest)
        .setAttribute("error", "Invalid password.");
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }
  

  @Test
  public void testDoPost_ValidUser() throws IOException, ServletException {
    Mockito.when(mockRequest.getParameter("username")).thenReturn("test username");
    Mockito.when(mockRequest.getParameter(BCrypt.hashpw("password", BCrypt.gensalt()))).thenReturn(BCrypt.hashpw("test password", BCrypt.gensalt()));

    UserStore mockUserStore = Mockito.mock(UserStore.class);
    loginServlet.setUserStore(mockUserStore);
    Mockito.when(mockUserStore.isUserRegistered("test username")).thenReturn(true);
    
    User mockUser = Mockito.mock(User.class);
    Mockito.when(mockUserStore.getUser("test username")).thenReturn(mockUser);
    Mockito.when(mockUser.getPassword()).thenReturn(BCrypt.hashpw("test password", BCrypt.gensalt()));

    HttpSession mockSession = Mockito.mock(HttpSession.class);
    Mockito.when(mockRequest.getSession()).thenReturn(mockSession);

    loginServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockUserStore, Mockito.never()).addUser(Mockito.any(User.class));

    mockSession.setAttribute("user", "test username");
    mockResponse.sendRedirect("/conversations");

    Mockito.verify(mockSession).setAttribute("user", "test username");
    Mockito.verify(mockResponse).sendRedirect("/conversations");
  }
}
