<?php
if (isset($_GET['number'])){ 
    $number = htmlentities($_GET['number']);
    $newRoom = htmlentities($_GET['newRoom']);
    $state = htmlentities($_GET['state']);
    
    include "connectDB.php";

    $sql1="SELECT id FROM Placement WHERE number='$newRoom'";
    $search1=mysqli_query($link, $sql1);
    if(mysqli_num_rows($search1)==0){
        echo "6";
        return;
    }
    
    $sql="SELECT 1 FROM AssetPlacement WHERE id_asset=$number";
    $search=mysqli_query($link, $sql);
    
    $result1=mysqli_fetch_array($search1)[0];
    
    if(mysqli_num_rows($search)>0) 
        $sql=" UPDATE `AssetPlacement` SET `id_placement`=$result1 WHERE id_asset=$number";
    else
        $sql=" INSERT INTO `AssetPlacement`(`id_asset`, `id_placement`) VALUES ($number,$result1)";

    $sql1=" UPDATE `Asset` SET `state`=$state WHERE id=$number";
    $result=mysqli_query($link, $sql);
    $result1=mysqli_query($link, $sql1);
    if (!$result || !$result1)
        echo "1";
    else
        echo "22";
}
else
    echo "1";
?>