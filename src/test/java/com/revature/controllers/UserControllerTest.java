package com.revature.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.revature.services.UserService;
import com.revature.entities.User;
import com.revature.exceptions.UniquenessViolationException;
import com.revature.utils.JavalinUtil;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.Javalin;


@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
   
    @Mock
    UserService userServiceMock;

    @InjectMocks
    UserController userController;

    

    @Test
    public void createSuccessTest() throws UniquenessViolationException{
        Context ctx = mock(Context.class);
        User user = new User();
        when(ctx.bodyAsClass(User.class)).thenReturn(user);
        when(userServiceMock.create(any())).thenReturn(user);
        userController.create(ctx);
    }
   
    @Test
    public void createFailedTest() throws UniquenessViolationException{
        Context ctx = mock(Context.class);
        User user = new User();
        when(ctx.bodyAsClass(User.class)).thenReturn(user);
        when(userServiceMock.create(any())).thenThrow(UniquenessViolationException.class);
        Exception exception = assertThrows(UniquenessViolationException.class, () -> {
            userController.create(ctx);
        });
    }

    @Test
    public void handleUniqenessViolationExceptionTest(){
        Context ctx = mock(Context.class);
        UniquenessViolationException e = mock(UniquenessViolationException.class);
        userController.handleUniqenessViolationException(e, ctx);
    }


}
   //Stuff to test Javalin server, not the User Controller
    /* 
    HttpClient client;

    @BeforeEach
    public void setUpTests(){
        Javalin server = JavalinUtil.startServer();
        client = HttpClient.newBuilder().build();

    }


    @Test
    public void createSuccessTest() throws IOException, InterruptedException{
        String requestBody = "{\"username\": \"pundo2\"," + 
                                "\"email\": \"robert308@revature.net\"," + 
                                "\"password\": \"hiddenPassword\"," + 
                                "\"firstName\": \"Robert\"," + 
                                "\"lastName\": \"Perry\"}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("/users"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody)) 
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200,response.statusCode());
    } */

