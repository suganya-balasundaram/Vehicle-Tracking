<?php
if($_SERVER['REQUEST_METHOD']=='POST'){
$con=mysqli_connect("localhost","root","","earthrecycler");
$latitude=$_POST['latitude'];
$longitude=$_POST['longitude'];
$countryname=$_POST['countryname'];
$locality=$_POST['locality'];
$address=$_POST['address'];
$loginuser=$_POST['loginuser'];
$loginuserid=$_POST['loginuserid'];
$loginuseremail=$_POST['loginuseremail'];
$loginuserdesignation=$_POST['loginuserdesignation'];

$sql_u = "SELECT * FROM locationtracking WHERE loginuser = '$loginuser' OR loginuserid = '$loginuserid'";
  	
  	$res_u = mysqli_query($con, $sql_u);
  	

  	if (mysqli_num_rows($res_u) > 0) {
  	  mysqli_query($con,"UPDATE locationtracking set latitude='" . $_POST['latitude'] . "', longitude='" . $_POST['longitude'] . "', countryname='" . $_POST['countryname'] . "', locality='" . $_POST['locality'] . "' ,address='" . $_POST['address'] . "' WHERE loginuserid='" . $_POST['loginuserid'] . "'");
  	}
 
 
			else{		
 $sql="INSERT INTO `locationtracking`(`latitude`, `longitude`, `countryname`, `locality`, `address`, `loginuser`, `loginuserid`, `loginuseremail`, `loginuserdesignation`) VALUES ('".$latitude."','".$longitude."','".$countryname."','".$locality."','".$address."','".$loginuser."','".$loginuserid."','".$loginuseremail."','".$loginuserdesignation."')";
	if(mysqli_query($con,$sql)){
		echo("Yes");
	}else{
		echo("No");
	}
 }


}
 


?>


