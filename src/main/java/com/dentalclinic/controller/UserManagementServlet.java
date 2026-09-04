package com.dentalclinic.controller;

import com.dentalclinic.dao.UserDAO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(
    name = "UserManagementServlet",
    urlPatterns = {"/api/users"}
)
public class UserManagementServlet extends HttpServlet {

    private UserDAO userDAO;
    private final Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }



    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        setJsonResponse(response);

        try {

            List<Map<String, Object>> users =
                    userDAO.getAllUsers();

            Map<String, Object> result =
                    new HashMap<>();

            result.put("success", true);
            result.put("count", users.size());
            result.put("users", users);

            response.setStatus(
                    HttpServletResponse.SC_OK
            );

            response.getWriter().write(
                    gson.toJson(result)
            );

        } catch (Exception e) {

            sendError(response, e);
        }
    }


    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        setJsonResponse(response);

        try {

            String body =
                    readRequestBody(request);

            JsonObject json =
                    JsonParser.parseString(body)
                            .getAsJsonObject();

            String username =
                    getString(json, "username");

            String password =
                    getString(json, "password");

            String fullName =
                    getString(json, "fullName");

            String email =
                    getString(json, "email");

            String role =
                    getString(json, "role");

            String status = "ACTIVE";

  if (json.has("status") && !json.get("status").isJsonNull()) {
    status = json.get("status").getAsString();
  }

            if (username == null ||
                username.isEmpty() ||
                password == null ||
                password.isEmpty() ||
                fullName == null ||
                fullName.isEmpty() ||
                role == null ||
                role.isEmpty()) {

                response.setStatus(
                        HttpServletResponse.SC_BAD_REQUEST
                );

                response.getWriter().write(
                        gson.toJson(
                                createResponse(
                                        false,
                                        "Username, password, full name and role are required"
                                )
                        )
                );

                return;
            }

            
            boolean success =
                    userDAO.addUser(
                            username,
                            password,
                            fullName,
                            email,
                            role,
                            status
                    );

            if (success) {

                response.setStatus(
                        HttpServletResponse.SC_CREATED
                );

                response.getWriter().write(
                        gson.toJson(
                                createResponse(
                                        true,
                                        "User added successfully"
                                )
                        )
                );

            } else {

                response.setStatus(
                        HttpServletResponse.SC_BAD_REQUEST
                );

                response.getWriter().write(
                        gson.toJson(
                                createResponse(
                                        false,
                                        "Unable to add user"
                                )
                        )
                );
            }

        } catch (Exception e) {

            sendError(response, e);
        }
    }

    @Override
    protected void doPut(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        setJsonResponse(response);

        try {

            String idParam =
                    request.getParameter("id");

            if (idParam == null ||
                idParam.trim().isEmpty()) {

                response.setStatus(
                        HttpServletResponse.SC_BAD_REQUEST
                );

                response.getWriter().write(
                        gson.toJson(
                                createResponse(
                                        false,
                                        "User ID is required"
                                )
                        )
                );

                return;
            }

            int userId =
                    Integer.parseInt(idParam);

            String body =
                    readRequestBody(request);

            JsonObject json =
                    JsonParser.parseString(body)
                            .getAsJsonObject();

            String username =
                    getString(json, "username");

            String fullName =
                    getString(json, "fullName");

            String email =
                    getString(json, "email");

            String role =
                    getString(json, "role");

            String status = "ACTIVE";

if (json.has("status") && !json.get("status").isJsonNull()) {
    status = json.get("status").getAsString();
}

            boolean success =
                    userDAO.updateUser(
                            userId,
                            username,
                            fullName,
                            email,
                            role,
                            status
                    );

            if (success) {

                response.setStatus(
                        HttpServletResponse.SC_OK
                );

                response.getWriter().write(
                        gson.toJson(
                                createResponse(
                                        true,
                                        "User updated successfully"
                                )
                        )
                );

            } else {

                response.setStatus(
                        HttpServletResponse.SC_NOT_FOUND
                );

                response.getWriter().write(
                        gson.toJson(
                                createResponse(
                                        false,
                                        "User not found"
                                )
                        )
                );
            }

        } catch (NumberFormatException e) {

            response.setStatus(
                    HttpServletResponse.SC_BAD_REQUEST
            );

            response.getWriter().write(
                    gson.toJson(
                            createResponse(
                                    false,
                                    "Invalid user ID"
                            )
                    )
            );

        } catch (Exception e) {

            sendError(response, e);
        }
    }

    @Override
    protected void doDelete(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        setJsonResponse(response);

        try {

            String idParam =
                    request.getParameter("id");

            if (idParam == null ||
                idParam.trim().isEmpty()) {

                response.setStatus(
                        HttpServletResponse.SC_BAD_REQUEST
                );

                response.getWriter().write(
                        gson.toJson(
                                createResponse(
                                        false,
                                        "User ID is required"
                                )
                        )
                );

                return;
            }

            int userId =
                    Integer.parseInt(idParam);

           
            if (userId == 1) {

                response.setStatus(
                        HttpServletResponse.SC_FORBIDDEN
                );

                response.getWriter().write(
                        gson.toJson(
                                createResponse(
                                        false,
                                        "Main administrator cannot be deleted"
                                )
                        )
                );

                return;
            }

            boolean success =
                    userDAO.deleteUser(userId);

            if (success) {

                response.setStatus(
                        HttpServletResponse.SC_OK
                );

                response.getWriter().write(
                        gson.toJson(
                                createResponse(
                                        true,
                                        "User deleted successfully"
                                )
                        )
                );

            } else {

                response.setStatus(
                        HttpServletResponse.SC_NOT_FOUND
                );

                response.getWriter().write(
                        gson.toJson(
                                createResponse(
                                        false,
                                        "User not found"
                                )
                        )
                );
            }

        } catch (NumberFormatException e) {

            response.setStatus(
                    HttpServletResponse.SC_BAD_REQUEST
            );

            response.getWriter().write(
                    gson.toJson(
                            createResponse(
                                    false,
                                    "Invalid user ID"
                            )
                    )
            );

        } catch (Exception e) {

            sendError(response, e);
        }
    }



    private String readRequestBody(
            HttpServletRequest request)
            throws IOException {

        StringBuilder body =
                new StringBuilder();

        BufferedReader reader =
                request.getReader();

        String line;

        while ((line = reader.readLine()) != null) {
            body.append(line);
        }

        return body.toString();
    }



    private String getString(
            JsonObject json,
            String field) {

        if (!json.has(field) ||
            json.get(field).isJsonNull()) {

            return null;
        }

        return json.get(field).getAsString();
    }



    private Map<String, Object> createResponse(
            boolean success,
            String message) {

        Map<String, Object> result =
                new HashMap<>();

        result.put("success", success);
        result.put("message", message);

        return result;
    }



    private void sendError(
            HttpServletResponse response,
            Exception e)
            throws IOException {

        e.printStackTrace();

        response.setStatus(
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR
        );

        Map<String, Object> error =
                new HashMap<>();

        error.put("success", false);
        error.put(
                "error",
                "User operation failed"
        );

        error.put(
                "message",
                e.getMessage() == null
                        ? "Unknown error"
                        : e.getMessage()
        );

        response.getWriter().write(
                gson.toJson(error)
        );
    }



    private void setJsonResponse(
            HttpServletResponse response) {

        response.setContentType(
                "application/json"
        );

        response.setCharacterEncoding(
                "UTF-8"
        );
    }
}