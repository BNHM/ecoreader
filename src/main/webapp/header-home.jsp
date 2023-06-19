<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE HTML>

<html>
<head>
    <title>MVZ EcoReader</title>

    <link rel="stylesheet" type="text/css" href="css/ecoreader.css" />

    <link rel="stylesheet" type="text/css" href="css/jquery-ui.css" />
    <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="css/bootstrap-combobox.css">
    <link rel="stylesheet" type="text/css" href="css/bootstrap-multiselect.css">
    <link rel="stylesheet" type="text/css" href="css/jquery.fancybox.css"/>
    <link rel="stylesheet" type="text/css" href="css/jquery.fancybox-thumbs.css"/>
    <link rel="stylesheet" type="text/css" href="css/alerts.css"/>

    <script type="text/javascript" src="js/jquery.js"></script>
    <script type="text/javascript" src="js/jquery-ui.min.js"></script>
    <script type="text/javascript" src="js/bootstrap.min.js"></script>
    <script type="text/javascript" src="js/bootstrap-combobox.js"></script>
    <script type="text/javascript" src="js/bootstrap-multiselect.js"></script>
    <script type="text/javascript" src="js/jquery.fancybox.pack.js"></script>
    <script type="text/javascript" src="js/jquery.fancybox-thumbs.js"></script>
    <script type="text/javascript" src="js/ecoreader.js"></script>
    <script>
      (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
      (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
      m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
      })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');

      ga('create', 'UA-83600134-1', 'auto');
      ga('send', 'pageview');

    </script>

<!-- Google tag (gtag.js) -->
<script async src="https://www.googletagmanager.com/gtag/js?id=G-25QQRRZ7FF"></script>
<script>
  window.dataLayer = window.dataLayer || [];
  function gtag(){dataLayer.push(arguments);}
  gtag('js', new Date());

  gtag('config', 'G-25QQRRZ7FF');
</script>
</head>


<body>
<div class="alert-container"><div id="alerts"></div></div>

<div id="main">
    <div id="header">
	  <div id="menubar">
		<div id="welcome">
	      <div id="title">EcoReader</div>
	    </div><!--close welcome-->

        <div id="menu_items">
	        <ul id="menu">
                <li class="current"></li>
                <li><a href="index.jsp">Search</a></li>
                <li><a href="about.jsp">About</a></li>
                <li><a href="https://mvzarchives.berkeley.edu/">Archives</a></li>
                <li><a href="https://calphotos.berkeley.edu/mvz.html" target="_blank">Photos</a></li>
            </ul>
        </div><!--close menu-->
      </div><!--close menubar-->
	</div><!--close header-->
