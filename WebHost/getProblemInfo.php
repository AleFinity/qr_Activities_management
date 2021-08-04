<?php
if (isset($_GET['number'])){ 
    $number = htmlentities($_GET['number']);

    include "connectDB.php";
    
    $sql = "SELECT 		`Repair`.`characteristicBreak`
            FROM `AssetRepair` 
    			JOIN `Repair`
                	ON `Repair`.id = `AssetRepair`.idRepair               
                WHERE `AssetRepair`.idAsset=$number";
    $todb = mysqli_fetch_array(mysqli_query($link,$sql))[0];
    
    $sql1 = "SELECT number FROM Placement WHERE id!=$todb";
    $todb = mysqli_query($link,$sql1);
    $mass = array();
    
    while($rws = mysqli_fetch_assoc($todb)){
       $mass[] = $rws['number'];
    }
    
    $json_inv_str = json_encode($mass);
    echo $json_inv_str;
}
else
    echo "1";
# 1 - логин и пароль верные
# 2 - ошибка входа
?>