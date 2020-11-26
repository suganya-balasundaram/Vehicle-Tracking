<?php
 
$host='localhost';  //sql104.byethost12.com  
 
$uname='root';
 
$pwd='';
 
$db="earthrecycler";

$con = mysqli_connect($host,$uname,$pwd,$db);
// Check connection
if (!$con) {
    die("Connection failed: " . mysqli_connect_error());
}
 
 
$query=mysqli_query($con, "select * from locationtracking");  //fetch all data from Location table
 
while($row=mysqli_fetch_array($query))
{
	$flag[]=$row;
}
 echo json_encode(array('FL' => $flag));  //json output

mysqli_close($con);
?>