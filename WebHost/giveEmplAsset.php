<?php
if (isset($_GET['number']) && isset($_GET['fio'])){
    $number = htmlentities($_GET['number']);
    $fio = htmlentities($_GET['fio']);

    include "connectDB.php";
    
    $sql="SELECT 1 FROM Employee WHERE name='$fio'";
    $search=mysqli_query($link, $sql);
    if(mysqli_num_rows($search)>0){
        $sql1="
            INSERT INTO `GiveAssetTemporarily`(`id_asset`, `id_nameEmployee`, `dateGive`) VALUES (
            $number,(SELECT id FROM Employee WHERE name='$fio'),CURRENT_DATE())";
        $sql2="UPDATE `Asset` SET `state`=4 WHERE `id`=$number";
        
        $result1=mysqli_query($link, $sql1);
        $result2=mysqli_query($link, $sql2);
        
        if (!$result1 || !$result2)
            echo "1";
        else
            echo "16";
    }
    else
        echo "4";
}
else
    echo "1";
?>