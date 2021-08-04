<?php
if (isset($_GET['number'])){ 
    $number = htmlentities($_GET['number']);
    
    include "connectDB.php";
    
    $sql="UPDATE `Repair` SET `dateRepair`=CURRENT_DATE() WHERE `id`=(SELECT idRepair FROM `AssetRepair` WHERE `idAsset`=$number)";
    $result=mysqli_query($link, $sql);
    if (!$result)
        echo "1";
    else
        echo "23";
}
else
    echo "1";
?>