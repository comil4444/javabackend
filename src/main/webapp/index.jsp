<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<form id="form1" action="admin/product/upload.do" method="post" enctype="multipart/form-data">
	<input type="file" name="upload_file"/>
	<input type="submit" value="上传图片"/>
</form>
<form id="form2" action="admin/product/richtext_img_upload.do" method="post" enctype="multipart/form-data">
	<input type="file" name="upload_file"/>
	<input type="submit" value="富文本图片"/>
</form>
</body>
</html>