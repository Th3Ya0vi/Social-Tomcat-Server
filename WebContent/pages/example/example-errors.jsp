<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
	<head>
 
		<style type="text/css">
		.errors {
			background-color:#FFCCCC;
			border:1px solid #CC0000;
			width:400px;
			margin-bottom:8px;
		}
		.errors li{ 
			list-style: none; 
		}
		</style>
 
	</head>
	<body>
	 	<s:text name="current.version"/>
		 <s:if test="hasActionErrors()">
		   <div class="errors">
		      <s:actionerror/>
		   </div>
		</s:if>
	</body>
</html>