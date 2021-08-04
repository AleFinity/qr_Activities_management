<?php

include "connectDB.php";

$sql1 = "SELECT number FROM Placement";
$todb = mysqli_query($link,$sql1) or die("1");
$mass = array();

while($rws = mysqli_fetch_assoc($todb)){
   $mass[] = $rws['number'];
}

$json_inv_str = json_encode($mass);
echo $json_inv_str;
?>