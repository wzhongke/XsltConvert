<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2016/8/31
  Time: 14:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>Who are you?</title>
    <link href="/static/css/bootstrap.min.css" rel="stylesheet" />
    <style>
        .well{
            width: 50%;
            margin: 0 auto;
            clear:both;
            position: relative;
        }
    </style>
</head>
<body>

<div class="container">
    <h1>Who are you ?</h1>
    <form action="manage" method="post" class="form-horizontal well">
        <div class="form-group">
            <label for="username" class="col-sm-3 control-label">Your caption: </label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="username" placeholder="your caption" name="name">
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-3 col-sm-10">
                <button type="submit" class="btn btn-primary">manage</button>
                <button type="reset" class="btn btn-default">reset</button>
            </div>
        </div>
    </form>
</div>
<script src="/static/js/jquery-1.11.1.min.js" type="text/javascript"></script>
<script src="/static/js/bootstrap.min.js" type="text/javascript"></script>
</body>
</html>
