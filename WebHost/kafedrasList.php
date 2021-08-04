<?php
if (isset($_GET['number'])){ 
    $number = htmlentities($_GET['number']);

    include "connectDB.php";
    
    $sql = "SELECT id_kafedra FROM `AssetKafedra` WHERE id_asset=$number";
    $tod = mysqli_query($link,$sql);
    if(mysqli_num_rows($tod)<1){
        echo "1";
        return;
    }
    
    $todb = mysqli_fetch_array($tod)[0];
    $sql1 = "SELECT name FROM Kafedra WHERE id!=$todb";
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
# 1 - логин и пароль верные
# 2 - ошибка входа
?>
