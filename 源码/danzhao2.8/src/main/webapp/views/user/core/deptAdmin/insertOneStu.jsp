<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="static/css/student.css" />
    <script type="text/javascript" src="static/js/deptAdmin/insertOneStu.js"></script>

  </head>
  
  <body>
    <div class="centercontent">
        <div class="pageheader">
            <h1 class="pagetitle">学生信息界面</h1>
            <span class="pagedesc">学生信息添加</span>
        </div>
     <div class="contentwrapper_1">
        <form id="insertStuInfoForm" onsubmit="return false;">
        <input type="hidden" name="deptid" id="deptid" value="${sessionScope.user.deptid }">
        <div class="col-12">
			<div class="col-4">
			    <div class="col-9">
				   <label class="leb">考生号:</label>	
			     <input type="text" name="stunumber" check="required num" >
			    </div>
			</div>
			<div class="col-4">
			  <div class="col-9">
				<label class="leb">姓名:</label>	
			    <input type="text" name="stuname" check="required chinese">
			    </div>
			</div>
			<div class="col-4">
			  <div class="col-9">
				<label class="leb">性别:</label>	
			    <input type="radio" name="stusex" value="男">男
     				&nbsp;&nbsp;
     				<input type="radio" name="stusex" value="女">女
			    </div>
			</div>
		</div>
		
		<div class="col-12">	
			<div class="col-4">
			   <div class="col-9">
				  <label class="leb">联系电话1:</label>	
			     <input type="text" name="stutel1" check="required phone" maxlength="11">
			  </div>
			</div>
			<div class="col-4">
			  <div class="col-9">
				<label class="leb">联系电话2:</label>	
			    <input type="text" name="stutel2" check="phone" maxlength="11">
			   </div>
			</div>
			<div class="col-4">
			  <div class="col-9">
				<label class="leb">最新联系电话:</label>	
			    <input type="text" name="stutel3" check="phone" maxlength="11">
			    </div>
			</div>
		</div>
		
		<div class="col-12">
			<div class="col-4">
			  <div class="col-9">
				<label class="leb">科类:</label>	
			    <input type="text" name="ktype" check="chinese">
			   </div>
			</div>
			<div class="col-4">
			  <div class="col-9">
				<label class="leb">职高对口科类:</label>	
			    <input type="text" name="zgtype" check="chinese">
			    </div>
			</div>
			<div class="col-4">
			  <div class="col-9">
				<label class="leb">毕业学校:</label>	
			    <input type="text" name="schoolname" check="chinese">
			    </div>
			</div>
		</div>
		
		<div class="col-12">
			
			<div class="col-4">
			  <div class="col-9">
				<label class="leb">所属专业:</label>	
			    <select name="profid">
     				<option>软件技术</option>
     			</select>
     			</div>
			</div>
			<div class="col-4">
			  <div class="col-9">
				<label class="leb">考场号:</label>	
			    <select name="erid">
     				<option>1504</option>
     			</select>
     			</div>
			</div>
			<div class="col-4">
			  <div class="col-9">
				<label class="leb">所属系部:</label>	
			    <select name="testtime">
                        <option value="1">上午</option>
                        <option value="2">下午</option>
                        <option value="3">晚上</option>
                    </select>
     			</div>
			</div>
		</div>
		
		<div class="col-12">	
			<div class="col-4">
			  <div class="col-9">
				<label class="leb">准考证号:</label>	
			    <input type="text" name="examnumber"  check="required num">
			    </div>
			</div>
			<div class="col-4">
			  <div class="col-9">
				<label class="leb">身份证号:</label>	
			    <input type="text" name="idcard" check="idcard" maxlength="18">
			    </div>
			</div>
		</div>
		
		<div class="col-12" align="center">
			<input type="submit" value="&nbsp;添 &nbsp;&nbsp;加&nbsp;">
		</div>
		<!-- 
        <table class="student_table">
            <tr>
                <td>考生号</td>
                <td><input type="text" name="stunumber" check="required num" ></td>
                <td>姓名</td>
                <td><input type="text" name="stuname" maxlength="8" check="required chinese"></td>
                <td>性别</td>
                <td>
                    <input type="radio" name="stusex" value="男" checked="checked">男
                    &nbsp;&nbsp;
                    <input type="radio" name="stusex" value="女">女
                </td>
            </tr>
            <tr>
                <td>联系电话1</td>
                <td><input type="text" name="stutel1" maxlength="11"></td>
                <td>联系电话2</td>
                <td><input type="text" name="stutel2" maxlength="11"></td>
                <td>最新联系电话</td>
                <td><input type="text" name="stutel3" maxlength="11"></td>
            </tr>
            <tr>
                <td>普高科类</td>
                <td><input type="text" name="ktype"></td>
                <td>职高对口科类</td>
                <td><input type="text" name="zgtype"></td>
                <td>毕业学校</td>
                <td><input type="text" name="schoolname"></td>
            </tr>
            <tr>
               
                <td>所属专业</td>
                <td>
                    <select name="profid">
                        <option>软件技术</option>
                    </select>
                </td>
                <td>考场号</td>
                <td>
                    <select name="erid">
                        <option>1504</option>
                    </select>
                </td>
                <td>考试场次</td>
                <td>
                    <select name="testtime">
                        <option value="1">上午</option>
                        <option value="2">下午</option>
                        <option value="3">晚上</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td>准考证号</td>
                <td><input type="text" name="examnumber" check="required num"></td>
                <td>身份证号</td>
                <td><input type="text" name="idcard" maxlength="18" check="required"></td>
                
            </tr>
            <tr>
                <td colspan="6" ><br></td>
            </tr>
            <tr>
                <td colspan="6" ><input type="submit" value="&nbsp;添 &nbsp;&nbsp;加&nbsp;"></td>
            </tr>
        </table>
          -->
        </form>
     </div>
    </div>
  </body>
</html>
