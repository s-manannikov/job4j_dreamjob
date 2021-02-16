<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="ru.job4j.store.PsqlStore" %>
<%@ page import="ru.job4j.model.Post" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
            integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
            integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
            integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>

    <title>Dream Job</title>

    <script>
        function validate() {
            const message = 'Please enter the name!';
            const name = $('#name').val();
            if (name === '') {
                alert(message);
                return false;
            }
        }
    </script>

</head>
<body>
<div class="container pt-3">
    <div class="row">
        <ul class="nav">
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()%>/index.do">Main</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()%>/posts.do">Jobs</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()%>/candidates.do">Candidates</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()%>/candidate/edit.do">Add candidate</a>
            </li>
            <li class="nav-item">
                <%if (request.getSession().getAttribute("user") == null) {%>
                <a class="nav-link" href="<%=request.getContextPath()%>/login.jsp">
                    Login
                        <%} else {%>
                    <a class="nav-link" href="<%=request.getContextPath()%>/logout"><c:out value="${user.name}"/> | Logout
                        <% } %>
                    </a>
            </li>
        </ul>
    </div>
<%
    String id = request.getParameter("id");
    Post post = new Post(0, "");
    if (id != null) {
        post = PsqlStore.instOf().findPostById(Integer.parseInt(id));
    }
%>
    <div class="row">
        <div class="card" style="width: 100%">
            <div class="card-header">
                <% if (id == null) { %>
                New Job
                <% } else { %>
                Edit Job
                <% } %>
            </div>
            <div class="card-body">
                <form action="<%=request.getContextPath()%>/posts.do?id=<%=post.getId()%>" method="post">
                    <div class="form-group">
                        <label>Job name</label>
                        <input type="text" class="form-control" name="name" id="name" value="<%=post.getName()%>">
                    </div>
                    <div class="form-group">
                    <button type="submit" class="btn btn-primary" onclick="return validate()">Save</button>
                    <% if (id != null) { %>
                    <a href="${pageContext.request.contextPath}/pdelete?id=<%=post.getId()%>" class="btn btn-danger">Delete</a>
                    <% } %>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>