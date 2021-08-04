<?php
if (isset($_GET['number'])){ 
    $number = htmlentities($_GET['number']);

    include "connectDB.php";
    
    $sql1 = "SELECT name FROM Employee WHERE id_kafedra=(SELECT id_kafedra FROM `AssetKafedra` WHERE id_asset=$number)";
    $todb = mysqli_query($link,$sql1);
    $mass = array();
    
    while($rws = mysqli_fetch_assoc($todb)){
       $mass[] = $rws['name'];
    }
    
    $json_inv_str = json_encode($mass);
    echo $json_inv_str;
}
else
    echo "1";
?>